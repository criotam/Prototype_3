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
# set up GPIO pins
GPIO.setup(motor1_for, GPIO.OUT) # Connected to PWMA
GPIO.setup(motor1_back, GPIO.OUT) # Connected to AIN2
GPIO.setup(motor2_for, GPIO.OUT) # Connected to AIN1
GPIO.setup(motor2_back, GPIO.OUT) # Connected to STBY
GPIO.setup(motor3_for, GPIO.OUT) # Connected to BIN1
GPIO.setup(motor3_back, GPIO.OUT) # Connected to BIN2
GPIO.setup(motor4_for, GPIO.OUT) # Connected to PWMB
GPIO.setup(motor4_back, GPIO.OUT)

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
while True:
    move_forward()
    time.sleep(10)
    move_backward()
    time.sleep(10)
    turn_left()
    time.sleep(5)
    turn_right()
    time.sleep(5)
    stop()
    time.sleep(5)
GPIO.cleanup()

