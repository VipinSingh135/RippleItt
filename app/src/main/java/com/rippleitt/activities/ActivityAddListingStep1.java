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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.HorizontalReyclerViewAdapter;
import com.rippleitt.callback.RemovePicCallback;
import com.rippleitt.commonUtilities.ImageFilePath;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.commonUtilities.SubmitListingAsync;
import com.rippleitt.commonUtilities.UpdateListingAsync;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.ListingSyncProcessCallback;
import com.rippleitt.modals.ListingSyncResponseTemplate;
import com.rippleitt.modals.ListingSyncTemplate;
import com.rippleitt.modals.SubCategoryListTemplate;
import com.rippleitt.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import id.zelory.compressor.Compressor;

public class ActivityAddListingStep1 extends AppCompatActivity implements View.OnClickListener, RemovePicCallback,ListingSyncProcessCallback {


    private EditText medittxtAddProductName;
    private EditText medittxtDescAddProduct;
    private RelativeLayout mrelAddProduct_back;
    private RelativeLayout mrelUploadProductImages;
    private Spinner mspinnerCategoryAddProduct;

    private Button mbtnAddProdSaveDraft;
    private Button mbtnAddPRoductPublish;
    private  RecyclerView recyclerViewExistingPics, recyclerViewNewPics;
    private Spinner spnrSubCategory;
    private RelativeLayout mRelLytSubCategoryContainer;
    private int MODE_DRAFT = 1;
//    private RadioButton mRdBtnProduct,
//            mRdBtnService,
//            mRdBtnNewProduct,
//            mRdBtnUsedProduct;

    private LinearLayout linlytProducts,linlytServices;
    private TextView tvProducts,tvServices;
    private LinearLayout linlytNew,linlytUsed;
    private TextView tvNew,tvUsed;
    private ImageView imgProducts, imgServices, imgNew,  imgUsed;
    private boolean isProduct=true,isNew=true;
    private LinearLayout linearProductType;
    private LinearLayout mLinLytAddedPics;
    public static final int PICK_IMAGE = 1;
    private static int RESULT_LOAD_IMG = 1;
    ArrayAdapter<String> spinnerCategoryArr;
    ArrayAdapter<String> spinnerUnitArr;
    ArrayList<String> arry_imges;
    private boolean spnr_cat_init = false;
    private HorizontalReyclerViewAdapter horizontalAdapter;
    private final int ACTION_READ_EXTERNAL_STORAGE = 13;

//    private RadioGroup mRdGrpListingType;
    private ProgressDialog mProgressDialog;
    private Compressor mCompressor;
    private boolean shouldPresetSubCategory=false;

    /// objects for location management....
    private ProgressDialog mPDialog;
    private final int REQUEST_IMAGE_CAPTURE = 21;
    private final int CAMERA_ACTION_IMAGE_CAPTURE = 19;
    private ScrollView mScrlVwWrapper;
    private final int ACTION_WRITE_EXTERNAL_STORAGE = 20;

    private String dirPath = "";

    String spinnerCategoryItem[] = {"Category", "Item1", "Item2", "Item3"};
    String spinnerUnitItem[] = {"Unit", "Item1", "Item2", "Item3"};

    ListingSyncTemplate listingTemplate = new ListingSyncTemplate();

