package com.rippleitt.commonUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hp on 05-03-2018.
 */

public class CommonUtils {


    /**
     * Create MediaFile Directory in external Storage
     */
    public static String createMediaDir() {
        String dirPath="";
        try {
            dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Ripple/";
            File dir_temp = new File(dirPath);
            if (!dir_temp.exists())
                dir_temp.mkdirs();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }


    private static ProgressDialog progress_Dialog = null;


    //TO VALIDATE EMAIL
    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public static void showProgress(Context context, String message) {

        // check if progress dialog is already visible. If visible, then remove it.
        dismissProgress();

        progress_Dialog = new ProgressDialog(context);
        //progress_Dialog = new ProgressDialog(context);
        //progress_Dialog.
        progress_Dialog.setCancelable(false);
        progress_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress_Dialog.setMessage(message);
        progress_Dialog.show();
    }


    public static void dismissProgress() {

        if (progress_Dialog != null) {
            try {
                if (progress_Dialog.isShowing()) {
                    progress_Dialog.dismiss();
                    progress_Dialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //____________________________________________________________________________________________________________________________
//TO VALIDATE PHONE NUMBER
    public static boolean isValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }

    //____________________________________________________________________________________________________________________________
    // bring cursor focus on the mentioned editbox
    public static void requestFocus(View view, Context context) {
        if (view.requestFocus()) {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //------------------------------hide_KeyBoard--------------------------------------------
    public static void keyboardHide(Context con, View v) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    con.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {

        }
    }

    //======================change Fragments==================
    public static void changeFragment(Context context, Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frmlytFragmentContainer, fragment).addToBackStack(null).commit();
    }

  //======================minute to hours==================
    public static String minutesToHours(int mins) {
        int hours = mins / 60; //since both are ints, you get an int
        int minutes = mins % 60;
        if (hours<=0)
            return  String.valueOf(minutes)+" mins ago";
        else if (hours==1)
            return  String.valueOf(hours)+" hr "+ String.valueOf(minutes)+" mins ago";
        else
            return  String.valueOf(hours)+" hrs "+ String.valueOf(minutes)+" mins ago";
    }

    //=================encrypt password==========

    public static String md5(String s) {

        try {

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }




    //===============split url=======

    // String url=productDetails.get(position).get("user_image");

    public static String splitUrl(String image_path) {
       // Log.e("URL_FOR_IMAGE",image_path);
        Uri uri = Uri.parse(image_path);
        String[] path = uri.getPath().split("/");
        String image = RippleittAppInstance.IMAGE_BASE_URL+ path[path.length - 1];

        Log.e("====IMAGE_SPLIT===", image);
        return image;
    }
}