package com.project.noteapp.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

    private ImageView thumbnail;
    public TextView title;

    public ViewHolder(ImageView thumbnail, TextView title) {
        this.thumbnail = thumbnail;
        this.title = title;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail.setImageBitmap(thumbnail);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public ImageView getThumbnail() {
        return this.thumbnail;
    }

    public TextView getTitle() {
        return this.title;
    }
}