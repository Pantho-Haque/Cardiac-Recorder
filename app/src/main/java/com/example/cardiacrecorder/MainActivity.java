package com.example.cardiacrecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    Animation frontFromTop;
    ImageView imageView;
    TextView textView;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        frontFromTop= AnimationUtils.loadAnimation(this,R.anim.front_fromtop);

        imageView=findViewById(R.id.frontImage);
        textView=findViewById(R.id.frontText);

        imageView.setAnimation(frontFromTop);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

                finish();
            }
        }, 3000);


    }
}