package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.fragments.FragmentMessages;
import com.rippleitt.modals.NotificationTemplate;
import com.rippleitt.modals.NotificationsResponseTemplate;

/**
 * Created by hp on 06-03-2018.
 */

public class NotificationListAdapter extends BaseAdapter {

    Context context;
    NotificationTemplate[] notificationsPacket;

    public NotificationListAdapter(Context context, NotificationTemplate[] data) {
        this.context=context;
        notificationsPacket=data;
    }

    @Override
    public int getCount() {
        return notificationsPacket.length ;
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
            holder= new NotificationListAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.notification_listing_view, viewGroup, false);
            holder.txtvwNotificationsText=(TextView) view.findViewById(R.id.txtVwUserNameNotification);
            holder.txtvwTimestamp=(TextView)view.findViewById(R.id.txtvwTimeStamp);

            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (NotificationListAdapter.ViewHolder) view.getTag();
        }

        holder.txtvwTimestamp.setText(notificationsPacket[i].getTime());
        holder.txtvwNotificationsText.setText(notificationsPacket[i].getMessage());
        return view;
    }
    static class ViewHolder {
        TextView txtvwNotificationsText,  txtvwTimestamp;

    }
}
