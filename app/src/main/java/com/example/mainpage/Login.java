package com.example.mainpage;

public class Login{
    private String loginMail;
    private String loginPassword;
    private boolean loginSeparation;

    public Login(String loginMail, String loginPassword, boolean loginSeparation){
        this.loginMail = loginMail;
        this.loginPassword = loginPassword;
        this.loginSeparation = loginSeparation;
    }

    public void setLoginMail(String loginMail) { this.loginMail = loginMail; }
    public void setLoginPassword (String loginPassword) { this.loginPassword = loginPassword; }
    public void setLoginSeparation(boolean loginSeparation) { this.loginSeparation = loginSeparation; }

    public String getLoginMail() { return loginMail; }
    public String getLoginPassword() { return loginPassword; }
    public boolean getLoginSeparation() { return loginSeparation; }
}
