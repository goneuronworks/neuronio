package java_example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class HttpSubmitExample {
	static URL loginUrl;
	static HttpURLConnection loginConn;
	static URL sendUrl;
	static HttpURLConnection sendConn;
	
	public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
		String cookie = httpLogin();
		httpSend(cookie);
		
		loginConn.disconnect();
        sendConn.disconnect();
    }
	
	//Work to identify things on the platform. "success" : true  Returns on Success.
	public static String httpLogin() throws IOException{
		String cookie = "";
		
		loginUrl = new URL("http://192.168.0.101:5000/login");
        loginConn = (HttpURLConnection) loginUrl.openConnection();        
        
        loginConn.setDoInput(true);
        loginConn.setDoOutput(true);
        loginConn.setUseCaches(false);
        loginConn.setReadTimeout(20000);
        loginConn.setRequestMethod("POST");  // or GET
        loginConn.setRequestProperty("Content-Type","application/json");
               
        OutputStream loginOs = loginConn.getOutputStream();
        OutputStreamWriter loginWriter = new OutputStreamWriter(loginOs);

        JSONObject data = new JSONObject();
        data.put("username", "example1@gmail.com");
        data.put("password", "test1234");
        
        loginWriter.write(data.toString());
        loginWriter.close();
        loginOs.close();
        
       
        Map<String, List<String>> headerFields = new HashMap<String, List<String>>();
        
        if(loginConn.getResponseCode() == HttpURLConnection.HTTP_OK){
        	StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(loginConn.getInputStream(),"utf-8"));
            
            //Extract the cookies from the response of the login url.
            headerFields = loginConn.getHeaderFields();
            List<String> cookiesList = new ArrayList<String>();
            cookiesList = headerFields.get("Set-Cookie");
            
            for(String value : cookiesList){
            	cookie += value;
            }
      
            String line = null;  
      
            while ((line = br.readLine()) != null) {
            	sb.append(line + "\n");  
            }  
      
            br.close();
      
            System.out.println(""+sb.toString());  
      
        }else{
            System.out.println("------------->"+loginConn.getResponseMessage());  
        }
		
		return cookie;
	}

	
	public static void httpSend(String cookie) throws IOException{
		sendUrl = new URL("http://192.168.0.101:5000/NeuronioExample/HTTPExample/NEURONIOHTTPExampleProtocol");
		sendConn = (HttpURLConnection) sendUrl.openConnection();                
        
        sendConn.setDoInput(true);
        sendConn.setDoOutput(true);
        sendConn.setUseCaches(false);
        sendConn.setReadTimeout(20000);
        sendConn.setRequestMethod("POST");
        sendConn.setRequestProperty("Cookie",cookie); //line 3
               
        OutputStream sendOs = sendConn.getOutputStream();
        OutputStreamWriter sendWriter = new OutputStreamWriter(sendOs);
        
        sendWriter.write("submitType=submit&"
        		+ "targetOid=T000000074&"
        		+ "stx=<&"
        		+ "oid=T000000074&"
        		+ "msg=Hello, World&"
        		+ "etx=>");
        sendWriter.close();
        sendOs.close();
        
        System.out.println("HttpResult : "+ sendConn.getResponseCode());
        
        if(sendConn.getResponseCode() == HttpURLConnection.HTTP_OK){
        	StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(sendConn.getInputStream(),"utf-8"));
      
            String line = null;  
      
            while ((line = br.readLine()) != null) {
            	sb.append(line + "\n");  
            }  
      
            br.close();  
      
            System.out.println(""+sb.toString());  
      
        }else{
            System.out.println("------------->"+sendConn.getResponseMessage());  
        }
	}
}
