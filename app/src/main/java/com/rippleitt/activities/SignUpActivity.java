package com.rippleitt.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.BuildConfig;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.PostalCodesAdapter;
import com.rippleitt.callback.ItemClickListener;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.commonUtilities.ImageFilePath;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.commonUtilities.SaveImage;
import com.rippleitt.commonUtilities.SignupAsync;
import com.rippleitt.controller.FetchAddressIntentService;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.Constants;
import com.rippleitt.modals.EditProfileDetailTemplate;
import com.rippleitt.modals.EditProfileSyncResponseTemplate;
import com.rippleitt.modals.IoCallback;
import com.rippleitt.modals.ListingResponsePayload;
import com.rippleitt.modals.PostalCodeApiResponse;
import com.rippleitt.modals.PostalCodeModal;
import com.rippleitt.modals.SignUpTemplate;
import com.rippleitt.modals.googleApiResults.GoogleApiResponse;
import com.rippleitt.modals.googleApiResults.Predictions;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class SignUpActivity extends AppCompatActivity
        implements View.OnClickListener, ItemClickListener,IoCallback {
    private RelativeLayout mrelSignup;
    private Button mbtnNextSignup;
    private EditText medittxtFrstName;
    private EditText medittxtLastName;
    private EditText medittxtEmailSignup;
    private EditText medittxtContactSignup;
    private EditText medittxPassSignup;
    private EditText medittxtConfirmPassSignup;
    private EditText medtxtBusinessName;
    private EditText medittxtaddr1Signup;
    private EditText edPostalCode;
    private EditText medittxtaddr2Signup;
    private CircleImageView mprofile_image;
    private LinearLayout mLinLytMaleWrapper, mLinLytFemaleWrapper;
    private TextView mTxtVwMaleLable, mTxtvwFemaleLabel;
    private ImageView mImgVwMaleIcon, mImgVwFemaleIcon;
    private LocationRequest mLocationRequest;
    private CheckBox mChkBxSmsPromo;
    private RelativeLayout mRelLytAbnBox, mRelLytAddressBox;
    private EditText mEdtxtAbnNumber;
    private int userType = 1;
    //===========camera======
    public static final int MEDIA_TYPE_IMAGE = 1;
    Uri fileUri;
    public static String imageee = "";
    File f = null;
    private static final String IMAGE_DIRECTORY_NAME = "MYPACK";
    SaveImage saveimage;
    String picturePath;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_Gallery_IMAGE_REQUEST_CODE = 2;
    SharedPreferences sharedPreferences;
    String image_path = "";
    private final int ACTION_READ_EXTERNAL_STORAGE = 13;
    private final int ACTION_WRITE_EXTERNAL_STORAGE = 20;
    private final int ACTION_ACCESS_FINE_LOCATION = 14;
    private final int ACTION_ACCESS_COARSE_LOCATION = 15;
    private final int RESULT_LOAD_IMG = 2;
    private SignUpTemplate signUpTemplate;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mRequestingLocationUpdates = true;
    private LocationCallback mLocationCallback;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Location mCurrentLocation;
    private AddressResultReceiver mResultReceiver;
    private CheckBox mChkBxTerms;
    private TextView mTxtVwTcLauncher,txtvwPrivacyPolicy;
    private Compressor mCompressor;
//    private RadioGroup mRdGrpAccountType;
    private double poledLatitude = 0, poledLongotude = 0;
    private ProgressDialog mPDialog;
    LocationListener locationListenerReference;
    LocationManager lManagerInstance;

    private final int REQUEST_IMAGE_CAPTURE = 18;
    private final int CAMERA_ACTION_IMAGE_CAPTURE = 19;
    private String dirPath;
    private ProgressDialog pdialog;
    private List<PostalCodeModal> placesList ;
    PostalCodesAdapter adapter;
    RecyclerView recyclerPostcodes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompressor = new Compressor(this);
        mCompressor.setQuality(25);
        setContentView(R.layout.activity_sign_up);
        signUpTemplate = new SignUpTemplate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        mPDialog = new ProgressDialog(this);
        mPDialog.setCancelable(false);

        Bundle bundle= getIntent().getExtras();
        if (bundle!=null){
            userType= bundle.getInt("userType",0);
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACTION_WRITE_EXTERNAL_STORAGE);
        } else {
            dirPath = CommonUtils.createMediaDir();
        }

        init();
        work();

        placesList=new ArrayList<>();
        adapter = new PostalCodesAdapter(placesList, SignUpActivity.this);
