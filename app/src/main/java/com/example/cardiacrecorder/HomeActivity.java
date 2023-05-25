package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {


    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private FirebaseAuth myAuth;
    private FloatingActionButton floatingActionButton;
    private ProgressDialog loader;

    String onlineUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cardiac Recorder");

        ArrayList<Model> list;
        list = new ArrayList<>();
        MyAdapter myAdapter=new MyAdapter(HomeActivity.this,list);

        // modal
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(myAdapter);


        // Firebase
        mAuth = FirebaseAuth.getInstance();
        onlineUserId="12345";
        reference= FirebaseDatabase.getInstance().getReference().child("entries").child(onlineUserId);

        // read from database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Model model=dataSnapshot.getValue(Model.class);
                    list.add(model);
                }
//                class SortByHead implements Comparator<Model> {
//                    // Used for sorting in ascending order of task head
//                    public int compare(@NonNull Model a,@NonNull Model b)
//                    {
//                        return b.entries.compareTo(a.entries);
//                    }
//                }
//
//                Collections.sort(list, new SortByHead());
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("failed", "Failed to read value.", error.toException());
            }
        });


        // floating action button
        floatingActionButton = findViewById(R.id.addTask);
        loader=new ProgressDialog(this);
        floatingActionButton.setOnClickListener(view -> {

            // creating view
            View myView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.input_file, null);
            final AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this).setView(myView).create();
            dialog.setCancelable(false);

            EditText systolicPressure = myView.findViewById(R.id.systolicPressure),
                    diastolicPressure = myView.findViewById(R.id.diastolicPressure),
                    heartRate=myView.findViewById(R.id.heartRate);

            TextView date=myView.findViewById(R.id.dateAndTime);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a dd/MM/yyyy");
            Date now = new Date();
            String theDate=sdf.format(now);
            date.setText(theDate);

            Button save=myView.findViewById(R.id.saveBtn),
                    cancel=myView.findViewById(R.id.cancelBtn);

            save.setOnClickListener((v)->{
                String systolicValue= systolicPressure.getText().toString().trim();
                String diastolicValue= diastolicPressure.getText().toString().trim();
                String heartRateValue= heartRate.getText().toString().trim();

                // systolic pressure validation
                if(TextUtils.isEmpty(systolicValue)){
                    systolicPressure.setError("Systolic Pressure is Required");
                }else if(Integer.parseInt(systolicValue) <0 || Integer.parseInt(systolicValue)>200 ){
                    systolicPressure.setError("Invalid Systolic Pressure");
                }

                // diastolic pressure validation
                else if(TextUtils.isEmpty(diastolicValue)){
                    diastolicPressure.setError("Diastolic Pressure is Required");
                }else if(Integer.parseInt(diastolicValue)<0 || Integer.parseInt(diastolicValue)>200 ){
                    diastolicPressure.setError("Invalid Diastolic Pressure");
                }

                // heart Rate validation
                else if(TextUtils.isEmpty(heartRateValue)){
                    heartRate.setError("Systolic Pressure is Required");
                }else if(Integer.parseInt(heartRateValue)<0 || Integer.parseInt(heartRateValue)>200 ){
                    diastolicPressure.setError("Invalid Diastolic Pressure");
                }

                else{
                    loader.setMessage("Adding The Entry ...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth = FirebaseAuth.getInstance();
                    //  onlineUserId = myAuth.getCurrentUser().getUid();
                    onlineUserId="12345";
                    reference= FirebaseDatabase.getInstance().getReference().child("entries").child(onlineUserId);
                    String id=reference.push().getKey();
                    Model data = new Model(systolicValue,diastolicValue,heartRateValue,theDate.toString(),id);
                    reference.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(HomeActivity.this, "Entry has been added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                String err = task.getException().toString();
                                Toast.makeText(HomeActivity.this, "Failed: " + err, Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });
                    dialog.dismiss();
                }
            });

            cancel.setOnClickListener((v)->{
                dialog.dismiss();

            });

            dialog.show();
        });
    }


    /**
     *
     * @param menu - The options menu in which you place your items.
     *
     * @return view - the menu will appear
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     * @param item -The menu item that was selected.
     *
     *      the functionality if an item is selected
     *
     * @return callback that denotes an item is selected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}