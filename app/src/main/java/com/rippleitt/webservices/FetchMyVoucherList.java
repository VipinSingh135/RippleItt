package com.rippleitt.webservices;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.MyVoucherResponsePayload;
import com.rippleitt.modals.VoucherTemplate;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchMyVoucherList {

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
//                            RippleittAppInstance.getInstance().setCURRENT_USER_LISTING(parsedResponse.getData());

                        ListView mlistManageVouchers = (ListView) ((Activity) context).findViewById(R.id.listVouchers);

                        List<VoucherTemplate> list = new ArrayList<>();
                        list.addAll(Arrays.asList(parsedResponse.getData()));

                        if (list.size() == 0) {
                            LinearLayout mLinLytCenter = ((Activity) context).findViewById(R.id.linlytCenter);
                            mLinLytCenter.setVisibility(View.VISIBLE);
                            Button mBtnAddListing = ((Activity) context).findViewById(R.id.btnAddVouchersCenter);
                            mBtnAddListing.setVisibility(View.GONE);
                            // Toast.makeText(context,"You have not submitted any listings yet",Toast.LENGTH_SHORT).show();
                        } else {
                            LinearLayout mLinLytCenter = ((Activity) context).findViewById(R.id.linlytCenter);
                            mLinLytCenter.setVisibility(View.GONE);
                            Button mBtnAddListing = ((Activity) context).findViewById(R.id.btnAddVouchersCenter);
                            mBtnAddListing.setVisibility(View.VISIBLE);
                        }


                        ManageVouchersListing manageVouchers = new ManageVouchersListing(context, list, loader);
                        mlistManageVouchers.setAdapter(manageVouchers);
                    } else {
                        Toast.makeText(context, "Could not fetch your vouchers", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Log.e("Response", response);


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
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
                // params.put("token", RippleittAppInstance.TOKEN_ID);
                params.put("token", PreferenceHandler.readString(context,
                        PreferenceHandler.AUTH_TOKEN, ""));
//                    params.put("page", page);
//                    Log.e("UserList_Params", params + "");
                return params;
            }
        };
        myRqst.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(myRqst);
    }

}
