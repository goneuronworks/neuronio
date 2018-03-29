package java_example.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NotSecurityUdpSendExample {
	static String ip = "192.168.0.101";
	static int port = 5000;
    static DatagramSocket socket;
    static InetAddress address;    
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    try{
	    	socket = new DatagramSocket(); 
	    	address = InetAddress.getByName(ip);
			
	    	sendPacket();
	    	recvPacket();
	    	
	    	sendPacket();
	    	
			socket.close();
	    }catch(Exception e){
	      System.out.println(e.getMessage());
	    }
	}
	
	private static void sendPacket() throws IOException{
		//protocol send structure
		//total length : 24
		String STX = "<";
		String OID = "T000000079";
		String MSG = "Hello, World";
		String ETX = ">";
		String sendMsg = STX + OID + MSG + ETX;
				
    	DatagramPacket packet = new DatagramPacket(sendMsg.getBytes(), sendMsg.getBytes().length,address, port);
    	
    	socket.send(packet);
    	
    	System.out.println("[Send Packet] : "+sendMsg);
	}
	
	private static void recvPacket() throws IOException{
		byte[] buffer = new byte[512];
		DatagramPacket response = new DatagramPacket(buffer, buffer.length);
		
		socket.receive(response);
		String quote = new String(buffer, 0, response.getLength());
		
		System.out.println("[Recv Packet] : " + quote);
	}
}
