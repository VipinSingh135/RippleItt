package com.rippleitt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.ListingFaqAdapter;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingFaqResponseTemplate;
import com.rippleitt.modals.ListingFaqTemplate;
import com.rippleitt.modals.PaymentTokenResponseTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 11/06/18.
 */

public class ActivityListingFaq extends AppCompatActivity implements View.OnClickListener {

    private TextView mTxtVwAskQuestion;
    private ListView mLstVwFaqList;
    private ProgressDialog mPDialog;
    private TextView mTxtVwNoFaq;
    private ImageView mImgVwBack;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_faq);
        initUI();

    }

    @Override
    public void onClick(View view) {
        if(view==mTxtVwAskQuestion){
            startActivity(new Intent(ActivityListingFaq.this, ActivityPostListingFaq.class));
        }if(view==mImgVwBack){
            finish();
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        mLstVwFaqList.setAdapter(null);
        volleyFetchListingFaq();
        if(RippleittAppInstance.getInstance().getListingFaqMode()==0){
            mTxtVwAskQuestion.setVisibility(View.VISIBLE);
        }else{
            mTxtVwAskQuestion.setVisibility(View.GONE);
        }
    }

    private void initUI(){

        mImgVwBack=(ImageView) findViewById(R.id.imgBackFAQ);
        mImgVwBack.setOnClickListener(this);
        mTxtVwAskQuestion=(TextView)findViewById(R.id.txtvwAskQuestion);
        mTxtVwAskQuestion.setOnClickListener(this);
        mLstVwFaqList=(ListView)findViewById(R.id.lstvwFaQList);
        mPDialog=new ProgressDialog(this);
        mPDialog.setCancelable(false);
        mTxtVwNoFaq=(TextView)findViewById(R.id.txtvwNoFaQ);
        mTxtVwNoFaq.setVisibility(View.GONE);
        //mLstVwFaqList.setOnClickListener(this);


        if(RippleittAppInstance.getInstance().getListingFaqMode()==1){
            mTxtVwAskQuestion.setVisibility(View.GONE);
        }else{
            mTxtVwAskQuestion.setVisibility(View.VISIBLE);
        }
    }

    private void volleyFetchListingFaq(){
        mPDialog.setCancelable(false);
        mPDialog.setMessage("Getting comments");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_LISTING_FAQ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();;
                        if(response.contains("\n"))response=response.replaceAll("\n","");
                        ListingFaqResponseTemplate listingFaq = new ListingFaqResponseTemplate();
                        listingFaq = (ListingFaqResponseTemplate)new Gson().fromJson(response,ListingFaqResponseTemplate.class);
                        if(listingFaq.getData()!=null && listingFaq.getData().length!=0){
                            populateList(listingFaq.getData());
                           // mTxtVwAskQuestion.setVisibility(View.GONE);
                            mTxtVwNoFaq.setVisibility(View.GONE);
                        }else{
                            mTxtVwNoFaq.setVisibility(View.VISIBLE);
                            //mTxtVwAskQuestion.setVisibility(View.VISIBLE);
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();;
                Toast.makeText(ActivityListingFaq.this,
                        "could not fetch listing FAQs, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("token",PreferenceHandler.readString(ActivityListingFaq.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id",RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void populateList(ListingFaqTemplate[] data){
        ListingFaqAdapter adapter = new ListingFaqAdapter(ActivityListingFaq.this, data);
        mLstVwFaqList.setAdapter(adapter);
    }

}
