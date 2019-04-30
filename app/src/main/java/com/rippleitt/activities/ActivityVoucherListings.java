package com.rippleitt.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.ManageVouchersListing;
import com.rippleitt.adapters.MyVoucherListing;
import com.rippleitt.callback.ItemClickListener;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.MyVoucherResponsePayload;
import com.rippleitt.modals.VoucherTemplate;
import com.rippleitt.webservices.FetchMyVouchersApi;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityVoucherListings extends AppCompatActivity implements View.OnClickListener, ItemClickListener {

    ImageView imgvwAddListing,imgBack;
    ListView mlistVouchers;
    //    Button mBtnAddNewVouchers;
    AVLoadingIndicatorView mLoader;
    Button mBtnAddNewVouchersCenter,btnDone;
    LinearLayout mLinLytCenterControl;
    int selected_pos=-1,voucher_type=-1;
    boolean isProduct=false;
    String voucher_id, listing_id;
    List<VoucherTemplate> list;
    MyVoucherListing manageVouchers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_listing);

        mLoader = (AVLoadingIndicatorView)findViewById(R.id.aviLoaderHomeCenter);
        mBtnAddNewVouchersCenter=(Button)findViewById(R.id.btnAddVouchersCenter);
        btnDone=(Button)findViewById(R.id.btnDone);
        mLinLytCenterControl=(LinearLayout)findViewById(R.id.linlytCenter);
        mLinLytCenterControl.setVisibility(View.GONE);
        mBtnAddNewVouchersCenter.setOnClickListener(this);
        mLoader.setVisibility(View.GONE);
        mlistVouchers=(ListView)findViewById(R.id.listVouchers);
        imgvwAddListing = findViewById(R.id.imgvwAddListing);
        imgBack = findViewById(R.id.imgBack);
        imgvwAddListing.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        listing_id=  RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID();

        if (RippleittAppInstance.getInstance().getCURRENT_ADDED_LISTING_OBJECT() != null) {

            if (RippleittAppInstance.getInstance().getCURRENT_ADDED_LISTING_OBJECT().getListing_type().equalsIgnoreCase("2")){
                isProduct=false;
            }else {
                isProduct=true;
            }


        } else if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT() != null) {
            if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getListing_type().equalsIgnoreCase("2")){
                isProduct=false;
            }else {
                isProduct=true;
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Toast.makeText(this,"Fetching Vouchers",Toast.LENGTH_LONG).show();
        list = new ArrayList<>();
        mlistVouchers.setAdapter(null);
        fetchMyVouchersApi(this,"0", mLoader);
        mLinLytCenterControl.setVisibility(View.GONE);
//        mBtnAddNewVouchers.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        if(view==mBtnAddNewVouchersCenter || view== imgvwAddListing){
            startActivityForResult(new Intent(ActivityVoucherListings.this, ActivityAddVoucher.class),11);
        }else if(view==imgBack){
            finish();
        }else if(view== btnDone){
            if (selected_pos<0) {

            }
            else if (voucher_type==3 && isProduct) {
                Toast.makeText(getBaseContext(), "This voucher is only applicable to service", Toast.LENGTH_SHORT).show();
            }
            else {
                if (list.get(selected_pos).getAsignedListingId().length()>0){
                    EditVoucherListingApi();
                }else{
                    AddVoucherListingApi();
                }
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        RippleittAppInstance.getInstance().getRequestQueue().cancelAll("rippleitt");
    }

    public void fetchMyVouchersApi(final Context context, final String page, final AVLoadingIndicatorView loader) {

        loader.setVisibility(View.VISIBLE);
        final ArrayList<HashMap<String, String>> arry_Details = new ArrayList<>();

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.FETCH_VOUCHER_LISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.hide();
                try {
                    MyVoucherResponsePayload parsedResponse = (MyVoucherResponsePayload)
                            new Gson()
                                    .fromJson(response, MyVoucherResponsePayload.class);
                    if (parsedResponse != null && parsedResponse.getResponse_code().equalsIgnoreCase("1")) {

                        ListView mlistManageVouchers = (ListView) ((Activity) context).findViewById(R.id.listVouchers);


                        list.addAll(Arrays.asList(parsedResponse.getData()));

                        if (list.size() == 0) {
                            LinearLayout mLinLytCenter = ((Activity) context).findViewById(R.id.linlytCenter);
                            mLinLytCenter.setVisibility(View.VISIBLE);
                            Button mBtnAddListing = ((Activity) context).findViewById(R.id.btnAddVouchersCenter);
                            mBtnAddListing.setVisibility(View.GONE);
                            btnDone.setVisibility(View.GONE);
                            // Toast.makeText(context,"You have not submitted any listings yet",Toast.LENGTH_SHORT).show();
                        } else {
                            LinearLayout mLinLytCenter = ((Activity) context).findViewById(R.id.linlytCenter);
                            mLinLytCenter.setVisibility(View.GONE);
                            Button mBtnAddListing = ((Activity) context).findViewById(R.id.btnAddVouchersCenter);
                            mBtnAddListing.setVisibility(View.VISIBLE);
                            btnDone.setVisibility(View.VISIBLE);
                        }

                        for (VoucherTemplate obj: list) {
                            if (obj.getAsignedListingId().contains(listing_id) || obj.getVoucherId().equals(voucher_id))
                                selected_pos= list.indexOf(obj);
                        }
                        manageVouchers = new MyVoucherListing(ActivityVoucherListings.this, list, loader,selected_pos);
                        mlistManageVouchers.setAdapter(manageVouchers);
                    } else {
                        Toast.makeText(context, "Could not fetch your vouchers", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("token", PreferenceHandler.readString(context,
                        PreferenceHandler.AUTH_TOKEN, ""));

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

//                        Toast.makeText(getApplicationContext(), response_.getResponse_message(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("voucher_name", list.get(selected_pos).getName());
                        intent.putExtra("voucher_type", list.get(selected_pos).getType());
                        intent.putExtra("voucher_amount", list.get(selected_pos).getAmount());
                        setResult(12, intent);
                        finish();
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
                params.put("asigned_listing_id", listing_id);
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
                        Intent intent = new Intent();
                        intent.putExtra("voucher_name", list.get(selected_pos).getName());
                        intent.putExtra("voucher_type", list.get(selected_pos).getType());
                        intent.putExtra("voucher_amount", list.get(selected_pos).getAmount());
                        setResult(12, intent);
                        finish();

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
                params.put("asigned_listing_id", list.get(selected_pos).getAsignedListingId()+","+listing_id);
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
    public void onItemClick(int pos) {
        selected_pos= pos;
        manageVouchers.notifyAdapter(pos);
        voucher_id= list.get(pos).getVoucherId();
        voucher_type= Integer.parseInt(list.get(pos).getType());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== RESULT_OK){
            if (requestCode==11){
                voucher_id= data.getStringExtra("voucher_id");
                voucher_type= data.getIntExtra("amount_type",-1);
//                Toast.makeText(this,voucher_id,Toast.LENGTH_LONG).show();
            }
        }
    }
}
