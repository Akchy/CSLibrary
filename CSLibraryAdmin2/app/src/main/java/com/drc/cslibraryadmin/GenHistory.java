package com.drc.cslibraryadmin;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GenHistory extends AppCompatActivity {



    Button fdate,tdate,search;
    DatePickerDialog datePickerDialog;
    FirebaseDatabase database;
    DatabaseReference ref;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;

    int x=1;

    int fmonth,fday, fyear,mday,mmonth,myear;

    Date fdateval,mdateval,cdateval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_history);
        setTitle("General History");


        database = FirebaseDatabase.getInstance();
        ref = database.getReference("History");
        search = (Button)findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GenHistory.this));

        fdate = (Button) findViewById(R.id.fdate);
        tdate = (Button) findViewById(R.id.tdate);
        // perform click event on edit text
        fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=1;
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int fYear = c.get(Calendar.YEAR); // current year
                int fMonth = c.get(Calendar.MONTH); // current month
                int fDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(GenHistory.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                fdate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                                fday = dayOfMonth;
                                fmonth = monthOfYear+1;
                                fyear=year;


                            }
                        },fYear, fMonth, fDay);
                datePickerDialog.show();
            }
        });

        tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=1;
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(GenHistory.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                tdate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                mday = dayOfMonth;
                                mmonth = monthOfYear+1;
                                myear=year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    public void searchHistory(View view){

        if(fdate.getText().equals("From")||tdate.getText().equals("To"))
            Toast.makeText(GenHistory.this, "Select the Date Interval", Toast.LENGTH_SHORT).show();
        else {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (x == 1) {
                        List<NewHis> list = new ArrayList<>();


                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                fdateval = sdf.parse(fday + "-" + fmonth + "-" + fyear);
                                mdateval = sdf.parse(mday + "-" + (mmonth) + "-" + myear);
                                cdateval = sdf.parse(postSnapshot.getKey());
                            } catch (ParseException p) {
                            }


                            //  Toast.makeText(History.this,cdateval.toString(),Toast.LENGTH_SHORT).show();

                            if ((fdateval.before(cdateval) || fdateval.equals(cdateval)) && (mdateval.after(cdateval) || mdateval.equals(cdateval))) {
                                //  Toast.makeText(History.this,"jjj",Toast.LENGTH_SHORT).show();
                                for (DataSnapshot innerSnapshot : postSnapshot.getChildren()) {


                                    NewHis newhis = new NewHis();
                                    newhis.setVal(innerSnapshot.getValue().toString());
                                    newhis.setTime(innerSnapshot.getKey());
                                    newhis.setDate(postSnapshot.getKey());
                                    list.add(newhis);
                                }
                            }

                        }
                        adapter = new RecyclerViewAdapterGenHistory(GenHistory.this, list);

                        recyclerView.setAdapter(adapter);
                        x++;
                        if (adapter.getItemCount() == 0) {
                            Toast.makeText(GenHistory.this, "No History", Toast.LENGTH_SHORT).show();
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }


    }


}
