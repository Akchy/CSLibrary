package com.drc.cslibraryadmin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class History extends AppCompatActivity {

    EditText bookno;
    Button fdate,tdate,search;
    DatePickerDialog datePickerDialog;
    FirebaseDatabase database;
    DatabaseReference ref,ref1;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;

    private Button scan_btn;
    int mode=0,x=1;

    int fmonth,fday, fyear,mday,mmonth,myear;

    Date fdateval,mdateval,cdateval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History");

        bookno = (EditText)findViewById(R.id.bno);
        search = (Button)findViewById(R.id.search);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("ID");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(History.this));

        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                x=1;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        };
        bookno.addTextChangedListener(textWatcher);

        scan_btn=(Button)findViewById(R.id.sno);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x=1;
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
                datePickerDialog = new DatePickerDialog(History.this,
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
                datePickerDialog = new DatePickerDialog(History.this,
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




       // Toast.makeText(History.this,fdate.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    public void searchHistory(View view){

        if(fdate.getText().equals("From")||tdate.getText().equals("To"))
            Toast.makeText(History.this, "Select the Date Interval", Toast.LENGTH_SHORT).show();
        else {
            ref1 = ref.child(bookno.getText().toString()).child("History");
            ref1.addValueEventListener(new ValueEventListener() {
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
                        adapter = new RecyclerViewAdapterHistory(History.this, list);

                        recyclerView.setAdapter(adapter);
                        x++;
                        if (adapter.getItemCount() == 0) {
                            Toast.makeText(History.this, "No History", Toast.LENGTH_SHORT).show();
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_history,menu);
        MenuItem menuItem = menu.findItem(R.id.action_settings);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent = new Intent(getApplicationContext(), GenHistory.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
