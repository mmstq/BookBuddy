package com.mmstq.bookbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Buy extends Fragment {
	private Activity activity;
	private Adapter adapter;
	private RecyclerView rv;
	private boolean category_clicked = false;
	private String category = null;
	private Menu menu;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Constant.which_method = false;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		activity.getMenuInflater().inflate(R.menu.popup_menu1, menu);
		this.menu = menu;
		super.onCreateOptionsMenu(menu, inflater);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Constant.setAnimMenu(R.drawable.filte,menu,activity);
		category = null;
		int id = item.getItemId();
		switch (id) {
			case R.id.btech:
				category = String.valueOf(item.getTitle());
				break;
			case R.id.mtech:
				category = String.valueOf(item.getTitle());
				break;
			case R.id.medical:
				category = String.valueOf(item.getTitle());
				break;
			case R.id.law:
				category = String.valueOf(item.getTitle());
				break;
			case R.id.other:
				category = String.valueOf(item.getTitle());
				break;
			case R.id.all:
				category = String.valueOf(item.getTitle());
				break;
			case R.id.free:
				category = String.valueOf(item.getTitle());
				break;
			default:
				break;
		}
		if (category != null) {
			category_clicked = true;
			onRun();
		}
		return super.onOptionsItemSelected(item);
		
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
									 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
		activity = getActivity();
		setHasOptionsMenu(true);
		FirebaseDatabase fd = FirebaseDatabase.getInstance();
		fd.getReference().child("ads");
		rv = rootView.findViewById(R.id.listview);
		onRun();
		return rootView;
	}
	
	private void onRun() {
		
		CollectionReference dr = FirebaseFirestore.getInstance().collection("ads");
		com.google.firebase.firestore.Query query;
		if (!category_clicked || category.equals("All")) {
			
			query = dr.orderBy("time");
			FirestoreRecyclerOptions<myData> options = new FirestoreRecyclerOptions.Builder<myData>()
					  .setQuery(query, myData.class)
					  .build();
			
			addData(options);
			
		} else if (category.equals("Free")) {
			
			query = dr.orderBy("price").whereEqualTo("price", category);
			FirestoreRecyclerOptions<myData> options = new FirestoreRecyclerOptions.Builder<myData>()
					  .setQuery(query, myData.class)
					  .build();
			addData(options);
			
		} else {
			
			query = dr.orderBy("category").whereEqualTo("category", category);
			FirestoreRecyclerOptions<myData> options = new FirestoreRecyclerOptions.Builder<myData>()
					  .setQuery(query, myData.class)
					  .build();
			addData(options);
			Toast.makeText(activity, category + " Books", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void addData(FirestoreRecyclerOptions<myData> options) {
		adapter = new Adapter(options);
		rv.setLayoutManager(new LinearLayoutManager(activity));
		rv.setAdapter(adapter);
		adapter.stopListening();
		adapter.startListening();
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		adapter.stopListening();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.startListening();
		
	}
	
}
