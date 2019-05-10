package com.rippleitt.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rippleitt.R;


public class TermsConditionsActivity extends AppCompatActivity {


    ImageView btnBack;
    TextView textView;
    TextView txtvwTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        btnBack= findViewById(R.id.imgBackProductDetail);
        textView= findViewById(R.id.textView);
        txtvwTitle= findViewById(R.id.txtvwTitle);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras= getIntent().getExtras();
        if (extras!=null){
            if (!extras.getBoolean("isTerms",true)){
                textView.setText(R.string.strPrivacyPolicy);
                txtvwTitle.setText("Privacy Policy");
            }else if (extras.getBoolean("isFAQ",false)){
                textView.setText(R.string.strDummy);
                txtvwTitle.setText("FAQs");
            }else
                textView.setText(R.string.strTermsCondition);
        }
    }

}
