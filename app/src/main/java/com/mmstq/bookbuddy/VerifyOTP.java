package com.mmstq.bookbuddy;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class VerifyOTP extends DialogFragment {
	private EditText phone;
	private static String verificationID;
	private FirebaseAuth mAuth;
	private SharedPreferences sp;
	private ProgressBar pb;
	private Context context;
	private String code_verify;
	
	
	static VerifyOTP newInstance() {
		return new VerifyOTP();
	}
	
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.log_in, container, false);
		Constant.runAnim = true;
		pb = v.findViewById(R.id.pb);
		pb.setVisibility(View.VISIBLE);
		phone = v.findViewById(R.id.phone_number);
		phone.setHint("OTP...");
		Button login = v.findViewById(R.id.login);
		login.setText("Submit");
		setCancelable(false);
		login.setOnClickListener(onClickListener);
		v.findViewById(R.id.github).setOnClickListener(onClickListener);
		Button back = v.findViewById(R.id.exit);
		back.setText("Back");
		back.setOnClickListener(onClickListener);
		Constant.setAnimDrawable(R.drawable.exit_to_back, back, context);
		return v;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAuth = FirebaseAuth.getInstance();
		context = getContext();
		if (context != null) {
			sp = context.getSharedPreferences("Phone", 0);
		}
		sendVerificationCode(sp.getString("Phone_No", ""));
		
	}
	
	@Override
	public int getTheme() {
		return R.style.FullScreenDialog;
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			switch (v.getId()) {
				
				case R.id.github:
					String urlString = "https://github.com/mmstq/BookBuddy";
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setPackage("com.android.chrome");
					try {
						startActivity(intent);
					} catch (ActivityNotFoundException ex) {
						intent.setPackage(null);
						startActivity(intent);
					}
					
					break;
				case R.id.exit:
					Login dialog = Login.newInstance();
					if (getFragmentManager() != null) {
						dialog.show(getFragmentManager(), "login");
					}
					break;
				default:
					String code = phone.getText().toString().trim();
					if (code.isEmpty() || code.length() < 6) {
						phone.setError("Enter Code...");
						phone.requestFocus();
						return;
					} else {
						if (code.equals(code_verify)) {
							PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
							signInWithCredential(credential);
						} else {
							phone.setError("Incorrect OTP..!");
							phone.requestFocus();
						}
						
					}
					break;
			}
		}
	};
	
	private void signInWithCredential(PhoneAuthCredential credential) {
		mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				
				Intent intent = new Intent(context, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				pb.setVisibility(View.GONE);
				startActivity(intent);
			}
		});
	}
	
	private void sendVerificationCode(String number) {
		PhoneAuthProvider.getInstance().verifyPhoneNumber(
				  number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack
		);
	}
	
	private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack =
			  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
				  @Override
				  public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
					  super.onCodeSent(s, forceResendingToken);
					  verificationID = s;
				  }
				  
				  @Override
				  public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
					  code_verify = phoneAuthCredential.getSmsCode();
					  if (code_verify != null) {
						  phone.setText(code_verify);
						  PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code_verify);
						  //signInWithCredential(credential);
					  }
				  }
				  
				  @Override
				  public void onVerificationFailed(FirebaseException e) {
					  Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
				  }
			  };
}
