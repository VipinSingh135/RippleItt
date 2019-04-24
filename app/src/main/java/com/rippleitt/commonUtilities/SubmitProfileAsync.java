package com.rippleitt.commonUtilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.EditProfileSyncProcessCallback;
import com.rippleitt.modals.EditProfileSyncResponseTemplate;
import com.rippleitt.modals.EditProfileSyncTemplate;
import com.rippleitt.modals.ListingSyncResponseTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by manishautomatic on 11/06/17.
 */
public class SubmitProfileAsync extends AsyncTask<Void,Void,EditProfileSyncResponseTemplate> {

    private Context parentReference;
    private EditProfileSyncProcessCallback callback;
    private EditProfileSyncTemplate Edit_PRofile;


    public SubmitProfileAsync(Context context, EditProfileSyncProcessCallback responseCallback, EditProfileSyncTemplate listingObject){
            this.parentReference=context;
            this.callback=responseCallback;
            this.Edit_PRofile=listingObject;
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
            String charset = "UTF-8";
            String requestURL = RippleittAppInstance.getInstance().getEDIT_PROFILE();
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            // lets load the values in the request.
            multipart.addFormField("token",PreferenceHandler.readString(parentReference,PreferenceHandler.AUTH_TOKEN,""));
            multipart.addFormField("user_type",Edit_PRofile.getUser_type());
            multipart.addFormField("fname",Edit_PRofile.getFname());
            multipart.addFormField("lname",Edit_PRofile.getLname());
            multipart.addFormField("number",Edit_PRofile.getNumber());
            multipart.addFormField("address1",Edit_PRofile.getAddress1());
            multipart.addFormField("address2",Edit_PRofile.getAddress2());
            multipart.addFormField("latitude",Edit_PRofile.getLatitude());
            multipart.addFormField("longitude",Edit_PRofile.getLongitude());
            multipart.addFormField("abn_number",Edit_PRofile.getAbnNumber());
            multipart.addFormField("business_name",Edit_PRofile.getBusinessName());

            Log.e("EDITPROFILE_PARAMS",multipart+"");

           // List<String> keys = new ArrayList<>(RippleittAppInstance.getInstance().getListingImages().keySet());

            if(!Edit_PRofile.getImgFilePath().equalsIgnoreCase("")){
                multipart.addFilePart("profile_pic", new File(Edit_PRofile.getImgFilePath()));
            }else{
                //multipart.addFilePart("profile_pic", new File(Edit_PRofile.getImgFilePath()));
            }


            List<String> response = multipart.finish();
            Log.v("rht", "SERVER REPLIED:");
            if(response!=null){
                String responsePacket = response.get(0);
                Gson gson = new Gson();
                EditProfileSyncResponseTemplate template = gson.fromJson(responsePacket,EditProfileSyncResponseTemplate.class);
                return template;
            }else{
                EditProfileSyncResponseTemplate template=new EditProfileSyncResponseTemplate();
                return template;
            }
        }catch (Exception e){
            EditProfileSyncResponseTemplate template=new EditProfileSyncResponseTemplate();
            return template;
        }
    }

    @Override
    public void onPostExecute(EditProfileSyncResponseTemplate responseTemplate){
        if(responseTemplate!=null && !responseTemplate.getResponse_code().equalsIgnoreCase("0")){
            Log.e("TEMPLATE_DATA",responseTemplate.getResponse_message());
//            Log.e("TEMPLATE_DATA",responseTemplate.getUserinformation().getEmail());
                callback.onSuccess(responseTemplate);
        }else{
            //responseTemplate.setResponse_code("0");
            //responseTemplate.setResponse_message("unable to update your information at the moment...");
            callback.onError(responseTemplate);
        }

    }
}
