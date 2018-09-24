package com.mmstq.bookbuddy;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Constant {
    public static String cellNumber;
    public static boolean which_method=false;
    public static String share_link;
    public static DatabaseReference dr= FirebaseDatabase.getInstance().getReference().child("ads");
    public static Query query= dr.orderByChild("time");
}
