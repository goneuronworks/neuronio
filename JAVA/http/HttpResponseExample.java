package java_example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import neuronio.security.utils.RSAUtil;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HttpResponseExample {
	
	public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		String cookie = "";
		
		json = certification();
		cookie = login(makePublicKey((String)json.get("publicKeyModulus"), (String)json.get("publicKeyExponent")));
		sendMessage(cookie);
    }
	
	public static HttpURLConnection setHttpConnection(URL url) throws IOException{
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setReadTimeout(20000);
		conn.setRequestMethod("POST");  // or GET
		
		return conn;
	}
	
	public static PublicKey makePublicKey(String publicKeyModulus, String publicKeyExponent) throws NoSuchAlgorithmException, InvalidKeySpecException{
		RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(
				new BigInteger(publicKeyModulus, 16), 
				new BigInteger(publicKeyExponent, 16)
				);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey key = (PublicKey) keyFactory.generatePublic(publicSpec);
		
		return key;
	}
		
	@SuppressWarnings("unchecked")
	public static JSONObject certification() throws IOException, ParseException{
		JSONObject resultJson = new JSONObject();
		URL url = new URL("http://192.168.0.102:5000/certification");
		HttpURLConnection conn = setHttpConnection(url);
		
		conn.setRequestProperty("Content-Type","application/json");
        
		//request
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);

        JSONObject data = new JSONObject();
        data.put("format", "json");
        
        writer.write(data.toString());
        writer.close();
        os.close();
        
        //response
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
        	StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line = null;  
            JSONParser parser = new JSONParser();
      
            while ((line = br.readLine()) != null) {
            	sb.append(line + "\n");  
            }
            br.close();
      
            System.out.println(""+sb.toString());
            
            resultJson = (JSONObject) parser.parse(sb.toString());
            
        }else{
            System.out.println("[HTTP Response msg] : "+conn.getResponseMessage());  
        }
        
        conn.disconnect();
        
        return resultJson;
	}
	
	//Work to identify things on the platform. "success" : true  Returns on Success.
	@SuppressWarnings("unchecked")
	public static String login(PublicKey publicKey) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		String resultCookie = "";
		URL url = new URL("http://192.168.0.102:5000/login");
		HttpURLConnection conn = setHttpConnection(url);
		
        conn.setRequestProperty("Content-Type","application/json");
               
        //request
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);

        JSONObject data = new JSONObject();
        data.put("securedUsername", RSAUtil.encrypt("example@gmail.com", publicKey));
        data.put("securedPassword", RSAUtil.encrypt("neuroniopw", publicKey));
        
        writer.write(data.toString());
        writer.close();
        os.close();
       
        //response
        System.out.println("HttpResult : "+ conn.getResponseCode());
        
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
        	StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
                        
            //Extract the cookies from the response of the login url.
            for(String value : conn.getHeaderFields().get("Set-Cookie")){
            	resultCookie += value;
            }
      
            String line = null;  
      
            while ((line = br.readLine()) != null) {
            	sb.append(line + "\n");  
            }
            br.close();
      
            System.out.println(""+sb.toString());  
      
        }else{
            System.out.println("[HTTP Response msg] : "+conn.getResponseMessage());  
        }
		
        conn.disconnect();
        
		return resultCookie;
	}
	
	
	public static void sendMessage(String cookie) throws IOException{
		URL url = new URL("http://192.168.0.102:5000/NeuronioExample/HTTPExample/NEURONIOHTTPExampleProtocol");
		HttpURLConnection conn = setHttpConnection(url);
		
        conn.setRequestProperty("Cookie",cookie); //line 3
               
        //request
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);
        
        writer.write("submitType=response&"
        		+ "targetOid=T000000074");
        writer.close();
        os.close();
        
        //response
        System.out.println("HttpResult : "+ conn.getResponseCode());
        
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
        	StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
      
            String line = null;  
      
            while ((line = br.readLine()) != null) {
            	sb.append(line + "\n");  
            }
            br.close();  
      
            System.out.println(""+sb.toString());  
      
        }else{
            System.out.println("[HTTP Response msg] : "+conn.getResponseMessage());  
        }
        
        conn.disconnect();
	}
}
