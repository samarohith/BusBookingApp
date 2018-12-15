package com.example.user.navigationdrawer;

import com.google.firebase.auth.FirebaseUser;

public class User {

    private String userEmail;
    private String userName;
    public String Limit;
    private String uid;

    public User(FirebaseUser user){

        this.userName = user.getDisplayName();
        this.userEmail = user.getEmail();
        this.uid=user.getUid();
        this.Limit = "0";
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLimit() {
        return Limit;
    }

    public String setLimit(String Limit) {
        this.Limit = Limit;
        return Limit;
    }

    public String getUid() {
        return uid;
    }


}

