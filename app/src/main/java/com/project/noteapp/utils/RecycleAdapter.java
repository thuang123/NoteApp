package com.project.noteapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.project.noteapp.ImageViewerActivity;
import com.project.noteapp.MainActivity;
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

        //Temporary
        if(!currentFile.isDirectory()) {
            currentThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(currentFile.getPath())
                    , THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        } else {
            Drawable d = context.getResources().getDrawable(R.drawable.camera_icon);
            currentThumbnail = ThumbnailUtils.extractThumbnail(((BitmapDrawable)d).getBitmap(), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        }

        ImageFile imageFile = new ImageFile(currentThumbnail, currentFile.getName());
        holder.setTitle(imageFile.getFileName());
        holder.setThumbnail(currentThumbnail);

        //Check if the current file is an directory or an image file, displays the thumbnail image
        if(!currentFile.isDirectory()) {
            Picasso.get().load(currentFile).fit().into(holder.getThumbnail());
        } else {
            Picasso.get().load(R.drawable.ic_bluefolder).resize(50,50).into(holder.getThumbnail());
        }

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

        final int thatPosition = holder.getAdapterPosition();
        final TextView thatOptionsMenu = holder.getOptionsMenu();
        View.OnClickListener optionsMenuListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(thatContext, thatOptionsMenu);
                popup.inflate(R.menu.options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final String filePath = currentFile.getPath();
                        switch (item.getItemId()) {
                            case R.id.delete_option:
                                AlertDialog.Builder builder = new AlertDialog.Builder(thatContext);
                                builder.setTitle("Confirm");
                                builder.setMessage("Are you sure?" + currentFile.getName() + " will be deleted.");
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        File file = new File(filePath);
                                        boolean deleteSuccess = file.delete();
                                        if (deleteSuccess) {
                                            Toast.makeText(thatContext, "File was successfully deleted!",
                                                    Toast.LENGTH_SHORT).show();
                                            objects.remove(thatPosition);
                                            notifyItemRemoved(thatPosition);
                                            notifyItemRangeChanged(thatPosition, objects.size());
                                        } else {
                                            Toast.makeText(thatContext, "Failed to delete file.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                break;
                            case R.id.convert_text_option:
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

        holder.getTitle().setOnClickListener(openImageListener);
        holder.getThumbnail().setOnClickListener(openImageListener);
        holder.getOptionsMenu().setOnClickListener(optionsMenuListener);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}