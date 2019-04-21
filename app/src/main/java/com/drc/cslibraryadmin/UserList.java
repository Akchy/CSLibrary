package com.drc.cslibraryadmin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class UserList extends AppCompatActivity {


    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;


    // Creating Progress dialog
    ProgressDialog progressDialog;

    String details[];

    // Creating List of ImageUploadInfo class.
    public List<NewBook> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("Users List");

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);


        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(UserList.this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(UserList.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Data.\nConnect to your Internet");

        // Showing progress dialog.
        progressDialog.show();



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

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference("users");


        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                List<NewBook> list = new ArrayList<>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                   // Toast.makeText(UserList.this, postSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    NewBook imageUploadInfo = postSnapshot.getValue(NewBook.class);
                    imageUploadInfo.setName(postSnapshot.child("name").getValue().toString());
                    imageUploadInfo.setAuthor(postSnapshot.getKey());
                    imageUploadInfo.setUser(details[0]);

                    list.add(imageUploadInfo);
                }

                adapter = new RecyclerViewAdapterUserList(UserList.this, list);

                recyclerView.setAdapter(adapter);

                if (adapter.getItemCount() == 0) {

                   // Toast.makeText(UserList.this, "List Empty", Toast.LENGTH_SHORT).show();
                }
                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });

    }
}
