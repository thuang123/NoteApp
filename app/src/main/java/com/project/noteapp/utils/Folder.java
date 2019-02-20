package com.project.noteapp.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.project.noteapp.MainActivity;
import com.project.noteapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Folder extends ListItem {
    private Activity activity;
    public Folder(File file, Activity activity) {
        super(file);
        this.activity = activity;
    }

    @Override
    public void clicked(Context thatContext) {
        if(activity instanceof MainActivity) {
            ((MainActivity)activity).openFolderFragment();
        }
    }

    @Override
    public void showThumbnail(ImageView thumbnail) {
        Picasso.get().load(R.drawable.ic_bluefolder).resize(50,50).into(thumbnail);

    }


}