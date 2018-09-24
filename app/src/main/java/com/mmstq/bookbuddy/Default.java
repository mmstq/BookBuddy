package com.mmstq.bookbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;


public class Default extends Fragment {
    private android.support.v4.app.FragmentTransaction ft;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_default, container, false);
        ft = getFragmentManager().beginTransaction();
        ImageView sell = (ImageView) rootView.findViewById(R.id.sell);
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Sell.class);
                startActivity(intent);
            }
        });
        ImageView buy = rootView.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Buy();
                if(getActivity()!=null){
                    ((MainActivity) getActivity()).replaceFragment(fragment);
                }
            }
        });
        ImageView bookbuddy = rootView.findViewById(R.id.bookbuddy);
        bookbuddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new About();
                if(getActivity()!=null){
                    ((MainActivity) getActivity()).replaceFragment(fragment);
                }
            }
        });
        ImageView listing = (ImageView) rootView.findViewById(R.id.listing);
        listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new myAds();
                if(getActivity()!=null){
                    ((MainActivity) getActivity()).replaceFragment(fragment);
                }


            }
        });
        ImageView s_o = (ImageView) rootView.findViewById(R.id.signout);
        s_o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle("Log Out").setIcon(R.drawable.sign_out);
                ad.setMessage("Do You Want To Log Out")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                mAuth.signOut();
                                Intent intent = getActivity().getIntent();
                                getActivity().finish();
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
}
