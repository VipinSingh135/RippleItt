package com.rippleitt.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.hootsuite.nachos.chip.Chip;
import com.rippleitt.R;
import com.rippleitt.adapters.HorizontalReyclerViewAdapter;
import com.rippleitt.adapters.ProductMultipleImagesAdapter;
import com.rippleitt.callback.RemovePicCallback;
import com.rippleitt.commonUtilities.ImageFilePath;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.commonUtilities.SubmitListingAsync;
import com.rippleitt.commonUtilities.UpdateListingAsync;
import com.rippleitt.controller.CustomTextWatcher;
import com.rippleitt.controller.FetchAddressIntentService;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.Constants;
import com.rippleitt.modals.ListingSyncProcessCallback;
import com.rippleitt.modals.ListingSyncResponseTemplate;
import com.rippleitt.modals.ListingSyncTemplate;
import com.rippleitt.modals.SubCategoryListTemplate;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.AddProductListingApi;
import com.robertlevonyan.views.chip.OnCloseClickListener;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import id.zelory.compressor.Compressor;

public class AddNewProduct extends AppCompatActivity
        implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        RemovePicCallback, ListingSyncProcessCallback, RadioGroup.OnCheckedChangeListener {

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
    private final int MODE_DRAFT = 1;
    private final int MODE_PUBLISH = 0;
    private RadioButton mRdBtnProduct,
            mRdBtnService,
            mRdBtnBuyNow,
            mRdBtnMakeOffer,
            mRdBtnBoth,
            mRdBtnPaymentModeCash,
            mRdBtnNewProduct,
            mRdBtnUsedProduct,
            mRdBtnPaymentModeRipple;
    private Spinner mSpnrnits;
    private LinearLayout linearProductType;
    private EditText mEdtxtQty;
    private CheckBox mChkBxTC;
    private TextView mTxtVwTc;
    private LinearLayout mLinLytRecyclerContainer;
    private EditText mEdtxtAddress;
    private EditText mEdtxtRewardPoint;
    private final int PLACE_PICKER_REQUEST = 12;
    String spinnerCategoryItem[] = {"Category", "Item1", "Item2", "Item3"};
    String spinnerUnitItem[] = {"Unit", "Item1", "Item2", "Item3"};
    public static final int PICK_IMAGE = 1;
    private static int RESULT_LOAD_IMG = 1;
    private double latitude__, longitude__;
    ArrayAdapter<String> spinnerCategoryArr;
    ArrayAdapter<String> spinnerUnitArr;
    ArrayList<String> arry_imges;
    private boolean spnr_cat_init = false;
    private HorizontalReyclerViewAdapter horizontalAdapter;
    private final int ACTION_READ_EXTERNAL_STORAGE = 13;

    private final int ACTION_ACCESS_LOCATION_ = 14;
    private RadioGroup mRdGrpListingType;
    private ProgressDialog mProgressDialog;
    private Compressor mCompressor;

    /// objects for location management....
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mRequestingLocationUpdates = true;
    private LocationCallback mLocationCallback;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private final int ACTION_WRITE_EXTERNAL_STORAGE = 20;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private final int ACTION_ACCESS_FINE_LOCATION = 18;
    private Location mCurrentLocation;
    private AddressResultReceiver mResultReceiver;
    private RadioButton mRdBtnServicePickUp, mRdBtnServiceZIP;
    private EditText mEdtxtZipCode;
    private ImageView mImgVwAddZipCode;
    private HorizontalScrollView mHrscrlVwZipContainer;
    private LinearLayout mLnLtZipContainer, linearBuyingOptions, linearShippingMethods;
    private ProgressDialog mPDialog;
    LocationListener locationListenerReference;
    LocationManager lManagerInstance;
    private double poledLatitude = 0, poledLongotude = 0;
    private String dirPath = "";
    private final int REQUEST_IMAGE_CAPTURE = 21;
    private final int CAMERA_ACTION_IMAGE_CAPTURE = 19;
    private ScrollView mScrlVwWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mCompressor = new Compressor(AddNewProduct.this);
        mCompressor.setQuality(25);
        RippleittAppInstance.getInstance().getListingImages().clear();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        init();
        mPDialog = new ProgressDialog(AddNewProduct.this);
        fetchCategories();
        RippleittAppInstance.getInstance().getServiceZIPCodes().clear();
        //createLocationCallback();
        //createLocationRequest();
        //buildLocationSettingsRequest();
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACTION_WRITE_EXTERNAL_STORAGE);
        } else {
            dirPath = com.rippleitt.commonUtilities.CommonUtils.createMediaDir();
        }
        captureUserLocation();
    }


    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationRequest.setMaxWaitTime(5000);

    }


    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    private void captureUserLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACTION_ACCESS_FINE_LOCATION);
        } else {
            doLocationStuff();
        }
    }


    private void doLocationStuff() {
        // mPDialog = new ProgressDialog(AddNewProduct.this);
        mPDialog.setMessage("Please wait...");
        mPDialog.show();
        final LocationTimeoutAsync locationAsync = new LocationTimeoutAsync();
        locationAsync.execute();

        //CommonUtils.showProgress(SignUpActivity.this, "Creating your account...");
        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lManagerInstance = locationManager;
// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                if (location != null) {
                    System.out.print(location);
                    locationManager.removeUpdates(this);
                    locationListenerReference = this;
                    List<Address> addresses = new ArrayList<Address>();
                    try {
                        Geocoder geocoder;
                        poledLatitude = location.getLatitude();
                        poledLongotude = location.getLongitude();
                        geocoder = new Geocoder(AddNewProduct.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        mEdtxtAddress.setText(addresses.get(0).getAddressLine(0));
                        mPDialog.dismiss();
                        locationAsync.cancel(true);
                    } catch (Exception e) {
                        locationAsync.cancel(true);
                        mEdtxtAddress.setText("Unknown street");
                        mPDialog.dismiss();
                    }
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e(provider, "");
            }

            public void onProviderEnabled(String provider) {
                Log.e(provider, "");
            }

            public void onProviderDisabled(String provider) {
                Log.e(provider, "");
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, locationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (RippleittAppInstance.getInstance().getListingImages().size() == 0) {
            mLinLytRecyclerContainer.setVisibility(View.GONE);
        } else {
            mLinLytRecyclerContainer.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        mProgressDialog.dismiss();
    }

    public void init() {
        mScrlVwWrapper = (ScrollView) findViewById(R.id.scrlvwWrapper);
        mRdBtnServiceZIP = (RadioButton) findViewById(R.id.rdBtnShippingServiceAreas);
        mRdBtnServicePickUp = (RadioButton) findViewById(R.id.rdBtnShippingPickup);
        mRdBtnServiceZIP.setOnClickListener(this);
        mRdBtnServicePickUp.setOnClickListener(this);
        mEdtxtZipCode = (EditText) findViewById(R.id.edtxtZipCode);
        mImgVwAddZipCode = (ImageView) findViewById(R.id.imgvwAddZipCode);
        mImgVwAddZipCode.setOnClickListener(this);
        mHrscrlVwZipContainer = (HorizontalScrollView) findViewById(R.id.hrscrlZipCodeContailer);
        linearProductType = (LinearLayout) findViewById(R.id.linearProductType);
        linearShippingMethods = findViewById(R.id.linearShippingMethods);
        linearBuyingOptions = findViewById(R.id.linearBuyingOptions);
        mLnLtZipContainer = (LinearLayout) findViewById(R.id.linlytZipCodeContainer);
        mChkBxTC = (CheckBox) findViewById(R.id.chkbxTerms);
        mTxtVwTc = (TextView) findViewById(R.id.txtvwTCLauncher);
        mTxtVwTc.setText(Html.fromHtml("<u>Rippleitt Terms & Conditions</u>"));
        mTxtVwTc.setOnClickListener(this);
        mRdBtnProduct = (RadioButton) findViewById(R.id.rdBtnProduct);
        mRdBtnService = (RadioButton) findViewById(R.id.rdBtnService);
        mRdBtnPaymentModeCash = (RadioButton) findViewById(R.id.rdBtnPaymentModecCash);
        mRdBtnNewProduct = (RadioButton) findViewById(R.id.rdBtnNewProduct);
        mRdBtnUsedProduct = (RadioButton) findViewById(R.id.rdBtnUsedProduct);
        mRdBtnPaymentModeRipple = (RadioButton) findViewById(R.id.rdBtnPaymentModeRipple);

        mRdBtnMakeOffer = (RadioButton) findViewById(R.id.rdBtnMakeOffer);
        mRdBtnBuyNow = (RadioButton) findViewById(R.id.rdBtnBuyNow);
        mRdBtnBoth = (RadioButton) findViewById(R.id.rdBtnBoth);
        mSpnrnits = (Spinner) findViewById(R.id.spnrUnits);
        mEdtxtQty = (EditText) findViewById(R.id.edittxtAddProductQty);
        medittxtDescAddProduct = (EditText) findViewById(R.id.edittxtDescAddProduct);
        medittxtAddProductName = (EditText) findViewById(R.id.edittxtAddProductName);
        medittxtAddProductPrice = (EditText) findViewById(R.id.edittxtAddProductPrice);
        mLinLytRecyclerContainer = (LinearLayout) findViewById(R.id.linlyImageContainer);
        mrelAddProduct_back = (RelativeLayout) findViewById(R.id.relAddProduct_back);
        mrelUploadProductImages = (RelativeLayout) findViewById(R.id.relUploadProductImages);
        mspinnerCategoryAddProduct = (Spinner) findViewById(R.id.spinnerCategoryAddProduct);
        mEdtxtAddress = (EditText) findViewById(R.id.edtxtAddress);
        mEdtxtAddress.setFocusable(false);
        mEdtxtAddress.setClickable(true);
        mEdtxtAddress.setOnClickListener(this);
        mEdtxtRewardPoint = (EditText) findViewById(R.id.edtxtReferralAmount);
        mbtnAddProdSaveDraft = (Button) findViewById(R.id.btnAddProdSaveDraft);
        mbtnAddPRoductPublish = (Button) findViewById(R.id.btnAddPRoductPublish);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerProductMultiImages);
        mRdGrpListingType = (RadioGroup) findViewById(R.id.radioGroupListingType);
        mRdGrpListingType.setOnCheckedChangeListener(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(AddNewProduct.this, LinearLayoutManager.VERTICAL) {
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
        medittxtAddProductPrice.addTextChangedListener(new CustomTextWatcher(
                medittxtAddProductPrice));
        mEdtxtRewardPoint.addTextChangedListener(new CustomTextWatcher(
                mEdtxtRewardPoint));
        mChkBxTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mChkBxTC.isChecked()) mChkBxTC.setError(null);
            }
        });
        mHrscrlVwZipContainer.setVisibility(View.GONE);
        mEdtxtZipCode.setVisibility(View.GONE);
        mImgVwAddZipCode.setVisibility(View.GONE);
        work();
        initSpinnerListners();

        mRdBtnBoth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRdBtnBuyNow.setChecked(false);
                    mRdBtnMakeOffer.setChecked(false);
                }
            }
        });
        mRdBtnBuyNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRdBtnBoth.setChecked(false);
                    mRdBtnMakeOffer.setChecked(false);
                }
            }
        });
        mRdBtnMakeOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRdBtnBuyNow.setChecked(false);
                    mRdBtnBoth.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view == mRdBtnServicePickUp) {
            mHrscrlVwZipContainer.setVisibility(View.GONE);
            mEdtxtZipCode.setVisibility(View.GONE);
            mImgVwAddZipCode.setVisibility(View.GONE);
        }
        if (view == mRdBtnServiceZIP) {
            mHrscrlVwZipContainer.setVisibility(View.VISIBLE);
            mEdtxtZipCode.setVisibility(View.VISIBLE);
            mImgVwAddZipCode.setVisibility(View.VISIBLE);
        }
        if (view == mImgVwAddZipCode) {
            verifyZip();
        }


        if (view == mEdtxtAddress) {
            // launch the place picker intent here...
            launchPlacePiker();
        }
        if (mTxtVwTc == view) {
//            String url = "http://rippleitt.com/listing_policies.php";
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
            Intent i = new Intent(AddNewProduct.this, TermsConditionsActivity.class);
            startActivity(i);
        }
        if (view == mrelAddProduct_back) {
            RippleittAppInstance.getInstance().getListingImages().clear();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }

        if (view == mrelUploadProductImages) {

            //addAttachments();
            showPhotoOptions();

/*
            Intent intent = new Intent(AddNewProduct.this, AlbumSelectActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, password);
            startActivityForResult(intent, Constants.REQUEST_CODE);*/
        }
        if (view == mbtnAddPRoductPublish) {
            if (validate())
                publishListing(MODE_PUBLISH);

        }

        if (view == mbtnAddProdSaveDraft) {
            if (validate())
                publishListing(MODE_DRAFT);
        }
    }

    private void publishListing(int flag) {
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
        if (mRdBtnPaymentModeCash.isChecked()) {
            listingTemplate.setPayment_mode("1");
        } else {
            listingTemplate.setPayment_mode("2");
        }
//        listingTemplate.setPayment_mode("1");
        if (mRdBtnNewProduct.isChecked()) {
            listingTemplate.setIs_new("1");
        } else {
            listingTemplate.setIs_new("0");
        }
        if (mRdBtnBuyNow.isChecked()) {
            listingTemplate.setDirect_buy("1");
        } else if (mRdBtnBoth.isChecked()) {
            listingTemplate.setDirect_buy("2");
        } else {
            listingTemplate.setDirect_buy("0");
        }
        listingTemplate.setLatitude(Double.toString(latitude__));

        listingTemplate.setLongitude(Double.toString(longitude__));
//        listingTemplate.setRewardAmount(mEdtxtRewardPoint.getText().toString().trim());
        if (mRdBtnService.isChecked()) {
            listingTemplate.setListing_type("2");
            listingTemplate.setIs_new("1");
            listingTemplate.setQuantity("10");
            listingTemplate.setPrice("10");
        }
        if (mRdBtnServicePickUp.isChecked()) {
            listingTemplate.setShippingType("1");
        }
        if (mRdBtnServiceZIP.isChecked()) {
            listingTemplate.setShippingType("2");
            String zipPayload = "";
            for (String zip : RippleittAppInstance.getInstance().getServiceZIPCodes().keySet()) {
                zipPayload = zipPayload + zip + ",";
            }
            zipPayload = zipPayload.replaceAll(",$", "");
            listingTemplate.setZipCodes(zipPayload);
        }

        new SubmitListingAsync(this, this, listingTemplate, flag).execute();
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


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            //Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(AddNewProduct.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(AddNewProduct.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
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
        if (RippleittAppInstance.getInstance().getListingImages().keySet().size() == 0) {
            Toast.makeText(AddNewProduct.this,
                    "Please add a listing image", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (medittxtAddProductName.getText().toString().trim().equalsIgnoreCase("")) {

            medittxtAddProductName.setError("Please enter listing title");
            mScrlVwWrapper.post(new Runnable() {
                @Override
                public void run() {
                    mScrlVwWrapper.scrollTo(0, medittxtAddProductName.getTop() - 20);
                }
            });
            // Toast.makeText(AddNewProduct.this, "Please enter product name",Toast.LENGTH_LONG).show();
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
            Toast.makeText(AddNewProduct.this, "Please select listing category", Toast.LENGTH_LONG).show();
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
            Toast.makeText(AddNewProduct.this, "Please select listing sub category", Toast.LENGTH_LONG).show();
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
            // Toast.makeText(AddNewProduct.this, "Please enter listing description    ",Toast.LENGTH_LONG).show();
            return false;
        }

        if (!mRdBtnService.isChecked() && mEdtxtQty.getText().toString().trim().equalsIgnoreCase("")) {
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
        if (!mRdBtnService.isChecked() && mEdtxtQty.getText().toString().trim().equalsIgnoreCase("0")) {
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
        if (!mRdBtnService.isChecked() && medittxtAddProductPrice.getText().toString().trim().equalsIgnoreCase("")) {
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

        if (!mRdBtnService.isChecked() && listingPrice < 1) {
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
//        if (PreferenceHandler.readString(AddNewProduct.this,
//                PreferenceHandler.USER_TYPE, "").equalsIgnoreCase("1")) {
//            Float flt = Float.parseFloat(medittxtAddProductPrice.getText().toString());
//            if (flt > 2000) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("With an individual account you can post a listing with maximum value of $2000")
//                        .setCancelable(false)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                //do things
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();
//                return false;
//            }
//        }


//        float referralAmount = 0;
//        try {
//            referralAmount = Float.parseFloat(mEdtxtRewardPoint.getText().toString().trim());
//        } catch (Exception e) {
//
//        }
//
//
//        if (referralAmount < 1) {
//            mEdtxtRewardPoint.setError("Referral amount must not be less than $1");
//            mScrlVwWrapper.post(new Runnable() {
//                @Override
//                public void run() {
//                    mScrlVwWrapper.scrollTo(0, mEdtxtRewardPoint.getTop());
//                }
//            });
//            //Toast.makeText(AddNewProduct.this, "Please enter referral amount    ",Toast.LENGTH_LONG).show();
//            return false;
//        }


        if (mEdtxtAddress.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(AddNewProduct.this, "Please enter listing address ", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (mEdtxtRewardPoint.getText().toString().trim().equalsIgnoreCase("")) {
//            mEdtxtRewardPoint.setError("Please enter referral amount");
//            mScrlVwWrapper.post(new Runnable() {
//                @Override
//                public void run() {
//                    mScrlVwWrapper.scrollTo(0, mEdtxtRewardPoint.getTop());
//                }
//            });
//            //Toast.makeText(AddNewProduct.this, "Please enter referral amount    ",Toast.LENGTH_LONG).show();
//            return false;
//        }

        if (mspinnerCategoryAddProduct.getSelectedItemPosition() == 0) {
            Toast.makeText(AddNewProduct.this, "Please select a category", Toast.LENGTH_SHORT);
            return false;
        }
        if (spnrSubCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(AddNewProduct.this, "Please select a sub category", Toast.LENGTH_SHORT);
            return false;
        }

        /*if(mRdBtnServiceZIP.isChecked()){
            if(RippleittAppInstance.getInstance().getServiceZIPCodes().keySet().size()==0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please provide service ZIP codes")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        }*/

        if (!mChkBxTC.isChecked()) {
            com.rippleitt.utils.CommonUtils
                    .showSingleButtonAlert(AddNewProduct.this,
                            "Please accept the Rippleitt Listing Policies");
            //mChkBxTerms.setError("Please accept the terms of usage.");
            /*Toast.makeText(SignUpActivity.this,
                    "Please accept the terms of usage.",
                    Toast.LENGTH_SHORT).show();*/
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
                                    .getPath(AddNewProduct.this,
                                            data.getClipData().getItemAt(index).getUri())));

                            if (!RippleittAppInstance.getInstance().getListingImages().containsKey(compresedFile.getAbsolutePath())) {
                                RippleittAppInstance.getInstance()
                                        .getListingImages().put(compresedFile.getAbsolutePath(), compresedFile.getAbsolutePath());
                            }

                        /*
                        if(!RippleittAppInstance.getInstance().getListingImages()
                                .containsKey(data.getClipData().getItemAt(index).getUri())){
                            RippleittAppInstance.getInstance().getListingImages().put(
                                    ImageFilePath.getPath(AddNewProduct.this,
                                            data.getClipData().getItemAt(index).getUri()),
                                    data.getClipData().getItemAt(index).getUri());
                        }
                        */
                        }

                    } else {

                        File compresedFile = mCompressor.compressToFile(new File(ImageFilePath
                                .getPath(AddNewProduct.this,
                                        data.getData())));

                        if (!RippleittAppInstance.getInstance().getListingImages().containsKey(compresedFile.getAbsolutePath())) {
                            RippleittAppInstance.getInstance()
                                    .getListingImages().put(compresedFile.getAbsolutePath(), compresedFile.getAbsolutePath());
                        }
                    /*
                    if(!RippleittAppInstance.getInstance().getListingImages()
                            .containsKey(data.getData())){
                        RippleittAppInstance.getInstance().getListingImages().put(
                                ImageFilePath.getPath(AddNewProduct.this,
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
            if (requestCode == PLACE_PICKER_REQUEST) {
                mProgressDialog.dismiss();
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    String address = String.format("%s", place.getName());
                    if (address != null) {
                        mEdtxtAddress.setText(address);
                        latitude__ = place.getLatLng().latitude;
                        longitude__ = place.getLatLng().longitude;
                        mProgressDialog.dismiss();
                    }

                    // Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }

            }
            if (requestCode == CAMERA_ACTION_IMAGE_CAPTURE) {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    // mImageView.setImageBitmap(imageBitmap);
                    if (imageBitmap != null) {
                        new SaveBitmapAsync(imageBitmap).execute();
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
                    Toast.makeText(AddNewProduct.this, "Read photo permission denied", Toast.LENGTH_SHORT).show();
                    ;
                }
                break;
            case ACTION_ACCESS_LOCATION_:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    Toast.makeText(AddNewProduct.this, "Location permission denied", Toast.LENGTH_LONG).show();
                    mPDialog.dismiss();
                    mEdtxtAddress.setText("Unknown street");
                }
                break;
            case ACTION_ACCESS_FINE_LOCATION:
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Toast.makeText(AddNewProduct.this, "Location permission denied", Toast.LENGTH_LONG).show();
                    mPDialog.dismiss();
                    mEdtxtAddress.setText("Unknown street");
                    return;

                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doLocationStuff();
                }
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(AddNewProduct.this, "Location permission denied", Toast.LENGTH_LONG).show();
                    mPDialog.dismiss();
                    mEdtxtAddress.setText("Unknown street");
                }

                break;
            case ACTION_WRITE_EXTERNAL_STORAGE:

                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    dirPath = com.rippleitt.commonUtilities.CommonUtils.createMediaDir();
                } else {
                    Toast.makeText(AddNewProduct.this,
                            "write permission denied, unable to launch camera", Toast.LENGTH_SHORT).show();
                    ;
                }

                break;
            case REQUEST_IMAGE_CAPTURE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchCamera();
                } else {
                    Toast.makeText(AddNewProduct.this,
                            "Camera permission denied", Toast.LENGTH_SHORT).show();
                    ;
                }
                break;
            default:
                break;
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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
                            ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(AddNewProduct.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    RippleittAppInstance.getInstance().getCategories());
                            mspinnerCategoryAddProduct.setAdapter(karant_adapter);
                            // mLinLytRegion.setVisibility(View.VISIBLE);

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
                Toast.makeText(AddNewProduct.this, "could not fetch categories, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(AddNewProduct.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                if (mRdBtnService.isChecked()) {
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
                            ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(AddNewProduct.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    RippleittAppInstance.getInstance().getSubCategories());
                            spnrSubCategory.setAdapter(karant_adapter);
                            mRelLytSubCategoryContainer.setVisibility(View.VISIBLE);
                            // mLinLytRegion.setVisibility(View.VISIBLE);

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
                Toast.makeText(AddNewProduct.this, "could not fetch sub category, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(AddNewProduct.this,
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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
        mLinLytRecyclerContainer.setVisibility(View.VISIBLE);
        Set<String> keys = RippleittAppInstance.getInstance().getListingImages().keySet();
        String[] keysArray = keys.toArray(new String[keys.size()]);
        // now we draw the horizontal slider

        recyclerView.addItemDecoration(new DividerItemDecoration(AddNewProduct.this, LinearLayoutManager.HORIZONTAL));
        horizontalAdapter = new HorizontalReyclerViewAdapter(keysArray,
                getApplicationContext(),
                this, 1, 0);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AddNewProduct.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(horizontalAdapter);
        recyclerView.setLayoutManager(horizontalLayoutManager);

    }

    @Override
    public void removePic(String absPath, int type, int positin) {
        RippleittAppInstance.getInstance().getListingImages().remove(absPath);
        if (RippleittAppInstance.getInstance().getListingImages().keySet().size() == 0) {
            mLinLytRecyclerContainer.setVisibility(View.GONE);
        }
        populateProductImages();
    }

    @Override
    public void onInit() {
        //Toast.makeText(AddNewProduct.this,"sync-init",Toast.LENGTH_LONG).show();
        CommonUtils.showProgress(AddNewProduct.this, "Submitting your listing...");
    }

    @Override
    public void onError(ListingSyncResponseTemplate template) {
        CommonUtils.dismissProgress();
        Toast.makeText(AddNewProduct.this, "Could not complete your request", Toast.LENGTH_LONG).show();
        RippleittAppInstance.getInstance().getListingImages().clear();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onSuccess(ListingSyncResponseTemplate template) {
        CommonUtils.dismissProgress();
        if (template.getDraft_flag().equalsIgnoreCase("0")) {
            // listing was for publication..
            if (template.getIs_card_available().equalsIgnoreCase("0")) {
//             card not available, so we ask the user to add a card....
                addCardPrompt();
            } else {
                Toast.makeText(AddNewProduct.this,
                        "Thank you for adding new listing!! It’s under review and will be posted within 24 hours.",
                        Toast.LENGTH_LONG).show();
                CommonUtils.dismissProgress();
                RippleittAppInstance.getInstance().getListingImages().clear();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

        } else {
            Toast.makeText(AddNewProduct.this,
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

    @Override
    public void onImageCicked(String absPath, int type, int position, int displayMode) {

    }

    private void addCardPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please add a credit card to your account in order to publish a listing.")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RippleittAppInstance.getInstance().setAddCardMode(1);
                        startActivity(new Intent(AddNewProduct.this, UpdateListingAsync.class));
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
            linearShippingMethods.setVisibility(View.GONE);
        }

        fetchCategories();

    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);


        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            mRequestingLocationUpdates = false;
            mEdtxtAddress.setText(mAddressOutput);
            com.rippleitt.commonUtilities.CommonUtils.dismissProgress();

            // Show a toast message if an address was found.


            // Reset. Enable the Fetch Address button and stop showing the progress bar.

        }
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                if (mCurrentLocation != null) {
                    //signUpTemplate.setLatitude(Double.toString(mCurrentLocation.getLatitude()));
                    //signUpTemplate.setLongitude(Double.toString(mCurrentLocation.getLongitude()));
                }
                // remove the location callbacks and fetch the address...
                mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                        .addOnCompleteListener(AddNewProduct.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                mRequestingLocationUpdates = false;

                            }
                        });
                // here we start reverse Geocoding....
                startIntentService(mCurrentLocation);
            }
        };
    }

    protected void startIntentService(Location locationObject) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, locationObject);
        startService(intent);
    }

    private void verifyZip() {
        if (mEdtxtZipCode.getText().toString().trim().equalsIgnoreCase("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please provide mail character ZIP code")
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
        final com.robertlevonyan.views.chip.Chip chip = new com.robertlevonyan.views.chip.Chip(AddNewProduct.this);
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
            final com.robertlevonyan.views.chip.Chip chip = new com.robertlevonyan.views.chip.Chip(AddNewProduct.this);
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

    private class LocationTimeoutAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        public void onPostExecute(Void v) {
            if (mPDialog.isShowing()) {
                mPDialog.dismiss();
                try {
                    lManagerInstance.removeUpdates(locationListenerReference);
                } catch (Exception e) {

                }

            }
        }
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
                File file = new File(AddNewProduct.this.getCacheDir(), filename);
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

//    Is_drafted = null
//    Latitude = "0.0"
//    Longitude = "0.0"
//    address = "C-184, Industrial Area, Sector 75, Sahibzada Ajit Singh Nagar, Punjab 140308, India"
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

}
