package com.mmstq.bookbuddy;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Sell extends AppCompatActivity {
    private EditText bookName;
    private EditText add;
    private EditText price;
    private Context context;
    private EditText description;
    private CheckBox cb;
    private EditText semester;
    private Button spinner;
    private boolean empty;
    private String category=null;
    private Map<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        context= this;
        bookName = (EditText) findViewById(R.id.bookname);
        add = (EditText) findViewById(R.id.address);
        final EditText phoneNo = (EditText) findViewById(R.id.phone);
        price = (EditText) findViewById(R.id.price);
        description = (EditText) findViewById(R.id.description);
        semester = (EditText) findViewById(R.id.semester);
        cb = findViewById(R.id.donate);

        map = new HashMap<>();
        phoneNo.setText(Constant.cellNumber);
        spinner = findViewById(R.id.category);
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(context,spinner);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        category=String.valueOf(item.getTitle());
                        Toast.makeText(context,category,Toast.LENGTH_SHORT).show();
                        spinner.setText("Category > "+category);
                        return true;
                    }
                });
            }
        });
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb.isChecked()) {
                    price.setVisibility(View.INVISIBLE);
                } else {
                    price.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ProgressDialog pd = new ProgressDialog(Sell.this);
        pd.setMessage("Posting Ad");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setProgress(0);

        int id = item.getItemId();
        if(id==R.id.post){
            FirebaseDatabase fr = FirebaseDatabase.getInstance();
            final DatabaseReference dr = fr.getReference().child("ads/");
            map.put("book", getBookName());
            map.put("description", getDescription());
            map.put("address", getAddress());
            map.put("phone", Constant.cellNumber);
            map.put("semester", getSemester());
            map.put("price", getPrice());
            map.put("time", getTime());
            map.put("category",getSpinner());

            if (empty) {
                pd.show();
                dr.push().setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.dismiss();
                                Toast.makeText(Sell.this, "Done", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Sell.this, "Error, Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(Sell.this, "Fill Details Properly", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sell_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private String getBookName(){
        if(TextUtils.isEmpty(bookName.getText())) {
            bookName.setError("Enter Book Name");
            bookName.requestFocus();
            empty=false;
            return null;
        }
        return bookName.getText().toString();

    }
    private String getTime(){
        return java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }
    private String getAddress(){
        if(TextUtils.isEmpty(add.getText())) {
            add.setError("Enter Author Name");
            add.requestFocus();
            empty=false;
            return null;
        }
        return add.getText().toString();
    }
    private String getPrice(){
        String price1;
        if(cb.isChecked()){
            empty=true;
            price1 = "Free :)";
        }else {
            if(TextUtils.isEmpty(price.getText())) {
                price.setError("Enter Price");
                price.requestFocus();
                empty=false;
                return null;
            }else {
                price1= ("₹"+price.getText().toString());
                empty=true;
            }
        }
        return price1 ;
    }
    private String getDescription(){
        if(TextUtils.isEmpty(description.getText())) {
            description.setError("Enter Description");
            description.requestFocus();
            empty=false;
            return null;
        }
        return description.getText().toString();
    }
    private String getSemester(){
        if(TextUtils.isEmpty(semester.getText())) {
            semester.setError("Enter Semester");
            semester.requestFocus();
            empty=false;
            return null;
        }
        return semester.getText().toString();
    }

    public String getSpinner() {
        if(category==null){
            spinner.setError("Select Category");
            spinner.requestFocus();
            empty=false;
            return null ;
        }else {
            empty=true;
        }
        return category;

    }
}
