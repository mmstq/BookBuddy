package com.mmstq.bookbuddy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTP {
    private static String verificationID;
    private FirebaseAuth mAuth;
    private Context context;
    static SharedPreferences sp;
    private Dialog dialog;
    private EditText phone;
    private String code_verify;
    private String code;

    public void run(final Context cntxt) {
        mAuth = FirebaseAuth.getInstance();
        context =cntxt;

        sp= cntxt.getSharedPreferences("Phone",0);
        sendVerificationCode(sp.getString("Phone_No",""));
        TextView title1;
        Button nBtn, pBtn;
        dialog = new Dialog(cntxt);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.login_dialog);

        title1 = (TextView) dialog.findViewById(R.id.title);
        phone = (EditText) dialog.findViewById(R.id.phone_number);
        nBtn = (Button) dialog.findViewById(R.id.negativeBtn);
        pBtn = (Button) dialog.findViewById(R.id.positiveBtn);
        phone.setHint("OTP");

        title1.setText("Verification");
        pBtn.setText("Resend");
        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn.run(cntxt);
                dialog.dismiss();
            }
        });
        nBtn.setText("Verify");
        nBtn.setVisibility(View.VISIBLE);
        nBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = phone.getText().toString().trim();
                if(code.isEmpty()||code.length()<6){
                    phone.setError("Enter Code...");
                    phone.requestFocus();
                    return;
                }else {
                    if(code.equals(code_verify)) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
                        signInWithCredential(credential);
                    }else {
                        phone.setError("Incorrect OTP..!");
                        phone.requestFocus();
                    }

                }

            }
        });
        dialog.show();
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    dialog.dismiss();
                    context.startActivity(intent);
            }
        });
    }
    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationID=s;
                }
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    code_verify= phoneAuthCredential.getSmsCode();
                    if(code_verify != null){
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code_verify);
                        signInWithCredential(credential);
                    }
                }
                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            };
}
