package com.project.noteapp.utils;

public abstract class ListItem {

    private String fileName;

    public ListItem(String fileName) {

        this.fileName = fileName;
    }

    public void setTitle(String fileName) {
        this.fileName = fileName;
    }
/*
    public Bitmap getThumbnail() {
        return this.imageThumbnail;
    }
*/

    public String getFileName() {
        return this.fileName;
    }
}
