package com.rippleitt.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.webservices.SearchApi;
import com.rippleitt.webservices.SearchContactApi;
import com.sdsmdg.tastytoast.TastyToast;
import com.wang.avi.AVLoadingIndicatorView;

public class ActivityAddContacts extends AppCompatActivity implements View.OnClickListener {


    RelativeLayout mrelAddContact_back;
    EditText medtxtAddContact;
    AVLoadingIndicatorView mLoaderContactSearch;
    TextView mTxtVwCponMatches;
    ImageView mImgVwClearKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        init();
    }

    public void init(){
        mImgVwClearKeyword=(ImageView)findViewById(R.id.imgVwClearSearch);
        mImgVwClearKeyword.setOnClickListener(this);
        mTxtVwCponMatches=(TextView)findViewById(R.id.txtvwNoResults);
        mTxtVwCponMatches.setVisibility(View.GONE);
        mrelAddContact_back=(RelativeLayout)findViewById(R.id.relAddContact_back);
        medtxtAddContact=(EditText)findViewById(R.id.edtxtAddContact);
        mLoaderContactSearch=(AVLoadingIndicatorView)findViewById(R.id.aviLoaderContactSearch);
        mLoaderContactSearch.hide();
        mrelAddContact_back.setOnClickListener(this);


        ///===================search=====================
        medtxtAddContact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //   TastyToast.makeText(getActivity(), medtxtQuickSearch.getText().toString(), TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    if (v.getText().toString().trim().equalsIgnoreCase("")) {
                        TastyToast.makeText(ActivityAddContacts.this, "Enter text to search", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    } else {
                        InputMethodManager imm = (InputMethodManager)ActivityAddContacts.this
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(medtxtAddContact.getWindowToken(), 0);

                        mTxtVwCponMatches.setVisibility(View.GONE);
                        new SearchContactApi().searchContactApi(ActivityAddContacts.this,
                                medtxtAddContact.getText().toString(),mLoaderContactSearch);
                        }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view==mrelAddContact_back){
            finish();
        }
        if(view==mImgVwClearKeyword){
            medtxtAddContact.setText("");
            ListView listVwContacts=(ListView)findViewById(R.id.listVwContacts);
            listVwContacts.setAdapter(null);

        }

    }
}
