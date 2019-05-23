package com.example.mainpage.user;

import java.io.Serializable;

public class User implements Serializable {
    private String userMail;
    private String userName;
    private String userCheck;

    public User() {
    }

    public User(String userMail, String userName, String userCheck) {
        this.userMail = userMail;
        this.userName = userName;
        this.userCheck = userCheck;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCheck() {
        return userCheck;
    }

    public void setUserCheck(String userCheck) {
        this.userCheck = userCheck;
    }

    @Override
    public String toString() {
        return "User{" +
                "userMail='" + userMail + '\'' +
                ", userName='" + userName + '\'' +
                ", userCheck='" + userCheck + '\'' +
                '}';
    }
}