package com.example.mainpage;

public class House {
    private String houseIdx;
    private int housePic;
    private String housePrice;
    private String houseSpace;
    private String houseComment;
    private String houseAddress;
    private String userMail;

    public House(String houseIdx, int housePic, String housePrice, String houseSpace, String houseComment, String houseAddress, String userMail) {
        this.houseIdx = houseIdx;
        this.housePic = housePic;
        this.housePrice = housePrice;
        this.houseSpace = houseSpace;
        this.houseComment = houseComment;
        this.houseAddress = houseAddress;
        this.userMail = userMail;
    }

    public String getHouseIdx() {
        return houseIdx;
    }

    public void setHouseIdx(String houseIdx) {
        this.houseIdx = houseIdx;
    }

    public int getHousePic() {
        return housePic;
    }

    public void setHousePic(int housePic) {
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

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @Override
    public String toString() {
        return "House{" +
                "houseIdx='" + houseIdx + '\'' +
                ", housePic='" + housePic + '\'' +
                ", housePrice='" + housePrice + '\'' +
                ", houseSpace='" + houseSpace + '\'' +
                ", houseComment='" + houseComment + '\'' +
                ", houseAddress='" + houseAddress + '\'' +
                ", userMail='" + userMail + '\'' +
                '}';
    }
}
