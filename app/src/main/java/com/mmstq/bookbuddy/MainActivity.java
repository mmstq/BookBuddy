package com.mmstq.bookbuddy;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private ActionBarDrawerToggle mToggle;
	private boolean onBackPressedCount = false;
	private FragmentManager fm;
	private DrawerLayout drawer;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (FirebaseAuth.getInstance().getCurrentUser() == null) {
			Login dialog = Login.newInstance();
			dialog.show(getSupportFragmentManager(), "tag");
			return;
			
		} else {
			
			setContentView(R.layout.activity_main);
			Update update = new Update();
			update.onCheck(this);
			SharedPreferences sp = getSharedPreferences("Phone", 0);
			Constant.cellNumber = sp.getString("Phone_No", "+91");
			android.util.Log.d("phone", Constant.cellNumber);
			
		}
		
		fm = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = new defaultFrag();
		ft.replace(R.id.frameLayout, fragment).commit();
		
		drawer = findViewById(R.id.drawer_layout);
		mToggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
		drawer.addDrawerListener(mToggle);
		mToggle.syncState();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		NavigationView navigationView = findViewById(R.id.navidrawer);
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.setItemIconTintList(null);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		
		int id = item.getItemId();
		Fragment fragment = null;
		
		switch (id) {
			case R.id.home:
				fragment = new defaultFrag();
				
				break;
			case R.id.about:
				fragment = new About();
				
				break;
			case R.id.share:
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT,
						  "Buy, Sell Or Donate Used Books " +
									 "Within MDU Campus.\nLink: " +
									 Constant.share_link);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
				break;
			case R.id.click_me:
				AlertDialog.Builder adb = new AlertDialog.Builder(this);
				adb.setIcon(R.drawable.attention);
				adb.setTitle("Caution..!");
				adb.setMessage(R.string.caution)
						  .setPositiveButton("Got It", new DialogInterface.OnClickListener() {
							  @Override
							  public void onClick(DialogInterface dialog, int which) {
								  dialog.dismiss();
							  }
						  }).create().show();
				break;
			
			default:
				break;
		}
		
		drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		final Fragment finalFragment = fragment;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (finalFragment != null) {
					replaceFragment(finalFragment);
				}
				
			}
		}, 280);
		return true;
		
	}
	
	public void replaceFragment(Fragment fragment) {
		final android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if (!fragment.isAdded()) {
			ft.addToBackStack("defaultFrag");
		}
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.slide_out_right);
		ft.replace(R.id.frameLayout, fragment, "defaultFrag").commit();
		
	}
	
	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			if (fm.getBackStackEntryCount() > 0) {
				fm.popBackStack();
			} else {
				if (onBackPressedCount) {
					super.onBackPressed();
					return;
				}
				this.onBackPressedCount = true;
				Snackbar sb = Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), getResources().getText(R.string.exit), Snackbar.LENGTH_SHORT);
				SnackbarHelper.configSnackbar(MainActivity.this, sb);
				sb.show();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						onBackPressedCount = false;
					}
				}, 1800);
				
			}
		}
	}
}
