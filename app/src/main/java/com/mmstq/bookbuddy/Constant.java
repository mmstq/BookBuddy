package com.mmstq.bookbuddy;

import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;

class Constant {
	static String cellNumber;
	static boolean which_method = false;
	static String share_link;
	static boolean runAnim = false;
	
	static void setAnimDrawable(int drawable, Button button, Context context){
		AnimatedVectorDrawable animatedVectorDrawable =
				  (AnimatedVectorDrawable) context.getResources().getDrawable(drawable);
		animatedVectorDrawable.setBounds(0, 0, 60, 60);
		button.setCompoundDrawablesWithIntrinsicBounds(animatedVectorDrawable, null, null, null);
		animatedVectorDrawable.start();
	}
	static void setAnimMenu(int drawable, Menu menu, Context context){
		AnimatedVectorDrawable animatedVectorDrawable =
				  (AnimatedVectorDrawable) context.getResources().getDrawable(drawable);
		menu.getItem(0).setIcon(animatedVectorDrawable);
		animatedVectorDrawable.start();
	}
	static void setAnimImg(int drawable, ImageView menu, Context context){
		final AnimatedVectorDrawable animatedVectorDrawable =
				  (AnimatedVectorDrawable) context.getResources().getDrawable(drawable);
		menu.setImageDrawable(animatedVectorDrawable);
		animatedVectorDrawable.start();
		animatedVectorDrawable.registerAnimationCallback(new Animatable2.AnimationCallback() {
			@Override
			public void onAnimationEnd(Drawable drawable) {
				super.onAnimationEnd(drawable);
				animatedVectorDrawable.start();
			}
		});
	}
}
