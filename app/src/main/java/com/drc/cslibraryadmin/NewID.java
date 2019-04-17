package com.drc.cslibraryadmin;

public class NewID {
    private String id,date,par,key,user;

    public NewID() {
    }

    public NewID(String id, String date,String key, String par,String user) {
        this.id = id;
        this.date = date;
        this.par = par;
        this.key=key;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPar() {
        return par;
    }

    public void setPar(String par) {
        this.par = par;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
