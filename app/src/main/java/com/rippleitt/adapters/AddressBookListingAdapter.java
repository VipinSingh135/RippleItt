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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 06-03-2018.
 */

public class AddressBookListingAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> arr_contacts;

    public AddressBookListingAdapter(Context context,
                                     ArrayList<HashMap<String,String>> arr_contacts) {
        this.context=context;
        this.arr_contacts=arr_contacts;
    }

    @Override
    public int getCount() {
        return arr_contacts.size();
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
            holder= new AddressBookListingAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.address_book_listing, viewGroup, false);
            holder.imgVwAddrListUserPic=(CircleImageView)view.findViewById(R.id.imgVwAddrListUserPic);
            holder.txtVwAddrBookList=(TextView)view.findViewById(R.id.txtVwAddrBookList);


            Picasso.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            arr_contacts
                                    .get(i).get("user_image")))
                    .placeholder(R.drawable.bg_round_border)
                    .error(R.drawable.bg_round_border)
                    .into(holder.imgVwAddrListUserPic);
            //=================set_userName============
            holder.txtVwAddrBookList.setText(arr_contacts.get(i).get("user_name"));


            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (AddressBookListingAdapter.ViewHolder) view.getTag();
        }

return view;
    }
    static class ViewHolder {
        CircleImageView imgVwAddrListUserPic;
        TextView txtVwAddrBookList;

    }
}
