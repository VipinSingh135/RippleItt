package com.rippleitt.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rippleitt.R;
import com.rippleitt.adapters.FilterListAdapter;
import com.rippleitt.adapters.FilterSubCategoryAdapter;
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
public class FetchSubCategoryApi
{
    public void fetchSubCategoryApi(final Context context,final  String category_id)
    {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading..");
       // dialog.setCancelable(false);
        dialog.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL +RippleittAppInstance.FETCH_subCATEGORY_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e("total",total);
                Log.e("Response", response);
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("response_code");
                    String msg = object.getString("response_message");

                    ArrayList<HashMap<String,String>> arr_Category=new ArrayList<>();
                    Log.e("Status", status + "");
                    if (status.equalsIgnoreCase("1")) {

                        JSONArray arrData=object.getJSONArray("data");
                        for (int i=0;i<arrData.length();i++){

                            HashMap<String,String> hash_category=new HashMap<>();
                            JSONObject obj_category=arrData.getJSONObject(i);
                            hash_category.put("subcategory_id",obj_category.getString("id"));
                            hash_category.put("subcategory_name",obj_category.getString("name"));
                            arr_Category.add(hash_category);

                        }
                        if (arr_Category.size()!=0) {
                            LinearLayout mlinrSubCatLayout = (LinearLayout) ((Activity) context).findViewById(R.id.linrSubCatLayout);
                            mlinrSubCatLayout.setVisibility(View.VISIBLE);

                            ListView listVwSubCatFilter = (ListView) ((Activity) context).findViewById(R.id.listVwSubCatFilter);
                            FilterSubCategoryAdapter filterSubCategoryAdapter = new FilterSubCategoryAdapter(context, arr_Category);
                            listVwSubCatFilter.setAdapter(filterSubCategoryAdapter);
                        }
                        else {
                            TastyToast.makeText(context,"No such Sub Category Found..",TastyToast.LENGTH_SHORT,TastyToast.INFO);
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
                params.put("token", RippleittAppInstance.TOKEN_ID);
                params.put("category_id", category_id);
                Log.e("fetch_SubCategory_Params",params+"");
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
