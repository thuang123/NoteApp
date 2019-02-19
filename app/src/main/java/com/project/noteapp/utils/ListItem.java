package com.project.noteapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public abstract class ListItem {

    private String fileName;
    private File file;

    public ListItem(File file) {
        this.file = file;
        this.fileName = file.getName();

    }

    public File getFile() {
        return this.file;
    }

    public void setTitle(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public abstract void clicked(Context thatContext);

    public void deleteListItem(Context context, RecycleAdapter adapter, List<File> objects , int position) {
        boolean deleteSuccess = file.delete();
        if (deleteSuccess) {
            Toast.makeText(context, "File was successfully deleted!",
                    Toast.LENGTH_SHORT).show();
            objects.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, objects.size());
        } else {
            Toast.makeText(context, "Failed to delete file.",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
