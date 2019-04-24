package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.MyDisputesData;
import com.rippleitt.modals.MyDisputesTemplate;

public class MyDisputesAdapter extends BaseAdapter {

    private Context parentRef;
    private MyDisputesData[] data;
    private LayoutInflater mInflater;

    public MyDisputesAdapter(Context context, MyDisputesData[] listngs){
        parentRef=context;
        data=listngs;
        mInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.length;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyDisputesAdapter.ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new MyDisputesAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)parentRef).getLayoutInflater();
            view = inflater.inflate(R.layout.layout_my_disputes, viewGroup,
                    false);
            holder.mImgVwProductThumb=(ImageView)view.findViewById(R.id.imgVwProductThumb);
            holder.mTxtVwProductTitle=(TextView)view.findViewById(R.id.txtVwProductNam);
            holder.mFrmLytBadge=(FrameLayout) view.findViewById(R.id.frmlytOrderFlagColor);
            holder.mTxtVwOrderNumber=(TextView)view.findViewById(R.id.txtvwrderNumber);
            holder.mTxtVwDisputeId=(TextView)view.findViewById(R.id.txtvwDisputeId);
            holder.mTxtvwBadgeStatus=(TextView)view.findViewById(R.id.txtvwLabelOrderStatus);
            holder.mTxtVwProductAcceptedPrice=(TextView)view.findViewById(R.id.txtVwUserBid);
            //txtvwrderStatus
            holder.mTxtVwOrderStatus=(TextView)view.findViewById(R.id.txtvwrderStatus);


            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (MyDisputesAdapter.ViewHolder) view.getTag();
        }


        //====================setImgae=================

        if(data[i].getOrderDetails().getListing_photos().length!=0){

            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getOrderDetails().getListing_photos()[0].getPhoto_path()
                    ))
                    .override(80,80)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImgVwProductThumb);
        }

        holder.mTxtVwProductTitle.setText(data[i].getOrderDetails().getProductname());
        holder.mTxtVwDisputeId.setText(data[i].getDisputeId());
        if (data[i].getOrderDetails().getDirect_buy().equals("1")){
            holder.mTxtVwProductAcceptedPrice.setText("Accepted Price: $" + data[i].getOrderDetails().getListing_price()
                    + " (" + data[i].getOrderDetails().getQuantity() + ""
                    + " Qty)");
        }else if(data[i].getOrderDetails().getDirect_buy().equals("0")){
            holder.mTxtVwProductAcceptedPrice.setText("Accepted Price: $" + data[i].getOrderDetails().getBid_price()
                    + " (" + data[i].getOrderDetails().getQuantity() + ""
                    + " Qty)");
        }
        holder.mTxtVwOrderNumber.setText(data[i].getOrderDetails().getOrder_id());
        if(data[i].getOrderDetails().getIs_buying().equalsIgnoreCase("1")){
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#65b488"));
            holder.mTxtvwBadgeStatus.setText(" Buying ");
        }else{
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#555555"));
            holder.mTxtvwBadgeStatus.setText(" Selling");
        }
        //order status
        holder.mTxtVwOrderStatus.setText(data[i].getStatus());
        if(data[i].getStatusCode().equalsIgnoreCase("0")){
            holder.mTxtVwOrderStatus.setTextColor(Color.parseColor("#555555"));
        }else if(data[i].getStatusCode().equalsIgnoreCase("1")){
            holder.mTxtVwOrderStatus.setTextColor(Color.parseColor("#65b488"));
        }else if(data[i].getStatusCode().equalsIgnoreCase("2")){
            holder.mTxtVwOrderStatus.setTextColor(Color.parseColor("#e17100"));
        }else{
            holder.mTxtVwOrderStatus.setTextColor(Color.parseColor("#FFE10000"));
        }
        return view;
    }


    static class ViewHolder {
        ImageView mImgVwProductThumb;
        TextView mTxtVwProductTitle;
        TextView mTxtVwProductAcceptedPrice;
        TextView mTxtVwOrderNumber;
        TextView mTxtVwDisputeId;
        FrameLayout mFrmLytBadge;
        TextView mTxtvwBadgeStatus;
        TextView mTxtVwOrderStatus;

    }
}
