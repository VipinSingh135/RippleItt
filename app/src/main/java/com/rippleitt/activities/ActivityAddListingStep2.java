package com.rippleitt.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.HorizontalReyclerViewAdapter;
import com.rippleitt.adapters.PostalCodesAdapter;
import com.rippleitt.callback.ItemClickListener;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.commonUtilities.UpdateListingAsync;
import com.rippleitt.controller.CustomTextWatcher;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.ListingSyncProcessCallback;
import com.rippleitt.modals.ListingSyncResponseTemplate;
import com.rippleitt.modals.ListingSyncTemplate;
import com.rippleitt.modals.PostalCodeApiResponse;
import com.rippleitt.modals.PostalCodeModal;
import com.rippleitt.utils.CommonUtils;
import com.robertlevonyan.views.chip.OnCloseClickListener;
import com.wang.avi.AVLoadingIndicatorView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class ActivityAddListingStep2 extends AppCompatActivity
        implements View.OnClickListener, ListingSyncProcessCallback, ItemClickListener {


    private EditText medittxtAddProductPrice;
    private RelativeLayout mrelAddProduct_back;

    private Button mbtnAddProdSaveDraft;
    private Button btnAddVoucher;
    private Button mbtnAddPRoductPublish;
    private final int MODE_DRAFT = 1;
    private final int MODE_PUBLISH = 0;
    //    private RadioButton mRdBtnBuyNow, mRdBtnMakeOffer, mRdBtnBoth, mRdBtnPaymentModeCash, mRdBtnPaymentModeRipple;
//    private RelativeLayout linlytCash, linlytCreditCard, linlytBuyNow, linlytBoth, linlytMakeOffer;
    private LinearLayout linlytCash, linlytCreditCard;
//    , linlytBuyNow,  linlytMakeOffer;
    private CheckBox chkBuyNow,chkMakeOffer;
//    private boolean isBuy=false,isOffer=false;

    private TextView tvCash, tvCreditCard, tvBuyNow,  tvMakeOffer;
    private ImageView imgPickup, imgShipping, imgCash,  imgCreditCard;

    private int paymentMode = 1;

    private Spinner mSpnrnits;
    private EditText mEdtxtQty;
    private CheckBox mChkBxTC;
    private TextView mTxtVwTc;
    private EditText mEdtxtAddress;
    private final int PLACE_PICKER_REQUEST = 12;
    public static final int PICK_IMAGE = 1;
    private static int RESULT_LOAD_IMG = 1;
    private boolean spnr_cat_init = false;
    private HorizontalReyclerViewAdapter horizontalAdapter;
    private final int ACTION_READ_EXTERNAL_STORAGE = 13;

    private final int ACTION_ACCESS_LOCATION_ = 14;
    private ProgressDialog mProgressDialog;
    private Compressor mCompressor;

    //    private RadioButton mRdBtnServicePickUp, mRdBtnServiceZIP;
    private LinearLayout linlytPickup, linlytShipping;
    private TextView tvPickup, tvShipping;
    private boolean isPickup = true;
    private EditText mEdtxtZipCode;
    private ImageView mImgVwAddZipCode;
    private HorizontalScrollView mHrscrlVwZipContainer;
    private LinearLayout mLnLtZipContainer;
    private LinearLayout linearShippingMethods;
    private ProgressDialog mPDialog;
    private ScrollView mScrlVwWrapper;
    private ImageButton btnDeleteVoucher;
    private LinearLayout linearVoucher;
    private TextView tvVoucherText;
    ListingSyncTemplate listingTemplate = new ListingSyncTemplate();
//    private EditText mEdtxtRewardPoint;
    String voucher_id = null;
    private AVLoadingIndicatorView mLoader;


    private List<PostalCodeModal> placesList ;
    PostalCodesAdapter adapter;
    RecyclerView recyclerPostcodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing_step2);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mCompressor = new Compressor(ActivityAddListingStep2.this);
        mCompressor.setQuality(25);
        RippleittAppInstance.getInstance().getListingImages().clear();
        init();
        mPDialog = new ProgressDialog(ActivityAddListingStep2.this);
        RippleittAppInstance.getInstance().getServiceZIPCodes().clear();

        if (RippleittAppInstance.getInstance().getCURRENT_ADDED_LISTING_OBJECT() != null) {

            if (RippleittAppInstance.getInstance().getCURRENT_ADDED_LISTING_OBJECT().getListing_type().equalsIgnoreCase("2")){
                linearShippingMethods.setVisibility(View.GONE);
                mEdtxtQty.setVisibility(View.GONE);
            }else {
                mEdtxtQty.setVisibility(View.VISIBLE);
                linearShippingMethods.setVisibility(View.VISIBLE);
            }

            listingTemplate = RippleittAppInstance.getInstance().getCURRENT_ADDED_LISTING_OBJECT();
            if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT() != null)
                preloadListingData();
//            if(listingTemplate.getIs_live().equalsIgnoreCase("1")){
//                mbtnAddProdSaveDraft.setVisibility(View.GONE);
//                mEdtxtRewardPoint.setEnabled(false);
//                medittxtAddProductPrice.setEnabled(false);
//            }

        } else if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT() != null) {
            if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getListing_type().equalsIgnoreCase("2")){
                linearShippingMethods.setVisibility(View.GONE);
                mEdtxtQty.setVisibility(View.GONE);
            }else {
                linearShippingMethods.setVisibility(View.VISIBLE);
                mEdtxtQty.setVisibility(View.VISIBLE);
            }
            preloadListingData();
        }


        placesList=new ArrayList<>();
        adapter = new PostalCodesAdapter(placesList, ActivityAddListingStep2.this);
