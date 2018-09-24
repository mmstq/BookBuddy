package com.mmstq.bookbuddy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Update {

    private  DocumentSnapshot ds;
    private  double version;

    public void onCheck(final Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dr = db.collection("Update").document("Params");
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    ds =task.getResult();
                    boolean is_available = (boolean) ds.get("is_available");
                    double version_number = Double.parseDouble((String) ds.get("version_number")) ;
                    Constant.share_link = (String) ds.get("url");
                    try {
                        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        version = Double.parseDouble(pInfo.versionName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(is_available&& version <version_number){
                        TextView message1, title1;
                        Button nBtn, pBtn;
                        final Dialog dialog;
                        dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.login_dialog);

                        title1 = (TextView) dialog.findViewById(R.id.title);
                        message1 = (TextView) dialog.findViewById(R.id.message);
                        nBtn = (Button) dialog.findViewById(R.id.negativeBtn);
                        pBtn = (Button) dialog.findViewById(R.id.positiveBtn);

                        title1.setText((String) ds.get("title"));
                        message1.setText((String) ds.get("description"));
                        pBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.share_link));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        nBtn.setVisibility(View.VISIBLE);
                        nBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });dialog.show();
                        }
                    }
                }
        });
    }
}
