package com.rippleitt.webservices;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
import com.rippleitt.adapters.ManageProductListing;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingResponsePayload;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin on 10/24/2017.
 */
public class FetchUserListingApi
{
    public void fetchUserListingApi(final Context context, final String page, final AVLoadingIndicatorView loader )
    {

        loader.setVisibility(View.VISIBLE);
        final ArrayList<HashMap<String,String>> arry_Details=new ArrayList<>();

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL +RippleittAppInstance.FEATCH_USER_LISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loader.hide();

                try{
                    ListingResponsePayload parsedResponse = (ListingResponsePayload)
                            new Gson()
                                    .fromJson(response,ListingResponsePayload.class);
                    if(parsedResponse!=null && parsedResponse.getResponse_code().equalsIgnoreCase("1")){
                        RippleittAppInstance.getInstance().setCURRENT_USER_LISTING(parsedResponse.getData());

                        ListView mlistManageProducts=(ListView)((Activity)context).findViewById(R.id.listManageProducts);
                        if(parsedResponse.getData().length==0){
                            LinearLayout mLinLytCenter = ((Activity)context).findViewById(R.id.linlytCenter);
                            mLinLytCenter.setVisibility(View.VISIBLE);
                            Button mBtnAddListing = ((Activity)context).findViewById(R.id.btnAddPRoductPublish);
                            mBtnAddListing.setVisibility(View.GONE);
//                           Toast.makeText(context,"You have not submitted any listings yet",Toast.LENGTH_SHORT).show();
                        }else{
                            LinearLayout mLinLytCenter = ((Activity)context).findViewById(R.id.linlytCenter);
                            mLinLytCenter.setVisibility(View.GONE);
                            Button mBtnAddListing = ((Activity)context).findViewById(R.id.btnAddPRoductPublish);
                            mBtnAddListing.setVisibility(View.VISIBLE);
                        }


                        ManageProductListing manageProductListing=new ManageProductListing(context,parsedResponse.getData(),loader);
                        mlistManageProducts.setAdapter(manageProductListing);
                    }else{
                        Toast.makeText(context,"Could not fetch your listings",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
               // Log.e("Response", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.hide();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
               // params.put("token", RippleittAppInstance.TOKEN_ID);
                params.put("token", PreferenceHandler.readString(context,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("page", page);
                Log.e("UserList_Params",params+"");
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
