package com.rippleitt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.rippleitt.modals.PriceRangeResponseTemplate;
import com.rippleitt.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

public class ReferFriendActivity extends AppCompatActivity implements View.OnClickListener{

   RelativeLayout mrelReferFriendBack;
   Button mBtnShareCode;
   //TextView mTxtVWCopyLink;
    private RadioButton mRdBtnEmail, mRdBtnSMS;
    private EditText mEdtxtID;
    private ImageView mImgVwBack;
    private ProgressDialog mPDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);
        init();
    }

    public void init(){
        mPDialog=new ProgressDialog(this);
        mPDialog.setCancelable(false);
        mEdtxtID=(EditText)findViewById(R.id.edtxtSharePhone);
        mrelReferFriendBack=(RelativeLayout)findViewById(R.id.relReferFriendBack);
        mImgVwBack=(ImageView)findViewById(R.id.imgBackReferFriend);
        mImgVwBack.setOnClickListener(this);
        mBtnShareCode=(Button)findViewById(R.id.btnShareReferralCode);
        mRdBtnEmail=(RadioButton)findViewById(R.id.rdBtnEmail);
        mRdBtnSMS=(RadioButton)findViewById(R.id.edBtnPhone);
        mRdBtnSMS.setOnClickListener(this);
        mRdBtnEmail.setOnClickListener(this);
        mBtnShareCode.setOnClickListener(this);
        mrelReferFriendBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==mrelReferFriendBack){
            finish();
        }if(view==mBtnShareCode){
            if(validate()){
                volleyPostShareRequest();
            }
        }if(view==mImgVwBack){
            finish();
        }if(view==mRdBtnEmail){
            mEdtxtID.setInputType(InputType.TYPE_CLASS_TEXT);
            mEdtxtID.setHint("Email*");
            mEdtxtID.setText("");
            mEdtxtID.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1000)});
        }if(view==mRdBtnSMS){
            mEdtxtID.setText("");
            mEdtxtID.setInputType(InputType.TYPE_CLASS_PHONE);
            mEdtxtID.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
            mEdtxtID.setHint("Contact Number*");
        }
    }






    private boolean validate(){
        if(mRdBtnEmail.isChecked()){// type email
            if(!CommonUtils.isValidEmail(mEdtxtID.getText().toString())){
                mEdtxtID.setError("Please provide a valid email.");
                return false;
            }
            SharedPreferences sharedPreferences=ReferFriendActivity.this
                    .getSharedPreferences("preferences", Context.MODE_PRIVATE);

            if(mEdtxtID.getText().toString().equalsIgnoreCase(sharedPreferences.getString("email",""))){
                mEdtxtID.setError("You cannot share a listing to yourself.");
                return false;
            }
        }if(mRdBtnSMS.isChecked()){
            if(mEdtxtID.getText().toString().toCharArray().length<10){
                mEdtxtID.setError("Please provide a valid contact number.");
                return false;
            }
            SharedPreferences sharedPreferences=ReferFriendActivity.this
                    .getSharedPreferences("preferences", Context.MODE_PRIVATE);

            if(mEdtxtID.getText().toString().equalsIgnoreCase(sharedPreferences.getString("mobilenumber",""))){
                mEdtxtID.setError("You cannot share  a listing to yourself.");
                return false;
            }

            //mobilenumber
        }
        return true;
    }



    private void volleyPostShareRequest(){

        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getShareListing() ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        try{
                            PriceRangeResponseTemplate response_ = (PriceRangeResponseTemplate)gson.fromJson(response,PriceRangeResponseTemplate.class);
                            if(response_.getResponse_code()==1){

                                AlertDialog.Builder builder = new AlertDialog.Builder(ReferFriendActivity.this);
                                builder.setMessage("The listing has been shared successfully! ")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                finish();
                                            }
                                        });
                                builder.show();

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Invalid keywords",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){

                        }

                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReferFriendActivity.this,
                        "could not complete your request, please try again",
                        Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ReferFriendActivity.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id",RippleittAppInstance
                                            .getInstance()
                                            .getSELECTED_LISTING_DETAIL_OBJECT()
                                            .getListing_id());
                params.put("email",mEdtxtID.getText().toString().trim());
                params.put("mobile",mEdtxtID.getText().toString().trim());
                if(mRdBtnEmail.isChecked()){
                    params.put("type","1");
                }else{
                    params.put("type","2");
                }
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_categories");
    }
}
