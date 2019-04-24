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
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingOwnerTemplate;
import com.squareup.picasso.Picasso;

/**
 * Created by hp on 06-03-2018.
 */

public class MessagesListAdapter extends BaseAdapter {

    Context context;
    ListingOwnerTemplate[] threads;

    public MessagesListAdapter(Context context, ListingOwnerTemplate[] chatThreads) {
        this.context=context;
        this.threads=chatThreads;
    }

    @Override
    public int getCount() {
        return threads.length;
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
            holder= new MessagesListAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.address_book_listing, viewGroup, false);
            holder.imgVwAddrListUserPic=(ImageView)view.findViewById(R.id.imgVwAddrListUserPic);
            holder.txtVwAddrBookList=(TextView)view.findViewById(R.id.txtVwAddrBookList);
            view.setTag(holder);
        } else {
            holder = (MessagesListAdapter.ViewHolder) view.getTag();
        }
        holder.txtVwAddrBookList.setText(threads[i].getFullName());
        Picasso.with(context)
                .load(RippleittAppInstance.formatPicPath(
                        threads[i].getImage()))
                .placeholder(R.drawable.default_profile_icon)
                .error(R.drawable.default_profile_icon)
                .into(holder.imgVwAddrListUserPic);
        return view;
    }
    static class ViewHolder {
        ImageView imgVwAddrListUserPic;
        TextView txtVwAddrBookList;
    }
}
