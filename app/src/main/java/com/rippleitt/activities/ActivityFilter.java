package com.rippleitt.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.webservices.FetchCategoryApi;
import com.rippleitt.webservices.SearchApi;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ActivityFilter extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout mrelFilter_back;
    private Button mbtnApplyFilter;
    private AVLoadingIndicatorView maviLoaderHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
      //  maviLoaderHome = (AVLoadingIndicatorView) findViewById(R.id.aviLoaderHome);
        init();
        work();
    }

    public void init(){
        new FetchCategoryApi().fetchCategoryApi(this);
        mrelFilter_back=(RelativeLayout)findViewById(R.id.relFilter_back);
        mbtnApplyFilter=(Button)findViewById(R.id.btnApplyFilter);
        mrelFilter_back.setOnClickListener(this);
        mbtnApplyFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==mrelFilter_back){
            finish();
        }
       /* if (view==mbtnApplyFilter){


            new SearchApi().searchApi(this, "1","0", "", "13474949", "14880621", maviLoaderHome);
        }*/
    }

   public void work() {
   }

   /*    public void startAnim() {
        maviLoaderHome.show();
        // or avi.smoothToShow();
    }

    public void stopAnim() {
        maviLoaderHome.hide();
        // or avi.smoothToHide();
    }*/

}
