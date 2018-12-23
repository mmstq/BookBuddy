package com.mmstq.bookbuddy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mindorks.paracamera.Camera;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Sell extends AppCompatActivity {
    private EditText bookName,add,price,description,semester;
    private CheckBox cb;
    private Button spinner,imgButton;
    private boolean empty;
    private long time;
    private String category=null,path=null,downloadURL=null;
    private Map<String, Object> map;
    private Camera camera;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        bookName = findViewById(R.id.bookname);
        add = findViewById(R.id.address);
        final EditText phoneNo = findViewById(R.id.phone);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        semester = findViewById(R.id.semester);
        cb = findViewById(R.id.donate);

        camera = new Camera.Builder()
               .resetToCorrectOrientation(false)
               .setTakePhotoRequestCode(1)
               .setDirectory("Pics")
               .setName("temp_" + System.currentTimeMillis())
               .setImageFormat(Camera.IMAGE_JPEG)
               .setCompression(50)
               .setImageHeight(200)
               .build(Sell.this);

        map = new HashMap<>();
        phoneNo.setText(Constant.cellNumber);
        spinner = findViewById(R.id.category);
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(Sell.this,spinner);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        category=String.valueOf(item.getTitle());
                        spinner.setText(String.format("Category > %s", category));
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

       imgButton = findViewById(R.id.image_picker);
       imgButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              try {
                 camera.takePicture();
              } catch (IllegalAccessException e) {
                 e.printStackTrace();
              }
           }
        });
    }
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if(requestCode == Camera.REQUEST_TAKE_PHOTO) {
         try {
            path= camera.getCameraBitmapPath();
            imgButton.setText(R.string.imgsltd);
            imgButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.imgcheck,0,0,0);
         } catch (Exception e) { path=null; }
      }else {
         Toast.makeText(Sell.this,"Select Valid Image",Toast.LENGTH_SHORT).show();
      }

   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        pd = new ProgressDialog(Sell.this);
        pd.setMessage("Posting Ad");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setProgress(0);

        int id = item.getItemId();
        if(id==R.id.post){

           getTime();
           map.put("time", time);
           map.put("book", getBookName());
           map.put("description", getDescription());
           map.put("address", getAddress());
           map.put("phone", Constant.cellNumber);
           map.put("semester", getSemester());
           map.put("price", getPrice());
           map.put("category",getSpinner());

            if (onCheck()) {
                pd.show();
                getImage();
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
            empty = false;
            bookName.requestFocus();
            return null;
        }
        return bookName.getText().toString();

    }
    private void getTime(){
      time = System.currentTimeMillis();
   }
    private String getAddress(){
        if(TextUtils.isEmpty(add.getText())) {
            add.setError("Enter Address");
            empty = false;
            add.requestFocus();
            return null;

        }
        return add.getText().toString();
    }
    private String getPrice(){
        String price1;
        if(cb.isChecked()){
            empty=true;
            price1 = "Free";
        }else {
            if(TextUtils.isEmpty(price.getText())) {
                price.setError("Enter Price");
                empty = false;
                price.requestFocus();
                return null;

            }else {
                price1= ("₹"+price.getText().toString());
            }
        }
        return price1 ;
    }
    private String getDescription(){
        if(TextUtils.isEmpty(description.getText())) {
            description.setError("Enter Description");
            empty = false;
            description.requestFocus();
            return null;

        }
        return description.getText().toString();
    }
    private String getSemester(){
        if(TextUtils.isEmpty(semester.getText())) {
            semester.setError("Enter Semester");
            semester.requestFocus();
            empty = false;
            return null;
        }
        return semester.getText().toString();
    }
    private String getSpinner() {
        if(category==null){
            spinner.setError("Select Category");
            empty = false;
            spinner.requestFocus();
            return null;

        }
        return category;

    }
    private boolean onCheck(){
        if(category!=null){
            if(!TextUtils.isEmpty(semester.getText())){
                if(!TextUtils.isEmpty(description.getText())){
                    if(!TextUtils.isEmpty(price.getText())){
                        if(!TextUtils.isEmpty(add.getText())){
                            if(!TextUtils.isEmpty(bookName.getText())){
                                empty = true;
                            }
                        }
                    }
                }
            }
        }
        return empty;
    }
    private void getImage(){

       if(path!=null){

          Uri file = Uri.fromFile(new File(path));
          StorageReference storageRef = FirebaseStorage.getInstance().getReference();
          final StorageReference mountainsRef = storageRef.child("images/"+file.getLastPathSegment());

          mountainsRef.putFile(file).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(Sell.this);
                builder.setMessage("Failed To Upload Image\n" +
                        "Click 'Continue' To Post Ad w/o Image or " +
                        "Click 'Re-Try' Otherwise.").setTitle("Error").setIcon(R.drawable.attention);
                builder.setPositiveButton("Continue Anyway", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                      uploadFinal();
                      dialog.dismiss();
                      pd.show();
                   }
                }).setNegativeButton("Re-Try", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                      getImage();
                      dialog.dismiss();
                      pd.show();
                   }
                }).create().show();
             }
          }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                      downloadURL = uri.toString();
                      uploadFinal();
                   }
                });
             }
          });
       }else {
          pd.dismiss();
          AlertDialog.Builder builder = new AlertDialog.Builder(Sell.this);
          builder.setMessage("No Image Found\nContinue Without Image?").setTitle("No Image");
          builder.setIcon(R.drawable.attention);
          builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                uploadFinal();
                dialog.dismiss();
                pd.show();
             }
          }).setNegativeButton("No", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
             }
          }).create().show();
       }
    }
    private void uploadFinal(){

       DocumentReference dr = FirebaseFirestore.getInstance().collection("ads").document(String.valueOf(time));

      map.put("image",downloadURL);
      dr.set(map)
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                    pd.dismiss();
                    Toast.makeText(Sell.this, "Done", Toast.LENGTH_SHORT).show();
                    finish();

                 }
              }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
            Toast.makeText(Sell.this, "Error, Try Again", Toast.LENGTH_SHORT).show();
         }
      });
   }
    @Override
    protected void onDestroy() {
      super.onDestroy();
      camera.deleteImage();
   }

}
