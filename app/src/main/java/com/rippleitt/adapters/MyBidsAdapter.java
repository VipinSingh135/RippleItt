package com.rippleitt.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.webservices.DeleteListingProductApi;

import java.util.LinkedHashMap;

/**
 * Created by pc on password/27/2018.
 */

public class MyBidsAdapter extends BaseAdapter {

    Context context;
    ListingTemplate[] userListings;
    public MyBidsAdapter(Context context, ListingTemplate[] userListingsArr) {
        this.context=context;
        this.userListings=userListingsArr;
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
        ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.layout_my_bids_adapter, viewGroup,
                    false);
            holder.mImgVwProductThumb=(ImageView)view.findViewById(R.id.imgVwProductThumb);
            holder.mTxtVwProductTitle=(TextView)view.findViewById(R.id.txtVwProductNam);
            holder.mTxtVwProductAskPrice=(TextView)view.findViewById(R.id.txtvwItemAskPrice);
            holder.mTxtVwProductDescription=(TextView)view.findViewById(R.id.txtVwProductDescrption);
            holder.mTxtVwProductUserBidAmount=(TextView)view.findViewById(R.id.txtVwUserBid);
            holder.mLinLytAcceptBox=(LinearLayout)view.findViewById(R.id.linlytAcceptBox);

            holder.mImgVwTick=(ImageView)view.findViewById(R.id.imgvwAcceptTick);

            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (ViewHolder) view.getTag();
        }


        //====================setImgae=================
        if(userListings[i].getIs_mybids_accepted().equalsIgnoreCase("1")){
            holder.mImgVwTick.setVisibility(View.VISIBLE);
            holder.mLinLytAcceptBox.setVisibility(View.VISIBLE);
        }else{
            holder.mImgVwTick.setVisibility(View.GONE);
            holder.mLinLytAcceptBox.setVisibility(View.GONE);
        }
        if(userListings[i].getListing_photos().length!=0){

            Glide.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            userListings[i].getListing_photos()[0].getPhoto_path()
                    ))
                    .override(80,80)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImgVwProductThumb);
        }

        holder.mTxtVwProductAskPrice.setText("$"+userListings[i].getListing_price());
        holder.mTxtVwProductTitle.setText(userListings[i].getListing_name());
        holder.mTxtVwProductDescription.setText(userListings[i].getListing_description());
        holder.mTxtVwProductUserBidAmount.setText("Your Offer: $"+userListings[i]
                                                   .getMy_bid_amount()
                    +" ("+userListings[i].getQuantity()
                    +" Qty"
                    +")");

        return view;
    }

   /* private void showDeletionConfirmation(final String productID){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this listing?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context,"Deleting listing please wait",Toast.LENGTH_LONG).show();
                        new DeleteListingProductApi()
                                .deleteProductApi(context,productID);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }*/

    static class ViewHolder {
        ImageView mImgVwProductThumb;
        TextView mTxtVwProductTitle;
        TextView mTxtVwProductAskPrice;
        TextView mTxtVwProductDescription;
        TextView mTxtVwProductUserBidAmount;
        LinearLayout mLinLytAcceptBox;
        ImageView mImgVwTick;
    }


}
