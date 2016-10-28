package br.com.simplepass.cadevanaluno.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.hazelcast.com.eclipsesource.json.JsonObject;

public class Utils {

	public static JsonObject jsonOK(){
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("serverResult", "ok");
		return jsonObject;
	}
	
	public static String md5(String string) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(string.getBytes());
        byte array[] = md.digest();
        StringBuilder sb = new StringBuilder();

        for(byte b : array){
            //sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1,3));
            String h = Integer.toHexString(0xFF & b);
            while(h.length() < 2)
                h = "0" + h;

            sb.append(h);
        }

        return sb.toString();
	}
}
