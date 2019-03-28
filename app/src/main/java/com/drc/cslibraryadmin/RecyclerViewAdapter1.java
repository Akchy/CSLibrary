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
import java.util.jar.Attributes;

import static android.graphics.Color.BLUE;


public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder> {

    Context context;
    List<NewID> MainImageUploadInfoList;

    DatabaseReference databaseReference;

    public RecyclerViewAdapter1(Context context, List<NewID> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rem_recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewID UploadInfo = MainImageUploadInfoList.get(position);

       // Toast.makeText(context,UploadInfo.getId(),Toast.LENGTH_SHORT).show();

        holder.Name.setText(UploadInfo.getId());


        holder.date.setText(UploadInfo.getDate());

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
                                if(UploadInfo.getDate().equals("Nill")) {
                                    databaseReference = FirebaseDatabase.getInstance().getReference("Book");

                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                try {
                                                    if (postSnapshot.getKey().equals(UploadInfo.getPar())) {

                                                        FirebaseDatabase.getInstance().getReference("Book").child(UploadInfo.getPar()).child("IDs").child(UploadInfo.getKey()).removeValue();
                                                        int tempC = Integer.parseInt(postSnapshot.child("quan").getValue().toString());
                                                        int tempA = Integer.parseInt(postSnapshot.child("avail").getValue().toString());
                                                        if(tempC == 1){
                                                            FirebaseDatabase.getInstance().getReference("Book").child(UploadInfo.getPar()).removeValue();
                                                            FirebaseDatabase.getInstance().getReference("ID").child(UploadInfo.getId()).removeValue();
                                                            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                                            SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                                            String currentTime = sdftime.format(new Date());
                                                            String CurrentDate = sdfdate.format(new Date());
                                                            FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                                                    .setValue("Removed Book: "+ UploadInfo.getPar());
                                                        }
                                                        else {
                                                            FirebaseDatabase.getInstance().getReference("Book").child(UploadInfo.getPar()).child("quan").setValue(String.valueOf(tempC - 1));
                                                            FirebaseDatabase.getInstance().getReference("Book").child(UploadInfo.getPar()).child("avail").setValue(String.valueOf(tempA - 1));
                                                            FirebaseDatabase.getInstance().getReference("ID").child(UploadInfo.getId()).removeValue();
                                                            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                                            SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                                            String currentTime = sdftime.format(new Date());
                                                            String CurrentDate = sdfdate.format(new Date());
                                                            FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                                                    .setValue("Removed ID: "+ UploadInfo.getId() + "of Book: "+UploadInfo.getPar());
                                                        }
                                                    }
                                                }
                                                catch (NullPointerException c){

                                                }
                                            }

                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    FirebaseDatabase.getInstance().getReference().child("Book").child(UploadInfo.getPar()).child("IDs").child("key").removeValue();
                                    Toast.makeText(context,"Book Removed",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(context,"Book Not Available",Toast.LENGTH_SHORT).show();
                                }
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
        public TextView date;
        public Button del;

        public ViewHolder(View itemView) {
            super(itemView);


            date = (TextView) itemView.findViewById(R.id.Date);
            del = (Button) itemView.findViewById(R.id.del);

            Name = (TextView) itemView.findViewById(R.id.Name);
        }
    }

    public void updateList(List<NewID> newList)
    {
        MainImageUploadInfoList = new ArrayList<>();
        MainImageUploadInfoList.addAll(newList);
        notifyDataSetChanged();
    }
}