package com.mmstq.bookbuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ProductViewHolder> {
    private Context context;
    ArrayList<myData> data;

    AdapterRecycler(Context context, ArrayList<myData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cardview,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        holder.textTop.setText(data.get(position).getBook());
        holder.textMid.setText(data.get(position).getDescription());
        holder.textBottom.setText(data.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textTop, textMid, textBottom;
        ImageView imageView;
        ProductViewHolder(View itemView) {
            super(itemView);
            textTop = itemView.findViewById(R.id.upperText);
            textMid = itemView.findViewById(R.id.lowertext);
            textBottom = itemView.findViewById(R.id.price_text);
            imageView= itemView.findViewById(R.id.book);
        }
    }
}

