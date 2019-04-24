package com.rippleitt.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.HorizontalReyclerViewAdapter;
import com.rippleitt.callback.RemovePicCallback;
import com.rippleitt.commonUtilities.ImageFilePath;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.commonUtilities.SubmitListingAsync;
import com.rippleitt.commonUtilities.UpdateListingAsync;
import com.rippleitt.controller.CustomTextWatcher;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.ListingSyncProcessCallback;
import com.rippleitt.modals.ListingSyncResponseTemplate;
import com.rippleitt.modals.ListingSyncTemplate;
import com.rippleitt.modals.SubCategoryListTemplate;
import com.rippleitt.utils.CommonUtils;
import com.robertlevonyan.views.chip.OnCloseClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import id.zelory.compressor.Compressor;

public class ActivityEditProduct extends AppCompatActivity
        implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        RemovePicCallback,ListingSyncProcessCallback, RadioGroup.OnCheckedChangeListener {


    private EditText medittxtAddProductName;
    private EditText medittxtDescAddProduct;
    private EditText medittxtAddProductPrice;
    private EditText medittxtAddProductWeight;
    private RelativeLayout mrelAddProduct_back;
    private RelativeLayout mrelUploadProductImages;
    private Spinner mspinnerCategoryAddProduct;

    private Button mbtnAddProdSaveDraft;
    private Button mbtnAddPRoductPublish;
    RecyclerView recyclerView;
    private Spinner spnrSubCategory;
    private RelativeLayout mRelLytSubCategoryContainer;
    private final int MODE_DRAFT=1;
    private final int MODE_PUBLISH=0;
    private int INITIAL_QUANTITY=0;

    //private LinearLayout mLinLytRecyclerContainer;
    private EditText mEdtxtAddress;
    private EditText mEdtxtRewardPoint;

    public static final int PICK_IMAGE = 1;
    private static int RESULT_LOAD_IMG = 1;

    ArrayAdapter<String> spinnerCategoryArr;
    ArrayAdapter<String> spinnerUnitArr;
    ArrayList<String> arry_imges;
    private boolean spnr_cat_init=false;
    private HorizontalReyclerViewAdapter horizontalAdapter;
    private int ACTION_READ_EXTERNAL_STORAGE=13;
    private String currentCategoryName="", categoryID="";
    private String currentSbCategoryName="", subCategoryID="";
    private boolean shouldPresetSubCategory=true;
    private  RecyclerView recyclerViewExistingPics, recyclerViewNewPics;
    private TextView mTxtvwLableExistingPics, mTxtVwLableAddedPics;
    private LinearLayout mLinLytExistingPics, mLinLytAddedPics;
    private RadioButton mRdBtnProduct, mRdBtnService,mRdBtnPaymentModeCash,
            mRdBtnNewProduct,
            mRdBtnUsedProduct,
            mRdBtnBuyNow,mRdBtnBoth,mRdBtnMakeOffer,
            mRdBtnPaymentModeRipple;
    private RelativeLayout mRelLytBack;
    private Compressor mCompressor;
    private Spinner mSpnrnits;
    private EditText mEdtxtQty;
    private ProgressDialog mProgressDialog;
    private final int PLACE_PICKER_REQUEST = 12;
    private double latitude__,longitude__;
    private RadioButton mRdBtnServicePickUp,mRdBtnServiceZIP;
    private EditText mEdtxtZipCode;
    private ImageView mImgVwAddZipCode;
    private HorizontalScrollView mHrscrlVwZipContainer;
    private LinearLayout mLnLtZipContainer, linearBuyingOptions, linearShippingMethods;;
    private RadioGroup mRdGrpListingType;
    private final int REQUEST_IMAGE_CAPTURE=21;
    private final int CAMERA_ACTION_IMAGE_CAPTURE=19;
    private ScrollView mScrlVwWrappe;
    private LinearLayout linearProductType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        mCompressor= new Compressor(ActivityEditProduct.this);
        mCompressor.setQuality(25);
        initUI();
        fetchCategories();
        preloadListingData();
        hideLocalPicsContainer();
       // populateProductImages();


        mRdBtnBoth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mRdBtnBuyNow.setChecked(false);
                    mRdBtnMakeOffer.setChecked(false);
                }
            }
        });
        mRdBtnBuyNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mRdBtnBoth.setChecked(false);
                    mRdBtnMakeOffer.setChecked(false);
                }
            }
        });
        mRdBtnMakeOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mRdBtnBuyNow.setChecked(false);
                    mRdBtnBoth.setChecked(false);
                }
            }
        });
    }


    @Override
    public void onPause(){
        super.onPause();
        mProgressDialog.dismiss();
    }


    private void hideLocalPicsContainer(){
        mTxtVwLableAddedPics.setVisibility(View.GONE);
        mLinLytAddedPics.setVisibility(View.GONE);
    }

    public void initUI(){
        mScrlVwWrappe=(ScrollView)findViewById(R.id.scrlVwWrapper);
        mRdBtnServiceZIP=(RadioButton)findViewById(R.id.rdBtnShippingServiceAreas);
        mRdBtnServicePickUp=(RadioButton)findViewById(R.id.rdBtnShippingPickup);
        mRdBtnServiceZIP.setOnClickListener(this);
        mRdBtnServicePickUp.setOnClickListener(this);
        mEdtxtZipCode=(EditText)findViewById(R.id.edtxtZipCode);
        mImgVwAddZipCode=(ImageView)findViewById(R.id.imgvwAddZipCode);
        mImgVwAddZipCode.setOnClickListener(this);
        mHrscrlVwZipContainer=(HorizontalScrollView)findViewById(R.id.hrscrlZipCodeContailer);
        mLnLtZipContainer=(LinearLayout)findViewById(R.id.linlytZipCodeContainer);
        mRdGrpListingType=(RadioGroup)findViewById(R.id.radioGroupListingType);
        mRdGrpListingType.setOnCheckedChangeListener(this);
        mProgressDialog= new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Submitting your changes");
        mRdBtnProduct=(RadioButton)findViewById(R.id.rdBtnProduct);

        mRdBtnService=(RadioButton)findViewById(R.id.rdBtnService);
        mRelLytBack=(RelativeLayout)findViewById(R.id.relAddProduct_back);
        mRelLytBack.setOnClickListener(this);
        medittxtDescAddProduct=(EditText)findViewById(R.id.edittxtDescAddProduct);
        medittxtAddProductName=(EditText)findViewById(R.id.edittxtAddProductName);
        medittxtAddProductPrice=(EditText)findViewById(R.id.edittxtAddProductPrice);
        medittxtAddProductPrice.addTextChangedListener(new CustomTextWatcher(
                medittxtAddProductPrice));
        mRdBtnPaymentModeCash=(RadioButton)findViewById(R.id.rdBtnPaymentModecCash);
        mRdBtnNewProduct=(RadioButton)findViewById(R.id.rdBtnNewProduct);
        mRdBtnUsedProduct=(RadioButton)findViewById(R.id.rdBtnUsedProduct);
        mRdBtnMakeOffer=(RadioButton)findViewById(R.id.rdBtnMakeOffer);
        mRdBtnBuyNow=(RadioButton)findViewById(R.id.rdBtnBuyNow);
        mRdBtnBoth=(RadioButton)findViewById(R.id.rdBtnBoth);
        mRdBtnPaymentModeRipple=(RadioButton)findViewById(R.id.rdBtnPaymentModeRipple);
        mSpnrnits=(Spinner)findViewById(R.id.spnrUnits);
        mEdtxtQty=(EditText)findViewById(R.id.edittxtAddProductQty);
        //mRdBtnPaymentModeCash.setEnabled(false);
        //mRdBtnPaymentModeRipple.setEnabled(false);
        mSpnrnits.setClickable(false);
        mEdtxtAddress=(EditText)findViewById(R.id.edtxtAddress);
        mEdtxtAddress.setFocusable(false);
        mEdtxtAddress.setClickable(true);
        mEdtxtAddress.setOnClickListener(this);

        mrelAddProduct_back=(RelativeLayout) findViewById(R.id.relAddProduct_back);
        mrelUploadProductImages=(RelativeLayout) findViewById(R.id.relUploadProductImages);
        mspinnerCategoryAddProduct=(Spinner) findViewById(R.id.spinnerCategoryAddProduct);

        mLinLytAddedPics=(LinearLayout)findViewById(R.id.linlytAddedPics);
        mEdtxtRewardPoint=(EditText)findViewById(R.id.edtxtReferralAmount);
        mEdtxtRewardPoint.addTextChangedListener(new CustomTextWatcher(
                mEdtxtRewardPoint));
        mbtnAddProdSaveDraft=(Button) findViewById(R.id.btnAddProdSaveDraft);
        mbtnAddPRoductPublish=(Button) findViewById(R.id.btnAddPRoductPublish);
        recyclerViewNewPics = (RecyclerView)findViewById(R.id.recyclerProductMultiImages);
        recyclerViewExistingPics = (RecyclerView)findViewById(R.id.recyclerProductExistingMultiImages);
        linearProductType = (LinearLayout) findViewById(R.id.linearProductType);
        linearShippingMethods = findViewById(R.id.linearShippingMethods);
        linearBuyingOptions= findViewById(R.id.linearBuyingOptions);

        mTxtvwLableExistingPics=(TextView)findViewById(R.id.txtvwExistingPhotos);
        mTxtVwLableAddedPics=(TextView)findViewById(R.id.txtvwAddedPhotos);
        mLinLytExistingPics=(LinearLayout)findViewById(R.id.linlyExistingImageContainer);
        recyclerViewExistingPics.addItemDecoration(new DividerItemDecoration(ActivityEditProduct.this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        recyclerViewNewPics.addItemDecoration(new DividerItemDecoration(ActivityEditProduct.this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        spnrSubCategory=(Spinner)findViewById(R.id.spnrSubCategory);
        mRelLytSubCategoryContainer=(RelativeLayout)findViewById(R.id.relProductSubCategory);
        mRelLytSubCategoryContainer.setVisibility(View.GONE);
        arry_imges=new ArrayList<>();
        mrelAddProduct_back.setOnClickListener(this);
        mbtnAddPRoductPublish.setOnClickListener(this);
        mrelUploadProductImages.setOnClickListener(this);
        mbtnAddProdSaveDraft.setOnClickListener(this);
        initSpinnerListners();
        if(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getIs_live().equalsIgnoreCase("1")){
            mbtnAddProdSaveDraft.setVisibility(View.GONE);
            mEdtxtRewardPoint.setEnabled(false);
            medittxtAddProductPrice.setEnabled(false);
        }
    }
    private void initSpinnerListners(){
        mspinnerCategoryAddProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spnr_cat_init) {
                    if (i == 0) {
                        //return;
                        spnrSubCategory.setSelection(0);
                    }

                    performVolleyFetchSubCategories();
                    //mLinLytArea.setVisibility(View.VISIBLE);
                }
                spnr_cat_init = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void preloadListingData(){
        latitude__=Double.parseDouble(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getLatitude());
        longitude__=Double.parseDouble(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getLatitude());

        medittxtAddProductName.setText(RippleittAppInstance
                                        .getInstance()
                                        .getCURRENT_LISTING_OBJECT().getProductname());
        medittxtDescAddProduct.setText(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getListing_description());
        medittxtAddProductPrice.setText(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getListing_price());
        mEdtxtAddress.setText(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getAddress());
        mEdtxtRewardPoint.setText(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getRefer_amount().replaceAll("\\$",""));
       // mEdtxtRewardPoint.setEnabled(false);
        mEdtxtQty.setText(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getQuantity());
        try{
            INITIAL_QUANTITY=Integer.parseInt(RippleittAppInstance
                    .getInstance()
                    .getCURRENT_LISTING_OBJECT().getQuantity());
        }catch (Exception e){

        }

        if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getPayment_mode().equalsIgnoreCase("1")){
            mRdBtnPaymentModeCash.setChecked(true);
            mRdBtnPaymentModeRipple.setChecked(false);
        }else{
            mRdBtnPaymentModeCash.setChecked(false);
            mRdBtnPaymentModeRipple.setChecked(true);
        }

        if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getIs_new().equalsIgnoreCase("1")){
            mRdBtnNewProduct.setChecked(true);
            mRdBtnUsedProduct.setChecked(false);
        }else{
            mRdBtnNewProduct.setChecked(false);
            mRdBtnUsedProduct.setChecked(true);
        }

        if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getDirect_buy().equalsIgnoreCase("1")){
            mRdBtnBuyNow.setChecked(true);
            mRdBtnMakeOffer.setChecked(false);
            mRdBtnBoth.setChecked(false);
        }else if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getDirect_buy().equalsIgnoreCase("2")){
            mRdBtnBuyNow.setChecked(false);
            mRdBtnMakeOffer.setChecked(false);
            mRdBtnBoth.setChecked(true);
        }else{
            mRdBtnBuyNow.setChecked(false);
            mRdBtnMakeOffer.setChecked(true);
            mRdBtnBoth.setChecked(false);
        }

        if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getUnits().equalsIgnoreCase("Boxes")){
            mSpnrnits.setSelection(0);
        }

        if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getUnits().equalsIgnoreCase("Packets")){
            mSpnrnits.setSelection(1);
        }
        if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getUnits().equalsIgnoreCase("Pcs.")){
            mSpnrnits.setSelection(2);
        }
        mSpnrnits.setEnabled(false);
        if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getListing_type().equalsIgnoreCase("1")){
            mRdBtnProduct.setChecked(true);
            mRdBtnService.setChecked(false);
            linearProductType.setVisibility(View.VISIBLE);

        }else{
            mRdBtnProduct.setChecked(false);
            mRdBtnService.setChecked(true);
            linearProductType.setVisibility(View.GONE);
        }

        RippleittAppInstance
                .getInstance()
                .getCurrentListingExistingPics().clear();

        for(int index=0;index<RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getListing_photos().length;index++){
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
        preloadExistingImage();



        if(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getShipping_type().equalsIgnoreCase("1")){
                mEdtxtZipCode.setVisibility(View.GONE);
                mImgVwAddZipCode.setVisibility(View.GONE);
                mHrscrlVwZipContainer.setVisibility(View.GONE);
                mRdBtnServicePickUp.setChecked(true);
                mRdBtnServiceZIP.setChecked(false);
        }if(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getShipping_type().equalsIgnoreCase("2")){
            //mEdtxtZipCode.setVisibility(View.VISIBLE);
            //mImgVwAddZipCode.setVisibility(View.VISIBLE);
            //mHrscrlVwZipContainer.setVisibility(View.VISIBLE);
            mRdBtnServicePickUp.setChecked(false);
            mRdBtnServiceZIP.setChecked(true);
           // preloadExistingZipCodes();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==mRdBtnServicePickUp){
            mHrscrlVwZipContainer.setVisibility(View.GONE);
            mEdtxtZipCode.setVisibility(View.GONE);
            mLnLtZipContainer.setVisibility(View.GONE);
            mImgVwAddZipCode.setVisibility(View.GONE);
        }if(view==mRdBtnServiceZIP){
            //mHrscrlVwZipContainer.setVisibility(View.VISIBLE);
            //mEdtxtZipCode.setVisibility(View.VISIBLE);
            //mImgVwAddZipCode.setVisibility(View.VISIBLE);
        }if(view==mImgVwAddZipCode){
            //verifyZip();
        }
        if(view==mEdtxtAddress){
            // launch the place picker intent here...
            launchPlacePiker();
        }
        if (view==mrelUploadProductImages) {

            showPhotoOptions();
            //addAttachments();
        }if(view==mbtnAddProdSaveDraft){
            if(validate())
                publishListing(MODE_DRAFT);
        }if(view==mbtnAddPRoductPublish){
            if(validate())
                publishListing(MODE_PUBLISH);
        }if(view==mRelLytBack){
            finish();
        }
    }

    private void launchCamera(){

        // first check if camera permission is granted to the App...
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAMERA_ACTION_IMAGE_CAPTURE);
            }

        }
    }

    private void showPhotoOptions(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Import Image From:")
                .setCancelable(false)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        launchCamera();

                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addAttachments();
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //addAttachments();
            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();
    }

    private void launchPlacePiker(){
        try{
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        }catch (Exception e){
            mProgressDialog.dismiss();
            mEdtxtAddress.setEnabled(true);
        }
    }

    private void addAttachments() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                             Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, ACTION_READ_EXTERNAL_STORAGE);
        } else {
            chooseFromGallery();
        }
    }

    private void chooseFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    private void  publishListing(int flag){

        mProgressDialog.show();
        ListingSyncTemplate listingTemplate = new ListingSyncTemplate();
        listingTemplate.setProductname(medittxtAddProductName.getText().toString());
        listingTemplate.setAddress(mEdtxtAddress.getText().toString());
        listingTemplate.setCategory(RippleittAppInstance
                .getInstance()
                .getCategoryId(mspinnerCategoryAddProduct
                        .getSelectedItem().toString()));
        listingTemplate.setSub_category(RippleittAppInstance.getInstance()
                .getSubCategoryId(spnrSubCategory
                        .getSelectedItem().toString()));
        listingTemplate.setDescription(medittxtDescAddProduct.getText().toString());
        listingTemplate.setPrice(medittxtAddProductPrice.getText().toString());
//        listingTemplate.setRewardAmount(mEdtxtRewardPoint.getText().toString());
        listingTemplate.setRewardAmount("0");
        listingTemplate.setQuantity(mEdtxtQty.getText().toString().trim());
        listingTemplate.setUnits(mSpnrnits.getSelectedItem().toString());
//        if(mRdBtnPaymentModeCash.isChecked()){
//            listingTemplate.setPayment_mode("1");
//        }else{
//            listingTemplate.setPayment_mode("2");
//        }
        listingTemplate.setPayment_mode("1");
        listingTemplate.setLatitude(Double.toString(latitude__));
        listingTemplate.setLongitude(Double.toString(longitude__));

        if(mRdBtnService.isChecked())
            listingTemplate.setListing_type("2");

        if(mRdBtnBuyNow.isChecked())
            listingTemplate.setDirect_buy("1");
        else if(mRdBtnBoth.isChecked())
            listingTemplate.setDirect_buy("2");
        else
            listingTemplate.setDirect_buy("0");

        if(mRdBtnNewProduct.isChecked()){
            listingTemplate.setIs_new("1");
        }else{
            listingTemplate.setIs_new("0");
        }

        if(mRdBtnServicePickUp.isChecked())
            listingTemplate.setShippingType("1");

        if(mRdBtnServiceZIP.isChecked()){
            listingTemplate.setShippingType("2");
            /*

            String zipPayload="";
            for(String zip:RippleittAppInstance.getInstance().getServiceZIPCodesNew().keySet()){
                zipPayload=zipPayload+zip+",";
            }
            zipPayload = zipPayload.replaceAll(",$", "");
            listingTemplate.setZipCodes(zipPayload);
            String zipPayloadDeleted="";
            for(String zip:RippleittAppInstance.getInstance().getServiceZIPCodesDeleted().keySet()){
                zipPayloadDeleted=zipPayloadDeleted+zip+",";
            }
            zipPayloadDeleted = zipPayloadDeleted.replaceAll(",$", "");
            listingTemplate.setZipCodesRemoved(zipPayloadDeleted);
            */
        }
        if (mRdBtnService.isChecked()) {
            listingTemplate.setListing_type("2");
            listingTemplate.setIs_new("1");
            listingTemplate.setQuantity("10");
            listingTemplate.setPrice("10");
        }

        listingTemplate.setListingID(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getListing_id());
        new UpdateListingAsync(this,this, listingTemplate,flag).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG ){
                    if(resultCode == RESULT_OK
                    && null != data) {
                if(data.getClipData()!=null){
                    int imageCount=data.getClipData().getItemCount();
                    for(int index=0;index<imageCount;index++){


                        File compresedFile=      mCompressor.compressToFile(new File(ImageFilePath
                                .getPath(ActivityEditProduct.this,
                                        data.getClipData().getItemAt(index).getUri())));

                        if(!RippleittAppInstance.getInstance().getListingImages().containsKey(compresedFile.getAbsolutePath())){
                            RippleittAppInstance.getInstance()
                                    .getListingImages().put(compresedFile.getAbsolutePath(),compresedFile.getAbsolutePath());
                        }

                        // if the hashmap does not contain the image already we add it
                        // to avoid duplicate images beinf added.
                        /*
                        if(!RippleittAppInstance.getInstance().getListingImages()
                                .containsKey(data.getClipData().getItemAt(index).getUri())){
                            RippleittAppInstance.getInstance().getListingImages().put(
                                    ImageFilePath.getPath(ActivityEditProduct.this,
                                            data.getClipData().getItemAt(index).getUri()),
                                    data.getClipData().getItemAt(index).getUri());
                        }
                        */
                    }

                }else{

                    File compresedFile=      mCompressor.compressToFile(new File(ImageFilePath
                            .getPath(ActivityEditProduct.this,
                                    data.getData())));

                    if(!RippleittAppInstance.getInstance().getListingImages().containsKey(compresedFile.getAbsolutePath())){
                        RippleittAppInstance.getInstance()
                                .getListingImages().put(compresedFile.getAbsolutePath(),compresedFile.getAbsolutePath());
                    }
                    /*
                    if(!RippleittAppInstance.getInstance().getListingImages()
                            .containsKey(data.getData())){
                        RippleittAppInstance.getInstance().getListingImages().put(
                                ImageFilePath.getPath(ActivityEditProduct.this,
                                        data.getData()),
                                data.getData());
                    }
                */
                }

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }

                populateProductImages();

            }
        if(requestCode==PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = String.format("%s", place.getName());
                if(address!=null){
                    mEdtxtAddress.setText(address);
                    latitude__=place.getLatLng().latitude;
                    longitude__=place.getLatLng().longitude;
                }

                // Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }

        }if(requestCode==CAMERA_ACTION_IMAGE_CAPTURE){
                if(resultCode==RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    // mImageView.setImageBitmap(imageBitmap);
                    if(imageBitmap!=null){
                        new SaveBitmapAsync(imageBitmap).execute();
                    }

                }else{
                    Toast.makeText(this, "You haven't taken any Image",
                            Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void populateProductImages(){
        mLinLytAddedPics.setVisibility(View.VISIBLE);
        Set<String> keys = RippleittAppInstance.getInstance().getListingImages().keySet();
        String[] keysArray = keys.toArray(new String[keys.size()]);
        // now we draw the horizontal slider
        if(keysArray.length!=0){
            mTxtVwLableAddedPics.setText("Added Pics");
            mTxtVwLableAddedPics.setVisibility(View.VISIBLE);
            mLinLytAddedPics.setVisibility(View.VISIBLE);

            recyclerViewNewPics.addItemDecoration(new DividerItemDecoration(ActivityEditProduct.this, LinearLayoutManager.HORIZONTAL));
            horizontalAdapter = new HorizontalReyclerViewAdapter(keysArray,
                    getApplicationContext(),this,1,2);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ActivityEditProduct.this,
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewNewPics.setLayoutManager(horizontalLayoutManager);
            recyclerViewNewPics.setAdapter(horizontalAdapter);
        }else{
            mTxtVwLableAddedPics.setText("You have not added any pics");
            mLinLytAddedPics.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void removePic(String absPath, int fileType,int position) {
            if(fileType==1){ // this is a server url..
                //delete listing pic here
                // remove pic from local hashmap..
                RippleittAppInstance.getInstance().getListingImages().remove(absPath);
                populateProductImages();
            }else{
                if(position==RippleittAppInstance.getInstance().getCurrentTappedImageindex()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Primary image cannot be deleted!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    confirmDeletion(absPath);
                }

            }
    }

    @Override
    public void onImageCicked(String absPath, int type, int position, int displayMode) {
        if(displayMode==1){
            if(position!=RippleittAppInstance.getInstance().getCurrentTappedImageindex()){
                RippleittAppInstance.getInstance().setCurrentTappedImageindex(position);
                vlleyUpdateListingPrimaryImage(absPath);
            }
        }
    }


    private void confirmDeletion(final String path){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this pic?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    volleyDeletePic(path);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onError(ListingSyncResponseTemplate template) {
            mProgressDialog.dismiss();
    }

    @Override
    public void onSuccess(ListingSyncResponseTemplate template) {

        if(template.getDraft_flag().equalsIgnoreCase("0")){
            // listing was for publication..
//            if(template.getIs_card_available().equalsIgnoreCase("0")){
//                 card not available, so we ask the user to add a card....
//                addCardPrompt();
//            }else{
                Toast.makeText(ActivityEditProduct.this,
                        "Your Listing has been updated successfully...",
                        Toast.LENGTH_LONG).show();
                CommonUtils.dismissProgress();
                RippleittAppInstance.getInstance().getListingImages().clear();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
//            }

        }else{
            Toast.makeText(ActivityEditProduct.this,
                    "Your Listing has been updated successfully...",
                    Toast.LENGTH_LONG).show();
            RippleittAppInstance.getInstance().getListingImages().clear();
            finish();
            RippleittAppInstance.getInstance().setCURRENT_ADDED_LISTING_OBJECT(null);
            RippleittAppInstance.getInstance().setCURRENT_LISTING_OBJECT(null);
        }

        mProgressDialog.dismiss();

    }

    private void performVolleyFetchSubCategories(){
       // mProgressDialog.setMessage("Getting sub categories");
        //mProgressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance
                .getInstance().getFETCH_SUBCATEGORIES() ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        SubCategoryListTemplate response_ = (SubCategoryListTemplate)gson.fromJson(response,
                                                                SubCategoryListTemplate.class);
                        if(response_.getResponse_code()==1){
                            RippleittAppInstance
                                    .getInstance()
                                    .setSubCategories(response_.getData());
                            ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(ActivityEditProduct.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    RippleittAppInstance.getInstance().getSubCategories());
                            spnrSubCategory.setAdapter(karant_adapter);
                            mRelLytSubCategoryContainer.setVisibility(View.VISIBLE);
                            // mLinLytRegion.setVisibility(View.VISIBLE);
                            if(shouldPresetSubCategory){
                                shouldPresetSubCategory=false;
                                preselectSubCategory();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "Invalid keywords", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ActivityEditProduct.this,"could not fetch sub category, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityEditProduct.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("category_id",RippleittAppInstance
                        .getInstance()
                        .getCategoryId(mspinnerCategoryAddProduct.getSelectedItem().toString()));
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }
    private void fetchCategories(){
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getFETCH_CATEGORIES() ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // mProgressDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate)gson.fromJson(response,CategoryListTemplate.class);
                        if(response_.getResponse_code()==1){
                            RippleittAppInstance
                                    .getInstance()
                                    .setCategories(response_.getData());
                            ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(ActivityEditProduct.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    RippleittAppInstance.getInstance().getCategories());
                            mspinnerCategoryAddProduct.setAdapter(karant_adapter);
                            // mLinLytRegion.setVisibility(View.VISIBLE);
                            preselectCategory();

                        }else{
                            Toast.makeText(getApplicationContext(), "Invalid keywords", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ActivityEditProduct.this,"could not fetch categories, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityEditProduct.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                if (mRdBtnService.isChecked()) {
                    params.put("type", "2");
                }else{
                    params.put("type", "1");
                }

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }
    private void preselectCategory(){
        mspinnerCategoryAddProduct.setSelection(getIndex(mspinnerCategoryAddProduct,
                RippleittAppInstance
                        .getInstance()
                        .getCURRENT_LISTING_OBJECT().getCategory().getName()));
        shouldPresetSubCategory=true;
        performVolleyFetchSubCategories();
    }
    // returns the index of the current selected position for the spinner provided...
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    private void preselectSubCategory(){
        spnrSubCategory.setSelection(getIndex(spnrSubCategory,
                RippleittAppInstance
                        .getInstance()
                        .getCURRENT_LISTING_OBJECT().getSubcategory().getName()));

    }
    // loads the existing images for this listing
    private void preloadExistingImage(){
        if(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getListing_photos().length==0){
            mTxtvwLableExistingPics.setText("This listing does not have any existing photos.");
            mLinLytExistingPics.setVisibility(View.GONE);
        }else{
            mTxtvwLableExistingPics.setText("Existing Photos");
            mLinLytExistingPics.setVisibility(View.VISIBLE);
            mTxtVwLableAddedPics.setText("Added photos");
            mLinLytExistingPics.setVisibility(View.VISIBLE);

            String[] keysArray = RippleittAppInstance.getInstance().getCurrentListingExistingPics().keySet()
                                .toArray(new String[RippleittAppInstance.getInstance().getCurrentListingExistingPics().keySet().size()]);
            recyclerViewExistingPics.addItemDecoration(new DividerItemDecoration(ActivityEditProduct.this, LinearLayoutManager.HORIZONTAL));
            horizontalAdapter = new HorizontalReyclerViewAdapter(keysArray,
                    getApplicationContext(),this,2,1);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ActivityEditProduct.this,
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewExistingPics.setLayoutManager(horizontalLayoutManager);
            recyclerViewExistingPics.setAdapter(horizontalAdapter);


        }
    }


    private void volleyDeletePic(final String picPath){
            mProgressDialog.setMessage("Deleting listing image");
            mProgressDialog.show();
        //Toast.makeText(ActivityEditProduct.this, "deleting pic",Toast.LENGTH_LONG).show();
        String picID="";
        for(int index=0; index<RippleittAppInstance
                            .getInstance()
                            .getCURRENT_LISTING_OBJECT().getListing_photos().length;index++){
            if(RippleittAppInstance
                    .getInstance()
                    .getCURRENT_LISTING_OBJECT()
                    .getListing_photos()[index]
                    .getPhoto_path().equalsIgnoreCase(picPath)){
                picID=RippleittAppInstance
                        .getInstance()
                        .getCURRENT_LISTING_OBJECT()
                        .getListing_photos()[index]
                        .getPhoto_id();
            }
        }

        final String _pic_id=picID;

        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getDELETE_PHOTO() ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate)gson.fromJson(response,CategoryListTemplate.class);
                        if(response_.getResponse_code()==1){

                            RippleittAppInstance.getInstance().getCurrentListingExistingPics().remove(picPath);
                            preloadExistingImage();
                            Toast.makeText(ActivityEditProduct.this,
                                    "Pic deleted successfully",
                                    Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getApplicationContext(), "Invalid keywords", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ActivityEditProduct.this,"could not delete the photo, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityEditProduct.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("pic_id",_pic_id);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }
    private void vlleyUpdateListingPrimaryImage(final String picPath){
        mProgressDialog.setMessage("Updating primary Image...");
        mProgressDialog.show();
        //Toast.makeText(ActivityEditProduct.this, "deleting pic",Toast.LENGTH_LONG).show();
        String picID="";
        for(int index=0; index<RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getListing_photos().length;index++){
            if(RippleittAppInstance
                    .getInstance()
                    .getCURRENT_LISTING_OBJECT()
                    .getListing_photos()[index]
                    .getPhoto_path().equalsIgnoreCase(picPath)){
                picID=RippleittAppInstance
                        .getInstance()
                        .getCURRENT_LISTING_OBJECT()
                        .getListing_photos()[index]
                        .getPhoto_id();
            }
        }

        final String _pic_id=picID;

        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.UPDATE_LISTING_PRIMARY_IMAGE ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate)gson.fromJson(response,CategoryListTemplate.class);
                        if(response_.getResponse_code()==1){

                          //  RippleittAppInstance.getInstance().getCurrentListingExistingPics().remove(picPath);
                            preloadExistingImage();


                        }else{
                            Toast.makeText(getApplicationContext(), "Invalid keywords", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ActivityEditProduct.this,"could not update primary photo, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityEditProduct.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("image_id",_pic_id);
                params.put("listing_id",RippleittAppInstance
                        .getInstance()
                        .getCURRENT_LISTING_OBJECT().getListing_id());
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }

    private boolean validate(){
        if(medittxtAddProductName.getText().toString().trim().equalsIgnoreCase("")){
            medittxtAddProductName.setError("Please enter listing title");
            //Toast.makeText(ActivityEditProduct.this, ,Toast.LENGTH_LONG).show();
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, medittxtAddProductName.getTop()-20);
                }
            });
            return false;
        }
        if(RippleittAppInstance
                .getInstance()
                .getCategoryId(mspinnerCategoryAddProduct
                        .getSelectedItem().toString())
                .equalsIgnoreCase("0")
                ||(RippleittAppInstance
                .getInstance()
                .getCategoryId(mspinnerCategoryAddProduct
                        .getSelectedItem().toString())
                .equalsIgnoreCase(""))){
            Toast.makeText(ActivityEditProduct.this, "Please select listing category",Toast.LENGTH_LONG).show();
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, mspinnerCategoryAddProduct.getBottom());
                }
            });
            return false;
        }
        if(RippleittAppInstance
                .getInstance()
                .getSubCategoryId(spnrSubCategory
                        .getSelectedItem().toString())
                .equalsIgnoreCase("0")
                ||(RippleittAppInstance
                .getInstance()
                .getSubCategoryId(spnrSubCategory
                        .getSelectedItem().toString())
                .equalsIgnoreCase(""))){
            Toast.makeText(ActivityEditProduct.this, "Please select listing sub category",Toast.LENGTH_LONG).show();
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, spnrSubCategory.getBottom());
                }
            });
            return false;
        }

        if(!mRdBtnService.isChecked() && mEdtxtQty.getText().toString().trim().equalsIgnoreCase("")){
            mEdtxtQty.setError("Please enter listing quantity");
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, mEdtxtQty.getBottom()+70);
                }
            });
            //Toast.makeText(AddNewProduct.this, "Please enter listing quantity    ",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!mRdBtnService.isChecked() && mEdtxtQty.getText().toString().trim().equalsIgnoreCase("0")){
            mEdtxtQty.setError("Please enter valid listing quantity");
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, mEdtxtQty.getBottom()+70);
                }
            });
            //Toast.makeText(AddNewProduct.this, "Please enter valid listing quantity    ",Toast.LENGTH_LONG).show();
            return false;
        }

        if(medittxtDescAddProduct.getText().toString().trim().equalsIgnoreCase("")){
            medittxtDescAddProduct.setError( "Please enter listing description");
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, medittxtDescAddProduct.getTop());
                }
            });
            return false;

        }
        if(!mRdBtnService.isChecked() && medittxtAddProductPrice.getText().toString().trim().equalsIgnoreCase("")){
            medittxtAddProductPrice.setError( "Please enter listing price    ");
            //Toast.makeText(ActivityEditProduct.this, "",Toast.LENGTH_LONG).show();
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, medittxtAddProductPrice.getTop());
                }
            });
            return false;
        }
        float listingPrice=0;
        try{
            listingPrice = Float.parseFloat(medittxtAddProductPrice.getText().toString().trim());
        }catch (Exception e){

        }

        if(!mRdBtnService.isChecked() && listingPrice<1){
            medittxtAddProductPrice.setError("Price must not be less than $1");
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, medittxtAddProductPrice.getTop());
                }
            });
            // Toast.makeText(AddNewProduct.this, "Please enter listing price    ",Toast.LENGTH_LONG).show();
            return false;
        }



        if(PreferenceHandler.readString(ActivityEditProduct.this,
                PreferenceHandler.USER_TYPE,"").equalsIgnoreCase("1")){
            Float flt = Float.parseFloat(medittxtAddProductPrice.getText().toString());
            if(flt>2000){
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setMessage("With an individual account you can post a listing with maximum value of $2000")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        }

        if(mEdtxtAddress.getText().toString().trim().equalsIgnoreCase("")){
            mEdtxtAddress.setError("Please enter listing address    ");
            //Toast.makeText(ActivityEditProduct.this, "Please enter listing address    ",Toast.LENGTH_LONG).show();
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, mEdtxtAddress.getTop());
                }
            });
            return false;
        }

        float referralAmount=0;
        try{
            referralAmount = Float.parseFloat(mEdtxtRewardPoint.getText().toString().trim());
        }catch (Exception e){

        }


