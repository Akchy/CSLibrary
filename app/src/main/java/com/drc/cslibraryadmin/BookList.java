package com.drc.cslibraryadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookList extends AppCompatActivity implements SearchView.OnQueryTextListener{

    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    RecyclerViewAdapter3 adapt;

    // Creating Progress dialog
    ProgressDialog progressDialog;



    // Creating List of ImageUploadInfo class.
    public List<NewBook> list = new ArrayList<>();
    public List<NewBook> list1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        setTitle("Books");

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddBook.class);
                context.startActivity(intent);
            }
        });



        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        adapt = new RecyclerViewAdapter3(getApplicationContext(),list);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(BookList.this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(BookList.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Data.\nConnect to your Internet");

        // Showing progress dialog.
        progressDialog.show();

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");


        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                List<NewBook> list = new ArrayList<>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    NewBook imageUploadInfo = postSnapshot.getValue(NewBook.class);

                    list.add(imageUploadInfo);
                }

                list1=list;
                adapter = new RecyclerViewAdapter3(BookList.this, list);

                recyclerView.setAdapter(adapter);

                if (adapter.getItemCount() == 0) {

                    Toast.makeText(BookList.this, "List Empty", Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        String userinput = s.toLowerCase();
        List<NewBook> newList = new ArrayList();
        for(NewBook name: list1)
        {
            if(name.getName().toLowerCase().contains(userinput)){
                newList.add(name);
            }

        }


        adapt.updateList(newList);
        recyclerView.setAdapter(adapt);
        if (adapter.getItemCount() == 0) {
            Toast.makeText(BookList.this, "List Empty", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
