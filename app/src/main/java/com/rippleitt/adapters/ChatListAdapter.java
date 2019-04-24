package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.modals.ChatMessageTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by pc on mail/30/2018.
 */

public class ChatListAdapter  extends BaseAdapter {

    Context context;
    ArrayList<ChatMessageTemplate> chats = new ArrayList<>();

    public ChatListAdapter(Context context, ArrayList<ChatMessageTemplate> chatLog
                            ) {
        this.chats=chatLog;
        this.context=context;

    }

    @Override
    public int getCount() {
        return chats.size();
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
            holder= new ViewHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.layout_chat_cell, viewGroup, false);
            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mLinLytGreyBox=(LinearLayout) view.findViewById(R.id.linlytSelfView);
        holder.mLinLytGreenBox=(LinearLayout) view.findViewById(R.id.linlytOtherView);
        holder.mtxtVwSelfMessage=(TextView) view.findViewById(R.id.txtvwSelfMessage);
        holder.mtxtVwOtherMessage=(TextView) view.findViewById(R.id.txtvwOtherMessage);
        holder.mtxtVwSelfTimeStamp=(TextView) view.findViewById(R.id.txtvwSelfTimestamp);
        holder.mTxtVwOtherTimeStamp=(TextView) view.findViewById(R.id.txtvwOtherTimestamp);

        if(chats.get(i).getUserID()
                        .equalsIgnoreCase(PreferenceHandler
                                .readString(context,PreferenceHandler.USER_ID,""))){
        // this is a self message...
            holder.mLinLytGreenBox.setVisibility(View.GONE);
            holder.mLinLytGreyBox.setVisibility(View.VISIBLE);
            holder.mtxtVwSelfMessage.setText(chats.get(i).getText());
            TimeZone tz = TimeZone.getDefault();
            Calendar cal = Calendar.getInstance(tz);
            int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
            long currentTime= (long) chats.get(i).getTimestamp();
            currentTime -= offsetInMillis;
            Date date = new Date(currentTime);
           // holder.mtxtVwSelfTimeStamp.setText(
            //DateFormat.getDateTimeInstance().format(date));
            holder.mtxtVwSelfTimeStamp.setText(convertDate(chats.get(i).getTimestamp().toString()));

        }else{
            holder.mLinLytGreenBox.setVisibility(View.VISIBLE);
            holder.mLinLytGreyBox.setVisibility(View.GONE);
            holder.mtxtVwOtherMessage.setText(chats.get(i).getText());
            TimeZone tz = TimeZone.getDefault();
            Calendar cal = Calendar.getInstance(tz);
            int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
            long currentTime= (long)chats.get(i).getTimestamp();
            currentTime -= offsetInMillis;
            Date date = new Date(currentTime);
            holder.mTxtVwOtherTimeStamp.setText(convertDate(chats.get(i).getTimestamp().toString()));
            //holder.mTxtVwOtherTimeStamp.setText(
              //      DateFormat.getDateTimeInstance().format(date));
        }

        return view;
    }

    static class ViewHolder {
       LinearLayout mLinLytGreyBox, mLinLytGreenBox;
       TextView mtxtVwSelfMessage, mtxtVwOtherMessage,
                mtxtVwSelfTimeStamp, mTxtVwOtherTimeStamp;
    }


    private static String convertDate(String dateInMilliseconds) {
        //DateFormat.getDateInstance().format("Mdd/MM/yyyy hh:mm:ss a",dateInMilliseconds);
        //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy hh:mm:ss a");
        Date d = new Date(Long.parseLong(dateInMilliseconds));
        //Date resultdate = new Date(dateInMilliseconds);
        return sdf.format(d);
    }
}
