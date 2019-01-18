package com.project.noteapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.project.noteapp.utils.ImageFile;
import com.project.noteapp.utils.ListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //private final int THUMBNAIL_SIZE = 64;
    private final static String TAG = "MainActivity";

    //private ArrayList<ImageFile> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "started successfully. <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        final Button cameraButton = findViewById(R.id.CameraButton);
        final Camera newCamera = new Camera(this, getPackageManager(), getOutputMediaFile());
        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newCamera.dispatchTakePictureIntent();
            }
        });

        // Find NoteApp image files
        String noteAppPicturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/NoteApp";

        File directory = new File(noteAppPicturePath);
        File[] files = directory.listFiles();
        ArrayList<File> data = new ArrayList<>(Arrays.asList(files));
//        if (files != null) {
//            for (int i = 0; i < files.length; i++) {
//                Log.d("Files", "FileName:" + files[i].getName());
//                Bitmap currentThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(files[i].getPath()), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
//                ImageFile imageFile = new ImageFile(currentThumbnail, files[i].getName());
//                data.add(imageFile);
//            }
//        }

        if (!data.isEmpty()) {
            ListView listView = findViewById(R.id.listview);
            listView.setAdapter(new ListAdapter(this, R.layout.list_item, data));
        } else {
            System.out.println("Data was empty <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    /** Create a File for saving an image on device **/
    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "NoteApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("NoteApp", "failed to create directory");
                return null;
            }
        }

        return mediaStorageDir;
    }
}
