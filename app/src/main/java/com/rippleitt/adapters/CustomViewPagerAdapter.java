package com.rippleitt.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityFullScreenImageView;
import com.rippleitt.controller.RippleittAppInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pc on password/13/2018.
 */

public class CustomViewPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomViewPagerAdapter(Context mContext) {
        this.mContext = mContext;
       // this.arry_pics=arry_pics;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
       // return mResources.length;
        return RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getListing_photos().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.view_pager_layout, container, false);

        ImageView imageViewProduct = (ImageView) itemView.findViewById(R.id.imageViewProduct);
     /*   imageViewProduct.setImageResource(mResources[position]);*/



        itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            RippleittAppInstance.getInstance().setCURRENT_SELECTED_IMAGE_POSITION(position);
            mContext.startActivity(new Intent(mContext, ActivityFullScreenImageView.class));
            }
        });


        Glide.with(mContext)
                .load(RippleittAppInstance.formatPicPath(RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_photos()[position].getPhoto_path()))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(imageViewProduct);

        /*
        Picasso.with(mContext)
                .load(RippleittAppInstance.formatPicPath(RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_photos()[position].getPhoto_path()))
                .placeholder(R.drawable.rounded_corner_)
                .error(R.drawable.bg_round_border)
                .into(imageViewProduct);
                */

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
