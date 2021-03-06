package com.example.geehy.hangerapplication.gridview_home;

import java.io.Serializable;

/**
 * Created by JHS on 2018-02-09.
 */

public class CoordyItem implements Serializable{
    private String topImgURL;
    private String bottomImgURL;
    private String fullCodiImgURL;
    private String codiName;
    private int tall;
    private int weight;
    private int no; //in database codi number -> auto increase
    private int likes;
    private int isShare;

    public CoordyItem(){
        this.bottomImgURL="";
        this.topImgURL="";
        this.codiName="";
        this.fullCodiImgURL="";
        this.likes = 0;
        this.no=0;
        this.isShare=0;
        this.tall= 0;
        this.weight=0;


    }

    public CoordyItem(String top, String bottom, String fullCodi, String codiName, int no){
        this.bottomImgURL = bottom;
        this.topImgURL=top;
        this.fullCodiImgURL=fullCodi;
        this.codiName=codiName;
        this.no = no;
    }

    public int getTall() {
        return tall;
    }

    public int getWeight() {
        return weight;
    }

    public void setTall(int tall) {
        this.tall = tall;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getIsShare() {
        return isShare;
    }

    public void setIsShare(int isShare) {
        this.isShare = isShare;
    }

    public String getFullCodiImgURL() {
        return fullCodiImgURL;
    }

    public void setFullCodiImgURL(String fullCodiImgURL) {
        this.fullCodiImgURL = fullCodiImgURL;
    }
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCodiName() {
        return codiName;
    }

    public void setCodiName(String codiName) {
        this.codiName = codiName;
    }

    public String getBottomImgURL() {
        return bottomImgURL;
    }

    public String getTopImgURL() {
        return topImgURL;
    }

    public void setBottomImgURL(String bottomImgURL) {
        this.bottomImgURL = bottomImgURL;
    }

    public void setTopImgURL(String topImgURL) {
        this.topImgURL = topImgURL;
    }
}
