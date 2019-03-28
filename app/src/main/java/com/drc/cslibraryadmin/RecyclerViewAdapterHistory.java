package com.drc.cslibraryadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.BLUE;


public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewAdapterHistory.ViewHolder> {

    Context context;
    List<NewHis> MainImageUploadInfoList;

    DatabaseReference databaseReference;

    public RecyclerViewAdapterHistory(Context context, List<NewHis> TempList) {

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