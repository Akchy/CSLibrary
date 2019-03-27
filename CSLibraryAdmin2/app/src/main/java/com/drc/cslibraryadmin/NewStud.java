package com.drc.cslibraryadmin;

public class NewStud {
    private String name,avail,amount;
    private String reg,count,due;

    public NewStud() {
    }


    public NewStud(String name, String fees, String count,String avail,String amount,String due){
        this.name = name;
        this.reg = fees;
        this.count=count;
        this.avail=avail;
        this.amount=amount;
        this.due=due;
    }


    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAvail() {
        return avail;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

}
