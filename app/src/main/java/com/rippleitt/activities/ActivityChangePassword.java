package com.rippleitt.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.ChangePasswordTemplate;
import com.rippleitt.utils.CommonUtils;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;
import java.util.Map;

public class ActivityChangePassword extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout mrelChangePassBack;
    private EditText medittxtPass;
    private EditText medittxtNewPass;
    private EditText medittxtConfirmPass;
    private Button mbtnSavePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
    }


    public void init(){
        mrelChangePassBack=(RelativeLayout)findViewById(R.id.relChangePassBack);
        medittxtPass=(EditText) findViewById(R.id.edittxtPass);
        medittxtNewPass=(EditText) findViewById(R.id.edittxtNewPass);
        medittxtConfirmPass=(EditText) findViewById(R.id.edittxtConfirmPass);
        mbtnSavePass=(Button) findViewById(R.id.btnSavePass);
        mbtnSavePass.setOnClickListener(this);
        mrelChangePassBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==mrelChangePassBack){
            finish();
        }
        if (view==mbtnSavePass){
                if(validate()){
                    changePasswordApi(CommonUtils.convertToMD5(medittxtPass.getText().toString()),CommonUtils.convertToMD5(medittxtNewPass.getText().toString()));

                }

        }
    }


    private void changePasswordApi(final String current_password, final String new_password ){
        //CommonUtils.showProgress(AddNewProduct.this, "Please wait...");

        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getCHANGE_PASSWORD() ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        ChangePasswordTemplate response_ = (ChangePasswordTemplate) gson.fromJson(response,ChangePasswordTemplate.class);
                        if(response_.getResponse_code()==1){


                            showConfirmationDialog();



                        }else{

                            Toast.makeText(ActivityChangePassword.this,
                                    response_.getResponse_message(),
                                    Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   Toast.makeText(ActivityChangePassword.this,"could not fetch categories, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityChangePassword.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("current_password",current_password );
                params.put("new_password",new_password  );

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }


    private void showConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your password has been successfully updated. For your account's safety, you will be logged out now. " +
                "Please login using your new password.")
                .setCancelable(false)
                .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        SharedPreferences sharedPreferences=getSharedPreferences(RippleittAppInstance.PREFERENCES,MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(ActivityChangePassword.this,
                                ActivityLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();



                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



    private boolean validate(){
        if(medittxtPass.getText().toString().equalsIgnoreCase("")){
            medittxtPass.setError("Please enter your current password");
            return false;
        }else if(medittxtPass.getText().toString().trim().equalsIgnoreCase("")){
            medittxtPass.setError("Please enter valid password");
            return false;
        }else if(medittxtNewPass.getText().toString().equalsIgnoreCase("")){
            medittxtNewPass.setError("Please enter new password");
            return false;
        }else if(medittxtNewPass.getText().toString().trim().equalsIgnoreCase("")){
            medittxtNewPass.setError("New password must be minimum six characters long");
            return false;
        }else if(medittxtNewPass.getText().toString().trim().toCharArray().length<6){
            medittxtNewPass.setError("New password must be minimum six characters long");
            return false;
        }else if(!medittxtConfirmPass.getText().toString().trim().equals(medittxtNewPass.getText().toString().trim())){
            medittxtConfirmPass.setError("New password and confirm password do not match");
            return false;
        }else {
            return true;
        }
    }


}
