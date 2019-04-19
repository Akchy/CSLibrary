package com.drc.cslibraryadmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSettingsActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference ref;
    ProgressDialog progressDialog;
    TextView addusr,chpass,chuname,delusr;
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



            }
        });

        chuname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


        delusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
    }
}
