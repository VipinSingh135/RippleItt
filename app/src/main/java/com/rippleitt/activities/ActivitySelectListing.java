package com.rippleitt.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.rippleitt.adapters.SelectListingAdapter;
import com.rippleitt.callback.ItemClickListener;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.ListingResponsePayload;
import com.rippleitt.modals.ListingTemplate;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ActivitySelectListing extends AppCompatActivity implements View.OnClickListener, ItemClickListener {

    ImageView imgSearch,imgBack;
    ImageView imgCancel;
    TextView mtxtVwTitileFragments;
    EditText edSearch;
    ListView listManageProducts;
    Button btnAdd;
    CheckBox chkSelectAll;
    AVLoadingIndicatorView mLoader;
    SelectListingAdapter listingAdapter;
    List<ListingTemplate> userListings;
    List<ListingTemplate> selectedListings;
    List<ListingTemplate> searchedListings;
    String voucher_id = "";
    String added_listing_ids = "";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_listing);

        imgSearch = (ImageView) findViewById(R.id.imgVwSearchIcon);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        edSearch = (EditText) findViewById(R.id.edtxtQuickSearch);
        chkSelectAll = findViewById(R.id.chkSelectAll);
        mLoader = (AVLoadingIndicatorView) findViewById(R.id.aviLoaderHomeCenter);
        mLoader.setVisibility(View.GONE);
        imgCancel = (ImageView) findViewById(R.id.imgVwClearSearch);
        listManageProducts = (ListView) findViewById(R.id.listManageProducts);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        selectedListings = new ArrayList<>();
        userListings = new ArrayList<>();
        searchedListings = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            voucher_id = bundle.getString("voucher_id");
            if (bundle.getString("added_listing")!=null){
                added_listing_ids= bundle.getString("added_listing");
            }else {
                added_listing_ids=null;
            }
        }
//        listManageProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchedListings.clear();
                if (s.length() < 0) {
                    searchedListings.addAll(userListings);
                } else {
                    for (ListingTemplate listingTemplate : userListings) {
                        if (listingTemplate.getProductname().contains(s)) {
                            searchedListings.add(listingTemplate);
                        }
                    }
                }
                listingAdapter.notifyAdapter(searchedListings, selectedListings);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        chkSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedListings.addAll(searchedListings);
                } else {
                    selectedListings.clear();
                }
                listingAdapter.notifyAdapter(searchedListings, selectedListings);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(this, "Fetching Listings", Toast.LENGTH_LONG).show();
        selectedListings.clear();
        userListings.clear();
        searchedListings.clear();
        listManageProducts.setAdapter(null);
        listingAdapter = new SelectListingAdapter(ActivitySelectListing.this, userListings, selectedListings, mLoader);
        fetchUserListingApi();
    }

    @Override
    public void onClick(View view) {
        if (view == btnAdd) {
            if (selectedListings == null || selectedListings.size() < 0) {
                Toast.makeText(getBaseContext(), "Select atleast one listing", Toast.LENGTH_SHORT).show();
            } else {
               showConfirmation();
            }
        }else if (view==imgBack){
            this.finish();
        }
    }

