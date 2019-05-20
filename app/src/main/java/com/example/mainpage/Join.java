package com.example.mainpage;

public class Join {
    private String joinName;
    private String joinMail;
    private String joinPassword;
    private boolean joinSeparation;

    public Join() {
    }

    public Join(String joinName, String joinMail, String joinPassword, boolean joinSeparation){
        this.joinName = joinName;
        this.joinMail = joinMail;
        this.joinPassword = joinPassword;
        this.joinSeparation = joinSeparation;
    }

    public void setJoinName(String joinName) { this.joinName = joinName; }
    public void setJoinMail(String joinMail) { this.joinMail = joinMail; }
    public void setJoinPassword(String joinPassword) { this.joinPassword = joinPassword; }
    public void setJoinSeparation(boolean joinSeparation) { this.joinSeparation = joinSeparation; }

    public String getJoinName() { return joinName; }
    public String getJoinMail() { return joinMail; }
    public String getJoinPassword() { return joinPassword; }
    public boolean getJoinSeparation() { return joinSeparation; }
}