package com.example.memorite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memorite.R;
import com.example.memorite.model.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Entry> entryArrayList;

    public EntryAdapter(Context context, ArrayList<Entry> entryArrayList){
        this.context = context;
        this.entryArrayList = entryArrayList;
    }

    @NonNull
    @Override
    public EntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryAdapter.ViewHolder holder, int position) {
        Entry entry = entryArrayList.get(position);
        holder.cardDate.setText(entry.getDate());
        holder.cardOptions.setImageResource(R.drawable.ic_baseline_more_vert_24);
        holder.cardTitle.setText(entry.getTitle());

        if(entry.getImage() != null){
            System.out.println("Not null");
            Glide.with(context).asBitmap().load(entry.getImage()).into(holder.cardImage);
        }else{
            System.out.println("Null");
            holder.cardImage.setImageResource(R.drawable.ic_baseline_landscape_24);
        }

    }

    @Override
    public int getItemCount() {
        return entryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView cardImage, cardOptions;
        private TextView cardTitle, cardDate;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardImage);
            cardOptions = itemView.findViewById(R.id.cardOptions);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDate = itemView.findViewById(R.id.cardDate);
        }
    }
}
