package com.rippleitt.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.DashboardTemplate;
import com.rippleitt.modals.NotificationsResponseTemplate;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentBuyerDashboard extends Fragment {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    private TextView mTxtWvWalletBalance,mTxtVwOrderCount, mTxtVwOrderThisWeek,
                     mTxtVwCountReferrals, mTxtVwRewardEarned,
                     mTxtvwListingViewed, mTxtVwWishlistCount, mTxtVwOrderMadeCount, mTXtVwWeeklyOrderReceived,
                     mTxtVwPayment, mTxtVwBuyers;
    AVLoadingIndicatorView mLoader;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_buyer_dashboard, container, false);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Dashboard");
        initUI(view);
        return view;
    }





    @Override
    public void onResume(){
        super.onResume();
        volleyFetchDashboard();
    }



    private void initUI(View view){
        mTxtWvWalletBalance=(TextView)view.findViewById(R.id.txtvwWalletBalance);
        mTxtVwOrderCount=(TextView)view.findViewById(R.id.txtvwOrderCount);
        mTxtVwOrderThisWeek=(TextView)view.findViewById(R.id.txtvwOrderWeekCount);
        mTxtVwCountReferrals=(TextView)view.findViewById(R.id.txtvwReferalCount);
        mTxtVwRewardEarned=(TextView)view.findViewById(R.id.txtvwReferralEarned);
        mTxtvwListingViewed=(TextView)view.findViewById(R.id.txtvwListingViewCount);
        mTxtVwWishlistCount=(TextView)view.findViewById(R.id.txtvwWishlistCount);
        mTxtVwOrderMadeCount=(TextView)view.findViewById(R.id.txtvwOrderReceivedCount);
        mTXtVwWeeklyOrderReceived=(TextView)view.findViewById(R.id.txtvwOrderReceivedWeekly);
        mTxtVwPayment=(TextView)view.findViewById(R.id.txtvwPaymentReceivedCount);
        mTxtVwBuyers=(TextView)view.findViewById(R.id.txtvwBuyerCount);
        mLoader=(AVLoadingIndicatorView)view.findViewById(R.id.aviLoaderDashboard);

    }


    private void volleyFetchDashboard(){

        mLoader.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_USER_DASHBOARD,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       mLoader.setVisibility(View.GONE);
                        try{
                            DashboardTemplate notifications = new DashboardTemplate();
                            notifications=(DashboardTemplate) new Gson().fromJson(response,DashboardTemplate.class);
                            if(notifications.getResponse_code().equalsIgnoreCase("1")){
                                mTxtWvWalletBalance.setText("$" + String.format("%.2f",Float.parseFloat(notifications.getWalletbalance())));
                                mTxtVwOrderCount.setText(notifications.getTotalordermade());
                                mTxtVwOrderThisWeek.setText(notifications.getTotalorderreceived7days());
                                mTxtVwCountReferrals.setText(notifications.getTotalreferralmade());
                                mTxtVwRewardEarned.setText("$" + String.format("%.2f",Float.parseFloat(notifications.getRewardsearned())));
                                mTxtvwListingViewed.setText(notifications.getTotalviewedproducts());
                                mTxtVwWishlistCount.setText(notifications.getTotalwishlistadded());
                                mTxtVwOrderMadeCount.setText(notifications.getTotalorderreceived());
                                mTXtVwWeeklyOrderReceived.setText(notifications.getTotalorderreceived7days());
                                mTxtVwPayment.setText("$" + String.format("%.2f",Float.parseFloat(notifications.getTotalpaymentreceived())));
                                mTxtVwBuyers.setText(notifications.getTotalbuyers());
                            }else{
//                                {"response_code":1,"response_message":"results fetched successfully","totalordermade":"password","totalreferralmade":"user","totalreferralmade7days":"user","rewardsearned":"50","totalviewedproducts":"18","totalwishlistadded":"0","totalorderreceived":"password","totalorderreceived7days":"password","totalpaymentreceived":"244","totalbuyers":"business","walletbalance":"50"}
                            }

                        }catch (Exception e){
                            Toast.makeText(getActivity(),"could not update your wallet, please try again",Toast.LENGTH_LONG).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "could not fetch your address book, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                String token = PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN,"");
                params.put("token",token);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");


    }


}
