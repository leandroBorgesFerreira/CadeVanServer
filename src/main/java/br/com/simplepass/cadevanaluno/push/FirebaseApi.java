package br.com.simplepass.cadevanaluno.push;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.simplepass.cadevanaluno.dto.FirebasePushRequest;


public class FirebaseApi {
	public static final String FIREBASE_API_KEY = "AIzaSyAHxycgSPKZFvkJIWfzomedcQ4gkRkvfBs";
	
	public static boolean sendPush(FirebasePushRequest request){
		URL url;
		
		try {
			url = new URL("https://fcm.googleapis.com/fcm/send");
			//Consumir o auth/token manualmente
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(15000);				
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Authorization", "key=" + FIREBASE_API_KEY);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
         
            Gson gson = new GsonBuilder().create();
            String requestString = gson.toJson(request);
            
            System.out.println(requestString);
                        
            writer.write(requestString);
            writer.flush();
            writer.close();
            os.close();
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpsURLConnection.HTTP_OK) {
            	String anwser = "";           
            	
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                
                while ((line=br.readLine()) != null) {               
                    anwser += line;                                                    
                } 
                
                System.out.println(anwser);
                
                return true;
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
}
