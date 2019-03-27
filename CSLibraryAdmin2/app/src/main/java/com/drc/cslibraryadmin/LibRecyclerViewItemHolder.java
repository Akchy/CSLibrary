package com.drc.cslibraryadmin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LibRecyclerViewItemHolder extends RecyclerView.ViewHolder {

    private TextView libTitleText = null;

    private ImageView libImageView = null;

    public LibRecyclerViewItemHolder(View itemView) {
        super(itemView);

        if(itemView != null)
        {
            libTitleText = (TextView)itemView.findViewById(R.id.card_view_image_title);

            libImageView = (ImageView)itemView.findViewById(R.id.card_view_image);
        }
    }

    public TextView getLibTitleText() {
        return libTitleText;
    }

    public ImageView getLibImageView() {
        return libImageView;
    }
}