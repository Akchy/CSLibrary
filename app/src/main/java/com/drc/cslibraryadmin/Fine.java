package com.drc.cslibraryadmin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class Fine extends AppCompatActivity {


    DatabaseReference refs,ref_fine,ref_fine_list;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;
    ProgressDialog progressDialog;
    String retdate,studname;
    Date c,newc;
    int diff,amount,sum,amt,x=0,x1=1,due,fine;
    List<NewFine> list  = new ArrayList<>();
    TextView fintext;


    // Creating List of ImageUploadInfo class.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Fine");

      //  Toast.makeText(Fine.this,"aaaa",Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_fine);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Fine.this));
        progressDialog = new ProgressDialog(Fine.this);
        progressDialog.setMessage("Loading Data.\nConnect to your Internet");
        progressDialog.show();
        refs =FirebaseDatabase.getInstance().getReference("Stud");
        ref_fine=FirebaseDatabase.getInstance().getReference("Fine");
        ref_fine_list=FirebaseDatabase.getInstance().getReference("FineList");
        fintext = (TextView)findViewById(R.id.fintext);
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
                            studname = postSnapshot.child("sname").getValue().toString();
                           // Toast.makeText(Fine.this, postSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                            noofdays();
                            if (diff > 0) {
                                //Toast.makeText(Fine.this, postSnapshot.child("return").getValue().toString(), Toast.LENGTH_SHORT).show();
                                amt = Integer.parseInt(postSnapshot.child("amt").getValue().toString());
                                fine = fine + (amount * diff);

                                FirebaseDatabase.getInstance().getReference("FineList").child(postSnapshotouter.getKey()).child(postSnapshot.getKey())
                                        .child("amt").setValue(String.valueOf(fine));
                                //  Toast.makeText(Fine.this, String.valueOf(sum), Toast.LENGTH_SHORT).show();

                            }
                        }

                        NewFine imageUploadInfo = new NewFine();
                        imageUploadInfo.setName(studname);
                        imageUploadInfo.setAmt(String.valueOf(fine));
                        imageUploadInfo.setSid(postSnapshotouter.getKey());
                        //  Toast.makeText(Fine.this,"a",Toast.LENGTH_SHORT).show();
                        if(fine!=0)
                        list.add(imageUploadInfo);
                        progressDialog.dismiss();
                    }
                    x++;




                        /*  if(due!=0) {
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {

                                    if (x1 == 0) {
                                        x1++;
                                        sum = 0;
                                        //    Toast.makeText(Fine.this,"0",Toast.LENGTH_SHORT).show();

                                        for (DataSnapshot postSnapshotouter : snapshot.getChildren()) {
                                            if (!postSnapshotouter.child("studID").getValue().toString().equals("Nill")) {
                                                if (postSnapshotouter.child("studID").getValue().toString().equals(postSnapshot.getKey())) {
                                                    x++;
                                                    ret = postSnapshotouter.child("return").getValue().toString();
                                                    par = postSnapshotouter.child("parent").getValue().toString();
                                                    retdate = postSnapshotouter.child("return").getValue().toString();
                                                    noofdays();
                                                    if (diff > 0) {
                                                        amt = Integer.parseInt(postSnapshot.child("amount").getValue().toString());
                                                        // Toast.makeText(Fine.this, String.valueOf(amt), Toast.LENGTH_SHORT).show();
                                                        sum = sum + (amount * diff);
                                                        FirebaseDatabase.getInstance().getReference("Stud").child(postSnapshot.getKey())
                                                                .child("amount").setValue(String.valueOf(sum));

                                                        //  Toast.makeText(Fine.this, String.valueOf(sum), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }

                                        NewFine imageUploadInfo = new NewFine();
                                        imageUploadInfo.setName(postSnapshot.child("name").getValue().toString());
                                        imageUploadInfo.setAmt(postSnapshot.child("amount").getValue().toString());
                                        imageUploadInfo.setSid(postSnapshot.getKey());
                                        //  Toast.makeText(Fine.this,"a",Toast.LENGTH_SHORT).show();
                                        if (Integer.parseInt(postSnapshot.child("amount").getValue().toString()) != 0)
                                            list.add(imageUploadInfo);


                                        adapter = new RecyclerViewAdapterFine(Fine.this, list);
                                        recyclerView.setAdapter(adapter);
                                        if (adapter.getItemCount() == 0) {
                                            Toast.makeText(Fine.this, "List Empty", Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();

                                        //   Toast.makeText(Fine.this,"1",Toast.LENGTH_SHORT).show();
                                        x1 = 0;

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //    Toast.makeText(Fine.this,"1",Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();
                                }
                            });
                        }

                        */
//11111111111


                            //Toast.makeText(Fine.this,"1",Toast.LENGTH_SHORT).show();


                }
                refs.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        adapter = new RecyclerViewAdapterFine(Fine.this, list);
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                        if(adapter.getItemCount()==0){
                            fintext.setVisibility(View.VISIBLE);
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
