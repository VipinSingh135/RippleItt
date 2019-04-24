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
import com.rippleitt.activities.ActivityOrderDetails;
import com.rippleitt.adapters.MyOrdersAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.MyReferralsResponsePacket;
import com.rippleitt.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentMyOrders extends Fragment {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    private ListView mLstViwMyOrdersw;
    private TextView mTxtVwNoOrders;
    private AVLoadingIndicatorView mLoaderMyOrders;
    private ListingTemplate[] userOrders;
    private MyOrdersAdapter myOrdersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_my_orders, container, false);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("My Orders");
        initUI(view);
        return view;
    }

    private void initUI(View view){
        mLoaderMyOrders=(AVLoadingIndicatorView)view.findViewById(R.id.loaderMyOrders);
        mLstViwMyOrdersw=(ListView)view.findViewById(R.id.listVwMyOrders);
        mTxtVwNoOrders=(TextView)view.findViewById(R.id.txtvwNoOrders);
        mTxtVwNoOrders.setVisibility(View.GONE);
    }

    @Override
    public void onResume(){
        super.onResume();
        volleyFetchOrders();
        RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(null);
        RippleittAppInstance.getInstance().setCURRENT_SELECTED_LISTING_ID(null);

    }

    private void volleyFetchOrders(){

        mLoaderMyOrders.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_USER_ORDERS
                ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mLoaderMyOrders.setVisibility(View.GONE);
                        try{
                            MyReferralsResponsePacket responsePacket =(MyReferralsResponsePacket) new Gson().fromJson(response,
                                    MyReferralsResponsePacket.class);
                            if(responsePacket.getResponse_code().equalsIgnoreCase("1")){
                                populateList(responsePacket.getData());
                            }else{
                                mTxtVwNoOrders.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){

                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoaderMyOrders.setVisibility(View.GONE);
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

    private void populateList(ListingTemplate[] ordersList){
        if(ordersList.length==0)mTxtVwNoOrders.setVisibility(View.VISIBLE);
        userOrders=ordersList;

        myOrdersAdapter = new MyOrdersAdapter(getActivity(),userOrders);
        mLstViwMyOrdersw.setAdapter(myOrdersAdapter);
        mLstViwMyOrdersw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(userOrders[i]);
                    startActivity(new Intent(getActivity(), ActivityOrderDetails.class));
            }
        });
    }

}
