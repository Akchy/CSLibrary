package com.drc.cslibraryadmin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static android.graphics.Color.colorSpace;


public class RecyclerViewAdapter3 extends RecyclerView.Adapter<RecyclerViewAdapter3.ViewHolder> {

    Context context;
    List<NewBook> MainImageUploadInfoList;

    String id,ids;
    StringBuilder tot = new
            StringBuilder("");
    StringBuilder all_id = new
            StringBuilder("");
    int x=0,flag=0,once=0;
    String arrOfStr[];
    String all[];
    int len;

    int c,exist_count;
    DatabaseReference id_ref;
    public List<String> idlist = new ArrayList<>();


    public RecyclerViewAdapter3(Context context, List<NewBook> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewBook UploadInfo = MainImageUploadInfoList.get(position);

        holder.Name.setText(UploadInfo.getName() );
        holder.fees.setText(UploadInfo.getAuthor());


        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, RemBook.class);
                intent.putExtra("name", holder.Name.getText().toString()+" by "+holder.fees.getText().toString());
//                Toast.makeText(context,holder.imageNameTextView.toString(),Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, RemBook.class);
                intent.putExtra("name", holder.Name.getText().toString()+" by "+holder.fees.getText().toString());
//                Toast.makeText(context,holder.imageNameTextView.toString(),Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                once=0;
              tot = new
                        StringBuilder("");
                all_id = new
                        StringBuilder("");

              id_ref =FirebaseDatabase.getInstance().getReference("ID");
                id_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        flag=0;


                        if (once < 1) {
                            x = 0;
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                if (postSnapshot.child("parent").getValue().toString().equals(holder.Name.getText().toString() + " by " + holder.fees.getText().toString())) {
                                    tot.append(postSnapshot.getKey());
                                    tot.append(" ");

                                }
                                all_id.append(postSnapshot.getKey());
                                x++;
                                all_id.append(" ");
                            }


                            AlertDialog.Builder alert = new AlertDialog.Builder(context)
                                    .setMessage("Name: " + holder.Name.getText().toString() + "\nAuthor: " + holder.fees.getText().toString()
                                            + "\nIDs: " + tot);
                            alert.setTitle("Add New Book");

                            LinearLayout layout = new LinearLayout(context);
                            layout.setOrientation(LinearLayout.VERTICAL);

                            final EditText count = new EditText(context);
                            count.setHint("Enter the no. of copies");
                            count.setInputType(InputType.TYPE_CLASS_NUMBER);
                            layout.addView(count);

                            final EditText ID = new EditText(context);
                            ID.setHint("Enter the ID's with spaces");
                            layout.addView(ID);


                            alert.setView(layout);

                            alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    id = ID.getText().toString();
                                    try {
                                        c = Integer.parseInt(count.getText().toString());
                                        arrOfStr = id.split(" ");
                                        String temp = all_id.toString();
                                        all = temp.split(" ");
                                        len= all.length;
                                    }
                                    catch (NumberFormatException e){

                                    }
                                    for (int i = 0; i < c; i++) {
                                        for (int j = 0; j <len; j++) {
                                            try {
                                                if (arrOfStr[i].equals(all[j])) {
                                                    Toast.makeText(context, "ID Already Exist", Toast.LENGTH_SHORT).show();
                                                    flag = 1;
                                                }
                                            }
                                            catch (ArrayIndexOutOfBoundsException c){

                                            }

                                        }
                                    }
                                    if(ID.length()==0||count.length()==0)
                                        flag=1;

                                    if (flag != 1) {
                                        exist_count = Integer.parseInt(UploadInfo.getQuan());
                                        FirebaseDatabase.getInstance().getReference("Book").child(holder.Name.getText().toString() + " by " + holder.fees.getText().toString()).child("quan").setValue(String.valueOf(exist_count + c));
                                        FirebaseDatabase.getInstance().getReference("Book").child(holder.Name.getText().toString() + " by " + holder.fees.getText().toString()).child("avail").setValue(String.valueOf(exist_count + c));
                                        for (int i = 0, j = exist_count; i < c; i++, j++) {
                                            FirebaseDatabase.getInstance().getReference("Book").child(holder.Name.getText().toString() + " by " + holder.fees.getText().toString()).child("IDs").child(String.valueOf(j)).setValue(arrOfStr[i]);
                                            FirebaseDatabase.getInstance().getReference("ID").child(arrOfStr[i]).child("parent").setValue(holder.Name.getText().toString() + " by " + holder.fees.getText().toString());
                                            FirebaseDatabase.getInstance().getReference("ID").child(arrOfStr[i]).child("return").setValue("Nill");
                                            FirebaseDatabase.getInstance().getReference("ID").child(arrOfStr[i]).child("studID").setValue("Nill");
                                        }
                                        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                                        SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
                                        String currentTime = sdftime.format(new Date());
                                        String CurrentDate = sdfdate.format(new Date());
                                        FirebaseDatabase.getInstance().getReference("History").child(CurrentDate).child(currentTime)
                                                .setValue("Added ID: "+ Arrays.toString(arrOfStr) +" of Book: "+holder.Name.getText().toString() );
                                        Toast.makeText(context, "Saved Sucessfully", Toast.LENGTH_SHORT).show();

                                    }



                                    //  Toast.makeText(context, Arrays.toString(arrOfStr), Toast.LENGTH_SHORT).show();

                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // what ever you want to do with No option.
                                }
                            });


                            final AlertDialog dialog = alert.create();

                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface arg0) {
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(BLUE);
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(BLUE);
                                }
                            });
                            dialog.show();


                            //               Toast.makeText(context,"2",Toast.LENGTH_SHORT).show();
                        once++;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });



            }
        });


        //Loading image from Glide library.

    }




    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Name;
        public TextView fees,date;
        public Button add,del;

        public ViewHolder(View itemView) {
            super(itemView);


            fees = (TextView) itemView.findViewById(R.id.Author);
            add = (Button) itemView.findViewById(R.id.add);
            Name = (TextView) itemView.findViewById(R.id.Name);
            del=(Button)itemView.findViewById(R.id.del);
        }
    }

    public void updateList(List<NewBook> newList)
    {
        MainImageUploadInfoList = new ArrayList<>();
        MainImageUploadInfoList.addAll(newList);
        notifyDataSetChanged();
    }


}