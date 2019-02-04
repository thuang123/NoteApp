package com.project.noteapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.project.noteapp.ImageViewerActivity;
import com.project.noteapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final int THUMBNAIL_SIZE = 64;

    private LayoutInflater inflater;
    private Context context;
    private List<File> objects;
    private final File storageDir;

    public RecycleAdapter(@NonNull Context context, @NonNull List<File> objects, File storageDir) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.objects = objects;
        this.storageDir = storageDir;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        view.setTag(holder);
        return holder;
    }

    /**
     * Processes all the NoteApp data by attaching listeners and placing file data into views.  This method is called
     * within an iteration through all the this.objects data.
     * @param position which is the index of the current this.objects list being iterated through.
     * @param holder which is a single list view object assigned to hold the image and text data that will be displayed
     *               in the application.
     */
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final File currentFile = objects.get(position);
        Bitmap currentThumbnail;
        if(!currentFile.isDirectory()) {
            currentThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(currentFile.getPath())
                    , THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        } else {
            currentThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(context.getResources()
                    ,R.drawable.camera_icon), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        }

        ImageFile imageFile = new ImageFile(currentThumbnail, currentFile.getName());
        holder.setTitle(imageFile.getFileName());

        Picasso.get().load(currentFile).fit().into(holder.getThumbnail());

        final Context thatContext = this.context;
        final RecycleAdapter that = this;
        View.OnClickListener openImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thatContext, ImageViewerActivity.class);
                intent.setData(Uri.fromFile(currentFile));
                thatContext.startActivity(intent);
            }
        };
        holder.getTitle().setOnClickListener(openImageListener);
        holder.getThumbnail().setOnClickListener(openImageListener);

        final TextView thatOptionsMenu = holder.getOptionsMenu();
        View.OnClickListener optionsMenuListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(thatContext, thatOptionsMenu);
                popup.inflate(R.menu.options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_option:
                                // TODO: Delete option functionality
                                break;
                            case R.id.convert_text_option:
                                String filePath = currentFile.getPath();
                                Bitmap bitMap = BitmapFactory.decodeFile(filePath);
                                TextExtractor textExtractor = new TextExtractor(thatContext, that.storageDir);
                                textExtractor.generateTextFileFromImage(bitMap);
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        };
        holder.getOptionsMenu().setOnClickListener(optionsMenuListener);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}