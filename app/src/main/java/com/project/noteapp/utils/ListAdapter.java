package com.project.noteapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.noteapp.ImageViewerActivity;
import com.project.noteapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ListAdapter extends ArrayAdapter<File> {

    private int layout;
    private Context context;
    private final int THUMBNAIL_SIZE = 64;

    public ListAdapter(@NonNull Context context, int resource, @NonNull List<File> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewHolder;

        final File currentFile = getItem(position);
        Bitmap currentThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(currentFile.getPath()), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        ImageFile imageFile = new ImageFile(currentThumbnail, currentFile.getName());

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder((ImageView) convertView.findViewById(R.id.list_item_thumbnail), (TextView) convertView.findViewById(R.id.list_item_text));

            final Context that = this.context;
            View.OnClickListener openImageListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(that, ImageViewerActivity.class);
                    intent.setData(Uri.fromFile(currentFile));
                    that.startActivity(intent);
                }
            };
            viewHolder.getTitle().setOnClickListener(openImageListener);
            viewHolder.getThumbnail().setOnClickListener(openImageListener);

            convertView.setTag(viewHolder);
        }
        mainViewHolder = (ViewHolder) convertView.getTag();
        mainViewHolder.setTitle(imageFile.getFileName());
        Picasso.get().load(currentFile).fit().into(mainViewHolder.getThumbnail());

        return convertView;
    }
}