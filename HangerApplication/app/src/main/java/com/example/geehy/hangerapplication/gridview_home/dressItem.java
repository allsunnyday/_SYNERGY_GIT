package com.example.geehy.hangerapplication.gridview_home;

import java.io.Serializable;

public class dressItem implements Serializable{
    //보여지는것
    private String imgURL;

    //안보여지는것
    private String dressName;
    private String dressColor;
    private String dressTag;
    private String Cat1;
    private String season; // spring 0 summer 1 fall 2 winter 3
    private int colorFlag;
    private String brand;




    public dressItem(){
        this.imgURL = "";
        this.dressName = "";
        this.dressColor = "";
        this.dressTag = null;
        this.Cat1 = "";
        this.season = "";
        this.colorFlag=0; // 0이면 색 X
        this.brand="";

    }

    public dressItem(String iu, String dN, String dC, String dT, String c1, String c2, String sn, boolean isc, boolean iso){
        this.imgURL = iu;
        this.dressName = dN;
        this.dressColor = dC;
        this.dressTag = dT;
        this.Cat1 = c1;
        this.season = sn;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getColorFlag() {
        return colorFlag;
    }

    public void setColorFlag(int colorFlag) {
        this.colorFlag = colorFlag;
    }
    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getDressName() {
        return dressName;
    }

    public void setDressName(String dressName) {
        this.dressName = dressName;
    }

    public String getDressColor() {
        return dressColor;
    }

    public void setDressColor(String dressColor) {
        this.dressColor = dressColor;
    }

    public String getDressTag() {
        return dressTag;
    }

    public void setDressTag(String dressTag) {
        this.dressTag = dressTag;
    }

    public String getCat1() {
        return Cat1;
    }

    public void setCat1(String cat1) {
        Cat1 = cat1;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }


}
