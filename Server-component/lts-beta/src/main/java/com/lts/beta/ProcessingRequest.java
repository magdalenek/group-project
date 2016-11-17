package com.lts.beta;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class ProcessingRequest {



    private String userId;
    private String facebookToken;
    private Integer sensitivityLevel;


    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public Integer getSensitivityLevel() {
        return sensitivityLevel;
    }

    public void setSensitivityLevel(Integer sensitivityLevel) {
        this.sensitivityLevel = sensitivityLevel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ProcessingRequest{" +
                "userId='" + userId + '\'' +
                ", facebookToken='" + facebookToken + '\'' +
                ", sensitivityLevel='" + sensitivityLevel + '\'' +
                '}';
    }
}
