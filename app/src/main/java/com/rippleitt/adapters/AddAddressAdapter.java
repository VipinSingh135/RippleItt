package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ContactSharingTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 06-03-2018.
 */

public class AddAddressAdapter extends BaseAdapter {

    Context context;
    ArrayList<ContactSharingTemplate> arr_contacts;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private TreeSet mSeparatorsSet = new TreeSet();

    public AddAddressAdapter(Context context,  ArrayList<ContactSharingTemplate>arr_contacts) {
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

        if (view == null) {
            holder= new ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.layout_share_addressbook, viewGroup, false);
            holder.imgVwAddrListUserPic=(CircleImageView)view.findViewById(R.id.imgVwAddrListUserPic);
            holder.txtVwAddrBookList=(TextView)view.findViewById(R.id.txtVwAddrBookList);
            holder.mImgVwShareStatus=(ImageView) view.findViewById(R.id.imgvwShareTick);
            holder.mLinlytHeader=(LinearLayout)view.findViewById(R.id.linlytHeader);
            holder.mTxtVwHeaderTitle=(TextView)view.findViewById(R.id.txtvwHeaderTitle);

            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(arr_contacts.get(i).isIslabel()){
            holder.txtVwAddrBookList.setVisibility(View.GONE);
            holder.imgVwAddrListUserPic.setVisibility(View.GONE);
            holder.mImgVwShareStatus.setVisibility(View.GONE);
            holder.imgVwAddrListUserPic.setVisibility(View.GONE);
            holder.mLinlytHeader.setVisibility(View.VISIBLE);
            holder.mTxtVwHeaderTitle.setText(arr_contacts.get(i).getTitle());
        }
        else{
            holder.txtVwAddrBookList.setVisibility(View.VISIBLE);
            holder.imgVwAddrListUserPic.setVisibility(View.VISIBLE);
            holder.mImgVwShareStatus.setVisibility(View.VISIBLE);
            holder.imgVwAddrListUserPic.setVisibility(View.VISIBLE);
            holder.mLinlytHeader.setVisibility(View.GONE);
            try{
                Picasso.with(context)
                        .load(RippleittAppInstance.formatPicPath(
                                arr_contacts
                                        .get(i).getImage()))
                        .placeholder(R.drawable.default_profile_icon)
                        .error(R.drawable.default_profile_icon)
                        .into(holder.imgVwAddrListUserPic);
            }catch (Exception e){
                holder.imgVwAddrListUserPic.setImageResource(R.drawable.default_profile_icon);
            }

            //=================set_userName============
            holder.txtVwAddrBookList.setText(arr_contacts.get(i).getName()
                    +" "+ arr_contacts.get(i).getLastname());

            if(arr_contacts.get(i).getShare()==0){
                holder.mImgVwShareStatus.setVisibility(View.GONE);
            }else{
                holder.mImgVwShareStatus.setVisibility(View.VISIBLE);
            }

        }

return view;
    }
    static class ViewHolder {
        CircleImageView imgVwAddrListUserPic;
        TextView txtVwAddrBookList;
        ImageView mImgVwShareStatus;
        LinearLayout mLinlytHeader;
        TextView mTxtVwHeaderTitle;

    }


}
