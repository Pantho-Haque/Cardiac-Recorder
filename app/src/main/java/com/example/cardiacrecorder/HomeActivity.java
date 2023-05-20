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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {


    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cardiac Recorder");



        // floating action button
        floatingActionButton = findViewById(R.id.addTask);
        loader=new ProgressDialog(this);
        floatingActionButton.setOnClickListener(view -> {

            // creating view
            View myView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.input_file, null);
            final AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this).setView(myView).create();
            dialog.setCancelable(false);

            Button save=myView.findViewById(R.id.saveBtn),
                    cancel=myView.findViewById(R.id.cancelBtn);

            save.setOnClickListener((v)->{
                dialog.dismiss();
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