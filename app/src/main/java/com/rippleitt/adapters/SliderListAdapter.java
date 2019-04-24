package com.rippleitt.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.modals.SliderItems;

import java.util.ArrayList;

/**
 * Created by manishautomatic on 04/03/18.
 */

public class SliderListAdapter extends BaseAdapter {

    private int selectedIndex=0;
    private Context parentReference;
    private ArrayList<SliderItems> sliderItemsReference;
    private LayoutInflater mInflater;

    public SliderListAdapter(Context context , ArrayList<SliderItems> sliderItems){
        this.parentReference=context;
        this.sliderItemsReference=sliderItems;
        mInflater=LayoutInflater.from(parentReference);
    }

    public void notifyAdapter(ArrayList<SliderItems> sliderItems ){
        sliderItemsReference= sliderItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sliderItemsReference.size();
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
        if(view==null){
            holder=new ViewHolder();
            view = mInflater.inflate(R.layout.layout_slider_row_item,null);
            holder.imageViewDrawable=(ImageView)view.findViewById(R.id.imgvwItemIcon);
            holder.viewMessage=(View) view.findViewById(R.id.viewMessage);
            holder.mTxtVwOptionLable=(TextView)view.findViewById(R.id.txtvwSliderItem);
            holder.mFrmLytDivider=(FrameLayout)view.findViewById(R.id.frmlytDivider);
            holder.mSelectorHighlighter=(FrameLayout)view.findViewById(R.id.frmlytHighlighter);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        if (sliderItemsReference.get(i).isMessage()){
            holder.viewMessage.setVisibility(View.VISIBLE);
//            holder.linearLayoutItem.setBackgroundColor(parentReference.getResources().getColor(R.color.clear_light));
        }else{
            holder.viewMessage.setVisibility(View.GONE);
//            holder.linearLayoutItem.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.imageViewDrawable.setImageResource(sliderItemsReference.get(i).getSliderDrawableResource());
        holder.mTxtVwOptionLable.setText(sliderItemsReference.get(i).getSliderOptionName());
        if(i==selectedIndex){
            holder.mSelectorHighlighter.setBackgroundColor(Color.parseColor("#289b98"));
        }else{
            holder.mSelectorHighlighter.setBackgroundColor(Color.parseColor("#313131"));
        }

        return view;
    }


    private class ViewHolder{
        TextView mTxtVwOptionLable;
        ImageView imageViewDrawable;
        View viewMessage;
        FrameLayout mSelectorHighlighter, mFrmLytDivider;
    }




    public void updateSelectionIndex(int selectedPosition){
        this.selectedIndex=selectedPosition;
    }
}
