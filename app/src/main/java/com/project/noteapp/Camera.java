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
    static final int REQUEST_TAKE_PHOTO = 1;
    private PackageManager packageManager;

    private static final int REQUEST_OPEN = 99;

    public Camera(Activity activity, PackageManager packageManager) {
        this.activity = activity;
        this.packageManager = packageManager;
        this.storageDir = getApplicationStorageDirectory();
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (this.storageDir == null) {
            this.storageDir = getApplicationStorageDirectory();
        }
        File image = new File(this.storageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
        return image;
    }

    /** Create a File for saving an image on device **/
    private static File getApplicationStorageDirectory(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "NoteApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("NoteApp", "failed to create directory");
                return null;
            }
        }
        return mediaStorageDir;
    }

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
