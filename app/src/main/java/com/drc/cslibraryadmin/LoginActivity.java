package com.drc.cslibraryadmin;

import android.content.Intent;
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
        ref = database.getReference("users");


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long n= dataSnapshot.getChildrenCount();
                        Toast.makeText(getApplicationContext(),""+n,Toast.LENGTH_SHORT).show();
                        List<String> namelist0 = new ArrayList<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            namelist0.add(postSnapshot.getKey().toString());
                        }
                        unamelist=namelist0;

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                if(uname.getText().toString().equals("drc") && pass.getText().toString().equals("drc")){


                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }else {
                  //  Toast.makeText(getApplicationContext(),"Invalid Username or Password",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
