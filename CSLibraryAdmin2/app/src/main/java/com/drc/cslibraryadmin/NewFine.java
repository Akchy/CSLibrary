package com.drc.cslibraryadmin;

public class NewFine {
    private String name,amt,sid;

    public NewFine() {
    }

    public NewFine(String name, String amt, String parent, String rdate,String sid) {
        this.name = name;
        this.amt = amt;
        this.sid=sid;
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
