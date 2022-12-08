package com.example.otp_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP_SEND_ACTIVITY extends AppCompatActivity {

    EditText number;
    Button send;

    String phonenumber;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_send);

        mAuth = FirebaseAuth.getInstance();

        number = findViewById(R.id.Phone_Number);
        send = findViewById(R.id.send_btn);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phonenumber = number.getText().toString().trim();

                if(phonenumber.isEmpty())
                {
                    number.setError("Enter a phone number");
                    number.requestFocus();
                    return;
                }
                else
                {
                    if(phonenumber.length() == 10)
                    {
                        otpSend();
                    }
                    else
                    {
                        number.setError("Enter a valid phone number");
                        number.requestFocus();
                        return;
                    }
                }
            }

            private void otpSend() {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+92" + phonenumber,
                        60, TimeUnit.SECONDS,
                        OTP_SEND_ACTIVITY.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OTP_SEND_ACTIVITY.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                Intent intent = new Intent(getApplicationContext(), OTP_VERIFICATION_ACTIVITY.class);
                                intent.putExtra("pn", phonenumber);
                                intent.putExtra("otp", s);
                                startActivity(intent);
                            }
                        }
                );
            }
        });
    }
}