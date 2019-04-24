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
import com.rippleitt.activities.ActivityAddListingStep1;
import com.rippleitt.activities.ActivityAddListingStep2;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.webservices.DeleteListingProductApi;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by hp on 06-03-2018.
 */

public class ManageProductListing extends BaseAdapter {

    Context context;
    ListingTemplate[] userListings;
    AVLoadingIndicatorView mLoader;
    public ManageProductListing(Context context, ListingTemplate[] userListingsArr, AVLoadingIndicatorView loader) {
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
        ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new ManageProductListing.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.manage_listing_view, viewGroup, false);
            holder.txtVwProductNameMAnage=(TextView)view.findViewById(R.id.txtVwProductNameMAnage);
            holder.imgVwProductManageList=(ImageView)view.findViewById(R.id.imgVwProductManageList);
            holder.txtVwProductDescrptionMAnage=(TextView)view.findViewById(R.id.txtVwProductDescrptionMAnage);
            holder.txtVwProductPriceMAnage=(TextView)view.findViewById(R.id.txtVwProductPriceMAnage);
            holder.mrelLayoutMangeList=(RelativeLayout) view.findViewById(R.id.relLayoutMangeList);
            holder.mrelMenuManage=(RelativeLayout) view.findViewById(R.id.relMenuManage);
            holder.mTxtVwDraftStatus=(TextView) view.findViewById(R.id.txtvwDraftStatus);
            holder.mFrmLytBadge=(FrameLayout) view.findViewById(R.id.frmlytOrderFlagColor);
            holder.mTxtvwBadgeStatus=(TextView)view.findViewById(R.id.txtvwLabelOrderStatus);
            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (ManageProductListing.ViewHolder) view.getTag();
        }

    //==============set data=============
        holder.txtVwProductNameMAnage.setText(userListings[i].getProductname());
        holder.txtVwProductDescrptionMAnage.setText(userListings[i].getListing_description());
        holder.txtVwProductPriceMAnage.setText("$"+userListings[i].getListing_price());

        if (userListings[i].getQuantity()==null || userListings[i].getQuantity().length()<1){
            holder.mTxtVwDraftStatus.setText("Drafted");
            holder.txtVwProductPriceMAnage.setVisibility(View.GONE);

        }else {
            holder.txtVwProductPriceMAnage.setVisibility(View.VISIBLE);
            holder.mTxtVwDraftStatus.setText(userListings[i].getListing_flag_name());
        }
        Log.e(userListings[i].getListing_flag_name(),userListings[i].getListing_flag());

        if(userListings[i].getListing_flag().equalsIgnoreCase("5")
           || userListings[i].getListing_flag().equalsIgnoreCase("6")){
                holder.mTxtVwDraftStatus.setTextColor(Color.RED);
        }else{
            holder.mTxtVwDraftStatus.setTextColor(Color.parseColor("#65b488"));
        }

    //====================setImgae=================

        if(userListings[i].getListing_photos().length!=0){


            Glide.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            userListings[i].getListing_photos()[0].getPhoto_path()
                    ))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgVwProductManageList);


        }

    //================click on menu==================
        holder.mrelMenuManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Edit","Delete","Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Edit")) {

                            RippleittAppInstance.getInstance()
                                    .setCURRENT_LISTING_OBJECT(userListings[i]);
                            if (userListings[i].getQuantity()!=null && userListings[i].getQuantity().trim().length()>0) {
                                context.startActivity(new Intent(context,
                                        ActivityAddListingStep2.class));
                            }else{
                                context.startActivity(new Intent(context,
                                        ActivityAddListingStep1.class));
                            }
                        }
                        if (options[item].equals("Delete")) {
                            showDeletionConfirmation((userListings[i].getListing_id()));
                           // new DeleteListingProductApi().deleteProductApi(context,arr_details.get(i).get("product_id"));
                        }
                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        //==============================================

        if (userListings[i].getListing_type().equals("1")){
            holder.mFrmLytBadge.setVisibility(View.VISIBLE);
        }else{
            holder.mFrmLytBadge.setVisibility(View.GONE);
            holder.txtVwProductPriceMAnage.setVisibility(View.GONE);
        }

        if(userListings[i].getIs_new().equals("1")){
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#2f4fb9"));
            holder.mTxtvwBadgeStatus.setText(" New ");
        }else{
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#d2742d"));
            holder.mTxtvwBadgeStatus.setText(" Used ");
        }

        return view;
    }


    private void showDeletionConfirmation(final String productID){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this listing?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context,"Deleting listing please wait",Toast.LENGTH_LONG).show();
                         new DeleteListingProductApi()
                                 .deleteProductApi(context,productID,mLoader);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    static class ViewHolder {
        ImageView imgVwProductManageList;
        TextView txtVwProductNameMAnage;
        TextView txtVwProductDescrptionMAnage;
        TextView txtVwProductPriceMAnage;
        TextView mTxtVwDraftStatus;
        RelativeLayout mrelLayoutMangeList;
        RelativeLayout mrelMenuManage;
        FrameLayout mFrmLytBadge;
        TextView mTxtvwBadgeStatus;
    }

}
