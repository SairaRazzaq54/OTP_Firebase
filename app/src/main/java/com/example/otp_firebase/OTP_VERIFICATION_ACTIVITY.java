package com.example.otp_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTP_VERIFICATION_ACTIVITY extends AppCompatActivity {

    TextView phone_number;
    EditText code;
    Button verify, resend;

    String CODE, PHONENUMBER, otpcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        phone_number = findViewById(R.id.phone_number_tv);

        Bundle bundle = getIntent().getExtras();
        PHONENUMBER = bundle.getString("pn");
        otpcode = bundle.getString("otp");
        phone_number.setText("+92"+PHONENUMBER);

        code = findViewById(R.id.otp_code);
        verify = findViewById(R.id.verify_btn);
        resend = findViewById(R.id.resend_btn);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CODE = code.getText().toString().trim();

                if(CODE.isEmpty())
                {
                    code.setError("Enter an OTP");
                    code.requestFocus();
                    return;
                }
                else
                {
                    if(CODE.length() == 6)
                    {
                        OTP_VERIFIED();
                    }
                    else
                    {
                        code.setError("Enter a valid OTP");
                        code.requestFocus();
                        return;
                    }
                }
            }

            private void OTP_VERIFIED()
            {
                if(otpcode == null)
                {
                    Toast.makeText(OTP_VERIFICATION_ACTIVITY.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otpcode, CODE);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(OTP_VERIFICATION_ACTIVITY.this, "Verification successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(OTP_VERIFICATION_ACTIVITY.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OTP_VERIFICATION_ACTIVITY.this, "OTP Resend", Toast.LENGTH_SHORT).show();
            }
        });
    }
}