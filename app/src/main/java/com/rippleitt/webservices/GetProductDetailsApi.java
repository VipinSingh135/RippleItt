package com.rippleitt.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin on 10/24/2017.
 */
public class GetProductDetailsApi
{
    public void getProductDetailsApi(final Context context,final String listing_id)
    {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL +RippleittAppInstance.FETCHING_PRODUCT_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e("total",total);
                Log.e("Response", response);
                try {
                    ArrayList<HashMap<String,String>>arrProductDetails=new ArrayList<>();
                    HashMap<String,String> hashProductDetails=new HashMap<>();

                    JSONObject object = new JSONObject(response);
                    String status = object.getString("response_code");
                    String msg = object.getString("response_message");

                    Log.e("Status", status + "");
                    if (status.equalsIgnoreCase("1")) {

                        JSONObject data=object.getJSONObject("data");
                        String productName=data.getString("listing_name");
                        String productId=data.getString("listing_id");

                        hashProductDetails.put("product_id",productId);
                        hashProductDetails.put("product_name",productName);

                        JSONObject categoryObj=data.getJSONObject("category");
                        String categoryName=categoryObj.getString("name");
                        String categoryId=categoryObj.getString("id");

                        hashProductDetails.put("category_id",categoryId);
                        hashProductDetails.put("category_name",categoryName);

                        JSONObject subCategoryObj=data.getJSONObject("subcategory");
                        String subCategoryName=subCategoryObj.getString("name");
                        String subCategoryId=subCategoryObj.getString("id");

                        hashProductDetails.put("subcat_id",subCategoryId);
                        hashProductDetails.put("subcat_name",subCategoryName);

                        JSONObject locationObj=data.getJSONObject("location");
                        String lat=locationObj.getString("latitude");
                        String lng=locationObj.getString("longitude");
                        String addr=locationObj.getString("address");

                        hashProductDetails.put("lat",lat);
                        hashProductDetails.put("lng",lng);
                        hashProductDetails.put("addr",addr);

                        String productPrice=data.getString("listing_price");
                        String rating=data.getString("listing_rating");
                        String description=data.getString("listing_description");

                        hashProductDetails.put("product_price",productPrice);
                        hashProductDetails.put("rating",rating);
                        hashProductDetails.put("description",description);

                        JSONObject userinformationObj=data.getJSONObject("userinformation");
                        String userName=userinformationObj.getString("fname")+" "+userinformationObj.getString("lname");
                        String email=userinformationObj.getString("email");
                        String mobilenumber=userinformationObj.getString("mobilenumber");
                        String userImage=userinformationObj.getString("image");

                        hashProductDetails.put("user_name",userName);
                        hashProductDetails.put("email",email);
                        hashProductDetails.put("mobile_number",mobilenumber);
                        hashProductDetails.put("user_image",userImage);
                        hashProductDetails.put("wishlist_status",data.getString("isaddedtowishlist"));



                        JSONArray arrProductImges=data.getJSONArray("listing_photos");

                        ArrayList<HashMap<String,String>> arr_images=new ArrayList<>();
                        for (int i=0;i<arrProductImges.length();i++){
                            JSONObject objProductImages=arrProductImges.getJSONObject(i);

                            HashMap<String,String> hash_img=new HashMap<>();
                            hash_img.put("product_pic",objProductImages.getString("photo_path"));
                            hash_img.put("product_pic_id",objProductImages.getString("photo_id"));

                          // hashProductDetails.put("product_image",objProductImages.getString("photo_path"));

                           arr_images.add(hash_img);
                            Log.e("ARRAY_PRODUCT_IMAGE",arr_images+"");
                        }
                        RippleittAppInstance.setArrayImages(arr_images);
                       // Log.e("ARRAY_PRODUCT_IMAGE_OUT",arr_images+"");

                        Log.e("GlobalImage_LIST",arr_images+"");

                        RippleittAppInstance.setHashMapProductDetails(hashProductDetails);

                        dialog.dismiss();
                     //=============navigate to Product Details screen===============
                        context.startActivity(new Intent(context, ProductDetailsActivity.class));

                        //=================================
                        Log.e("ARRAY_PRODUCT_DATA",hashProductDetails.toString());
                        //Log.e("ARRAY_PRODUCT_IMAGE",arr_images+"");
                    }
                    else {
                        TastyToast.makeText(context,msg,TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
                params.put("token", PreferenceHandler.readString(context,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id", listing_id);
                Log.e("GetPRoductDetail_Params",params+"");
                return params;
            }
        };
        myRqst.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(myRqst);
    }
}
