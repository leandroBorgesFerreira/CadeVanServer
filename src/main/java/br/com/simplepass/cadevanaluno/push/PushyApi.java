package br.com.simplepass.cadevanaluno.push;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.simplepass.cadevanaluno.dto.PushyPushRequest;
import java.util.Map;
import org.springframework.web.client.RestTemplate;



public class PushyApi{
    public static ObjectMapper mapper = new ObjectMapper();
    public static final String PUSHY_SECRET_API_KEY = "84d5f6a83b8dd5811461520de1a05c8ce9fbf4d211ad711dab88c48adfafa266";
    
    public static void sendPush2( PushyPushRequest req ) throws Exception{
        // Get custom HTTP client
        HttpClient client = new DefaultHttpClient();

        // Create post request
        HttpPost request = new HttpPost( "https://api.pushy.me/push?api_key=" + PUSHY_SECRET_API_KEY );

        // Set content type to JSON
        request.addHeader("Content-Type", "application/json");

        // Convert API request to JSON
        String json = mapper.writeValueAsString(req);

        // Send post data as string
        request.setEntity(new StringEntity(json));

        // Execute the request
        HttpResponse response = client.execute(request, new BasicHttpContext());

        // Get response JSON as string
        String responseJSON = EntityUtils.toString(response.getEntity());

        // Convert JSON response into HashMap
        Map<String, Object> map = mapper.readValue(responseJSON, Map.class);

        // Got an error?
        if ( map.containsKey("error") )
        {
            // Throw it
            throw new Exception(map.get("error").toString());
        }
    }
    
    
}