    private LinearLayout mLinLytExistingPics;
    private TextView mTxtvwLableExistingPics, mTxtVwLableAddedPics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing_step1);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mCompressor = new Compressor(ActivityAddListingStep1.this);
        mCompressor.setQuality(25);
        RippleittAppInstance.getInstance().getListingImages().clear();
        init();
        mPDialog = new ProgressDialog(ActivityAddListingStep1.this);
        fetchCategories();
        RippleittAppInstance.getInstance().getServiceZIPCodes().clear();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACTION_WRITE_EXTERNAL_STORAGE);
        } else {
            dirPath = com.rippleitt.commonUtilities.CommonUtils.createMediaDir();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (RippleittAppInstance.getInstance().getListingImages().size() == 0) {
            mLinLytAddedPics.setVisibility(View.GONE);
        } else {
            mLinLytAddedPics.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onPause() {
        super.onPause();

        mProgressDialog.dismiss();
    }

    public void init() {
        mScrlVwWrapper = (ScrollView) findViewById(R.id.scrlvwWrapper);
        linearProductType = (LinearLayout) findViewById(R.id.linearProductType);
//        mRdBtnProduct = (RadioButton) findViewById(R.id.rdBtnProduct);
//        mRdBtnService = (RadioButton) findViewById(R.id.rdBtnService);
//        mRdBtnNewProduct = (RadioButton) findViewById(R.id.rdBtnNewProduct);
//        mRdBtnUsedProduct = (RadioButton) findViewById(R.id.rdBtnUsedProduct);

        linlytNew= findViewById(R.id.linlytNew);
        linlytUsed= findViewById(R.id.linlytUsed);
        tvUsed= findViewById(R.id.tvUsed);
        tvNew= findViewById(R.id.tvNew);
        imgNew= findViewById(R.id.imgNew);
        imgUsed= findViewById(R.id.imgUsed);
        imgProducts= findViewById(R.id.imgProducts);
        imgServices= findViewById(R.id.imgServices);
        linlytProducts= findViewById(R.id.linlytProducts);
        linlytServices= findViewById(R.id.linlytServices);
        tvProducts= findViewById(R.id.tvProducts);
        tvServices= findViewById(R.id.tvServices);

        medittxtDescAddProduct = (EditText) findViewById(R.id.edittxtDescAddProduct);
        medittxtAddProductName = (EditText) findViewById(R.id.edittxtAddProductName);
        mLinLytAddedPics = (LinearLayout) findViewById(R.id.linlyImageContainer);
        mrelAddProduct_back = (RelativeLayout) findViewById(R.id.relAddProduct_back);
        mrelUploadProductImages = (RelativeLayout) findViewById(R.id.relUploadProductImages);
        mspinnerCategoryAddProduct = (Spinner) findViewById(R.id.spinnerCategoryAddProduct);
        mbtnAddProdSaveDraft = (Button) findViewById(R.id.btnAddProdSaveDraft);
        mbtnAddPRoductPublish = (Button) findViewById(R.id.btnAddPRoductPublish);
        recyclerViewNewPics = (RecyclerView) findViewById(R.id.recyclerProductMultiImages);
        recyclerViewExistingPics = (RecyclerView)findViewById(R.id.recyclerProductExistingMultiImages);
//        mRdGrpListingType = (RadioGroup) findViewById(R.id.radioGroupListingType);
        recyclerViewNewPics.addItemDecoration(new DividerItemDecoration(ActivityAddListingStep1.this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        spnrSubCategory = (Spinner) findViewById(R.id.spnrSubCategory);
        mRelLytSubCategoryContainer = (RelativeLayout) findViewById(R.id.relProductSubCategory);
        // mRelLytSubCategoryContainer.setVisibility(View.GONE);
        arry_imges = new ArrayList<>();
        mrelAddProduct_back.setOnClickListener(this);
        mbtnAddPRoductPublish.setOnClickListener(this);
        mrelUploadProductImages.setOnClickListener(this);
        mbtnAddProdSaveDraft.setOnClickListener(this);
        linlytNew.setOnClickListener(this);
        linlytUsed.setOnClickListener(this);
        linlytProducts.setOnClickListener(this);
        linlytServices.setOnClickListener(this);

        mTxtvwLableExistingPics=(TextView)findViewById(R.id.txtvwExistingPhotos);
        mTxtVwLableAddedPics=(TextView)findViewById(R.id.txtvwAddedPhotos);
        mLinLytExistingPics=(LinearLayout)findViewById(R.id.linlyExistingImageContainer);

        if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT()!=null){
            preloadListingData();
        }else{
            mTxtvwLableExistingPics.setVisibility(View.GONE);
            mLinLytExistingPics.setVisibility(View.GONE);
            mTxtVwLableAddedPics.setVisibility(View.GONE);
//            mLinLytExistingPics.setVisibility(View.GONE);
        }
        work();
        initSpinnerListners();

    }

    @Override
    public void onClick(View view) {


        if (view == mrelAddProduct_back) {
            RippleittAppInstance.getInstance().getListingImages().clear();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }

        if (view == mrelUploadProductImages) {

            //addAttachments();
            showPhotoOptions();

        }
        if (view == mbtnAddPRoductPublish) {
            if (validate()) {
                MODE_DRAFT = 1;
                publishListing(1);
            }
        }

        if (view == mbtnAddProdSaveDraft) {
            if (validate()) {
                MODE_DRAFT = 2;
                publishListing(1);
            }
        }

        if (view == linlytNew) {
            linlytNew.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytUsed.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            isNew=true;
            imgNew.setImageResource(R.drawable.white_cicle);
            imgUsed.setImageResource(R.drawable.light_blue_circle);
            tvNew.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvUsed.setTextColor(getResources().getColor(R.color.grey2));
        }
        if (view == linlytUsed) {
            linlytUsed.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytNew.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            isNew=false;
            imgUsed.setImageResource(R.drawable.white_cicle);
            imgNew.setImageResource(R.drawable.light_blue_circle);
            tvUsed.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvNew.setTextColor(getResources().getColor(R.color.grey2));
        }
        if (view == linlytProducts) {
            if(!isProduct){
                isProduct=true;
                fetchCategories();
            }
            linearProductType.setVisibility(View.VISIBLE);
            linlytProducts.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytServices.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            tvProducts.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvServices.setTextColor(getResources().getColor(R.color.grey2));
            imgProducts.setImageResource(R.drawable.white_cicle);
            imgServices.setImageResource(R.drawable.light_blue_circle);

        }
        if (view == linlytServices) {
            if(isProduct){
                isProduct=false;
                fetchCategories();
            }
            linearProductType.setVisibility(View.GONE);
            linlytProducts.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            linlytServices.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            tvServices.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvProducts.setTextColor(getResources().getColor(R.color.grey2));
            imgServices.setImageResource(R.drawable.white_cicle);
            imgProducts.setImageResource(R.drawable.light_blue_circle);
        }

    }

    private void publishListing(int flag) {
        listingTemplate.setProductname(medittxtAddProductName.getText().toString());
        listingTemplate.setCategory(RippleittAppInstance
                .getInstance()
                .getCategoryId(mspinnerCategoryAddProduct
                        .getSelectedItem().toString()));
        listingTemplate.setSub_category(RippleittAppInstance.getInstance()
                .getSubCategoryId(spnrSubCategory
                        .getSelectedItem().toString()));
        listingTemplate.setDescription(medittxtDescAddProduct.getText().toString());
//        listingTemplate.setRewardAmount(mEdtxtRewardPoint.getText().toString());
        listingTemplate.setRewardAmount("0");

//        listingTemplate.setPayment_mode("1");
        if (isNew) {
            listingTemplate.setIs_new("1");
        } else {
            listingTemplate.setIs_new("0");
        }

        if (!isProduct) {
            listingTemplate.setListing_type("2");
            listingTemplate.setIs_new("1");
//            listingTemplate.setQuantity("10");
//            listingTemplate.setPrice("10");
        }

        if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT()!=null){
            listingTemplate.setListingID(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getListing_id());
            RippleittAppInstance.getInstance().setCURRENT_SELECTED_LISTING_ID(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getListing_id());
            new UpdateListingAsync(this, this, listingTemplate, flag).execute();
        }else {
            new SubmitListingAsync(this, this, listingTemplate, flag).execute();
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
        if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT()==null) {
            if (RippleittAppInstance.getInstance().getListingImages().keySet().size() == 0) {
                Toast.makeText(ActivityAddListingStep1.this,
                        "Please add a listing image", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (medittxtAddProductName.getText().toString().trim().equalsIgnoreCase("")) {

            medittxtAddProductName.setError("Please enter listing title");
            mScrlVwWrapper.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrapper.scrollTo(0, medittxtAddProductName.getTop() - 20);
                }
            });
            // Toast.makeText(AddListingStep1.this, "Please enter product name",Toast.LENGTH_LONG).show();
            return false;
        }

        if ((RippleittAppInstance
                .getInstance()
                .getCategoryId(mspinnerCategoryAddProduct
                        .getSelectedItem().toString())
                .equalsIgnoreCase("0")) || (RippleittAppInstance
                .getInstance()
                .getCategoryId(mspinnerCategoryAddProduct
                        .getSelectedItem().toString())
                .equalsIgnoreCase(""))) {
            Toast.makeText(ActivityAddListingStep1.this, "Please select listing category", Toast.LENGTH_LONG).show();
            mScrlVwWrapper.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrapper.scrollTo(30, mspinnerCategoryAddProduct.getBottom());
                }
            });
            return false;
        }
        if (RippleittAppInstance
                .getInstance()
                .getSubCategoryId(spnrSubCategory
                        .getSelectedItem().toString())
                .equalsIgnoreCase("0")
                || (RippleittAppInstance
                .getInstance()
                .getSubCategoryId(spnrSubCategory
                        .getSelectedItem().toString())
                .equalsIgnoreCase(""))) {
            Toast.makeText(ActivityAddListingStep1.this, "Please select listing sub category", Toast.LENGTH_LONG).show();
            mScrlVwWrapper.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrapper.scrollTo(30, spnrSubCategory.getBottom());
                }
            });

            return false;
        }


        if (medittxtDescAddProduct.getText().toString().trim().equalsIgnoreCase("")) {
            medittxtDescAddProduct.setError("Please enter listing description");
            mScrlVwWrapper.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrapper.scrollTo(0, medittxtDescAddProduct.getBottom() + 100);
                }
            });
            // Toast.makeText(AddListingStep1.this, "Please enter listing description    ",Toast.LENGTH_LONG).show();
            return false;
        }


        if (mspinnerCategoryAddProduct.getSelectedItemPosition() == 0) {
            Toast.makeText(ActivityAddListingStep1.this, "Please select a category", Toast.LENGTH_SHORT);
            return false;
        }
        if (spnrSubCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(ActivityAddListingStep1.this, "Please select a sub category", Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    private void addAttachments() {


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, ACTION_READ_EXTERNAL_STORAGE);
        } else {
            chooseFromGallery();


            //startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + mTxtVwListingContactPhone.getText())));
        }
    }

    public void work() {
        //==================spinner_category===============
        spinnerCategoryArr = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, spinnerCategoryItem);
        spinnerCategoryArr.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        mspinnerCategoryAddProduct.setAdapter(spinnerCategoryArr);

        //==================spinner_category===============
        spinnerUnitArr = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, spinnerUnitItem);
        spinnerUnitArr.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

    }

    private void chooseFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG) {
                if (resultCode == RESULT_OK
                        && null != data) {
                    if (data.getClipData() != null) {
                        int imageCount = data.getClipData().getItemCount();
                        if (imageCount > 5) imageCount = 5;
                        for (int index = 0; index < imageCount; index++) {
                            // if the hashmap does not contain the image already we add it
                            // to avoid duplicate images beinf added.
                            File compresedFile = mCompressor.compressToFile(new File(ImageFilePath
                                    .getPath(ActivityAddListingStep1.this,
                                            data.getClipData().getItemAt(index).getUri())));

                            if (!RippleittAppInstance.getInstance().getListingImages().containsKey(compresedFile.getAbsolutePath())) {
                                RippleittAppInstance.getInstance()
                                        .getListingImages().put(compresedFile.getAbsolutePath(), compresedFile.getAbsolutePath());
                            }

                        /*
                        if(!RippleittAppInstance.getInstance().getListingImages()
                                .containsKey(data.getClipData().getItemAt(index).getUri())){
                            RippleittAppInstance.getInstance().getListingImages().put(
                                    ImageFilePath.getPath(AddListingStep1.this,
                                            data.getClipData().getItemAt(index).getUri()),
                                    data.getClipData().getItemAt(index).getUri());
                        }
                        */
                        }

                    } else {

                        File compresedFile = mCompressor.compressToFile(new File(ImageFilePath
                                .getPath(ActivityAddListingStep1.this,
                                        data.getData())));

                        if (!RippleittAppInstance.getInstance().getListingImages().containsKey(compresedFile.getAbsolutePath())) {
                            RippleittAppInstance.getInstance()
                                    .getListingImages().put(compresedFile.getAbsolutePath(), compresedFile.getAbsolutePath());
                        }
                    /*
                    if(!RippleittAppInstance.getInstance().getListingImages()
                            .containsKey(data.getData())){
                        RippleittAppInstance.getInstance().getListingImages().put(
                                ImageFilePath.getPath(AddListingStep1.this,
                                        data.getData()),
                                data.getData());
                    }
                    */

                    }

                } else {
                    if (requestCode == RESULT_LOAD_IMG) {
                        Toast.makeText(this, "You haven't picked Image",
                                Toast.LENGTH_LONG).show();
                    }

                }

                populateProductImages();

            }
            if (requestCode == CAMERA_ACTION_IMAGE_CAPTURE) {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    // mImageView.setImageBitmap(imageBitmap);
                    if (imageBitmap != null) {
                        new ActivityAddListingStep1.SaveBitmapAsync(imageBitmap).execute();
                    }

                } else {
                    Toast.makeText(this, "You haven't taken any Image",
                            Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 13:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    chooseFromGallery();
                } else {
                    Toast.makeText(ActivityAddListingStep1.this, "Read photo permission denied", Toast.LENGTH_SHORT).show();
                    ;
                }
                break;
            case ACTION_WRITE_EXTERNAL_STORAGE:

                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    dirPath = com.rippleitt.commonUtilities.CommonUtils.createMediaDir();
                } else {
                    Toast.makeText(ActivityAddListingStep1.this,
                            "write permission denied, unable to launch camera", Toast.LENGTH_SHORT).show();
                    ;
                }

                break;
            case REQUEST_IMAGE_CAPTURE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchCamera();
                } else {
                    Toast.makeText(ActivityAddListingStep1.this,
                            "Camera permission denied", Toast.LENGTH_SHORT).show();
                    ;
                }
                break;
            default:
                break;
        }
    }


    private void showPhotoOptions() {
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

    private void launchCamera() {

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

    private void fetchCategories() {
        mProgressDialog.setMessage("Getting Categories");
        mProgressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getFETCH_CATEGORIES(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate) gson.fromJson(response, CategoryListTemplate.class);
                        if (response_.getResponse_code() == 1) {
                            RippleittAppInstance
                                    .getInstance()
                                    .setCategories(response_.getData());
                            ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(ActivityAddListingStep1.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    RippleittAppInstance.getInstance().getCategories());
                            mspinnerCategoryAddProduct.setAdapter(karant_adapter);
                            // mLinLytRegion.setVisibility(View.VISIBLE);
                            if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT()!=null){
                                preselectCategory();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid keywords", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ActivityAddListingStep1.this, "could not fetch categories, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityAddListingStep1.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                if (!isProduct) {
                    params.put("type", "2");
                } else {
                    params.put("type", "1");
                }

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }

    private void performVolleyFetchSubCategories() {
        mProgressDialog.setMessage("Getting Sub Categories");
        mProgressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance
                .getInstance().getFETCH_SUBCATEGORIES(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        SubCategoryListTemplate response_ = (SubCategoryListTemplate) gson.fromJson(response, SubCategoryListTemplate.class);
                        if (response_.getResponse_code() == 1) {
                            RippleittAppInstance
                                    .getInstance()
                                    .setSubCategories(response_.getData());
                            ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(ActivityAddListingStep1.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    RippleittAppInstance.getInstance().getSubCategories());
                            spnrSubCategory.setAdapter(karant_adapter);
                            mRelLytSubCategoryContainer.setVisibility(View.VISIBLE);
                            // mLinLytRegion.setVisibility(View.VISIBLE);
                            if(shouldPresetSubCategory){
                                shouldPresetSubCategory=false;
                                preselectSubCategory();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid keywords", Toast.LENGTH_SHORT).show();
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(ActivityAddListingStep1.this, "could not fetch sub category, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityAddListingStep1.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("category_id", RippleittAppInstance
                        .getInstance()
                        .getCategoryId(mspinnerCategoryAddProduct.getSelectedItem().toString()));
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }

    private void initSpinnerListners() {
        mspinnerCategoryAddProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spnr_cat_init) {
                    if (i == 0) {
                        //
                        spnrSubCategory.setSelection(0);
                        return;

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

    private void populateProductImages() {
        mLinLytAddedPics.setVisibility(View.VISIBLE);
        Set<String> keys = RippleittAppInstance.getInstance().getListingImages().keySet();
        String[] keysArray = keys.toArray(new String[keys.size()]);
        // now we draw the horizontal slider

        recyclerViewNewPics.addItemDecoration(new DividerItemDecoration(ActivityAddListingStep1.this, LinearLayoutManager.HORIZONTAL));
        horizontalAdapter = new HorizontalReyclerViewAdapter(keysArray,
                getApplicationContext(),
                this, 1, 0);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ActivityAddListingStep1.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNewPics.setAdapter(horizontalAdapter);
        recyclerViewNewPics.setLayoutManager(horizontalLayoutManager);

    }

//    @Override
//    public void removePic(String absPath, int type, int positin) {
//        RippleittAppInstance.getInstance().getListingImages().remove(absPath);
//        if (RippleittAppInstance.getInstance().getListingImages().keySet().size() == 0) {
//            mLinLytAddedPics.setVisibility(View.GONE);
//        }
//        populateProductImages();
//    }

    @Override
    public void onInit() {
        //Toast.makeText(AddListingStep1.this,"sync-init",Toast.LENGTH_LONG).show();
        CommonUtils.showProgress(ActivityAddListingStep1.this, "Submitting your listing...");
    }

    @Override
    public void onError(ListingSyncResponseTemplate template) {
        CommonUtils.dismissProgress();
        Toast.makeText(ActivityAddListingStep1.this, "Could not complete your request", Toast.LENGTH_LONG).show();
        RippleittAppInstance.getInstance().getListingImages().clear();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onSuccess(ListingSyncResponseTemplate template) {
        CommonUtils.dismissProgress();
//        if (template.getDraft_flag().equalsIgnoreCase("0")) {
//            // listing was for publication..
//            if (template.getIs_card_available().equalsIgnoreCase("0")) {
////             card not available, so we ask the user to add a card....
//                addCardPrompt();
//            } else {
////                Toast.makeText(ActivityAddListingStep1.this,
////                        "Thank you for adding new listing!! It’s under review and will be posted within 24 hours.",
////                        Toast.LENGTH_LONG).show();
//                CommonUtils.dismissProgress();
//                RippleittAppInstance.getInstance().getListingImages().clear();
//                Intent returnIntent = new Intent();
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
//            }
//
//        } else {
        if (MODE_DRAFT==2) {
            Toast.makeText(ActivityAddListingStep1.this,
                    "Your draft has been saved",
                    Toast.LENGTH_LONG).show();
            CommonUtils.dismissProgress();
            RippleittAppInstance.getInstance().getListingImages().clear();

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }else{
            RippleittAppInstance.getInstance().getListingImages().clear();
            RippleittAppInstance.getInstance().setCURRENT_ADDED_LISTING_OBJECT(listingTemplate);
            if (RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT()!=null){
                RippleittAppInstance.getInstance().setCURRENT_SELECTED_LISTING_ID(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getListing_id());
            }else {
                RippleittAppInstance.getInstance().setCURRENT_SELECTED_LISTING_ID(template.getListing_id());
            }
            Intent returnIntent = new Intent(ActivityAddListingStep1.this,ActivityAddListingStep2.class);
            startActivity(returnIntent);
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

    @Override
    public void onImageCicked(String absPath, int type, int position, int displayMode) {
        if(displayMode==1){
            if(position!=RippleittAppInstance.getInstance().getCurrentTappedImageindex()){
                RippleittAppInstance.getInstance().setCurrentTappedImageindex(position);
                vlleyUpdateListingPrimaryImage(absPath);
            }
        }
    }

//    private void addCardPrompt() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Please add a credit card to your account in order to publish a listing.")
//                .setCancelable(false)
//                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        RippleittAppInstance.getInstance().setAddCardMode(1);
//                        startActivity(new Intent(ActivityAddListingStep1.this, AddPaymentMethod.class));
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

//    @Override
//    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//
//        if (i == R.id.rdBtnProduct) {
////            RadioButton rdBtnShipping = (RadioButton) findViewById(R.id.rdBtnShippingServiceAreas);
////            rdBtnShipping.setText("Shipping");
//            linearProductType.setVisibility(View.VISIBLE);
//        }
//        if (i == R.id.rdBtnService) {
////            RadioButton rdBtnShipping = (RadioButton) findViewById(R.id.rdBtnShippingServiceAreas);
////            rdBtnShipping.setText("Service Areas");
//            linearProductType.setVisibility(View.GONE);
//        }


//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class SaveBitmapAsync extends AsyncTask<Void, Void, String> {


        Bitmap bitMap;
        boolean didCompressAndCopy = false;

        private SaveBitmapAsync(Bitmap fileToSave) {
            bitMap = fileToSave;
            //pdialog= new ProgressDialog(SignUpActivity.this);
        }

        @Override
        public void onPreExecute() {
            mPDialog.setCancelable(false);
            mPDialog.setMessage("Saving file");

        }

        @Override
        protected String doInBackground(Void... voids) {
            String filePath = "";
            try {
                String filename = "SKU_PIC_" + System.currentTimeMillis() + ".jpg";
                //fileDestination = new File(dirPath, filename);
                File file = new File(ActivityAddListingStep1.this.getCacheDir(), filename);
                FileOutputStream fOut = new FileOutputStream(file);
                bitMap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
                filePath = file.getAbsolutePath();


            } catch (Exception e) {
                didCompressAndCopy = false;
                e.printStackTrace();
            }
            return filePath;
        }

        @Override
        public void onPostExecute(String v) {
            mPDialog.dismiss();
            Log.e(v, v);
            RippleittAppInstance.getInstance()
                    .getListingImages().put(v, v);
            populateProductImages();


        }
    }

    private void preselectCategory(){
        mspinnerCategoryAddProduct.setSelection(getIndex(mspinnerCategoryAddProduct,
                RippleittAppInstance
                        .getInstance()
                        .getCURRENT_LISTING_OBJECT().getCategory().getName()));
        shouldPresetSubCategory=true;
//        performVolleyFetchSubCategories();
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

    private void preloadListingData(){

        listingTemplate.setLatitude(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getLatitude());

        listingTemplate.setLongitude(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getLatitude());

        listingTemplate.setListing_type(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getListing_type());


        listingTemplate.setPrice(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getListing_price());
        listingTemplate.setAddress(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getLocation().getAddress());

        listingTemplate.setQuantity(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getQuantity());

        listingTemplate.setPayment_mode(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getPayment_mode());


        listingTemplate.setIs_new(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getIs_new());


        listingTemplate.setLatitude(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getDirect_buy());

//        if(RippleittAppInstance
//                .getInstance()
//                .getCURRENT_LISTING_OBJECT()
//                .getUnits().equalsIgnoreCase("Boxes")){
//            mSpnrnits.setSelection(0);
//        }

//        if(RippleittAppInstance
//                .getInstance()
//                .getCURRENT_LISTING_OBJECT()
//                .getUnits().equalsIgnoreCase("Packets")){
//            mSpnrnits.setSelection(1);
//        }
//        if(RippleittAppInstance
//                .getInstance()
//                .getCURRENT_LISTING_OBJECT()
//                .getUnits().equalsIgnoreCase("Pcs.")){
//            mSpnrnits.setSelection(2);
//        }
//        mSpnrnits.setEnabled(false);



        listingTemplate.setShippingType(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getShipping_type());

        medittxtAddProductName.setText(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getProductname());
        medittxtDescAddProduct.setText(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT().getListing_description());

        if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getIs_new().equalsIgnoreCase("1")){
//            mRdBtnNewProduct.setChecked(true);
//            mRdBtnUsedProduct.setChecked(false);
            linlytNew.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytUsed.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            isNew=true;
            imgNew.setImageResource(R.drawable.white_cicle);
            imgUsed.setImageResource(R.drawable.light_blue_circle);
            tvNew.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvUsed.setTextColor(getResources().getColor(R.color.grey2));
        }else{
            linlytUsed.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytNew.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            isNew=false;
            imgUsed.setImageResource(R.drawable.white_cicle);
            imgNew.setImageResource(R.drawable.light_blue_circle);
            tvUsed.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvNew.setTextColor(getResources().getColor(R.color.grey2));

//            mRdBtnNewProduct.setChecked(false);
//            mRdBtnUsedProduct.setChecked(true);

        }


        if(RippleittAppInstance
                .getInstance()
                .getCURRENT_LISTING_OBJECT()
                .getListing_type().equalsIgnoreCase("1")){
            isProduct=true;
            linearProductType.setVisibility(View.VISIBLE);
            linlytProducts.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytServices.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            tvProducts.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvServices.setTextColor(getResources().getColor(R.color.grey2));
            imgProducts.setImageResource(R.drawable.white_cicle);
            imgServices.setImageResource(R.drawable.light_blue_circle);

        }else{
//            isProduct=false;
//            mRdBtnProduct.setChecked(false);
//            mRdBtnService.setChecked(true);
            isProduct=false;
            linearProductType.setVisibility(View.GONE);
            linlytProducts.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            linlytServices.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            tvServices.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvProducts.setTextColor(getResources().getColor(R.color.grey2));
            imgServices.setImageResource(R.drawable.white_cicle);
            imgProducts.setImageResource(R.drawable.light_blue_circle);
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


    }

    // loads the existing images for this listing
    private void preloadExistingImage(){
        if(RippleittAppInstance.getInstance().getCURRENT_LISTING_OBJECT().getListing_photos().length==0){
            mTxtvwLableExistingPics.setText("This listing does not have any existing photos.");
            mLinLytExistingPics.setVisibility(View.GONE);
        }else{
            mTxtvwLableExistingPics.setText("Existing Photos");
            mTxtVwLableAddedPics.setText("Added photos");
            mLinLytExistingPics.setVisibility(View.VISIBLE);

            String[] keysArray = RippleittAppInstance.getInstance().getCurrentListingExistingPics().keySet()
                    .toArray(new String[RippleittAppInstance.getInstance().getCurrentListingExistingPics().keySet().size()]);
            recyclerViewExistingPics.addItemDecoration(new DividerItemDecoration(ActivityAddListingStep1.this, LinearLayoutManager.HORIZONTAL));
            horizontalAdapter = new HorizontalReyclerViewAdapter(keysArray,
                    getApplicationContext(),this,2,1);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ActivityAddListingStep1.this,
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewExistingPics.setLayoutManager(horizontalLayoutManager);
            recyclerViewExistingPics.setAdapter(horizontalAdapter);


        }
    }


    @Override
    public void removePic(String absPath, int fileType,int position) {
        if(fileType==1){ // this is a server url..
            //delete listing pic here
            // remove pic from local hashmap..
            RippleittAppInstance.getInstance().getListingImages().remove(absPath);
            if (RippleittAppInstance.getInstance().getListingImages().keySet().size() == 0) {
                mLinLytAddedPics.setVisibility(View.GONE);
            }
            populateProductImages();
        }else{
            if(position==RippleittAppInstance.getInstance().getCurrentTappedImageindex()){
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
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
    private void volleyDeletePic(final String picPath){
        mProgressDialog.setMessage("Deleting listing image");
        mProgressDialog.show();
        //Toast.makeText(ActivityAddListingStep1.this, "deleting pic",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ActivityAddListingStep1.this,
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
                Toast.makeText(ActivityAddListingStep1.this,"could not delete the photo, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityAddListingStep1.this,
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
        //Toast.makeText(ActivityAddListingStep1.this, "deleting pic",Toast.LENGTH_LONG).show();
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
                Toast.makeText(ActivityAddListingStep1.this,"could not update primary photo, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityAddListingStep1.this,
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


}
