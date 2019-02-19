package com.project.noteapp.utils;

import android.app.Activity;
import android.content.Context;
import com.project.noteapp.MainActivity;

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


}