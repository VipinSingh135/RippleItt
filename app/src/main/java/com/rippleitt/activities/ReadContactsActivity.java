package com.rippleitt.activities;

/**
 * Created by pc on mail/user/2018.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.rippleitt.adapters.CustomListAdapterContacts;
import com.rippleitt.callback.ItemSelectionCallback;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.AddressBookShareTemplate;
import com.rippleitt.modals.ContactTemplate;
import com.rippleitt.modals.ContactUploadTemplate;
import com.rippleitt.utils.CommonUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by manishautomatic on 29/01/16.
 */
public class ReadContactsActivity extends AppCompatActivity implements ItemSelectionCallback, View.OnClickListener {

    private Toolbar toolbar;
    private ListView mLstVwContacts;
    private ArrayList<ContactTemplate> mArrLstContactsList = new ArrayList<>();
    private ArrayList<ContactTemplate> mArrLstRefrenceContacts = new ArrayList<>();
    private CustomListAdapterContacts mCustomListAdapterContacts;
    private TextView  mBtnUnselectAll;
    private EditText mEdtxtQuickSearch;
    private Button mBtnImport;
    private SharedPreferences sharedPref;
    private ImageView mImgVwClearSearch;
    private final int ACTION_READ_CONTACTS=20;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_read_contacts);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        initializeLayout();
        mArrLstContactsList.clear();;
        setTextChangedListner();
        // verify if permission exists....
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.READ_CONTACTS}, ACTION_READ_CONTACTS);
        } else {
            new LoadContactsAyscn().execute();
            //startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + mTxtVwListingContactPhone.getText())));
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACTION_READ_CONTACTS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    new LoadContactsAyscn().execute();
                } else {
                    Toast.makeText(ReadContactsActivity.this,"Read contacts permission denied",Toast.LENGTH_SHORT).show();;
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(ReadContactsActivity.this);
    }

    private void setTextChangedListner() {
        mEdtxtQuickSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 0) {
                    mCustomListAdapterContacts.filter(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initializeLayout() {
        mImgVwClearSearch=(ImageView)findViewById(R.id.imgVwClearSearch);
        mImgVwClearSearch.setOnClickListener(this);
        mLstVwContacts = (ListView)findViewById(R.id.listView1);

        mBtnUnselectAll =(TextView)toolbar.findViewById(R.id.btnUnselectAll);

        mBtnUnselectAll.setOnClickListener(this);
        mEdtxtQuickSearch = (EditText)findViewById(R.id.inputSearch);
        mBtnImport=(Button)findViewById(R.id.btnImportAction);
        mBtnImport.setOnClickListener(this);
    }

    @Override
    public void onSelected(int position) {
        mArrLstRefrenceContacts.get(position).setIsselected(true);
    }

    @Override
    public void onUnSelected(int position) {
        mArrLstRefrenceContacts.get(position).setIsselected(false);
    }

    @Override
    public void setReferenceListContact(ArrayList<ContactTemplate> refrenceList) {
        this.mArrLstRefrenceContacts.clear();
        this.mArrLstRefrenceContacts.addAll(refrenceList);
    }




    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnUnselectAll){
            for (ContactTemplate template:mArrLstContactsList) {
                template.setIsselected(false);
                mCustomListAdapterContacts.notifyDataSetChanged();
            }

        }if(view.getId()==R.id.btnImportAction){
            mBtnImport.setEnabled(false);
            preparePatientRecords();

        }if(view==mImgVwClearSearch){
            mEdtxtQuickSearch.setText("");
        }


    }

    private void preparePatientRecords() {
        // first verify if max limit has not been exceeded.
        ArrayList<ContactTemplate> contactsToCopy = new ArrayList<>();
        contactsToCopy.clear();;
        ArrayList<ContactUploadTemplate> contactTemplate=new ArrayList<>();
        for(ContactTemplate contact :mArrLstRefrenceContacts) {
            if (contact.isselected()) {

                if(contact!=null
                        && (contact.getContactName()!=null)
                        && (contact.getContactPhoneNumber()!=null) ){

                    ContactUploadTemplate uploadTemplate= new ContactUploadTemplate();
                    uploadTemplate.setFname(contact.getContactName());
                    uploadTemplate.setLname("");
                    if(contact.getContactEmailAddress()!=null)
                        uploadTemplate.setEmail(contact.getContactEmailAddress());
                    if(contact.getContactPhoneNumber()!=null)
                        uploadTemplate.setMobilenumber(contact.getContactPhoneNumber());
                    contactTemplate.add(uploadTemplate);
                }


            }
        }
        if(contactTemplate.size()!=0) {
            volleyUploadContact(contactTemplate);
        }else{
            mBtnImport.setEnabled(true);
            Toast.makeText(ReadContactsActivity.this,"Please select a contact to import",Toast.LENGTH_SHORT).show();
        }
    }




    // load the contacts asynchronously...

    private class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<ContactTemplate>> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd = ProgressDialog.show(ReadContactsActivity.this, "Loading Contacts",
                    "Please Wait");
        }

        @Override
        protected ArrayList<ContactTemplate> doInBackground(Void... params) {
            // TODO Auto-generated method stub


            Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            while (c.moveToNext()) {
                ContactTemplate template = new ContactTemplate();
                template.setContactName(c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                template.setContactPhoneNumber(c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                template.setContactPhoneNumber(c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
                template.setIsselected(false);
                mArrLstContactsList.add(template);
                mArrLstRefrenceContacts.add(template);

            }
            c.close();

            return mArrLstContactsList;
        }

        @Override
        protected void onPostExecute(ArrayList<ContactTemplate> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(contacts);
            pd.cancel();
            mCustomListAdapterContacts = new CustomListAdapterContacts(ReadContactsActivity.this,
                    mArrLstContactsList,ReadContactsActivity.this);
            mLstVwContacts.setAdapter(mCustomListAdapterContacts);

        }

    }


    private void volleyUploadContact(final ArrayList<ContactUploadTemplate> contactList){

      final ProgressDialog pDialog =new ProgressDialog(ReadContactsActivity.this);
      pDialog.setCancelable(false);
      pDialog.setMessage("Importing your contacts");
      pDialog.show();;
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.ADD_EXTERNAL_CONTACT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        mBtnImport.setEnabled(true);
                        try{
                            AddressBookShareTemplate addressBook = (AddressBookShareTemplate)
                                    new Gson().fromJson(response,AddressBookShareTemplate.class);
                            if(addressBook.getResponse_code().equalsIgnoreCase("1")) {


                            }
                        }catch (Exception e){

                            Toast.makeText(ReadContactsActivity.this,
                                    "Could not import your contacts", Toast.LENGTH_SHORT).show();
                        }
                        AddressBookShareTemplate addressBook = (AddressBookShareTemplate)
                                new Gson().fromJson(response,AddressBookShareTemplate.class);
                        if(addressBook.getResponse_code().equalsIgnoreCase("1")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ReadContactsActivity.this);
                            builder.setMessage("Contacts were successfully imported to your Rippleitt Address Book")
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
                mBtnImport.setEnabled(true);
                pDialog.dismiss();
                Toast.makeText(ReadContactsActivity.this,
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
                params.put("token", PreferenceHandler.readString(ReadContactsActivity.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("users",payload);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }

}
