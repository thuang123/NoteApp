package com.project.noteapp.utils;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Retrieves the data found within the device's picture directory inside NoteApp folder and
 * passes data to ListAdapter to create ListView of data.
 */
public class ApplicationPathDataRetrievalTask extends AsyncTask<String, Void, ArrayList<File>> {

    private Context context;
    private RecycleAdapter appData;
    private RecyclerView appRecyclerView;
    private File storageDirectory;

    public ApplicationPathDataRetrievalTask(Context context, RecycleAdapter appData, RecyclerView appRecyclerView
    , File storageDirectory) {
        this.context = context;
        this.appData = appData;
        this.appRecyclerView = appRecyclerView;
        this.storageDirectory = storageDirectory;
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
            this.appData = new RecycleAdapter(this.context, result, storageDirectory);
            recyclerView.setAdapter(this.appData);
        } else {
            System.out.println("Data was empty <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }
}