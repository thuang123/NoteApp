package com.project.noteapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.project.noteapp.utils.FolderManager;
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
    private String currentLocation;

    private FolderManager folderManager;
    private String newFolderName;

    private DrawerLayout drawerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "started successfully. <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        // Initial startup in "main" folder
        this.currentLocation = "main";
        this.folderManager = new FolderManager(getApplicationStorageDirectory());

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        final ImageButton cameraButton = findViewById(R.id.camera_button);
        this.appCamera = new Camera(this, getPackageManager(), getApplicationStorageDirectory());
        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                appCamera.dispatchTakePictureIntent();
            }
        });

        drawerlayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerlayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_hamburger);

        // Find NoteApp image files
        new ApplicationPathDataRetrievalTask(this, null, (RecyclerView) findViewById(R.id.recyclerview)).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Specifies Menu option functionality
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        switch (id) {
            //Opens up drawer when hamburger icon is pressed
            case android.R.id.home:
                drawerlayout.openDrawer(GravityCompat.START);
                return true;


            // New folder functionality
            case R.id.menu_new_folder:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Specify a new folder name:");
                builder.setPositiveButton(android.R.string.ok, null);
                // Set up the input
                final EditText input = new EditText(this);
                final AlertDialog alertDialog = builder.create();
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // Set up the buttons
                final Context that = this;
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("pressed","pressed");
                        String inputString = input.getText().toString();
                        if (inputString != null || !inputString.equals("")) {
                            newFolderName = inputString;
                            folderManager.createNewFolder(newFolderName);
                            newFolderName = null;
                            alertDialog.dismiss();
                            new ApplicationPathDataRetrievalTask(that, null, (RecyclerView) findViewById(R.id.recyclerview)).execute();
                        }
                    }
                });

/**
                if (newFolderName != null && !newFolderName.equals("")) {
                    this.folderManager.createNewFolder(newFolderName);
                    newFolderName = null;
                }*/
                builder.show();

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Overrides default onActivityResult() method to retrieve image data from application's camera capture.
     * Saves temporary image capture data into a new file stored into device's ../Picture/NoteApp directory.
     * Updates application data ListAdapter view for display new captured data by calling new ApplicationPathDataRetrievalTask().
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            try {
                File photoFile = appCamera.createImageFile(this.currentLocation);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes and returns the NoteApp data directory under device's main Picture directory
     * for application data storage.
     * Returns existing directory if one already exists.
     *
     * @return File pointing to device's ../Picture/NoteApp directory
     */
    private static File getApplicationStorageDirectory() {
        String currentExternalStorageState = Environment.getExternalStorageState();
        if (!currentExternalStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY) && !currentExternalStorageState.equals(Environment.MEDIA_UNMOUNTED)) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "NoteApp");
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("NoteApp", "failed to create directory");
                    return null;
                }
            }
            return mediaStorageDir;
        } else {
            Log.d("NoteApp", "application storage directory retrieval failed.  Either media is READ only or SD is unmounted.");
            return null;
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

        /**
         * The main purpose of AsyncTask used here to retrieve NoteApp data from device's picture directory, so
         * the main thread can focus on rendering UI functionality.
         * @returns Array list of all data contained within ../Picture/NoteApp or null if no files are found.
         * This list is passed into onPostExecute() method immediately for processing.
         */
        @Override
        protected ArrayList<File> doInBackground(String... params) {
            String noteAppPicturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/NoteApp";
            File directory = new File(noteAppPicturePath);
            File[] files = directory.listFiles();
            if (files != null && files.length != 0) {
                return new ArrayList<>(Arrays.asList(files));
            } else {
                return null;
            }
        }

        /**
         * @param result array list containing all data contained within device's ../Picture/NoteApp directory.
         * Processes application data by passing data to a new RecycleAdapter that setups View objects and attaches listeners.
         * RecycleAdapter is then attached to application's recyclerView which is contains the list data seen in application's MainActivity.
         */
        @Override
        protected void onPostExecute(ArrayList<File> result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                RecyclerView recyclerView = this.appRecyclerView;
                this.appData = new RecycleAdapter(this.context, result, getApplicationStorageDirectory());
                recyclerView.setAdapter(this.appData);
            } else {
                System.out.println("Data was empty <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        }
    }
}
