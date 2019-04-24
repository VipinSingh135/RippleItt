package com.rippleitt.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ActivityListingDispute;
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.adapters.MyDisputesAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.DisbursalObject;
import com.rippleitt.modals.MyDisputesData;
import com.rippleitt.modals.MyDisputesTemplate;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.modals.ReferralUsers;
import com.rippleitt.modals.UserDisbursalResponseTemplate;
import com.rippleitt.modals.UserWalletResponseTemplate;
import com.rippleitt.modals.WalletObject;
import com.rippleitt.utils.CommonUtils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentMyDisbursals extends Fragment {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    TextView tvTotalAmount;
    LinearLayout disbursalDataContainer;
    TextView txtvwNoDisbursal;

    AVLoadingIndicatorView mLoaderDisbursal;
    int totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //  View view = inflater.inflate(R.layout.layout_fragment_Disbursal, container, false);

        View view = inflater.inflate(R.layout.layout_fragment_my_dispersals, container, false);

        work();
        mimgvwtoggleMap = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments = (TextView) ((ActivityHome) getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Disbursal");

        initUI(view);

        return view;
    }

    public void work() {

    }

    private void initUI(View view) {
        txtvwNoDisbursal = (TextView) view.findViewById(R.id.txtvwNoDisbursal);
        txtvwNoDisbursal.setVisibility(View.GONE);
        tvTotalAmount = (TextView) view.findViewById(R.id.tvTotalAmount);
        disbursalDataContainer = (LinearLayout) view.findViewById(R.id.disbursalDataContainer);
        mLoaderDisbursal = (AVLoadingIndicatorView) view.findViewById(R.id.aviLoaderDisbursal);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchVolleyDisbursalData();
    }

    private void fetchVolleyDisbursalData() {
        mLoaderDisbursal.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_MY_DISBURSALS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mLoaderDisbursal.setVisibility(View.GONE);
                        try {
                            UserDisbursalResponseTemplate Disbursal = new UserDisbursalResponseTemplate();
                            Disbursal = (UserDisbursalResponseTemplate) new Gson().fromJson(response, UserDisbursalResponseTemplate.class);
                            if (Disbursal.getResponseCode() == 1) {
//                                totalAmount= Integer.parseInt(Disbursal.getDisbursal_balance());
                                if (Disbursal.getData().length == 0) {
                                    tvTotalAmount.setVisibility(View.VISIBLE);
                                    txtvwNoDisbursal.setVisibility(View.VISIBLE);
                                }

                                populateDisbursalChain(Disbursal.getData());
                            } else {
                                Toast.makeText(getActivity(),
                                        "could not update your Disbursal, please try again",
                                        Toast.LENGTH_LONG).show();

                            }

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "could not update your Disbursal, please try again", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "could not fetch your address book, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String token = PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, "");
                params.put("token", token);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void populateDisbursalChain(DisbursalObject[] data) {
        disbursalDataContainer.removeAllViews();
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
        int total=0;
        for (DisbursalObject disbursalObject : data) {
            View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.layour_disbursal_row, null);
            TextView tvDisbursalDate = (TextView) titleView.findViewById(R.id.tvDisbursalDate);
            TextView tvDisbursalAmount = (TextView) titleView.findViewById(R.id.tvDisbursalAmount);
            TextView tvDisbursalStatus = (TextView) titleView.findViewById(R.id.tvStatus);
            tvDisbursalDate.setText(disbursalObject.getSubmittedOn());
            tvDisbursalAmount.setText("$"+disbursalObject.getAmount());

            if(disbursalObject.getStatus().equalsIgnoreCase("0")){
                tvDisbursalStatus.setTextColor(Color.parseColor("#e17100"));
                tvDisbursalStatus.setText("Pending");
            }else if(disbursalObject.getStatus().equalsIgnoreCase("1")){
                tvDisbursalStatus.setTextColor(Color.parseColor("#e17100"));
                tvDisbursalStatus.setText("In Processing");
            }else if(disbursalObject.getStatus().equalsIgnoreCase("2")){
                tvDisbursalStatus.setTextColor(Color.parseColor("#65b488"));
                tvDisbursalStatus.setText("Rejected");
            }else{
                tvDisbursalStatus.setTextColor(Color.parseColor("#65b488"));
                tvDisbursalStatus.setText("Completed");
                total+= Integer.parseInt(disbursalObject.getAmount());
            }
            disbursalDataContainer.addView(titleView);

        }

        tvTotalAmount.setText("$"+total);
    }

//    private void volleyFetchItemDetails(final String listingID){
//        final ProgressDialog pDialog = new ProgressDialog(getActivity());
//        pDialog.setCancelable(true);
//        pDialog.setMessage("Getting Listing details...");
//        pDialog.show();
//        StringRequest sr = new StringRequest(Request.Method.POST,
//                RippleittAppInstance.BASE_URL
//                        +RippleittAppInstance.FETCHING_PRODUCT_DETAILS ,
//                new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        try{
//                            Gson g = new Gson();
//                            ProductDetailsResponseTemplate productDetails=
//                                    (ProductDetailsResponseTemplate)g.fromJson(response,ProductDetailsResponseTemplate.class);
//                            RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(productDetails.getData());
//
//                            startActivity(new Intent(getActivity(),ProductDetailsActivity.class));
//                            RippleittAppInstance.getInstance()
//                                    .setCURRENT_SELECTED_LISTING_ID( RippleittAppInstance
//                                            .getInstance()
//                                            .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());
//                        }catch (Exception e){
//                            Toast.makeText(getActivity(),
//                                    "could not fetch listing details",
//                                    Toast.LENGTH_LONG).show();
//                        }
//
//
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(),
//                        "could not fetch categories, please try again",
//                        Toast.LENGTH_LONG).show();
//                VolleyLog.d("", "Error: " + error.getMessage());
//                Log.d("", ""+error.getMessage()+","+error.toString());
//                CommonUtils.dismissProgress();
//            }
//        }){
//            @Override
//            protected Map<String,String> getParams(){
//
//                Map<String, String> params = new HashMap<>();
//                params.put("token", PreferenceHandler.readString(getActivity(),
//                        PreferenceHandler.AUTH_TOKEN, ""));
//                params.put("listing_id",listingID);
//                return params;
//            }
//        };
//        sr.setShouldCache(false);
//        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
//    }


}
