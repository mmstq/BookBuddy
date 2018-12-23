package com.mmstq.bookbuddy;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class defaultFrag extends android.support.v4.app.Fragment {
	private Activity activity;
	final private int READ_EXTERNAL = 123;
	ImageView im;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_def, container, false);
		activity = getActivity();
		rootView.findViewById(R.id.sell)
				  .setOnClickListener(new View.OnClickListener() {
					  @Override
					  public void onClick(View v) {
						  if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
									 != PackageManager.PERMISSION_GRANTED) {
							  requestPermission();
						  } else {
							  Intent intent = new Intent(activity, Sell.class);
							  startActivity(intent);
						  }
					  }
				  });
		rootView.findViewById(R.id.buy)
				  .setOnClickListener(new View.OnClickListener() {
					  @Override
					  public void onClick(View v) {
						  
						  android.support.v4.app.Fragment fragment = new Buy();
						  ((MainActivity) activity).replaceFragment(fragment);
					  }
				  });
		im = rootView.findViewById(R.id.bookbuddy);
		im.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Constant.setAnimImg(R.drawable.ball,im,activity);
				//android.support.v4.app.Fragment fragment = new About();
				//((MainActivity) activity).replaceFragment(fragment);
			}
		});
		rootView.findViewById(R.id.listing)
				  .setOnClickListener(new View.OnClickListener() {
					  @Override
					  public void onClick(View v) {
						  android.support.v4.app.Fragment fragment = new myAds();
						  ((MainActivity) activity).replaceFragment(fragment);
					  }
				  });
		rootView.findViewById(R.id.signout)
				  .setOnClickListener(new View.OnClickListener() {
					  @Override
					  public void onClick(View v) {
						  AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
						  ad.setTitle("Login Out").setIcon(R.drawable.sign_out);
						  ad.setMessage("Do You Want To Login Out")
									 .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
										 @Override
										 public void onClick(DialogInterface dialog, int which) {
											 FirebaseAuth mAuth = FirebaseAuth.getInstance();
											 mAuth.signOut();
											 Intent intent = activity.getIntent();
											 activity.finish();
											 startActivity(intent);
										 }
									 }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							  @Override
							  public void onClick(DialogInterface dialog, int which) {
								  dialog.dismiss();
							  }
						  }).create().show();
					  }
				  });
		return rootView;
	}
	
	private void requestPermission() {
		ActivityCompat.requestPermissions(activity, new String[]
							 {Manifest.permission.READ_EXTERNAL_STORAGE},
				  READ_EXTERNAL);
		
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case READ_EXTERNAL:
				if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
					Toast.makeText(activity, "App Will Not Work Properly", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
