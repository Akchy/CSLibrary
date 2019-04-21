package com.drc.cslibraryadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserSettingsActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference ref;
    ProgressDialog progressDialog;
    TextView addusr,chpass,chuname,delusr,username ;
    Button signout;
    String details[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        setTitle("User Settings");

        addusr=(TextView)findViewById(R.id.addusr);
        chpass=(TextView)findViewById(R.id.changepass);
        chuname=(TextView)findViewById(R.id.changeusr);
        delusr=(TextView)findViewById(R.id.delusr);
        database = FirebaseDatabase.getInstance();
        signout = findViewById(R.id.out);
        username = findViewById(R.id.Username);

        try {
            FileInputStream fstream;
            fstream = openFileInput("user_details");
            StringBuffer sbuffer = new StringBuffer();
            int i;
            while ((i = fstream.read())!= -1){
                sbuffer.append((char)i);
            }
            fstream.close();
            details = sbuffer.toString().split("\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        username.append(details[0]);




        addusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingsActivity.this);
                builder.setTitle("Add User");
                View viewInflated = LayoutInflater.from(UserSettingsActivity.this).inflate(R.layout.add_new_user, null);
                builder.setView(viewInflated);


                final EditText duname = (EditText) viewInflated.findViewById(R.id.duname);
                final EditText dname = (EditText) viewInflated.findViewById(R.id.dname);
                final EditText dpass = (EditText) viewInflated.findViewById(R.id.dpass);
                final EditText dpass1 = (EditText) viewInflated.findViewById(R.id.dpass1);

                builder.setCancelable(false)
                        .setPositiveButton("Add User",new DialogInterface.OnClickListener() {
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
                AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String uname1=duname.getText().toString();
                        String name1=dname.getText().toString();
                        String pass2=dpass.getText().toString();
                        String pass3=dpass1.getText().toString();

                        if(uname1.isEmpty() || name1.isEmpty() || pass2.isEmpty() || pass3.isEmpty()){
                            Toast.makeText(getApplicationContext(),"These Fields cannot be empty",Toast.LENGTH_SHORT).show();
                        }
                        else{

                            if(pass2.equals(pass3)) {
                                progressDialog = new ProgressDialog(UserSettingsActivity.this);
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();

                                ref = database.getReference("users");
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(uname1)) {
                                            Toast.makeText(getApplicationContext(), "Username Already Exists", Toast.LENGTH_SHORT).show();
                                            progressDialog.cancel();
                                        } else {
                                            ref.child(uname1).child("name").setValue(name1);
                                            ref.child(uname1).child("pass").setValue(pass2);
                                            progressDialog.cancel();

                                            try {
                                                FileInputStream fstream;
                                                fstream = openFileInput("user_details");
                                                StringBuffer sbuffer = new StringBuffer();
                                                int i;
                                                while ((i = fstream.read())!= -1){
                                                    sbuffer.append((char)i);
                                                }
                                                details = sbuffer.toString().split("\n");
                                                fstream.close();

                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                            SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                            String currentTime = sdftime.format(new Date());
                                            String CurrentDate = sdfdate.format(new Date());
                                            FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                                    .setValue("New User "+uname1+" is added by " + details[0]);

                                            Toast.makeText(getApplicationContext(),"New User "+uname1+" is added.",Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }else {
                                Toast.makeText(getApplicationContext(),"Passwords Do NOT Match",Toast.LENGTH_SHORT).show();

                            }

                        }

                        Boolean wantToCloseDialog = false;
                        //Do stuff, possibly set wantToCloseDialog to true then...
                        if(wantToCloseDialog)
                            dialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });


            }
        });


        chpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingsActivity.this);
                builder.setTitle("Change Password");
                View viewInflated = LayoutInflater.from(UserSettingsActivity.this).inflate(R.layout.change_pass, null);
                builder.setView(viewInflated);


                final EditText oldpass = (EditText) viewInflated.findViewById(R.id.old_pass);
                final EditText uname = (EditText) viewInflated.findViewById(R.id.uname);
                final EditText newpass = (EditText) viewInflated.findViewById(R.id.new_pass);

                builder.setCancelable(false)
                        .setPositiveButton("Change Password",new DialogInterface.OnClickListener() {
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
                AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String passold=oldpass.getText().toString();
                        String passnew=newpass.getText().toString();
                        String username=uname.getText().toString();

                        if(passold.isEmpty() || passnew.isEmpty() || username.isEmpty()){
                            Toast.makeText(getApplicationContext(),"These Fields cannot be empty",Toast.LENGTH_SHORT).show();
                        }
                        else{

                            if(!passold.equals(passnew)) {
                                progressDialog = new ProgressDialog(UserSettingsActivity.this);
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();

                                ref = database.getReference("users");
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(username)) {
                                            if(dataSnapshot.child(username).child("pass").getValue().toString().equals(passold)) {
                                                ref.child(username).child("pass").setValue(passnew);

                                                try {
                                                    FileInputStream fstream;
                                                    fstream = openFileInput("user_details");
                                                    StringBuffer sbuffer = new StringBuffer();
                                                    int i;
                                                    while ((i = fstream.read())!= -1){
                                                        sbuffer.append((char)i);
                                                    }
                                                    details = sbuffer.toString().split("\n");
                                                    fstream.close();

                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                                SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                                String currentTime = sdftime.format(new Date());
                                                String CurrentDate = sdfdate.format(new Date());
                                                FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                                        .setValue("Password of "+username+" is changed by " + details[0]);

                                                Toast.makeText(getApplicationContext(), "Password of "+ username + " changed", Toast.LENGTH_SHORT).show();
                                                progressDialog.cancel();
                                            }

                                            else
                                                progressDialog.cancel();
                                                Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"User Mismatch",Toast.LENGTH_SHORT).show();
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

        chuname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingsActivity.this);
                builder.setTitle("Change Password");
                View viewInflated = LayoutInflater.from(UserSettingsActivity.this).inflate(R.layout.change_user, null);
                builder.setView(viewInflated);


                final EditText olduname = (EditText) viewInflated.findViewById(R.id.old_uname);
                final EditText newuname = (EditText) viewInflated.findViewById(R.id.new_uname);

                builder.setCancelable(false)
                        .setPositiveButton("Change Username",new DialogInterface.OnClickListener() {
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
                AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String unameold=olduname.getText().toString();
                        String unamenew=newuname.getText().toString();

                        if(unameold.isEmpty() || unamenew.isEmpty()){
                            Toast.makeText(getApplicationContext(),"These Fields cannot be empty",Toast.LENGTH_SHORT).show();
                        }
                        else{

                            if(!unamenew.equals(unameold)) {
                                progressDialog = new ProgressDialog(UserSettingsActivity.this);
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();

                                ref = database.getReference("users");
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(unameold)) {

                                            String p = dataSnapshot.child(unameold).child("pass").getValue().toString();
                                            String n = dataSnapshot.child(unameold).child("name").getValue().toString();
                                            ref.child(unameold).removeValue();
                                            ref.child(unamenew).child("pass").setValue(p);
                                            ref.child(unamenew).child("name").setValue(n);

                                            try {
                                                FileInputStream fstream;
                                                fstream = openFileInput("user_details");
                                                StringBuffer sbuffer = new StringBuffer();
                                                int i;
                                                while ((i = fstream.read())!= -1){
                                                    sbuffer.append((char)i);
                                                }
                                                details = sbuffer.toString().split("\n");
                                                fstream.close();

                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                            SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                            String currentTime = sdftime.format(new Date());
                                            String CurrentDate = sdfdate.format(new Date());
                                            FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                                    .setValue("Username of  "+unameold+" is changed to "+unamenew + " by " + details[0]);
                                            Toast.makeText(getApplicationContext(), "Username of "+ unameold + " changed to " + unamenew, Toast.LENGTH_SHORT).show();
                                            progressDialog.cancel();



                                        } else {

                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"User Not Found",Toast.LENGTH_SHORT).show();
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


        delusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, UserList.class);
                context.startActivity(intent);


            }
        });
    }

    public void SignOut (View view){


        AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingsActivity.this);
        builder.setTitle("Sing Out");
        View viewInflated = LayoutInflater.from(UserSettingsActivity.this).inflate(R.layout.sign_out, null);
        builder.setView(viewInflated);


        TextView text = viewInflated.findViewById(R.id.text);
        text.setText("Thank You for your Service");

        builder.setCancelable(false)
                .setPositiveButton("Log Out",new DialogInterface.OnClickListener() {
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
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    FileOutputStream fstream;
                    deleteFile("user_details");
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);
                    finish();
                   /* Quit();

                    finish();
                    System.exit(0);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    */// This above line close correctly

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

    }


}
