package com.drc.cslibraryadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class LibRecyclerViewDataAdapter extends RecyclerView.Adapter<LibRecyclerViewItemHolder> {

    private List<LibRecyclerViewItem> libItemList;

    public LibRecyclerViewDataAdapter(List<LibRecyclerViewItem> carItemList) {
        this.libItemList = carItemList;
    }

    @Override
    public LibRecyclerViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View carItemView = layoutInflater.inflate(R.layout.activity_card_view_item, parent, false);

        // Get car title text view object.
        final TextView carTitleView = (TextView)carItemView.findViewById(R.id.card_view_image_title);
        // Get car image view object.
        final ImageView carImageView = (ImageView)carItemView.findViewById(R.id.card_view_image);
        // When click the image.
        carImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get car title text.
                String name = carTitleView.getText().toString();
                // Create a snackbar and show it.
               if(name.equals("Books")){
                   Context context = v.getContext();
                   Intent intent = new Intent(context, BookList.class);
                   context.startActivity(intent);
               }

                if(name.equals("Students")){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, StudList.class);
                    context.startActivity(intent);
                }

                if(name.equals("Issue")){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Issue.class);
                    context.startActivity(intent);
                }

                if(name.equals("Return")){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Return.class);
                    context.startActivity(intent);
                }
                if(name.equals("History")){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, History.class);
                    context.startActivity(intent);
                }
                if(name.equals("Fine")){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Fine.class);
                    context.startActivity(intent);
                }
                if(name.equals("Settings")){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Setting.class);
                    context.startActivity(intent);
                }
                if(name.equals("About Us")){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, AboutUs.class);
                    context.startActivity(intent);
                }
                if(name.equals("User Settings")){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, UserSettingsActivity.class);
                    context.startActivity(intent);
                }

            }
        });

        // Create and return our custom lib Recycler View Item Holder object.
        LibRecyclerViewItemHolder ret = new LibRecyclerViewItemHolder(carItemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(LibRecyclerViewItemHolder holder, int position) {
        if(libItemList!=null) {
            // Get car item dto in list.
            LibRecyclerViewItem carItem = libItemList.get(position);

            if(carItem != null) {
                // Set lib item title.
                holder.getLibTitleText().setText(carItem.getLibName());
                // Set lib image resource id.
                holder.getLibImageView().setImageResource(carItem.getLibImageId());
            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(libItemList!=null)
        {
            ret = libItemList.size();
        }
        return ret;
    }
}