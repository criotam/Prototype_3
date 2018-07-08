import websocket
import time
import serial
import RPi.GPIO as GPIO

initAngle = 0.0

ser = serial.Serial(
port='/dev/ttyACM0',
baudrate=9600,
parity=serial.PARITY_NONE,
stopbits=serial.STOPBITS_ONE,
bytesize=serial.EIGHTBITS,
timeout=1
)
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
motor1_for = 21
motor1_back = 20
motor2_for = 16
motor2_back = 26
motor3_for = 6
motor3_back = 5
motor4_for = 25
motor4_back = 24
ir1 = 17
# set up GPIO pins
GPIO.setup(motor1_for, GPIO.OUT) # Connected to PWMA
GPIO.setup(motor1_back, GPIO.OUT) # Connected to AIN2
GPIO.setup(motor2_for, GPIO.OUT) # Connected to AIN1
GPIO.setup(motor2_back, GPIO.OUT) # Connected to STBY
GPIO.setup(motor3_for, GPIO.OUT) # Connected to BIN1
GPIO.setup(motor3_back, GPIO.OUT) # Connected to BIN2
GPIO.setup(motor4_for, GPIO.OUT) # Connected to PWMB
GPIO.setup(motor4_back, GPIO.OUT)
GPIO.setup(ir1,GPIO.IN)

#move forward
def move_forward():
    GPIO.output(motor1_for, GPIO.HIGH)
    GPIO.output(motor1_back, GPIO.LOW)
    GPIO.output(motor2_for, GPIO.HIGH)
    GPIO.output(motor2_back, GPIO.LOW)
    GPIO.output(motor3_for, GPIO.HIGH)
    GPIO.output(motor3_back, GPIO.LOW)
    GPIO.output(motor4_for, GPIO.HIGH)
    GPIO.output(motor4_back, GPIO.LOW)
#time.sleep(10)
#move backward
def move_backward():
    GPIO.output(motor1_for, GPIO.LOW)
    GPIO.output(motor1_back, GPIO.HIGH)
    GPIO.output(motor2_for, GPIO.LOW)
    GPIO.output(motor2_back, GPIO.HIGH)
    GPIO.output(motor3_for, GPIO.LOW)
    GPIO.output(motor3_back, GPIO.HIGH)
    GPIO.output(motor4_for, GPIO.LOW)
    GPIO.output(motor4_back, GPIO.HIGH)
#time.sleep(10)
#turn left
def turn_left():
    GPIO.output(motor1_for, GPIO.LOW)
    GPIO.output(motor1_back, GPIO.HIGH)
    GPIO.output(motor2_for, GPIO.HIGH)
    GPIO.output(motor2_back, GPIO.LOW)
    GPIO.output(motor3_for, GPIO.LOW)
    GPIO.output(motor3_back, GPIO.HIGH)
    GPIO.output(motor4_for, GPIO.HIGH)
    GPIO.output(motor4_back, GPIO.LOW)
#time.sleep(5)
#turn right
def turn_right():
    GPIO.output(motor1_for, GPIO.HIGH)
    GPIO.output(motor1_back, GPIO.LOW)
    GPIO.output(motor2_for, GPIO.LOW)
    GPIO.output(motor2_back, GPIO.HIGH)
    GPIO.output(motor3_for, GPIO.HIGH)
    GPIO.output(motor3_back, GPIO.LOW)
    GPIO.output(motor4_for, GPIO.LOW)
    GPIO.output(motor4_back, GPIO.HIGH)
#time.sleep(5)
#stop
def stop():
    GPIO.output(motor1_for, GPIO.LOW)
    GPIO.output(motor1_back, GPIO.LOW)
    GPIO.output(motor2_for, GPIO.LOW)
    GPIO.output(motor2_back, GPIO.LOW)
    GPIO.output(motor3_for, GPIO.LOW)
    GPIO.output(motor3_back, GPIO.LOW)
    GPIO.output(motor4_for, GPIO.LOW)
    GPIO.output(motor4_back, GPIO.LOW)


def on_message(ws, message):
    print(message)
    find_command(message)

def on_error(ws, error):
    print(error)

def on_close(ws):
    print ("### closed ###")

def on_open(ws):
    print ("connection opened")
    ws.send("fork_lift")