//        edPostalCode.setThreshold(1); //will start working from first character
        recyclerPostcodes.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerPostcodes.setAdapter(adapter);
        recyclerPostcodes.setVisibility(View.GONE);

//        edPostalCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){

        mEdtxtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEdtxtAddress.hasFocus()) {
                    recyclerPostcodes.setVisibility(View.VISIBLE);
                    postalCodeApi(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mProgressDialog.dismiss();
    }

    public void init() {
        mScrlVwWrapper = (ScrollView) findViewById(R.id.scrlvwWrapper);
//        mRdBtnServiceZIP = (RadioButton) findViewById(R.id.rdBtnShippingServiceAreas);
//        mRdBtnServicePickUp = (RadioButton) findViewById(R.id.rdBtnShippingPickup);
//        mRdBtnServiceZIP.setOnClickListener(this);
//        mRdBtnServicePickUp.setOnClickListener(this);

        linlytShipping = findViewById(R.id.linlytShipping);
        linlytPickup = findViewById(R.id.linlytPickup);
        tvShipping = findViewById(R.id.tvShipping);
        tvPickup = findViewById(R.id.tvPickup);

        mEdtxtZipCode = (EditText) findViewById(R.id.edtxtZipCode);
        mImgVwAddZipCode = (ImageView) findViewById(R.id.imgvwAddZipCode);
        mImgVwAddZipCode.setOnClickListener(this);
        mHrscrlVwZipContainer = (HorizontalScrollView) findViewById(R.id.hrscrlZipCodeContailer);
//        mEdtxtRewardPoint = (EditText) findViewById(R.id.edtxtReferralAmount);
        linearShippingMethods = findViewById(R.id.linearShippingMethods);
//        linearBuyingOptions = findViewById(R.id.linearBuyingOptions);
        mLnLtZipContainer = (LinearLayout) findViewById(R.id.linlytZipCodeContainer);
        recyclerPostcodes= findViewById(R.id.recyclerPostcodes);
        mChkBxTC = (CheckBox) findViewById(R.id.chkbxTerms);
        chkBuyNow = (CheckBox) findViewById(R.id.chkBuyNow);
        chkMakeOffer = (CheckBox) findViewById(R.id.chkMakeOffer);
        mTxtVwTc = (TextView) findViewById(R.id.txtvwTCLauncher);
        mTxtVwTc.setText(Html.fromHtml("<u>Rippleitt Terms & Conditions</u>"));
        mTxtVwTc.setOnClickListener(this);
//        mRdBtnPaymentModeCash = (RadioButton) findViewById(R.id.rdBtnPaymentModecCash);
//        mRdBtnPaymentModeRipple = (RadioButton) findViewById(R.id.rdBtnPaymentModeRipple);
//
//        mRdBtnMakeOffer = (RadioButton) findViewById(R.id.rdBtnMakeOffer);
//        mRdBtnBuyNow = (RadioButton) findViewById(R.id.rdBtnBuyNow);
//        mRdBtnBoth = (RadioButton) findViewById(R.id.rdBtnBoth);

        linlytCash = findViewById(R.id.linlytCash);
        linlytCreditCard = findViewById(R.id.linlytCreditCard);
//        linlytBuyNow = findViewById(R.id.linlytBuyNow);
//        linlytBoth = findViewById(R.id.linlytBoth);
//        linlytMakeOffer = findViewById(R.id.linlytMakeOffer);

        imgCreditCard = findViewById(R.id.imgCreditCard);
        imgCash = findViewById(R.id.imgCash);
        imgPickup = findViewById(R.id.imgPickup);
        imgShipping = findViewById(R.id.imgShipping);
        tvCash = findViewById(R.id.tvCash);
        tvCreditCard = findViewById(R.id.tvCreditCard);
//        tvBuyNow = findViewById(R.id.tvBuyNow);
//        tvBoth = findViewById(R.id.tvBoth);
//        tvMakeOffer = findViewById(R.id.tvMakeOffer);

        mSpnrnits = (Spinner) findViewById(R.id.spnrUnits);
        mEdtxtQty = (EditText) findViewById(R.id.edittxtAddProductQty);
        medittxtAddProductPrice = (EditText) findViewById(R.id.edittxtAddProductPrice);
        mrelAddProduct_back = (RelativeLayout) findViewById(R.id.relAddProduct_back);
        mEdtxtAddress = (EditText) findViewById(R.id.edtxtAddress);
//        mEdtxtAddress.setFocusable(false);
//        mEdtxtAddress.setClickable(true);
//        mEdtxtAddress.setOnClickListener(this);
        btnAddVoucher = (Button) findViewById(R.id.btnAddVoucher);
        mbtnAddProdSaveDraft = (Button) findViewById(R.id.btnAddProdSaveDraft);
        mbtnAddPRoductPublish = (Button) findViewById(R.id.btnAddPRoductPublish);
        tvVoucherText = findViewById(R.id.tvVoucherText);
        btnDeleteVoucher = findViewById(R.id.btnDeleteVoucher);
        linearVoucher = findViewById(R.id.linearVoucher);

        mLoader = findViewById(R.id.mLoader);

        mrelAddProduct_back.setOnClickListener(this);
        mbtnAddPRoductPublish.setOnClickListener(this);
        mbtnAddProdSaveDraft.setOnClickListener(this);

        linlytCash.setOnClickListener(this);
        linlytCreditCard.setOnClickListener(this);
//        linlytBuyNow.setOnClickListener(this);
//        linlytBoth.setOnClickListener(this);
//        linlytMakeOffer.setOnClickListener(this);
        linlytShipping.setOnClickListener(this);
        linlytPickup.setOnClickListener(this);
        mLoader.setVisibility(View.GONE);

        medittxtAddProductPrice.addTextChangedListener(new CustomTextWatcher(
                medittxtAddProductPrice));
        mChkBxTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mChkBxTC.isChecked()) mChkBxTC.setError(null);
            }
        });
        mHrscrlVwZipContainer.setVisibility(View.GONE);
        mEdtxtZipCode.setVisibility(View.GONE);
        mImgVwAddZipCode.setVisibility(View.GONE);


