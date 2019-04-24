package com.rippleitt.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rippleitt.R;
import com.rippleitt.adapters.CustomViewPagerAdapter;
import com.rippleitt.adapters.FullScreenPagerAdapter;
import com.rippleitt.controller.RippleittAppInstance;
import com.robohorse.pagerbullet.PagerBullet;
import com.squareup.picasso.Picasso;

/**
 * Created by pc on password/15/2018.
 */

public class ActivityFullScreenImageView extends AppCompatActivity implements View.OnClickListener {


    private ImageView mImgVwCrossHair;
    private PagerBullet mPagerBullet;
    private FullScreenPagerAdapter fullScreenAdapter;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_full_screen_image_view);
        initUI();
        setupPager();
        //loadImgPath();
    }

    @Override
    public void onClick(View view) {
            if(view==mImgVwCrossHair){
                finish();
            }
    }

    private void initUI(){
        mImgVwCrossHair=(ImageView)findViewById(R.id.imgvwCrossHair);

        mPagerBullet=(PagerBullet)findViewById(R.id.bltpgerListingImage);
        mPagerBullet.setIndicatorTintColorScheme(Color.WHITE, Color.parseColor("#289b98"));
        mImgVwCrossHair.setOnClickListener(this);
    }


    private void setupPager(){
        fullScreenAdapter = new FullScreenPagerAdapter(this,RippleittAppInstance.getInstance().getFullScreenImageMode());
        mPagerBullet.setAdapter(fullScreenAdapter);
        mPagerBullet.setCurrentItem(RippleittAppInstance.getInstance().getCURRENT_SELECTED_IMAGE_POSITION());
    }


    @Override
    public void onBackPressed(){
        finish();
    }
}
