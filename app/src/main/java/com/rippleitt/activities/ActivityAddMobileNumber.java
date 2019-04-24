package com.rippleitt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 06/06/18.
 */

public class ActivityAddMobileNumber extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnSubmit;
    private ProgressDialog mPDialog;
    private EditText mEdtxtPhone;
    private EditText mEdtxtCode;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_phone);
        initUI();
    }


    private void initUI(){
        mPDialog=new ProgressDialog(this);
        mPDialog.setCancelable(false);
        mEdtxtPhone=(EditText)findViewById(R.id.edittxtPhone);
        mEdtxtCode=(EditText)findViewById(R.id.edittxtPhoneCode);
        mBtnSubmit=(Button)findViewById(R.id.btnVerifyOTP);
        mBtnSubmit.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        if(mBtnSubmit==view){
            if(validate()){
                volleyDispatchOtp();
            }
        }
    }


    private boolean validate(){
        if(mEdtxtCode.getText().toString().trim()
                .equalsIgnoreCase("")
                ||mEdtxtCode.getText().toString().trim().toCharArray().length>3){
            mEdtxtCode.setError("Please provide a valid country code");
            return false;
        }
        if(mEdtxtPhone.getText().toString().trim()
                .equalsIgnoreCase("")
                ||mEdtxtPhone.getText().toString().trim().toCharArray().length<9){
            mEdtxtPhone.setError("Please provide a valid contact number");
            return false;
        }
        return true;
    }



    private void volleyDispatchOtp(){
        mPDialog.setMessage("Submitting your request..");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getUpdateMobileNumber() ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate)gson.fromJson(response,CategoryListTemplate.class);
                        if(response_.getResponse_code()==1){
                            showSuccessAlert("OTP has been sent to your contact number",0);

                        }else{
                            Toast.makeText(getApplicationContext(), response_.getResponse_message(), Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                Toast.makeText(ActivityAddMobileNumber.this,"Could not complete your request",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                String userID= PreferenceHandler.readString(ActivityAddMobileNumber.this, PreferenceHandler.USER_ID,"");
                params.put("user_id",userID);
                params.put("mobilenumber",mEdtxtCode.getText().toString().trim()+mEdtxtPhone.getText().toString().trim());

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }



    private void showSuccessAlert(final String message, final int flag){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                            RippleittAppInstance.getInstance().setEmailToVerify(mEdtxtCode.getText().toString().trim()+mEdtxtPhone.getText().toString().trim());
                            RippleittAppInstance.getInstance().setVerificationMode(2);// mobile
                            RippleittAppInstance.getInstance().setMobileNUmberToVerify(mEdtxtCode.getText().toString().trim()+mEdtxtPhone.getText().toString().trim());
                            startActivity(new Intent(ActivityAddMobileNumber.this, ActivityVerifyEmail.class));
//                            finish();


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
