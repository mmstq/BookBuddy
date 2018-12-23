package com.mmstq.bookbuddy;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;

public class SnackbarHelper {

    public static void configSnackbar(Context context, Snackbar snackbar){
        addMargins(snackbar);
        setRoundBordersBg(context,snackbar);
        ViewCompat.setElevation(snackbar.getView(),1f);
    }

    private static void setRoundBordersBg(Context context, Snackbar snackbar) {
        snackbar.getView().setBackground(context.getDrawable(R.drawable.snackbar));

    }

    private static void addMargins(Snackbar snackbar) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbar.getView().getLayoutParams();
        params.setMargins(17,12,17,17);
        snackbar.getView().setLayoutParams(params);
    }
}
