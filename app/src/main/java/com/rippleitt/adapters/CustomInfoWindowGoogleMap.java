package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;

/**
 * Created by pc on password/29/2018.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private LayoutInflater lInflater;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
        lInflater=LayoutInflater.from(ctx);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = lInflater
                .inflate(R.layout.layout_custom_info_window, null);

        TextView tvTitle = view.findViewById(R.id.txtVwProductWishlistTitle);
        TextView tvDetails = view.findViewById(R.id.txtVwProductWishlistDetail);
        TextView tvUserName= view.findViewById(R.id.txtVwUserNameHome);
        TextView tvProductPrice = view.findViewById(R.id.txtVwProductWishlistPrice);
        TextView tvReferalAmount = view.findViewById(R.id.txtVwProductReferAmount);
        RoundedImageView imgvwProductImage=view.findViewById(R.id.imageProductWishlist);

        ListingTemplate infoWindowData = (ListingTemplate) marker.getTag();


        tvTitle.setText(infoWindowData.getProductname());
        tvDetails.setText(infoWindowData.getListing_description());
        tvUserName.setText(infoWindowData.getUserinformation().getFullName());
        tvProductPrice.setText("$"+infoWindowData.getListing_price());
        tvReferalAmount.setText("Earn :"+infoWindowData.getRefer_amount());

        if(infoWindowData.getListing_photos().length!=0){
            try{
                if(infoWindowData
                        .getListing_photos()[0].getPhoto_path().equalsIgnoreCase("null")
                        || infoWindowData
                        .getListing_photos()[0].getPhoto_path().equalsIgnoreCase("")){
                    imgvwProductImage.setImageResource(R.drawable.default_profile_icon);
                }else{
                    Glide.with(context)
                            .load(RippleittAppInstance.formatPicPath(
                                    infoWindowData
                                            .getListing_photos()[0].getPhoto_path()))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(imgvwProductImage);
                }

            }catch (Exception e){
                imgvwProductImage.setImageResource(R.drawable.default_profile_icon);
            }
        }

        try{
            Glide.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            infoWindowData
                                    .getUserinformation().getImage()))
                    .fitCenter()
                    .into(imgvwProductImage);
        }catch (Exception e){
            imgvwProductImage.setImageResource(R.drawable.default_profile_icon);
        }

        return view;
    }
}
