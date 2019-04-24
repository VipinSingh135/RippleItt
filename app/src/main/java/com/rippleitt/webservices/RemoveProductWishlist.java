package com.rippleitt.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rippleitt.R;
import com.rippleitt.callback.WishlistSelectCallback;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin on 10/24/2017.
 */
public class RemoveProductWishlist
{
    public void remoeProductWishlistApi(final Context context, final String screen_status, final String product_id,
                                        final WishlistSelectCallback callback)
    {
       /* final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");*/
       // dialog.setCancelable(false);
//        dialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL +RippleittAppInstance.REMOVE_WISHLIST_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e("total",total);
                Log.e("Response", response);
                try {
                  //  dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                  String  status = object.getString("response_code");
                    String msg = object.getString("response_message");

                    Log.e("Status", status + "");
                    if (status.equalsIgnoreCase("1")) {

                        if (screen_status.equalsIgnoreCase("1")){
                            new FetchWishlistApi().fetchWishlistApi(context,"0",callback);
                        }
                        else {
                                ImageView mimgVwAddToWishlist=(ImageView)((Activity)context).findViewById(R.id.imgVwAddToWishlist);
                        mimgVwAddToWishlist.setBackgroundResource(R.drawable.wishlist_icon);
                        }

                        TastyToast.makeText(context,"Listing removed from Wishlist",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    }
                    else {
                        TastyToast.makeText(context,msg,TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
                params.put("token", PreferenceHandler.readString(context,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id", product_id);
                Log.e("RemoveWishlist_Params",params+"");
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
