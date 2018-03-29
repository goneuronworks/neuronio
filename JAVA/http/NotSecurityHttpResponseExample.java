package java_example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;

public class NotSecurityHttpResponseExample {
	
	public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
		String cookie = httpLogin();
		httpSend(cookie);
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
	
	//Work to identify things on the platform. "success" : true  Returns on Success.
	@SuppressWarnings("unchecked")
	public static String httpLogin() throws IOException{
		String cookie = "";
		URL url = new URL("http://192.168.0.101:5000/login");
		HttpURLConnection conn = setHttpConnection(url);
		
		conn.setRequestProperty("Content-Type","application/json");
		
		//request               
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);

        JSONObject data = new JSONObject();
        data.put("username", "example1@gmail.com");
        data.put("password", "test1234");
        
        writer.write(data.toString());
        writer.close();
        os.close(); 
        
        //response        
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
        	StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //Extract the cookies from the response of the login url.
            for(String value : conn.getHeaderFields().get("Set-Cookie")){
            	cookie += value;
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
		
		return cookie;
	}

	
	public static void httpSend(String cookie) throws IOException{
		URL url = new URL("http://192.168.0.101:5000/NeuronioExample/HTTPExample/NEURONIOHTTPExampleProtocol");
		HttpURLConnection conn = setHttpConnection(url);
		
		conn.setRequestProperty("Cookie",cookie);
		
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
