package com.project.noteapp.utils;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

/**
 * Retrieves the data found within the device's picture directory inside NoteApp folder and
 * passes data to ListAdapter to create ListView of data.
 */
public class ApplicationPathDataRetrievalTask extends AsyncTask<String, Void, ArrayList<ListItem>> {

    private Context context;
    private RecycleAdapter appData;
    private RecyclerView appRecyclerView;
    private File storageDirectory;
    private Activity activity;

    public ApplicationPathDataRetrievalTask(Context context, RecycleAdapter appData, RecyclerView appRecyclerView
    , File storageDirectory, Activity activity) {
        this.context = context;
        this.appData = appData;
        this.appRecyclerView = appRecyclerView;
        this.storageDirectory = storageDirectory;
        this.activity = activity;
    }

    /**
     * The main purpose of AsyncTask used here to retrieve NoteApp data from device's picture directory, so
     * the main thread can focus on rendering UI functionality.
     * @returns Array list of all data contained within ../Picture/NoteApp or null if no files are found.
     * This list is passed into onPostExecute() method immediately for processing.
     */
    @Override
    protected ArrayList<ListItem> doInBackground(String... params) {
        String noteAppPicturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/NoteApp";
        File directory = new File(noteAppPicturePath);
        File[] files = directory.listFiles();
        ArrayList<ListItem> list = new ArrayList<>();
        for (File file : files) {
            if(!file.isDirectory()) {
                ListItem item = new ImageFile(file);
                list.add(item);
            } else {
                ListItem item = new Folder(file, this.activity);
                list.add(item);
            }
        }
        if (list != null && list.size() != 0) {
            return list;
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
    protected void onPostExecute(ArrayList<ListItem> result) {
        super.onPostExecute(result);
        if (result != null && !result.isEmpty()) {
            RecyclerView recyclerView = this.appRecyclerView;
            this.appData = new RecycleAdapter(this.context, result, storageDirectory, this.activity);
            recyclerView.setAdapter(this.appData);
        } else {
            System.out.println("Data was empty <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

}