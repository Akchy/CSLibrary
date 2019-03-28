package com.drc.cslibraryadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE;
import static com.google.android.gms.internal.zzbfq.NULL;

public class AddBook extends AppCompatActivity {

    EditText bookname, author,bookid,quan;
    Button insert;
    FirebaseDatabase database;
    DatabaseReference ref,id_ref;
    NewBook user;
    String id,nameofparent;
    int x=1,flag=0,flag1=0;


    public List<String> namelist = new ArrayList<>();
    public List<String> idlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookname = (EditText) findViewById(R.id.bookname);
        author = (EditText) findViewById(R.id.author);
        quan = (EditText) findViewById(R.id.quan);
        insert = (Button) findViewById(R.id.button);
        bookid = (EditText) findViewById(R.id.bookid);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Book");
        id_ref =database.getReference("ID");
        user = new NewBook();
        id = bookid.getText().toString();
        setTitle("Add Book");



        namelist();
        idlist();

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
        bookname.addTextChangedListener(textWatcher);
        bookid.addTextChangedListener(textWatcher);
        author.addTextChangedListener(textWatcher);
    }

    private void getValues() {
        user.setName(bookname.getText().toString());
        user.setAuthor(author.getText().toString());
        user.setQuan(quan.getText().toString());
        user.setAvail(quan.getText().toString());



    }


    public void btnInsert(View view) {
        if(bookname.length()==0||author.length()==0||bookid.length()==0){
            Toast.makeText(this,"Please Enter All details",Toast.LENGTH_SHORT).show();
        }
        else{
            ref.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nameofparent=bookname.getText().toString()+" by "+author.getText().toString();
                    checkname(nameofparent);

                    if (x == 1) {
                        if(flag!=1) {
                            getValues();

                            String ids = bookid.getText().toString();
                            String[] currencies = ids.split(" ");
                            int j;
                            for (j = 0; j < Integer.parseInt(quan.getText().toString()); j++) {
                                try {
                                    flag1 = 0;
                                    checkid(currencies[j]);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    break;
                                }
                            }

                            if (flag1 != 1) {

                                // Book adding part
                                if (j == Integer.parseInt(quan.getText().toString())) {
                                    ref.child(nameofparent).setValue(user);
                                    SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                    SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                    String currentTime = sdftime.format(new Date());
                                    String CurrentDate = sdfdate.format(new Date());
                                    FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                            .setValue("Added Book: "+ nameofparent +" of ID: "+ Arrays.toString(currencies));
                                    for (int i = 0; i < Integer.parseInt(quan.getText().toString()); i++) {
                                        ref.child(nameofparent).child("IDs").child(String.valueOf(i)).setValue(currencies[i]);
                                        id_ref.child(currencies[i]).child("key").setValue(String.valueOf(i));
                                        id_ref.child(currencies[i]).child("studID").setValue("Nill");
                                        id_ref.child(currencies[i]).child("parent").setValue(nameofparent);
                                        id_ref.child(currencies[i]).child("return").setValue("Nill");
                                        Toast.makeText(AddBook.this, "Book Added", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(AddBook.this, "Quantities and No. of ID's Mismatch", Toast.LENGTH_SHORT).show();
                                }
                                x++;

                            }
                            else if(flag1==1){
                                Toast.makeText(AddBook.this,"ID Already Exist.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(flag==1){
                            Toast.makeText(AddBook.this,"Book Already Exist, Check the List.",Toast.LENGTH_SHORT).show();
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    public void checkname(String n){
        flag=0;
        for(String a: namelist){
            if(a.equals(n)) {
                flag = 1;
                return;
            }
        }
    }

    public void checkid(String n){
        for(String a: idlist){
            if(a.equals(n)) {
                flag1 = 1;
                return;
            }
        }
    }

    public void namelist(){


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> namelist0 = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    namelist0.add(postSnapshot.getKey().toString());
                }
                namelist=namelist0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void idlist(){
        id_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> namelist0 = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    namelist0.add(postSnapshot.getKey().toString());
                }
                idlist=namelist0;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



}
