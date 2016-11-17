package models;

public class UserIdAndSensitivityLevel {

    //lts.beta16@gmail.com
    //imperial16
    private String userId;
    private Integer sensitivityLevel;


    public UserIdAndSensitivityLevel(String userId, Integer sensitivityLevel) {
        this.sensitivityLevel = sensitivityLevel;
        this.userId = userId;
    }

    public UserIdAndSensitivityLevel(){

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
}
