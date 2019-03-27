package com.drc.cslibraryadmin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapterGenHistory extends RecyclerView.Adapter<RecyclerViewAdapterGenHistory.ViewHolder> {

    Context context;
    List<NewHis> MainImageUploadInfoList;

    DatabaseReference databaseReference;

    public RecyclerViewAdapterGenHistory(Context context, List<NewHis> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.his_recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewHis UploadInfo = MainImageUploadInfoList.get(position);

       // Toast.makeText(context,UploadInfo.getId(),Toast.LENGTH_SHORT).show();

        holder.Name.setText(UploadInfo.getVal());


        holder.date.setText(UploadInfo.getDate());

        holder.time.setText(UploadInfo.getTime());


        //Loading image from Glide library.

    }




    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Name;
        public TextView date,time;


        public ViewHolder(View itemView) {
            super(itemView);


            date = (TextView) itemView.findViewById(R.id.Date);

            Name = (TextView) itemView.findViewById(R.id.Name);


            time = (TextView) itemView.findViewById(R.id.time);
        }
    }

    public void updateList(List<NewHis> newList)
    {
        MainImageUploadInfoList = new ArrayList<>();
        MainImageUploadInfoList.addAll(newList);
        notifyDataSetChanged();
    }
}