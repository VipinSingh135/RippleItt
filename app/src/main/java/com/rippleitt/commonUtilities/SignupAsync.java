package com.rippleitt.commonUtilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rippleitt.activities.ActivityLogin;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.EditProfileSyncProcessCallback;
import com.rippleitt.modals.EditProfileSyncResponseTemplate;
import com.rippleitt.modals.EditProfileSyncTemplate;
import com.rippleitt.modals.IoCallback;
import com.rippleitt.modals.SignUpTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on password/22/2018.
 */

public class SignupAsync  extends AsyncTask<Void,Void,EditProfileSyncResponseTemplate> {

    private Context parentReference;
    private IoCallback callback;
    private SignUpTemplate payload;


    public SignupAsync(Context context, IoCallback responseCallback,
                       SignUpTemplate signUpTemplate){
        this.parentReference=context;
        this.callback=responseCallback;
        this.payload=signUpTemplate;
    }

    @Override
    public void onPreExecute(){
        callback.onInit();
    }

    @Override
    protected EditProfileSyncResponseTemplate doInBackground(Void... voids) {
        EditProfileSyncResponseTemplate responseTemplate = new EditProfileSyncResponseTemplate();
        // now we will post the lead object to the server...
        try{
            RippleittAppInstance.getInstance().setEmailToVerify(payload.getEmail());
            String countryCode="AUS";
            String charset = "UTF-8";
            String requestURL = RippleittAppInstance.getInstance().getSIGNUP();
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            // lets load the values in the request.
            multipart.addFormField("email",payload.getEmail());
            multipart.addFormField("password",payload.getPassword());
            multipart.addFormField("fname",payload.getFname());
            multipart.addFormField("lname",payload.getLname());
            multipart.addFormField("number",payload.getNumber());
            multipart.addFormField("gender","1");
            multipart.addFormField("address1",payload.getAddress1());
            multipart.addFormField("address2",payload.getAddress2());
            multipart.addFormField("latitude",payload.getLatitude());
            multipart.addFormField("longitude",payload.getLongitude());
            multipart.addFormField("user_type",Integer.toString(payload.getUserType()));
            multipart.addFormField("abn",payload.getAbnNumber());
            multipart.addFormField("post_code",payload.getPost_code());
            multipart.addFormField("business_name",payload.getBusinessName());
            multipart.addFormField("sms_promo",Integer.toString(payload.getSmsPromo()));
            multipart.addFormField("email_promo",Integer.toString(payload.getEmailPromo()));
            multipart.addFormField("country","Australia");
            multipart.addFormField("country_code",countryCode);
            multipart.addFormField("country_code__","as");
            if(!payload.getProfilePicPath().equalsIgnoreCase("")) {
                multipart.addFilePart("profile_pic", new File(payload.getProfilePicPath()));
            }else{
               // multipart.addFilePart("profile_pic", null);
            }

            List<String> response = multipart.finish();
            Log.v("rht", "SERVER REPLIED:");
            if(response!=null){
                String responsePacket = response.get(0);
                Gson gson = new Gson();
                EditProfileSyncResponseTemplate template = gson.fromJson(responsePacket,
                                                           EditProfileSyncResponseTemplate.class);
                return template;
            }else{
                EditProfileSyncResponseTemplate template=new EditProfileSyncResponseTemplate();
                template.setResponse_code("0");
                template.setResponse_message("internal error");
                return template;
            }
        }catch (Exception e){
            EditProfileSyncResponseTemplate template=new EditProfileSyncResponseTemplate();
            return template;
        }
    }

    @Override
    public void onPostExecute(EditProfileSyncResponseTemplate responseTemplate){
        if(responseTemplate!=null && responseTemplate.getResponse_code().equalsIgnoreCase("1")){
            Log.e("TEMPLATE_DATA",responseTemplate.getResponse_message());
//            Log.e("TEMPLATE_DATA",responseTemplate.getUserinformation().getEmail());

            SharedPreferences sharedPreferences=parentReference.getSharedPreferences("preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("user_id",responseTemplate.getUser_id());
            editor.commit();
            PreferenceHandler.writeString(parentReference,PreferenceHandler.INIT_MODE,PreferenceHandler.SIGNUP);

            callback.onSuccess(responseTemplate);
        }else{
            //responseTemplate.setResponse_code("0");
            //responseTemplate.setResponse_message("unable to sync this lead, please try again later...");
            callback.onError(responseTemplate);
        }
    }
}
