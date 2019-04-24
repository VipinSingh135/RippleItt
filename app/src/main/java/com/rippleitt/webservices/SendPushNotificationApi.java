package com.rippleitt.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.PojoFCMPush;
import com.rippleitt.modals.PojoFCMPushData;
import com.rippleitt.modals.PojoFCMPushDataResp;
import com.rippleitt.modals.PojoFCMPushNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SendPushNotificationApi {

    public void SendPushNotificationApi(final Context context, final String message, final String device_token) {
//        final ProgressDialog dialog = new ProgressDialog(context);
//        dialog.setMessage("Logging you in...");
//         dialog.setCancelable(false);
//        dialog.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.FCM_URL + RippleittAppInstance.SEND_PUSH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e("total",total);
                Log.e("Response", response);
                try {
//                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    int status = object.getInt("success");

                    Log.e("Status", status + "");
                    if (status==1) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
                params.put("Authorization", RippleittAppInstance.FCM_SERVER_KEY);
                params.put("Content-Type", "application/json");

                Log.e("FCM_Params", params + "");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                PojoFCMPushData pojoData= new PojoFCMPushData();
                PojoFCMPushDataResp pojoDataResp= new PojoFCMPushDataResp();
                PojoFCMPushNotification pojoNotification = new PojoFCMPushNotification();

                pojoDataResp.setDevice("Android");
                if(FirebaseInstanceId.getInstance().getToken()!=null){
                    pojoDataResp.setDevice_token( FirebaseInstanceId.getInstance().getToken());

                }else {
                    pojoDataResp.setDevice_token("0");
                }
                pojoDataResp.setIcon("");
                pojoDataResp.setIs_deleted(0);
                pojoDataResp.setMessage(message);
                SharedPreferences sharedPreferences = context
                        .getSharedPreferences("preferences", Context.MODE_PRIVATE);
                String name= sharedPreferences.getString("user_name", "");


                String id= PreferenceHandler.readString(context,
                        PreferenceHandler.AUTH_TOKEN, "");


                if (sharedPreferences.getString("image", "").equalsIgnoreCase("")
                        ||
                        sharedPreferences.getString("image", "").equalsIgnoreCase("null")
                        ) {
                }else{
                    pojoDataResp.setUser_image(sharedPreferences.getString("image", ""));

                }

                pojoDataResp.setUsername(name);
                pojoDataResp.setStatus(1);
                pojoDataResp.setType("chat");
                pojoDataResp.setUser_id(id);

                pojoData.setBody(message);
                pojoData.setResp(pojoDataResp);
                pojoData.setTitle(name);

                pojoNotification.setBody(message);
                pojoNotification.setTitle(name);

                PojoFCMPush pojo= new PojoFCMPush();
                pojo.setTo(device_token);
                pojo.setData(pojoData);
                pojo.setPriority("high");
                pojo.setSound("default");
                pojo.setContent_available(true);
                pojo.setNotification(pojoNotification);


//                String data= pojoData.toJSON();
                String push = new Gson().toJson(pojo);


                return push.getBytes();
            }
        };
        myRqst.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(myRqst);

    }
}
