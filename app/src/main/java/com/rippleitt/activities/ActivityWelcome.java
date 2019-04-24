package com.rippleitt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rippleitt.R;

/**
 * Created by pc on password/22/2018.
 */

public class ActivityWelcome extends AppCompatActivity
implements View.OnClickListener{

    private Button mBtnontinue;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_welcome);
        initUI();
    }


    private void initUI(){
        mBtnontinue=(Button)findViewById(R.id.btnContinue);
        mBtnontinue.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==mBtnontinue){
            startActivity(new Intent(ActivityWelcome.this,
                    SplashActivity.class));
            finish();
        }
    }
}
