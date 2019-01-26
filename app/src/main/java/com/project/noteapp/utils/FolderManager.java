package com.project.noteapp.utils;

import java.io.File;

public class FolderManager {

    private File storageDir;

    public FolderManager(File storageDir) {
        this.storageDir = storageDir;
    }

    public File createNewFolder(String folderName) {
        File folder = new File(this.storageDir.getPath() + File.separator +
                folderName + ".txt");
        return folder;
    }
}
