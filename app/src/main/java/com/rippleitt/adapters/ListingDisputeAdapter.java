package com.rippleitt.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.activities.ActivityAddDispute;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingDisputeTemplate;
import com.rippleitt.modals.ListingFaqTemplate;

public class ListingDisputeAdapter extends BaseAdapter {

    private ListingDisputeTemplate[] data;
    private Context context;
    private LayoutInflater mInflater;

    public ListingDisputeAdapter(Context parentReference, ListingDisputeTemplate[] DisputeArray){
        this.data=DisputeArray;
        context=parentReference;
        mInflater=LayoutInflater.from(parentReference);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ListingDisputeAdapter.ViewHolder holder;
        if(view==null){
            holder=new ListingDisputeAdapter.ViewHolder();
            view= mInflater.inflate(R.layout.adapter_dispute_listing,null);
            holder.mLinLytQuestionBox=(LinearLayout)view.findViewById(R.id.linlytQuestionBox);
            holder.mTxtVwQuestion=(TextView)view.findViewById(R.id.txtvwQuestion);
            holder.mTxtVwQuestionTime=(TextView)view.findViewById(R.id.txtvwQuestionDate);
            view.setTag(holder);
        } else{
            holder=(ListingDisputeAdapter.ViewHolder)view.getTag();
        }
        holder.mTxtVwQuestion.setText("" + data[i].getMessage());
        holder.mTxtVwQuestionTime.setText(data[i].getPostedBy() + " " + data[i].getPostedOn());

        if (data[i].getPostedBy().equals("Admin")) {
//            holder.mLinLytQuestionBox.setBackground(context.getResources().getDrawable(R.drawable.answet_box_bg));
            holder.mTxtVwQuestionTime.setTextColor(Color.BLACK);
        }else if (data[i].getPostedBy().equals(PreferenceHandler.readString(context,
                PreferenceHandler.AUTH_TOKEN, ""))){
            holder.mTxtVwQuestionTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else{
            holder.mTxtVwQuestionTime.setTextColor(context.getResources().getColor(R.color.color_3));
        }
        return view;
    }


    private class ViewHolder{
        private TextView mTxtVwQuestion,
                mTxtVwQuestionTime;
        private LinearLayout  mLinLytQuestionBox;
    }
}
