package com.rippleitt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rippleitt.R;
import com.sdsmdg.tastytoast.TastyToast;

public class SellerReview extends AppCompatActivity implements View.OnClickListener {


    Button btnSubmitReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_review);
        init();
    }

    public  void init(){
        btnSubmitReview=(Button)findViewById(R.id.btnSubmitReview);
        btnSubmitReview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==btnSubmitReview) {
            TastyToast.makeText(getApplicationContext(), "Thanks For Providing Your Valuable Feedback", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
