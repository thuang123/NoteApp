package com.project.noteapp.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;

import com.project.noteapp.MainActivity;

import java.io.File;

public class Folder extends ListItem {
    private FileStatePagerAdapter mFileStatePagerAdapter;
    private ViewPager mViewPager;
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