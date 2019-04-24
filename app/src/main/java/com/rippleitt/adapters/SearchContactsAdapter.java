package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.webservices.AddContactApi;
import com.rippleitt.webservices.SearchContactApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 06-03-2018.
 */

public class SearchContactsAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> arr_contacts;

    public SearchContactsAdapter(Context context, ArrayList<HashMap<String,String>> arr_contacts) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder= new SearchContactsAdapter.ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.address_book_listing, viewGroup, false);
            holder.imgVwAddrListUserPic=(CircleImageView)view.findViewById(R.id.imgVwAddrListUserPic);
            holder.txtVwAddrBookList=(TextView)view.findViewById(R.id.txtVwAddrBookList);


            Picasso.with(context)
                    .load(RippleittAppInstance.formatPicPath(
                            arr_contacts
                                    .get(i).get("image")))
                    .placeholder(R.drawable.default_profile_icon)
                    .error(R.drawable.default_profile_icon)
                    .into(holder.imgVwAddrListUserPic);
            //=================set_userName============
            holder.txtVwAddrBookList.setText(arr_contacts.get(i).get("name"));

            //==============click()==========
            holder.txtVwAddrBookList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    //Uncomment the below code to Set the message and title from the strings.xml file
                    //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                    //Setting message manually and performing action on button click
                    builder.setMessage("Add this contact to your Address Book?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                  //  finish();
                                    new AddContactApi().addContactApi(context,arr_contacts.get(i).get("user_id"));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alert.show();


                }
            });

            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (SearchContactsAdapter.ViewHolder) view.getTag();
        }

return view;
    }
    static class ViewHolder {
        CircleImageView imgVwAddrListUserPic;
        TextView txtVwAddrBookList;

    }
}
