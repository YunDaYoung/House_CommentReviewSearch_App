package com.example.mainpage;

public class Review {
    private String user_mail;
    private String user_review;

    public Review(String user_mail, String user_review) {
        this.user_mail = user_mail;
        this.user_review = user_review;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public void setUser_review(String user_review){
        this.user_review = user_review;
    }

    public String getUser_mail(){
        return user_mail;
    }

    public String getUser_review(){
        return user_review;
    }

    @Override
    public String toString() {
        return "사용자 이메일 : " + user_mail + "\n" + "리뷰 내용 : " + user_review;
    }
}
