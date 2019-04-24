package com.rippleitt.webservices;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rippleitt.R;
import com.rippleitt.adapters.SearchContactsAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.sdsmdg.tastytoast.TastyToast;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin on 10/24/2017.
 */
public class SearchContactApi
{
    public void searchContactApi(final Context context, final String keyword, final AVLoadingIndicatorView loader)
    {

        if(loader!=null)
            loader.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL +RippleittAppInstance.SEARCH_CONTACTS_LISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                        if(loader!=null){
                            loader.hide();
                        }
                    ArrayList<HashMap<String,String>> arrayList_contact=new ArrayList<>();
                  //  maviLoaderHome.hide();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("response_code");
                    String msg = object.getString("response_message");
                    Log.e("Status", status + "");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray arr_data=object.getJSONArray("data");


                        if(arr_data.length()==0){
                            TextView txtvwNoMatches=(TextView)((Activity)context).findViewById(R.id.txtvwNoResults);
                            txtvwNoMatches.setVisibility(View.VISIBLE);
                        }

                        for (int i=0;i<arr_data.length();i++){
                            JSONObject obj_data=arr_data.getJSONObject(i);
                            HashMap<String,String> hash_Data=new HashMap<>();
                            hash_Data.put("name",obj_data.getString("name")+" "+obj_data.getString("lastname"));
                            hash_Data.put("image",obj_data.getString("image"));
                            hash_Data.put("user_id",obj_data.getString("userid"));

                            arrayList_contact.add(hash_Data);
                        }
                        Log.e("ARRAY_CONTACTS",arrayList_contact+"");

                        ListView listVwContacts=(ListView)((Activity)context).findViewById(R.id.listVwContacts);
                        SearchContactsAdapter searchContactsAdapter=new SearchContactsAdapter(context,arrayList_contact);
                        listVwContacts.setAdapter(searchContactsAdapter);

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
               // params.put("token","46ce6a5908cb60d3e15c8a5caf67b9e1");
                params.put("searchname", keyword);
                Log.e("SerchContacts_Params",params+"");
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
