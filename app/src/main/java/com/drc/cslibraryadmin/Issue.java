package com.drc.cslibraryadmin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.UpdateLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
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

public class Issue extends AppCompatActivity {

    EditText bookno,studno;
    NewBook user;
    FirebaseDatabase database;
    DatabaseReference ref,ref1,refb;
    int x=0,x1;
    String name,bookname,bookauthor,studname, details[];
    int a,x2=5;
    TextView bn,ba,sn;
    int bookavail;


    private Button scan_btn,scan_btn2,button;
    int mode=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        setTitle("Issue Book");
        bookno = (EditText)findViewById(R.id.bookno);
        studno = (EditText)findViewById(R.id.studno);
        bn = (TextView)findViewById(R.id.bn);
        ba = (TextView)findViewById(R.id.ba);
        sn = (TextView)findViewById(R.id.sn);
        user = new NewBook();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("ID");
        ref1 = database.getReference("Stud");
        refb = database.getReference("Book");



        scan_btn=(Button)findViewById(R.id.sno);
        scan_btn2=(Button)findViewById(R.id.ssno);
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
        scan_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Student Id Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                mode=2;
            }
        });



        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                x=1;
                bn.setText("");
                ba.setText("");
                bookvalues();
                avail();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        };

        TextWatcher textWatcher1 = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                x=1;
                sn.setText("");
                studvalues();
                validate();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        };
        bookno.addTextChangedListener(textWatcher);
        studno.addTextChangedListener(textWatcher1);



    }

    public void studvalues()
    {
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChild(studno.getText().toString())) {
                        studname = dataSnapshot.child(studno.getText().toString()).child("name").getValue().toString();
                        sn.setText(studname);
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

    public  void bookvalues()
    {
        name="";
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotouter) {

                try {
                    if (dataSnapshotouter.hasChild(bookno.getText().toString())) {
                        //    Toast.makeText(Issue.this,"wait",Toast.LENGTH_SHORT).show();
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
                        try{
                            if(dataSnapshotinner.hasChild(name)){
                                a=Integer.parseInt(dataSnapshotinner.child(name).child("avail").getValue().toString());
                                //Toast.makeText(Issue.this,String.valueOf(a), Toast.LENGTH_SHORT).show();
                            }
                        }catch (NullPointerException c){

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

    public void validate(){
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChild(studno.getText().toString())) {
                       // Toast.makeText(Issue.this,dataSnapshot.child(studno.getText().toString()).toString(),Toast.LENGTH_SHORT).show();
                        if (Integer.parseInt(dataSnapshot.child(studno.getText().toString()).child("avail").getValue().toString()) > 0)
                        {
                            bookavail = Integer.parseInt(dataSnapshot.child(studno.getText().toString()).child("avail").getValue().toString());
//                        Toast.makeText(Issue.this,bookavail,Toast.LENGTH_SHORT).show();
                            x1 = 5;
                            x2=5;
                        }else {
                            x2 = 0;
                        }
                    } else {
                        x1 = 0;
                    }
                }catch (NullPointerException c){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void btnInsert(View view) {


        final String id = bookno.getText().toString();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(x==1) {
                    try {
                        String temp_id = dataSnapshot.child(id).child("studID").getValue().toString();
                         if (temp_id.equals("Nill")&&x1!=0&&x2!=0){
                            ref.child(id).child("studID").setValue(studno.getText().toString());
                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar c = Calendar.getInstance();
                            try{
                                //Setting the date to the given date
                                c.setTime(sdf.parse(date));
                            }catch(ParseException e){
                                e.printStackTrace();
                            }
                            c.add(Calendar.DAY_OF_MONTH, 7);
                            String newDate = sdf.format(c.getTime());
                            ref1.child(studno.getText().toString()).child("avail").setValue(String.valueOf(bookavail-1));
                            FirebaseDatabase.getInstance().getReference("Book").child(name).child("avail").setValue(String.valueOf(a-1));
                            ref.child(id).child("return").setValue(newDate);
                            x++;

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

                             SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                             SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                             String currentTime = sdftime.format(new Date());
                             String CurrentDate = sdfdate.format(new Date());

                             FirebaseDatabase.getInstance().getReference("FineList").child(studno.getText().toString()).child(id + " " + studno.getText().toString()).child("parent").setValue(dataSnapshot.child(id).child("parent").getValue().toString());
                             FirebaseDatabase.getInstance().getReference("FineList").child(studno.getText().toString()).child(id + " " + studno.getText().toString()).child("sname").setValue(studname);
                             FirebaseDatabase.getInstance().getReference("FineList").child(studno.getText().toString()).child(id + " " + studno.getText().toString()).child("sid").setValue(studno.getText().toString());
                             FirebaseDatabase.getInstance().getReference("FineList").child(studno.getText().toString()).child(id + " " + studno.getText().toString()).child("bookid").setValue(id);
                             FirebaseDatabase.getInstance().getReference("FineList").child(studno.getText().toString()).child(id + " " + studno.getText().toString()).child("amt").setValue("0");
                             FirebaseDatabase.getInstance().getReference("FineList").child(studno.getText().toString()).child(id + " " + studno.getText().toString()).child("return").setValue(newDate);
                             FirebaseDatabase.getInstance().getReference("ID").child(id).child("History").child(CurrentDate).child(currentTime)
                                     .setValue("Issued Book: "+ dataSnapshot.child(id).child("parent").getValue().toString() + " of ID: " +
                                             id + " to Student ID: "+studno.getText().toString() + " by " + details[0]);


                            Toast.makeText(Issue.this,"Book Issued",Toast.LENGTH_SHORT).show();
                        } else if(!temp_id.equals("Nill"))
                            Toast.makeText(Issue.this, "Book Not Available", Toast.LENGTH_SHORT).show();
                        else if (x1==0)
                             Toast.makeText(Issue.this, "Invalid Reg No.", Toast.LENGTH_SHORT).show();
                        else if(x2==0)
                             Toast.makeText(Issue.this, "Book Limit Exceeded", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {

                    }x++;
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
                else if(mode==2) studno.setText(result.getContents());
            }

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
