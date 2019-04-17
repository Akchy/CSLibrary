package com.drc.cslibraryadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class AddStud extends AppCompatActivity {

    EditText name, reg;
    Button insert;
    FirebaseDatabase database;
    DatabaseReference ref,ref1;
    NewStud user;
    int x=1;
    String bookavail,details[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stud);

        name = (EditText) findViewById(R.id.name);
        reg = (EditText) findViewById(R.id.reg);
        insert = (Button) findViewById(R.id.button);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Stud");
        user = new NewStud();

        setTitle("Add Student");

        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                x=1;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        };
        name.addTextChangedListener(textWatcher);
        reg.addTextChangedListener(textWatcher);

    }


    private void getValues() {
        user.setName(name.getText().toString());
        user.setReg(reg.getText().toString());
        user.setAvail(bookavail);
        user.setCount(bookavail);
        user.setAmount("0");
        user.setDue("0");

    }
    public void bookavail(){
        ref1=FirebaseDatabase.getInstance().getReference("BookNos");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("nos")) {
                    // if(Integer.parseInt(dataSnapshot.child(studid).child("avail").getValue().toString())>0)
                    bookavail=dataSnapshot.child("nos").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void btnInsert(View view) {
        bookavail();
        if(name.length()==0||reg.length()==0){
            Toast.makeText(this,"Please Enter All details",Toast.LENGTH_SHORT).show();
        }
        else{

            ref.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (x == 1) {
                        getValues();
                        ref.child(reg.getText().toString()).setValue(user);
                        x++;


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
                                .setValue("Added Student: "+ name.getText().toString() +" with Reg ID: "+reg.getText().toString()
                                        + "by " + details[0]);

                        Toast.makeText(AddStud.this, "Student Added", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
