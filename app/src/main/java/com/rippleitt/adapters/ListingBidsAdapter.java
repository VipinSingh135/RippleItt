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
import com.rippleitt.callback.AcceptBidCallback;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.BidTemplate;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pc on password/23/2018.
 */

public class ListingBidsAdapter  extends BaseAdapter {

    Context context;
    BidTemplate[] listingBids;
    String status="0";
    AcceptBidCallback callback__;
    public ListingBidsAdapter(Context context,
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
        final BuyerBidsAdapter.ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new BuyerBidsAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.listing_bids_view, viewGroup, false);
            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (BuyerBidsAdapter.ViewHolder) view.getTag();
        }
        holder.mtxtVwBidBuyerName=(TextView)view.findViewById(R.id.txtVwBidBuyerName);
        holder.txtvwMinutes=(TextView)view.findViewById(R.id.txtvwMinutes);
        holder.mtxtBuyerBidPrice=(TextView) view.findViewById(R.id.txtvwBidAmount);
        holder.mimgVwBidBuyerPic=(CircleImageView) view.findViewById(R.id.imgVwBidBuyerPic);
        //txtvwAcceptBid
        if(listingBids[i].getIs_accepted().equalsIgnoreCase("1")){

            holder.mImgVwAcceptTick.setVisibility(View.VISIBLE);
        }else{

            holder.mImgVwAcceptTick.setVisibility(View.GONE);
        }
        if(Integer.parseInt(listingBids[i].getMinutes())<1440){

            holder.txtvwMinutes.setText(CommonUtils.minutesToHours(Integer.parseInt(listingBids[i].getMinutes())));
            holder.mTxtVwAcceptBid.setVisibility(View.VISIBLE);
        }else{

            holder.txtvwMinutes.setText("Expired");
            holder.mTxtVwAcceptBid.setVisibility(View.INVISIBLE);
        }

        //==========setDetails================
        holder.mtxtVwBidBuyerName.setText(
                listingBids[i].getUserinformation().getFname()+
                        " "+ listingBids[i].getUserinformation().getLname());

        holder.mtxtBuyerBidPrice.setText("$ "+listingBids[i].getBidprice());

        //==================acceptBid=====================

        //==================hide_AcceptButton==============


        //====================setImgae=================
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
        TextView mtxtBuyerBidPrice;

    }

}
