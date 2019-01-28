package com.project.noteapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public RecycleAdapter(@NonNull Context context, @NonNull List<File> objects) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        view.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final File currentFile = objects.get(position);
        Bitmap currentThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(currentFile.getPath()), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        ImageFile imageFile = new ImageFile(currentThumbnail, currentFile.getName());
        //Folder folder = new Folder()

        holder.setTitle(imageFile.getFileName());

     //   if(currentFile instanceof Folder) {
     //   }

        Picasso.get().load(currentFile).fit().into(holder.getThumbnail());

        final Context that = this.context;
        View.OnClickListener openImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(that, ImageViewerActivity.class);
                intent.setData(Uri.fromFile(currentFile));
                that.startActivity(intent);
            }
        };
        holder.getTitle().setOnClickListener(openImageListener);
        holder.getThumbnail().setOnClickListener(openImageListener);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}