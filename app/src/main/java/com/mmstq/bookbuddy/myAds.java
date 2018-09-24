package com.mmstq.bookbuddy;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class myAds extends Fragment {
    private AdapterRecycler adapterRecycler;
    private ArrayList<myData> list;
    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity activity;
    private ProgressBarGIFDialog.Builder progressBarGIFDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myads, container, false);
        activity = getActivity();
        progressBarGIFDialog= new ProgressBarGIFDialog.Builder(activity);
        progressBarGIFDialog.setCancelable(false)
                .setTitleColor(R.color.colorPrimary)
                .setLoadingGifID(R.drawable.loading)
                .setDoneGifID(R.drawable.done)
                .setDoneTitle("Done")
                .setLoadingTitle("Loading Your Ads");

        Constant.which_method = true;
        rv = (RecyclerView) rootView.findViewById(R.id.listview1);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.str1);
        onRun();
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener((FragmentActivity) activity, rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, final int position) {
                        startDialog(position);
                    }
                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.str1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                list = new ArrayList<>();
                rv.setLayoutManager(null);
                rv.setAdapter(null);
                adapterRecycler.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRun();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1800);
            }
        });
        return rootView;
    }

    public void onRun() {
        progressBarGIFDialog.build();
        list = new ArrayList<>();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("ads").orderByChild("time").getRef();
        Query query = dr.orderByChild("phone").equalTo(Constant.cellNumber);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        myData myData = ds.getValue(myData.class);
                        list.add(0,myData);
                    }
                }else{
                    Toast.makeText(activity,"No Ads Found",Toast.LENGTH_SHORT).show();
                }

                adapterRecycler = new AdapterRecycler(activity, list);
                rv.setLayoutManager(new LinearLayoutManager(activity));
                rv.setAdapter(adapterRecycler);
                adapterRecycler.notifyDataSetChanged();
                progressBarGIFDialog.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void startDialog(final int position){
        TextView title,description, address, price, sem, phone_text, date_text,category;
        Button nBtn, pBtn;
        final Dialog dialog;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.ads_dialog);

        category = (TextView)dialog.findViewById(R.id.cate);
        category.setText(list.get(position).getCategory());
        title = (TextView) dialog.findViewById(R.id.title1);
        title.setText(list.get(position).getBook());
        description = (TextView) dialog.findViewById(R.id.desc);
        description.setText(list.get(position).getDescription());
        address = (TextView) dialog.findViewById(R.id.address);
        address.setText(list.get(position).getAddress());
        price = (TextView) dialog.findViewById(R.id.price2);
        price.setText(list.get(position).getPrice());
        sem = (TextView) dialog.findViewById(R.id.semester_text);
        sem.setText(list.get(position).getSemester());
        phone_text = (TextView) dialog.findViewById(R.id.phone_text);
        phone_text.setText(list.get(position).getPhone());
        date_text = (TextView) dialog.findViewById(R.id.date_text);
        date_text.setText(list.get(position).getTime());

        nBtn = (Button) dialog.findViewById(R.id.nBtn);
        pBtn = (Button) dialog.findViewById(R.id.pBtn);
        if(Constant.which_method){
            pBtn.setText("Delete");
        }

        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("ads").orderByChild("time").equalTo(list.get(position).getTime()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child: dataSnapshot.getChildren()) {
                                    child.getRef().setValue(null);
                                    Toast.makeText(activity,"Deleted",Toast.LENGTH_SHORT).show();
                                    list.clear();
                                    list = new ArrayList<>();
                                    rv.setLayoutManager(null);
                                    rv.setAdapter(null);
                                    onRun();
                                    dialog.dismiss();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(activity,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        nBtn.setVisibility(View.VISIBLE);
        nBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
