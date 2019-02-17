package com.project.noteapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.File;

public abstract class ListItem {

    private String fileName;
    private File file;

    public ListItem(File file) {
        this.file = file;
        this.fileName = file.getName();

    }

    public File getFile() {
        return this.file;
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

    public abstract void clicked(Context thatContext);

    public void delete(Context context) {

    }
}
