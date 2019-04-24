package com.rippleitt.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.adapters.ManageProductListing;
import com.rippleitt.adapters.UnlockedVouchersAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingResponsePayload;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.MyListingDetailsPayload;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.FetchUnlockedVouchers;
import com.rippleitt.webservices.FetchUserListingApi;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentUnlockedVouchers extends Fragment implements View.OnClickListener {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    ListView mlistManageProducts;
    AVLoadingIndicatorView mLoader;
    LinearLayout mLinLytCenterControl;
    ManageProductListing manageProductListing;
    ListingTemplate[] unlockedListings;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_unlocked_vouchers, container, false);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mLoader = (AVLoadingIndicatorView)view.findViewById(R.id.aviLoaderHomeCenter);
        mLinLytCenterControl=(LinearLayout)view.findViewById(R.id.linlytCenter);
        mLinLytCenterControl.setVisibility(View.GONE);
        mLoader.setVisibility(View.GONE);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Unlocked Vouchers");
        mlistManageProducts = (ListView) view.findViewById(R.id.listManageProducts);

        mlistManageProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String listing_id="";
                listing_id=unlockedListings[i].getListing_id();

                RippleittAppInstance.getInstance().setCURRENT_SELECTED_LISTING_ID(listing_id);
                volleyFetchItemDetails(listing_id);
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Toast.makeText(getActivity(),"Fetching Listings",Toast.LENGTH_LONG).show();
        mlistManageProducts.setAdapter(null);
        fetchUnlockedVouchersApi("0");
        mLinLytCenterControl.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onPause(){
        super.onPause();
        RippleittAppInstance.getInstance().getRequestQueue().cancelAll("rippleitt");
    }


    public void fetchUnlockedVouchersApi(final String page) {
        mLoader.setVisibility(View.VISIBLE);
        final ArrayList<HashMap<String, String>> arry_Details = new ArrayList<>();

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.FETCH_UNLOCKED_VOUCHERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mLoader.hide();

                try {
                    ListingResponsePayload parsedResponse = (ListingResponsePayload)
                            new Gson()
                                    .fromJson(response, ListingResponsePayload.class);
                    if (parsedResponse != null && parsedResponse.getResponse_code().equalsIgnoreCase("1")) {

                        if (parsedResponse.getData().length == 0) {
                            mLinLytCenterControl.setVisibility(View.VISIBLE);
//                            Button mBtnAddListing = ((Activity)context).findViewById(R.id.btnAddPRoductPublish);
//                            mBtnAddListing.setVisibility(View.GONE);
                            // Toast.makeText(context,"You have not submitted any listings yet",Toast.LENGTH_SHORT).show();
                        } else {
                            unlockedListings= parsedResponse.getData();
                            mLinLytCenterControl.setVisibility(View.GONE);
//                            Button mBtnAddListing = ((Activity)context).findViewById(R.id.btnAddPRoductPublish);
//                            mBtnAddListing.setVisibility(View.VISIBLE);
                        }


                        UnlockedVouchersAdapter unlockedVouchersAdapter = new UnlockedVouchersAdapter(getContext(), parsedResponse.getData(), mLoader);
                        mlistManageProducts.setAdapter(unlockedVouchersAdapter);
                    } else {
                        Toast.makeText(getContext(), "Could not fetch your listings", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Log.e("Response", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoader.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
                // params.put("token", RippleittAppInstance.TOKEN_ID);
                params.put("token", PreferenceHandler.readString(getContext(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("page", page);
                Log.e("UserList_Params", params + "");
                return params;
            }
        };
        myRqst.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(myRqst);
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
                        try{
                            Gson g = new Gson();
                            ProductDetailsResponseTemplate productDetails=
                                    (ProductDetailsResponseTemplate)g.fromJson(response,ProductDetailsResponseTemplate.class);
                            RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(productDetails.getData());
//                            shouldTriggerRecent=false;
                            startActivity(new Intent(getActivity(),ProductDetailsActivity.class));
                            RippleittAppInstance.getInstance()
                                    .setCURRENT_SELECTED_LISTING_ID( RippleittAppInstance
                                            .getInstance()
                                            .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());
                        } catch (Exception e){
                            Toast.makeText(getActivity(),
                                    "could not fetch listing details",
                                    Toast.LENGTH_LONG).show();
                        }


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

}

