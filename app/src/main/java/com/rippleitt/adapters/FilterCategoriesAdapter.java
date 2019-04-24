package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.callback.FilterParamSelected;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ContactSharingTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 06-03-2018.
 */

public class FilterCategoriesAdapter extends BaseAdapter {

    Context context;
    private int dataMode=0;  // 1-- categories , 2-- subcategories
    private Object[] categoryID;
    FilterParamSelected selection;


    public FilterCategoriesAdapter(Context context, int _mode, FilterParamSelected callback) {
        this.context=context;
        this.dataMode=_mode;
        categoryID = RippleittAppInstance
                .getInstance()
                .getCurrentLoadedCategories().keySet().toArray();
        selection=callback;
    }

    @Override
    public int getCount()
    {
        return categoryID.length;
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
        ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new FilterCategoriesAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.filters_cell_adapter, viewGroup, false);
            holder.txtVwCategoryName=(TextView)view.findViewById(R.id.txtvwCategoryTitle);
            holder.mChkBoxSelectionState=(CheckBox) view.findViewById(R.id.chkbxSelected);
            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (FilterCategoriesAdapter.ViewHolder) view.getTag();
        }

        holder.txtVwCategoryName.setText(RippleittAppInstance
                                        .getInstance()
                                        .getCurrentLoadedCategories()
                                        .get(categoryID[i]).getName());
        if(RippleittAppInstance
                .getInstance()
                .getCurrentLoadedCategories()
                .get(categoryID[i]).getIsTicked()==1){
            holder.mChkBoxSelectionState.setChecked(true);
        }else{
            holder.mChkBoxSelectionState.setChecked(false);
        }

        holder.mChkBoxSelectionState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(RippleittAppInstance
                        .getInstance()
                        .getCurrentLoadedCategories()
                        .get(categoryID[i]).getIsTicked()==1){
                    ((CheckBox)view).setChecked(false);
                    RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedCategories()
                            .get(categoryID[i]).setIsTicked(0);
                    selection.categorySelected(RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedCategories()
                            .get(categoryID[i]).getId(),0);
                }else{
                    ((CheckBox)view).setChecked(true);
                    RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedCategories()
                            .get(categoryID[i]).setIsTicked(1);
                    selection.categorySelected(RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedCategories()
                            .get(categoryID[i]).getId(),1);
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
