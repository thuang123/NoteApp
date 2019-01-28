package com.project.noteapp.utils;

import android.util.Log;

import java.io.File;

public class FolderManager {

    private File storageDir;

    public FolderManager(File storageDir) {
        this.storageDir = storageDir;
    }

    public File createNewFolder(String folderName) {
        File folder = new File(this.storageDir.getPath() + File.separator +
                folderName);
        if (!folder.mkdirs()) {
            Log.d("NoteApp", "failed to create directory");
            return null;
        }
        return folder;
    }
}
