
/*
 * The example is tested with Arduino YUN. 
 * If you are using other wifi module, 
 * you can employ a library that matches the wifi module.
 */
 
#include <SPI.h>
#include <Bridge.h>
#include <BridgeClient.h>
#include <PubSubClient.h>

const char* MQTT_BROKER = "192.168.0.101";
uint16_t MQTT_PORT = 5000;

void callback(char* topic, byte* payload, unsigned int length) {
  // handle message arrived
}

BridgeClient ethClient;
PubSubClient client(ethClient);

long lastReconnectAttempt = 0;

boolean reconnect() {
  //client ID, user, password 
  //For the user, password, type the apI - user registered in General- > Standard certification.
  if (client.connect("T000000073", "example1@gmail.com", "test1234")) {
    // Once connected, publish an announcement...
    client.publish("/NeuronioExample/MQTTExample/hello","hello world");
    
    // ... and resubscribe
    client.subscribe("/NeuronioExample/MQTTExample/hello");
   }
   
   return client.connected();
}

void setup()
{
  Bridge.begin();
  client.setServer(MQTT_BROKER, MQTT_PORT);
  client.setCallback(callback);
  
  delay(3000);
  lastReconnectAttempt = 0;
}

void loop()
{
  if (!client.connected()) {
    long now = millis();
    if (now - lastReconnectAttempt > 5000) {
      lastReconnectAttempt = now;
      // Attempt to reconnect
      if (reconnect()) {
        lastReconnectAttempt = 0;
      }
    }
    
   } else {
    // Client connected
    client.loop();
   }
}
