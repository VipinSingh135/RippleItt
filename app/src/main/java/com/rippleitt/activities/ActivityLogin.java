package com.rippleitt.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.callback.GetUserCallback;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.commonUtilities.NetworkUtil;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.controller.UserRequest;
import com.rippleitt.modals.FbAuthTemplate;
import com.rippleitt.modals.MyBidsResponseTemplate;
import com.rippleitt.modals.User;
import com.rippleitt.webservices.LoginApi;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity
        implements View.OnClickListener, GetUserCallback.IGetUserResponse {


    private CallbackManager callbackManager;
    private EditText medittxtEmailLogin;
    private EditText medittxtPassLogin;
    private TextView mtxtvmSignUpLogin;
    private Button mbtnLogin;
    private Button mbtnFacbookLogin;
    private TextView mTxtVwForgtPassword;
    private LoginButton mBtnFbSDKLogin;
    private static final String EMAIL = "email";
    private String fb_authToken = "";
    private String fb_user_id = "";
    private ProgressDialog mPDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPDialog = new ProgressDialog(ActivityLogin.this);
        init();
        callbackManager = CallbackManager.Factory.create();
        initFbBtnCallback();
    }

    public void init() {
        mTxtVwForgtPassword = (TextView) findViewById(R.id.txtVwForgotPass);
        medittxtEmailLogin = (EditText) findViewById(R.id.edittxtEmailLogin);
        medittxtPassLogin = (EditText) findViewById(R.id.edittxtPassLogin);
        mtxtvmSignUpLogin = (TextView) findViewById(R.id.txtvmSignUpLogin);
        mbtnLogin = (Button) findViewById(R.id.btnLogin);
        mbtnFacbookLogin = (Button) findViewById(R.id.btnFacbookLogin);
        mBtnFbSDKLogin = (LoginButton) findViewById(R.id.fb_login_button);
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
        mBtnFbSDKLogin.setLoginBehavior(LoginBehavior.WEB_ONLY);
        //mBtnFbSDKLogin.setLoginBehavior();
        mTxtVwForgtPassword.setOnClickListener(this);
        mbtnLogin.setOnClickListener(this);
        mtxtvmSignUpLogin.setOnClickListener(this);
        mbtnFacbookLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mTxtVwForgtPassword) {
            startActivity(new Intent(ActivityLogin.this, ActivityForgotPassword.class));
        }
        //=================login_button==============
        if (view == mbtnLogin) {

            if (!CommonUtils.isValidEmail(medittxtEmailLogin.getText().toString().trim())) {
                medittxtEmailLogin.setError("Please enter a valid email");
                return;
            }
            if (medittxtPassLogin.getText().toString().trim().equalsIgnoreCase("")) {
                medittxtPassLogin.setError("Please enter password");
                return;
            }

            if (NetworkUtil.getConnectivityStatus(this)) {

                new LoginApi().loginApi(this,
                        medittxtEmailLogin.getText().toString().trim(),
                        convertToMD5(medittxtPassLogin.getText().toString().trim()));
            } else {
                TastyToast.makeText(getApplicationContext(), "No Internet Connection", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

            }

        }
        //==================facebook login==============
        if (view == mbtnFacbookLogin) {
            //TastyToast.makeText(getApplicationContext(),"FaceBook Login",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
            mBtnFbSDKLogin.performClick();
        }
        //=======================Signup_Button=========

        if (view == mtxtvmSignUpLogin) {
            Intent intent = new Intent(ActivityLogin.this, ActivitySignupOptions.class);
            startActivity(intent);
        }
    }


    private void initFbBtnCallback() {


        mBtnFbSDKLogin.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        mBtnFbSDKLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult != null) {
                    fb_authToken = loginResult.getAccessToken().getToken();
                    fb_user_id = loginResult.getAccessToken().getUserId();
                } else {
                    Toast.makeText(ActivityLogin.this, "Facebook login error", Toast.LENGTH_LONG).show();
                }
                System.out.print(loginResult);
            }

            @Override
            public void onCancel() {
                System.out.print("fb-login-cancelled");
                Toast.makeText(ActivityLogin.this, "Facebook login cancelled", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {

                System.out.print(exception);
                Toast.makeText(ActivityLogin.this, "Error in Facebook Authentication", Toast.LENGTH_LONG).show();

            }
        });


        //callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.print(loginResult);
                        if (loginResult != null) {
                            fb_authToken = loginResult.getAccessToken().getToken();
                            fb_user_id = loginResult.getAccessToken().getUserId();
                            UserRequest.makeUserRequest(new GetUserCallback(ActivityLogin.this).getCallback());
                        } else {
                            Toast.makeText(ActivityLogin.this, "Facebook login error", Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(ActivityLogin.this, "Facebook login cancelled", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(ActivityLogin.this, "Error in Facebook Authentication", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private static String convertToMD5(String value) {
        try {

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(value.getBytes());
            byte messageDigest[] = digest.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCompleted(User user) {

        System.out.print(user);
        performVolleyFbAuthentication(user);
    }

    private void performVolleyFbAuthentication(final User user) {
        mPDialog.setCancelable(false);
        mPDialog.setMessage("Logging you in...");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.PERFORM_FB_LOGIN,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();
                        FbAuthTemplate authObj = (FbAuthTemplate) new Gson().fromJson(response, FbAuthTemplate.class);
                        if (authObj != null && authObj.getResponse_code().equalsIgnoreCase("1")) {
                            SharedPreferences sharedPreferences__ = ActivityLogin.this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor__ = sharedPreferences__.edit();
                            editor__.putString("user_id", authObj.getUserinformation().getUser_id());
                            editor__.commit();

                            PreferenceHandler.writeString(ActivityLogin.this, PreferenceHandler.INIT_MODE, PreferenceHandler.LOGIN);
                            RippleittAppInstance.getInstance().setEmailVerified(authObj.getUserinformation().getEmail_verified());
                            RippleittAppInstance.getInstance().setMobileVerified(authObj.getUserinformation().getPhone_verified());
                            if (authObj.getUserinformation().getEmail_verified().equalsIgnoreCase("0")) {
                                RippleittAppInstance.getInstance().setVerificationMode(1);
                                RippleittAppInstance.getInstance().setEmailToVerify(authObj.getUserinformation().getEmail());
                                ActivityLogin.this.startActivity(new Intent(ActivityLogin.this, ActivityVerifyEmail.class));
                            }
                            if (authObj.getUserinformation().getEmail_verified().equalsIgnoreCase("1") &&
                                    (authObj.getUserinformation().getPhone_verified().equalsIgnoreCase("0"))) {
                                RippleittAppInstance.getInstance().setVerificationMode(2);
                                ActivityLogin.this.startActivity(new Intent(ActivityLogin.this, ActivityAddMobileNumber.class));
                            }
                            if (authObj.getUserinformation().getEmail_verified().equalsIgnoreCase("1") &&
                                    (authObj.getUserinformation().getPhone_verified().equalsIgnoreCase("1"))) {

                                TastyToast.makeText(getApplicationContext(), "Welcome ! User", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                //Toast.makeText(ActivityLogin.this, "Welcome ! User",Toast.LENGTH_LONG).show();
                                SharedPreferences sharedPreferences = ActivityLogin.this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString( PreferenceHandler.USER_TYPE, authObj.getUserinformation());
                                editor.putString("userTokenId", authObj.getUserinformation().getToken());
                                editor.putString("user_name", authObj.getUserinformation().getFname() + " " + authObj.getUserinformation().getLname());
                                editor.putString("email", authObj.getUserinformation().getEmail());
                                editor.putString("image", authObj.getUserinformation().getImage());
                                editor.putString("address1", authObj.getUserinformation().getAddress1());
                                editor.putString("mobilenumber", authObj.getUserinformation().getMobilenumber());
                                editor.putString("user_id", authObj.getUserinformation().getUser_id());
                                editor.putString("fname", authObj.getUserinformation().getFname());
                                editor.putString("lname", authObj.getUserinformation().getLname());
                                editor.putString("card_flag", authObj.getUserinformation().getIs_card_available());
                                editor.commit();
                                startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
                                finish();

                            }

                        } else {
                            Toast.makeText(ActivityLogin.this,
                                    "could not complete the fb login, please try again", Toast.LENGTH_LONG).show();

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                Toast.makeText(ActivityLogin.this,
                        "could not fetch your address book, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                    Log.d("", "" + error.getMessage() + "," + body);
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String user_name = user.getName();
                String fName = "", lName = "";
                if (user_name.contains(" ")) {
                    fName = user_name.split(" ")[0];
                    lName = user_name.split(" ")[1];
                } else {
                    fName = user_name;
                }
                String fcmToken = PreferenceHandler.readString(ActivityLogin.this, PreferenceHandler.FCM_TOKEN, "");
                if (FirebaseInstanceId.getInstance().getToken() != null) {
                    fcmToken = FirebaseInstanceId.getInstance().getToken();
                    Log.d("Firebase token: ", "callLoginApi: " + fcmToken);
                } else {
                    fcmToken = "0";
                }
                params.put("fcm_token", fcmToken);
                params.put("email", user.getEmail());
                params.put("social_id", fb_user_id);
                params.put("fname", fName);
                params.put("lname", lName);
//                params.put("fcm_token",fcmToken);
                params.put("country", "Australia");
                params.put("country_code", "AUS");
                params.put("fb_token", fb_authToken);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }
}
