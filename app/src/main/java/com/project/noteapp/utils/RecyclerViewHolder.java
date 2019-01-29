package com.project.noteapp.utils;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.noteapp.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private ImageView thumbnail;
    private TextView title;
    private TextView optionsMenu;

    public RecyclerViewHolder(View listItemView) {
        super(listItemView);
        this.thumbnail = listItemView.findViewById(R.id.list_item_thumbnail);
        this.title = listItemView.findViewById(R.id.list_item_text);
        this.optionsMenu = listItemView.findViewById(R.id.textViewOptions);
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail.setImageBitmap(thumbnail);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setOptionsMenu(TextView optionsMenu) {
        this.optionsMenu = optionsMenu;
    }

    public ImageView getThumbnail() {
        return this.thumbnail;
    }

    public TextView getTitle() {
        return this.title;
    }

    public TextView getOptionsMenu() {
        return this.optionsMenu;
    }
}