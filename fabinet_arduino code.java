#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <string.h>

char command[10];
const char* ssid1 = "LeeSpot";
const char* password1 = "10239160";
//const char* ssid1 = "Galaxy s10+";
//const char* password1 = "19970719";
//const char* ssid1 = "1011-2.4G";
//const char* password1 = "kpu123456!";
const char* mqttServer = "3.34.255.198";
const int   mqttPort = 1883;
int interval = 2500;
char A1[] = "A-1-1";
char A2[] = "A-1-2";
char A3[] = "B-1-1";
char A4[] = "B-1-2";
bool flag1 = false;
bool flag2 = false;
bool flag3 = false;
bool flag4 = false;

WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
  Serial.begin(115200);
  pinMode(2, INPUT); // 입력으로 설정합니다
  pinMode(2, INPUT_PULLUP); // 풀업 ON
  pinMode(16, OUTPUT);
  pinMode(12, INPUT); // 입력으로 설정합니다
  pinMode(12, INPUT_PULLUP); // 풀업 ON
  pinMode(5, OUTPUT);
  pinMode(14, INPUT); // 입력으로 설정합니다
  pinMode(14, INPUT_PULLUP); // 풀업 ON
  pinMode(0, OUTPUT);
  pinMode(4, INPUT); // 입력으로 설정합니다
  pinMode(4, INPUT_PULLUP); // 풀업 ON
  pinMode(13, OUTPUT);
  //randomSeed(analogRead(0));
  WiFi.begin(ssid1, password1);
  
  while (WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.println("Connected to the WiFi network");

  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);

  while(!client.connected()){
    Serial.println("Connecting to MQTT...");

    if(client.connect("ESP8266Client")){
      Serial.println("connected");
    }else{
      Serial.print("failed with state "); Serial.println(client.state());
      delay(2000);
    }
  }
  if(client.subscribe("heum/username2")) {
    Serial.println("Subscription Valid !");
  }
}

void loop() {
  client.loop();
  delay(50);
  if (digitalRead(2) == LOW && flag1 == false) {  //문 닫힌상태고 flag가 false일때
    //문 잠그기
    digitalWrite(16,LOW);
  }
  if (digitalRead(2) == HIGH) {  //문열리면
    flag1 = false;
  }
  if (digitalRead(12) == LOW && flag2 == false) {  
    digitalWrite(5,LOW);
  }
  if (digitalRead(12) == HIGH) {  
    flag2 = false;
  }
  if (digitalRead(14) == LOW && flag3 == false) {  
    digitalWrite(0,LOW);
  }
  if (digitalRead(14) == HIGH) {  
    flag3 = false;
  }
  if (digitalRead(4) == LOW && flag4 == false) {  
    digitalWrite(13,LOW);
  }
  if (digitalRead(4) == HIGH) {  
    flag4 = false;
  }
}

void callback(char* topic, byte* payload, unsigned int length){
  Serial.print("Message arrived in topic: ");
  Serial.println(topic);

  Serial.print("Message:");
  for (int i=0 ; i<length ; i++){
    Serial.print((char)payload[i]);
    command[i] = (char)payload[i];
  } 
  Serial.println("\n--------------------");

  //int num = atoi(command);
   if(!strcmp(command,A1)){
    digitalWrite(16, HIGH);
    Serial.println("HIGH");
    flag1 = true;
   }
   if(!strcmp(command,A2)){
    digitalWrite(5, HIGH);
    Serial.println("HIGH");
    flag2 = true;
   }
   if(!strcmp(command,A3)){
    digitalWrite(0, HIGH);
    Serial.println("HIGH");
    flag3 = true;
   }
   if(!strcmp(command,A4)){
    digitalWrite(13, HIGH);
    Serial.println("HIGH");
    flag4 = true;
   }
}