//        edPostalCode.setThreshold(1); //will start working from first character
        recyclerPostcodes.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerPostcodes.setAdapter(adapter);
        recyclerPostcodes.setVisibility(View.GONE);

//        edPostalCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){

                    edPostalCode.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (edPostalCode.hasFocus()) {
                                recyclerPostcodes.setVisibility(View.VISIBLE);
                                postalCodeApi(s.toString());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
//                }
//            }
//        });

//        captureUserLocation();

    }

    @Override
    protected void onResume() {
        super.onResume();
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


    private void doLocationStuff() {
        mPDialog.setMessage("Setting up your account...");
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
                        geocoder = new Geocoder(SignUpActivity.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        medittxtaddr1Signup.setText(addresses.get(0).getAddressLine(0));
                        mPDialog.dismiss();
                        locationAsync.cancel(true);
                    } catch (Exception e) {
                        locationAsync.cancel(true);
                        medittxtaddr1Signup.setText("Unknown street");
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


    public void init(){
        mRelLytAbnBox=(RelativeLayout)findViewById(R.id.relLytAbnBox);
        mEdtxtAbnNumber=(EditText)findViewById(R.id.mEdtxtAbnNumber);
        mRelLytAddressBox=(RelativeLayout)findViewById(R.id.relLytBusinessNameBox);
        medtxtBusinessName=(EditText)findViewById(R.id.mEdtxtBusinessName);
        mRelLytAbnBox.setVisibility(View.GONE);
        mRelLytAddressBox.setVisibility(View.GONE);
//        mRdGrpAccountType=(RadioGroup)findViewById(R.id.rdgrpAcType);
//        mRdGrpAccountType.setOnCheckedChangeListener(this);
        recyclerPostcodes= findViewById(R.id.recyclerPostcodes);
        mrelSignup=(RelativeLayout)findViewById(R.id.relSignup);
        mprofile_image=(CircleImageView) findViewById(R.id.profile_image);
        mbtnNextSignup=(Button)findViewById(R.id.btnNextSignup);
        medittxtFrstName=(EditText)findViewById(R.id.edittxtFrstName);
        medittxtLastName=(EditText)findViewById(R.id.edittxtLastName);
        medittxtEmailSignup=(EditText)findViewById(R.id.edittxtEmailSignup);
        medittxtContactSignup=(EditText)findViewById(R.id.edittxtContactSignup);
        medittxPassSignup=(EditText)findViewById(R.id.edittxPassSignup);
        medittxtConfirmPassSignup=(EditText)findViewById(R.id.edittxtConfirmPassSignup);
        medittxtaddr1Signup=(EditText)findViewById(R.id.edittxtaddr1Signup);
        edPostalCode=(EditText) findViewById(R.id.edPostalCode);
        medittxtaddr2Signup=(EditText)findViewById(R.id.edittxtaddr2Signup);
        mLinLytFemaleWrapper=(LinearLayout)findViewById(R.id.linlytFemaleWrapper);
        mLinLytMaleWrapper=(LinearLayout)findViewById(R.id.linlytMaleWrapper);
//        mChkBxEmailPromo=(CheckBox)findViewById(R.id.chkboxEmailPromo);
        mChkBxSmsPromo=(CheckBox)findViewById(R.id.chkboxSMSPromo);
        mChkBxTerms=(CheckBox)findViewById(R.id.chkbxTerms);
        mTxtVwTcLauncher=(TextView)findViewById(R.id.txtvwTCLauncher);
        txtvwPrivacyPolicy=(TextView)findViewById(R.id.txtvwPrivacyPolicy);
        mTxtVwTcLauncher.setText(Html.fromHtml("<u>Terms & Conditions</u>"));
        txtvwPrivacyPolicy.setText(Html.fromHtml("<u>Privacy Policy</u>"));
        mTxtVwTcLauncher.setOnClickListener(this);
        txtvwPrivacyPolicy.setOnClickListener(this);
        mTxtVwMaleLable=(TextView)findViewById(R.id.txtvwMaleLabel);
        mTxtvwFemaleLabel=(TextView)findViewById(R.id.txtvwFemaleLabel);
        mImgVwFemaleIcon=(ImageView)findViewById(R.id.imgvwFemale);
        mImgVwMaleIcon=(ImageView)findViewById(R.id.imgvwMale);
        saveimage=new SaveImage(this);
        mbtnNextSignup.setOnClickListener(this);
        mLinLytFemaleWrapper.setOnClickListener(this);
        mLinLytMaleWrapper.setOnClickListener(this);
        mrelSignup.setOnClickListener(this);
        mprofile_image.setOnClickListener(this);
        mChkBxTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mChkBxTerms.isChecked())
                    mChkBxTerms.setError(null);
            }
        });
        medittxtaddr1Signup.setText("Unknown street");
    }
    public void work(){
        if (userType==2) {
                mRelLytAbnBox.setVisibility(View.VISIBLE);
                mRelLytAddressBox.setVisibility(View.VISIBLE);
        }else {
            mRelLytAbnBox.setVisibility(View.GONE);
            mRelLytAddressBox.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        if(view==mTxtVwTcLauncher){
//            String url = "http://rippleitt.com/terms.html";
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
            Intent i= new Intent(SignUpActivity.this, TermsConditionsActivity.class);
            startActivity(i);
        }
        if(view==txtvwPrivacyPolicy){
//            String url = "http://rippleitt.com/terms.html";
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
            Intent i= new Intent(SignUpActivity.this, TermsConditionsActivity.class);
            i.putExtra("isTerms",false);
            startActivity(i);
        }
        //==========backpress_signup========
        if (view==mrelSignup){
            finish();
        }
        //===========signup_button==========
        if (view==mbtnNextSignup){
            if(validate()){
                new SignupAsync(SignUpActivity.this, this,signUpTemplate ).execute();
            }
        }
        //==================upload_image
        if (view==mprofile_image){
            //addAttachments();
            showPhotoOptions();
        }if(mLinLytMaleWrapper==view){
            signUpTemplate.setGender("1");
            mImgVwFemaleIcon.setImageDrawable(getResources().getDrawable(R.drawable.female));
            mImgVwMaleIcon.setImageDrawable(getResources().getDrawable(R.drawable.male_active));
            mTxtVwMaleLable.setTextColor(Color.WHITE);
            mTxtvwFemaleLabel.setTextColor(Color.GRAY);
        }if(mLinLytFemaleWrapper==view){
            signUpTemplate.setGender("2");
            mImgVwFemaleIcon.setImageDrawable(getResources().getDrawable(R.drawable.female_active));
            mImgVwMaleIcon.setImageDrawable(getResources().getDrawable(R.drawable.male));
            mTxtVwMaleLable.setTextColor(Color.GRAY);
            mTxtvwFemaleLabel.setTextColor(Color.WHITE);
        }
    }

    private boolean validate(){
        if(medittxtFrstName.getText().toString().trim().equalsIgnoreCase("")){
            medittxtFrstName.setError("Please provide first name.");
            medittxtFrstName.getParent().requestChildFocus(medittxtFrstName,medittxtFrstName);
            return false;
        }
//        if(medittxtLastName.getText().toString().trim().equalsIgnoreCase("")){
//            medittxtLastName.setError("Please provide last name.");
//            medittxtLastName.getParent().requestChildFocus(medittxtLastName,medittxtLastName);
//            return false;
//        }
        if(!CommonUtils.isValidEmail(medittxtEmailSignup.getText().toString().trim())){
            medittxtEmailSignup.setError("Please provide a valid email address.");
            medittxtEmailSignup.getParent()
                    .requestChildFocus(medittxtEmailSignup,medittxtEmailSignup);
            return false;
        }if(medittxPassSignup.getText()
                .toString().equalsIgnoreCase("")){
            medittxPassSignup.setError("Please provide password.");
            medittxPassSignup.getParent().requestChildFocus(medittxPassSignup,medittxPassSignup);
            return false;
        }if(edPostalCode.getText()
                .toString().equalsIgnoreCase("")){
            edPostalCode.setError("Please provide post code.");
            edPostalCode.getParent().requestChildFocus(edPostalCode,edPostalCode);
            return false;
        }if(medittxPassSignup.getText()
                .toString()
                .trim().equalsIgnoreCase("")){
            medittxPassSignup.setError("Please provide a valid six character password. ");
            medittxPassSignup.getParent().requestChildFocus(medittxPassSignup,medittxPassSignup);
            return false;
        }if(medittxPassSignup.getText().toString().trim().toCharArray().length<6){
            medittxPassSignup.setError("Please provide a valid six character password.");
            medittxPassSignup.getParent().requestChildFocus(medittxPassSignup,medittxPassSignup);
            return false;
        }if(!medittxPassSignup.getText().toString()
                .trim()
                .equalsIgnoreCase(medittxtConfirmPassSignup
                        .getText().toString().trim())){
            medittxtConfirmPassSignup.setError("Password and confirm password do not match.");
            medittxtConfirmPassSignup.getParent().requestChildFocus(medittxtConfirmPassSignup,medittxtConfirmPassSignup);
            return false;
        }
//        if(medittxtaddr1Signup.getText().toString().trim().equalsIgnoreCase("")){
//                medittxtaddr1Signup.setError("Please provide address 1");
//            medittxtaddr1Signup.getParent().requestChildFocus(medittxtaddr1Signup,medittxtaddr1Signup);
//                return false;
//        }
        if(mRelLytAbnBox.getVisibility()==View.VISIBLE){
            if(mEdtxtAbnNumber.getText().toString().trim().toCharArray().length!=11){
                mEdtxtAbnNumber.setError("Please provide valid 11 character ABN number.");
                return false;
            }if(medtxtBusinessName.getText().toString().trim().equalsIgnoreCase("")){
                medtxtBusinessName.setError("Please provide business name.");
                return false;
            }
        }

        // now we fill the signup object....
        signUpTemplate.setFname(medittxtFrstName.getText().toString().trim());
        signUpTemplate.setLname("  ");
        signUpTemplate.setEmail(medittxtEmailSignup.getText().toString().trim());
        signUpTemplate.setNumber(medittxtContactSignup.getText().toString().trim());
        signUpTemplate.setPassword(CommonUtils.md5(medittxPassSignup.getText().toString().trim()));
        signUpTemplate.setPost_code(edPostalCode.getText().toString().trim());
//        signUpTemplate.setAddress1(medittxtaddr1Signup.getText().toString().trim());
//        signUpTemplate.setAddress2(medittxtaddr2Signup.getText().toString().trim());

        signUpTemplate.setLatitude(Double.toString(poledLatitude));
        signUpTemplate.setLongitude(Double.toString(poledLongotude));
        if(userType==1){
            signUpTemplate.setUserType(1);
        }else{
            signUpTemplate.setUserType(2);
            signUpTemplate.setAbnNumber(mEdtxtAbnNumber.getText().toString().trim());
            signUpTemplate.setBusinessName(medtxtBusinessName.getText().toString().trim());
        }
        if(mChkBxSmsPromo.isChecked()){
            signUpTemplate.setSmsPromo(1);
            signUpTemplate.setEmailPromo(1);
        }
//        if(mChkBxEmailPromo.isChecked()){
//        }

        if(!mChkBxTerms.isChecked()){
            com.rippleitt.utils.CommonUtils
                    .showSingleButtonAlert(SignUpActivity.this,
                            "Please accept the Terms & Conditions.");
            //mChkBxTerms.setError("Please accept the terms of usage.");
            /*Toast.makeText(SignUpActivity.this,
                    "Please accept the terms of usage.",
                    Toast.LENGTH_SHORT).show();*/
            return false;
        }
       // signUpTemplate.setRefer_code(mEd.getText().toString().trim());

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (recyclerPostcodes.getVisibility()== View.VISIBLE){
            recyclerPostcodes.setVisibility(View.GONE);
        }else{

            finish();
        }
    }

    private void showPhotoOptions(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Import Image From")
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
        alert.show();
    }

    protected void startIntentService(Location locationObject) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, locationObject);
        startService(intent);
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


    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


