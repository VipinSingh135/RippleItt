package com.rippleitt.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.utils.CommonUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 06/06/18.
 */

public class ActivityVerifyEmail extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdtxtEmail;
    private EditText mEdtxtOTP;
    private Button mBtnSubmit;
    private TextView mtxtVwResendOTP;
    private RelativeLayout relBack;
    private ProgressDialog mPDialog;
    private TextView mtxtVwTitle;
    private TextView tvPhoneVerifyTxt;
    private LinearLayout linearStep3, linearStep4;

    private EditText mEdtxtPhoneNumber;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_validate_email);
        initUI();
    }


    @Override
    public void onClick(View view) {
        if (view == mBtnSubmit) {
            if (validate()) {
                volleyValidateEmail();
            }
        }
        if (view == mtxtVwResendOTP) {
            voleyResendOtp();
        }
        if (view == relBack) {
            finish();
        }

    }

    private void initUI() {
        mtxtVwTitle = (TextView) findViewById(R.id.txtvwTitle);
        mEdtxtEmail = (EditText) findViewById(R.id.edittxtEmailLogin);
        mEdtxtPhoneNumber = (EditText) findViewById(R.id.edittxtEmailLogin);
        relBack = findViewById(R.id.relBack);

        mEdtxtOTP = (EditText) findViewById(R.id.edittxtPassLogin);
        mtxtVwResendOTP = (TextView) findViewById(R.id.txtvwResendOTP);
        tvPhoneVerifyTxt = (TextView) findViewById(R.id.tvPhoneVerifyTxt);
        mBtnSubmit = (Button) findViewById(R.id.btnVerifyOTP);
        linearStep4 = findViewById(R.id.linearStep4);
        linearStep3 = findViewById(R.id.linearStep3);
//        relativeEmail=findViewById(R.id.relEmailLogin);

        mBtnSubmit.setOnClickListener(this);
        mtxtVwResendOTP.setOnClickListener(this);
        relBack.setOnClickListener(this);
        mEdtxtEmail.setText(RippleittAppInstance.getInstance().getEmailToVerify());
        mEdtxtEmail.setEnabled(false);
        mPDialog = new ProgressDialog(this);
        if (RippleittAppInstance.getInstance().getVerificationMode() == 2) {
            mtxtVwTitle.setText("VERIFY CONTACT NUMBER");
            tvPhoneVerifyTxt.setVisibility(View.VISIBLE);
            tvPhoneVerifyTxt.setText(getString(R.string.strEnterPhoneOtp));
            linearStep4.setVisibility(View.VISIBLE);
            linearStep3.setVisibility(View.INVISIBLE);
            relBack.setVisibility(View.VISIBLE);
        }
        if (RippleittAppInstance.getInstance().getVerificationMode() == 1) {
            tvPhoneVerifyTxt.setVisibility(View.VISIBLE);
            tvPhoneVerifyTxt.setText(getString(R.string.strEnterEmailOtp));
            mtxtVwTitle.setText("VERIFY EMAIL");
            linearStep4.setVisibility(View.INVISIBLE);
            linearStep3.setVisibility(View.VISIBLE);
            relBack.setVisibility(View.INVISIBLE);
        }
    }


    private boolean validate() {
        if (mEdtxtOTP.getText().toString().trim().equalsIgnoreCase("")) {
            mEdtxtOTP.setError("Please provide OTP");
            return false;
        }

        return true;
    }


    private void voleyResendOtp() {
        mPDialog.setMessage("Submitting your request..");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getResendOtp(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate) gson.fromJson(response, CategoryListTemplate.class);
                        if (response_.getResponse_code() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityVerifyEmail.this);
                            builder.setMessage("OTP has been successfully resent!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Could not complete your request.", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                Toast.makeText(ActivityVerifyEmail.this, "Could not complete your request", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", mEdtxtEmail.getText().toString().trim());
                params.put("mobilenumber", mEdtxtEmail.getText().toString().trim());
                if (RippleittAppInstance.getInstance().getVerificationMode() == 2) {
                    params.put("type", "2");
                } else {
                    params.put("type", "1");
                }


                params.put("otp", mEdtxtOTP.getText().toString().trim());
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");

    }


    private void volleyValidateEmail() {

        if (RippleittAppInstance.getInstance().getVerificationMode() == 2) {
            mPDialog.setMessage("Verifying your contact number...");
        } else {
            mPDialog.setMessage("Verifying your email address...");
        }


        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getVerifyUserCredentials(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();
                        Log.d("", response.toString());
//                            Gson gson = new Gson();
//                            CategoryListTemplate response_ = (CategoryListTemplate)gson.fromJson(response,CategoryListTemplate.class);
//                            if(response_.getResponse_code()==1){
//                                if(RippleittAppInstance.getInstance().getVerificationMode()==2){
//                                    showSuccessAlert("Contact number verified successfully",1);
//                                }else{
//                                    showSuccessAlert("Email verified successfully",1);
//                                }
//
//
//                            }else{
//                                Toast.makeText(getApplicationContext(), "Invalid OTP.", Toast.LENGTH_SHORT).show();
//                                // show response_message in alert...
//                            }
                        //   Log.e("total",total);
                        Log.e("Response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("response_code");
                            String msg = object.getString("response_message");

                            Log.e("Status", status + "");
                            if (status.equalsIgnoreCase("1")) {

                                if (RippleittAppInstance.getInstance().getVerificationMode() == 2) {
//                                    showSuccessAlert("Contact number verified successfully",1);


                                    JSONObject userDetailsObj = object.getJSONObject("userinformation");

                                    String userName = userDetailsObj.getString("fname") + " " + userDetailsObj.getString("lname");
                                    String userTokenId = userDetailsObj.getString("token");
                                    String email = userDetailsObj.getString("email");
                                    String mobilenumber = userDetailsObj.getString("mobilenumber");
                                    String longitude = userDetailsObj.getString("longitude");
                                    String latitude = userDetailsObj.getString("latitude");
                                    String address1 = userDetailsObj.getString("address1");
//                        String postal_code = userDetailsObj.getString("post_code");
                                    String address2 = userDetailsObj.getString("address2");
                                    String gender = userDetailsObj.getString("gender");
                                    String image = userDetailsObj.getString("image");
                                    String referalcode = userDetailsObj.getString("referalcode");
                                    String user_id = userDetailsObj.getString("user_id");
                                    String user_type = userDetailsObj.getString("user_type");
                                    String abn_number = userDetailsObj.getString("abn_number");
                                    String account_number = userDetailsObj.getString("bank_account_number");
                                    String business_name = userDetailsObj.getString("business_name");
                                    PreferenceHandler.writeString(getApplicationContext(), PreferenceHandler.USER_TYPE, user_type);
                                    PreferenceHandler.writeString(getApplicationContext(), PreferenceHandler.ABN_NUMBER, abn_number);
                                    PreferenceHandler.writeString(getApplicationContext(), PreferenceHandler.BUSINESS_NAME, business_name);
                                    PreferenceHandler.writeString(getApplicationContext(), PreferenceHandler.ACCOUNT_NUMBER, account_number);

                                    String email_verificationFlag = userDetailsObj.getString("email_verified");
                                    String mobile_verificationFlag = userDetailsObj.getString("phone_verified");

                                    RippleittAppInstance.TOKEN_ID = userTokenId;
                                    RippleittAppInstance.userObject = userDetailsObj;
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("user_id", user_id);
                                    editor.commit();

                                    PreferenceHandler.writeString(getApplicationContext(), PreferenceHandler.INIT_MODE, PreferenceHandler.LOGIN);
                                    RippleittAppInstance.getInstance().setEmailVerified(email_verificationFlag);
                                    RippleittAppInstance.getInstance().setMobileVerified(mobile_verificationFlag);
                                    if (email_verificationFlag.equalsIgnoreCase("0")) {
                                        RippleittAppInstance.getInstance().setVerificationMode(1);
                                        RippleittAppInstance.getInstance().setEmailToVerify(email);
                                        startActivity(new Intent(getBaseContext(), ActivityVerifyEmail.class));
                                    }
                                    if (email_verificationFlag.equalsIgnoreCase("1") &&
                                            (mobile_verificationFlag.equalsIgnoreCase("0"))) {
                                        RippleittAppInstance.getInstance().setVerificationMode(2);
                                        startActivity(new Intent(getBaseContext(), ActivityAddMobileNumber.class));
                                    }
                                    if (email_verificationFlag.equalsIgnoreCase("1") &&
                                            (mobile_verificationFlag.equalsIgnoreCase("1"))) {

                                        editor.putString("userTokenId", userTokenId);
                                        editor.putString("user_name", userName);
                                        editor.putString("email", email);
                                        editor.putString("image", image);
                                        editor.putString("address1", address1);
//                            editor.putString("postal_codes", post_code);
                                        editor.putString("address2", address2);
                                        editor.putString("mobilenumber", mobilenumber);
                                        editor.putString("user_id", user_id);
                                        editor.putString("fname", userDetailsObj.getString("fname"));
                                        editor.putString("lname", userDetailsObj.getString("lname"));
                                        editor.putString("abn_no", userDetailsObj.getString("abn_number"));
                                        editor.putString("business_name", userDetailsObj.getString("business_name"));
                                        editor.commit();
                                        TastyToast.makeText(getBaseContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                        Intent intent= new Intent(getBaseContext(), ActivityHome.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    finish();

                                } else {
                                    showSuccessAlert("Email verified successfully", 1);
                                }

                            } else {
                                TastyToast.makeText(getBaseContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                Toast.makeText(ActivityVerifyEmail.this, "Invalid OTP", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                if (RippleittAppInstance.getInstance().getVerificationMode() == 1) {
                    params.put("type", "1");
                    params.put("email", mEdtxtEmail.getText().toString().trim());
                } else {
                    params.put("type", "2");
                    params.put("mobilenumber", mEdtxtPhoneNumber.getText().toString().trim());
                }
//                    params.put("email",mEdtxtEmail.getText().toString().trim());
//                    params.put("mobilenumber",mEdtxtEmail.getText().toString().trim());

                params.put("otp", mEdtxtOTP.getText().toString().trim());
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");

    }

    private void showSuccessAlert(final String message, final int flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (RippleittAppInstance.getInstance().getVerificationMode() == 1) {
                            startActivity(new Intent(ActivityVerifyEmail.this, ActivityAddMobileNumber.class));
                            finish();
                        } else {
                            if (PreferenceHandler.readString(ActivityVerifyEmail.this, PreferenceHandler.INIT_MODE, PreferenceHandler.LOGIN)
                                    .equalsIgnoreCase(PreferenceHandler.LOGIN)) {
                                startActivity(new Intent(ActivityVerifyEmail.this, ActivityWelcome.class));
                            } else {
                                startActivity(new Intent(ActivityVerifyEmail.this, ActivityWelcome.class));
                            }
                            finish();
                        }


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
