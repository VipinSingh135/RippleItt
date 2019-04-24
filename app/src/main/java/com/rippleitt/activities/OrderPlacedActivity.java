package com.rippleitt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rippleitt.R;

public class OrderPlacedActivity extends AppCompatActivity implements View.OnClickListener {

   Button mbtnGiveRatingAndReviewSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        init();
    }

    public void init(){
        mbtnGiveRatingAndReviewSeller=(Button)findViewById(R.id.btnGiveRatingAndReviewSeller);
        mbtnGiveRatingAndReviewSeller.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==mbtnGiveRatingAndReviewSeller){
            startActivity(new Intent(OrderPlacedActivity.this,SellerReview.class));
        }
    }
}
