package com.drc.cslibraryadmin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemBook extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseReference databaseReference,refb;

    RecyclerView.Adapter adapter ;

    ProgressDialog progressDialog;
    NewID newid=new NewID();
    TextView bookn,authorn;

    String name,bookname,bookauthor,details[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rem_book);

        Bundle bundle = getIntent().getExtras();
         name = bundle.getString("name");

        refb=FirebaseDatabase.getInstance().getReference("Book");
        bookn =(TextView)findViewById(R.id.book);
        authorn = (TextView)findViewById(R.id.author);

        text_values();

        setTitle("Remove Book");

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);


        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(RemBook.this));

        databaseReference = FirebaseDatabase.getInstance().getReference("ID");

        progressDialog = new ProgressDialog(RemBook.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Data.\nConnect to your Internet");

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

        // Showing progress dialog.
        progressDialog.show();
        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                List<NewID> list = new ArrayList<>();


                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
                        if (postSnapshot.child("parent").getValue().toString().equals(name)) {

                            NewID newid = new NewID();
                            String id = postSnapshot.getKey();
                            String dt = postSnapshot.child("return").getValue().toString();
                            String key = postSnapshot.child("key").getValue().toString();
                            newid.setKey(key);
                            newid.setId(id);
                            newid.setPar(name);
                            newid.setDate(dt);
                            newid.setUser(details[0]);

                            list.add(newid);
                           // Toast.makeText(RemBook.this,id,Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (NullPointerException c){

                    }
                }
                adapter = new RecyclerViewAdapter1(RemBook.this, list);

                recyclerView.setAdapter(adapter);

                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.

                progressDialog.dismiss();


            }
        });
    }

    public void text_values()
    {
        refb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotinner) {
                try {
                    if (dataSnapshotinner.hasChild(name)) {
                        bookname = dataSnapshotinner.child(name).child("name").getValue().toString();
                        bookauthor = dataSnapshotinner.child(name).child("author").getValue().toString();
                        bookn.setText(bookname);
                        authorn.setText(bookauthor);
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
}
