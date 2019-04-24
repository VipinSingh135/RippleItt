package com.rippleitt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.rippleitt.modals.AccountDetailTemplate;
import com.rippleitt.modals.CardAccountsTemplate;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

public class ActivityBankAccount extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mrelImageBackPayment;
    private EditText medittxtAccountNumber;
    private EditText medittxtBSB;
    private EditText medittxtName;
    private EditText edittxtBankName;
    private Spinner spnrHolderType;
    private Spinner spnrAccountType;
    private Button mbtnNextAddPayment;
    private ProgressDialog mPDialog;
    String spinnerHolderItem[] = {"Personal", "Business"};
    String spinnerAccountItem[] = {"Savings", "Checking"};
    ArrayAdapter<CharSequence> spinnerHolderArr;
    ArrayAdapter<CharSequence> spinnerAccounttypeArr;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_bank_account);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        init();
        volleyFetchAccountDetails();
    }


    public void init() {
        mrelImageBackPayment = (RelativeLayout) findViewById(R.id.relImageBackPayment);
        mrelImageBackPayment.setOnClickListener(this);
        medittxtAccountNumber = (EditText) findViewById(R.id.edittxtAccountNumber);
        //mImgVwFinish.setOnClickListener(this);
        spnrHolderType = (Spinner) findViewById(R.id.spnrHolderType);
        spnrAccountType = (Spinner) findViewById(R.id.spnrAccountType);
        medittxtBSB = (EditText) findViewById(R.id.edittxtBSB);
        medittxtName = (EditText) findViewById(R.id.edittxtName);
        edittxtBankName = (EditText) findViewById(R.id.edittxtBankName);
        mbtnNextAddPayment = (Button) findViewById(R.id.btnNextAddPayment);

        medittxtAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPDialog = new ProgressDialog(this);
        mrelImageBackPayment.setOnClickListener(this);
        mbtnNextAddPayment.setOnClickListener(this);

        //==================spinner_acc===============
        spinnerAccounttypeArr = ArrayAdapter.createFromResource(ActivityBankAccount.this, R.array.strArrayAccType, R.layout.custom_spinner_item);
        spinnerAccounttypeArr.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spnrAccountType.setAdapter(spinnerAccounttypeArr);

        //==================spinner_holder===============
        spinnerHolderArr = ArrayAdapter.createFromResource(ActivityBankAccount.this, R.array.strArrayHolderType, R.layout.custom_spinner_item);
        spinnerHolderArr.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spnrHolderType.setAdapter(spinnerHolderArr);

    }

    @Override
    public void onClick(View view) {
        //============back_icon==========
        if (view == mrelImageBackPayment) {
            finish();
        }
        //=============btnNextAddPayment============
        if (view == mbtnNextAddPayment) {
            if (validate()) {
//                new ActivityBankAccount.PostCardAsync().execute();
                //captureCardInfo();
                volleyPostAccountDetails();
            }

        }
    }

    private boolean validate() {
        if (medittxtAccountNumber.getText().toString().trim().toCharArray().length != 10) {
            medittxtAccountNumber.setError("Please provide a valid account number");
            return false;
        }
        if (edittxtBankName.getText().toString().trim().equalsIgnoreCase("")) {
            edittxtBankName.setError("Please provide bank name");
            return false;
        }if (spnrAccountType.getSelectedItemPosition()==0){
//            spnrAccountType.setError("Please provide bank name");
            Toast.makeText(getBaseContext(),"Please select account type",Toast.LENGTH_LONG).show();
            return false;
        }if (spnrHolderType.getSelectedItemPosition()==0){
//            spnrAccountType.setError("Please provide bank name");
            Toast.makeText(getBaseContext(),"Please select holder type",Toast.LENGTH_LONG).show();
            return false;
        }
        if (medittxtBSB.getText().toString().trim().toCharArray().length != 6) {
            medittxtBSB.setError("Please provide valid BSB");
            return false;
        }
        if (medittxtName.getText().toString().trim().equalsIgnoreCase("")) {
            medittxtName.setError("Please provide your full name");
            return false;
        }
        if (!medittxtName.getText().toString().trim().contains(" ")) {
            medittxtName.setError("Please provide your first name and last name");
            return false;
        }
        return true;
    }

    private void volleyFetchAccountDetails() {
        mPDialog.setCancelable(false);
        mPDialog.setMessage("Getting account details");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_ACCOUNT_DETAILS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();
                        ;
//                        {"response_code":1,"response_message":"operation performed successfully","bank_account_name":"new account","bank_account_bsb":"155525","account_type":"savings","holder_type":"personal","bank_account_number":"2825255825"}
                        AccountDetailTemplate accountDetails = new AccountDetailTemplate();
                        accountDetails = (AccountDetailTemplate) new Gson().fromJson(response, AccountDetailTemplate.class);
                        if (accountDetails.getAccount_name() != null) {
                            medittxtName.setText(accountDetails.getAccount_name());
                        }
                        if (accountDetails.getBank_name() != null) {
                            edittxtBankName.setText(accountDetails.getBank_name());
                        }
                        if (accountDetails.getAccount_bsb() != null) {
                            medittxtBSB.setText(accountDetails.getAccount_bsb());
                        }
                        if (accountDetails.getAccount_number() != null) {
                            medittxtAccountNumber.setText(accountDetails.getAccount_number());
                        }
                        if (accountDetails.getAccount_type() != null) {
                            if (accountDetails.getAccount_type().equals("savings"))
                                spnrAccountType.setSelection(1);
                            else
                                spnrAccountType.setSelection(2);
                        }
                        if (accountDetails.getHolder_type() != null) {
                            if (accountDetails.getHolder_type().equals("personal"))
                                spnrHolderType.setSelection(1);
                            else
                                spnrHolderType.setSelection(2);
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                ;
                Toast.makeText(ActivityBankAccount.this,
                        "could not fetch account details, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("token", PreferenceHandler.readString(ActivityBankAccount.this,
                        PreferenceHandler.AUTH_TOKEN, ""));

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void volleyPostAccountDetails() {
        mPDialog.setMessage("Submitting account details");
        mPDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_ACCOUNT_DETAILS_SUBMIT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();

                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate) gson.fromJson(response, CategoryListTemplate.class);
                        if (response_.getResponse_code() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBankAccount.this);
                            builder.setMessage("Your bank accout details have been submitted successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                            PreferenceHandler.writeString(ActivityBankAccount.this, PreferenceHandler.ACCOUNT_NUMBER, medittxtAccountNumber.getText().toString());
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Could not submit your response", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                Toast.makeText(ActivityBankAccount.this, "could not submit your response, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityBankAccount.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("account_name", medittxtName.getText().toString());
                params.put("account_bsb", medittxtBSB.getText().toString());
                params.put("account_type", spnrAccountType.getSelectedItem().toString().toLowerCase());
                params.put("bank_name", edittxtBankName.getText().toString());
                params.put("holder_type", spnrHolderType.getSelectedItem().toString().toLowerCase());
                params.put("account_number", medittxtAccountNumber.getText().toString());

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "account_details");

    }

}
