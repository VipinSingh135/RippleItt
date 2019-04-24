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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.webservices.DeleteListingProductApi;
import com.wang.avi.AVLoadingIndicatorView;

public class UnlockedVouchersAdapter extends BaseAdapter {

    Context context;
    ListingTemplate[] userListings;
    AVLoadingIndicatorView mLoader;
    public UnlockedVouchersAdapter(Context context, ListingTemplate[] userListingsArr, AVLoadingIndicatorView loader) {
        this.context=context;
        this.userListings=userListingsArr;
        this.mLoader=loader;
    }

    @Override
    public int getCount() {
        return userListings.length;
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
        UnlockedVouchersAdapter.ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new UnlockedVouchersAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.adapter_unlocked_vouchers, viewGroup, false);
            holder.txtVwProductNameMAnage=(TextView)view.findViewById(R.id.txtVwProductNameMAnage);
            holder.txtVwVoucher=(TextView)view.findViewById(R.id.txtVwVoucher);
            holder.imgVwProductManageList=(ImageView)view.findViewById(R.id.imgVwProductManageList);
            holder.txtVwProductDescrptionMAnage=(TextView)view.findViewById(R.id.txtVwProductDescrptionMAnage);
            holder.txtVwProductPriceMAnage=(TextView)view.findViewById(R.id.txtVwProductPriceMAnage);
            holder.mrelLayoutMangeList=(RelativeLayout) view.findViewById(R.id.relLayoutMangeList);
            holder.mTxtVwDraftStatus=(TextView) view.findViewById(R.id.txtvwDraftStatus);
            holder.mFrmLytBadge=(FrameLayout) view.findViewById(R.id.frmlytOrderFlagColor);
            holder.mTxtvwBadgeStatus=(TextView)view.findViewById(R.id.txtvwLabelOrderStatus);
            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (UnlockedVouchersAdapter.ViewHolder) view.getTag();
        }

        //==============set data=============
        holder.txtVwProductNameMAnage.setText(userListings[i].getListing_name());
        holder.txtVwProductDescrptionMAnage.setText(userListings[i].getListing_description());
        holder.txtVwProductPriceMAnage.setText("$"+userListings[i].getListing_price());
        holder.mTxtVwDraftStatus.setText(userListings[i].getListing_flag_name());
        Log.e(userListings[i].getListing_flag_name(),userListings[i].getListing_flag());
        if(userListings[i].getListing_flag()!=null && userListings[i].getListing_flag().equalsIgnoreCase("user")
                || userListings[i].getListing_flag().equalsIgnoreCase("6")){
            holder.mTxtVwDraftStatus.setTextColor(Color.RED);
        }else{
            holder.mTxtVwDraftStatus.setTextColor(Color.parseColor("#65b488"));
        }

        //====================setImgae=================

        if(userListings[i].getListing_photos()!=null && userListings[i].getListing_photos().length!=0){


            Glide.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            userListings[i].getListing_photos()[0].getPhoto_path()
                    ))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgVwProductManageList);


        }


        //==============================================

        if (userListings[i].getListing_type()!=null && userListings[i].getListing_type().equals("1")){
            holder.mFrmLytBadge.setVisibility(View.VISIBLE);
        }else{
            holder.mFrmLytBadge.setVisibility(View.GONE);
            holder.txtVwProductPriceMAnage.setVisibility(View.GONE);
        }

        if(userListings[i].getIs_new()!=null && userListings[i].getIs_new().equals("1")){
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#2f4fb9"));
            holder.mTxtvwBadgeStatus.setText(" New ");
        }else{
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#d2742d"));
            holder.mTxtvwBadgeStatus.setText(" Used ");
        }

        if (userListings[i].getVoucher_details()!=null && userListings[i].getVoucher_details().getAmount()!=null) {
            if (userListings[i].getVoucher_details().getType().equals("1")) {
                holder.txtVwVoucher.setText("$" + userListings[i].getVoucher_details().getAmount());
            } else {
                holder.txtVwVoucher.setText(userListings[i].getVoucher_details().getAmount() + "%");
            }

        }else {
            holder.txtVwVoucher.setText("Not Applicable");
        }

        return view;
    }

    static class ViewHolder {
        ImageView imgVwProductManageList;
        TextView txtVwProductNameMAnage;
        TextView txtVwVoucher;
        TextView txtVwProductDescrptionMAnage;
        TextView txtVwProductPriceMAnage;
        TextView mTxtVwDraftStatus;
        RelativeLayout mrelLayoutMangeList;
        FrameLayout mFrmLytBadge;
        TextView mTxtvwBadgeStatus;
    }

}
