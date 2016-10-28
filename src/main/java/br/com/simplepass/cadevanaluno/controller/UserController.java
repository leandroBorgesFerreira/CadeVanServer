package br.com.simplepass.cadevanaluno.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.simplepass.cadevanaluno.domain.User;
import br.com.simplepass.cadevanaluno.dto.AccessToken;
import br.com.simplepass.cadevanaluno.dto.RecoverPasswordBean;
import br.com.simplepass.cadevanaluno.exception.BadRequestException;
import br.com.simplepass.cadevanaluno.exception.ResourceNotFoundException;
import br.com.simplepass.cadevanaluno.repository.UsersRepository;
import br.com.simplepass.cadevanaluno.utils.Utils;
import io.swagger.annotations.ApiOperation;

@RestController("userController")
public class UserController {
	@Inject
	private UsersRepository usersRepository;
	
	@Inject
	private JavaMailSender javaMailSender;
	
	@RequestMapping(value="/users/{id}", method=RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userUpdated){
		if(id != userUpdated.getId()){
			throw new BadRequestException("The ID in the URL and in the User object are not the same.");
		}
		
		User user = usersRepository.findById(id);
		
		if(user == null){
			throw new ResourceNotFoundException("User with ID: " + userUpdated.getId() +
					" not found.");
		} else{				
			userUpdated.setPassword(user.getPassword());
			User userReturn = usersRepository.save(userUpdated);
			
			return new ResponseEntity<>(userReturn, HttpStatus.OK);
		}
	}

	@RequestMapping(value="/users", method=RequestMethod.GET)
	@ApiOperation(value="Retrives all users", response=User.class, responseContainer="List")
	public ResponseEntity<Iterable<User>> getAllUsers(
			@RequestParam(required=false, value="phoneNumber") String phoneNumber){
		Iterable<User> users;
		
		if(phoneNumber == null){
			users = usersRepository.findAll();
		} else{
			User user = usersRepository.findByPhoneNumber(phoneNumber);
			
			if(user == null){
				throw new ResourceNotFoundException("User with phone: " + phoneNumber +
						" not found.");
			}
			
			ArrayList<User> userList = new ArrayList<>();
			userList.add(user);
			users = userList;
		}
		
		if(users.iterator().hasNext()){
			return new ResponseEntity<>(users, HttpStatus.OK);
		} else{
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/users", method=RequestMethod.POST)
	public ResponseEntity<User> createUser(@Valid @RequestBody User user){
		user = usersRepository.save(user);	
		
		URL url;
		try {
			url = new URL("http://localhost:8080/cadevan/oauth/token");
			//Consumir o auth/token manualmente
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(10000);				
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Authorization", "Basic YXBwOnRvcF9zZWNyZXQ=");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			Map<String, String> requetsParams = new HashMap<>();
			
			requetsParams.put("username", user.getPhoneNumber());
			requetsParams.put("password", user.getPassword());
			requetsParams.put("grant_type", "password");
			
			OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(requetsParams));
            
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
                    
                    Gson gson = new GsonBuilder().create();
                    AccessToken token = gson.fromJson(anwser, AccessToken.class);
                    
                    user.setAccessToken(token.getAccess_token());
                    return new ResponseEntity<>(user, HttpStatus.CREATED);
                }                             
            } else{
            	System.out.println("não conseguiu contactar servidor");
            	return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else 
                result.append("&");
 
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        } 
 
        return result.toString();
    } 
	
	@RequestMapping(value="/users/recoverPassword", method=RequestMethod.POST)
	public ResponseEntity<User> recoverPassword(@RequestBody RecoverPasswordBean passwordBean){
		User user = usersRepository.findByPhoneNumber(passwordBean.getPhoneNumber());
		
		if(user == null){
			throw new ResourceNotFoundException("User with phone: " + passwordBean.getPhoneNumber() +
					" not found.");
		}
		
		String email = user.getEmail();
		
		try{
			String encrypt = Utils.md5(String.valueOf(user.getId()));
			
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(email);
			mailMessage.setSubject("Recuperar senha");
			mailMessage.setText("Olá, " + user.getName() + ".\n\n "+ 
					"Você solicitou a recuperação de senha do CadeVan. Acesse o link : \n\n" + 
					"http://simplepass.teramundi.com/cadevan/changePassword/changePassword.php?encrypt="+encrypt+"&action=reset' \n\n" +
					"Para redefinir a sua senha. \n\n" +
					"Atenciosamente, \n" +
					"Equipe CadeVan/Simple Pass");
			mailMessage.setFrom("contato@simplepass.com.br");
						
			javaMailSender.send(mailMessage);
			
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch(NoSuchAlgorithmException e){			
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
