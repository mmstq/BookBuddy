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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBarDrawerToggle mToggle;
    private boolean onBackPressedCount = false;
    private android.support.v4.app.FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            LogIn.run(MainActivity.this);
        }else {
            Update update = new Update();
            SharedPreferences sp = getSharedPreferences("Phone", 0);
            Constant.cellNumber  =sp.getString("Phone_No", "");
        }
        fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft1 = fm.beginTransaction();
        android.support.v4.app.Fragment fragment = new Default();
        ft1.replace(R.id.frameLayout,fragment).commit();

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navidrawer);
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
        android.support.v4.app.Fragment fragment= null;
        switch (id){
            case R.id.home:
                fragment= new Default();
                break;
            case R.id.about:
                fragment = new About();
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,R.string.heading+Constant.share_link);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if(fragment!=null){
            final android.support.v4.app.Fragment finalFragment = fragment;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    replaceFragment(finalFragment);
                }
            },280);


        }
        return true;
    }
    public void replaceFragment(Fragment fragment){
        final android.support.v4.app.FragmentTransaction ft= fm.beginTransaction();
        if(!fragment.isAdded()){
            ft.addToBackStack("Default");
        }
        ft.setCustomAnimations( android.R.anim.fade_in,android.R.anim.slide_out_right);
        ft.replace(R.id.frameLayout, fragment).commit();


    }
    @Override
    public void onBackPressed() {

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
