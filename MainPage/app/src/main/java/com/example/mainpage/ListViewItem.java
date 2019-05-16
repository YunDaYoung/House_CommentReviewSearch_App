package com.example.mainpage;

public class ListViewItem {
    private int pic;
    private String text;
    public int getPic(){
        return pic;
    }
    public String getText(){
        return text;
    }
    public ListViewItem(int pic,String text){
        this.pic = pic;
        this.text = text;
    }
}