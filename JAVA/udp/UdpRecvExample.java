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

public class UdpRecvExample {
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
	    	
	    	recvPacket();
	    	
			socket.close();
	    }catch(Exception e){
	      System.out.println(e.getMessage());
	    }
	}
	
	private static String getSendMessage(){
		String encryptData = "";
		
    	try {
    		//protocol structure => STX | OID | MSG | ETX 
    		String STX = "<";
    		String OID = "T000000079";
    		String MSG = "Hello, World      ";
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
		byte[] buffer = new byte[512];
		DatagramPacket response = new DatagramPacket(buffer, buffer.length);
		
		socket.receive(response);
		String quote = new String(buffer, 0, response.getLength());
		
		System.out.print("[Recv Packet] : ");
		//System.out.println(quote);
		
		try {
			System.out.println(AESUtil.decryptData(privateKey, ivKey, quote));
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
