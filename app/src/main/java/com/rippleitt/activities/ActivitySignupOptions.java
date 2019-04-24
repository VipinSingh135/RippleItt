package com.rippleitt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.rippleitt.R;

public class ActivitySignupOptions extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relativeIndividual,relativeBusiness;
    private ImageButton btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_options);

        relativeIndividual= findViewById(R.id.relativeIndividual);
        relativeBusiness= findViewById(R.id.relativeBusiness);
        btnBack= findViewById(R.id.btnBack);

        relativeIndividual.setOnClickListener(this);
        relativeBusiness.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v== relativeBusiness){
            Intent intent = new Intent(ActivitySignupOptions.this, SignUpActivity.class);
            intent.putExtra("userType",2);
            startActivity(intent);
        }else if (v== relativeIndividual){
            Intent intent = new Intent(ActivitySignupOptions.this, SignUpActivity.class);
            intent.putExtra("userType",1);
            startActivity(intent);
        }else {
             finish();
        }
    }
}
