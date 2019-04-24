package com.rippleitt.firebase;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rippleitt.activities.ActivityMyProductDetails;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.MyListingDetailsPayload;
import com.rippleitt.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on mail/10/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        if(refreshedToken!=null) {
            // save the token in shared prefs
            PreferenceHandler.writeString(getApplicationContext(),PreferenceHandler.FCM_TOKEN,refreshedToken);
            if(!PreferenceHandler.readString(getApplicationContext(),
                                            PreferenceHandler.AUTH_TOKEN,"")
                                            .equalsIgnoreCase(""))
            sendRegistrationToServer(refreshedToken);
        }
    }

    private void sendRegistrationToServer(final String token) {
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance
                .UPDATE_FIREBASE_TOKEN ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("", response.toString());


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(getActivity(),"could not fetch sub category, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                //CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(getApplicationContext(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("fcm_token",token);

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }
}
