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
import com.rippleitt.modals.UserReviewTemplate;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 06-03-2018.
 */

public class ReviewListAdapter extends BaseAdapter {

    Context context;
    private UserReviewTemplate[] data;

    public ReviewListAdapter(Context context, UserReviewTemplate[] userReviews) {
        this.context=context;
        data=userReviews;
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
            holder= new ReviewListAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.review_list_view, viewGroup, false);
            holder.imgvwProfilePic=(CircleImageView)view.findViewById(R.id.imgVwUserReviewPic);
            holder.txtvwReviewText=(TextView)view.findViewById(R.id.txtvwReviewBody);
            holder.txtvwUserName=(TextView)view.findViewById(R.id.txtvwUserName);
            view.setTag(holder);
        } else {
            holder = (ReviewListAdapter.ViewHolder) view.getTag();
        }

        holder.txtvwReviewText.setText(data[i].getText());
        holder.txtvwUserName.setText(data[i].getFname()+" "+data[i].getLname());
        try{
            Glide.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getImage())
                    )
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.default_profile_icon)
                    .error(R.drawable.default_profile_icon)
                    .into(holder.imgvwProfilePic);
        }catch (Exception e){
            e.printStackTrace();
            holder.imgvwProfilePic.setImageResource(R.drawable.default_profile_icon);
        }

return view;
    }
    static class ViewHolder {
        CircleImageView imgvwProfilePic;
        TextView txtvwUserName,txtvwReviewText;

    }
}
