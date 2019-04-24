package com.rippleitt.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityAddListingStep1;
import com.rippleitt.activities.ActivityAddListingStep2;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ActivityMyProductDetails;
import com.rippleitt.activities.AddNewProduct;
import com.rippleitt.adapters.ManageProductListing;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.MyListingDetailsPayload;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.FetchUserListingApi;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentManageListing extends Fragment implements View.OnClickListener {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    ListView mlistManageProducts;
    Button mBtnAddNewListing;
    AVLoadingIndicatorView mLoader;
    Button mBtnAddNewListingCenter;
    LinearLayout mLinLytCenterControl;

    ManageProductListing manageProductListing;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_manage_listing, container, false);
        mimgvwtoggleMap = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwtoggleMap);
        mLoader = (AVLoadingIndicatorView) view.findViewById(R.id.aviLoaderHomeCenter);
        mBtnAddNewListingCenter = (Button) view.findViewById(R.id.btnAddPRoductPublishCenter);
        mLinLytCenterControl = (LinearLayout) view.findViewById(R.id.linlytCenter);
        mLinLytCenterControl.setVisibility(View.GONE);
        mBtnAddNewListingCenter.setOnClickListener(this);
        mLoader.setVisibility(View.GONE);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments = (TextView) ((ActivityHome) getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("My Listings");
        mlistManageProducts = (ListView) view.findViewById(R.id.listManageProducts);
        mBtnAddNewListing = (Button) view.findViewById(R.id.btnAddPRoductPublish);
        mBtnAddNewListing.setOnClickListener(this);
        mlistManageProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (RippleittAppInstance
                        .getInstance().getCURRENT_USER_LISTING()[i].getListing_flag_name()
                        .equalsIgnoreCase("drafted")) {

                    RippleittAppInstance.getInstance()
                            .setCURRENT_LISTING_OBJECT(RippleittAppInstance
                                    .getInstance().getCURRENT_USER_LISTING()[i]);
                    if (RippleittAppInstance
                            .getInstance().getCURRENT_USER_LISTING()[i].getQuantity() != null && RippleittAppInstance
                            .getInstance().getCURRENT_USER_LISTING()[i].getQuantity().trim().length() > 0) {
                        startActivity(new Intent(getActivity(),
                                ActivityAddListingStep2.class));
                    } else {
                        startActivity(new Intent(getActivity(),
                                ActivityAddListingStep1.class));

                    }
                }else if (RippleittAppInstance
                        .getInstance().getCURRENT_USER_LISTING()[i].getQuantity() == null || RippleittAppInstance
                        .getInstance().getCURRENT_USER_LISTING()[i].getQuantity().length() < 1) {
                    RippleittAppInstance.getInstance()
                            .setCURRENT_LISTING_OBJECT(RippleittAppInstance
                                    .getInstance().getCURRENT_USER_LISTING()[i]);
                    startActivity(new Intent(getActivity(),
                            ActivityAddListingStep1.class));

                } else {

                    RippleittAppInstance
                            .getInstance()
                            .setCURRENT_SELECTED_LISTING_ID(RippleittAppInstance
                                    .getInstance().getCURRENT_USER_LISTING()[i].getListing_id());
                    Toast.makeText(getActivity(), "Getting listing details...", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getActivity(),ActivityMyProductDetails.class));
                    volleyFetchProductDetails();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "Fetching Listings", Toast.LENGTH_LONG).show();
        mlistManageProducts.setAdapter(null);
        new FetchUserListingApi().fetchUserListingApi(getContext(), "0", mLoader);
        mLinLytCenterControl.setVisibility(View.GONE);
        mBtnAddNewListing.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        if (view == mBtnAddNewListing || view == mBtnAddNewListingCenter) {
            RippleittAppInstance.getInstance().setCURRENT_LISTING_OBJECT(null);
            startActivity(new Intent(getActivity(), ActivityAddListingStep1.class));
        }
    }

    private void volleyFetchProductDetails() {

        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance
                .getInstance().LISTING_DETAILS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        MyListingDetailsPayload payload =
                                (MyListingDetailsPayload) gson.fromJson(response, MyListingDetailsPayload.class);
                        if (payload.getResponse_code().equalsIgnoreCase("1")) {
                            RippleittAppInstance
                                    .getInstance()
                                    .setMY_CURRENT_LISTING(payload.getData());
//                            RippleittAppInstance
//                                    .getInstance()
//                                    .setSELECTED_LISTING_DETAIL_OBJECT(payload.getData());
                            startActivity(new Intent(getActivity(), ActivityMyProductDetails.class));
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "could not fetch sub category, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(getActivity(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id", RippleittAppInstance
                        .getInstance().getCURRENT_SELECTED_LISTING_ID());

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    @Override
    public void onPause() {
        super.onPause();
        RippleittAppInstance.getInstance().getRequestQueue().cancelAll("rippleitt");
    }

}
