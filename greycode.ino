int enablePin = 10;

int in1Pin = 9;

int in2Pin = 8;

int enable2Pin = 6;

int in3Pin = 5;

int in4Pin = 4;

int statusPin= 13;

//left
int outputApin1 = 2;
int outputBpin1 = 11;

//right
int outputApin2 = 3;
int outputBpin2 = 12;

long leftCount = 0;
long rightCount = 0;

int prevLeftState = 0;
int prevRightState = 0;

int curLeftState = 0;
int curRightState = 0;

int CENTER_RPM = 180;

double CURRENT_LEFT_RPM = 180.0;

double CURRENT_RIGHT_RPM = 180.0;

double DELTA = 5;

void setup()
{

Serial.begin(9600);

pinMode(in1Pin, OUTPUT);

pinMode(in2Pin, OUTPUT);

pinMode(in3Pin, OUTPUT);

pinMode(in4Pin, OUTPUT);

pinMode(enablePin, OUTPUT);

pinMode(outputApin1,INPUT_PULLUP);

pinMode(outputBpin1,INPUT);

pinMode(outputApin2,INPUT_PULLUP);

pinMode(outputBpin2,INPUT);

//attachInterrupt(0, left, RISING);

//attachInterrupt(1, right, RISING);

prevLeftState = digitalRead(outputApin1);
prevRightState = digitalRead(outputApin2);

//digitalWrite(13,HIGH);
int speed = 250;
boolean reverse = true;
moveForward(CENTER_RPM, CENTER_RPM, reverse);
}

//1->left
//2->right
int curRightVal = 0;
int curLeftVal = 0;
int prevRightVal = 0;
int prevLeftVal = 0;

long prevRecTime = 0;
long curRecTime = 0;

void loop(){

curRecTime = millis();

if(curLeftVal > prevLeftVal){  leftCount++; prevLeftVal = curLeftVal; } //clockwise
else if(curLeftVal < prevLeftVal){ leftCount--; prevLeftVal = curLeftVal; } //counter clockwise

if(curRightVal < prevRightVal){  rightCount++; prevRightVal = curRightVal; } //clockwise
else if(curRightVal > prevRightVal){ rightCount--; prevRightVal = curRightVal; } //counter clockwise

if(digitalRead(outputApin1) == 0 && digitalRead(outputBpin1) == 0){
  curLeftVal = 0;
}else if(digitalRead(outputApin1) == 1 && digitalRead(outputBpin1) == 0){
  curLeftVal = 1;
}else if(digitalRead(outputApin1) == 1 && digitalRead(outputBpin1) == 1){
  curLeftVal = 3;
}else if(digitalRead(outputApin1) == 1 && digitalRead(outputBpin1) == 0){
  curLeftVal = 2;
}

if(digitalRead(outputApin2) == 0 && digitalRead(outputBpin2) == 0){
  curRightVal = 0;
}else if(digitalRead(outputApin2) == 1 && digitalRead(outputBpin2) == 0){
  curRightVal = 1;
}else if(digitalRead(outputApin2) == 1 && digitalRead(outputBpin2) == 1){
  curRightVal = 3;
}else if(digitalRead(outputApin2) == 1 && digitalRead(outputBpin2) == 0){
  curRightVal = 2;
}

Serial.println(String(curLeftVal) + "\t" + String(curRightVal) + "\t" + String(leftCount)+"\t"+String(rightCount)+"\t"+String(CURRENT_LEFT_RPM)+"\t"+String(CURRENT_RIGHT_RPM));

if(curRecTime - prevRecTime > 2000){
  //Serial.println(String(leftCount)+"\t"+String(rightCount)+"\t"+String(CURRENT_LEFT_RPM)+"\t"+String(CURRENT_RIGHT_RPM));
  
  pidFunction(leftCount, rightCount);
  prevRecTime = curRecTime;
}

}


void pidFunction(long leftCounter, long rightCounter){

  if(leftCounter-rightCounter>5){

    digitalWrite(13,HIGH);
    
    CURRENT_RIGHT_RPM+=DELTA;
  
    CURRENT_LEFT_RPM-=DELTA;
  
    moveForward(CURRENT_RIGHT_RPM, CURRENT_LEFT_RPM, true);
  }
  //fast rigt wheel
  else if(rightCounter-leftCounter>5){

    digitalWrite(13,LOW);
    
    CURRENT_RIGHT_RPM-=DELTA;
  
    CURRENT_LEFT_RPM+=DELTA;
  
    moveForward(CURRENT_RIGHT_RPM, CURRENT_LEFT_RPM, true);
  }


  if (CURRENT_RIGHT_RPM>255){
       CURRENT_RIGHT_RPM = 255;
       analogWrite(enablePin,255);
  }
  else if(CURRENT_RIGHT_RPM<0){
       CURRENT_RIGHT_RPM = 0;
       analogWrite(enablePin,0);
  }

  if (CURRENT_LEFT_RPM>255){
       CURRENT_LEFT_RPM = 255;
       analogWrite(enable2Pin,255);
  }
  else if(CURRENT_LEFT_RPM<0){
       CURRENT_LEFT_RPM = 0;
       analogWrite(enable2Pin,0);
  }
     
  //Serial.println(String(CURRENT_LEFT_RPM)+"\t"+String(CURRENT_RIGHT_RPM));
  
}


void moveForward(int speed_right, int speed_left, boolean reverse){

analogWrite(enable2Pin, speed_left);

digitalWrite(in1Pin, ! reverse);

digitalWrite(in2Pin, reverse);

analogWrite(enablePin, speed_right);

digitalWrite(in3Pin, ! reverse);

digitalWrite(in4Pin, reverse);
}
