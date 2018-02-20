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
    private int no; //in database codi number -> auto increase
    private int likes;

    public CoordyItem(){
        this.bottomImgURL="";
        this.topImgURL="";
        this.codiName="";
        this.fullCodiImgURL="";
        this.likes = -1;
        this.no=0;
    }

    public CoordyItem(String top, String bottom, String fullCodi, String codiName, int no){
        this.bottomImgURL = bottom;
        this.topImgURL=top;
        this.fullCodiImgURL=fullCodi;
        this.codiName=codiName;
        this.no = no;
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
