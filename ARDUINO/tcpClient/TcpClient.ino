#include <BridgeClient.h>
#include <Bridge.h>

BridgeClient client;
const byte SERVER_ADDR[] = {192, 168, 0, 101};
const int SERVER_PORT = 5000;

const uint8_t sendProtocol[] = "<T000000081Hello, World>";
const uint8_t responseProtocol[] = "<T000000082Hello, World123456>";


void setup() {
  Serial.begin(9600);  
}

void loop() {  
  Bridge.begin();

  //You can send packets according to the protocol.
  
  client.connect(SERVER_ADDR, SERVER_PORT);
  Serial.println("Server connect_");
  client.write(sendProtocol, 24);
  while (client.available() > 0) {
    char c = client.read();
    Serial.print(c);
  }
  
  client.write(responseProtocol, 30);
  while (client.available() > 0) {
    char c = client.read();
    Serial.print(c);
  }
  
  client.stop();

  delay(5000);
}
