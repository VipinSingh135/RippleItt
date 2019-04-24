package com.rippleitt.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ActivityListingDetailsMyReferral;
import com.rippleitt.adapters.MyReferralsAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.MyBidsResponseTemplate;
import com.rippleitt.modals.MyReferralsResponsePacket;
import com.rippleitt.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentMyReferrals extends Fragment {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    private AVLoadingIndicatorView loaderMyReferrals;
    private TextView mTxtVNoReferrals;
    private ListView mlstVwMyReferrals;
    private MyReferralsAdapter referralsAdapter;
    private ListingTemplate[] referredListing;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_my_referrals, container, false);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("My Referrals");
        initUI(view);
        return view;
    }


    @Override
    public void onResume(){
        super.onResume();
        volleyFetchMyReferrals();
    }


    private void initUI(View view){
        loaderMyReferrals=(AVLoadingIndicatorView)view.findViewById(R.id.loaderMyReferrals);
        mlstVwMyReferrals=(ListView)view.findViewById(R.id.listVwMyReferrals);
        mTxtVNoReferrals=(TextView)view.findViewById(R.id.txtvwNoReferrals);
        mTxtVNoReferrals.setVisibility(View.GONE);
    }

    private void populateList(ListingTemplate[] listings){

        if(listings.length==0){
            mTxtVNoReferrals.setVisibility(View.VISIBLE);
        }else{
            mTxtVNoReferrals.setVisibility(View.GONE);
            referredListing=listings;
            referralsAdapter = new MyReferralsAdapter(getActivity(),listings);
            mlstVwMyReferrals.setAdapter(referralsAdapter);

            mlstVwMyReferrals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(referredListing[i]);
                    startActivity(new Intent(getActivity(), ActivityListingDetailsMyReferral.class));
                }
            });
        }

    }

    private void volleyFetchMyReferrals(){

        loaderMyReferrals.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_REFERAALS
                        ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loaderMyReferrals.setVisibility(View.GONE);
                        try{
                            MyReferralsResponsePacket responsePacket =(MyReferralsResponsePacket) new Gson().fromJson(response,
                                    MyReferralsResponsePacket.class);
                            if(responsePacket.getResponse_code().equalsIgnoreCase("1")){
                                populateList(responsePacket.getData());
                            }else{
                                mTxtVNoReferrals.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loaderMyReferrals.setVisibility(View.GONE);
                Toast.makeText(getActivity(),
                        "could not fetch your referral history",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(getActivity(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

}
