package com.mmstq.bookbuddy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Buy extends Fragment {
    private Activity activity;
    private AdapterRecycler adapterRecycler;
    private ArrayList<myData> list;
    private RecyclerView rv;
    private boolean category_clicked=true;
    private ProgressBarGIFDialog.Builder progressBarGIFDialog;
    private String category;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.which_method=false;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        activity.getMenuInflater().inflate(R.menu.popup_menu1,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        category=null;
        int id = item.getItemId();
        switch (id){
            case R.id.btech:
                category= String.valueOf(item.getTitle());
                break;
            case R.id.mtech:
                category= String.valueOf(item.getTitle());
                break;
            case R.id.medical:
                category= String.valueOf(item.getTitle());
                break;
            case R.id.law:
                category= String.valueOf(item.getTitle());
                break;
            case R.id.other:
                category= String.valueOf(item.getTitle());
                break;
            case R.id.all:
                category= String.valueOf(item.getTitle());
                break;
            default:
                break;
        }
        if(category!=null){
            category_clicked= false;
            onRun();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
        activity = getActivity();
        progressBarGIFDialog= new ProgressBarGIFDialog.Builder(activity);
        progressBarGIFDialog.setCancelable(false)
                .setTitleColor(R.color.colorPrimary)
                .setLoadingGifID(R.drawable.loading)
                .setDoneGifID(R.drawable.done)
                .setDoneTitle("Done")
                .setLoadingTitle("Loading Books");

        setHasOptionsMenu(true);

        rv = (RecyclerView) rootView.findViewById(R.id.listview);

        onRun();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipetorefresh);
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
                },1800);
            }
        });

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                TextView title,description, address, price, sem, phone_text, date_text,category;
                Button nBtn, pBtn;
                final Dialog dialog;
                dialog = new Dialog(getActivity());
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


                pBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+list.get(position).getPhone()));
                        getActivity().startActivity(intent);
                        dialog.dismiss();
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

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return rootView;
    }

    private void onRun(){

        DatabaseReference dr;
        Query query;
        if(category_clicked || category.equals("All")){
            list = new ArrayList<>();
            rv.setLayoutManager(new LinearLayoutManager(activity));
            dr = FirebaseDatabase.getInstance().getReference().child("ads").orderByChild("time").getRef();
            query = dr;
            addData(query);
        }else {
            list.clear();
            list = new ArrayList<>();
            rv.setLayoutManager(null);
            rv.setAdapter(null);
            adapterRecycler.notifyDataSetChanged();
            dr = FirebaseDatabase.getInstance().getReference().child("ads").orderByChild("time").getRef();
            query = dr.orderByChild("category").equalTo(category);
            addData(query);
        }


    }
    private void addData(Query query){
        progressBarGIFDialog.build();
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
                rv.setLayoutManager(new LinearLayoutManager(activity));
                adapterRecycler= new AdapterRecycler(activity,list);
                rv.setAdapter(adapterRecycler);
                adapterRecycler.notifyDataSetChanged();
                progressBarGIFDialog.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
