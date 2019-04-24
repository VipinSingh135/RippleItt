package com.rippleitt.activities;

import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.github.promisepay.PPCard;
import com.github.promisepay.PromisePay;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CardAccountsTemplate;
import com.rippleitt.modals.FbAuthTemplate;
import com.rippleitt.modals.PaymentTokenResponseTemplate;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddPaymentMethod extends AppCompatActivity  implements View.OnClickListener{


    private RelativeLayout mrelImageBackPayment;
    private EditText medittxtCardNumber;
    private EditText medittxtExpireDate;
    private EditText medittxtCvv;
    private Spinner mSpnrMonth, mSpnrYear;
    private EditText medittxtCardHolderName;
    private Button mbtnNextAddPayment;
    private TextView mtxtSkipAddPayment;
    private ProgressDialog mPDialog;
    private String authorisationToken="";
    String card_id="";
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_method);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        init();
        volleyFetchAuthorization();
    }

    public void init(){
        mrelImageBackPayment=(RelativeLayout)findViewById(R.id.relImageBackPayment);
        medittxtCardNumber=(EditText) findViewById(R.id.edittxtCardNumber);
        mSpnrMonth=(Spinner)findViewById(R.id.edittxtExpireMonth);
        mSpnrYear=(Spinner)findViewById(R.id.edittxtExpireYear);

        ArrayAdapter<CharSequence> arrayAdapterMonth = ArrayAdapter.createFromResource(AddPaymentMethod.this, R.array.calendar_months, R.layout.custom_spinner_item);
        ArrayAdapter<CharSequence> arrayAdapterYear = ArrayAdapter.createFromResource(AddPaymentMethod.this, R.array.calendar_year, R.layout.custom_spinner_item);
        arrayAdapterMonth.setDropDownViewResource(R.layout.spinner_textview);
        arrayAdapterYear.setDropDownViewResource(R.layout.spinner_textview);
        mSpnrMonth.setAdapter(arrayAdapterMonth);
        mSpnrYear.setAdapter(arrayAdapterYear);
        medittxtCvv=(EditText) findViewById(R.id.edittxtCvv);
        medittxtCardHolderName=(EditText) findViewById(R.id.edittxtCardHolderName);
        mbtnNextAddPayment=(Button) findViewById(R.id.btnNextAddPayment);
        mtxtSkipAddPayment=(TextView) findViewById(R.id.txtSkipAddPayment);
        //medittxtCardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());
        medittxtCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (count <= medittxtCardNumber.getText().toString().length()
                        &&(medittxtCardNumber.getText().toString().length()==4
                        ||medittxtCardNumber.getText().toString().length()==9
                        ||medittxtCardNumber.getText().toString().length()==14)){
                    medittxtCardNumber.setText(medittxtCardNumber.getText().toString()+" ");
                    int pos = medittxtCardNumber.getText().length();
                    medittxtCardNumber.setSelection(pos);
                }else if (count >= medittxtCardNumber.getText().toString().length()
                        &&(medittxtCardNumber.getText().toString().length()==4
                        ||medittxtCardNumber.getText().toString().length()==9
                        ||medittxtCardNumber.getText().toString().length()==14)){
                    medittxtCardNumber.setText(medittxtCardNumber.getText().toString().substring(0,medittxtCardNumber.getText().toString().length()-1));
                    int pos = medittxtCardNumber.getText().length();
                    medittxtCardNumber.setSelection(pos);
                }
                count = medittxtCardNumber.getText().toString().length();

            }
        });
        mPDialog= new ProgressDialog(this);
        mrelImageBackPayment.setOnClickListener(this);
        mbtnNextAddPayment.setOnClickListener(this);
        mtxtSkipAddPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //============back_icon==========
        if (view==mrelImageBackPayment){
            finish();
        }
        //=============btnNextAddPayment============
        if (view==mbtnNextAddPayment){

            if(validate()){

                new PostCardAsync().execute();
                //captureCardInfo();
            }

        }
        //===============txtSkipAddPayment==========
        if (view==mtxtSkipAddPayment){

            if(RippleittAppInstance.getInstance().getAddCardMode()==0){
                Intent intent = new Intent(AddPaymentMethod.this,
                        ActivityWelcome.class);
                startActivity(intent);
                //  TastyToast.makeText(getApplicationContext(), "SignUp Successfuly", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                finish();
            }else{
                finish();
            }



        }
    }

    private void captureCardInfo(){
        mPDialog.setMessage("Veryfying your card...");
        mPDialog.show();
        PromisePay promisePay = PromisePay.getInstance();
        promisePay.initialize("prelive", "OPTIONAL_PUBLIC_KEY");
        PPCard card = new PPCard(medittxtCardNumber.getText().toString().trim(),
                                medittxtCardHolderName.getText().toString().trim(),
                                mSpnrMonth.getSelectedItem().toString(),
                                mSpnrYear.getSelectedItem().toString(),
                                medittxtCvv.getText().toString().trim());
        promisePay.createCardAccount(authorisationToken, card, new PromisePay.OnPromiseRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    CardAccountsTemplate cardTemplate = new CardAccountsTemplate();
                    cardTemplate=(CardAccountsTemplate)new Gson().fromJson(response.toString(),CardAccountsTemplate.class);
                    String card_id=response.getJSONObject("card_accounts").getString("id");
                    volleyPostCardId(card_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                    //Toast.makeText(AddPaymentMethod.this, e.getMessage(),Toast.LENGTH_LONG).show();
                //return null;
            }
        });

    }

    private boolean validate(){
        if(medittxtCardNumber.getText().toString().trim().toCharArray().length!=19){
            medittxtCardNumber.setError("Please provide a valid credit card number");
            return false;
        } if(mSpnrMonth.getSelectedItem().toString().equalsIgnoreCase("Expiration Month*")){
            Toast.makeText(AddPaymentMethod.this,
                    "Please select expiration month",Toast.LENGTH_LONG).show();
            return false;
        }

        if(mSpnrYear.getSelectedItem().toString().equalsIgnoreCase("Expiration Year*")){
            Toast.makeText(AddPaymentMethod.this,
                    "Please select expiration year",Toast.LENGTH_LONG).show();
            return false;
        }if(medittxtCvv.getText().toString().trim().toCharArray().length!=3){
            medittxtCvv.setError("Please provide valid CVV");
            return false;
        } if(medittxtCardHolderName.getText().toString().trim().equalsIgnoreCase("")){
            medittxtCardHolderName.setError("Please provide your full name");
            return false;
        }if(!medittxtCardHolderName.getText().toString().trim().contains(" ")){
            medittxtCardHolderName.setError("Please provide your first name and last name");
            return false;
        }

        int monthCode= Calendar.getInstance().get(Calendar.MONTH);
        int yearCode= Calendar.getInstance().get(Calendar.YEAR);
        String strYear= Integer.toString(yearCode);
        if(mSpnrYear.getSelectedItem().toString().equalsIgnoreCase(strYear)){
            int selectedMonthPosition=mSpnrMonth.getSelectedItemPosition();
            if(selectedMonthPosition<(monthCode+1)){
                Toast.makeText(AddPaymentMethod.this,"You have provided an expired card",Toast.LENGTH_LONG).show();
                return  false;
            }
        }

        return true;
    }

    private void volleyFetchAuthorization(){
        mPDialog.setCancelable(false);
        mPDialog.setMessage("Configuring secure channel...");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.GET_CARD_TOKEN,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            mPDialog.dismiss();;
                        if(response.contains("\n"))response=response.replaceAll("\n","");
                        PaymentTokenResponseTemplate tokenObj = new PaymentTokenResponseTemplate();
                        tokenObj = (PaymentTokenResponseTemplate)new Gson().fromJson(response,PaymentTokenResponseTemplate.class);

                        if(!tokenObj.getResponse_code().equalsIgnoreCase("")){
                            authorisationToken=tokenObj.getToken();
                        }else{
                            Toast.makeText(AddPaymentMethod.this, "Could not complete your request",Toast.LENGTH_LONG).show();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();;
                Toast.makeText(AddPaymentMethod.this,
                        "could not fetch your 1 book, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();

                String userID = PreferenceHandler.readString(AddPaymentMethod.this, PreferenceHandler.USER_ID,"");

                params.put("user_id",userID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }



    private void volleyPostCardId(final String cardID){

        mPDialog.setCancelable(false);
        mPDialog.setMessage("Securely saving your card...");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.UPDATE_CARD_ID,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       mPDialog.dismiss();;
                        FbAuthTemplate authResponse = new FbAuthTemplate();
                        authResponse = (FbAuthTemplate)new Gson().fromJson(response,FbAuthTemplate.class);
                        if(authResponse.getResponse_code().equalsIgnoreCase("1")){
                                Toast.makeText(AddPaymentMethod.this,
                                        "Your card was successfully authenticated",Toast.LENGTH_LONG).show();
                            if(RippleittAppInstance.getInstance().getAddCardMode()==0){
                                Intent intent = new Intent(AddPaymentMethod.this,
                                        ActivityWelcome.class);
                                startActivity(intent);
                                finish();
                                // TastyToast.makeText(getApplicationContext(), "SignUp Successfuly", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                finish();
                            }else{
                                finish();
                            }


                        }else{
                            if(RippleittAppInstance.getInstance().getAddCardMode()==0){
                                Intent intent = new Intent(AddPaymentMethod.this,
                                        ActivityWelcome.class);
                                startActivity(intent);
                                finish();
                                // TastyToast.makeText(getApplicationContext(), "SignUp Successfuly", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                finish();
                            }else{
                                finish();
                            }
                        }



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();;
                Toast.makeText(AddPaymentMethod.this,
                        "could not fetch your 1 book, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                String userID = PreferenceHandler.readString(AddPaymentMethod.this,
                        PreferenceHandler.USER_ID,"");
                params.put("user_id",userID);
                params.put("card_id",cardID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");


    }

    private class PostCardAsync extends AsyncTask<Void, Void, String> {

        @Override
        public void onPreExecute(){
            mPDialog.setMessage("Verifying your card...");
            mPDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {

            try{
                ProviderInstaller.installIfNeeded(AddPaymentMethod.this);
                PromisePay promisePay = PromisePay.getInstance();
                promisePay.initialize("prelive", "OPTIONAL_PUBLIC_KEY");
                PPCard card = new PPCard(medittxtCardNumber.getText().toString().trim(),
                        medittxtCardHolderName.getText().toString().trim(),
                        mSpnrMonth.getSelectedItem().toString(),
                        mSpnrYear.getSelectedItem().toString(),
                        medittxtCvv.getText().toString().trim());
                promisePay.createCardAccount(authorisationToken, card, new PromisePay.OnPromiseRequestListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {

                            CardAccountsTemplate cardTemplate = new CardAccountsTemplate();
                            cardTemplate=(CardAccountsTemplate)new Gson().fromJson(response.toString(),CardAccountsTemplate.class);
                            String _card_id=response.getJSONObject("card_accounts").getString("id");
                            card_id=_card_id;
                            //volleyPostCardId(card_id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        //Toast.makeText(AddPaymentMethod.this, e.getMessage(),Toast.LENGTH_LONG).show();
                        //return null;
                    }
                });

            }catch (Exception e){

            }



            return card_id;
        }


        @Override
        public void onPostExecute(String card_id___){
            mPDialog.dismiss();
            if(!card_id.equalsIgnoreCase("")){
                volleyPostCardId(card_id);
            }else{
                Toast.makeText(AddPaymentMethod.this, "Could not process your card",Toast.LENGTH_LONG).show();
            }

        }
    }




    public static class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final char space = ' ';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }
    }


}
