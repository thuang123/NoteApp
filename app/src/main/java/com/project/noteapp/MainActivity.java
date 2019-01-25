package com.project.noteapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.project.noteapp.utils.RecycleAdapter;
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

        final ImageButton cameraButton = findViewById(R.id.camera_button);
        this.appCamera = new Camera(this, getPackageManager());
        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                appCamera.dispatchTakePictureIntent();
            }
        });

        // Find NoteApp image files
        new ApplicationPathDataRetrievalTask(this, null, (RecyclerView) findViewById(R.id.recyclerview)).execute();
    }

    /**
     * Overrides default onActivityResult() method to retrieve image data from application's camera capture.
     * Saves temporary image capture data into a new file stored into device's ../Picture/NoteApp directory.
     * Updates application data ListAdapter view for display new captured data.
     */
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
                // Update appData displayed in application
                new ApplicationPathDataRetrievalTask(this, null, (RecyclerView) findViewById(R.id.recyclerview)).execute();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the data found within the device's picture directory inside NoteApp folder and
     * passes data to ListAdapter to create ListView of data.
     */
    private static class ApplicationPathDataRetrievalTask extends AsyncTask<String, Void, ArrayList<File>> {

        private Context context;
        private RecycleAdapter appData;
        private RecyclerView appRecyclerView;

        private ApplicationPathDataRetrievalTask(Context context, RecycleAdapter appData, RecyclerView appRecyclerView) {
            this.context = context;
            this.appData = appData;
            this.appRecyclerView = appRecyclerView;
        }

        @Override
        protected ArrayList<File> doInBackground(String... params) {
            String noteAppPicturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/NoteApp";
            File directory = new File(noteAppPicturePath);
            File[] files = directory.listFiles();
            if (files != null && files.length != 0) {
                return new ArrayList<>(Arrays.asList(files));
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<File> result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                RecyclerView recyclerView = this.appRecyclerView;
                this.appData = new RecycleAdapter(this.context, result);
                recyclerView.setAdapter(this.appData);
            } else {
                System.out.println("Data was empty <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        }
    }
}
