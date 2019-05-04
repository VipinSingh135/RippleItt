package com.rippleitt.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.callback.ItemClickListener;
import com.rippleitt.modals.PostalCodeModal;

import java.util.List;

public class PostalCodesAdapter extends RecyclerView.Adapter<PostalCodesAdapter.MyViewHolder> {

    List<PostalCodeModal> list;
    ItemClickListener itemClickListener;

    public PostalCodesAdapter(List<PostalCodeModal> modalList, Activity activity) {
        list= modalList;
        itemClickListener= (ItemClickListener) activity;
    }

    public void notifyAdapter(List<PostalCodeModal> modalList) {
        list= modalList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_postalcode_item,parent,false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position).getPostcode()+" ,"+ list.get(position).getName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName= itemView.findViewById(R.id.tvName);
        }
    }
}