//        mRdBtnBoth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mRdBtnBuyNow.setChecked(false);
//                    mRdBtnMakeOffer.setChecked(false);
//                }
//            }
//        });
//        mRdBtnBuyNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mRdBtnBoth.setChecked(false);
//                    mRdBtnMakeOffer.setChecked(false);
//                }
//            }
//        });
//        mRdBtnMakeOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mRdBtnBuyNow.setChecked(false);
//                    mRdBtnBoth.setChecked(false);
//                }
//            }
//        });

        if (PreferenceHandler.readString(ActivityAddListingStep2.this, PreferenceHandler.USER_TYPE, "1")
                .equalsIgnoreCase("2")) {
            btnAddVoucher.setVisibility(View.VISIBLE);
        } else {
            btnAddVoucher.setVisibility(View.GONE);
        }

        btnAddVoucher.setOnClickListener(this);
        btnDeleteVoucher.setOnClickListener(this);
        linearVoucher.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {

//        if (view == mRdBtnServicePickUp) {
//            mHrscrlVwZipContainer.setVisibility(View.GONE);
//            mEdtxtZipCode.setVisibility(View.GONE);
//            mImgVwAddZipCode.setVisibility(View.GONE);
//        }
//        if (view == mRdBtnServiceZIP) {
//            mHrscrlVwZipContainer.setVisibility(View.VISIBLE);
//            mEdtxtZipCode.setVisibility(View.VISIBLE);
//            mImgVwAddZipCode.setVisibility(View.VISIBLE);
//        }
        if (view == linlytPickup) {
//            mHrscrlVwZipContainer.setVisibility(View.GONE);
//            mEdtxtZipCode.setVisibility(View.GONE);
//            mImgVwAddZipCode.setVisibility(View.GONE);

            linlytPickup.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytShipping.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgPickup.setImageResource(R.drawable.white_cicle);
            imgShipping.setImageResource(R.drawable.light_blue_circle);
            isPickup = true;
            tvPickup.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvShipping.setTextColor(getResources().getColor(R.color.grey2));

        }
        if (view == linlytShipping) {
//            mHrscrlVwZipContainer.setVisibility(View.VISIBLE);
//            mEdtxtZipCode.setVisibility(View.VISIBLE);
//            mImgVwAddZipCode.setVisibility(View.VISIBLE);
            linlytShipping.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytPickup.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgPickup.setImageResource(R.drawable.light_blue_circle);
            imgShipping.setImageResource(R.drawable.white_cicle);
            isPickup = false;
            tvShipping.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvPickup.setTextColor(getResources().getColor(R.color.grey2));
        }
        if (view == mImgVwAddZipCode) {
            verifyZip();
        }
        if (view == btnDeleteVoucher) {
            RemoveVoucherListingApi();

        }
        if (view == btnAddVoucher) {
//             launch the place picker intent here...
            Intent intent = new Intent(ActivityAddListingStep2.this, ActivityVoucherListings.class);
            this.startActivityForResult(intent, 12);
        }
        if (mTxtVwTc == view) {
//            String url = "http://rippleitt.com/listing_policies.php";
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
            Intent i = new Intent(ActivityAddListingStep2.this, TermsConditionsActivity.class);
            startActivity(i);
        }
        if (view == mrelAddProduct_back) {
            if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT() != null) {
                Intent intent = new Intent(ActivityAddListingStep2.this, ActivityAddListingStep1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
            }
            RippleittAppInstance.getInstance().getListingImages().clear();
            finish();
        }

        if (view == mbtnAddPRoductPublish) {
            if (validate())
                publishListing(MODE_PUBLISH);

        }

        if (view == mbtnAddProdSaveDraft) {
            if (validate())
                publishListing(MODE_DRAFT);
        }
        if (view == linlytCash) {
            linlytCash.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytCreditCard.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgCash.setImageResource(R.drawable.white_cicle);
            imgCreditCard.setImageResource(R.drawable.light_blue_circle);
            paymentMode = 1;
            tvCash.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvCreditCard.setTextColor(getResources().getColor(R.color.grey2));
        }
        if (view == linlytCreditCard) {
            linlytCreditCard.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytCash.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgCreditCard.setImageResource(R.drawable.white_cicle);
            imgCash.setImageResource(R.drawable.light_blue_circle);
            paymentMode = 2;
            tvCreditCard.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvCash.setTextColor(getResources().getColor(R.color.grey2));
        }
//        if (view == linlytBuyNow) {
//            if (!isBuy) {
//                isBuy=true;
//                chkBuyNow.setChecked(true);
//            }else{
//                isBuy=false;
//                chkBuyNow.setChecked(false);
//            }
//            linlytBuyNow.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
//            linlytMakeOffer.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            linlytBoth.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            buyOption = 1;
//            tvBuyNow.setTextColor(getResources().getColor(R.color.white));
//            tvMakeOffer.setTextColor(getResources().getColor(R.color.green_light));
//            tvBoth.setTextColor(getResources().getColor(R.color.green_light));

//        }
//        if (view == linlytBoth) {
//            linlytBuyNow.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            linlytMakeOffer.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            linlytBoth.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
//            buyOption = 2;
//            tvBuyNow.setTextColor(getResources().getColor(R.color.green_light));
//            tvMakeOffer.setTextColor(getResources().getColor(R.color.green_light));
//            tvBoth.setTextColor(getResources().getColor(R.color.white));
//
//        }
//        if (view == linlytMakeOffer) {
//            linlytBuyNow.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            linlytMakeOffer.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
//            linlytBoth.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            buyOption = 0;
//            tvBuyNow.setTextColor(getResources().getColor(R.color.green_light));
//            tvMakeOffer.setTextColor(getResources().getColor(R.color.white));
//            tvBoth.setTextColor(getResources().getColor(R.color.green_light));
//            if (!isOffer) {
//                isOffer=true;
//                chkMakeOffer.setChecked(true);
//            }else{
//                isOffer=false;
//                chkMakeOffer.setChecked(false);
//            }
//            buyOption = 1;
//        }

    }

    private void publishListing(int flag) {
        listingTemplate.setAddress(mEdtxtAddress.getText().toString());
        listingTemplate.setPrice(medittxtAddProductPrice.getText().toString());
//        listingTemplate.setRewardAmount(mEdtxtRewardPoint.getText().toString());
        listingTemplate.setRewardAmount("0");
        if (listingTemplate.getListing_type().equalsIgnoreCase("1")) {
            listingTemplate.setQuantity(mEdtxtQty.getText().toString().trim());
        }else {
            listingTemplate.setQuantity("1000");
        }
        listingTemplate.setUnits(mSpnrnits.getSelectedItem().toString());
        if (paymentMode == 1) {
            listingTemplate.setPayment_mode("1");
        } else {
            listingTemplate.setPayment_mode("2");
        }
//        if (buyOption == 1) {
//            listingTemplate.setDirect_buy("1");
//        } else if (buyOption == 2) {
//            listingTemplate.setDirect_buy("2");
//        } else {
//            listingTemplate.setDirect_buy("0");
//        }

        if (chkMakeOffer.isChecked() && chkBuyNow.isChecked()) {
            listingTemplate.setDirect_buy("2");
        } else if (chkMakeOffer.isChecked()) {
            listingTemplate.setDirect_buy("0");
        } else if (chkBuyNow.isChecked()){
            listingTemplate.setDirect_buy("1");
        }


//        if (mRdBtnServicePickUp.isChecked()) {
//            listingTemplate.setShippingType("1");
//        }
//        if (mRdBtnServiceZIP.isChecked()) {
//            listingTemplate.setShippingType("2");
//            String zipPayload = "";
//            for (String zip : RippleittAppInstance.getInstance().getServiceZIPCodes().keySet()) {
//                zipPayload = zipPayload + zip + ",";
//            }
//            zipPayload = zipPayload.replaceAll(",$", "");
//            listingTemplate.setZipCodes(zipPayload);
//        }
        if (isPickup) {
            listingTemplate.setShippingType("1");
        } else {
            listingTemplate.setShippingType("2");
            String zipPayload = "";
            for (String zip : RippleittAppInstance.getInstance().getServiceZIPCodes().keySet()) {
                zipPayload = zipPayload + zip + ",";
            }
            zipPayload = zipPayload.replaceAll(",$", "");
            listingTemplate.setZipCodes(zipPayload);
        }
        listingTemplate.setListingID(RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID());


//        new SubmitListingAsync(this, this, listingTemplate, flag).execute();
        new UpdateListingAsync(this, this, listingTemplate, flag).execute();

    }

    private void launchPlacePiker() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACTION_ACCESS_LOCATION_);
        } else {
            // user has already given location permission, we now launch the picker.
            try {
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (Exception e) {
                mProgressDialog.dismiss();
                mEdtxtAddress.setEnabled(true);
                mProgressDialog.dismiss();
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean validate() {
//        if (!listingTemplate.getListing_type().equals("2") && mEdtxtQty.getText().toString().trim().equalsIgnoreCase("")) {
        if (mEdtxtQty.getText().toString().trim().equalsIgnoreCase("")&&  !RippleittAppInstance.getInstance().getCURRENT_ADDED_LISTING_OBJECT().getListing_type().equalsIgnoreCase("2")){
            mEdtxtQty.setError("Please enter listing quantity");
            mScrlVwWrapper.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrapper.scrollTo(0, mEdtxtQty.getTop());
                }
            });
            //Toast.makeText(AddNewProduct.this, "Please enter listing quantity    ",Toast.LENGTH_LONG).show();
            return false;
        }
//        if (!listingTemplate.getListing_type().equals("2") && mEdtxtQty.getText().toString().trim().equalsIgnoreCase("0")) {
        if (mEdtxtQty.getText().toString().trim().equalsIgnoreCase("0")&&  !RippleittAppInstance.getInstance().getCURRENT_ADDED_LISTING_OBJECT().getListing_type().equalsIgnoreCase("2")) {
            mEdtxtQty.setError("Please enter valid listing quantity");
            mScrlVwWrapper.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrapper.scrollTo(0, mEdtxtQty.getTop());
                }
            });
            //Toast.makeText(AddNewProduct.this, "Please enter valid listing quantity    ",Toast.LENGTH_LONG).show();
            return false;
        }
