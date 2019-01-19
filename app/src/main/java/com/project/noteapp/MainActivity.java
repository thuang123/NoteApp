package com.project.noteapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.project.noteapp.utils.ListAdapter;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private final static int REQUEST_CODE = 99;

    private Camera appCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "started successfully. <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        final Button cameraButton = findViewById(R.id.CameraButton);
        this.appCamera = new Camera(this, getPackageManager());
        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                appCamera.dispatchTakePictureIntent();
            }
        });

        // Find NoteApp image files
        String noteAppPicturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/NoteApp";
        File directory = new File(noteAppPicturePath);
        File[] files = directory.listFiles();
        if (files != null && files.length != 0) {
            ArrayList<File> data = new ArrayList<>(Arrays.asList(files));
            ListView listView = findViewById(R.id.listview);
            listView.setAdapter(new ListAdapter(this, R.layout.list_item, data));
        } else {
            System.out.println("Data was empty <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            try {
                File photoFile = appCamera.createImageFile();
                Bitmap bitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Save scanned bitmap data into created image file
                FileOutputStream outStream = new FileOutputStream(photoFile);
                bitMap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
                // Cleanup temporary data
                getContentResolver().delete(uri, null, null);
                System.out.println("Photo was successfully saved into: >>>>>>>>>>>>>>>>>>>>>> " + Uri.fromFile(photoFile).toString());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
