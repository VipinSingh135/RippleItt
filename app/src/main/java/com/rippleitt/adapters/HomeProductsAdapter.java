package com.rippleitt.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rippleitt.R;
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.callback.HomeProductTapped;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.webservices.AddToWishListApi;
import com.rippleitt.webservices.GetProductDetailsApi;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 06-03-2018.
 */

public class HomeProductsAdapter extends RecyclerView.Adapter<HomeProductsAdapter.MyViewHolder> {

    Context context;
    HomeProductTapped callbackReference;
    int currentFilterMode = 1;
    private ArrayList<ListingTemplate> results = new ArrayList<>();

    public HomeProductsAdapter(Context context,
                               HomeProductTapped callback,
                               ArrayList<ListingTemplate> data) {
        this.context = context;
        callbackReference = callback;
        this.results = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_view,
                parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.mimageProductWishlist.setImageResource((Integer) productImages.get(position));
        holder.mrelAddWishlistImage.setBackgroundResource(R.drawable.wishlist_icon);
        holder.mtxtVwProductWishlistTitle.setText(results.get(position).getProductname());
        holder.mtxtVwProductWishlistPrice.setText("$" + results.get(position).getListing_price());
        holder.mtxtVwProductWishlistDetail.setText(results.get(position).getListing_description());
        holder.mtxtVwUserNameDetails.setText(results.get(position).getUserinformation().getFname());
        holder.mrelAddWishlistImage.setVisibility(View.GONE);
        if (results.get(position).getRefer_amount().equalsIgnoreCase("")) {
            holder.mTxtVwReferAmount.setVisibility(View.GONE);
        } else {
            holder.mTxtVwReferAmount.setText("Earn: "
                    + results.get(position)
                    .getRefer_amount()
            );
        }

        if (results.get(position).getHas_voucher().equalsIgnoreCase("1") && results.get(position).getVoucher_details() != null) {
            holder.mtxtVwVoucherPrice.setVisibility(View.VISIBLE);
            if (results.get(position).getVoucher_details().getType().equals("1")) {
                holder.mtxtVwVoucherPrice.setText("$"+results.get(position).getVoucher_details().getAmount()+" 0ff");
            } else if (results.get(position).getVoucher_details().getType().equals("2")) {
                holder.mtxtVwVoucherPrice.setText(results.get(position).getVoucher_details().getAmount()+"% 0ff");
            } else if (results.get(position).getVoucher_details().getType().equals("3")) {
                holder.mtxtVwVoucherPrice.setText("Free Session");
            }
        } else {
            holder.mtxtVwVoucherPrice.setVisibility(View.GONE);
        }

        if (results.get(position).getListing_type().equals("1")) {
            holder.mFrmLytBadge.setVisibility(View.VISIBLE);
            holder.mtxtVwProductWishlistPrice.setVisibility(View.VISIBLE);
            holder.mTxtVwReferAmount.setVisibility(View.GONE);
        } else {
            holder.mFrmLytBadge.setVisibility(View.GONE);
            holder.mtxtVwProductWishlistPrice.setVisibility(View.VISIBLE);
            holder.mTxtVwReferAmount.setVisibility(View.GONE);
        }
        if (results.get(position).getIs_new().equals("1")) {
            holder.mFrmLytBadge.setBackground(context.getResources().getDrawable(R.drawable.curved_top_green_background));
            holder.mTxtvwBadgeStatus.setText(" New ");
        } else {
            holder.mFrmLytBadge.setBackground(context.getResources().getDrawable(R.drawable.curved_top_grey_background));
            holder.mTxtvwBadgeStatus.setText(" Used ");
        }

        if ((results.get(position)
                .getListing_photos().length != 0)) {
            try {
                if (results
                        .get(position)
                        .getListing_photos()[0].getPhoto_path().equalsIgnoreCase("null")
                        || results
                        .get(position)
                        .getListing_photos()[0].getPhoto_path().equalsIgnoreCase("")) {
                    holder.mimageProductWishlist.setImageResource(R.drawable.default_profile_icon);
                } else {

                    Glide.with(context)
                            .load(RippleittAppInstance.formatPicPath(
                                    results
                                            .get(position)
                                            .getListing_photos()[0].getPhoto_path()))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(holder.mimageProductWishlist);
                }

            } catch (Exception e) {
                holder.mimageProductWishlist.setImageResource(R.drawable.default_profile_icon);
            }
        }

        try {
            Picasso.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            results
                                    .get(position)
                                    .getUserinformation().getImage()))
                    .error(R.drawable.default_profile_icon)
                    .placeholder(R.drawable.default_profile_icon)
                    .into(holder.imgVwprofileImageHome)
            ;

        } catch (Exception e) {
            holder.imgVwprofileImageHome.setImageResource(R.drawable.default_profile_icon);
        }


        //=============onitem click=========
        holder.mlinrListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callbackReference.itemSelected(position);
                } catch (Exception e) {

                }


            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return results.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView mimageProductWishlist;
        RelativeLayout mrelAddWishlistImage;
        TextView mtxtVwProductWishlistTitle;
        TextView mtxtVwProductWishlistPrice;
        TextView mtxtVwVoucherPrice;
        TextView mtxtVwProductWishlistDetail;
        TextView mtxtVwUserNameDetails;
        CircleImageView imgVwprofileImageHome;
        TextView mTxtVwReferAmount;
        LinearLayout mlinrListItem;
        FrameLayout mFrmLytBadge;
        TextView mTxtvwBadgeStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            mimageProductWishlist = (RoundedImageView) itemView.findViewById(R.id.imageProductWishlist);
            mrelAddWishlistImage = (RelativeLayout) itemView.findViewById(R.id.relAddWishlistImage);
            mlinrListItem = (LinearLayout) itemView.findViewById(R.id.linrListItem);
            mtxtVwProductWishlistTitle = (TextView) itemView.findViewById(R.id.txtVwProductWishlistTitle);
            mtxtVwProductWishlistPrice = (TextView) itemView.findViewById(R.id.txtVwProductWishlistPrice);
            mtxtVwVoucherPrice = (TextView) itemView.findViewById(R.id.txtVwVoucherPrice);
            mtxtVwProductWishlistDetail = (TextView) itemView.findViewById(R.id.txtVwProductWishlistDetail);
            imgVwprofileImageHome = (CircleImageView) itemView.findViewById(R.id.imgVwprofileImageHome);
            mtxtVwUserNameDetails = (TextView) itemView.findViewById(R.id.txtVwUserNameHome);
            mTxtVwReferAmount = (TextView) itemView.findViewById(R.id.txtVwProductReferAmount);
            mFrmLytBadge = (FrameLayout) itemView.findViewById(R.id.frmlytOrderFlagColor);
            mTxtvwBadgeStatus = (TextView) itemView.findViewById(R.id.txtvwLabelOrderStatus);
            //relAddWishlistImage
            // get the reference of item view's
        }
    }
}
