import time
import RPi.GPIO as GPIO

# Declare the GPIO settings
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
ir2 = 27
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
GPIO.setup(ir2,GPIO.IN)
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
#time.sleep(5)

i=0
j=0
stop()
time.sleep(10)
flag=0
previous_state1 = 1
previous_state2 = 1
rot = 2*3.14*3.5
dist = rot
total_time=0.0
starttime1 = time.time()
move_forward()
while True:
    current_state1 = GPIO.input(ir1)
    current_state2 = GPIO.input(ir2)
    if(previous_state1==0 and current_state1==1 ):
        i+=1
        if(i==1):
            stoptime1 = time.time()
            time_taken = stoptime1-starttime1
	    starttime2 = time.time()
	    print(starttime2)
        if(i==2):
            stoptime2 = time.time()
            print(stoptime2)
            time_taken2 = stoptime2-starttime2
            speed = rot/time_taken2
            print(str(speed)+"cm/s")
            dist2 = time_taken*speed
            dist1 = dist2+speed*time_taken2
            print("distance covered "+str(dist2)+"first strip detected")
            print("distance covered "+str(dist1)+"2nd rotation completed")
            dist=dist1
        if(i>=3):
            
            dist += rot
            #print(str(i)+" rotation completed in 1st wheel")
            print("Distance covered ="+str(dist)+"cm, number of rotation ="+str(i))
    #if(previous_state2==0 and current_state2==1):
	#j+=1
	#print(str(j)+" rotation completed in 2nd wheel")
    
    #previous_state2 = current_state2
        if(i==6):
            #dist += rot
            #print("Distance covered ="+str(dist)+"cm")
            stoptime3 = time.time()
            total_time = stoptime3 - starttime1
            print("total time taken = "+str(total_time))
            stop()
            time.sleep(20)
    previous_state1 = current_state1
    
GPIO.cleanup()
