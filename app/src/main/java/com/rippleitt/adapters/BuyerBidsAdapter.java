package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.callback.AcceptBidCallback;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.BidTemplate;
import com.rippleitt.webservices.AcceptOfferApi;
import com.rippleitt.webservices.FetchSubCategoryApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 06-03-2018.
 */

public class BuyerBidsAdapter extends BaseAdapter {

    Context context;
    BidTemplate[] listingBids;
    String status="0";
    AcceptBidCallback callback__;
    public BuyerBidsAdapter(Context context,
                            BidTemplate[] bids, AcceptBidCallback cBack) {
        this.context=context;
        listingBids=bids;
        callback__=cBack;
    }

    @Override
    public int getCount() {
       return listingBids.length;
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
            holder= new BuyerBidsAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.buyer_bidview, viewGroup, false);
            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (BuyerBidsAdapter.ViewHolder) view.getTag();
        }
        holder.mtxtVwBidBuyerName=(TextView)view.findViewById(R.id.txtVwBidBuyerName);
        holder.mtxtBuyerBidPrice=(TextView) view.findViewById(R.id.txtvwBidAmount);
        holder.mImgVwAcceptTick=(ImageView)view.findViewById(R.id.imgvwTickAccepted);
        holder.mimgVwBidBuyerPic=(CircleImageView) view.findViewById(R.id.imgVwBidBuyerPic);
        holder.mTxtVwAcceptBid=(TextView) view.findViewById(R.id.txtvwAcceptBid);
        //txtvwAcceptBid
        if(listingBids[i].getIs_accepted().equalsIgnoreCase("1")){
           holder.mImgVwAcceptTick.setVisibility(View.VISIBLE);
           holder.mTxtVwAcceptBid.setVisibility(View.VISIBLE);
        }else{
            holder.mImgVwAcceptTick.setVisibility(View.GONE);
        }


        if(listingBids[i].getHideAccept().equalsIgnoreCase("1")){
                holder.mTxtVwAcceptBid.setVisibility(View.GONE);
            }
            if(listingBids[i].getIs_accepted().equalsIgnoreCase("1")){
                holder.mTxtVwAcceptBid.setVisibility(View.GONE);
                holder.mImgVwAcceptTick.setVisibility(View.VISIBLE);
            }

        //==========setDetails================
        holder.mtxtVwBidBuyerName.setText(
                listingBids[i].getUserinformation().getFullName());

        holder.mtxtBuyerBidPrice.setText("$ "+listingBids[i].getBidprice());

        holder.mTxtVwAcceptBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback__.acceptBid(listingBids[i].getBidid(),Integer.parseInt(listingBids[i].getQuantity()));
            }
        });

        Picasso.with(context)
                .load(RippleittAppInstance.formatPicPath(
                       listingBids[i].getUserinformation().getImage()))
                .placeholder(R.drawable.bg_round_border)
                .error(R.drawable.bg_round_border)
                .into(holder.mimgVwBidBuyerPic);
        //===============================================
        //===================setUserImage===========
return view;
    }

    static class ViewHolder {
        CircleImageView mimgVwBidBuyerPic;
        TextView mtxtVwBidBuyerName;
        TextView txtvwMinutes;
        TextView mtxtBuyerBidPrice;
        TextView mTxtVwAcceptBid;
        ImageView mImgVwAcceptTick;
    }
}
