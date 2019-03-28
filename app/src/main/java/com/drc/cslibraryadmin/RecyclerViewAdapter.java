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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLUE;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    String name1;
    DatabaseReference databaseReference;
    List<NewBook> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, List<NewBook> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final NewBook UploadInfo = MainImageUploadInfoList.get(position);



        databaseReference = FirebaseDatabase.getInstance().getReference("Stud");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
          //      name1 = snapshot.child(UploadInfo.getStudID()).child("name").getValue(String.class);
                holder.sname.setText(name1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //Toast.makeText(context,UploadInfo.getStudID(),Toast.LENGTH_SHORT).show();
        holder.Name.setText(UploadInfo.getName() );
        holder.fees.setText(UploadInfo.getAuthor());
      //  holder.date.setText(UploadInfo.getRetdate());
        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                        //set message, title, and icon
                        .setTitle("Return")
                        .setMessage("Do you want to Return "+ holder.Name.getText().toString()+"\n\nIssued By: "+name1+"\nStudent Reg No: ")//+UploadInfo.getStudID()+"\nReturn Date: "+UploadInfo.getRetdate() )
                        .setPositiveButton("Return", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code

                                //Toast.makeText(context,UploadInfo.getBookID(),Toast.LENGTH_SHORT).show();

                              //  FirebaseDatabase.getInstance().getReference().child("Book").child(UploadInfo.getBookID()).child("retdate").setValue("Nill");
                              //  FirebaseDatabase.getInstance().getReference().child("Book").child(UploadInfo.getBookID()).child("studID").setValue("Nill");
                              //  FirebaseDatabase.getInstance().getReference().child("Stud").child(UploadInfo.getStudID()).child("bookID").setValue("Nill");
                                Toast.makeText(context,"Book Returned",Toast.LENGTH_SHORT).show();
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


        //Loading image from Glide library.

    }



    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Name,sname;
        public TextView fees,date;
        public Button down;

        public ViewHolder(View itemView) {
            super(itemView);


            fees = (TextView) itemView.findViewById(R.id.Author);
            date = (TextView) itemView.findViewById(R.id.Date);
            down = (Button) itemView.findViewById(R.id.down);
            sname = (TextView) itemView.findViewById(R.id.Sname);
            Name = (TextView) itemView.findViewById(R.id.Name);
        }
    }

    public void updateList(List<NewBook> newList)
    {
        MainImageUploadInfoList = new ArrayList<>();
        MainImageUploadInfoList.addAll(newList);
        notifyDataSetChanged();
    }
}