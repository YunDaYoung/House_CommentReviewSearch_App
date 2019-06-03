package com.example.mainpage.user;

public class Review {
    private String user_mail;
    private String user_review;
    private  String house_idx;

    public Review(){}
    public Review(String user_mail, String user_review, String house_idx) {
        this.user_mail = user_mail;
        this.user_review = user_review;
        this.house_idx = house_idx;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public void setUser_review(String user_review){
        this.user_review = user_review;
    }

    public void setHouse_idx(String house_idx) { this.house_idx = house_idx; }

    public String getUser_mail(){
        return user_mail;
    }

    public String getUser_review(){
        return user_review;
    }

    public String getHouse_idx() { return  house_idx;}

    @Override
    public String toString() {
        return "사용자 이메일 : " + user_mail + " " + "리뷰 내용 : " + user_review + " " + "집 번호 : " + house_idx;
    }
}