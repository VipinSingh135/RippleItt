package com.rippleitt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by manishautomatic on 11/06/18.
 */

public class ActivityPostListingFaq extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImgVWBack;
    private EditText mEdtxtQuestionBody;
    private Button mBtnPostQuestion;
    private ProgressDialog mPDialog;
    private TextView mtxtVwTitle;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_post_listing_faq);
        initUI();
    }


    private void initUI(){
        mtxtVwTitle=(TextView)findViewById(R.id.txtvwTitle);
        mImgVWBack=(ImageView)findViewById(R.id.imgBackFAQ);
        mImgVWBack.setOnClickListener(this);
        mEdtxtQuestionBody=(EditText)findViewById(R.id.edittxtDescReview);
        mEdtxtQuestionBody.setOnClickListener(this);
        mBtnPostQuestion=(Button)findViewById(R.id.btnSubmitFaq);
        mBtnPostQuestion.setOnClickListener(this);
        mPDialog=new ProgressDialog(this);
        mPDialog.setCancelable(false);
        if(RippleittAppInstance.getInstance().getListingFaqMode()==1){// user is viewing self listing
            mtxtVwTitle.setText("Add Comment");
            mEdtxtQuestionBody.setHint("Write your comment here...");
        }else{
            mtxtVwTitle.setText("Add Comment");
            mEdtxtQuestionBody.setHint("Write your comment here...");
        }
    }


    @Override
    public void onClick(View view) {
        if(mImgVWBack==view){
            finish();
        }if(mBtnPostQuestion==view){
            if(validate()){
                volleyPostFaq();
            }
        }
    }


    private boolean validate(){
        if(mEdtxtQuestionBody.getText().toString().trim().equalsIgnoreCase("")) {
            mEdtxtQuestionBody.setError("Please provide your input");
            return false;
        }
        if(mEdtxtQuestionBody.getText().toString().replaceAll("\n","").matches(".*\\d+.*")){
            com.rippleitt.utils.CommonUtils.showSingleButtonAlert(ActivityPostListingFaq.this,
                    "Comment must not contain numeric values");
            return false;
        }
        return true;
    }



    private void volleyPostFaq(){
        mPDialog.setMessage("Submitting your input");
        mPDialog.show();

        String requestURL="";
        if(RippleittAppInstance.getInstance().getListingFaqMode()==0){
            requestURL=RippleittAppInstance.getInstance().getPostListingFaq();
        }else{
            requestURL=RippleittAppInstance.getInstance().getPostFaqResponse();
        }

        StringRequest sr = new StringRequest(Request.Method.POST, requestURL ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate)gson.fromJson(response,CategoryListTemplate.class);
                        if(response_.getResponse_code()==1){
                            if(RippleittAppInstance.getInstance().getListingFaqMode()==1){

                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPostListingFaq.this);
                                builder.setMessage("Your comment was submitted successfully")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }else{

                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPostListingFaq.this);
                                builder.setMessage("Your comment was submitted successfully")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }


                        }else{
                            Toast.makeText(getApplicationContext(), "Could not submit your response", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                Toast.makeText(ActivityPostListingFaq.this,"could not submit your response, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityPostListingFaq.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id",RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());
                if(RippleittAppInstance.getInstance().getListingFaqMode()==1){
                    params.put("answer_body",mEdtxtQuestionBody.getText().toString().trim());
                    params.put("question_id",RippleittAppInstance.getInstance().getCurrentQuestionId());
                }else{
                    params.put("question_body",mEdtxtQuestionBody.getText().toString().trim());
                }

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }
}
