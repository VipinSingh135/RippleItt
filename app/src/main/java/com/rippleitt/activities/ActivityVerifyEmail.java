package com.rippleitt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private LinearLayout linearStep3,linearStep4;

    private EditText mEdtxtPhoneNumber;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_validate_email);
        initUI();
    }


    @Override
    public void onClick(View view) {
        if(view==mBtnSubmit){
            if(validate()){
                volleyValidateEmail();
            }
        }if(view==mtxtVwResendOTP){
            voleyResendOtp();
        }
        if(view==relBack){
            finish();
        }

    }

    private void initUI(){
        mtxtVwTitle=(TextView) findViewById(R.id.txtvwTitle);
        mEdtxtEmail=(EditText)findViewById(R.id.edittxtEmailLogin);
        mEdtxtPhoneNumber=(EditText)findViewById(R.id.edittxtEmailLogin);
        relBack=findViewById(R.id.relBack);

        mEdtxtOTP=(EditText)findViewById(R.id.edittxtPassLogin);
        mtxtVwResendOTP=(TextView)findViewById(R.id.txtvwResendOTP);
        tvPhoneVerifyTxt=(TextView)findViewById(R.id.tvPhoneVerifyTxt);
        mBtnSubmit=(Button)findViewById(R.id.btnVerifyOTP);
        linearStep4=findViewById(R.id.linearStep4);
        linearStep3=findViewById(R.id.linearStep3);
//        relativeEmail=findViewById(R.id.relEmailLogin);

        mBtnSubmit.setOnClickListener(this);
        mtxtVwResendOTP.setOnClickListener(this);
        relBack.setOnClickListener(this);
        mEdtxtEmail.setText(RippleittAppInstance.getInstance().getEmailToVerify());
        mEdtxtEmail.setEnabled(false);
        mPDialog=new ProgressDialog(this);
        if(RippleittAppInstance.getInstance().getVerificationMode()==2){
            mtxtVwTitle.setText("VERIFY CONTACT NUMBER");
            tvPhoneVerifyTxt.setVisibility(View.VISIBLE);
            tvPhoneVerifyTxt.setText(getString(R.string.strEnterPhoneOtp));
            linearStep4.setVisibility(View.VISIBLE);
            linearStep3.setVisibility(View.INVISIBLE);
            relBack.setVisibility(View.VISIBLE);
        }if(RippleittAppInstance.getInstance().getVerificationMode()==1){
            tvPhoneVerifyTxt.setVisibility(View.VISIBLE);
            tvPhoneVerifyTxt.setText(getString(R.string.strEnterEmailOtp));
            mtxtVwTitle.setText("VERIFY EMAIL");
            linearStep4.setVisibility(View.INVISIBLE);
            linearStep3.setVisibility(View.VISIBLE);
            relBack.setVisibility(View.INVISIBLE);
        }
    }


    private boolean validate(){
        if(mEdtxtOTP.getText().toString().trim().equalsIgnoreCase("")){
            mEdtxtOTP.setError("Please provide OTP");
            return false;
        }

        return true;
    }



    private void voleyResendOtp(){
        mPDialog.setMessage("Submitting your request..");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getResendOtp() ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate)gson.fromJson(response,CategoryListTemplate.class);
                        if(response_.getResponse_code()==1){
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
                        }else{
                            Toast.makeText(getApplicationContext(), "Could not complete your request.", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                Toast.makeText(ActivityVerifyEmail.this,"Could not complete your request",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("email",mEdtxtEmail.getText().toString().trim());
                params.put("mobilenumber",mEdtxtEmail.getText().toString().trim());
                if(RippleittAppInstance.getInstance().getVerificationMode()==2){
                    params.put("type","2");
                }else{
                    params.put("type","1");
                }


                params.put("otp",mEdtxtOTP.getText().toString().trim());
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");

    }


    private void volleyValidateEmail(){

        if(RippleittAppInstance.getInstance().getVerificationMode()==2){
            mPDialog.setMessage("Verifying your contact number...");
        }else{
            mPDialog.setMessage("Verifying your email address...");
        }


            mPDialog.show();
            StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getVerifyUserCredentials() ,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mPDialog.dismiss();
                            Log.d("", response.toString());
                            Gson gson = new Gson();
                            CategoryListTemplate response_ = (CategoryListTemplate)gson.fromJson(response,CategoryListTemplate.class);
                            if(response_.getResponse_code()==1){
                                if(RippleittAppInstance.getInstance().getVerificationMode()==2){
                                    showSuccessAlert("Contact number verified successfully",1);
                                }else{
                                    showSuccessAlert("Email verified successfully",1);
                                }


                            }else{
                                Toast.makeText(getApplicationContext(), "Invalid OTP.", Toast.LENGTH_SHORT).show();
                                // show response_message in alert...
                            }
                            CommonUtils.dismissProgress();
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mPDialog.dismiss();
                    Toast.makeText(ActivityVerifyEmail.this,"Invalid OTP",Toast.LENGTH_LONG).show();
                    VolleyLog.d("", "Error: " + error.getMessage());
                    Log.d("", ""+error.getMessage()+","+error.toString());
                    CommonUtils.dismissProgress();
                }
            }){
                @Override
                protected Map<String,String> getParams(){

                    Map<String, String> params = new HashMap<>();

                    if(RippleittAppInstance.getInstance().getVerificationMode()==1){
                        params.put("type","1");
                        params.put("email",mEdtxtEmail.getText().toString().trim());
                    }else{
                        params.put("type","2");
                        params.put("mobilenumber",mEdtxtPhoneNumber.getText().toString().trim());
                    }
//                    params.put("email",mEdtxtEmail.getText().toString().trim());
//                    params.put("mobilenumber",mEdtxtEmail.getText().toString().trim());

                    params.put("otp",mEdtxtOTP.getText().toString().trim());
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
                        if(RippleittAppInstance.getInstance().getVerificationMode()==1){
                            startActivity(new Intent(ActivityVerifyEmail.this, ActivityAddMobileNumber.class));
                            finish();
                        }else{
                            if(PreferenceHandler.readString(ActivityVerifyEmail.this, PreferenceHandler.INIT_MODE,PreferenceHandler.LOGIN)
                                    .equalsIgnoreCase(PreferenceHandler.LOGIN)){
                                startActivity(new Intent(ActivityVerifyEmail.this, ActivityWelcome.class));
                            }else{
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
