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
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.adapters.MyBidsAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.AddressBookShareTemplate;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.MyBidsResponseTemplate;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on password/27/2018.
 */

public class FragmentMyBids extends Fragment {

    private ListView mLstVwMyBids;
    private AVLoadingIndicatorView mLoader;
    private MyBidsAdapter mAdapter;
    private TextView mTxtVwNoBids;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.layout_fragment_mybids,
                            container, false );
    initUI(view);
    return view;
    }




    private void initUI(View view){

        // hide the header controls from the parent activity header...
        mTxtVwNoBids=(TextView)view.findViewById(R.id.txtvwNoBids);
        mTxtVwNoBids.setVisibility(View.GONE);
        ImageView  mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        ImageView  mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        TextView mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("My Offers");
        mLstVwMyBids=(ListView)view.findViewById(R.id.listVwMyBids);
        mLoader=(AVLoadingIndicatorView)view.findViewById(R.id.aviLoaderMyBids);
    }


    @Override
    public void onResume(){
        super.onResume();
        volleyFetchUserBids();

    }



    private void volleyFetchUserBids(){
        mLoader.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL
                        +RippleittAppInstance.FETCH_MY_BIDS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyBidsResponseTemplate myBidsPacket = (MyBidsResponseTemplate)
                                new Gson().fromJson(response,MyBidsResponseTemplate.class);
                        if(myBidsPacket.getResponse_code().equalsIgnoreCase("1")) {
                            if (mLoader != null)
                                mLoader.setVisibility(View.GONE);
                                populateList(myBidsPacket.getData());
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "could not fetch your address book, please try again",Toast.LENGTH_LONG).show();
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


    private void populateList(final ListingTemplate[] userBids){

        if(userBids.length==0)mTxtVwNoBids.setVisibility(View.VISIBLE);
        mAdapter=new MyBidsAdapter(getActivity(),userBids);
        mLstVwMyBids.setAdapter(mAdapter);
        mLstVwMyBids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                volleyFetchItemDetails(userBids[i].getListing_id());
            }
        });
    }


    private void volleyFetchItemDetails(final String listingID){
        mLoader.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL
                        +RippleittAppInstance.FETCHING_PRODUCT_DETAILS ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mLoader.setVisibility(View.GONE);

                        Gson g = new Gson();
                        ProductDetailsResponseTemplate productDetails=
                                (ProductDetailsResponseTemplate)g.fromJson(response,ProductDetailsResponseTemplate.class);
                        RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(productDetails.getData());
                        startActivity(new Intent(getActivity(),ProductDetailsActivity.class));
                        RippleittAppInstance.getInstance()
                                .setCURRENT_SELECTED_LISTING_ID( RippleittAppInstance
                                        .getInstance()
                                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "could not fetch categories, please try again",
                        Toast.LENGTH_LONG).show();
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
                params.put("listing_id",listingID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }


    @Override
    public void onPause(){
        super.onPause();
        RippleittAppInstance.getInstance().getRequestQueue().cancelAll("rippleitt");
    }

}
