package com.mmstq.bookbuddy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;


public class myAds extends android.support.v4.app.Fragment {
   private Adapter adapter;
   private RecyclerView rv;
   private Activity activity;
   private ProgressDialog pd;


   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.myads, container, false);
      activity = getActivity();
      pd = new ProgressDialog(activity);
      pd.setMessage("Loading");
      pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      pd.setIndeterminate(true);
      pd.setProgress(0);

      Constant.which_method = true;
      rv = rootView.findViewById(R.id.listview1);
      onRun();
      adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
         @Override
         public void onItemClick(final myData model,View v) {
            final PopupMenu menu = new PopupMenu(activity,v);
            menu.getMenuInflater().inflate(R.menu.popup_delete,menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
               @Override
               public boolean onMenuItemClick(MenuItem item) {
                  adapter.stopListening();
                  HashMap<String,Object> map = new HashMap<>();
                  DocumentReference dr = FirebaseFirestore.getInstance().collection("ads").document(String.valueOf(model.getTime()));

                  if(model.getImage()!=null){
                     FirebaseStorage.getInstance().getReferenceFromUrl(model.getImage()).delete();
                  }

                  map.put("time", FieldValue.delete());
                  map.put("book", FieldValue.delete());
                  map.put("description", FieldValue.delete());
                  map.put("address", FieldValue.delete());
                  map.put("phone", FieldValue.delete());
                  map.put("semester", FieldValue.delete());
                  map.put("price", FieldValue.delete());
                  map.put("category",FieldValue.delete());
                  map.put("image",FieldValue.delete());
                  dr.update(map);
                  Toast.makeText(activity,"Deleted",Toast.LENGTH_SHORT).show();
                  adapter.startListening();

                  return true;
               }
            });
            menu.show();

         }
      });
      return rootView;
   }

   public void onRun() {
      
      CollectionReference dr = FirebaseFirestore.getInstance().collection("ads");
      com.google.firebase.firestore.Query query;
      query = dr.orderBy("phone").whereEqualTo("phone", Constant.cellNumber);
      FirestoreRecyclerOptions<myData> options = new FirestoreRecyclerOptions.Builder<myData>()
              .setQuery(query, myData.class)
              .build();

      pd.show();
      adapter = new Adapter(options);
      rv.setLayoutManager(new LinearLayoutManager(activity));
      rv.setAdapter(adapter);
      adapter.notifyDataSetChanged();
      new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
            pd.dismiss();
         }
      }, 500);

   }

   @Override
   public void onResume() {
      super.onResume();
      adapter.startListening();
      Log.d("TAG", "hiii");
   }

   @Override
   public void onPause() {
      super.onPause();
      adapter.stopListening();
      Log.d("TAG", "hiii");
   }
}

