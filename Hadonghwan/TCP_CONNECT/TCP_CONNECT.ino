#include <ESP8266WiFi.h>

#ifndef STASSID
#define STASSID "olleh_WiFi_EFF1"    //  와이파이 이름
#define STAPSK  "0000007898"  //와이파이 비밀번호
#endif

/*와이파이*/
const char* ssid     = STASSID;
const char* password = STAPSK;

/*TCP 서버 포트*/
const int tcpPort = 22485;    //  서버로 사용될 포트(지금 예시에서는 22485를 사용)
WiFiServer wifiServer(tcpPort);
void setup() {
  /*시리얼 모니터 보드레이트*/
  Serial.begin(115200);

  /*WiFi setup*/
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  /*와이파이 연결될 때까지 반복*/
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println();
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  wifiServer.begin();
  Serial.println("Server On");
  Serial.print("Port: ");
  Serial.println(tcpPort);
  pinMode(13,OUTPUT);
}

String str ="";
String on = "ON";
String off = "OFF";
void loop() {
  boolean type = true;
  WiFiClient client = wifiServer.available();                    
  digitalWrite(13, LOW);   
  if (client) {
    Serial.println("Client connected");

    while (client.connected()) {
      while (client.available() > 0) {
        
        char c = client.read();        
        Serial.write(c);
        if(c == '\n'){
          type != type;
        }else{
          type = true;          
        }
  
        if(type){                      
          str += c;
          if(str.equals(on)){
            delay(1000);
            digitalWrite(13, HIGH);
            Serial.println(str);
            str = "";
          }else if(str.equals(off)){
            delay(1000);           
            digitalWrite(13, LOW);
            Serial.println(str);
            str = "";
          }
        }        
       }
       str = "";        
       delay(10);
      }    
      client.stop();
      Serial.println("Client disconnected");
   }
}
