package com.drc.cslibraryadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLUE;


public class RecyclerViewAdapterFine extends RecyclerView.Adapter<RecyclerViewAdapterFine.ViewHolder> {

    Context context;
    List<NewFine> MainImageUploadInfoList;
    int amt;

    public RecyclerViewAdapterFine(Context context, List<NewFine> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fine_recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final NewFine UploadInfo = MainImageUploadInfoList.get(position);


        holder.StudName.setText(UploadInfo.getName());


        holder.amt.setText(UploadInfo.getAmt() + "/-");

        holder.del.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

           final AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                .setTitle("Fine")
                .setMessage("Settle the fine of "+ holder.StudName.getText().toString()
                        + "\nStudent ID: " +  UploadInfo.getSid()
                + "\nAmount: " + holder.amt.getText().toString())
                .setPositiveButton("Settle", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                       // if(UploadInfo.getStudID().equals("Nill")) {
                        FirebaseDatabase.getInstance().getReference("Stud").child(UploadInfo.getSid()).child("due").setValue("0");
                        FirebaseDatabase.getInstance().getReference("Stud").child(UploadInfo.getSid()).child("amount").setValue("0");
                       /*     Toast.makeText(context,"Book Removed",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context,"Book Not Available",Toast.LENGTH_SHORT).show();
                        }*/

                       dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
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
        public Button del;

        public ViewHolder(View itemView) {
            super(itemView);


            amt = (TextView) itemView.findViewById(R.id.amt);
            del = (Button) itemView.findViewById(R.id.del);

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