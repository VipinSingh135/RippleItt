package com.rippleitt.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.callback.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class RestictedDaysAdapter extends RecyclerView.Adapter<RestictedDaysAdapter.MyViewHolder> {

    List<String> selected= new ArrayList<>();
    List<String> list= new ArrayList<>();
    Context context;
    ItemClickListener listener;
    public RestictedDaysAdapter(Context baseContext, List<String> interestList, List<String> selectedInterests, ItemClickListener listener) {
        selected= selectedInterests;
        list= interestList;
        context= baseContext;
        this.listener= listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interests,parent,false);
        MyViewHolder viewHolder= new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position));
        if (selected.size()!=0){
            for (String obj: selected){
                if (obj.equals(list.get(position))){
                    holder.chkBox.setChecked(true);
                    break;
                }else{
                    holder.chkBox.setChecked(false);
                }
            }
        }else {
            holder.chkBox.setChecked(false);
        }

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox chkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName=  itemView.findViewById(R.id.tvName);
            chkBox= itemView.findViewById(R.id.chkBox);
        }
    }

}
