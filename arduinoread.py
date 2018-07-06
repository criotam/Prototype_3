import serial
ser = serial.Serial('COM3',9600)
while True:
    serString = ser.readline()
    dataArray = serString.decode("utf8").split(',')
    if(len(dataArray)==3):
        right = dataArray[0]
        left = dataArray[1]
        back = dataArray[2]
        print("right sensor="+str(right))
        print("left sensor="+str(left))
        print("back sensor="+str(back))
    if(len(dataArray)==2):
        right = dataArray[0]
        left = dataArray[1]
        if(right<20 or left<20):
            #stop as obstacle is detected