//        if (!listingTemplate.getListing_type().equals("2") && medittxtAddProductPrice.getText().toString().trim().equalsIgnoreCase("")) {
        if (medittxtAddProductPrice.getText().toString().trim().equalsIgnoreCase("")) {
            medittxtAddProductPrice.setError("Please enter listing price");
            mScrlVwWrapper.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrapper.scrollTo(0, medittxtAddProductPrice.getTop());
                }
            });
            // Toast.makeText(AddNewProduct.this, "Please enter listing price    ",Toast.LENGTH_LONG).show();
            return false;
        }
        float listingPrice = 0;
        try {
            listingPrice = Float.parseFloat(medittxtAddProductPrice.getText().toString().trim());
        } catch (Exception e) {

        }

        if (!listingTemplate.getListing_type().equals("2") && listingPrice < 1) {
            medittxtAddProductPrice.setError("Price must not be less than $1");
            mScrlVwWrapper.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrapper.scrollTo(0, medittxtAddProductPrice.getTop());
                }
            });
            // Toast.makeText(AddNewProduct.this, "Please enter listing price    ",Toast.LENGTH_LONG).show();

            return false;
        }



//        float referralAmount = 0;
//        try {
//            referralAmount = Float.parseFloat(mEdtxtRewardPoint.getText().toString().trim());
//        } catch (Exception e) {
//
//        }


