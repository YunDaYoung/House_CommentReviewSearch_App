package com.example.mainpage;

public class House {
    private String houseIdx;
    private String housePic;
    private String housePrice;
    private String houseSpace;
    private String houseComment;
    private String houseAddress1;
    private String houseAddress2;
    private String houseAddress3;
    private String userMail;

    public House() {
    }

    public House(String houseIdx, String housePic, String housePrice, String houseSpace, String houseComment, String houseAddress1, String houseAddress2, String houseAddress3, String userMail) {
        this.houseIdx = houseIdx;
        this.housePic = housePic;
        this.housePrice = housePrice;
        this.houseSpace = houseSpace;
        this.houseComment = houseComment;
        this.houseAddress1 = houseAddress1;
        this.houseAddress2 = houseAddress2;
        this.houseAddress3 = houseAddress3;
        this.userMail = userMail;
    }

    public String getHouseIdx() {
        return houseIdx;
    }

    public void setHouseIdx(String houseIdx) {
        this.houseIdx = houseIdx;
    }

    public String getHousePic() {
        return housePic;
    }

    public void setHousePic(String housePic) {
        this.housePic = housePic;
    }

    public String getHousePrice() {
        return housePrice;
    }

    public void setHousePrice(String housePrice) {
        this.housePrice = housePrice;
    }

    public String getHouseSpace() {
        return houseSpace;
    }

    public void setHouseSpace(String houseSpace) {
        this.houseSpace = houseSpace;
    }

    public String getHouseComment() {
        return houseComment;
    }

    public void setHouseComment(String houseComment) {
        this.houseComment = houseComment;
    }

    public String getHouseAddress1() {
        return houseAddress1;
    }

    public void setHouseAddress1(String houseAddress1) {
        this.houseAddress1 = houseAddress1;
    }

    public String getHouseAddress2() {
        return houseAddress2;
    }

    public void setHouseAddress2(String houseAddress2) {
        this.houseAddress2 = houseAddress2;
    }

    public String getHouseAddress3() {
        return houseAddress3;
    }

    public void setHouseAddress3(String houseAddress3) {
        this.houseAddress3 = houseAddress3;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @Override
    public String toString() {
        return "{" +
                "houseIdx='" + houseIdx + '\'' +
                ", housePic=" + housePic +
                ", housePrice='" + housePrice + '\'' +
                ", houseSpace='" + houseSpace + '\'' +
                ", houseComment='" + houseComment + '\'' +
                ", houseAddress1='" + houseAddress1 + '\'' +
                ", houseAddress2='" + houseAddress2 + '\'' +
                ", houseAddress3='" + houseAddress3 + '\'' +
                ", userMail='" + userMail + '\'' +
                '}';
    }
}