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
import com.rippleitt.activities.ActivityAddMobileNumber;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ActivityVerifyEmail;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin on 10/24/2017.
 */
public class LoginApi {
    public void loginApi(final Context context, final String email, final String password) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Logging you in...");
        // dialog.setCancelable(false);
        dialog.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e("total",total);
                Log.e("Response", response);
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("response_code");
                    String msg = object.getString("response_message");

                    Log.e("Status", status + "");
                    if (status.equalsIgnoreCase("1")) {

                        JSONObject userDetailsObj = object.getJSONObject("userinformation");

                        String userName = userDetailsObj.getString("fname") + " " + userDetailsObj.getString("lname");
                        String userTokenId = userDetailsObj.getString("token");
                        String email = userDetailsObj.getString("email");
                        String mobilenumber = userDetailsObj.getString("mobilenumber");
                        String longitude = userDetailsObj.getString("longitude");
                        String latitude = userDetailsObj.getString("latitude");
                        String address1 = userDetailsObj.getString("address1");
                        String postal_code = userDetailsObj.getString("post_code");
                        String address2 = userDetailsObj.getString("address2");
                        String gender = userDetailsObj.getString("gender");
                        String image = userDetailsObj.getString("image");
                        String referalcode = userDetailsObj.getString("referalcode");
                        String user_id = userDetailsObj.getString("user_id");
                        String user_type = userDetailsObj.getString("user_type");
                        String abn_number = userDetailsObj.getString("abn_number");
                        String account_number = userDetailsObj.getString("bank_account_number");
                        String business_name = userDetailsObj.getString("business_name");
                        PreferenceHandler.writeString(context, PreferenceHandler.USER_TYPE, user_type);
                        PreferenceHandler.writeString(context, PreferenceHandler.ABN_NUMBER, abn_number);
                        PreferenceHandler.writeString(context, PreferenceHandler.BUSINESS_NAME, business_name);
                        PreferenceHandler.writeString(context, PreferenceHandler.ACCOUNT_NUMBER, account_number);

                        String email_verificationFlag = userDetailsObj.getString("email_verified");
                        String mobile_verificationFlag = userDetailsObj.getString("phone_verified");

                        RippleittAppInstance.TOKEN_ID = userTokenId;
                        RippleittAppInstance.userObject = userDetailsObj;
                        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_id", user_id);
                        editor.commit();

                        PreferenceHandler.writeString(context, PreferenceHandler.INIT_MODE, PreferenceHandler.LOGIN);
                        RippleittAppInstance.getInstance().setEmailVerified(email_verificationFlag);
                        RippleittAppInstance.getInstance().setMobileVerified(mobile_verificationFlag);
                        if (email_verificationFlag.equalsIgnoreCase("0")) {
                            RippleittAppInstance.getInstance().setVerificationMode(1);
                            RippleittAppInstance.getInstance().setEmailToVerify(email);
                            context.startActivity(new Intent(context, ActivityVerifyEmail.class));
                        }
                        if (email_verificationFlag.equalsIgnoreCase("1") &&
                                (mobile_verificationFlag.equalsIgnoreCase("0"))) {
                            RippleittAppInstance.getInstance().setVerificationMode(2);
                            context.startActivity(new Intent(context, ActivityAddMobileNumber.class));
                        }
                        if (email_verificationFlag.equalsIgnoreCase("1") &&
                                (mobile_verificationFlag.equalsIgnoreCase("1"))) {

                            editor.putString("userTokenId", userTokenId);
                            editor.putString("user_name", userName);
                            editor.putString("email", email);
                            editor.putString("image", image);
                            editor.putString("address1", address1);
                            editor.putString("post_code", postal_code);
                            editor.putString("address2", address2);
                            editor.putString("mobilenumber", mobilenumber);
                            editor.putString("user_id", user_id);
                            editor.putString("fname", userDetailsObj.getString("fname"));
                            editor.putString("lname", userDetailsObj.getString("lname"));
                            editor.putString("abn_no", userDetailsObj.getString("abn_number"));
                            editor.putString("business_name", userDetailsObj.getString("business_name"));
                            editor.commit();
                            TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            context.startActivity(new Intent(context, ActivityHome.class));
                        }
                        ((Activity) context).finish();

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
                dialog.dismiss();
                TastyToast.makeText(context, error.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
                params.put("email", email);
                params.put("password", password);
                String fcmToken = PreferenceHandler.readString(context, PreferenceHandler.FCM_TOKEN, "");
                if(FirebaseInstanceId.getInstance().getToken()!=null){
                    fcmToken = FirebaseInstanceId.getInstance().getToken();
                    Log.d("Firebase token: ", "callLoginApi: " +fcmToken);
                }else {
                    fcmToken="0";
                }
                params.put("fcm_token", fcmToken);
                params.put("device_type", "Android");
                Log.e("LOGIN_Params", params + "");
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