//    private void volleyFetchProductDetails(){
//
//        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance
//                .getInstance().LISTING_DETAILS ,
//                new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("", response.toString());
//                        Gson gson = new Gson();
//                        MyListingDetailsPayload payload =
//                                (MyListingDetailsPayload)gson.fromJson(response,MyListingDetailsPayload.class);
//                        if(payload.getResponse_code().equalsIgnoreCase("1")){
//                            RippleittAppInstance
//                                    .getInstance()
//                                    .setMY_CURRENT_LISTING(payload.getData());
//                            startActivity(new Intent(getActivity(),ActivityMyProductDetails.class));
//                        }
//
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(),"could not fetch sub category, please try again",Toast.LENGTH_LONG).show();
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
//                params.put("listing_id",RippleittAppInstance
//                        .getInstance().getCURRENT_SELECTED_LISTING_ID());
//
//                return params;
//            }
//        };
//        sr.setShouldCache(false);
//        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
//    }

    @Override
    public void onPause() {
        super.onPause();
        RippleittAppInstance.getInstance().getRequestQueue().cancelAll("rippleitt");
    }

    public void fetchUserListingApi() {

        mLoader.setVisibility(View.VISIBLE);

        final RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.FEATCH_USER_LISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mLoader.hide();

                try {
                    ListingResponsePayload parsedResponse = (ListingResponsePayload)
                            new Gson()
                                    .fromJson(response, ListingResponsePayload.class);
                    if (parsedResponse != null && parsedResponse.getResponse_code().equalsIgnoreCase("1")) {
//                        RippleittAppInstance.getInstance().setCURRENT_USER_LISTING(parsedResponse.getData());

                        if (parsedResponse.getData().length != 0) {
//                            Collections.addAll(userListings, parsedResponse.getData());
                            for (ListingTemplate template: parsedResponse.getData()) {
                                if (template.getHas_voucher().equals("1")){
                                    if (added_listing_ids!=null && added_listing_ids.contains(template.getListing_id())){
                                        userListings.add(template);
                                        selectedListings.add(template);
                                    }
//                                    else if (add)
                                }
                                else if (template.getQuantity()==null || template.getQuantity().length()<1) {
//                                    userListings.add(template);
                                }else if (Integer.parseInt(template.getQuantity()) > 0) {
                                    userListings.add(template);
                                }
                            }
                            searchedListings.clear();
                            searchedListings.addAll(userListings);
                            listingAdapter = new SelectListingAdapter(ActivitySelectListing.this, userListings, selectedListings, mLoader);
                            listManageProducts.setAdapter(listingAdapter);
                        } else {
                            Toast.makeText(getBaseContext(), parsedResponse.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getBaseContext(), "Could not fetch your listings", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


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

                params.put("token", PreferenceHandler.readString(getBaseContext(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("page", "0");
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

    public void AddVoucherListingApi() {

        mLoader.setVisibility(View.VISIBLE);

        final RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.ADD_VOUCHER_LISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mLoader.hide();

                try {
                    Gson gson = new Gson();
                    CategoryListTemplate response_ = (CategoryListTemplate) gson.fromJson(response, CategoryListTemplate.class);
                    if (response_.getResponse_code() == 1) {
//                            if(RippleittAppInstance.getInstance().getListingDisputeMode()==1){

                        Toast.makeText(getApplicationContext(), response_.getResponse_message(), Toast.LENGTH_SHORT).show();
                        ActivitySelectListing.this.finish();

                    } else {
                        Toast.makeText(getApplicationContext(), response_.getResponse_message(), Toast.LENGTH_SHORT).show();
                        // show response_message in alert...
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


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

                params.put("token", PreferenceHandler.readString(getBaseContext(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("voucher_id", voucher_id);
                String listingIds = "";
                for (int i = 0; i < selectedListings.size(); ++i) {
                    if (i==0){
                        listingIds += selectedListings.get(i).getListing_id();
                    }else {
                        listingIds += ","+selectedListings.get(i).getListing_id();
                    }
                }
                params.put("asigned_listing_id", listingIds);
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

    public void EditVoucherListingApi() {

        mLoader.setVisibility(View.VISIBLE);

        final RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.EDIT_VOUCHER_LISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mLoader.hide();

                try {
                    Gson gson = new Gson();
                    CategoryListTemplate response_ = (CategoryListTemplate) gson.fromJson(response, CategoryListTemplate.class);
                    if (response_.getResponse_code() == 1) {
//                            if(RippleittAppInstance.getInstance().getListingDisputeMode()==1){

                        Toast.makeText(getApplicationContext(), response_.getResponse_message(), Toast.LENGTH_SHORT).show();
                        ActivitySelectListing.this.finish();

                    } else {
                        Toast.makeText(getApplicationContext(), response_.getResponse_message(), Toast.LENGTH_SHORT).show();
                        // show response_message in alert...
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


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

                params.put("token", PreferenceHandler.readString(getBaseContext(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("voucher_id", voucher_id);
                String listingIds = "";
                for (int i = 0; i < selectedListings.size(); ++i) {
                    if (i==0){
                        listingIds += selectedListings.get(i).getListing_id();
                    }else {
                        listingIds += ","+selectedListings.get(i).getListing_id();
                    }
                }
                params.put("asigned_listing_id", listingIds);
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

    @Override
    public void onItemClick(int position) {
        if (selectedListings.contains(searchedListings.get(position))) {
            selectedListings.remove(searchedListings.get(position));
        } else {
            selectedListings.add(searchedListings.get(position));
        }
        listingAdapter.notifyAdapter(searchedListings, selectedListings);
    }

    private void showConfirmation(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        builder.setMessage("Add voucher to selected listings?");

        builder.setTitle(null)
                .setCancelable(false)
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        new BuyNowApi().buyNowApi(SetYourPriceActivity.this,mEdtxtAddQty.getText().toString().trim(),voucher_id);
                        if (added_listing_ids!=null && added_listing_ids.length()>0){
                            EditVoucherListingApi();
                        }else {
                            AddVoucherListingApi();
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //addAttachments();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();
    }
}
