package com.lts.beta.track;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserTrackingService {

    private ConcurrentHashMap<String, FacebookClient> users = new ConcurrentHashMap<>();

    public void addUser(String token){
        try {
            users.put(token, new DefaultFacebookClient(token, Version.VERSION_2_2));
        } catch (Exception e){
            System.out.print("some error, check what");
            e.printStackTrace();
        }
    }

    public void updateUsers() {
        Map<String, FacebookClient> copy = new HashMap<>(users);
        for (Map.Entry<String, FacebookClient> entry : copy.entrySet()) {
            String token = entry.getKey();
            FacebookClient facebookClient = entry.getValue();

        }
    }


}
