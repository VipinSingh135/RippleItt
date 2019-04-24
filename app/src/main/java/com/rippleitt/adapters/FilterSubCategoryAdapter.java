package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hp on 06-03-2018.
 */

public class FilterSubCategoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> arrFilter;
    public FilterSubCategoryAdapter(Context context, ArrayList<HashMap<String,String>> arrFilter) {
        this.context=context;
        this.arrFilter=arrFilter;
    }

    @Override
    public int getCount() {
        return arrFilter.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new FilterSubCategoryAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.filter_adapter_view, viewGroup, false);

            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (FilterSubCategoryAdapter.ViewHolder) view.getTag();
        }

       Button btnApplyFilter=(Button) ((Activity)context).findViewById(R.id.btnApplyFilter);
        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent();
                ((Activity)context).setResult(Activity.RESULT_OK,returnIntent);
                ((Activity)context). finish();
            }
        });


        holder.mtxtVwFilterItems=(TextView)view.findViewById(R.id.txtVwFilterItems);
        holder.mimgVwSelectIcon=(ImageView) view.findViewById(R.id.imgVwSelectIcon);
        holder.mtxtVwFilterItems.setText(arrFilter.get(i).get("subcategory_name"));
        holder.mimgVwSelectIcon.setVisibility(View.GONE);
        holder.mtxtVwFilterItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mimgVwSelectIcon.setVisibility(View.VISIBLE);
                RippleittAppInstance.FILTER_SUBCAT_ID=arrFilter.get(i).get("subcategory_id");
            }
        });



return view;
    }

    static class ViewHolder {
        ImageView imgVwAddrListUserPic;
        ImageView mimgVwSelectIcon;
        TextView mtxtVwFilterItems;
    }
}
