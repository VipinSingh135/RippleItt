package com.rippleitt.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on password/29/2018.
 */

public class ActivityRecoverPassword extends AppCompatActivity
        implements View.OnClickListener {

    private EditText mEdtxtEmail;
    private EditText mEditTextOtp;
    private EditText mEdtxtPassword;
    private EditText mEdtxtConfirmPassword;
    private Button mBtnUpdatePassword;
    private ProgressDialog mProgressDialog;
    private ImageView mImgVwFinish;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_recover_password);
        initUI();
    }


    private void initUI() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Updating your password...");
        mImgVwFinish = (ImageView) findViewById(R.id.imgBackSignup);
        mImgVwFinish.setOnClickListener(this);
        mProgressDialog.setCancelable(false);
        mEdtxtEmail = (EditText) findViewById(R.id.edittxtEmail);
        mEdtxtEmail.setText(RippleittAppInstance.getInstance().getAccountEmail());
        mEditTextOtp = (EditText) findViewById(R.id.edtxtOtp);
        mEdtxtPassword = (EditText) findViewById(R.id.edittxPassSignup);
        mEdtxtConfirmPassword = (EditText) findViewById(R.id.edittxtConfirmPassSignup);
        mBtnUpdatePassword = (Button) findViewById(R.id.btnUpdatePassword);
        mBtnUpdatePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnUpdatePassword) {
            if (validate()) {
                updatePassword();
            }
        }
        if (view == mImgVwFinish) {
            finish();
        }
    }

    private boolean validate() {
        if (!CommonUtils.isValidEmail(mEdtxtEmail.getText().toString().trim())) {

            mEdtxtEmail.setError("Please provide a valid email");

            return false;
        }
        if (mEditTextOtp.getText().toString().trim().equalsIgnoreCase("")) {
            mEditTextOtp.setError("Please provide OTP");

            return false;
        }
        if (mEdtxtPassword.getText().toString().equalsIgnoreCase("")) {
            mEdtxtPassword.setError("Please provide new password.");
            return false;
        }
        if (mEdtxtPassword.getText().toString().trim().equalsIgnoreCase("")
                || mEdtxtPassword.getText().toString().trim().toCharArray().length < 6) {
            mEdtxtPassword.setError("Please provide a valid six character password.");
            return false;
        }
        if (!mEdtxtPassword.getText().toString().equals(mEdtxtConfirmPassword.getText().toString())) {
            mEdtxtConfirmPassword.setError("Password and confirm password do not match");

            return false;
        }

        return true;
    }


    private void updatePassword() {
        mProgressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.OTP_VALIDATION,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate) gson.fromJson(response,
                                CategoryListTemplate.class);
                        if (response_.getResponse_code() == 1) {

                            showUpdationDialog();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    response_.getResponse_message(),
                                    Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ActivityRecoverPassword.this,
                        "could not complete your request.",
                        Toast.LENGTH_SHORT).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", mEdtxtEmail.getText().toString().trim());
                params.put("otp", mEditTextOtp.getText().toString().trim());
                params.put("pw", convertToMD5(mEdtxtConfirmPassword.getText().toString().trim()));
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
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


    private void showUpdationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your password has been successfully updated. For your account's safety, you will be logged out now." +
                " Please login using your new password.")
                .setCancelable(false)
                .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = getSharedPreferences(RippleittAppInstance.PREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(ActivityRecoverPassword.this,
                                ActivityLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
