package com.drc.cslibraryadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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


public class RecyclerViewAdapterUserList extends RecyclerView.Adapter<RecyclerViewAdapterUserList.ViewHolder> {

    Context context;
    List<NewBook> MainImageUploadInfoList;
    DatabaseReference databaseReference;
    DatabaseReference ref;
    ProgressDialog progressDialog;




    public RecyclerViewAdapterUserList(Context context, List<NewBook> TempList) {

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
        final NewBook UploadInfo = MainImageUploadInfoList.get(position);

        holder.Name.setText(UploadInfo.getName() );
        holder.Uname.setText(UploadInfo.getAuthor());


        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setTitle("Delete User");
                View viewInflated = LayoutInflater.from(context).inflate(R.layout.user_list, null);
                builder.setView(viewInflated);


                final EditText user = (EditText) viewInflated.findViewById(R.id.del_user);
                final EditText pass = (EditText) viewInflated.findViewById(R.id.del_pass);
                final TextView text = (TextView) viewInflated.findViewById(R.id.text);

                text.setText("Please enter the Username and Password of the User to delete");

                builder.setCancelable(false)
                        .setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String uname=user.getText().toString();
                        String password=pass.getText().toString();

                        if(uname.isEmpty() || password.isEmpty()){
                            Toast.makeText(context,"These Fields cannot be empty",Toast.LENGTH_SHORT).show();
                        }
                        else{

                            progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();

                            ref = FirebaseDatabase.getInstance().getReference("users");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(uname)) {
                                        if(dataSnapshot.child(uname).child("pass").getValue().toString().equals(password)
                                        && !dataSnapshot.child(uname).child("name").getValue().toString().equals(UploadInfo.getName())) {
                                            ref.child(uname).removeValue();


                                            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                            SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                            String currentTime = sdftime.format(new Date());
                                            String CurrentDate = sdfdate.format(new Date());
                                            FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                                    .setValue("User  "+uname+" deleted by " + UploadInfo.getUser());


                                            Toast.makeText(context, "User " + uname + " deleted.", Toast.LENGTH_SHORT).show();
                                            progressDialog.cancel();

                                        }
                                        else if(dataSnapshot.child(uname).child("name").getValue().toString().equals(UploadInfo.getName())){
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Cannot Delete Current User", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                        else {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Username and Doesn't Match", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    } else {

                                        progressDialog.dismiss();
                                        Toast.makeText(context,"User Not Found",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                    dialog.dismiss();
                                }
                            });


                        }

                        Boolean wantToCloseDialog = false;
                        //Do stuff, possibly set wantToCloseDialog to true then...
                        if(wantToCloseDialog)
                            progressDialog.dismiss();
                        dialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });





            }
        });





    }




    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Name;
        public TextView Uname;
        public Button del;

        public ViewHolder(View itemView) {
            super(itemView);


            Uname = (TextView) itemView.findViewById(R.id.SName);
            Name = (TextView) itemView.findViewById(R.id.Reg);
            del=(Button)itemView.findViewById(R.id.del);
        }
    }

    public void updateList(List<NewBook> newList)
    {
        MainImageUploadInfoList = new ArrayList<>();
        MainImageUploadInfoList.addAll(newList);
        notifyDataSetChanged();
    }


}