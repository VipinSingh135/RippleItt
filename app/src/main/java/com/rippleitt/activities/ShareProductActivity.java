package com.rippleitt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ContactSharingTemplate;

public class ShareProductActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView mimgVwContactIcon;
    TextView mtxtVwAddContactsShareProduct;
    RelativeLayout mrelShareProduct_back;
    TextView mTxtVwAddContact;
    EditText mEdtstShard;
    Button mBtnShareProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_product);
        init();
    }
    public void init(){
        mimgVwContactIcon=(ImageView)findViewById(R.id.imgVwContactIcon);
        mtxtVwAddContactsShareProduct=(TextView) findViewById(R.id.txtVwAddContactsShareProduct);
        mrelShareProduct_back=(RelativeLayout) findViewById(R.id.relShareProduct_back);
        mTxtVwAddContact=(TextView)findViewById(R.id.txtVwAddContactsShareProduct);
        mEdtstShard=(EditText)findViewById(R.id.edittxtSharedUserName);
        mBtnShareProduct= (Button)findViewById(R.id.btnSendShareProduct);
        mBtnShareProduct.setOnClickListener(this);
        mTxtVwAddContact.setOnClickListener(this);
        mimgVwContactIcon.setOnClickListener(this);
        mtxtVwAddContactsShareProduct.setOnClickListener(this);
        mrelShareProduct_back.setOnClickListener(this);
    }


    @Override
    public void onResume(){
        super.onResume();

        if(RippleittAppInstance.getInstance().getUserCurrentAddressBook()!=null){
            populateEditField();
        }

    }



    private void populateEditField(){
        String sharedContactNames="";

        for(ContactSharingTemplate contact: RippleittAppInstance.getInstance().getUserCurrentAddressBook()){
            if(contact.getShare()==1){
                sharedContactNames=
                        sharedContactNames
                        +" "
                        +contact.getName()
                                +" "
                                +contact.getLastname()+",";
            }
        }
        sharedContactNames = sharedContactNames.replaceAll(",$", "");
        mEdtstShard.setText(sharedContactNames);
    }

    @Override
    public void onClick(View view) {
        if (view==mimgVwContactIcon){
           startActivity(new Intent(ShareProductActivity.this,AddressBookActivity.class));
        }
        if (view==mtxtVwAddContactsShareProduct){

        }
        if (view==mrelShareProduct_back){
            finish();
        }if(view==mTxtVwAddContact){
            startActivity(new Intent(ShareProductActivity.this,ActivityAddContacts.class));
        }if(view==mBtnShareProduct){

            resetShareFlags();


            Toast.makeText(ShareProductActivity.this, "Listing has been successfully referred",Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void resetShareFlags(){
        for(ContactSharingTemplate contact : RippleittAppInstance
                                            .getInstance()
                                            .getUserCurrentAddressBook()){
            contact.setShare(0);
        }
    }
}
