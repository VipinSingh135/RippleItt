package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.callback.FilterParamSelected;
import com.rippleitt.controller.RippleittAppInstance;

/**
 * Created by manishautomatic on 12/06/18.
 */

public class FilterPriceRangeAdapter extends BaseAdapter {

    Context context;
    private int dataMode=0;  // 1-- categories , 2-- subcategories , password= prices
    private Object[] priceID;
    FilterParamSelected selection;


    public FilterPriceRangeAdapter(Context context, int _mode, FilterParamSelected callback) {
        this.context=context;
        this.dataMode=_mode;
        priceID = RippleittAppInstance
                .getInstance()
                .getCurrentLoadedPriceRanges().keySet().toArray();
        selection=callback;
    }

    @Override
    public int getCount()
    {
        return priceID.length;
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
        FilterSubCategoriesAdapter.ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new FilterSubCategoriesAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.filters_cell_adapter, viewGroup, false);
            holder.txtVwCategoryName=(TextView)view.findViewById(R.id.txtvwCategoryTitle);
            holder.mChkBoxSelectionState=(CheckBox) view.findViewById(R.id.chkbxSelected);
            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (FilterSubCategoriesAdapter.ViewHolder) view.getTag();
        }

        holder.txtVwCategoryName.setText(RippleittAppInstance
                .getInstance()
                .getCurrentLoadedPriceRanges()
                .get(priceID[i]).getTitle());
        if(RippleittAppInstance
                .getInstance()
                .getCurrentLoadedPriceRanges()
                .get(priceID[i]).getIsTicked()==1
                ||
                RippleittAppInstance.getInstance()
                        .getSearchPriceRangePayload().containsKey(priceID[i])
                ){
            holder.mChkBoxSelectionState.setChecked(true);
        }else{
            holder.mChkBoxSelectionState.setChecked(false);
        }

        holder.mChkBoxSelectionState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(RippleittAppInstance
                        .getInstance()
                        .getCurrentLoadedPriceRanges()
                        .get(priceID[i]).getIsTicked()==1){
                    ((CheckBox)view).setChecked(false);
                    RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedPriceRanges()
                            .get(priceID[i]).setIsTicked(0);
                    selection.priceRangeSelected(RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedPriceRanges()
                            .get(priceID[i]).getRange_id(),0);
                }else{
                    ((CheckBox)view).setChecked(true);
                    RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedPriceRanges()
                            .get(priceID[i]).setIsTicked(1);
                    selection.priceRangeSelected(RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedPriceRanges()
                            .get(priceID[i]).getRange_id(),1);
                }
            }
        });

        return view;
    }
    static class ViewHolder {

        TextView txtVwCategoryName;
        CheckBox mChkBoxSelectionState;

    }



}
