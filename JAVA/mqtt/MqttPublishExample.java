package java_example.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPublishExample{
	static MqttClient client;
	
	public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
		String topic        = "/NeuronioExample/MQTTExample/hello";
        String content      = "Hello, world";
        String broker       = "tcp://192.168.0.101:5000";
        String clientId     = "T000000073";

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId);
            
        	sampleClient.setCallback(new MqttCallback() {
				@Override
				public void connectionLost(Throwable throwable) {
					System.out.println("connect lost");
				}
				
				@Override
				public void messageArrived(String t, MqttMessage m) throws Exception {
					System.out.println("[msg print]");
					System.out.println(m.toString());
				}
				@Override
				public void deliveryComplete(IMqttDeliveryToken t) {
					System.out.println("delivery complete");
				}
			});
        	
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("example1@gmail.com");
            connOpts.setPassword("test1234".toCharArray());
            connOpts.setCleanSession(true);
            
            sampleClient.connect(connOpts);
                        
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setRetained(true);
            
            sampleClient.publish(topic, message);
            
            System.out.println("publish done.");
            
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
		
	}
}
