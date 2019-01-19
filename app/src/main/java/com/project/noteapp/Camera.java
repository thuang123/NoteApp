package com.project.noteapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera {

    private File storageDir;
    private Activity activity;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private PackageManager packageManager;

    public Camera(Activity activity, PackageManager packageManager, File storageDir) {
        this.activity = activity;
        this.packageManager = packageManager;
        this.storageDir = storageDir;
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity,"com.project.noteapp.fileprovider",photoFile));
            System.out.println("Photo saved into: >>>>>>>>>>>>>>>>>>>>>> " + Uri.fromFile(photoFile).toString());
            this.activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = new File(this.storageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
