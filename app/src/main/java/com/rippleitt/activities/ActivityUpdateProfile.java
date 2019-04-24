package com.rippleitt.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.login.LoginManager;
import com.rippleitt.R;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.commonUtilities.ImageFilePath;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.commonUtilities.SaveImage;
import com.rippleitt.commonUtilities.SubmitListingAsync;
import com.rippleitt.commonUtilities.SubmitProfileAsync;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.EditProfileSyncProcessCallback;
import com.rippleitt.modals.EditProfileSyncResponseTemplate;
import com.rippleitt.modals.EditProfileSyncTemplate;
import com.rippleitt.modals.ListingSyncTemplate;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ActivityUpdateProfile extends AppCompatActivity implements View.OnClickListener, EditProfileSyncProcessCallback, RadioGroup.OnCheckedChangeListener {


    private RelativeLayout mrelUpdateProfileBack;
    private EditText medittxtFrstName;
    private EditText medittxtLstName;
    private EditText medittxtEmail;
    private EditText medittxtPhone;
    private EditText medittxtPostalCode;
    private EditText medittxtAddress;
    private EditText medittxtAddressTwo;
    private CircleImageView medit_profile_image;
    private Button mbtnSaveProfile;
    public static final int PICK_IMAGE = 1;
    private static int RESULT_LOAD_IMG = 1;
    private AVLoadingIndicatorView mLoader;

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
    private int ACTION_READ_EXTERNAL_STORAGE = 13;
    SharedPreferences sharedPreferences;
    String image_path = "";
    private Compressor mCompressor;
    private EditText mEdtxtAccountType, mEdtxtAnNumber;
    private final int REQUEST_IMAGE_CAPTURE = 18;
    private final int CAMERA_ACTION_IMAGE_CAPTURE = 19;
    private ProgressDialog pdialog;
    private EditText mEdtxtBusinessName;
    private int userType;
    private RadioGroup mRdGrpAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompressor = new Compressor(this);
        mCompressor.setQuality(25);
        setContentView(R.layout.activity_update_profile);
        init();
        setData();
    }

    public void init() {
        saveimage = new SaveImage(this);
        mEdtxtAccountType = (EditText) findViewById(R.id.edtxtAccType);
        mEdtxtAnNumber = (EditText) findViewById(R.id.edtxtAbnNumber);
        mRdGrpAccountType = (RadioGroup) findViewById(R.id.rdgrpAcType);
        mEdtxtBusinessName = (EditText) findViewById(R.id.edtxtBusinessName);
        medittxtPostalCode = (EditText) findViewById(R.id.edittxtPostalCode);
        if (PreferenceHandler.readString(ActivityUpdateProfile.this, PreferenceHandler.USER_TYPE, "1")
                .equalsIgnoreCase("2")) {
            mEdtxtAnNumber.setVisibility(View.VISIBLE);
            mEdtxtBusinessName.setVisibility(View.VISIBLE);
            mEdtxtAccountType.setText("Business");
            mRdGrpAccountType.check(R.id.rdbtnBusiness);
            userType=2;
            mEdtxtAnNumber.setText(PreferenceHandler.readString(ActivityUpdateProfile.this, PreferenceHandler.ABN_NUMBER, ""));
            mEdtxtBusinessName.setText(PreferenceHandler.readString(ActivityUpdateProfile.this, PreferenceHandler.BUSINESS_NAME, ""));
        } else {
            mEdtxtAnNumber.setVisibility(View.GONE);
            mEdtxtBusinessName.setVisibility(View.GONE);
            mRdGrpAccountType.check(R.id.rdbtnPersonal);
            userType=1;
            mEdtxtAccountType.setText("Individual");
        }
        mrelUpdateProfileBack = (RelativeLayout) findViewById(R.id.relUpdateProfileBack);
        medittxtFrstName = (EditText) findViewById(R.id.edittxtFrstName);
        medittxtLstName = (EditText) findViewById(R.id.edittxtLstName);
        medittxtEmail = (EditText) findViewById(R.id.edittxtEmail);
        medittxtPhone = (EditText) findViewById(R.id.edittxtPhone);
        medittxtAddress = (EditText) findViewById(R.id.edittxtAddress);
        medittxtAddressTwo = (EditText) findViewById(R.id.edittxtAddressTwo);
        medit_profile_image = (CircleImageView) findViewById(R.id.edit_profile_image);
        mbtnSaveProfile = (Button) findViewById(R.id.btnSaveProfile);
        mRdGrpAccountType.setOnCheckedChangeListener(this);
        mbtnSaveProfile.setOnClickListener(this);
        mrelUpdateProfileBack.setOnClickListener(this);
        medit_profile_image.setOnClickListener(this);
        // medit_profile_image.setImageResource(R.drawable.ic_cloud_upload_black_24dp);
        mLoader = (AVLoadingIndicatorView) findViewById(R.id.aviLoaderHomeBottom);
        mLoader.hide();
        setData();
    }

    @Override
    public void onClick(View view) {
        if (view == mrelUpdateProfileBack) {
            finish();
        }
        if (view == mbtnSaveProfile) {
            if (validate()) {
                EditProfileApi();
            }


        }
        if (view == medit_profile_image) {
            // addAttachments();
            showPhotoOptions();
        }
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
    //====================


    private boolean validate() {

        if (medittxtFrstName.getText().toString().trim().equalsIgnoreCase("")) {

            medittxtFrstName.setError("Please enter your first name");
            // Toast.makeText(ActivityUpdateProfile.this,
            //         "Please enter your first name",Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (medittxtLstName.getText().toString().trim().equalsIgnoreCase("")) {
//            medittxtLstName.setError("Please enter your last name");
//            //Toast.makeText(ActivityUpdateProfile.this,
//            //     "Please enter your first name",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (medittxtAddress.getText().toString().trim().equalsIgnoreCase("")) {
//            medittxtAddress.setError("Please enter your address 1");
//            // Toast.makeText(ActivityUpdateProfile.this,
//            //      "Please enter your first name",Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (mEdtxtAnNumber.getVisibility() == View.VISIBLE) {
            if (mEdtxtAnNumber.getText().toString().trim().equalsIgnoreCase("")
                    || mEdtxtAnNumber.getText().toString().trim().toCharArray().length != 11) {
                mEdtxtAnNumber.setError("Please provide a valid ABN ");
                return false;
            }
            if (mEdtxtBusinessName.getText().toString().trim().equalsIgnoreCase("")) {
                mEdtxtBusinessName.setError("Please provide business name ");
                return false;
            }
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


    private void chooseFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    //==============onActivity=================
    //==================OnActivity_Result======================================
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG) {
                if (resultCode == RESULT_OK && null != data) {

                    if (data.getClipData() != null) {
                        int imageCount = data.getClipData().getItemCount();
                        for (int index = 0; index < imageCount; index++) {
                            // if the hashmap does not contain the image already we add it
                            // to avoid duplicate images beinf added.

                            try {
                                File compresedFile = mCompressor.compressToFile(new File(ImageFilePath
                                        .getPath(ActivityUpdateProfile.this,
                                                data.getClipData().getItemAt(index).getUri())));
                                image_path = compresedFile.getAbsolutePath();
                                Picasso.Builder builder = new Picasso.Builder(this);
                                builder.listener(new Picasso.Listener() {
                                    @Override
                                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                        exception.printStackTrace();
                                    }
                                });
                                builder.build()
                                        .load(compresedFile)
                                        .placeholder(R.drawable.default_profile_icon)
                                        .error(R.drawable.default_profile_icon)
                                        .into(medit_profile_image);

                            } catch (Exception e) {
                                medit_profile_image.setImageResource(R.drawable.default_profile_icon);
                            }
                        }

                    } else {

                        try {
                            File compresedFile = mCompressor.compressToFile(new File(ImageFilePath
                                    .getPath(ActivityUpdateProfile.this,
                                            data.getData())));
                            image_path = compresedFile.getAbsolutePath();
                            Picasso.Builder builder = new Picasso.Builder(this);
                            builder.listener(new Picasso.Listener() {
                                @Override
                                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                    exception.printStackTrace();
                                }
                            });
                            builder.build()
                                    .load(compresedFile)
                                    .placeholder(R.drawable.default_profile_icon)
                                    .error(R.drawable.default_profile_icon)
                                    .into(medit_profile_image);

                        } catch (Exception e) {
                            medit_profile_image.setImageResource(R.drawable.default_profile_icon);
                        }


                    }

                } else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
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


    private void showPhotoOptions() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Import Image From")
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

    //====================================

    private void EditProfileApi() {
        EditProfileSyncTemplate editProfileSyncTemplate = new EditProfileSyncTemplate();
        editProfileSyncTemplate.setFname(medittxtFrstName.getText().toString());
        editProfileSyncTemplate.setLname("  ");
        editProfileSyncTemplate.setNumber(medittxtPhone.getText().toString());
        editProfileSyncTemplate.setPost_code(medittxtPostalCode.getText().toString().trim());
//        editProfileSyncTemplate.setAddress1(medittxtAddress.getText().toString());
//        editProfileSyncTemplate.setAddress2(medittxtAddressTwo.getText().toString());
//        editProfileSyncTemplate.setLatitude("0.0");
//        editProfileSyncTemplate.setLongitude("0.0");
        editProfileSyncTemplate.setUser_type(String.valueOf(userType));
        editProfileSyncTemplate.setImgFilePath(image_path);
        editProfileSyncTemplate.setAbnNumber(mEdtxtAnNumber.getText().toString());
        editProfileSyncTemplate.setBusinessName(mEdtxtBusinessName.getText().toString());

        mLoader.show();
        mbtnSaveProfile.setEnabled(false);

        new SubmitProfileAsync(ActivityUpdateProfile.this, this, editProfileSyncTemplate).execute();
    }

    public void setData() {
        sharedPreferences = ActivityUpdateProfile.this
                .getSharedPreferences("preferences", Context.MODE_PRIVATE);
        // medittxtFrstName.setText(sharedPreferences.getString("user_name",""));
        medittxtEmail.setText(sharedPreferences.getString("email", ""));
        medittxtFrstName.setText(sharedPreferences.getString("fname", ""));
        medittxtLstName.setText(sharedPreferences.getString("lname", ""));
        medittxtPhone.setText(sharedPreferences.getString("mobilenumber", ""));
        medittxtAddress.setText(sharedPreferences.getString("address1", ""));
        medittxtAddressTwo.setText(sharedPreferences.getString("address2", ""));


        medittxtEmail.setEnabled(false);

        if (sharedPreferences.getString("image", "").equalsIgnoreCase("")
                || sharedPreferences.getString("image", "").equalsIgnoreCase("null")) {
            medit_profile_image.setImageResource(R.drawable.default_profile_icon);
        } else {
            Picasso.with(ActivityUpdateProfile.this)
                    .load(RippleittAppInstance
                            .formatPicPath(sharedPreferences.getString("image", "")))
                    .error(R.drawable.default_profile_icon)
                    .placeholder(R.drawable.default_profile_icon)
                    .into(medit_profile_image);
        }


    }

    @Override
    public void onInit() {
        Toast.makeText(ActivityUpdateProfile.this, "Updating your profile information", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(EditProfileSyncResponseTemplate template) {
        mLoader.hide();
        mbtnSaveProfile.setEnabled(true);
        Toast.makeText(ActivityUpdateProfile.this, template.getResponse_message(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(EditProfileSyncResponseTemplate template) {

        mLoader.hide();
        mbtnSaveProfile.setEnabled(true);

        Log.e("USER_DETAILS", template.getResponse_message() + "");
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUpdateProfile.this);
        builder.setMessage("You need to login again for changes to apply")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        SharedPreferences sharedPreferences = getSharedPreferences(RippleittAppInstance.PREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        try {
                            LoginManager.getInstance().logOut();
                        } catch (Exception e) {

                        }
                        Intent intent = new Intent(ActivityUpdateProfile.this, ActivityLogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

//        if (PreferenceHandler.readString(ActivityUpdateProfile.this, PreferenceHandler.USER_TYPE, "1")
//                .equalsIgnoreCase(template.getUserinformation().)) {

//        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("user_name", template.getUserinformation().getFname() + " " + template.getUserinformation().getLname());
//        editor.putString("fname", template.getUserinformation().getFname());
//        editor.putString("lname", template.getUserinformation().getLname());
//        editor.putString("email", template.getUserinformation().getEmail());
//        editor.putString("image", template.getUserinformation().getImage());
//        editor.putString("mobilenumber", template.getUserinformation().getMobilenumber());
//        editor.putString("longitude", template.getUserinformation().getLongitude());
//        editor.putString("latitude", template.getUserinformation().getLatitude());
//        editor.putString("latitude", template.getUserinformation().getLatitude());
//        editor.putString("address1", template.getUserinformation().getAddress1());
//        editor.putString("address2", template.getUserinformation().getAddress2());
////        editor.pu("post_code",payload.getPost_code());
//
//        editor.putString("abn_number", template.getUserinformation().getAbn_number());
//        editor.putString("business_name", template.getUserinformation().getBusiness_name());
//
//
//        editor.commit();
//        Toast.makeText(ActivityUpdateProfile.this,
//                "Your profile has been updated successfully...",
//                Toast.LENGTH_LONG).show();
//        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                launchCamera();
            } else {
                Toast.makeText(ActivityUpdateProfile.this,
                        "Camera permission denied", Toast.LENGTH_SHORT).show();
                ;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.rdbtnBusiness) {
            userType = 2;
            mEdtxtAnNumber.setVisibility(View.VISIBLE);
            mEdtxtBusinessName.setVisibility(View.VISIBLE);
        }
        if (checkedId == R.id.rdbtnPersonal) {
            userType = 1;
            mEdtxtAnNumber.setVisibility(View.GONE);
            mEdtxtBusinessName.setVisibility(View.GONE);
        }
    }


    private class SaveBitmapAsync extends AsyncTask<Void, Void, String> {


        Bitmap bitMap;
        boolean didCompressAndCopy = false;

        private SaveBitmapAsync(Bitmap fileToSave) {
            bitMap = fileToSave;
            pdialog = new ProgressDialog(ActivityUpdateProfile.this);
            pdialog.show();
        }

        @Override
        public void onPreExecute() {
            pdialog.setCancelable(false);
            pdialog.setMessage("Saving file");

        }

        @Override
        protected String doInBackground(Void... voids) {
            String filePath = "";
            try {
                String filename = "SKU_PIC_" + System.currentTimeMillis() + ".jpg";
                //fileDestination = new File(dirPath, filename);
                File file = new File(ActivityUpdateProfile.this.getCacheDir(), filename);
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
            pdialog.dismiss();
            Log.e(v, v);

            try {
                image_path = v;
                Picasso.Builder builder = new Picasso.Builder(ActivityUpdateProfile.this);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }
                });
                builder.build()
                        .load(new File(v))
                        .placeholder(R.drawable.default_profile_icon)
                        .error(R.drawable.default_profile_icon)
                        .into(medit_profile_image);

            } catch (Exception e) {
                medit_profile_image.setImageResource(R.drawable.default_profile_icon);
            }


        }
    }

}
