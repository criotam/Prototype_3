import RPi.GPIO as GPIO
import time
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(17,GPIO.IN)
GPIO.setup(27,GPIO.IN)
i=0
j=0
while True:
	if(GPIO.input(17)==0):
		i+=1
		time.sleep(0.3)
		print(str(i)+"rotation completed in 1st wheel")
	if(GPIO.input(27)==0):
		time.sleep(0.3)
		j+=1
		print(str(j)+"rotation completed in 2nd wheel")
GPIO.cleanup()

