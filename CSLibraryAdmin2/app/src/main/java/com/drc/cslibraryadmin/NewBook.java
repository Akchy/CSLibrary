package com.drc.cslibraryadmin;

public class NewBook {
    private String name;
    private String author;
    private String quan,avail;

    public NewBook() {
    }

    public String getAvail() {
        return avail;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public NewBook(String name, String author, String quan, String avail) {
        this.name = name;
        this.author = author;
        this.quan = quan;
        this.avail = avail;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
