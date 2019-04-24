package com.rippleitt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.callback.WishlistSelectCallback;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.MyListingDetailsPayload;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.DeleteListingProductApi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 4/10/2018.
 */

public class ActivitySubmitReview  extends AppCompatActivity
        implements View.OnClickListener, WishlistSelectCallback {

    private int rating=1;
    private ImageView mImgVwRatingStarOne, mImgVwRatingStarTwo,
                            mImgVwRatingStarThree,
                            mImgVwRatingStarFour,
                            mImgVwRatingStarFive;
    private EditText mEdtxtReviewMessage;
    private Button mBtnSubmitReview;
    private ProgressDialog mProgressDialog;
    private RelativeLayout mRelLytBack;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_sumbit_review);
        initUI();
    }


    @Override
    public void onClick(View view) {
        if(view==mRelLytBack){
            finish();
        }
        if(view==mImgVwRatingStarOne){
            mImgVwRatingStarOne.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarTwo.setImageResource(R.drawable.ic_star_grey_24dp);
            mImgVwRatingStarThree.setImageResource(R.drawable.ic_star_grey_24dp);
            mImgVwRatingStarFour.setImageResource(R.drawable.ic_star_grey_24dp);
            mImgVwRatingStarFive.setImageResource(R.drawable.ic_star_grey_24dp);
            rating=1;
        }if(view==mImgVwRatingStarTwo){
            mImgVwRatingStarOne.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarTwo.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarThree.setImageResource(R.drawable.ic_star_grey_24dp);
            mImgVwRatingStarFour.setImageResource(R.drawable.ic_star_grey_24dp);
            mImgVwRatingStarFive.setImageResource(R.drawable.ic_star_grey_24dp);
            rating=2;
        }if(view==mImgVwRatingStarThree){
            mImgVwRatingStarOne.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarTwo.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarThree.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarFour.setImageResource(R.drawable.ic_star_grey_24dp);
            mImgVwRatingStarFive.setImageResource(R.drawable.ic_star_grey_24dp);
            rating=3;
        }if(view==mImgVwRatingStarFour){
            mImgVwRatingStarOne.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarTwo.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarThree.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarFour.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarFive.setImageResource(R.drawable.ic_star_grey_24dp);
            rating=4;
        }if(view==mImgVwRatingStarFive){
            mImgVwRatingStarOne.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarTwo.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarThree.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarFour.setImageResource(R.drawable.ic_star_green_24dp);
            mImgVwRatingStarFive.setImageResource(R.drawable.ic_star_green_24dp);
            rating=5;
        } if(view==mBtnSubmitReview){
            if(mEdtxtReviewMessage.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(ActivitySubmitReview.this, "Please provide review",
                        Toast.LENGTH_SHORT).show();
                return;
            }if(rating==1){
                confirmZeroRatingReview();
            }else{
                volleySubmitReview();
            }

        }
    }

    @Override
    public void onWishListitemSelected(String listing_id) {

    }


    private void confirmZeroRatingReview(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySubmitReview.this);
        builder.setMessage("You are about to provide a one star rating that will affect the rating of this seller?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      //  Toast.makeText(context,"Deleting listing please wait",Toast.LENGTH_LONG).show();
                        volleySubmitReview();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void initUI(){
        mProgressDialog= new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mImgVwRatingStarOne=(ImageView)findViewById(R.id.imgvwRatingstarOne);
        mImgVwRatingStarTwo=(ImageView)findViewById(R.id.imgvwRatingstarTwo);
        mImgVwRatingStarThree=(ImageView)findViewById(R.id.imgvwRatingstarThree);
        mImgVwRatingStarFour=(ImageView)findViewById(R.id.imgvwRatingstarFour);
        mImgVwRatingStarFive=(ImageView)findViewById(R.id.imgvwRatingstarFive);
        mImgVwRatingStarOne.setOnClickListener(this);
        mImgVwRatingStarTwo.setOnClickListener(this);
        mImgVwRatingStarThree.setOnClickListener(this);
        mImgVwRatingStarFour.setOnClickListener(this);
        mImgVwRatingStarFive.setOnClickListener(this);
        mEdtxtReviewMessage=(EditText)findViewById(R.id.edittxtDescReview);
        mBtnSubmitReview=(Button)findViewById(R.id.btnSubmitReview);
        mRelLytBack=(RelativeLayout)findViewById(R.id.relProductDetailsback);
        mRelLytBack.setOnClickListener(this);
        mBtnSubmitReview.setOnClickListener(this);
    }


    private void volleySubmitReview(){
        mProgressDialog.setMessage("Submitting your review...");
        mProgressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,RippleittAppInstance.SUBMIT_REVIEW ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        MyListingDetailsPayload payload =
                                (MyListingDetailsPayload)gson.fromJson(response,MyListingDetailsPayload.class);
                        if(payload.getResponse_code().equalsIgnoreCase("1")){
                            // show single button alert here...

                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySubmitReview.this);
                            builder.setMessage("Your review was submitted successfully.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ActivitySubmitReview.this,
                        "could not submit your review...",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivitySubmitReview.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("order_id",RippleittAppInstance
                        .getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_id());
                params.put("message",mEdtxtReviewMessage.getText().toString().trim());
                params.put("rating",Integer.toString(rating));

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }
}
