package com.rippleitt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.PojoFCMPushDataResp;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private ImageView mimgvwLogoSplash;
    SharedPreferences sharedPreferences;
    boolean isMessage=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
//            Bundle[{google.delivered_priority=high, google.sent_time=1543990007543, google.ttl=2419200, google.original_priority=high, _fbSourceApplicationHasBeenSet=true, body=hdhdsgsg, from=430907793177, resp={"is_deleted":0,"user_id":"0da97f2af242033f20e7ee900be1087b","user_image":"..\/..\/listing_pics\/1538662558.33.","device_token":"fHepASOqfRQ:APA91bGItvAt1lDzLB9qr4i1eeFHaIzitFXPCV4zIl_JdCQRIaIdv36xkg4Fi5cus56FcgFOEiwAQXNwVa1NpflMYD-qKEKnQYqFU76RlBDYuOqSddqaqyLaAUVAjix_4AzkATWOvYjK","icon":"","id":"","message":"hdhdsgsg","type":"chat","device":"Android","status":1,"username":"John Paul"}, title=John Paul, google.message_id=0:1543990007560995%69139c4c69139c4c, collapse_key=com.rippleitt}]

            Log.d("Splach Activity : ", bundle.toString());
            Log.d("msg", "onMessageReceived: " + bundle.get("resp"));
            if (bundle.getString("resp")!=null) {
                PojoFCMPushDataResp pojo = new Gson().fromJson(bundle.getString("resp"), PojoFCMPushDataResp.class);
                if (pojo.getType().equals("chat") ){
                    isMessage=true;
                }
            }
        }
        init();
    }

    public void init() {
        mimgvwLogoSplash = (ImageView) findViewById(R.id.imgvwLogoSplash);
        Animation an2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
       // mimgvwLogoSplash.startAnimation(an2);


        //=====Handling Time============
        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!sharedPreferences.getString("userTokenId", "").equals("")) {
                    RippleittAppInstance.TOKEN_ID=sharedPreferences.getString("userTokenId","");

                    Log.e("USER_ID", sharedPreferences.getString("userTokenId", "").toString());
                    Intent intent = new Intent(SplashActivity.this, ActivityHome.class);
                    if (isMessage){
                        intent.putExtra("isMessage",true);
                    }
                    startActivity(intent);
//
//                    Intent intent1;
//                    intent1 = new Intent(RippleittAppInstance.broadcastMessage);
//                    intent1.putExtra("isMessage",true);
//                    LocalBroadcastManager.getInstance(SplashActivity.this).sendBroadcast(intent1);
//
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

    }

}
