package com.rippleitt.commonUtilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingSyncProcessCallback;
import com.rippleitt.modals.ListingSyncResponseTemplate;
import com.rippleitt.modals.ListingSyncTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * Created by manishautomatic on 11/06/17.
 */
public class SubmitListingAsync extends AsyncTask<Void,Void,ListingSyncResponseTemplate> {

    private Context parentReference;
    private ListingSyncProcessCallback callback;
    private ListingSyncTemplate CURRENT_LISTING;
    private int publishMode=-1;


    public SubmitListingAsync(Context context, ListingSyncProcessCallback responseCallback, ListingSyncTemplate listingObject, int draft_flag){
            this.parentReference=context;
            this.callback=responseCallback;
            this.CURRENT_LISTING=listingObject;
            this.publishMode=draft_flag;
    }
//    {"response_code":0,"is_card_available":"1","response_message":" could not submit your listing.."}
    @Override
    public void onPreExecute(){
        callback.onInit();
    }

    @Override
    protected ListingSyncResponseTemplate doInBackground(Void... voids) {
        ListingSyncResponseTemplate responseTemplate = new ListingSyncResponseTemplate();
        // now we will post the lead object to the server...
        try{
            String charset = "UTF-8";
            String requestURL = RippleittAppInstance.getInstance().getSUBMIT_LISTING();
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            // lets load the values in the request.
            multipart.addFormField("token",PreferenceHandler.readString(parentReference,PreferenceHandler.AUTH_TOKEN,""));
            multipart.addFormField("productname",CURRENT_LISTING.getProductname());
            multipart.addFormField("category",CURRENT_LISTING.getCategory());
            multipart.addFormField("sub_category",CURRENT_LISTING.getSub_category());
            multipart.addFormField("description",CURRENT_LISTING.getDescription());
            multipart.addFormField("price",CURRENT_LISTING.getPrice());
            multipart.addFormField("longitude",CURRENT_LISTING.getLatitude());
            multipart.addFormField("latitude",CURRENT_LISTING.getLongitude());
            multipart.addFormField("address",CURRENT_LISTING.getAddress());
            multipart.addFormField("listing_type",CURRENT_LISTING.getListing_type());
            multipart.addFormField("is_drafted",Integer.toString(publishMode));
            multipart.addFormField("refer_amount",CURRENT_LISTING.getRewardAmount());
            multipart.addFormField("quantity",CURRENT_LISTING.getQuantity());
            multipart.addFormField("payment_mode",CURRENT_LISTING.getPayment_mode());
            multipart.addFormField("is_new",CURRENT_LISTING.getIs_new());
            multipart.addFormField("units",CURRENT_LISTING.getUnits());
            multipart.addFormField("shipping_type",CURRENT_LISTING.getShippingType());
            multipart.addFormField("zip_codes",CURRENT_LISTING.getZipCodes());
            multipart.addFormField("direct_buy",CURRENT_LISTING.getDirect_buy());

            List<String> keys = new ArrayList<>(RippleittAppInstance.getInstance().getListingImages().keySet());
            multipart.addFormField("photo_count", Integer.toString(keys.size()-1));
            for(int index=0;index<keys.size();index++){
                if(index==0){
                    multipart.addFilePart("listing_primary_pic", new File(keys.get(index)));
                }else{
                    multipart.addFilePart("listing_pic_"+Integer.toString(index-1), new File(keys.get(index)));
                }
            }
            List<String> response = multipart.finish();
            Log.v("rht", "SERVER REPLIED:");
            if(response!=null){
                String responsePacket = response.get(0);
                Gson gson = new Gson();
                ListingSyncResponseTemplate template = gson.fromJson(responsePacket,ListingSyncResponseTemplate.class);
                return template;
            }else{
                ListingSyncResponseTemplate template=new ListingSyncResponseTemplate();
                return template;
            }
        }catch (Exception e){
            Log.v("rht", "Error : "+ e);
            ListingSyncResponseTemplate template=new ListingSyncResponseTemplate();
            return template;
        }
    }

    @Override
    public void onPostExecute(ListingSyncResponseTemplate responseTemplate){
        if(responseTemplate!=null && !responseTemplate.getResponse_code().equalsIgnoreCase("")){
            responseTemplate.setDraft_flag(Integer.toString(publishMode));
                callback.onSuccess(responseTemplate);
        }else{
            responseTemplate.setResponse_code("0");
            responseTemplate.setResponse_message("unable to sync this lead, please try again later...");
            callback.onError(responseTemplate);
        }
    }

}
