package com.drc.cslibraryadmin;

public class LibRecyclerViewItem {

    // Save car name.
    private String libName;

    // Save car image resource id.
    private int libImageId;

    public LibRecyclerViewItem(String libName, int libImageId) {
        this.libName = libName;
        this.libImageId = libImageId;
    }

    public String getLibName() {
        return libName;
    }

    public void setCarName(String libName) {
        this.libName = libName;
    }

    public int getLibImageId() {
        return libImageId;
    }

    public void setLibImageId(int libImageId) {
        this.libImageId = libImageId;
    }
}