/*
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                if(data.getClipData()!=null){
                    int imageCount=data.getClipData().getItemCount();
                    for(int index=0;index<imageCount;index++){
                        // if the hashmap does not contain the image already we add it
                        // to avoid duplicate images beinf added.
                    }

                }else{
                    signUpTemplate.setProfilePicPath( ImageFilePath.getPath(SignUpActivity.this,
                            data.getData()));
                }

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
            Picasso.with(SignUpActivity.this)
                    .load(new File(signUpTemplate.getProfilePicPath()))
                    .resize(80,80)
                    .into(mprofile_image);
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }
*/
    private void addAttachments() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG);
    }

    //=================CamerA Functions=======
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onInit() {
        CommonUtils.showProgress(SignUpActivity.this, "Creating your account...");
    }

    @Override
    public void onError(Object responseTemplate) {
        CommonUtils.dismissProgress();;
        Toast.makeText(SignUpActivity.this,
                ((EditProfileSyncResponseTemplate)responseTemplate).getResponse_message(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(Object responseTemplate) {

            Toast.makeText(SignUpActivity.this,"Thanks!! You're almost there",Toast.LENGTH_LONG).show();
           RippleittAppInstance.getInstance().setVerificationMode(1);
            startActivity(new Intent(SignUpActivity.this,
                    ActivityVerifyEmail.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                           int[] grantResults) {

        if(requestCode==ACTION_ACCESS_FINE_LOCATION){
            if (grantResults.length <= 0) {
                Toast.makeText(SignUpActivity.this,"Location permission denied",Toast.LENGTH_LONG).show();
                mPDialog.dismiss();
                medittxtaddr1Signup.setText("Unknown street");
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                return;
            }  if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doLocationStuff();
            }  if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(SignUpActivity.this,"Location permission denied",Toast.LENGTH_LONG).show();
                    mPDialog.dismiss();
                    medittxtaddr1Signup.setText("Unknown street");
            }

        }if(requestCode==REQUEST_IMAGE_CAPTURE){
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                launchCamera();
            } else {
                Toast.makeText(SignUpActivity.this,
                        "Camera permission denied", Toast.LENGTH_SHORT).show();
                ;
            }
        }if(requestCode==ACTION_WRITE_EXTERNAL_STORAGE){
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                dirPath= CommonUtils.createMediaDir();
            } else {
                Toast.makeText(SignUpActivity.this,
                        "write permission denied, unable to launch camera", Toast.LENGTH_SHORT).show();
                ;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_CHECK_SETTINGS){
                if(resultCode==Activity.RESULT_OK){

                } if(resultCode==Activity.RESULT_CANCELED){
                        mRequestingLocationUpdates = false;
                }
        }if(requestCode==RESULT_LOAD_IMG){
            if(resultCode==Activity.RESULT_OK){
                try{
                    File compresedFile=      mCompressor.compressToFile(new File(ImageFilePath
                            .getPath(SignUpActivity.this,
                                    data.getData())));
                    signUpTemplate.setProfilePicPath( compresedFile.getAbsolutePath());
                    Picasso.Builder builder = new Picasso.Builder(this);
                    builder.listener(new Picasso.Listener()
                    {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    });
                    builder.build().load(new File(signUpTemplate.getProfilePicPath())).into(mprofile_image);

                }catch (Exception e){

                }

                /*
                Picasso.with(SignUpActivity.this)
                        .load(new File(signUpTemplate.getProfilePicPath()))
                        .resize(100,100)
                        .into(mprofile_image);
                        */
            } if(resultCode==Activity.RESULT_CANCELED){
                mRequestingLocationUpdates = false;
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
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
    }

//    @Override
//    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//            if(i==R.id.rdbtnBusiness){
//                userType=1;
//                mRelLytAbnBox.setVisibility(View.VISIBLE);
//                mRelLytAddressBox.setVisibility(View.VISIBLE);
//            }if(i==R.id.rdbtnPersonal){
//                userType=2;
//            mRelLytAbnBox.setVisibility(View.GONE);
//            mRelLytAddressBox.setVisibility(View.GONE);
//        }
//    }


    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);


        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
           String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
           mRequestingLocationUpdates=false;
            medittxtaddr1Signup.setText(mAddressOutput);
            CommonUtils.dismissProgress();

            // Show a toast message if an address was found.


            // Reset. Enable the Fetch Address button and stop showing the progress bar.

        }
    }


    private class SaveBitmapAsync extends AsyncTask<Void,Void,String>{



        Bitmap bitMap;
        boolean didCompressAndCopy=false;

        private SaveBitmapAsync(Bitmap fileToSave){
            bitMap=fileToSave;
            pdialog= new ProgressDialog(SignUpActivity.this);
        }

        @Override
        public void onPreExecute(){
            pdialog.setCancelable(false);
            pdialog.setMessage("Saving file");

        }

        @Override
        protected String doInBackground(Void... voids) {
            String filePath="";
            try{
                String filename = "SKU_PIC_" + System.currentTimeMillis() + ".jpg";
                //fileDestination = new File(dirPath, filename);
                File file = new File(dirPath,filename);
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
            pdialog.dismiss();
            Log.e(v,v);
            signUpTemplate.setProfilePicPath( v);
            Picasso.Builder builder = new Picasso.Builder(SignUpActivity.this);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build().load(new File(signUpTemplate.getProfilePicPath())).into(mprofile_image);

        }
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
                params.put("token", PreferenceHandler.readString(SignUpActivity.this,
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
        edPostalCode.clearFocus();
        CommonUtils.keyboardHide(getBaseContext(),getWindow().getDecorView().getRootView());
        edPostalCode.setText(placesList.get(pos).getPostcode());
        recyclerPostcodes.setVisibility(View.GONE);
    }
}
