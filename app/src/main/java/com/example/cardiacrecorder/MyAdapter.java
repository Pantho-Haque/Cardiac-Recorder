package com.example.cardiacrecorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Model> list;

    public MyAdapter(Context context, ArrayList<Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.each_measurement,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model measure=list.get(position);
        holder.systolicValue.setText(measure.getSystolic());
        holder.diastolicValue.setText(measure.getDiastolic());
        holder.heartRate.setText(measure.getHeartRate());
        holder.date.setText(measure.getDate());
        holder.comment.setText(measure.getComment());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView systolicValue,diastolicValue,heartRate,date,comment;
        Button update, delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            systolicValue=itemView.findViewById(R.id.systolicValue);
            diastolicValue=itemView.findViewById(R.id.diastolicValue);
            heartRate=itemView.findViewById(R.id.heartRateValue);
            date=itemView.findViewById(R.id.dateValue);
            comment=itemView.findViewById(R.id.comment);

            update=itemView.findViewById(R.id.updateButton);
            delete=itemView.findViewById(R.id.deleteButton);
        }
    }
}