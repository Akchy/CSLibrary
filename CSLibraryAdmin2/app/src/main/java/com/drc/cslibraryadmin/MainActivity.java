package com.drc.cslibraryadmin;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    DatabaseReference ref_fine,book_nos;
    int amount,nos;
    private List<LibRecyclerViewItem> libItemList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        Toast.makeText(MainActivity.this,String.valueOf(currentDateandTime),Toast.LENGTH_LONG).show();*/

        initializeLibItemList();

        // Create the recyclerview.
        RecyclerView libRecyclerView = (RecyclerView)findViewById(R.id.card_view_recycler_list);
        // Create the grid layout manager with 2 columns.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        // Set layout manager.
        libRecyclerView.setLayoutManager(gridLayoutManager);

        // Create car recycler view data adapter with car item list.
        LibRecyclerViewDataAdapter carDataAdapter = new LibRecyclerViewDataAdapter(libItemList);
        // Set data adapter.
        libRecyclerView.setAdapter(carDataAdapter);

        ref_fine=FirebaseDatabase.getInstance().getReference("Fine");

            ref_fine.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        amount = Integer.parseInt(dataSnapshot.child("fine").getValue().toString());
                    // Toast.makeText(Fine.this,String.valueOf(amount),Toast.LENGTH_SHORT).show();
                    }catch (NullPointerException p){
                        ref_fine.child("fine").setValue(1);
                        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                        String currentTime = sdftime.format(new Date());
                        String CurrentDate = sdfdate.format(new Date());
                        FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                .setValue("Set the fine to 1" );
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        book_nos=FirebaseDatabase.getInstance().getReference("BookNos");

        book_nos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    nos = Integer.parseInt(dataSnapshot.child("nos").getValue().toString());
                    // Toast.makeText(Fine.this,String.valueOf(amount),Toast.LENGTH_SHORT).show();
                }catch (NullPointerException p){
                    book_nos.child("nos").setValue(2);
                    SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                    String currentTime = sdftime.format(new Date());
                    String CurrentDate = sdfdate.format(new Date());
                    FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                            .setValue("Set the book nos per student to 2" );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    /* Initialise car items in list. */
    private void initializeLibItemList()
    {
        if(libItemList == null)
        {
            libItemList = new ArrayList<LibRecyclerViewItem>();
            libItemList.add(new LibRecyclerViewItem("Issue", R.drawable.issue));
            libItemList.add(new LibRecyclerViewItem("Return", R.drawable.returnb));
            libItemList.add(new LibRecyclerViewItem("Books", R.drawable.book));
            libItemList.add(new LibRecyclerViewItem("Students", R.drawable.student));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.setting,menu);
        MenuItem menuItem = menu.findItem(R.id.itm1);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if (id == R.id.itm1) {

            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);
        }
        if (id == R.id.fin) {

            Intent intent = new Intent(getApplicationContext(), Fine.class);
            startActivity(intent);
        }
        if (id == R.id.his) {

            Intent intent = new Intent(getApplicationContext(), History.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}