def find_command(message):
    dataArray = message.split('@')
    print(dataArray)
    length = len(dataArray)
    print(length)
    i=0
    for i in range(length):
	command = dataArray[i]
	command=command.split(':')
	if (command[0]=='turn'):
	    turn_command(command[1])
        elif(command[0]=='move'):
	    move_command(command[1])

def turn_command(command):
    print('turn command')
    global initAngle
    finalAngle = float(command)
    stop()
    time.sleep(5)
    i=0
    while i<7:
        print(i)
        i+=1
        serString = ser.readline()
        print(serString)
    try:
        print(serString)
        initAngle = float(serString)
    except ValueError:
        print("value error")
    print('staRT')
    diff_angle = initAngle - finalAngle
    if(diff_angle>0):
        while (initAngle>finalAngle):
    	    turn_right()
    	    serString = ser.readline()
    	    print(serString)
	    try:
    	       serString = float(serString)
    	       initAngle = serString
 	    except ValueError:
               print("value error")
    	print('stopped')
        stop()
    elif(diff_angle<0):
    	while (initAngle<finalAngle):
            turn_left()
            serString = ser.readline()
	    print(serString)
            try:
	       serString = float(serString)
	       initAngle = serString
            except ValueError:
               print("value error")
    	print('stopped')
        stop()
    else:
    	stop()
    print(initAngle)
    ws.send('rotation:'+str(initAngle))
    time.sleep(5)


def move_command(command):
    print('move command')
    global initAngle
    final_distance = float(command) * 100
    print(str(final_distance))
    previous_state1 = 1
    current_state1 =  1
    rot = 2*3.14*(3.5)
    dist = rot
    total_time=0.0

    starttime1 = time.time()
    move_forward()
    speed = 0.0
    i=1
    initial_distance = 0
    while(initial_distance<final_distance):
        #print("true contdition")
        #print("Waiting for one rotation  to complete")
        current_state1 = GPIO.input(ir1)
        #print("state"+str(current_state1)+":"+str(previous_state1))
	if(previous_state1==1 and current_state1==0 ):
    	    if(i==1):
        	stoptime1 = time.time()
            	time_taken = stoptime1-starttime1
	    	starttime2 = time.time()
	    	print(starttime2)
                i=2
            elif(i==2):
            	stoptime2 = time.time()
            	print(stoptime2)
            	time_taken2 = stoptime2-starttime2
                print("time taken2"+str(time_taken2))
                if(time_taken2>0.5):
                    i=3
            	    speed=rot/time_taken2
            	    print(str(speed)+"cm/s")
            	    dist2 = time_taken*speed
            	    dist1 = dist2+speed*time_taken2
            	    print("distance covered "+str(dist2)+"first strip detected")
            	    print("distance covered "+str(dist1)+"2nd rotation completed")
            	    dist=dist1
		    ws.send('distance:'+str(dist)+'@'+'rotation:'+str(initAngle))
            elif(i==3):
	    	while True:
        	    distance2 = final_distance - dist
                    print("distance and speed"+str(distance2)+":"+str(speed))
        	    time2 = distance2/speed
		    time3=time2/4
                    print("time3"+str(time3))
		    time.sleep(time3)
		    dist3 = speed*time3
		    dist4 = dist + dist3
		    ws.send('distance:'+str(dist3)+'@'+'rotation:'+str(initAngle))
		    time.sleep(time3)
		    dist4 = dist4+dist3
		    ws.send('distance:'+str(dist3)+'@'+'rotation:'+str(initAngle))
		    time.sleep(time3)
		    dist4 = dist4+dist3
		    ws.send('distance:'+str(dist3)+'@'+'rotation:'+str(initAngle))
		    time.sleep(time3)
		    dist4 = dist4+dist3
		    ws.send('distance:'+str(dist3)+'@'+'rotation:'+str(initAngle))
		    current_time = time.time()
		    print(current_time-starttime1)
        	    stop()
                    initial_distance = final_distance;
        	    break
    	previous_state1 = current_state1


if __name__ == "__main__":
    websocket.enableTrace(True)
    ws = websocket.WebSocketApp("ws://192.168.1.4:8080/BotControllerGateway/bot_gateway",
                              on_message = on_message,
                              on_error = on_error,
                              on_close = on_close)
    ws.on_open = on_open
    ws.run_forever()
