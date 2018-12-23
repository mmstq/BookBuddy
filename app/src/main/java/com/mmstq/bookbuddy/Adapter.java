package com.mmstq.bookbuddy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Adapter extends FirestoreRecyclerAdapter<myData, Adapter.dataHolder> {
	private Context context;
	private ProgressDialog pd;
	private OnItemClickListener listener;
	
	
	@Override
	public void onDataChanged() {
		super.onDataChanged();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				pd.dismiss();
			}
		}, 500);
		
	}
	
	@Override
	public void onError(@NonNull FirebaseFirestoreException e) {
		super.onError(e);
		pd.dismiss();
		
		
	}
	
	Adapter(FirestoreRecyclerOptions<myData> options) {
		super(options);
		
	}
	
	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		context = recyclerView.getContext();
		pd = new ProgressDialog(context);
		pd.setMessage("Loading Books");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setIndeterminate(true);
		pd.setCancelable(false);
		pd.setProgress(0);
		pd.show();
	}
	
	@Override
	protected void onBindViewHolder(@NonNull final dataHolder holder, int position, @NonNull final myData model) {
		Log.d("contextobv", String.valueOf(context));
		holder.textTop.setText(model.getBook());
		holder.textMid.setText(model.getDescription());
		holder.textBottom.setText(model.getPrice());
		String uri = model.getImage();
		if (uri != null) {
			Glide.with(context).load(Uri.parse(model.getImage())).into(holder.imageView);
		} else {
			holder.imageView.setImageResource(R.drawable.no_image);
		}
		
		holder.textMid.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null && Constant.which_method) {
					listener.onItemClick(model, holder.textMid);
				} else {
					showDialog(model);
				}
			}
		});
		holder.textTop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null && Constant.which_method) {
					listener.onItemClick(model, holder.textTop);
				} else {
					showDialog(model);
				}
			}
		});
		holder.imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null && Constant.which_method) {
					listener.onItemClick(model, holder.imageView);
				} else {
					showDialog(model);
				}
			}
		});
	}
	
	@NonNull
	@Override
	public dataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.cardview, parent, false);
		return new dataHolder(view);
	}
	
	class dataHolder extends RecyclerView.ViewHolder {
		TextView textTop, textMid, textBottom;
		ImageView imageView;
		
		dataHolder(View itemView) {
			super(itemView);
			textTop = itemView.findViewById(R.id.upperText);
			textMid = itemView.findViewById(R.id.lowertext);
			textBottom = itemView.findViewById(R.id.price_text);
			imageView = itemView.findViewById(R.id.book);
		}
	}
	
	
	@SuppressLint("SimpleDateFormat")
	private void showDialog(final myData model) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(model.getTime());
		String image = model.getImage();
		TextView title, description, address, price, sem, phone_text, date_text, category;
		Button nBtn, pBtn;
		ImageView book;
		final Dialog dialog;
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(R.layout.ads_dialog);
		book = dialog.findViewById(R.id.book);
		
		if (image != null) {
			Glide.with(context).load(Uri.parse(image)).into(book);
		} else {
			book.getLayoutParams().height = 40;
			
		}
		
		category = dialog.findViewById(R.id.cate);
		category.setText(model.getCategory());
		title = dialog.findViewById(R.id.title1);
		title.setText(model.getBook());
		description = dialog.findViewById(R.id.desc);
		description.setText(model.getDescription());
		address = dialog.findViewById(R.id.address);
		address.setText(model.getAddress());
		price = dialog.findViewById(R.id.price2);
		price.setText(model.getPrice());
		sem = dialog.findViewById(R.id.semester_text);
		sem.setText(model.getSemester());
		phone_text = dialog.findViewById(R.id.phone_text);
		phone_text.setText(model.getPhone());
		date_text = dialog.findViewById(R.id.date_text);
		date_text.setText(new SimpleDateFormat("E, dd-MMM yy, hh:mm a").format(calendar.getTime()));
		
		nBtn = dialog.findViewById(R.id.nBtn);
		pBtn = dialog.findViewById(R.id.pBtn);
		
		pBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + model.getPhone()));
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
		});
		dialog.show();
	}
	
	public interface OnItemClickListener {
		void onItemClick(myData model, View v);
	}
	
	void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}
}


