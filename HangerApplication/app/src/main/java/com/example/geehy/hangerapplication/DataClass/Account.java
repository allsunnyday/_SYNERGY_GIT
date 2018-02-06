package com.example.geehy.hangerapplication.DataClass;


import java.io.Serializable;

public class Account implements Serializable {
    private String ID;
    private String Pwd;
    private String Uname;

    public Account(){
        this.ID="";
        this.Pwd = "";
        this.Uname="";
    }

    public Account(String i, String p , String u){
        this.ID=i;
        this.Pwd = p;
        this.Uname=u;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getID() {
        return ID;
    }

    public String getPwd() {
        return Pwd;
    }

    public String getUname() {
        return Uname;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPwd(String pwd) {
        Pwd = pwd;
    }

    public void setUname(String uname) {
        Uname = uname;
    }
}
