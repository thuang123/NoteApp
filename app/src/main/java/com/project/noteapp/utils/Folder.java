package com.project.noteapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.project.noteapp.ImageViewerActivity;

import java.io.File;

public class Folder extends ListItem {

    public Folder(File file) {
        super(file);
    }

    @Override
    public void clicked(Context thatContext) {
        Intent intent = new Intent(thatContext, ImageViewerActivity.class);
        intent.setData(Uri.fromFile(this.getFile()));
        thatContext.startActivity(intent);

    }


}