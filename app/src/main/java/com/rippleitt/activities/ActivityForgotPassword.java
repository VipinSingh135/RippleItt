package com.rippleitt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on password/29/2018.
 */

public class ActivityForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mRelLytBack;
    private EditText mEdtxtEmail;
    private Button mBtnForgotPassword;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_forgot_password);
        initUI();
    }


    private void initUI(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");
        mRelLytBack=(RelativeLayout)findViewById(R.id.relSignup);
        mEdtxtEmail=(EditText)findViewById(R.id.edittxtEmailSignup);
        mBtnForgotPassword=(Button)findViewById(R.id.btnNextSignup);
        mRelLytBack.setOnClickListener(this);
        mBtnForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==mRelLytBack){
            finish();
        }if(view==mBtnForgotPassword){
            if(validate()){
                requestRecoveryToken();
            }
        }
    }

    private boolean validate(){
        if(!CommonUtils.isValidEmail(mEdtxtEmail.getText().toString().trim())){
            mEdtxtEmail.setError("Please provide a valid email");
            /*
            Toast.makeText(ActivityForgotPassword.this,
                    "Please provide a valid email",
                    Toast.LENGTH_SHORT).show();
                    */
            return false;
        }
        return true;
    }


    private void requestRecoveryToken(){
        mProgressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FORGOT_PASSWORD ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate)gson.fromJson(response,
                                CategoryListTemplate.class);
                        if(response_.getResponse_code()==1){
                            RippleittAppInstance.getInstance().setAccountEmail(mEdtxtEmail.getText().toString().trim());
                            startActivity(new Intent(ActivityForgotPassword.this,ActivityRecoverPassword.class));
                        }else{
                            Toast.makeText(getApplicationContext(), response_.getResponse_message(),
                                    Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ActivityForgotPassword.this,
                        "could not complete your request.",
                        Toast.LENGTH_SHORT).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email",mEdtxtEmail.getText().toString().trim());
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }

}
