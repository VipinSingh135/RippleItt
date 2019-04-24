package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.webservices.FetchSubCategoryApi;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hp on 06-03-2018.
 */

public class FilterListAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> arrFilter;
    String status="0";
    public FilterListAdapter(Context context, ArrayList<HashMap<String,String>> arrFilter) {
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
            holder= new FilterListAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.filter_adapter_view, viewGroup, false);

            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (FilterListAdapter.ViewHolder) view.getTag();
        }
        holder.mtxtVwFilterItems=(TextView)view.findViewById(R.id.txtVwFilterItems);
        holder.mimgVwSelectIcon=(ImageView) view.findViewById(R.id.imgVwSelectIcon);
        holder.mtxtVwFilterItems.setText(arrFilter.get(i).get("category_name"));

        holder.mimgVwSelectIcon.setVisibility(View.GONE);
        holder.mtxtVwFilterItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RippleittAppInstance.FILTER_CATEGORY_ID=arrFilter.get(i).get("category_id");

                holder.mimgVwSelectIcon.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
                new FetchSubCategoryApi().fetchSubCategoryApi(context,arrFilter.get(i).get("category_id"));
            }
        });

return view;
    }

    static class ViewHolder {
        ImageView imgVwAddrListUserPic;
        TextView mtxtVwFilterItems;
        ImageView mimgVwSelectIcon;
    }
}
