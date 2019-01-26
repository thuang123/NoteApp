package com.project.noteapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera {

    private File storageDir;
    private Activity activity;
    private PackageManager packageManager;

    private static final int REQUEST_OPEN = 99;

    public Camera(Activity activity, PackageManager packageManager, File storageDir) {
        this.activity = activity;
        this.packageManager = packageManager;
        this.storageDir = storageDir;
    }

    /**
     * Creates a new file for an image to be saved into within the device's ../Picture/NoteApp directory.
     * @params the currentLocation of activity fragment specifying the folder in which the captured image
     * is to be placed into.  Appended at the end of file name as an identifier.
     * @return File with corresponding name uniquely identified with date and time.
     */
    public File createImageFile(String currentLocation) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = new File(this.storageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + "_" + currentLocation + ".jpg");
        return image;
    }

    /**
     * Creates a new intent to for image capture with device's camera.
     * Utilizes ScanLibrary external library for scan functionality.
     */
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(this.activity, ScanActivity.class);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            int preference = ScanConstants.OPEN_CAMERA;
            takePictureIntent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
            this.activity.startActivityForResult(takePictureIntent, REQUEST_OPEN);
        }
    }

}
