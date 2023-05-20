package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {



    private FirebaseAuth myAuth= FirebaseAuth.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String VerifyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText phoneNumber=findViewById(R.id.loginPhone);
        Button loginButton= findViewById(R.id.loginBtn);
        EditText verificationCode=findViewById(R.id.verificationcode);
        Button verifyButton= findViewById(R.id.verifyBtn);


        verificationCode.setEnabled(true);
        verifyButton.setSaveEnabled(true);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=phoneNumber.getText().toString().trim();
                if(phone.length() == 0){
                    Toast.makeText(LoginActivity.this, "Enter your Phone Number", Toast.LENGTH_SHORT).show();
                }else if(phone.length() != 10){
                    Toast.makeText(LoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(LoginActivity.this, "OTP sending...", Toast.LENGTH_SHORT).show();
                    sendOTP(phone);
                }
            }
        });
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vc=verificationCode.getText().toString().trim();
                if(vc==null){
                    Toast.makeText(LoginActivity.this, "Put you OTP", Toast.LENGTH_SHORT).show();
                }else{
                    verifyCode(vc);
                }

            }
        });
    }

    private void sendOTP(String phone) {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d("onVerificationCompleted", credential.toString());
                signInWithPhoneAuthCredential(credential);
//                            final String code = credential.getSmsCode();
//                            if(code!=null){ verifyCode(code);}
            }
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w("onVerificationFailed", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(LoginActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(LoginActivity.this, "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    Toast.makeText(LoginActivity.this,"reCAPTCHA verification attempted with null Activity", Toast.LENGTH_SHORT).show();
                }
//                            Log.e("failed",e.toString());
//                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d("onCodeSent", "onCodeSent:" + verificationId);
                VerifyID=verificationId;
            }


        };

        Log.w("PHONE",phone);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(myAuth)
                        .setPhoneNumber("+880"+phone)       // Phone number to verify
                        .setTimeout(1L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(LoginActivity.this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(VerifyID,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth fAuth=FirebaseAuth.getInstance();
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}