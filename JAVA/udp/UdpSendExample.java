package java_example.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import neuronio.security.utils.AESUtil;

public class UdpSendExample {
	static String ip = "192.168.0.101";
	static int port = 5000;
	static String privateKey = "vi0/UwhuXTM061TdhtyrcQ==";
    static String ivKey = "VswjHWnW/LCUWrdwzfyzlA==";
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
	
	private static String getSendMessage(){
		//You can type in a string that matches the protocol you want to transfer
		//When sending for authentication, all values must be filled to fit the total length of the protocol.
		
		String encryptData = "";
		
    	try {
    		//protocol structure => STX | OID | MSG | ETX 
    		String STX = "<";
    		String OID = "T000000078";
    		String MSG = "Hello, World";
    		String ETX = ">";
    		String sendMessage = STX + OID + MSG + ETX;
			try {
				encryptData = AESUtil.encryptData(privateKey, ivKey, sendMessage);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
    	catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return encryptData;
    }
	
	private static void sendPacket() throws IOException{
		String sendMsg = getSendMessage();
    	DatagramPacket packet = new DatagramPacket(sendMsg.getBytes(), sendMsg.getBytes().length,address, port);
    	
    	socket.send(packet);
    	
    	System.out.println("[Send Packet] : "+sendMsg);
	}
	
	private static void recvPacket() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramPacket response = new DatagramPacket(buffer, buffer.length);
		
		socket.receive(response);
		String quote = new String(buffer, 0, response.getLength());
		
		try {
			System.out.println("[Recv Packet] : " + AESUtil.decryptData(privateKey, ivKey, quote));
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
