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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Login extends DialogFragment {
	private EditText phone;
	private SharedPreferences.Editor editor;
	private Context context;
	
	
	static Login newInstance() {
		return new Login();
	}
	
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.log_in, container, false);
		phone = v.findViewById(R.id.phone_number);
		v.findViewById(R.id.login).setOnClickListener(onClickListener);
		v.findViewById(R.id.github).setOnClickListener(onClickListener);
		Button exit = v.findViewById(R.id.exit);
		exit.setOnClickListener(onClickListener);
		
		Constant.setAnimDrawable(R.drawable.back_to_exit,exit,context);
		
		return v;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getContext();
		editor = context.getSharedPreferences("Phone", 0).edit();
		setCancelable(false);
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
					System.exit(1);
					break;
				default:
					String phone_number = phone.getText().toString().trim();
					if (phone_number.isEmpty() || phone_number.length() < 10) {
						phone.setError("Number Is Required");
						phone.requestFocus();
						return;
					}
					String phone_no = "+91" + phone_number;
					Constant.cellNumber = phone_no;
					editor.putString("Phone_No", phone_no);
					editor.apply();
					phone.setText("");
					VerifyOTP b = VerifyOTP.newInstance();
					if (getFragmentManager() != null) {
						b.show(getFragmentManager(), "verify");
					}
					break;
			}
		}
	};
}
