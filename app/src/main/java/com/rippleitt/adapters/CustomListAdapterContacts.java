package com.rippleitt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.rippleitt.R;
import com.rippleitt.callback.ItemSelectionCallback;
import com.rippleitt.modals.ContactTemplate;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by manishautomatic on 14/12/15.
 */
public class CustomListAdapterContacts extends BaseAdapter {

    private Context parentReference;
    private ArrayList<ContactTemplate> lContactList = new ArrayList<ContactTemplate>();
    private LayoutInflater mLayoutInflator;
    private ItemSelectionCallback lCallback;
    private ArrayList<ContactTemplate> masterArrayList = new ArrayList<>();

    public CustomListAdapterContacts(Context context, ArrayList<ContactTemplate> appointments,
                                     ItemSelectionCallback callback){
        this.parentReference= context;
        this.lContactList.clear();
        this.lContactList.addAll(appointments);
        this.mLayoutInflator = LayoutInflater.from(context);
        this.lCallback= callback;
        this.masterArrayList.clear();
        this.masterArrayList.addAll(appointments);
    }



    @Override
    public int getCount() {
        return lContactList.size();
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
        if(view == null){
        holder = new ViewHolder();
            view= mLayoutInflator.inflate(R.layout.layout_contact_list_row,null);
            holder.txtVwPatientName= (TextView)view.findViewById(R.id.txtvwContactName);
            holder.chkbxSelect=(CheckBox)view.findViewById(R.id.chkbxSelect);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
            holder.txtVwPatientName.setText(lContactList.get(i).getContactName());

        holder.chkbxSelect.setChecked(lContactList.get(i).isselected());
        setupClicListners(holder, i);
        return view;
    }

    private void setupClicListners(ViewHolder holder, final int position) {

        holder.chkbxSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((CheckBox)view).isChecked()){
                    lCallback.onSelected(position);
                }else{
                    lCallback.onUnSelected(position);
                }

            }
        });
    }


    private class ViewHolder{
        TextView txtVwPatientName;

         CheckBox chkbxSelect;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lContactList.clear();
        if (charText.length() == 0) {
            lContactList.addAll(masterArrayList);
        } else {
            for (ContactTemplate wp : masterArrayList) {
                if (wp.getContactName().toLowerCase(Locale.getDefault())
                        .contains(charText)
                    || wp.getContactPhoneNumber().toLowerCase(Locale.getDefault())
                        .contains(charText)   ) {
                    lContactList.add(wp);
                    lCallback.setReferenceListContact(lContactList);
                }
            }
        }
        notifyDataSetChanged();
    }

}
