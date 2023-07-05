package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    private ProgressDialog loader;

    private EditText regName,regEmail, regPwd;
    private Button regBtn;
    private Button gtlLayout;
    private FirebaseAuth auth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loader = new ProgressDialog(this);

        regName=findViewById(R.id.nameReg);
        regEmail = findViewById(R.id.emailReg);
        regPwd = findViewById(R.id.passReg);

        regBtn = findViewById(R.id.regBtn);
        gtlLayout = findViewById(R.id.gtlLayout);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=regName.getText().toString().trim();
                String email = regEmail.getText().toString().trim();
                String pwd = regPwd.getText().toString().trim();


                if (TextUtils.isEmpty(name)) {
                    regName.setError("Name Required");
                    return;
                }else if (TextUtils.isEmpty(email)) {
                    regEmail.setError("Email Required");
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    regPwd.setError("Password required");
                    return;
                } else
                {
                    loader.setMessage("Registration in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                AccountInfo acc = new AccountInfo(name,email);
                                String uid = auth.getCurrentUser().getUid();
                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users");

                                reference.child(uid).setValue(acc).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            auth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(RegisterActivity.this, "You Have Registered successfully", Toast.LENGTH_LONG).show();

                                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                            finish();
                                                        }
                                                        else
                                                        {
                                                            String err = task.getException().toString();
                                                            Toast.makeText(RegisterActivity.this, "Failed: " + err, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                        } else {
                                            String err = task.getException().toString();
                                            Toast.makeText(RegisterActivity.this, "Failed: " + err, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.w("sad", "createUserWithEmail:failure", task.getException());
                                String err = "Error: " + task.getException().toString();
                                Toast.makeText(RegisterActivity.this, err, Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });
                }
            }
        });

        gtlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}