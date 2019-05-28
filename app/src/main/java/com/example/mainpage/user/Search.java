package com.example.mainpage.user;

public class Search {
    private String address1;
    private String address2;
    private String address3;
    private String price1;
    private String area1;
    private String price2;
    private String area2;
    private String searchword;
    Search(){}

    Search(String address1, String address2, String address3, String price1, String area1, String price2, String area2, String searchword){
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.price1 = price1;
        this.area1 = area1;
        this.price2 = price2;
        this.area2 = area2;
        this.searchword = searchword;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public void setArea1(String area1) {
        this.area1 = area1;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public void setArea2(String area2) {
        this.area2 = area2;
    }

    public void setSearchword(String searchword) {
        this.searchword = searchword;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getPrice1() {
        return price1;
    }

    public String getArea1() {
        return area1;
    }

    public String getPrice2() {
        return price2;
    }

    public String getArea2() {
        return area2;
    }

    public String getSearchword() {
        return searchword;
    }

    @Override
    public String toString() {
        return "Search{" +
                "address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", price1='" + price1 + '\'' +
                ", area1='" + area1 + '\'' +
                ", price2='" + price2 + '\'' +
                ", area2='" + area2 + '\'' +
                ", searchword='" + searchword + '\'' +
                '}';
    }
}
