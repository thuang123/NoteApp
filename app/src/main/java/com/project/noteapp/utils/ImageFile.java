package com.project.noteapp.utils;

import android.graphics.Bitmap;

public class ImageFile {

    private Bitmap imageThumbnail;
    private String imageFileName;

    public ImageFile(Bitmap thumbnail, String fileName) {
        this.imageThumbnail = thumbnail;
        this.imageFileName = fileName;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.imageThumbnail = thumbnail;
    }

    public void setTitle(String fileName) {
        this.imageFileName = fileName;
    }

    public Bitmap getThumbnail() {
        return this.imageThumbnail;
    }

    public String getFileName() {
        return this.imageFileName;
    }
}
