#include <Bridge.h>
#include <BridgeHttpClient.h>

BridgeHttpClient client1;
BridgeHttpClient client2;
String cookie;

void setup() {
  Bridge.begin(); // Initialize Bridge
  Serial.begin(9600);
  while (!Serial); // wait for a serial connection
}

String login(){
  //login
  client1.addHeader("Accept: application/json");
  client1.addHeader("Content-Type: application/json");
  client1.postAsync("http://192.168.0.101:5000/login", 
  "{\"username\":\"example1@gmail.com\", \"password\":\"test1234\"}");
  Serial.print("Sending request");
  
  while(1){
    if (client1.finished()) {
      Serial.println();
      Serial.println("Response Body:");
      while (client1.available() > 0) {
        char c = client1.read();
        Serial.print(c);
      }
      Serial.println();
      Serial.print("Response Code: ");
      Serial.println(client1.getResponseCode());
      
      String temp = client1.getResponseHeaders();
      
      int start = temp.indexOf("NEURONIOSESSIONID=");
      int end_at = temp.indexOf("Date:");
      
      //Extract cookie
      return temp.substring(start, end_at);
      
      } else {
      // not finished yet, wait and retry
        Serial.print(".");
        delay(500);
      } 
  }
}

void sendMsg(){
  //message send
  String temp = "Cookie: "+cookie;
  char header[temp.length()];
  temp.toCharArray(header, temp.length());
  
  client2.addHeader("Accept: */*");
  client2.addHeader("Content-Type: application/x-www-form-urlencoded");
  client2.addHeader(header);

  
  client2.postAsync("http://192.168.0.101:5000/NeuronioExample/HTTPExample/NEURONIOHTTPExampleProtocol", 
  "submitType=submit&targetOid=T000000074&stx=<&oid=T000000074&msg=Hello, World&etx=>");
  Serial.print("Sending request");

  while(1){
    if (client1.finished()) {
      Serial.println();
      Serial.println("Response Body:");
      while (client2.available() > 0) {
        char c = client2.read();
        Serial.print(c);
      }
      
      Serial.print("Response Code: ");
      Serial.println(client2.getResponseCode());

      return;      
      } else {
      // not finished yet, wait and retry
        Serial.print(".");
        delay(500);
      }
  }
}

void loop() {
  cookie = login();

  //After login, set the cookie in the header to call the url.
  sendMsg();
  
  while(1); //stop
}