//        if(referralAmount<1){
//            mEdtxtRewardPoint.setError("Referral amount must not be less than $1");
//            mEdtxtRewardPoint.post(new Runnable() {
//                @Override
//                public void run() {
//                    mEdtxtRewardPoint.scrollTo(30, mEdtxtRewardPoint.getTop());
//                }
//            });
//            //Toast.makeText(AddNewProduct.this, "Please enter referral amount    ",Toast.LENGTH_LONG).show();
//            return false;
//        }




//        if(mEdtxtRewardPoint.getText().toString().trim().equalsIgnoreCase("")){
//            mEdtxtRewardPoint.setError("Please enter listing referral amount.    ");
//
//            mScrlVwWrappe.post(new Runnable() {
//                @Override
//                public void run() {
//                    mScrlVwWrappe.scrollTo(0, mEdtxtRewardPoint.getTop());
//                }
//            });
//            return false;
//        }


        if(mspinnerCategoryAddProduct.getSelectedItemPosition()==0){
            Toast.makeText(ActivityEditProduct.this, "Please select a category",Toast.LENGTH_SHORT);
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, mspinnerCategoryAddProduct.getBottom());
                }
            });
            return false;
        }if(spnrSubCategory.getSelectedItemPosition()==0){
            Toast.makeText(ActivityEditProduct.this, "Please select a sub category",Toast.LENGTH_SHORT);
            mScrlVwWrappe.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrappe.scrollTo(0, spnrSubCategory.getBottom());
                }
            });
            return false;
        }

        /*
        if(mRdBtnServiceZIP.isChecked()){
            if(RippleittAppInstance.getInstance().getServiceZIPCodesExisting().keySet().size()==0 &&
            RippleittAppInstance.getInstance().getServiceZIPCodesNew().keySet().size()==0
                    ){


                return true;
            }
        }
        */

        try{
            int qytModified= Integer.parseInt(mEdtxtQty.getText().toString().trim());
            if(RippleittAppInstance
                    .getInstance()
                    .getCURRENT_LISTING_OBJECT().getIs_live()
                    .equalsIgnoreCase   ("1")&& (qytModified>INITIAL_QUANTITY)){
                Toast.makeText(ActivityEditProduct.this, "Listing quantity cannot be increased",Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch (Exception e){

        }

        return true;
    }

    private void refreshServiceZipCodes(){
        mLnLtZipContainer.removeAllViews();
        for(String zip:RippleittAppInstance.getInstance().getServiceZIPCodesExisting().keySet()){
            final com.robertlevonyan.views.chip.Chip chip = new com.robertlevonyan.views.chip.Chip(ActivityEditProduct.this);
            chip.setChipText(zip);
            chip.setClosable(true);
            LinearLayout
                    .LayoutParams params =   new LinearLayout
                    .LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            chip.setLayoutParams(params);

            chip.setOnCloseClickListener(new OnCloseClickListener() {
                @Override
                public void onCloseClick(View v) {
                    RippleittAppInstance.getInstance().getServiceZIPCodesExisting().remove(chip.getChipText());
                    RippleittAppInstance.getInstance().getServiceZIPCodesDeleted().put(chip.getChipText(),chip.getChipText());
                    refreshServiceZipCodes();
                }
            });
            mLnLtZipContainer.addView(chip);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (i == R.id.rdBtnProduct) {
            RadioButton rdBtnShipping = (RadioButton) findViewById(R.id.rdBtnShippingServiceAreas);
            rdBtnShipping.setText("Shipping");
            linearProductType.setVisibility(View.VISIBLE);
            mEdtxtQty.setVisibility(View.VISIBLE);
            medittxtAddProductPrice.setVisibility(View.VISIBLE);
            linearBuyingOptions.setVisibility(View.VISIBLE);
            linearShippingMethods.setVisibility(View.VISIBLE);
        }
        if (i == R.id.rdBtnService) {
            RadioButton rdBtnShipping = (RadioButton) findViewById(R.id.rdBtnShippingServiceAreas);
            rdBtnShipping.setText("Service Areas");
            linearProductType.setVisibility(View.GONE);
            mEdtxtQty.setVisibility(View.GONE);
            medittxtAddProductPrice.setVisibility(View.GONE);
            linearBuyingOptions.setVisibility(View.GONE);
            linearShippingMethods.setVisibility(View.GONE); }
        fetchCategories();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 13:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    chooseFromGallery();
                } else {
                    Toast.makeText(ActivityEditProduct.this,"Read photo permission denied",Toast.LENGTH_SHORT).show();;
                }
                break;


            case REQUEST_IMAGE_CAPTURE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchCamera();
                } else {
                    Toast.makeText(ActivityEditProduct.this,
                            "Camera permission denied", Toast.LENGTH_SHORT).show();
                    ;
                }
                break;
            default:
                break;
        }
    }



    private class SaveBitmapAsync extends AsyncTask<Void,Void,String> {



        Bitmap bitMap;
        boolean didCompressAndCopy=false;

        private SaveBitmapAsync(Bitmap fileToSave){
            bitMap=fileToSave;
            //pdialog= new ProgressDialog(SignUpActivity.this);
        }

        @Override
        public void onPreExecute(){
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Saving file");

        }

        @Override
        protected String doInBackground(Void... voids) {
            String filePath="";
            try{
                String filename = "SKU_PIC_" + System.currentTimeMillis() + ".jpg";
                //fileDestination = new File(dirPath, filename);
                File file = new File(ActivityEditProduct.this.getCacheDir(),filename);
                FileOutputStream fOut = new FileOutputStream(file);
                bitMap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
                filePath=file.getAbsolutePath();


            }catch (Exception e){
                didCompressAndCopy=false;
                e.printStackTrace();
            }
            return filePath;
        }

        @Override
        public void onPostExecute(String v){
            mProgressDialog.dismiss();
            Log.e(v,v);
            RippleittAppInstance.getInstance()
                    .getListingImages().put(v,v);
            populateProductImages();





        }
    }


}
