package com.drc.cslibraryadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLUE;


public class RecyclerViewAdapterFineList extends RecyclerView.Adapter<RecyclerViewAdapterFineList.ViewHolder> {

    Context context;
    List<NewFine> MainImageUploadInfoList;
    int amt;

    public RecyclerViewAdapterFineList(Context context, List<NewFine> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fine_list_recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final NewFine UploadInfo = MainImageUploadInfoList.get(position);


        holder.StudName.setText(UploadInfo.getBname());


        holder.amt.setText(UploadInfo.getAmt() + "/-");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                        .setTitle("Fine")
                        .setMessage("Student name: "+ holder.StudName.getText().toString()
                                + "\nStudent ID: " +  UploadInfo.getSid()
                                + "\nBook Name: " +  UploadInfo.getBname()
                                + "\nReturn Date: " +  UploadInfo.getRdate()
                                + "\nAmount: " + holder.amt.getText().toString())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                myQuittingDialogBox.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        myQuittingDialogBox.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(BLUE);
                        myQuittingDialogBox.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(BLUE);
                    }
                });
                myQuittingDialogBox.show();
            }
        });



    }
    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView StudName;
        public TextView amt;

        public ViewHolder(View itemView) {
            super(itemView);


            amt = (TextView) itemView.findViewById(R.id.amt);

            StudName = (TextView) itemView.findViewById(R.id.StudName);
        }
    }

    public void updateList(List<NewFine> newList)
    {
        MainImageUploadInfoList = new ArrayList<>();
        MainImageUploadInfoList.addAll(newList);
        notifyDataSetChanged();
    }
}