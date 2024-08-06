package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    Context context;
    ArrayList purpose,date,amount,time;

    public MyListAdapter(Context context,ArrayList purpose,ArrayList amount,ArrayList date,ArrayList time) {
        this.context=context;
        this.purpose=purpose;
        this.amount=amount;
        this.date=date;
        this.time=time;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.purpose.setText((String) purpose.get(position));
        holder.amount.setText((String) amount.get(position));
        holder.date.setText((String) date.get(position));
        holder.time.setText((String) time.get(position));
    }

    @Override
    public int getItemCount() {
        return purpose.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView purpose,date,amount,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            purpose = itemView.findViewById(R.id.purpose);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }


    // RecyclerView recyclerView;

}
