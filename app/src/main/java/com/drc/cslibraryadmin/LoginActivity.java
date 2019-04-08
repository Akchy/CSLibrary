package com.drc.cslibraryadmin;

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

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText uname,pass;
    FirebaseDatabase database;
    DatabaseReference ref,id_ref;
    Button log;
    public List<String> unamelist = new ArrayList<>();
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
                                if (passU.equals(rpass)) {
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);

                                    // close this activity
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }
}
