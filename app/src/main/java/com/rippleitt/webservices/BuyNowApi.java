package com.rippleitt.webservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rippleitt.activities.ActivityUpdatePaymentMethod;
import com.rippleitt.activities.AddPaymentMethod;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyNowApi {

    public void buyNowApi(final Context context, final String qty, final String voucher_id) {
        final ArrayList<HashMap<String, String>> arry_Details = new ArrayList<>();
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Buying your product");
        pDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.BUY_NOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pDialog.dismiss();
                try {

                    //  maviLoaderHome.hide();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("response_code");
                    String msg = object.getString("response_message");
                    String card_flag;
                    if (object.has("is_card_available")) {
                        card_flag = object.getString("is_card_available");
                    } else {
                        card_flag = "1";
                    }

                    Log.e("Status", status + "");
                    if (status.equalsIgnoreCase("1")) {

                        if(card_flag.equalsIgnoreCase("1")){

                        Intent returnIntent = new Intent();
                        ((Activity) context).setResult(Activity.RESULT_OK, returnIntent);
                        ((Activity) context).finish();
                        TastyToast.makeText(context, "Congratulations! Your Order has been placed" + "", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        }else{
                            //
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Please add a credit card to your account in order to buy for a listing.")
                                    .setCancelable(false)
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            RippleittAppInstance.getInstance().setAddCardMode(1);
                                            context.startActivity(new Intent(context, ActivityUpdatePaymentMethod.class));
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }


                    } else {
                        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
                params.put("token", PreferenceHandler.readString(context,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id", RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());
                params.put("quantity", qty);
                params.put("voucher_id", voucher_id);
                Log.e("MakeOffer_Params", params + "");

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
