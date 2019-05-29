package com.example.mainpage.user;

public class Like  {
    private String userMail;
    private String houseIdx;
    private String favoriteCheck = "0";

    public Like(){}

    public Like(String userMail, String houseIdx, String favoriteCheck){
        this.userMail = userMail;
        this.houseIdx = houseIdx;
        this.favoriteCheck = favoriteCheck;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getHouseIdx() {
        return houseIdx;
    }

    public String getFavoriteCheck() {
        return favoriteCheck;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public void setHouseIdx(String houseIdx) {
        this.houseIdx = houseIdx;
    }

    public void setFavoriteCheck(String favoriteCheck) {
        this.favoriteCheck = favoriteCheck;
    }

    @Override
    public String toString() {
        return "Like{" +
                "userMail='" + userMail + '\'' +
                ", houseIdx='" + houseIdx + '\'' +
                ", favoriteCheck='" + favoriteCheck + '\'' +
                '}';
    }
}
