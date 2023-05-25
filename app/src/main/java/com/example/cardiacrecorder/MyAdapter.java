package com.example.cardiacrecorder;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;

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

        // Normal pressures are systolic between 90 and 140 and diastolic between 60 and 90.
        if(Integer.parseInt(measure.getSystolic()) >=90 && Integer.parseInt(measure.getSystolic()) <=140){
            holder.notNormalText.setVisibility(View.GONE);
            holder.redCircle.setVisibility(View.GONE);
        }else{
            holder.notNormalText.setVisibility(View.VISIBLE);
            holder.redCircle.setVisibility(View.VISIBLE);
        }

        if(Integer.parseInt(measure.getDiastolic()) >=60 && Integer.parseInt(measure.getDiastolic()) <=90){
            holder.notNormalText.setVisibility(View.GONE);
            holder.redCircle.setVisibility(View.GONE);
        }else{
            holder.notNormalText.setVisibility(View.VISIBLE);
            holder.redCircle.setVisibility(View.VISIBLE);
        }
//            if(
//                (Integer.parseInt(measure.getSystolic()) >=90 && Integer.parseInt(measure.getSystolic()) <=140) &&
//                        (Integer.parseInt(measure.getDiastolic()) >=60 && Integer.parseInt(measure.getDiastolic()) <=90)
//        ){
//            holder.notNormalText.setVisibility(View.GONE);
//            holder.redCircle.setVisibility(View.GONE);
//        }


        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View myView = LayoutInflater.from(context).inflate(R.layout.input_file, null);
                final AlertDialog dialog = new AlertDialog.Builder(context).setView(myView).create();
                dialog.setCancelable(false);

                TextView systolic=myView.findViewById(R.id.systolicPressure),
                        diastolic=myView.findViewById(R.id.diastolicPressure),
                        heart=myView.findViewById(R.id.heartRate),
                        comment=myView.findViewById(R.id.writeComment),
                        date=myView.findViewById(R.id.dateAndTime),
                        header=myView.findViewById(R.id.headerOfEntry);

                Button saveButton=myView.findViewById(R.id.saveBtn),
                        cancelButton=myView.findViewById(R.id.cancelBtn);


                // putting the current measure values
                header.setText("Update The Entry");
                systolic.setText(measure.getSystolic());
                diastolic.setText(measure.getDiastolic());
                heart.setText(measure.getHeartRate());
                date.setText(measure.getDate());
                comment.setText(measure.getComment());


                FirebaseAuth myAuth = FirebaseAuth.getInstance();
                //  onlineUserId = myAuth.getCurrentUser().getUid();
                String onlineUserId="12345";
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("entries").child(onlineUserId);

                String id=measure.getId();

                dialog.show();

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a dd/MM/yyyy");
                        Date now = new Date();
                        String theDate=sdf.format(now);

                        Map<String,Object> map=new HashMap<>();
                        map.put("systolic",systolic.getText().toString().trim());
                        map.put("diastolic",diastolic.getText().toString().trim());
                        map.put("heartRate",heart.getText().toString().trim());
                        map.put("date",theDate);
                        map.put("comment",comment.getText().toString().trim());
                        map.put("id",id);

                        ProgressDialog loader=new ProgressDialog(context);
                        loader.setMessage("Updating The Entry...");
                        loader.setCanceledOnTouchOutside(true);
                        loader.show();

                        reference.child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Entry has been Updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    String err = task.getException().toString();
                                    Toast.makeText(context, "Failed: " + err, Toast.LENGTH_SHORT).show();
                                }
                                loader.dismiss();
                            }
                        });
                        dialog.dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog loader=new ProgressDialog(context);
                loader.setMessage("Deleting The Entry...");
                loader.setCanceledOnTouchOutside(true);
                loader.show();

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                //  onlineUserId = myAuth.getCurrentUser().getUid();
                String onlineUserId="12345";
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("entries").child(onlineUserId);

                reference.child(onlineUserId).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task <Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Task has been Deleted successfully", Toast.LENGTH_SHORT).show();

                                } else {
                                    String err = task.getException().toString();
                                    Toast.makeText(context, "Failed: " + err, Toast.LENGTH_SHORT).show();
                                }
                                loader.dismiss();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView systolicValue,diastolicValue,heartRate,date,comment,notNormalText;
        ImageView redCircle;
        Button update, delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            systolicValue=itemView.findViewById(R.id.systolicValue);
            diastolicValue=itemView.findViewById(R.id.diastolicValue);
            heartRate=itemView.findViewById(R.id.heartRateValue);
            date=itemView.findViewById(R.id.dateValue);
            comment=itemView.findViewById(R.id.comment);
            notNormalText=itemView.findViewById(R.id.notNormalText);

            redCircle=itemView.findViewById(R.id.redCircle);

            update=itemView.findViewById(R.id.updateButton);
            delete=itemView.findViewById(R.id.deleteButton);
        }
    }
}