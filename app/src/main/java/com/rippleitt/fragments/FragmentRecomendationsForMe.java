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
import com.rippleitt.activities.ActivityListingDetailsRecommended;
import com.rippleitt.adapters.MyReferralsAdapter;
import com.rippleitt.adapters.RecomendationsAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.MyReferralsResponsePacket;
import com.rippleitt.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentRecomendationsForMe extends Fragment {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    private AVLoadingIndicatorView mLoaderRecomendations;
    private TextView mTxtVwNoRecomendations;
    private ListView mLstViwMyRecomendations;
    private RecomendationsAdapter recomendationsAdapter;
    private ListingTemplate[] recomendedListings;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_recomendations_for_me, container, false);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Recommendations");
        initUI(view);
        return view;
    }

    private void initUI(View view){
        mLoaderRecomendations=(AVLoadingIndicatorView)view.findViewById(R.id.loaderRecomendationsForMe);
        mLstViwMyRecomendations=(ListView)view.findViewById(R.id.listVwRecomendationsForMe);
        mTxtVwNoRecomendations=(TextView)view.findViewById(R.id.txtvwNoRecomendations);
        mTxtVwNoRecomendations.setVisibility(View.GONE);
    }


    @Override
    public void onResume(){
        super.onResume();
        volleyFetchMyRecomendations();
    }


    private void volleyFetchMyRecomendations(){

        mLoaderRecomendations.setVisibility(View.VISIBLE);
        mLstViwMyRecomendations.setAdapter(null);
        mLstViwMyRecomendations.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_MY_RECOMENDATIONS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mLoaderRecomendations.setVisibility(View.GONE);
                        try{
                            MyReferralsResponsePacket responsePacket =(MyReferralsResponsePacket) new Gson().fromJson(response,
                                    MyReferralsResponsePacket.class);
                            if(responsePacket.getResponse_code().equalsIgnoreCase("1")){
                                populateList(responsePacket.getData());
                            }else{
                                mTxtVwNoRecomendations.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){
                            mTxtVwNoRecomendations.setVisibility(View.VISIBLE);
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoaderRecomendations.setVisibility(View.GONE);
                mTxtVwNoRecomendations.setVisibility(View.VISIBLE);
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



    private void populateList(ListingTemplate[] listings){
        if(listings.length==0){
            mTxtVwNoRecomendations.setVisibility(View.VISIBLE);
        }else{
            mTxtVwNoRecomendations.setVisibility(View.GONE);
            recomendedListings=listings;
            mLstViwMyRecomendations.setVisibility(View.VISIBLE);
            recomendationsAdapter = new RecomendationsAdapter(getActivity(),listings);
            mLstViwMyRecomendations.setAdapter(recomendationsAdapter);

            mLstViwMyRecomendations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(recomendedListings[i]);
                    startActivity(new Intent(getActivity(), ActivityListingDetailsRecommended.class));
                }
            });
        }


    }

}
