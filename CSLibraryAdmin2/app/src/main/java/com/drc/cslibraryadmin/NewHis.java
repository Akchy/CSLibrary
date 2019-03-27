package com.drc.cslibraryadmin;

public class NewHis {
    private String val,time,date;

    public NewHis(String val, String time,String date) {
        this.val = val;
        this.date = date;
        this.time = time;
    }

    public NewHis() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
