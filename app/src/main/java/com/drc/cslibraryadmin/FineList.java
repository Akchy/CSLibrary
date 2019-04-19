package com.drc.cslibraryadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.BLUE;

public class FineList extends AppCompatActivity {

    DatabaseReference refs,ref_fine,ref_fine_list;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;
    ProgressDialog progressDialog;
    String retdate,stname,bname,rdate,sid;
    Date c,newc;
    int diff,amount,amt,x=0,fine;
    List<NewFine> list  = new ArrayList<>();
    TextView studname;



    String studnetname, fineamt;

    // Creating List of ImageUploadInfo class.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Fine");


        Bundle bundle = getIntent().getExtras();
        studnetname = bundle.getString("name");
        fineamt = bundle.getString("amt");

        //  Toast.makeText(Fine.this,"aaaa",Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_fine_list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FineList.this));
        progressDialog = new ProgressDialog(FineList.this);
        progressDialog.setMessage("Loading Data.\nConnect to your Internet");
        progressDialog.show();
        refs =FirebaseDatabase.getInstance().getReference("Stud");
        ref_fine=FirebaseDatabase.getInstance().getReference("Fine");
        ref_fine_list=FirebaseDatabase.getInstance().getReference("FineList");
        studname = (TextView)findViewById(R.id.names);
        studname.setText(studnetname);


        // fineamount();




        ref_fine.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amount=Integer.parseInt(dataSnapshot.child("fine").getValue().toString());


                ref_fine_list.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(x==0) {
                            list = new ArrayList<>();

                            for (final DataSnapshot postSnapshotouter : dataSnapshot.getChildren()) {        //list of student id

                                fine=0;
                                for (final DataSnapshot postSnapshot : postSnapshotouter.getChildren()) {       //list of bookid and student id
                                    retdate = postSnapshot.child("return").getValue().toString();
                                    sid = postSnapshotouter.getKey();
                                    stname = postSnapshot.child("sname").getValue().toString();
                                    bname = postSnapshot.child("parent").getValue().toString();
                                    rdate = postSnapshot.child("return").getValue().toString();
                                    // Toast.makeText(Fine.this, postSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                    noofdays();
                                    if (diff > 0) {
                                        //Toast.makeText(Fine.this, postSnapshot.child("return").getValue().toString(), Toast.LENGTH_SHORT).show();
                                        amt = Integer.parseInt(postSnapshot.child("amt").getValue().toString());
                                        fine = (amount * diff);

                                        FirebaseDatabase.getInstance().getReference("FineList").child(postSnapshotouter.getKey()).child(postSnapshot.getKey())
                                                .child("amt").setValue(String.valueOf(fine));
                                        //  Toast.makeText(Fine.this, String.valueOf(sum), Toast.LENGTH_SHORT).show();

                                        NewFine imageUploadInfo = new NewFine();
                                        imageUploadInfo.setName(stname);
                                        imageUploadInfo.setAmt(String.valueOf(fine));
                                        imageUploadInfo.setBname(String.valueOf(bname));
                                        imageUploadInfo.setRdate(String.valueOf(rdate));

                                        imageUploadInfo.setSid(postSnapshotouter.getKey());
                                        //  Toast.makeText(Fine.this,"a",Toast.LENGTH_SHORT).show();
                                        list.add(imageUploadInfo);
                                        progressDialog.dismiss();
                                    }
                                }

                            }
                            x++;

                        }
                        refs.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                adapter = new RecyclerViewAdapterFineList(FineList.this, list);
                                recyclerView.setAdapter(adapter);
                                progressDialog.dismiss();

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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








    }
/*
    public void fineamount() {
        ref_fine=FirebaseDatabase.getInstance().getReference("Fine");
        ref_fine.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amount=Integer.parseInt(dataSnapshot.child("fine").getValue().toString());
               // Toast.makeText(Fine.this,String.valueOf(amount),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    public void SettleFine(View view){
        final AlertDialog myQuittingDialogBox =new AlertDialog.Builder(FineList.this)
                .setTitle("Settle Fine" )
                .setMessage("Student name: "+ stname
                        + "\nStudent ID: " +  sid
                        + "\nBook Name: " +  bname
                        + "\nReturn Date: " +  rdate
                        + "\nAmount: " + fine)
                .setPositiveButton("Settle", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseDatabase.getInstance().getReference("FineList").child(stname).removeValue();
                        dialog.dismiss();
                        Context context = view.getContext();
                        Intent intent = new Intent(context, Fine.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        finish();
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

    public void noofdays(){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        try{
            //Setting the date to the given date
            c=(sdf.parse(date));
            newc=(sdf.parse(retdate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        long difference = c.getTime() - newc.getTime();
        float daysBetween = (difference / (1000*60*60*24));
        diff=(int)daysBetween;
        //Toast.makeText(Fine.this,String.valueOf((int)daysBetween),Toast.LENGTH_SHORT).show();

    }
}
