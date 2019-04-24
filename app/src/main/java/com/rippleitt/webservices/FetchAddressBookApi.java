package com.rippleitt.webservices;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.AddAddressAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.AddressBookShareTemplate;
import com.sdsmdg.tastytoast.TastyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin on 10/24/2017.
 */
public class FetchAddressBookApi
{
    public void fetchAddressBookApi(final Context context, final String page, final AVLoadingIndicatorView maviLoaderHome )
    {
        final ArrayList<HashMap<String,String>> arry_Details=new ArrayList<>();

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest myRqst = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL +RippleittAppInstance.FETCH_ADDRESSBOOK_LIST,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    AddressBookShareTemplate addressBook = (AddressBookShareTemplate)
                            new Gson().fromJson(response,AddressBookShareTemplate.class);
                    if(addressBook.getResponse_code().equalsIgnoreCase("1")){
                        if(maviLoaderHome!=null)
                        maviLoaderHome.setVisibility(View.GONE);
                      //  RippleittAppInstance.getInstance().setUserCurrentAddressBook(addressBook.getData());
                        if(addressBook.getData()==null ||addressBook.getData().length==0 ){
                            Toast.makeText(context,"You do not have any people in your address book",Toast.LENGTH_LONG).show();
                        }
                       final ListView mlistVwAddressBook=(ListView)((Activity)context).findViewById(R.id.lstvwUserAddressBook);
                         AddAddressAdapter addressBookListingAdapter=new AddAddressAdapter(context,
                                RippleittAppInstance.getInstance().getUserCurrentAddressBook()
                                );
                        mlistVwAddressBook.setAdapter(addressBookListingAdapter);

                        mlistVwAddressBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                /*
                                if(RippleittAppInstance
                                        .getInstance()
                                        .getUserCurrentAddressBook()[i].getShare()==0){
                                    RippleittAppInstance
                                            .getInstance()
                                            .getUserCurrentAddressBook()[i].setShare(1);
                                }else{
                                    RippleittAppInstance
                                            .getInstance()
                                            .getUserCurrentAddressBook()[i].setShare(0);
                                }
                                AddAddressAdapter addressBookListingAdapter=new AddAddressAdapter(context,
                                        RippleittAppInstance.getInstance().getUserCurrentAddressBook()
                                );
                                mlistVwAddressBook.setAdapter(addressBookListingAdapter);

                                */
                            }
                        });
                    }


                } catch (Exception e) {
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
               // params.put("token", RippleittAppInstance.TOKEN_ID);
                params.put("token", PreferenceHandler.readString(context,
                        PreferenceHandler.AUTH_TOKEN, ""));  params.put("page", page);
                Log.e("Fetching_List_Params",params+"");
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
