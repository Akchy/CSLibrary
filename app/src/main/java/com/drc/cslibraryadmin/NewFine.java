package com.drc.cslibraryadmin;

public class NewFine {
    private String name,amt,sid,bname,rdate;

    public NewFine() {
    }

    public NewFine(String name, String amt, String rdate,String sid,String bname) {
        this.name = name;
        this.amt = amt;
        this.sid=sid;
        this.bname= bname;
        this.rdate=rdate;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}
