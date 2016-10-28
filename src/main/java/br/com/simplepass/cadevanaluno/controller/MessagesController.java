package br.com.simplepass.cadevanaluno.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import br.com.simplepass.cadevanaluno.domain.Driver;
import br.com.simplepass.cadevanaluno.domain.User;
import br.com.simplepass.cadevanaluno.dto.FirebasePushRequest;
import br.com.simplepass.cadevanaluno.dto.GcmMessage;
import br.com.simplepass.cadevanaluno.dto.PushyPushRequest;
import br.com.simplepass.cadevanaluno.exception.BadRequestException;
import br.com.simplepass.cadevanaluno.push.FirebaseApi;
import br.com.simplepass.cadevanaluno.push.PushyApi;
import br.com.simplepass.cadevanaluno.repository.DriversRepository;
import br.com.simplepass.cadevanaluno.repository.UsersRepository;


@RestController("messagesController")
public class MessagesController {
	@Inject
	UsersRepository usersRepository;
	@Inject
	DriversRepository driversRepository;
	
	@RequestMapping(value="/messages", method=RequestMethod.POST)
	public ResponseEntity<String> sendPushNotification(@RequestBody GcmMessage gcmMessage){
		List<String> pushyTokenList = new ArrayList<>();
		List<String> firebaseTokenList = new ArrayList<>();
		
		boolean pushySuccess = true;
		boolean firebaseSuccess = true;
		
		for(String phone : gcmMessage.getPhoneNumberList()){
			User user = usersRepository.findByPhoneNumber(phone);
			
			if(user != null){
				if(user.getOs().equals("android")){
					pushyTokenList.add(user.getGcmToken());
				} else if(user.getOs().equals("iOS")){
					firebaseTokenList.add(user.getGcmToken());
				}
				
			} else{
				Driver driver = driversRepository.findByPhoneNumber(phone);
				
				if(driver != null){
					if(driver.getOs().equals("android")){
						pushyTokenList.add(driver.getGcmToken());
					} else if(driver.getOs().equals("iOS")){
						firebaseTokenList.add(driver.getGcmToken());
					}
				}
			}
		}
		
		if(pushyTokenList.isEmpty() && firebaseTokenList.isEmpty()){
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT); 
		} else{
			if(!pushyTokenList.isEmpty()){
				pushySuccess = pushPushy(pushyTokenList, gcmMessage.getMessage(), gcmMessage.getType());
			}
			
			if(!firebaseTokenList.isEmpty()){
				firebaseSuccess = pushFirebase(firebaseTokenList, gcmMessage);
			}
			
			if(pushySuccess && firebaseSuccess){
				return new ResponseEntity<>(null, HttpStatus.OK);
			} else{
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	public boolean pushFirebase(List<String> tokenList, GcmMessage gcmMessage){
		Map<String, String> notification = new HashMap<>();
		
		notification.put("text", gcmMessage.getMessage());
		notification.put("title", gcmMessage.getTitle());
		
		FirebasePushRequest request = new FirebasePushRequest(tokenList, true, notification, null);
		
		return FirebaseApi.sendPush(request);
	}
	
	public boolean pushPushy(List<String> tokenList, String message, String type){
		Map<String, String> payload = new HashMap<>();
		
		payload.put("message", message);
		payload.put("type", type);
		
		PushyPushRequest push = new PushyPushRequest(payload, tokenList.toArray(new String[tokenList.size()]));
		
		try {
			PushyApi.sendPush2(push);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public ResponseEntity<String> push(List<String> tokenList, String message, String type){
		final String GCM_API_KEY = "AIzaSyAHxycgSPKZFvkJIWfzomedcQ4gkRkvfBs";
					
		final int retries = 3;
				
        Sender sender = new Sender(GCM_API_KEY);
        Message msg = new Message
        		.Builder()
        		.addData("message", message)
        		.addData("type", type)
        		.build();
        
        try {           
            MulticastResult result = sender.send(msg, tokenList, retries);
            /**
            * if you want to send to multiple then use below method
            * send(Message message, List<String> regIds, int retries)
            **/
                       
            if(result.getSuccess() > 0){
            	return new ResponseEntity<>(null, HttpStatus.OK); 
            } else{
            	return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);  
            }                
        } catch (InvalidRequestException e) {
        	throw new BadRequestException(e.getMessage(), e.getCause());          
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
		
        return new ResponseEntity<>("Error occurred while sending push notification",
        		HttpStatus.EXPECTATION_FAILED);
	}
}
