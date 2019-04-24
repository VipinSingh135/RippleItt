package com.rippleitt.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityAddContacts;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.AddNewProduct;
import com.rippleitt.activities.ReadContactsActivity;
import com.rippleitt.adapters.AddAddressAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.AddressBookShareTemplate;
import com.rippleitt.modals.ContactSharingTemplate;
import com.rippleitt.modals.ContactUploadTemplate;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.FetchAddressBookApi;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentAddressBook extends Fragment implements View.OnClickListener {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    private AVLoadingIndicatorView maviLoaderHome;
    private ListView mLstVwAddressBook ;
    private final int CONTACT_PICKER_REQUEST=89;
    private final int ACTION_READ_CONTACTS=90;
    private boolean shouldTriggerFetch=true;
    private TextView mTxtVwNoContacts;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_address_book, container, false);
        mTxtVwNoContacts=(TextView) view.findViewById(R.id.txtvwNoContacts);
        mTxtVwNoContacts.setVisibility(View.GONE);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        //mimgvwInitFilter.setVisibility(View.GONE);
        mLstVwAddressBook=(ListView)view.findViewById(R.id.lstvwUserAddressBook);
        maviLoaderHome=(AVLoadingIndicatorView) view.findViewById(R.id.aviLoaderAddressBook);
        mimgvwInitFilter.setImageDrawable(getActivity()
                .getResources().getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
        mimgvwInitFilter.setColorFilter(Color.argb(255, 255, 255, 255));
        mimgvwInitFilter.setOnClickListener(this);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity())
                .findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Address Book");
        mimgvwInitFilter.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view==mimgvwInitFilter){
            if(((ActivityHome)getActivity()).getTriggerMode()==2){
                showOptionsDialog();

            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Toast.makeText(getActivity(),"Updating your address book",Toast.LENGTH_LONG).show();
        if(shouldTriggerFetch)
             volleyFetchAddressBook();

        mTxtVwNoContacts.setVisibility(View.GONE);

       // new FetchAddressBookApi().fetchAddressBookApi(getActivity(),"0",maviLoaderHome);
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

                            if((addressBook.getData()==null || addressBook.getData().length==0)
                                    &&
                                    (addressBook.getNonrippleittuserdata()==null || addressBook.getNonrippleittuserdata().length==0)
                                    ){
                                mTxtVwNoContacts.setVisibility(View.VISIBLE);
                            }
                            if (maviLoaderHome != null)
                                maviLoaderHome.setVisibility(View.GONE);
                            // now populate the headers and list.
                            RippleittAppInstance.getInstance().getUserCurrentAddressBook().clear();
                            ContactSharingTemplate headerOne = new ContactSharingTemplate();
                            headerOne.setIslabel(true);
                            headerOne.setTitle("Rippleitt Users");
                            RippleittAppInstance.getInstance()
                                    .getUserCurrentAddressBook()
                                    .add(headerOne);
                            if(addressBook.getData()!=null && addressBook.getData().length!=0){
                                for(ContactSharingTemplate currentContact : addressBook.getData()){
                                    currentContact.setType(1);
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
                                    currentContact.setType(2);
                                    RippleittAppInstance.getInstance()
                                            .getUserCurrentAddressBook()
                                            .add(currentContact);
                                }
                            }
                            populateList();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "could not fetch your address book, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(getActivity(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }

    private void populateList(){
        AddAddressAdapter addressBookListingAdapter = new AddAddressAdapter(getActivity(),
                RippleittAppInstance.getInstance().getUserCurrentAddressBook()
        );
        mLstVwAddressBook.setAdapter(addressBookListingAdapter);
        mLstVwAddressBook.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                showDeletionConfirmation(RippleittAppInstance.getInstance()
                        .getUserCurrentAddressBook().get(i).getUserid(),
                        (RippleittAppInstance.getInstance()
                        .getUserCurrentAddressBook()
                        .get(i).getType())
                        );
                return false;
            }
        });

    }

    private void showDeletionConfirmation(final String contactID,final int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                Toast.makeText(getActivity(),
                                        "Contact has been removed from your Address Book",Toast.LENGTH_SHORT).show();
                                volleyFetchAddressBook();
                            }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
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
                params.put("token", PreferenceHandler.readString(getActivity(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("contact_id",contactID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "delete_contact");
    }

    private void showOptionsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Contact");

        // add a list
        String[] animals = {"Search on Rippleitt", "Import from Contacts", "Cancel"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        startActivity(new Intent(getContext(), ActivityAddContacts.class));
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

    private void importContacts(){
        startActivity(new Intent(getActivity(),ReadContactsActivity.class));
        /*
        new MultiContactPicker.Builder(getActivity()) //Activity/fragment context
               //Optional - default: MultiContactPicker.Azure
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .handleColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .showPickerForResult(CONTACT_PICKER_REQUEST);
                */
    }


    private void checkRuntimePermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, ACTION_READ_CONTACTS);
        } else {
           importContacts();
        }
    }


    public void readContacts(List<ContactResult> results){
        if(results!=null && results.size()!=0){
            shouldTriggerFetch=false;
                //now we prepare the upload packet.
            ArrayList<ContactUploadTemplate> contactTemplate=new ArrayList<>();
            for(ContactResult contact : results){

                ContactUploadTemplate uploadTemplate= new ContactUploadTemplate();
                uploadTemplate.setFname(contact.getDisplayName());
                uploadTemplate.setLname("");
                if(contact.getEmails().size()!=0)
                uploadTemplate.setEmail(contact.getEmails().get(0));
                if(contact.getPhoneNumbers().size()!=0)
                uploadTemplate.setMobilenumber(contact.getPhoneNumbers().get(0));
                contactTemplate.add(uploadTemplate);
            }
            volleyUploadContact(contactTemplate);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACTION_READ_CONTACTS :
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                   importContacts();
                } else {
                    Toast.makeText(getActivity(),
                            "Read contact permission denied",
                            Toast.LENGTH_SHORT).show();;
                }
                break;

            default:
                break;
        }
    }

    private void volleyUploadContact(final ArrayList<ContactUploadTemplate> contactList){

        maviLoaderHome.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.ADD_EXTERNAL_CONTACT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            AddressBookShareTemplate addressBook = (AddressBookShareTemplate)
                                    new Gson().fromJson(response,AddressBookShareTemplate.class);
                            if(addressBook.getResponse_code().equalsIgnoreCase("1")) {


                            }
                        }catch (Exception e){

                            Toast.makeText(getActivity(),
                                    "Could not import your contacts", Toast.LENGTH_SHORT).show();
                        }
                        AddressBookShareTemplate addressBook = (AddressBookShareTemplate)
                                new Gson().fromJson(response,AddressBookShareTemplate.class);
                        if(addressBook.getResponse_code().equalsIgnoreCase("1")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Contacts were successfully imported to your Rippleitt Address Book")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            volleyFetchAddressBook();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "could not fetch your address book, please try again",Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                String payload="";
                if(contactList.size()!=0){
                    Gson g = new Gson();
                    payload = g.toJson(contactList);
                }
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

}
