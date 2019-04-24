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
import com.rippleitt.activities.ActivityListingDispute;
import com.rippleitt.activities.ActivityOrderDetails;
import com.rippleitt.adapters.MyDisputesAdapter;
import com.rippleitt.adapters.MyOrdersAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.MyDisputesData;
import com.rippleitt.modals.MyDisputesTemplate;
import com.rippleitt.modals.MyReferralsResponsePacket;
import com.rippleitt.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

public class FragmentMyDisputes extends Fragment {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    private ListView mLstViwMyDisputesw;
    private TextView mTxtVwNoDisputes;
    private AVLoadingIndicatorView mLoaderMyDisputes;
    private MyDisputesData[] userDisputes;
    private MyDisputesAdapter myDisputesAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_my_disputes, container, false);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("My Disputes");
        initUI(view);
        return view;
    }
    private void initUI(View view){
        mLoaderMyDisputes=(AVLoadingIndicatorView)view.findViewById(R.id.loaderMyDisputes);
        mLstViwMyDisputesw=(ListView)view.findViewById(R.id.listVwMyDisputes);
        mTxtVwNoDisputes=(TextView)view.findViewById(R.id.txtvwNoDisputes);
        mTxtVwNoDisputes.setVisibility(View.GONE);
    }

    @Override
    public void onResume(){
        super.onResume();
        volleyFetchDisputes();
    }

    private void volleyFetchDisputes(){

        mLoaderMyDisputes.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_USER_DISPUTES
                ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mLoaderMyDisputes.setVisibility(View.GONE);
                        mTxtVwNoDisputes.setVisibility(View.INVISIBLE);
                        try{

                            MyDisputesTemplate responsePacket =(MyDisputesTemplate) new Gson().fromJson(response,
                                    MyDisputesTemplate.class);
                            if(responsePacket.getResponse_code().equalsIgnoreCase("1")){
                                populateList(responsePacket.getData());
                            }else{
                                mTxtVwNoDisputes.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){

                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoaderMyDisputes.setVisibility(View.GONE);
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

    private void populateList(MyDisputesData[] ordersList){
        if(ordersList.length==0)mTxtVwNoDisputes.setVisibility(View.VISIBLE);
        userDisputes=ordersList;

        myDisputesAdapter = new MyDisputesAdapter(getActivity(),userDisputes);
        mLstViwMyDisputesw.setAdapter(myDisputesAdapter);
        mLstViwMyDisputesw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(userDisputes[i].getOrderDetails());
                RippleittAppInstance.getInstance().setDisputeID(userDisputes[i].getDisputeId());
                RippleittAppInstance.getInstance().setDisputeTitle(userDisputes[i].getTitle());
                RippleittAppInstance.getInstance().setDisputeStatus(userDisputes[i].getStatus());
                RippleittAppInstance.getInstance().setDisputeMode(true);
                startActivity(new Intent(getActivity(), ActivityListingDispute.class));
            }
        });

    }

}
