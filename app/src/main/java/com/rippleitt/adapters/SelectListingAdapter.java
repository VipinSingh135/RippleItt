package com.rippleitt.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rippleitt.R;
import com.rippleitt.callback.ItemClickListener;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.webservices.DeleteListingProductApi;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class SelectListingAdapter extends BaseAdapter {

    Context context;
    List<ListingTemplate> userListings;
    List<ListingTemplate> selectedListings;
    AVLoadingIndicatorView mLoader;
    ItemClickListener itemClickListener;

    public SelectListingAdapter(Context context, List<ListingTemplate> userListingsArr, List<ListingTemplate> selectedListingsArr, AVLoadingIndicatorView loader) {
        this.context = context;
        this.userListings = userListingsArr;
        this.selectedListings = selectedListingsArr;
        this.mLoader = loader;
        this.itemClickListener= (ItemClickListener) context;
    }

    public void notifyAdapter(List<ListingTemplate> userListingsArr, List<ListingTemplate> selectedListingsArr) {
        this.userListings = userListingsArr;
        this.selectedListings = selectedListingsArr;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userListings.size();
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
        SelectListingAdapter.ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder = new SelectListingAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.adapter_select_listings, viewGroup, false);
            holder.txtVwProductNameMAnage = (TextView) view.findViewById(R.id.txtVwProductNameMAnage);
            holder.imgVwProductManageList = (ImageView) view.findViewById(R.id.imgVwProductManageList);
            holder.txtVwPrice = (TextView) view.findViewById(R.id.txtVwPrice);
            holder.mrelLayoutMangeList = (RelativeLayout) view.findViewById(R.id.relLayoutMangeList);
            holder.mFrmLytBadge = (FrameLayout) view.findViewById(R.id.frmlytOrderFlagColor);
            holder.mTxtvwBadgeStatus = (TextView) view.findViewById(R.id.txtvwLabelOrderStatus);
            holder.txtVwQuantity = (TextView) view.findViewById(R.id.txtVwQuantity);
            holder.checkBox = view.findViewById(R.id.checkbox);

            holder.checkBox.setClickable(false);
            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (SelectListingAdapter.ViewHolder) view.getTag();
        }

        //==============set data=============
        holder.txtVwProductNameMAnage.setText(userListings.get(i).getProductname());
        holder.txtVwPrice.setText("$"+userListings.get(i).getListing_price());
        holder.txtVwQuantity.setText(userListings.get(i).getQuantity());
        Log.e(userListings.get(i).getListing_flag_name(), userListings.get(i).getListing_flag());
//        if(userListings[i].getListing_flag().equalsIgnoreCase("user")
//                || userListings[i].getListing_flag().equalsIgnoreCase("6")){
//        }else{
//        }

        //====================setImgae=================

        if (userListings.get(i).getListing_photos().length != 0) {


            Glide.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            userListings.get(i).getListing_photos()[0].getPhoto_path()
                    ))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgVwProductManageList);
        }

        //================click on menu==================
        if (selectedListings.contains(userListings.get(i))) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        //==============================================
        if (userListings.get(i).getListing_type().equals("1")) {
            holder.mFrmLytBadge.setVisibility(View.VISIBLE);
        } else {
            holder.mFrmLytBadge.setVisibility(View.GONE);
        }

        if (userListings.get(i).getIs_new().equals("1")) {
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#2f4fb9"));
            holder.mTxtvwBadgeStatus.setText(" New ");
        } else {
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#d2742d"));
            holder.mTxtvwBadgeStatus.setText(" Used ");
        }

        holder.mrelLayoutMangeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(i);
            }
        });

        return view;
    }

    static class ViewHolder {
        ImageView imgVwProductManageList;
        TextView txtVwProductNameMAnage;
        TextView txtVwPrice,txtVwQuantity;
        RelativeLayout mrelLayoutMangeList;
        FrameLayout mFrmLytBadge;
        TextView mTxtvwBadgeStatus;
        CheckBox checkBox;
    }

}

