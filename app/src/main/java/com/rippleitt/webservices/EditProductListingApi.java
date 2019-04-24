package com.rippleitt.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rippleitt.controller.RippleittAppInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ravinder Pal Singh on 11/26/2016.
 */

public class EditProductListingApi extends AsyncTask<String, Void, String> {
    SharedPreferences sh;
    private URL connectURL;
    private String imagepath;
    private String response;
    byte[] dataToServer;
    private String fname, about, lname;
    Context c;
    int status;
   // private final ProgressDialog dialog;
    DataOutputStream dos = null;
    String jResponse;
    String thumbnail;
    ProgressDialog dialog ;
    String aa[]={"listing_pic_0","listing_pic_1","listing_pic_2","listing_pic_3","listing_pic_4"};

    String message="";
    ArrayList arrayList;
    String mproductName, mproductPrice,  mproductDescription,  mWeight,  mUnit,  mcategory,latitude,longitude,address;

    public EditProductListingApi(Context mContext, String mproductName, String mproductPrice, String mproductDescription, String mWeight, String mUnit, String mcategory, String latitude, String longitude, String address, ArrayList arryImages) {

        this.arrayList=arryImages;
        this.c = mContext;
        this.mproductName=mproductName;
        this.mproductPrice=mproductPrice;
        this.mproductDescription=mproductDescription;
        this.mWeight=mWeight;
        this.mUnit=mUnit;
        this.mcategory=mcategory;
        this.latitude=latitude;
        this.longitude=longitude;
        this.address=address;
        dialog = new ProgressDialog(c);

    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Please Wait.....");
        dialog.setCancelable(false);
        dialog.show();

       /* dialog = new ProgressDialog(c);*/
        try {
            connectURL = new URL(RippleittAppInstance.BASE_URL+RippleittAppInstance.EDIT_PRODUCT_LISTING);
        } catch (Exception ex) {
            Log.i("URL FORMATION", "MALFORMATED URL");
        }

    }

    @Override
    protected String doInBackground(String... params) {

        FileInputStream fileInputStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag = "3rd";
        String existingfilename = "";


        Log.e("FILE TYPE & FILE NAME", " " + arrayList);

        try {

            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

            conn.setDoInput(true);

            conn.setDoOutput(true);

            conn.setUseCaches(false);

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"token\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[0] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"productname\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[1] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"category\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[2] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);


            dos.writeBytes("Content-Disposition: form-data; name=\"photo_count\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[3] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"sub_category\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[4] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[5] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"price\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[6] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"is_drafted\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[7] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"longitude\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[8] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"latitude\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[9] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);


            dos.writeBytes("Content-Disposition: form-data; name=\"address\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[10] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            Log.e("Data", " " + " " + imagepath);

            for (int i = 0; i < arrayList.size(); i++)
            {
                Log.e("Enter the for loop", "Enter the forloop");

                String key="";
                File f;
                f=new File(arrayList.get(i).toString());
             /*   if(i==0)
                {
                    f = new File(imagepath);
                }
                else
                {
                    f = new File(thumbnail);
                }*/

                Log.e("KEYYYYYSSSS", aa[i]);
                //f = new File(imagepath);
                fileInputStream = new FileInputStream(f);
                existingfilename = f.getName();
              Log.e("Send_PArms",existingfilename);


                dos.writeBytes("Content-Disposition: form-data; name=\""+ aa[i] +"\";filename=\""+existingfilename + "\""
                        + lineEnd);

                if (existingfilename.endsWith(".jpg")) {
                    dos.writeBytes("Content-type: image/jpg;" + lineEnd);
                }
                if (existingfilename.endsWith(".png")) {
                    dos.writeBytes("Content-type: image/png;" + lineEnd);
                }
                if (existingfilename.endsWith(".gif"))
                {
                    dos.writeBytes("Content-type: image/gif;" + lineEnd);
                }
                if (existingfilename.endsWith(".jpeg"))
                {
                    dos.writeBytes("Content-type: image/jpeg;" + lineEnd);
                }

                //==============for_video==================
                if (existingfilename.endsWith(".mp4")) {
                    Log.i("videotype", "1");
                    dos.writeBytes("Content-type: video/mp4;" + lineEnd);
                }
                if (existingfilename.endsWith(".avi")) {
                    Log.i("videotype", "2");
                    dos.writeBytes("Content-type: video/avi;" + lineEnd);
                }
                if (existingfilename.endsWith(".ogg")) {
                    Log.i("videotype", "3");
                    dos.writeBytes("Content-type: video/ogg;" + lineEnd);
                }
                if (existingfilename.endsWith(".3gp")) {
                    Log.i("videotype", "4");
                    dos.writeBytes("Content-type: video/3gp;" + lineEnd);
                }
                //====================audio===================
                if (existingfilename.endsWith(".mp3")) {
                    Log.i("musictype", "5");
                    dos.writeBytes("Content-type: audio/mp3;" + lineEnd);
                }
                //================doc=======================
                if (existingfilename.endsWith(".doc")) {
                    Log.i("Doctype", "5");
                    dos.writeBytes("Content-type: text/doc;" + lineEnd);
                }
                dos.writeBytes(lineEnd);
                // **********
                int bytesAvailable = fileInputStream.available();

                int maxBufferSize = 1024*1024;

                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                if(i< arrayList.size())
                {
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    //dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    Log.e("VALUE",""+i);
                }
                else
                {
                    if(i+1==arrayList.size())
                    {
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                        Log.e("VALUE",""+i);
                    }
                    else
                    {
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        //dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                        Log.e("VALUE",""+i);

                    }
                }

              //  dos.writeBytes(lineEnd);
              //  dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                Log.e(Tag, "File is written");
                fileInputStream.close();
                dos.flush();
            }

            InputStream is = conn.getInputStream();

            int ch;

            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            jResponse = b.toString();

            response = "true";
            dos.close();

            dos.flush();
            Log.e("Upload image API ", jResponse);

            JSONObject jobj = new JSONObject(jResponse);
            status = jobj.getInt("response_code");

            Log.d("status", String.valueOf(status));

            if (status == 0) {
                message= jobj.getString("response_message");
            } else {
                message= jobj.getString("response_message");
            }

        } catch (MalformedURLException ex) {
            Log.e(Tag, "error: " + ex.getMessage(), ex);
        }

        catch (IOException ioe) {
            Log.e(Tag, "error: " + ioe.getMessage(), ioe);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    protected void onPostExecute(String result) {

        if(dialog.isShowing() && dialog!=null)
            dialog.dismiss();

        if (status == 0) {
            Toast.makeText(c, "Not Edited", Toast.LENGTH_SHORT).show();
            //dialog.dismiss();
        } else {
            Toast.makeText(c, "Product Edited", Toast.LENGTH_SHORT).show();
/*
            Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();

            Intent i=new Intent(c,Products.class);
            i.putExtra("cat_id",cat_id);
            i.putExtra("subcat_id",subcat_id);
            i.putExtra("product_value",product_value);
            i.putExtra("sub_value",sub_value);
            i.putExtra("stock_status",status_Stock);
            c.startActivity(i);
            ((Activity)c).finish();*/

        }
    }
}


