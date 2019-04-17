package com.drc.cslibraryadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.BLUE;


public class RecyclerViewAdapterStudList extends RecyclerView.Adapter<RecyclerViewAdapterStudList.ViewHolder> {

    Context context;
    List<NewStud> MainImageUploadInfoList;

    String id;



    public RecyclerViewAdapterStudList(Context context, List<NewStud> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stud_recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewStud UploadInfo = MainImageUploadInfoList.get(position);

        holder.Name.setText(UploadInfo.getName() );
        holder.reg.setText(UploadInfo.getReg());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete "+ holder.Name.getText().toString() )
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code

                                SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                String currentTime = sdftime.format(new Date());
                                String CurrentDate = sdfdate.format(new Date());
                                FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                        .setValue("Deleted Student: "+ holder.Name.getText().toString() +" with Reg ID: "+holder.reg.getText().toString() + " by " + UploadInfo.getUser());

                                FirebaseDatabase.getInstance().getReference().child("Stud").child(UploadInfo.getReg()).removeValue();
                                Toast.makeText(context,"Student Removed",Toast.LENGTH_SHORT).show();
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

                //               Toast.makeText(context,"2",Toast.LENGTH_SHORT).show();
            }
        });


        //Loading image from Glide library.

    }



    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Name;
        public TextView reg;
        public Button del;

        public ViewHolder(View itemView) {
            super(itemView);


            reg = (TextView) itemView.findViewById(R.id.Reg);
            Name = (TextView) itemView.findViewById(R.id.SName);
            del=(Button)itemView.findViewById(R.id.del);
        }
    }

    public void updateList(List<NewStud> newList)
    {
        MainImageUploadInfoList = new ArrayList<>();
        MainImageUploadInfoList.addAll(newList);
        notifyDataSetChanged();
    }


}