//        if (referralAmount < 1) {
//            mEdtxtRewardPoint.setError("Referral amount must not be less than $1");
//            mEdtxtRewardPoint.post(new Runnable() {
//                @Override
//                public void run() {
//                    mEdtxtRewardPoint.scrollTo(30, mEdtxtRewardPoint.getTop());
//                }
//            });
//            Toast.makeText(AddNewProduct.this, "Please enter referral amount    ",Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//
//
//
//        if (mEdtxtRewardPoint.getText().toString().trim().equalsIgnoreCase("")) {
//            mEdtxtRewardPoint.setError("Please enter listing referral amount.    ");

//            mScrlVwWrappe.post(new Runnable() {
//                @Override
//                public void run() {
//                    mScrlVwWrappe.scrollTo(0, mEdtxtRewardPoint.getTop());
//                }
//            });
//            return false;
//        }

        if (!chkBuyNow.isChecked() && !chkMakeOffer.isChecked()){
            Toast.makeText(ActivityAddListingStep2.this,
                    "Please select atleast one buying option.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mChkBxTC.isChecked()) {
            com.rippleitt.utils.CommonUtils
                    .showSingleButtonAlert(ActivityAddListingStep2.this,
                            "Please accept the Rippleitt Listing Policies");
            //mChkBxTerms.setError("Please accept the terms of usage.");
            /*Toast.makeText(SignUpActivity.this,
                    "Please accept the terms of usage.",
                    Toast.LENGTH_SHORT).show();*/
            return false;
        }

        return true;
    }

    @Override
    public void onInit() {
        //Toast.makeText(ActivityAddListingStep2.this,"sync-init",Toast.LENGTH_LONG).show();
        CommonUtils.showProgress(ActivityAddListingStep2.this, "Submitting your listing...");
    }

    @Override
    public void onError(ListingSyncResponseTemplate template) {
        CommonUtils.dismissProgress();
        Toast.makeText(ActivityAddListingStep2.this, "Could not complete your request", Toast.LENGTH_LONG).show();
        RippleittAppInstance.getInstance().getListingImages().clear();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onSuccess(ListingSyncResponseTemplate template) {
        CommonUtils.dismissProgress();
        if (!template.getDraft_flag().equalsIgnoreCase("1")) {
            // listing was for publication..
//            if (paymentMode==2 && template.getIs_card_available() != null && !template.getIs_card_available().equalsIgnoreCase("1")) {
//             card not available, so we ask the user to add a card....
//                addCardPrompt();
//            } else {
                Toast.makeText(ActivityAddListingStep2.this,
                        "Thank you for adding new listing!! It’s under review and will be posted within 24 hours.",
                        Toast.LENGTH_LONG).show();
                CommonUtils.dismissProgress();
                RippleittAppInstance.getInstance().getListingImages().clear();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
//            }

        } else {
            Toast.makeText(ActivityAddListingStep2.this,
                    "Your draft has been saved",
                    Toast.LENGTH_LONG).show();
            CommonUtils.dismissProgress();
            RippleittAppInstance.getInstance().getListingImages().clear();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        RippleittAppInstance.getInstance().getListingImages().clear();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void addCardPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please add a credit card to your account in order to publish a listing.")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RippleittAppInstance.getInstance().setAddCardMode(1);
                        Intent intent=new Intent(ActivityAddListingStep2.this, ActivityUpdatePaymentMethod.class);
                        intent.putExtra("isUpdate",false);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void verifyZip() {
        if (mEdtxtZipCode.getText().toString().trim().equalsIgnoreCase("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please provide 4 character ZIP code")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return;
        }
        // now we verify of the ZIP code is okay...
        String pattern = "^(0[289][0-9]{2})|([1345689][0-9]{3})|(2[0-8][0-9]{2})|(290[0-9])|(291[0-4])|(7[0-9][0-9]{2})|(7[8-9][0-9]{2})$";


        if (!mEdtxtZipCode.getText().toString().trim().matches(pattern)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please provide a valid ZIP code")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return;
        }


        if (RippleittAppInstance
                .getInstance()
                .getServiceZIPCodes()
                .containsValue(mEdtxtZipCode.getText().toString().trim())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This ZIP code is already added to this listing")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }
        RippleittAppInstance
                .getInstance()
                .getServiceZIPCodes()
                .put(mEdtxtZipCode.getText().toString().trim(), mEdtxtZipCode.getText().toString().trim());
        // now add this ZIP code to this listing...
        final com.robertlevonyan.views.chip.Chip chip = new com.robertlevonyan.views.chip.Chip(ActivityAddListingStep2.this);
        chip.setChipText(mEdtxtZipCode.getText().toString().trim());
        LinearLayout
                .LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout
                .LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 0, 4, 0);
        chip.setLayoutParams(params);

        chip.setClosable(true);
        chip.setOnCloseClickListener(new OnCloseClickListener() {
            @Override
            public void onCloseClick(View v) {
                RippleittAppInstance.getInstance().getServiceZIPCodes().remove(chip.getChipText());
                refreshServiceZipCodes();
            }
        });
        mEdtxtZipCode.setText("");
        mLnLtZipContainer.addView(chip);
    }

    private void refreshServiceZipCodes() {
        mLnLtZipContainer.removeAllViews();
        for (String zip : RippleittAppInstance.getInstance().getServiceZIPCodes().keySet()) {
            final com.robertlevonyan.views.chip.Chip chip = new com.robertlevonyan.views.chip.Chip(ActivityAddListingStep2.this);
            chip.setChipText(zip);
            chip.setClosable(true);
            LinearLayout
                    .LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            chip.setLayoutParams(params);

            chip.setOnCloseClickListener(new OnCloseClickListener() {
                @Override
                public void onCloseClick(View v) {
                    RippleittAppInstance.getInstance().getServiceZIPCodes().remove(chip.getChipText());
                    refreshServiceZipCodes();
                }
            });

            mLnLtZipContainer.addView(chip);
        }
    }

//    Is_drafted = null
//    Latitude = "0.0"
//    Longitude = "0.0"
//    1 = "C-184, Industrial Area, Sector 75, Sahibzada Ajit Singh Nagar, Punjab 140308, India"
//    category = "72655853"
//    description = "kvvkgkkg vkkgkggk"
//    direct_buy = "0"
//    is_new = "1"
//    listingID = null
//    listing_type = "2"
//    payment_mode = "1"
//    price = "0"
//    productname = "new service"
//    quantity = "0"
//    rewardAmount = "0"
//    shippingType = "1"
//    sub_category = "44228284"
//    units = "Boxes"
//    zipCodes = ""
//    zipCodesRemoved = ""

    private void preloadListingData() {

        listingTemplate.setLatitude(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getLatitude());
        listingTemplate.setLongitude(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getLatitude());

        listingTemplate.setProductname(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getProductname());
        listingTemplate.setDescription(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getListing_description());

        if (RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getListing_price() != null && !RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getListing_price().equals("null")) {
            medittxtAddProductPrice.setText(RippleittAppInstance
                    .getInstance()
                    .getCURRENT_LISTING_OBJECT().getListing_price());
        }

        if (RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getAddress() != null && !RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getAddress().equals("null")) {

            mEdtxtAddress.setText(RippleittAppInstance
                    .getInstance()
                    .getCURRENT_LISTING_OBJECT().getLocation().getAddress());
        }

//        if (RippleittAppInstance
//                .getInstance()
//                .getCURRENT_LISTING_OBJECT().getRefer_amount() != null) {
//            try {
//                float referAmount = Float.parseFloat(RippleittAppInstance
//                        .getInstance()
//                        .getCURRENT_LISTING_OBJECT().getRefer_amount());
//
//                if (referAmount != 0) {
//
//                    mEdtxtRewardPoint.setText(RippleittAppInstance
//                            .getInstance()
//                            .getCURRENT_LISTING_OBJECT().getRefer_amount().replaceAll("\\$", ""));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            // mEdtxtRewardPoint.setEnabled(false);
//        }

        if (RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getQuantity() != null && !RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getQuantity().equals("null")) {
            mEdtxtQty.setText(RippleittAppInstance
                    .getInstance()
                    .getCURRENT_LISTING_OBJECT().getQuantity());
        }
//        try{
//            INITIAL_QUANTITY=Integer.parseInt(RippleittAppInstance
//                    .getInstance()
//                    .getCURRENT_LISTING_OBJECT().getQuantity());
//        }catch (Exception e){
//
//        }

        if (RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getPayment_mode() == null || RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getPayment_mode().equalsIgnoreCase("1") || RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getPayment_mode().equalsIgnoreCase("")) {
            linlytCash.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytCreditCard.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgCash.setImageResource(R.drawable.white_cicle);
            imgCreditCard.setImageResource(R.drawable.light_blue_circle);
            paymentMode = 1;
            tvCash.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvCreditCard.setTextColor(getResources().getColor(R.color.grey2));

//            mRdBtnPaymentModeCash.setChecked(true);
//            mRdBtnPaymentModeRipple.setChecked(false);
        } else {
            linlytCreditCard.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytCash.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgCreditCard.setImageResource(R.drawable.white_cicle);
            imgCash.setImageResource(R.drawable.light_blue_circle);
            paymentMode = 2;
            tvCreditCard.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvCash.setTextColor(getResources().getColor(R.color.grey2));

//            mRdBtnPaymentModeCash.setChecked(false);
//            mRdBtnPaymentModeRipple.setChecked(true);
        }

        listingTemplate.setIs_new(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getIs_new());

        String str= RippleittAppInstance
                        .getInstance()
                        .getCURRENT_LISTING_OBJECT()
                        .getDirect_buy();
        Log.d("preloadListingData: ",str);
        if (RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getDirect_buy().equalsIgnoreCase("1")) {
//            isBuy=true;
            chkBuyNow.setChecked(true);
//            isOffer=false;
            chkMakeOffer.setChecked(false);

//            linlytBuyNow.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
//            linlytMakeOffer.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            linlytBoth.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            buyOption = 1;
//            tvBuyNow.setTextColor(getResources().getColor(R.color.white));
//            tvMakeOffer.setTextColor(getResources().getColor(R.color.green_light));
//            tvBoth.setTextColor(getResources().getColor(R.color.green_light));

//            mRdBtnBuyNow.setChecked(true);
//            mRdBtnMakeOffer.setChecked(false);
//            mRdBtnBoth.setChecked(false);
        } else if (RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getDirect_buy().equalsIgnoreCase("2")) {
//            linlytBuyNow.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            linlytMakeOffer.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            linlytBoth.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
//            buyOption = 2;
//            isOffer=true;
//            isBuy=false;
            chkBuyNow.setChecked(true);
            chkMakeOffer.setChecked(true);
//            tvBuyNow.setTextColor(getResources().getColor(R.color.green_light));
//            tvMakeOffer.setTextColor(getResources().getColor(R.color.green_light));
//            tvBoth.setTextColor(getResources().getColor(R.color.white));

//            mRdBtnBuyNow.setChecked(false);
//            mRdBtnMakeOffer.setChecked(false);
//            mRdBtnBoth.setChecked(true);
        } else {

//            linlytBuyNow.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            linlytMakeOffer.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
//            linlytBoth.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
//            buyOption = 0;
//            isBuy=true;
//            isOffer=true;
            chkMakeOffer.setChecked(true);
            chkBuyNow.setChecked(false) ;

//            tvBuyNow.setTextColor(getResources().getColor(R.color.green_light));
//            tvMakeOffer.setTextColor(getResources().getColor(R.color.white));
//            tvBoth.setTextColor(getResources().getColor(R.color.green_light));

//            mRdBtnBuyNow.setChecked(false);
//            mRdBtnMakeOffer.setChecked(true);
//            mRdBtnBoth.setChecked(false);
        }

        if (RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getUnits().equalsIgnoreCase("Boxes")) {
            mSpnrnits.setSelection(0);
        }

        if (RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getUnits().equalsIgnoreCase("Packets")) {
            mSpnrnits.setSelection(1);
        }
        if (RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getUnits().equalsIgnoreCase("Pcs.")) {
            mSpnrnits.setSelection(2);
        }
        mSpnrnits.setEnabled(false);
        listingTemplate.setListing_type(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getListing_type());

        RippleittAppInstance
                .getInstance()
                .getCurrentListingExistingPics().clear();

        for (int index = 0; index < RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getListing_photos().length; index++) {
            RippleittAppInstance
                    .getInstance()
                    .getCurrentListingExistingPics()
                    .put(RippleittAppInstance
                                    .getInstance()
                                    .getCURRENT_LISTING_OBJECT()
                                    .getListing_photos()[index].getPhoto_path(),
                            RippleittAppInstance
                                    .getInstance()
                                    .getCURRENT_LISTING_OBJECT()
                                    .getListing_photos()[index].getPhoto_path()
                    );
        }
        RippleittAppInstance.getInstance().setCurrentTappedImageindex(0);
//        preloadExistingImage();

        if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getShipping_type().equalsIgnoreCase("1")) {
            mEdtxtZipCode.setVisibility(View.GONE);
            mImgVwAddZipCode.setVisibility(View.GONE);
            mHrscrlVwZipContainer.setVisibility(View.GONE);

            linlytPickup.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytShipping.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgPickup.setImageResource(R.drawable.white_cicle);
            imgShipping.setImageResource(R.drawable.light_blue_circle);
            isPickup = true;
            tvPickup.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvShipping.setTextColor(getResources().getColor(R.color.grey2));

//            mRdBtnServicePickUp.setChecked(true);
//            mRdBtnServiceZIP.setChecked(false);
        }
        if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getShipping_type().equalsIgnoreCase("2")) {
            mEdtxtZipCode.setVisibility(View.VISIBLE);
            mImgVwAddZipCode.setVisibility(View.VISIBLE);
            mHrscrlVwZipContainer.setVisibility(View.VISIBLE);
//            mRdBtnServicePickUp.setChecked(false);
//            mRdBtnServiceZIP.setChecked(true);

            linlytShipping.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytPickup.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgShipping.setImageResource(R.drawable.white_cicle);
            imgPickup.setImageResource(R.drawable.light_blue_circle);
            isPickup = false;
            tvShipping.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvPickup.setTextColor(getResources().getColor(R.color.grey2));

            preloadExistingZipCodes();
        }

        if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getVoucher_details() != null) {
            String voucherName = RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getVoucher_details().getName();
            String voucherType = RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getVoucher_details().getType();
            String voucherAmount = RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getVoucher_details().getAmount();
            voucher_id = RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getVoucher_details().getVoucherId();
            if (voucherType.equals("1")) {
                tvVoucherText.setText("$" + voucherAmount + " off voucher added");
            } else {
                tvVoucherText.setText(voucherAmount + "% off voucher added");
            }
            linearVoucher.setVisibility(View.VISIBLE);
            btnAddVoucher.setVisibility(View.GONE);
        }
    }


    private void preloadExistingZipCodes() {
        RippleittAppInstance.getInstance().getServiceZIPCodesExisting().clear();
        for (String zip : RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getPostal_codes()) {
            RippleittAppInstance.getInstance().getServiceZIPCodesExisting().put(zip, zip);
        }

        for (String zip : RippleittAppInstance.getInstance().getServiceZIPCodesExisting().keySet()) {
            final com.robertlevonyan.views.chip.Chip chip = new com.robertlevonyan.views.chip.Chip(ActivityAddListingStep2.this);
            chip.setChipText(zip);
            chip.setClosable(true);
            LinearLayout
                    .LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            chip.setLayoutParams(params);

            chip.setOnCloseClickListener(new OnCloseClickListener() {
                @Override
                public void onCloseClick(View v) {
                    RippleittAppInstance.getInstance().getServiceZIPCodesExisting().remove(chip.getChipText());
                    RippleittAppInstance.getInstance().getServiceZIPCodesDeleted().put(chip.getChipText(), chip.getChipText());
                    refreshServiceZipCodes();
                }
            });
            mLnLtZipContainer.addView(chip);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Toast.makeText(getBaseContext(), data.getStringExtra("voucher_name"), Toast.LENGTH_LONG).show();
        if (resultCode == 12) {
            if (requestCode == 12 && data != null) {
                String voucherName = data.getStringExtra("voucher_name");
                String voucherType = data.getStringExtra("voucher_type");
                String voucherAmount = data.getStringExtra("voucher_amount");
                voucher_id = data.getStringExtra("voucher_id");

                if (voucherType.equals("1")) {
                    tvVoucherText.setText("$" + voucherAmount + " off voucher added");
                } else {
                    tvVoucherText.setText(voucherAmount + "% off voucher added");
                }
                linearVoucher.setVisibility(View.VISIBLE);
                btnAddVoucher.setVisibility(View.GONE);
            }
        }
    }


    public void RemoveVoucherListingApi() {

        mLoader.setVisibility(View.VISIBLE);
        linearVoucher.setVisibility(View.GONE);

        final RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.REMOVE_VOUCHER_LISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mLoader.hide();
                mLoader.setVisibility(View.GONE);

                try {
                    Gson gson = new Gson();
                    CategoryListTemplate response_ = (CategoryListTemplate) gson.fromJson(response, CategoryListTemplate.class);
                    if (response_.getResponse_code() == 1) {
//                            if(RippleittAppInstance.getInstance().getListingDisputeMode()==1){

                        voucher_id = null;
                        linearVoucher.setVisibility(View.GONE);
                        btnAddVoucher.setVisibility(View.VISIBLE);

//                        Toast.makeText(getApplicationContext(), response_.getResponse_message(), Toast.LENGTH_SHORT).show();

                    } else {
                        linearVoucher.setVisibility(View.VISIBLE);
                        btnAddVoucher.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), response_.getResponse_message(), Toast.LENGTH_SHORT).show();
                        // show response_message in alert...
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoader.hide();
                mLoader.setVisibility(View.GONE);
                linearVoucher.setVisibility(View.VISIBLE);
                btnAddVoucher.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("token", PreferenceHandler.readString(getBaseContext(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("voucher_id", voucher_id);
                params.put("listing_id", RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID());
                Log.e("UserList_Params", params + "");
                return params;
            }
        };
        myRqst.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(myRqst);
    }


    public void postalCodeApi(final String strSearch) {

//        String google_key= "AIzaSyBqjRtP_5cOGuigZyf5qNMq8LzcU7hzRY0";
//        String url= String.format("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%1$s&types=geocode&key=%2$s",
//                strSearch, google_key);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final StringRequest myRqst = new StringRequest(Request.Method.POST,RippleittAppInstance.BASE_URL + RippleittAppInstance.FETCH_POSTAL_CODES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                mProgressDialog.dismiss();
                try {
                    recyclerPostcodes.setVisibility(View.VISIBLE);

//                      maviLoaderHome.hide();
                    JSONObject object = new JSONObject(response);
                    PostalCodeApiResponse parsedResponse = (PostalCodeApiResponse)
                            new Gson()
                                    .fromJson(response, PostalCodeApiResponse.class);
                    placesList.clear();
                    placesList.addAll(parsedResponse.getData());
                    adapter.notifyAdapter(placesList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recyclerPostcodes.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityAddListingStep2.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("search", strSearch);
                System.out.print(params);
                return params;
            }
        };

        myRqst.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(myRqst);

    }

    @Override
    public void onItemClick(int pos) {
        mEdtxtAddress.clearFocus();
        com.rippleitt.commonUtilities.CommonUtils.keyboardHide(getBaseContext(),getWindow().getDecorView().getRootView());
        mEdtxtAddress.setText(placesList.get(pos).getPostcode());
        recyclerPostcodes.setVisibility(View.GONE);
    }

}
