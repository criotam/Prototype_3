

/* Interfacing 5 Ultrasonic HC-SR04 sensors for obstacle avoidance and postion mapping.
 * At the bottup time of the bot, only 3 sensors (the two side facing sensors and the sensor at the back of the bot) will be switched on for position mapping of the bot.
 * After 2 mins, the two front facing sensors are turned on while the remaining are turned off.
 * This is for detecting obstacles. 
 */

 
int trigPin1=2;
int echoPin1=3;

int trigPin2=4;
int echoPin2=5;

int trigPin3=6;
int echoPin3=7;

int trigPin4=8;
int echoPin4=9;

int trigPin5=10;
int echoPin5=11;

unsigned long start_time;
unsigned long cur_time;
const unsigned long period = 10000;
boolean flag = true;
int count = 0;

void setup() {

  start_time = millis();
  
  Serial.begin (9600);
  pinMode(trigPin1, OUTPUT);
  pinMode(echoPin1, INPUT);
   pinMode(trigPin2, OUTPUT);
  pinMode(echoPin2, INPUT);
   pinMode(trigPin3, OUTPUT);
  pinMode(echoPin3, INPUT);
   pinMode(trigPin4, OUTPUT);
  pinMode(echoPin4, INPUT);
   pinMode(trigPin5, OUTPUT);
  pinMode(echoPin5, INPUT);

}

void loop() {


 if(flag)
 { 
   start_time = millis();
   flag = false;
 }

 cur_time = millis();
 if(cur_time - start_time > 130000)
  {
    long duration1, front_right_dist;   // Distance from front-right sensor
    digitalWrite(trigPin1, LOW);  // Added this line
    delayMicroseconds(2); // Added this line
    digitalWrite(trigPin1, HIGH);
    delayMicroseconds(10); // Added this line
    digitalWrite(trigPin1, LOW);
    duration1 = pulseIn(echoPin1, HIGH);
    front_right_dist = (duration1/2) / 29.1;

    if (front_right_dist >= 500 || front_right_dist <= 0){
      Serial.println("Out of range");
    }
    else {
      Serial.print ( "Front-Right Sensor:  ");
      Serial.print (front_right_dist);
      Serial.println("cm");
    }
    delay(200);
    
    long duration2, front_left_dist;  // Distance from front-left sensor
    digitalWrite(trigPin2, LOW);  // Added this line
    delayMicroseconds(2); // Added this line
    digitalWrite(trigPin2, HIGH);
    delayMicroseconds(10); // Added this line
    digitalWrite(trigPin2, LOW);
    duration2 = pulseIn(echoPin2, HIGH);
    front_left_dist= (duration2/2) / 29.1;

    if (front_left_dist >= 500 || front_left_dist <= 0){
      Serial.println("Out of range");
    }
    else {
      Serial.print("Front-Left Sensor:  ");
      Serial.print(front_left_dist);
      Serial.println("cm");
    }
    delay(200);    

  Serial.println("-------------------------------------------------");
  Serial.println(" ");
  }  
  else
  {
    long duration3, right_dist;  // Distance from side-right sensor
    digitalWrite(trigPin3, LOW);  // Added this line
    delayMicroseconds(2); // Added this line
    digitalWrite(trigPin3, HIGH);
    delayMicroseconds(10); // Added this line
    digitalWrite(trigPin3, LOW);
    duration3 = pulseIn(echoPin3, HIGH);
    right_dist = (duration3/2) / 29.1;
  
     if (right_dist >= 500 || right_dist <= 0){
      Serial.println("Out of range");
    }
    else {
      Serial.print("Right Sensor:  ");
      Serial.print(right_dist);
      Serial.println("cm");
    }
    delay(200);
  
    long duration4, left_dist;   // Distance from side-left sensor
    digitalWrite(trigPin4, LOW);  // Added this line
    delayMicroseconds(2); // Added this line
    digitalWrite(trigPin4, HIGH);
    delayMicroseconds(10); // Added this line
    digitalWrite(trigPin4, LOW);
    duration4 = pulseIn(echoPin4, HIGH);
    left_dist = (duration4/2) / 29.1;
  
     if (left_dist >= 500 || left_dist <= 0){
      Serial.println("Out of range");
    }
    else {
      Serial.print("Left Sensor:  ");
      Serial.print(left_dist);
      Serial.println("cm");
    }
    delay(200);
   
    long duration5, back_dist;  // Distance from back sensor
    digitalWrite(trigPin5, LOW);  // Added this line
    delayMicroseconds(2); // Added this line
    digitalWrite(trigPin5, HIGH);
    delayMicroseconds(10); // Added this line
    digitalWrite(trigPin5, LOW);
    duration5 = pulseIn(echoPin5, HIGH);
    back_dist = (duration5/2) / 29.1;
  
    if (back_dist >= 500 || back_dist <= 0){
      Serial.println("Out of range");
    }
    else {
      Serial.print("Back Sensor:  ");
      Serial.print(back_dist);
      Serial.println("cm");
    }
    delay(200);
    
    Serial.println("---------------------------------------------");
    Serial.println(" ");
   } 
}
