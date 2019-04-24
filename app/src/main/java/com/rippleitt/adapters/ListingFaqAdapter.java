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
import com.rippleitt.activities.ActivityPostListingFaq;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingFaqTemplate;

/**
 * Created by manishautomatic on 12/06/18.
 */

public class ListingFaqAdapter extends BaseAdapter {

    private ListingFaqTemplate[] data;
    private Context context;
    private LayoutInflater mInflater;

    public ListingFaqAdapter(Context parentReference, ListingFaqTemplate[] faqArray){
        this.data=faqArray;
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
        ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            view= mInflater.inflate(R.layout.row_listing_faq,null);
            holder.mLinLytQuestionBox=(LinearLayout)view.findViewById(R.id.linlytQuestionBox);
            holder.mLinLytAnswerBox=(LinearLayout)view.findViewById(R.id.linlytAnswerBox);
            holder.mTxtVwQuestion=(TextView)view.findViewById(R.id.txtvwQuestion);
            holder.mTxtVwAnswer=(TextView)view.findViewById(R.id.txtvwAnswer);
            holder.mTxtVwQuestionTime=(TextView)view.findViewById(R.id.txtvwQuestionDate);
            holder.getmTxtVwAnswerTime=(TextView)view.findViewById(R.id.txtvwAnswerDate);
            view.setTag(holder);
        } else{
          holder=(ViewHolder)view.getTag();
        }

        holder.mTxtVwQuestion.setText(""+data[i].getQuestion_body());
        holder.mTxtVwQuestionTime.setText(data[i].getQuestioner()+" "+data[i].getPosted_on());
        if(data[i].getAnswer_body().equalsIgnoreCase("")){
            holder.mLinLytAnswerBox.setBackgroundResource(R.drawable.question_box_bg);
            if(RippleittAppInstance.getInstance().getListingFaqMode()==0){
                holder.mTxtVwAnswer.setText("Seller hasn't responded to this question yet.");
                holder.mLinLytAnswerBox.setVisibility(View.GONE);
            }else{
                holder.mTxtVwAnswer.setText("Tap here to comment");
                holder.mLinLytAnswerBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RippleittAppInstance.getInstance().setCurrentQuestionId(data[i].getQuestion_id());
                        context.startActivity(new Intent(context, ActivityPostListingFaq.class));
                    }
                });
            }

            holder.mTxtVwAnswer.setTextColor(Color.BLUE);
            holder.getmTxtVwAnswerTime.setText("");

        }else{
            holder.mLinLytAnswerBox.setBackgroundResource(R.drawable.answet_box_bg);
            holder.mTxtVwAnswer.setText(""+data[i].getAnswer_body());
            holder.getmTxtVwAnswerTime.setText(data[i].getResponder()+" "+data[i].getAnswer_date());
        }



        return view;
    }

    private class ViewHolder{
        private TextView mTxtVwQuestion,mTxtVwAnswer,
                         mTxtVwQuestionTime,getmTxtVwAnswerTime;
        private LinearLayout  mLinLytQuestionBox, mLinLytAnswerBox;
    }
}
