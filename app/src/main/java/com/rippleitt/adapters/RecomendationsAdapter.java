package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pc on mail/user/2018.
 */

public class RecomendationsAdapter  extends BaseAdapter {

    private Context parentRef;
    private ListingTemplate[] data;
    private LayoutInflater mInflater;

    public RecomendationsAdapter(Context context, ListingTemplate[] listngs){
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
        ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new ViewHolder();
            LayoutInflater inflater = ((Activity)parentRef).getLayoutInflater();
            view = inflater.inflate(R.layout.layout_my_recomendations, viewGroup,
                    false);
            holder.mImgVwProductThumb=(ImageView)view.findViewById(R.id.imgVwProductThumb);
            holder.mTxtVwProductTitle=(TextView)view.findViewById(R.id.txtVwProductNam);
            holder.mTxtVwProductAskPrice=(TextView)view.findViewById(R.id.txtvwItemAskPrice);
            holder.mTxtVwProductDescription=(TextView)view.findViewById(R.id.txtVwProductDescrption);
            holder.mTxtVwReferralCount=(TextView)view.findViewById(R.id.txtVwUserCount);
            holder.mImageViewFirstReferral=(CircleImageView) view.findViewById(R.id.imgVFirstRefferee);
            holder.mImageViewSecondReferral=(CircleImageView) view.findViewById(R.id.imgVSecondRefferee);
            holder.mImageViewThirdReferral=(CircleImageView) view.findViewById(R.id.imgVwThirdRefferee);


            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (ViewHolder) view.getTag();
        }


        //====================setImgae=================

        if(data[i].getListing_photos().length!=0){

            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getListing_photos()[0].getPhoto_path()
                    ))
                    .override(80,80)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImgVwProductThumb);
        }

        holder.mTxtVwProductAskPrice.setText("$"+data[i].getListing_price());
        holder.mTxtVwProductAskPrice.setVisibility(View.INVISIBLE);
        holder.mTxtVwProductTitle.setText(data[i].getListing_name());
        holder.mTxtVwProductDescription.setText(data[i].getListing_description());
        int referreeCount = data[i].getSender_array().length;
        if(referreeCount>3){
            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getSender_array()[0].getImage()))
                    .override(20,20)
                    .placeholder(R.drawable.default_profile_icon)
                    .error(R.drawable.default_profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageViewFirstReferral);
            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getSender_array()[1].getImage()))
                    .override(20,20)
                    .placeholder(R.drawable.default_profile_icon)
                    .error(R.drawable.default_profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageViewSecondReferral);
            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getSender_array()[2].getImage()))
                    .override(20,20)
                    .placeholder(R.drawable.default_profile_icon)
                    .error(R.drawable.default_profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageViewThirdReferral);
            holder.mTxtVwReferralCount.setText("and "+Integer.toString(referreeCount-3)+" others");
        }else{
            holder.mTxtVwReferralCount.setVisibility(View.GONE);
        }
        if(referreeCount==1){
            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getSender_array()[0].getImage()))
                    .override(30,30)
                    .error(R.drawable.default_profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageViewFirstReferral);
            holder.mImageViewSecondReferral.setVisibility(View.GONE);
            holder.mImageViewThirdReferral.setVisibility(View.GONE);
        }if(referreeCount==2){
            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getSender_array()[0].getImage()))
                    .override(30,30)
                    .error(R.drawable.default_profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageViewFirstReferral);
            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getSender_array()[1].getImage()))
                    .override(30,30)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.default_profile_icon)
                    .into(holder.mImageViewSecondReferral);
            holder.mImageViewThirdReferral.setVisibility(View.GONE);
        }if(referreeCount==3){
            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getSender_array()[0].getImage()))
                    .override(30,30)
                    .error(R.drawable.default_profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageViewFirstReferral);
            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getSender_array()[1].getImage()))
                    .override(30,30)
                    .error(R.drawable.default_profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageViewSecondReferral);
            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getSender_array()[2].getImage()))
                    .override(30,30)
                    .error(R.drawable.default_profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageViewThirdReferral);
        }

        return view;
    }


    static class ViewHolder {
        ImageView mImgVwProductThumb;
        TextView mTxtVwProductTitle;
        TextView mTxtVwProductAskPrice;
        TextView mTxtVwProductDescription;
        TextView mTxtVwReferralCount;
        CircleImageView mImageViewFirstReferral,
                mImageViewSecondReferral,
                mImageViewThirdReferral;

    }
}
