package com.drc.cslibraryadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText uname,pass;
    FirebaseDatabase database;
    DatabaseReference ref;
    Button log;
    String gname;
    public List<String> unamelist = new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uname=(EditText) findViewById(R.id.uname);
        pass=(EditText)findViewById(R.id.pass);
        log=(Button) findViewById(R.id.b2);

        database = FirebaseDatabase.getInstance();

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(LoginActivity.this);

                // Setting up message in Progress dialog.
                progressDialog.setMessage("Loading Data.\nConnect to your Internet");

                // Showing progress dialog.
                progressDialog.show();
                String userName=uname.getText().toString();
                String passU=pass.getText().toString();

                if(userName.isEmpty() || passU.isEmpty()){

                    Toast.makeText(getApplicationContext(), "Username & Password field cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    ref = database.getReference("users");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(userName)) {
                                String rpass = dataSnapshot.child(userName).child("pass").getValue(String.class);
                                String name = dataSnapshot.child(userName).child("name").getValue(String.class);
                                if (passU.equals(rpass)) {
                                    try {
                                        FileOutputStream fstream;
                                        fstream = openFileOutput("user_details", Context.MODE_PRIVATE);
                                        fstream.write(name.getBytes());
                                        fstream.close();
                                        Toast.makeText(getApplicationContext(), "Details Saved Successfully",Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                   /* try {
                                        FileInputStream fstream;
                                        fstream = openFileInput("user_details");
                                        StringBuffer sbuffer = new StringBuffer();
                                        int i;
                                        while ((i = fstream.read())!= -1){
                                            sbuffer.append((char)i);
                                        }
                                        fstream.close();
                                        String details[] = sbuffer.toString().split("\n");
                                        Toast.makeText(LoginActivity.this,
                                                "Name: "+ details[0], Toast.LENGTH_SHORT).show();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
*/
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }

                            progressDialog.dismiss();
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                }

            }
        });
    }
}
