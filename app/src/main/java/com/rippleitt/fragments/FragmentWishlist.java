package com.rippleitt.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.callback.WishlistSelectCallback;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.FetchWishlistApi;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentWishlist extends Fragment implements WishlistSelectCallback {

    ImageView mimgvwtoggleMap;
    TextView mtxtVwTitileFragments;
    ImageView mimgvwInitFilter;
    TextView mTxtViewNoEntries;
    private AVLoadingIndicatorView maviLoaderHomeCenter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_wishlist, container, false);
        mTxtViewNoEntries=(TextView)view.findViewById(R.id.txtvwNoEntires);
        mTxtViewNoEntries.setVisibility(View.GONE);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Wishlist");
        maviLoaderHomeCenter=(AVLoadingIndicatorView)view.findViewById(R.id.aviLoaderHomeCenter);

        return view;
    }


    @Override
    public void onResume(){
        super.onResume();;
        Toast.makeText(getActivity(),"Updating your wishlist",Toast.LENGTH_LONG).show();
        new FetchWishlistApi().fetchWishlistApi(getActivity(),"0",this);

    }

    @Override
    public void onWishListitemSelected(String listing_id) {
        // fetch the details of wishlist item....
        volleyFetchItemDetails(listing_id);
    }

    private void volleyFetchItemDetails(final String listingID){
        maviLoaderHomeCenter.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL
                        +RippleittAppInstance.FETCHING_PRODUCT_DETAILS ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        maviLoaderHomeCenter.setVisibility(View.GONE);
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
                        "could not fetch details, please try again",
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
