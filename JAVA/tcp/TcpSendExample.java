package java_example.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import neuronio.security.utils.AESUtil;

public class TcpSendExample {
	static OutputStream toServer;
    static InputStream fromServer;
    static Socket socket;
    
    static String privateKey = "vi0/UwhuXTM061TdhtyrcQ==";
    static String ivKey = "VswjHWnW/LCUWrdwzfyzlA==";
    static String ip = "192.168.0.101";
    static int port = 5000;
    
	public static void main(String[] args) throws Exception {
		try {
	        socket = new Socket(ip, port);
	        System.out.println("Connected");
	    }
	    catch (Exception e){
	        System.out.println("Can not connect to Server!");
	    }

	    toServer = socket.getOutputStream(); 
	    fromServer = socket.getInputStream();
	    
	    //Work to identify things on the platform. SUCCESS Returns on Success.
	    sendMessage();
    	receiveResponse();
    	
    	
    	System.out.println("Send Start ==============================");
    	sendMessage();
    }
	
	private static void sendMessage(){   	
    	try {
    		//You can type in a string that matches the protocol you want to transfer
    		//When sending for authentication, all values must be filled to fit the total length of the protocol.
    		
    		//Total Length : 24 
    		String STX = "<";
    		String OID = "T000000075";
    		String MSG = "Hello, World";
    		String ETX = ">";
    		String sendMessage = STX + OID + MSG + ETX;
	    	String encryptData;
	    	
			try {
				encryptData = AESUtil.encryptData(privateKey, ivKey, sendMessage);
				toServer.write(encryptData.getBytes());
				
				System.out.println("Send Message : " + sendMessage + " / " + encryptData);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
    	catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
	
	private static boolean receiveResponse() throws IOException, InterruptedException {
    	byte[] reciveMessage = new byte[10240];
    	int readSize = 10240;

    	try{
	    	readSize = fromServer.read(reciveMessage);

	    	if(readSize == -1){
	    		close();
	    	}
	    	
	    	if(readSize == 0) return true;
	    	
	    	byte[] message = new byte[readSize];
	    	
	    	System.arraycopy(reciveMessage, 0, message, 0, readSize);
	    	String decryptData = AESUtil.decryptData(privateKey, ivKey, new String(message).trim());
	   		System.out.println("\nRecive Message : " + decryptData);
    	}
    	catch(Exception e){
    		close();
    	}    	
    	
    	return false;
    }
	
	public static void close() throws IOException{
		System.out.println("Disconnect");
		
	    toServer.close();
	    fromServer.close();
	    socket.close();
    }
}
