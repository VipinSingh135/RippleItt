package com.rippleitt.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityChatDetails;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.AddNewProduct;
import com.rippleitt.adapters.MessagesListAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.ChatTemplate;
import com.rippleitt.modals.ListingOwnerTemplate;
import com.rippleitt.modals.UsersDetailResponseTemplate;
import com.rippleitt.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentMessages extends Fragment implements AdapterView.OnItemClickListener {

    ListView mlistVwMessages;
    MessagesListAdapter messagesListAdapter;
    ImageView mimgvwtoggleMap;
    TextView mtxtVwTitileFragments;
    ImageView mimgvwInitFilter;
    ArrayList<String> userIDsArray = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private TextView mTxtVwNoMessages;
    private ListingOwnerTemplate[] chatPartner;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_messages, container, false);
        mlistVwMessages=(ListView)view.findViewById(R.id.listVwMessages);
        mTxtVwNoMessages=(TextView)view.findViewById(R.id.txtvwNoMessages);
        mTxtVwNoMessages.setVisibility(View.GONE);
        work();
        return view;
    }

    public void work(){

        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Messages");
    }

    @Override
    public void onResume(){
        super.onResume();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        userIDsArray = new ArrayList<>();
        fetchMessages();
    }



    private void fetchMessages(){
        mProgressDialog.setMessage("Updating your messages...");
        mProgressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String dataStorePath = "chatList/"+ PreferenceHandler
                                        .readString(getActivity(),
                                        PreferenceHandler.USER_ID,"");
        DatabaseReference myRef = database.getReference(dataStorePath);

        if(myRef!=null){
//            myRef.orderByChild("timestamp");
            final HashMap<String,Long> hashMap= new HashMap<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getChildren();

                    // now we find the people I have chatted with...
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                       HashMap<String,Long> chatTemplate= (HashMap<String, Long>) postSnapshot.getValue();
                       hashMap.put(postSnapshot.getKey(),chatTemplate.get("timestamp"));

//                        userIDsArray.add(postSnapshot.getKey());
                    }
                    sortByValues(hashMap);

                    // now we get the user details for these users...
                    volleyFetchUserDetails();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.getCode();
                    mTxtVwNoMessages.setVisibility(View.VISIBLE);
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    private  HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
            userIDsArray.add((String) entry.getKey());
        }
        return sortedHashMap;
    }
    private void volleyFetchUserDetails(){
        if(userIDsArray==null || userIDsArray.size()==0){
            //promptNoChats();
            mTxtVwNoMessages.setVisibility(View.VISIBLE);
            mProgressDialog.dismiss();
            return;
        }

        // now call the api  get user details...
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.GET_USER_DETAILS ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        try{
                            Log.d("", response.toString());
                            Gson gson = new Gson();
                            UsersDetailResponseTemplate response_ = (UsersDetailResponseTemplate)gson.fromJson(response,UsersDetailResponseTemplate.class);
                            if(response_.getResponse_code()==1){
                                chatPartner=response_.getData();
                                populateList(response_.getData());
                            }else{

                                // show response_message in alert...
                            }
                            CommonUtils.dismissProgress();
                        }catch (Exception e){

                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                 VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                String payload="";
                for(String s:userIDsArray){
                    payload=payload+s+",";
                }
                payload=payload.replaceAll("\\,$","");
                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(getActivity(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("users",payload);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");

    }

    private void promptNoChats(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You have not exchaged any messages")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mProgressDialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void populateList(ListingOwnerTemplate[] data){
        if(data.length!=0){
            messagesListAdapter=new MessagesListAdapter(getActivity(),data);
            mlistVwMessages.setAdapter(messagesListAdapter);
            mlistVwMessages.setOnItemClickListener(this);
        }
       // mlistVwMessages.setAdapter(null);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(chatPartner!=null && chatPartner[i]!=null){
            RippleittAppInstance.getInstance().setCurrentChatPartner(chatPartner[i]);
            startActivity(new Intent(getActivity(), ActivityChatDetails.class));
        }

    }
}
