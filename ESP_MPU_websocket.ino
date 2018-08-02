#include <ESP8266WiFi.h>
#include <WebSocketClient.h>

const char* ssid     = "criotam";            /*Enter hotspot name*/
const char* password = "Panasonic@2018";             /*Enter Password*/
char path[] = "/BotControllerGateway/bot_gateway";
char host[] = "192.168.1.10";
  
WebSocketClient webSocketClient;

// Use WiFiClient class to create TCP connections
WiFiClient client;

const int BUTTON=2;
const int LED=0  ;
int BUTTONState=0;
String macId;

void setup() {
  
  pinMode(0, OUTPUT);
  pinMode(2,INPUT);
  
  Serial.begin(115200);
  Serial.println("SETUP");
  delay(10);

  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
     delay(250);
    digitalWrite(LED,LOW);
    delay(250);
    Serial.print(".");
    digitalWrite(LED,HIGH);
  }
digitalWrite(LED,HIGH);
delay(500);
digitalWrite(LED,LOW);
  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  delay(5000);


  // Connect to the websocket server
  if (client.connect(host, 8080)) {
    Serial.println("Connected");
    digitalWrite(LED,HIGH);
  } else {
    Serial.println("Connection failed.");
    while(1) {
      // Hang on failure
    }
  }

  // Handshake with the server
  webSocketClient.path = path;
  webSocketClient.host = host;
  if (webSocketClient.handshake(client)) {
    Serial.println("Handshake successful");
    
    macId = WiFi.macAddress();
    Serial.println(macId);
    webSocketClient.sendData("identifier_exp2fp:mac_id:"+macId);
  } else {
    Serial.println("Handshake failed.");
    while(1) {
      // Hang on failure
    }  
  }


}

union cvt{
  byte b[4];
  float f;
  };

  float us1;
  float us2;
  float mpu1;
  
void loop() {

  cvt c;

  BUTTONState=digitalRead(BUTTON);
  if(BUTTONState==HIGH)
  {
  //digitalWrite(LED,HIGH);
  }
  else
  {
  Serial.println("OFF");
  setup();
  //digitalWrite(LED,LOW);
  }
  
  if (client.connected()) {
    if(Serial.available()){
    char inByte = Serial.read();
    Serial.println(inByte);
    if(inByte=='f'){
      Serial.read();
      byte inData[4];
  
    Serial.readBytes(c.b,4);
    us1 = c.f;
    if(c.f > 500)
    {
      c.f = 45;
    }
    Serial.print(c.f);
    Serial.print(":");
    Serial.readBytes(c.b,4);
    us2 = c.f;
    if(c.f > 500)
    {
      c.f = 45;
    }
    Serial.print(c.f);
    Serial.print(":");
    Serial.readBytes(c.b,4);
    mpu1 = c.f;
    Serial.println(c.f);
    
    webSocketClient.sendData("raw_sensor#"+String(us1)+":"+String(us2)+":"+String(mpu1));
    }
   }

    String data;
    webSocketClient.getData(data);
    if (data.length() > 0) {
      Serial.print("Received data: ");
      Serial.println(data);
      webSocketClient.sendData("Message received");
    }  
    
  }
  // wait to fully let the client disconnect
//  delay(10);
}
