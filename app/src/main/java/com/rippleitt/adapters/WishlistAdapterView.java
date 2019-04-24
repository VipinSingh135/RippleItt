package com.rippleitt.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.rippleitt.R;
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.callback.WishlistSelectCallback;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.webservices.FetchWishlistApi;
import com.rippleitt.webservices.GetProductDetailsApi;
import com.rippleitt.webservices.RemoveProductWishlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 06-03-2018.
 */

public class WishlistAdapterView extends  RecyclerView.Adapter<WishlistAdapterView.MyViewHolder>{

    private  ArrayList<HashMap<String,String>> productDetails;
    Context context;
    WishlistSelectCallback wishlistCallback;

    public WishlistAdapterView(Context context, ArrayList<HashMap<String,String>> productDetails, WishlistSelectCallback callback) {
        this.context=context;
        this.productDetails = productDetails;
        wishlistCallback=callback;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.mimageProductWishlist.setImageResource((Integer) productImages.get(position));
       holder.mrelAddWishlistImage.setBackgroundResource(R.drawable.wishlist_red_icon);
       holder.mtxtVwProductWishlistTitle.setText(productDetails.get(position).get("product_name"));
       holder.mtxtVwProductWishlistPrice.setText("$"+productDetails.get(position).get("product_price"));
       holder.mtxtVwProductWishlistDetail.setText(productDetails.get(position).get("product_desc"));
       try{
           holder.mtxtVwUserNameDetails.setText(productDetails.get(position).get("user_name").split(" ")[0]);
       }catch (Exception e){
           holder.mtxtVwUserNameDetails.setText(productDetails.get(position).get("user_name"));
       }

        holder.mtxtVwRefferAmount.setText("Earn: $"+productDetails.get(position).get("refer_amount"));

        if(productDetails
                .get(position).get("user_image").equalsIgnoreCase("null")
         ||
                productDetails
                        .get(position).get("user_image").equalsIgnoreCase("")
                ){

            holder.imgVwprofileImageHome.setImageResource(R.drawable.default_profile_icon);
        }else {


            Picasso.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            productDetails
                                    .get(position).get("user_image")))
                    .placeholder(R.drawable.default_profile_icon)
                    .error(R.drawable.default_profile_icon)
                    .into(holder.imgVwprofileImageHome);
        }



        if (productDetails.get(position).get("listing_type").equals("1")){
            holder.mFrmLytBadge.setVisibility(View.VISIBLE);
            holder.mtxtVwProductWishlistPrice.setVisibility(View.VISIBLE);
            holder.mtxtVwRefferAmount.setVisibility(View.GONE);
        }else {
            holder.mFrmLytBadge.setVisibility(View.GONE);
            holder.mtxtVwProductWishlistPrice.setVisibility(View.GONE);
            holder.mtxtVwRefferAmount.setVisibility(View.GONE);
        }

        if(productDetails.get(position).get("is_new")!=null && !productDetails.get(position).get("is_new").equals("1")){
            holder.mFrmLytBadge.setBackground(context.getResources().getDrawable(R.drawable.curved_top_grey_background));
            holder.txtvwLabelOrderStatus.setText(" Used ");
        }else{
            holder.mFrmLytBadge.setBackground(context.getResources().getDrawable(R.drawable.curved_top_green_background));
            holder.txtvwLabelOrderStatus.setText(" New ");
        }


        Glide.with(context)
                .load(RippleittAppInstance.formatPicPath(
                        productDetails
                                .get(position).get("product_pic")))
                .into(holder.mimageProductWishlist);



        //=============remove_item_from_wishlist=====

        holder.mrelAddWishlistImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new RemoveProductWishlist().remoeProductWishlistApi(context,"1",
                       productDetails.get(position).get("product_id"),wishlistCallback);
            }
        });
        //=============onitem click=========
        holder.mlinrListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wishlistCallback.onWishListitemSelected(productDetails.get(position).get("product_id"));
                /*RippleittAppInstance.PRODUCT_ID=productDetails.get(position).get("product_id");
                Log.e("PRODUCT_ID_DETAILS",RippleittAppInstance.PRODUCT_ID);
                new GetProductDetailsApi().getProductDetailsApi(context, RippleittAppInstance.PRODUCT_ID);*/
              //  context.startActivity(new Intent(context, ProductDetailsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mimageProductWishlist;
        RelativeLayout mrelAddWishlistImage;
        TextView mtxtVwProductWishlistTitle;
        TextView mtxtVwProductWishlistPrice;
        TextView mtxtVwProductWishlistDetail;
        TextView mtxtVwUserNameDetails;
        TextView mtxtVwRefferAmount;
        TextView txtvwLabelOrderStatus;
        CircleImageView imgVwprofileImageHome;
        LinearLayout mlinrListItem;
        FrameLayout mFrmLytBadge;
        public MyViewHolder(View itemView) {
            super(itemView);
            mimageProductWishlist=(ImageView)itemView.findViewById(R.id.imageProductWishlist);
            mrelAddWishlistImage=(RelativeLayout) itemView.findViewById(R.id.relAddWishlistImage);
            mlinrListItem=(LinearLayout) itemView.findViewById(R.id.linrListItem);
            mtxtVwProductWishlistTitle=(TextView) itemView.findViewById(R.id.txtVwProductWishlistTitle);
            mtxtVwProductWishlistPrice=(TextView) itemView.findViewById(R.id.txtVwProductWishlistPrice);
            mtxtVwProductWishlistDetail=(TextView) itemView.findViewById(R.id.txtVwProductWishlistDetail);
            imgVwprofileImageHome=(CircleImageView) itemView.findViewById(R.id.imgVwprofileImageHome);
            mtxtVwUserNameDetails=(TextView) itemView.findViewById(R.id.txtVwUserNameHome);
            mtxtVwRefferAmount=(TextView) itemView.findViewById(R.id.txtVwProductReferAmount);
            mFrmLytBadge=(FrameLayout) itemView.findViewById(R.id.frmlytOrderFlagColor);
            txtvwLabelOrderStatus=(TextView) itemView.findViewById(R.id.txtvwLabelOrderStatus);
            // get the reference of item view's
        }
    }
}
