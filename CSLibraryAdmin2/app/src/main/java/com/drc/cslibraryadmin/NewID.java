package com.drc.cslibraryadmin;

public class NewID {
    private String id,date,par,key;

    public NewID() {
    }

    public NewID(String id, String date,String key, String par) {
        this.id = id;
        this.date = date;
        this.par = par;
        this.key=key;
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
