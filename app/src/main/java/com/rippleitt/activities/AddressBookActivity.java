package com.rippleitt.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.AddAddressAdapter;
import com.rippleitt.adapters.AddressBookListingAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.AddressBookShareTemplate;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.ContactSharingTemplate;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.FetchAddressBookApi;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import mx.com.quiin.contactpicker.SimpleContact;
import mx.com.quiin.contactpicker.ui.ContactPickerActivity;

public class AddressBookActivity extends AppCompatActivity implements View.OnClickListener {

    ListView mlistVwAddressBook;
    AddressBookListingAdapter addressBookListingAdapter;
    RelativeLayout mrelBackAddrBook;
   AVLoadingIndicatorView maviLoaderHome;
   TextView mTxtVwDone;
   private Button mBtnShareListing;
    private final int CONTACT_PICKER_REQUEST=89;
    private final int ACTION_READ_CONTACTS=90;
    private ArrayList<String> arrInternalContactsToShare = new ArrayList<>();
    private ArrayList<String> arrExternalContactsToShare = new ArrayList<>();
    private TextView mTxtVwNoContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        init();
    }


    @Override
    public void onResume(){
        super.onResume();
        volleyFetchAddressBook();
    }

    public void init() {
        mBtnShareListing=(Button)findViewById(R.id.btnShareAction);
        mlistVwAddressBook=(ListView)findViewById(R.id.listVwAddressBook);
        maviLoaderHome = (AVLoadingIndicatorView) findViewById(R.id.aviLoaderHome);
        mrelBackAddrBook=(RelativeLayout)findViewById(R.id.relBackAddrBook);
        mTxtVwNoContacts=(TextView)findViewById(R.id.txtvwNoConacts);
        mTxtVwDone=(TextView)findViewById(R.id.txtvwAdd);
        mrelBackAddrBook.setOnClickListener(this);
        mTxtVwDone.setOnClickListener(this);
        mBtnShareListing.setOnClickListener(this);
        //new FetchAddressBookApi().fetchAddressBookApi(this,"0",maviLoaderHome);
        startAnim();
    }


    @Override
    public void onClick(View view) {
        if(view==mrelBackAddrBook){
            finish();
        }if(view==mTxtVwDone){
            showOptionsDialog();
        }if(view==mBtnShareListing){
            shareListing();
        }
    }



    private void showOptionsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddressBookActivity.this);
        builder.setTitle("Add New Contact");

        // add a list
        String[] animals = {"Search on Rippleitt", "Import from Contacts", "Cancel"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        startActivity(new Intent(AddressBookActivity.this, ActivityAddContacts.class));
                        break;
                    case 1:

                        checkRuntimePermission();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;

                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed(){
        finish();
    }
    public void startAnim() {
        maviLoaderHome.show();
        // or avi.smoothToShow();
    }

    public void stopAnim() {
        maviLoaderHome.hide();
        // or avi.smoothToHide();
    }

    private void volleyFetchAddressBook(){
        maviLoaderHome.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL
                        +RippleittAppInstance.FETCH_ADDRESSBOOK_LIST,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AddressBookShareTemplate addressBook = (AddressBookShareTemplate)
                                new Gson().fromJson(response,AddressBookShareTemplate.class);
                        if(addressBook.getResponse_code().equalsIgnoreCase("1")) {
                            if (maviLoaderHome != null)
                                maviLoaderHome.setVisibility(View.GONE);

                            if((addressBook.getData()==null || addressBook.getData().length==0)
                                && (addressBook.getNonrippleittuserdata()==null || addressBook.getNonrippleittuserdata().length==0 )   ){
                                mTxtVwNoContacts.setVisibility(View.VISIBLE);
                                mBtnShareListing.setVisibility(View.GONE);
                                return;
                            }else{
                                mTxtVwNoContacts.setVisibility(View.GONE);
                                mBtnShareListing.setVisibility(View.VISIBLE);
                            }

                            RippleittAppInstance.getInstance().getUserCurrentAddressBook().clear();
                            ContactSharingTemplate headerOne = new ContactSharingTemplate();
                            headerOne.setIslabel(true);
                            headerOne.setTitle("Rippleitt Users");
                            RippleittAppInstance.getInstance()
                                    .getUserCurrentAddressBook()
                                    .add(headerOne);
                            if(addressBook.getData()!=null && addressBook.getData().length!=0){
                                for(ContactSharingTemplate currentContact : addressBook.getData()){
                                    currentContact.setIs_external(1);
                                    RippleittAppInstance.getInstance()
                                            .getUserCurrentAddressBook()
                                            .add(currentContact);
                                }
                            }
                            ContactSharingTemplate headerTwo = new ContactSharingTemplate();
                            headerTwo.setIslabel(true);
                            headerTwo.setTitle("Non-Rippleitt Users");
                            RippleittAppInstance.getInstance()
                                    .getUserCurrentAddressBook()
                                    .add(headerTwo);
                            if(addressBook.getNonrippleittuserdata()!=null && addressBook.getNonrippleittuserdata().length!=0){
                                for(ContactSharingTemplate currentContact : addressBook.getNonrippleittuserdata()){
                                    currentContact.setIs_external(2);
                                    RippleittAppInstance.getInstance()
                                            .getUserCurrentAddressBook()
                                            .add(currentContact);
                                }
                            }
                            if (addressBook.getData() == null || addressBook.getData().length == 0) {
                                //Toast.makeText(AddressBookActivity.this,
                                  //      "You do not have any people in your address book", Toast.LENGTH_LONG).show();
                            }

                            populateList();


                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddressBookActivity.this,
                        "could not fetch your address book, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(AddressBookActivity.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }

    private void populateList(){
        maviLoaderHome.setVisibility(View.GONE);
        final AddAddressAdapter addressBookListingAdapter = new AddAddressAdapter(AddressBookActivity.this,
                RippleittAppInstance.getInstance().getUserCurrentAddressBook()
        );
        mlistVwAddressBook.setAdapter(addressBookListingAdapter);

        mlistVwAddressBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(RippleittAppInstance
                        .getInstance()
                        .getUserCurrentAddressBook().get(i).getShare()==0){
                    String ownerId= RippleittAppInstance
                                            .getInstance()
                                            .getSELECTED_LISTING_DETAIL_OBJECT().getUserinformation().getUser_id();
                    if (RippleittAppInstance
                            .getInstance()
                            .getUserCurrentAddressBook().get(i).getUserid().equals(ownerId)){
                        Toast.makeText(AddressBookActivity.this,"Listing owner can not be selected to refer.",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        RippleittAppInstance
                                .getInstance()
                                .getUserCurrentAddressBook().get(i).setShare(1);
                    }
                }else{
                    RippleittAppInstance
                            .getInstance()
                            .getUserCurrentAddressBook().get(i).setShare(0);
                }

                /*
                AddAddressAdapter addressBookListingAdapter=new AddAddressAdapter(AddressBookActivity.this,
                        RippleittAppInstance.getInstance().getUserCurrentAddressBook()
                );
                mlistVwAddressBook.setAdapter(addressBookListingAdapter);
                */
                addressBookListingAdapter.notifyDataSetChanged();
            }
        });

        /*
        mlistVwAddressBook.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                showDeletionConfirmation(RippleittAppInstance.getInstance()
                                .getUserCurrentAddressBook().get(i).getUserid(),
                        (RippleittAppInstance.getInstance()
                                .getUserCurrentAddressBook()
                                .get(i).getIs_external())
                );
                return false;
            }
        });
        */
    }

    private void showDeletionConfirmation(final String contactID,final int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddressBookActivity.this);
        builder.setMessage("Remove this contact from your address book?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteContactVolley(contactID, type);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkRuntimePermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(AddressBookActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    AddressBookActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, ACTION_READ_CONTACTS);
        } else {
            importContacts();
        }
    }

    private void importContacts(){
        startActivity(new Intent(AddressBookActivity.this,ReadContactsActivity.class));
        /*
        new MultiContactPicker.Builder(AddressBookActivity.this) //Activity/fragment context
                //Optional - default: MultiContactPicker.Azure
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .handleColor(ContextCompat.getColor(AddressBookActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(AddressBookActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .showPickerForResult(CONTACT_PICKER_REQUEST);
                */
      //  Intent intent = new Intent(Intent.ACTION_PICK_ACTIVITY, ContactsContract.Contacts.CONTENT_URI);
        //startActivityForResult(intent, CONTACT_PICKER_REQUEST);
      //  Intent contactPicker = new Intent(this, ContactPickerActivity.class);
       // contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SHOW_CHIPS, false);
       // startActivityForResult(contactPicker, CONTACT_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CONTACT_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    List<ContactResult> results = MultiContactPicker.obtainResult(data);
                }else
                    Toast.makeText(this, "No contacts selected", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onActivityResult(requestCode,resultCode,data);
        }
    }


    private void shareListing(){
        // now we prepare the internal and external ids to be pushed...

        for(ContactSharingTemplate contact: RippleittAppInstance.getInstance().getUserCurrentAddressBook()){
            if( contact.getShare()==1 &&contact.getIs_external()==1){
                arrInternalContactsToShare.add(contact.getUserid());
            }if(contact.getShare()==1 &&contact.getIs_external()==2){
                arrExternalContactsToShare.add(contact.getUserid());
            }
        }
            if(arrInternalContactsToShare.size()==0 && arrExternalContactsToShare.size()==0){
                Toast.makeText(AddressBookActivity.this,"Please selcet a contact.",
                                        Toast.LENGTH_LONG).show();
                return;
            }

//            if (arrInternalContactsToShare.contains(PreferenceHandler.readString(AddressBookActivity.this,
//                    PreferenceHandler.AUTH_TOKEN, ""))){
//                Toast.makeText(AddressBookActivity.this,"Please selet a contact.",
//                        Toast.LENGTH_LONG).show();
//                return;
//            }
        volleyReferListing();
    }


    private void volleyReferListing(){
        maviLoaderHome.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.REFER_LISTING,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AddressBookShareTemplate addressBook = (AddressBookShareTemplate)
                                new Gson().fromJson(response,AddressBookShareTemplate.class);
                        if(addressBook.getResponse_code().equalsIgnoreCase("1")) {
                            if (maviLoaderHome != null)
                                maviLoaderHome.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddressBookActivity.this);
                            builder.setMessage("Listing has been successfully referred.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();


                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddressBookActivity.this,
                        "could not fetch your address book, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(AddressBookActivity.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id",RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());
                params.put("internal_contacts",new Gson().toJson(arrInternalContactsToShare));
                params.put("external_contacts",new Gson().toJson(arrExternalContactsToShare));
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }


    private void deleteContactVolley(final String contactID, final int type){
        maviLoaderHome.setVisibility(View.VISIBLE);
        String targetUrl =   RippleittAppInstance.REMOVE_CONTACT;
        if(type==2){
            targetUrl=   RippleittAppInstance.DELETE_EXTERNAL_CONTACT;
        }
        StringRequest sr = new StringRequest(Request.Method.POST,
                targetUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AddressBookShareTemplate addressBook = (AddressBookShareTemplate)
                                new Gson().fromJson(response,AddressBookShareTemplate.class);
                        if(addressBook!=null && addressBook.getResponse_code().equalsIgnoreCase("1")){
                            Toast.makeText(AddressBookActivity.this,
                                    "Contact has been removed from your Address Book",Toast.LENGTH_SHORT).show();
                            volleyFetchAddressBook();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddressBookActivity.this,
                        "could not complete your request.",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
//contact_id
                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(AddressBookActivity.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("contact_id",contactID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "delete_contact");
    }

}
