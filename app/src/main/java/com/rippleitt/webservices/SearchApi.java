package com.rippleitt.webservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

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
import com.rippleitt.adapters.HomeProductsAdapter;
import com.rippleitt.controller.RippleittAppInstance;
import com.sdsmdg.tastytoast.TastyToast;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin on 10/24/2017.
 */
public class SearchApi
{
    public void searchApi(final Context context,final String screen_status, final String page ,final String keyword ,final String category_id,final String sub_category_id,final AVLoadingIndicatorView maviLoaderHome)
    {
        final ArrayList<HashMap<String,String>> arry_Details=new ArrayList<>();

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL +
                RippleittAppInstance.CRITERIA_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {

                   // maviLoaderHome.hide();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("response_code");
                    String msg = object.getString("response_message");
                    Log.e("Status", status + "");
                    if (status.equalsIgnoreCase("1")) {

                        String pages_count = object.getString("totalpages");
                        String current_page = object.getString("currentpage");
                        String total_count = object.getString("totalcount");

                        JSONArray data_array = object.getJSONArray("data");

                        for (int i = 0; i < data_array.length(); i++) {
                            HashMap<String, String> hashData = new HashMap<>();
                            JSONObject obj_data = data_array.getJSONObject(i);

                            JSONObject obj_cat = obj_data.getJSONObject("category");
                            JSONObject obj_subCat = obj_data.getJSONObject("subcategory");
                            JSONObject obj_userinformation = obj_data.getJSONObject("userinformation");
                            JSONArray arr_prod_Photos = obj_data.getJSONArray("listing_photos");

                            hashData.put("product_id", obj_data.getString("listing_id"));
                            hashData.put("product_name", obj_data.getString("productname"));
                            hashData.put("product_price", obj_data.getString("listing_price"));
                            hashData.put("product_desc", obj_data.getString("listing_description"));
                            hashData.put("is_live", obj_data.getString("is_live"));

                            hashData.put("category_name", obj_cat.getString("name"));
                            hashData.put("category_id", obj_cat.getString("id"));

                            hashData.put("sub_category_id", obj_subCat.getString("name"));
                            hashData.put("sub_category_id", obj_subCat.getString("id"));

                            hashData.put("latitude", obj_data.getString("latitude"));
                            hashData.put("latitude", obj_data.getString("longitude"));
                            //  hashData.put("address",obj_location.getString("address"));

                            hashData.put("user_name", obj_userinformation.getString("fname") + " " + obj_userinformation.getString("lname"));
                            hashData.put("user_email", obj_userinformation.getString("email"));
                            hashData.put("user_mobilenumber", obj_userinformation.getString("mobilenumber"));
                            hashData.put("user_image", obj_userinformation.getString("image"));

                            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                            for (int j = 0; j < arr_prod_Photos.length(); j++) {
                                HashMap<String, String> hash_product_photos = new HashMap<>();
                                JSONObject obj_photos = arr_prod_Photos.getJSONObject(0);

                                hashData.put("product_id", obj_photos.getString("photo_id"));
                                hashData.put("product_pic", obj_photos.getString("photo_path"));

                                //arrayList.add(hash_product_photos);
                            }
                            Log.e("ARRAYLIST_IMAGES_search", arrayList + "");

                            arry_Details.add(hashData);
                            Log.e("ARRAYLIST_PRODUCT_Search", arry_Details + "");

                        }

                     /*   URL url = null;
                        try {
                           url = new URL(arry_Details.get(0).get("user_image"));

                          //  int id = Integer.parseInt(idStr);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        String[] segments = url.getPath().split("/");
                        String idStr = segments[segments.length-address];
                        Log.e("URL_IMAGE",idStr);
*/
                        if (arry_Details.size() == 0) {
                            TastyToast.makeText(context, "No Such listing forund..", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                        if (screen_status=="address") {
                            ((Activity)context).finish();

                           // context.startActivity(new Intent(context, ActivityHome.class));

                           /* RecyclerView recyclerView = (RecyclerView) ((Activity) context).findViewById(R.id.recyclerViewHomeProducts);
                            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(business, LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(staggeredGridLayoutManager);
                            HomeProductsAdapter customAdapter = new HomeProductsAdapter(context, arry_Details);
                            recyclerView.setAdapter(customAdapter);*/

                        }
                        else if (screen_status=="0"){
                        RecyclerView recyclerView = (RecyclerView) ((Activity) context).findViewById(R.id.recyclerViewHomeProducts);
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                       // HomeProductsAdapter customAdapter = new HomeProductsAdapter(context, arry_Details);
                       // recyclerView.setAdapter(customAdapter);
                        }
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

                params.put("page", page);
                params.put("category_id", category_id);
                params.put("sub_category_id", sub_category_id);
                params.put("keyword", keyword);
                Log.e("Search_List_Params",params+"");
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
