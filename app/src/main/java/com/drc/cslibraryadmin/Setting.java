package com.drc.cslibraryadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.BLUE;

public class Setting extends AppCompatActivity {



    FirebaseDatabase database;
    DatabaseReference ref,ref1,refs;
    EditText fine1,booknos;
    String f;
    String nos, details[];
    int num,setnum,x=0,flag=0,x1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        fine1 = (EditText)findViewById(R.id.finedit);
        booknos = (EditText)findViewById(R.id.nosedit);
        setTitle("Settings");


        database = FirebaseDatabase.getInstance();
        finevalue();
        nosvalue();
        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                x=1;
                x1=1;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        };
        booknos.addTextChangedListener(textWatcher);

      //  Toast.makeText(Setting.this,f,Toast.LENGTH_SHORT).show();



    }

    public void finevalue()
    {

        ref = database.getReference("Fine");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    f = dataSnapshot.child("fine").getValue(String.class);
                    fine1.setText(f);
                }
                catch (NullPointerException c){

                }
               // Toast.makeText(Setting.this,f,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void nosvalue()
    {
        ref1 = database.getReference("BookNos");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String email = dataSnapshot.child("nos").getValue(String.class);
                    try {
                        num = Integer.parseInt(email);
                    }catch (NumberFormatException c){

                    }
                    booknos.setText(String.valueOf(num));
                }catch (NullPointerException e){

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SetFin(View view)
    {
        String fine = fine1.getText().toString();
        if(fine.length()!=0) {


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

            ref.child("fine").setValue(fine);
            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
            String currentTime = sdftime.format(new Date());
            String CurrentDate = sdfdate.format(new Date());
            FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                    .setValue("Change the fine to "+ fine + " by " + details[0]);
            Toast.makeText(Setting.this,"Fine Changed",Toast.LENGTH_SHORT).show();
            x++;

        }
    }


    public void check_false()
    {
        refs = database.getReference("Stud");
        refs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (x1 == 1) {

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        int av = Integer.parseInt(postSnapshot.child("avail").getValue().toString());
                        if(setnum<0){
                            int dif=-setnum;
                            //  Toast.makeText(Setting.this,String.valueOf(dif)+" "+String.valueOf(av),Toast.LENGTH_SHORT).show();
                            if(dif>av){
                               flag=1;
                            }
                        }
                    }x1++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void SetNo(View view)
    {
        nosvalue();
        finevalue();
         nos = booknos.getText().toString();


        if(nos.equals("")){
            nos="0";
        }
        setnum = Integer.parseInt(nos) - num;
        flag=0;
        check_false();
       // Toast.makeText(Setting.this,String.valueOf(flag),Toast.LENGTH_SHORT).show();
        refs = database.getReference("Stud");
        refs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (x == 1 && flag==0) {

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        int av = Integer.parseInt(postSnapshot.child("avail").getValue().toString());
                        if(setnum>0) {
                            refs.child(postSnapshot.getKey()).child("avail").setValue(String.valueOf(av + setnum));
                            refs.child(postSnapshot.getKey()).child("count").setValue(nos);
                            if (nos.length() != 0) {

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


                                ref1.child("nos").setValue(nos);
                                SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                String currentTime = sdftime.format(new Date());
                                String CurrentDate = sdfdate.format(new Date());
                                FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                        .setValue("Set the Book nos per student to "+nos + " by " + details[0] );
                            }
                        }
                        else if(setnum<0){
                            int dif=-setnum;
                            //Toast.makeText(Setting.this,String.valueOf(dif)+" "+String.valueOf(av),Toast.LENGTH_SHORT).show();
                            if(dif<=av){
                                refs.child(postSnapshot.getKey()).child("avail").setValue(String.valueOf(av + setnum));
                                refs.child(postSnapshot.getKey()).child("count").setValue(String.valueOf(nos));
                                if (nos.length() != 0) {

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


                                    ref1.child("nos").setValue(nos);
                                    SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                    SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                    String currentTime = sdftime.format(new Date());
                                    String CurrentDate = sdfdate.format(new Date());
                                    FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                            .setValue("Set the Book nos per student to " + nos + " by " +details[0]);
                                }
                            }
                        }
                    }x++;
                }
                if(flag==1){
                    final AlertDialog myQuittingDialogBox =new AlertDialog.Builder(Setting.this)
                            //set message, title, and icon
                            .setTitle("Sorry")
                            .setMessage("Student(s) has currently few Books, try again after returning.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
