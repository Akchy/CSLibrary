package com.drc.cslibraryadmin;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Return extends AppCompatActivity {

    EditText bookno;
    NewBook user;
    FirebaseDatabase database;
    DatabaseReference ref,ref1,refb;
    int x=0,check=0,x1;
    String name,bookname,bookauthor,studname,studid, details[];
    int a,flag=0;
    int bookavail;
    TextView bn,ba,sn;

    private Button scan_btn,scan_btn2,button;
    int mode=0;

    List<NewBook> list2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        setTitle("Return Book");
        bookno = (EditText)findViewById(R.id.bookno);
        bn = (TextView)findViewById(R.id.bn);
        ba = (TextView)findViewById(R.id.ba);
        sn = (TextView)findViewById(R.id.sn);
        user = new NewBook();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("ID");
        ref1 = database.getReference("Stud");
        refb = database.getReference("Book");

        scan_btn=(Button)findViewById(R.id.sno);
        button=(Button)findViewById(R.id.button);
        final Activity activity = this;
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Book Id Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
        mode=1;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Book Id Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                mode=1;
            }
        });



        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                x=1;
                bn.setText("");
                ba.setText("");
                sn.setText("");
                bookvalues();
                avail();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        };

        bookno.addTextChangedListener(textWatcher);



    }


    public  void bookvalues()
    {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotouter) {

                name="";
                try {
                    if (dataSnapshotouter.hasChild(bookno.getText().toString())) {
                        name = dataSnapshotouter.child(bookno.getText().toString()).child("parent").getValue().toString();
                    }
                }
                catch (NullPointerException c){
                }


                refb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshotinner) {
                        try {
                            if (dataSnapshotinner.hasChild(name)) {
                                bookname = dataSnapshotinner.child(name).child("name").getValue().toString();
                                bookauthor = dataSnapshotinner.child(name).child("author").getValue().toString();
                                bn.setText(bookname);
                                ba.setText(bookauthor);
                            }
                        }
                        catch (NullPointerException c){
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                try {
                    studid = dataSnapshotouter.child(bookno.getText().toString()).child("studID").getValue().toString();
                    if (!studid.equals("Nill")) {
                        ref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshotinner) {
                                try {
                                    studname = dataSnapshotinner.child(studid).child("name").getValue().toString();
                                    sn.setText(studname);
                                    flag=1;
                                }catch (NullPointerException c){

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                        sn.setText("Book Not Issued");
                }catch (NullPointerException c){

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public  void avail()
    {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotouter) {

                try {
                    if (dataSnapshotouter.hasChild(bookno.getText().toString())) {
                        //    Toast.makeText(Issue.this,"wait",Toast.LENGTH_SHORT).show();
                        name = dataSnapshotouter.child(bookno.getText().toString()).child("parent").getValue().toString();
                    }
                }catch (NullPointerException c){

                }

                refb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshotinner) {
                        try {
                            if (dataSnapshotinner.hasChild(name)) {
                                a = Integer.parseInt(dataSnapshotinner.child(name).child("avail").getValue().toString());
                            }
                        }
                        catch (NullPointerException c){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    public void bookavail(){
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(studid)) {
                   // if(Integer.parseInt(dataSnapshot.child(studid).child("avail").getValue().toString())>0)
                        bookavail=Integer.parseInt(dataSnapshot.child(studid).child("avail").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void btnInsert(View view) {

        bookavail();
        final String id = bookno.getText().toString();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(x==1) {
                    try {
                        String temp_id = dataSnapshot.child(id).child("studID").getValue().toString();
                        //validate();
                        if (!temp_id.equals("Nill")){
                            ref.child(id).child("studID").setValue("Nill");
                            ref1.child(studid).child("avail").setValue(String.valueOf(bookavail+1));
                            FirebaseDatabase.getInstance().getReference("Book").child(name).child("avail").setValue(String.valueOf(a+1));

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

                            //write the code here for compare dates

                            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                            SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                            String currentTime = sdftime.format(new Date());
                            String CurrentDate = sdfdate.format(new Date());
                            FirebaseDatabase.getInstance().getReference("ID").child(id).child("History").child(CurrentDate).child(currentTime)
                                    .setValue("Returned Book: "+ dataSnapshot.child(id).child("parent").getValue().toString() + " of ID: " +
                                            id + " from Student ID: "+temp_id + " by " + details[0]);

                            ref.child(id).child("return").setValue("Nill");
                            x++;

                            Toast.makeText(Return.this,"Book Returned",Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result!=null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled this scanning", Toast.LENGTH_SHORT).show();
            }
            else{
                if(mode==1) bookno.setText(result.getContents());
            }

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
