package java_example.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NotSecurityTcpRecvExample {
	static OutputStream toServer;
    static InputStream fromServer;
    static Socket socket;

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
	    	    	    
	    System.out.println("Recv Wait.. ==============================");

	    receiveResponse();
    }
	
	
	private static void receiveResponse() throws IOException, InterruptedException {
    	byte[] reciveMessage = new byte[10240];
    	int readSize = 10240;

    	try{
	    	readSize = fromServer.read(reciveMessage);

	    	if(readSize == -1){
	    		close();
	    	}
	    	
	    	if(readSize == 0) return;
	    	
	    	byte[] message = new byte[readSize];

	    	System.arraycopy(reciveMessage, 0, message, 0, readSize);
	   		System.out.println("Recive Message : " + new String(message));
    	}
    	catch(Exception e){
    		close();
    	}
    	
    }
	
	private static void sendMessage(){   	
    	try {
    		String STX = "<";
    		String OID = "T000000077";
    		String MSG = "Hello, World      ";
    		String ETX = ">";
    		String sendMessage = STX + OID + MSG + ETX;

			toServer.write(sendMessage.getBytes());
			System.out.println("Send Message : " + sendMessage);
		}
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
		
	public static void close() throws IOException{
		System.out.println("Disconnect");
	    toServer.close();
	    fromServer.close();
	    socket.close();
    }
}
