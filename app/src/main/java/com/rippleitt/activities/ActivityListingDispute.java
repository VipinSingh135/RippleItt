package com.rippleitt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.rippleitt.adapters.ListingDisputeAdapter;
import com.rippleitt.adapters.ListingFaqAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingDisputeResponseTemplate;
import com.rippleitt.modals.ListingDisputeTemplate;
import com.rippleitt.modals.ListingFaqResponseTemplate;
import com.rippleitt.modals.ListingFaqTemplate;

import java.util.HashMap;
import java.util.Map;

public class ActivityListingDispute extends AppCompatActivity implements View.OnClickListener {

    private TextView mTxtVwAskQuestion,tvDisputeTitle,tvTitle;
    private ListView mLstVwDisputeList;
    private ProgressDialog mPDialog;
    private TextView mTxtVwNoFaq;
    private ImageView mImgVwBack;
    String disputeId=null;
    private Button btnAddComment;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_disputes);
        initUI();

    }

    @Override
    public void onClick(View view) {
        if(view==mTxtVwAskQuestion){
            startActivity(new Intent(ActivityListingDispute.this, ActivityOrderDetails.class));

        }else if(view==btnAddComment){
            startActivity(new Intent(ActivityListingDispute.this, ActivityAddDispute.class));

        }else if(view==mImgVwBack){
            finish();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mLstVwDisputeList.setAdapter(null);
        if (!disputeId.equals(""))
        volleyFetchListingDispute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RippleittAppInstance.getInstance().setDisputeMode(false);
    }

    private void initUI(){

        mImgVwBack=(ImageView) findViewById(R.id.imgBackFAQ);
        mImgVwBack.setOnClickListener(this);
        mTxtVwAskQuestion=(TextView)findViewById(R.id.txtvwAskQuestion);
        tvTitle=(TextView)findViewById(R.id.tvTitle);
        tvDisputeTitle=(TextView)findViewById(R.id.tvDisputeTitle);
        mTxtVwAskQuestion.setOnClickListener(this);
        mLstVwDisputeList=(ListView)findViewById(R.id.lstvwFaQList);
        btnAddComment=(Button) findViewById(R.id.btnAddComment);
        btnAddComment.setOnClickListener(this);
        mPDialog=new ProgressDialog(this);
        mPDialog.setCancelable(false);
        mTxtVwNoFaq=(TextView)findViewById(R.id.txtvwNoFaQ);
        mTxtVwNoFaq.setVisibility(View.GONE);
        //mLstVwFaqList.setOnClickListener(this);

        tvTitle.setText("Help");
        mTxtVwAskQuestion.setText("Order Info");
        mTxtVwNoFaq.setText("No comments posted yet.");
        if(!RippleittAppInstance.getInstance().getDisputeID().equals("")){
            disputeId= RippleittAppInstance.getInstance().getDisputeID();
        }else{
            disputeId= RippleittAppInstance.getInstance().getDisputeID();
        }
        if(RippleittAppInstance.getInstance().getDisputeStatus().equals("business")){
            btnAddComment.setVisibility(View.GONE);
        }else{
            btnAddComment.setVisibility(View.VISIBLE);
        }
//        if(!RippleittAppInstance.getInstance().getDisputeTitle().equals("")){
            tvDisputeTitle.setText(RippleittAppInstance.getInstance().getDisputeTitle());
//        }else{
//            tvDisputeTitle.setText(RippleittAppInstance.getInstance().getDisputeTitle());
//        }
    }

    private void volleyFetchListingDispute(){
        mPDialog.setCancelable(false);
        mPDialog.setMessage("Getting comments");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_LISTING_DISPUTE,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();;
                        if(response.contains("\n"))response=response.replaceAll("\n","");
                        ListingDisputeResponseTemplate listingDispute = new ListingDisputeResponseTemplate();
                        listingDispute = (ListingDisputeResponseTemplate)new Gson().fromJson(response,ListingDisputeResponseTemplate.class);
                        if(listingDispute.getData()!=null && listingDispute.getData().length!=0){
                            populateList(listingDispute.getData());
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
                Toast.makeText(ActivityListingDispute.this,
                        "could not fetch listing Disputes, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("token", PreferenceHandler.readString(ActivityListingDispute.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("dispute_id",disputeId);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void populateList(ListingDisputeTemplate[] data){
        ListingDisputeAdapter adapter = new ListingDisputeAdapter(ActivityListingDispute.this, data);
        mLstVwDisputeList.setAdapter(adapter);
    }

}
