package com.rippleitt.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.rippleitt.activities.ActivityWelcome;
import com.rippleitt.activities.AddPaymentMethod;
import com.rippleitt.controller.RippleittAppInstance;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Ravinder Pal Singh on 11/26/2016.
 */

public class SignUpApiWithImageUpload extends AsyncTask<String, Void, String> {
    SharedPreferences sharedPreferences;
    private URL connectURL;
    private String imagepath = "";
    private String response;
    Context c;
    int status;
    private final ProgressDialog dialog;
    DataOutputStream dos = null;
    String jResponse;
    String email, password,  fname,  lname,  number,  gender, address1, address2;

    public SignUpApiWithImageUpload(Context mContext, String email, String password, String fname, String lname, String number, String gender,String address1,String address2,String imagepath) {

        this.imagepath=imagepath;
        this.c = mContext;
        this.email=email;
        this.password=password;
        this.fname=fname;
        this.lname=lname;
        this.number=number;
        this.gender=gender;
        this.address1=address1;
        this.address2=address2;
        dialog = new ProgressDialog(c);

    }

    @Override
    protected void onPreExecute()
    {
        dialog.setMessage("Uploading.....");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        try {
            connectURL = new URL(RippleittAppInstance.BASE_URL+RippleittAppInstance.SIGNUP);
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
        String existingfilename="";

        Log.e("FILE TYPE & FILE NAME", " "+imagepath);

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

            //---------post_id

            dos.writeBytes("Content-Disposition: form-data; name=\"email\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[0] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"password\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[1] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"fname\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[2] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);


            dos.writeBytes("Content-Disposition: form-data; name=\"lname\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[3] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"number\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[4] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"gender\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[5] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"address1\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[6] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"address2\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + params[7] + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            Log.e("Data", " " + " " + imagepath);
            //--------------image--------------
            if (imagepath.equals(""))
            {
                dos.writeBytes("Content-Disposition: form-data; name=\"profile_pic\";filename=\"" + "" + "\"" + lineEnd);
            }
            else
                {
                File f = new File(imagepath);
                fileInputStream = new FileInputStream(f);
                existingfilename = f.getName();
                dos.writeBytes("Content-Disposition: form-data; name=\"profile_pic\";filename=\"" + existingfilename + "\"" + lineEnd);

                if (existingfilename.endsWith(".jpg")) {
                    dos.writeBytes("Content-type: image/jpg;" + lineEnd);
                }
                if (existingfilename.endsWith(".png")) {
                    dos.writeBytes("Content-type: image/png;" + lineEnd);
                }
                if (existingfilename.endsWith(".gif")) {
                    dos.writeBytes("Content-type: image/gif;" + lineEnd);
                }
                if (existingfilename.endsWith(".jpeg")) {
                    dos.writeBytes("Content-type: image/jpeg;" + lineEnd);
                }

                if (existingfilename.endsWith(".doc")) {
                    dos.writeBytes("Content-type: document/doc;" + lineEnd);
                }
                if (existingfilename.endsWith(".txt")) {
                    dos.writeBytes("Content-type: text/txt;" + lineEnd);
                }
                if (existingfilename.endsWith(".mp4")) {
                    dos.writeBytes("Content-type: video/mp4;" + lineEnd);
                }
                if (existingfilename.endsWith(".avi")) {
                    dos.writeBytes("Content-type: video/avi;" + lineEnd);
                }
                if (existingfilename.endsWith(".ogg")) {
                    dos.writeBytes("Content-type: video/ogg;" + lineEnd);
                }
                if (existingfilename.endsWith(".3gp")) {
                    dos.writeBytes("Content-type: video/3gp;" + lineEnd);
                }
                if (existingfilename.endsWith(".mp3")) {
                    dos.writeBytes("Content-type: audio/mp3;" + lineEnd);
                }

                dos.writeBytes(lineEnd);

                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 1024*1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                Log.e(Tag, "File is written");
                fileInputStream.close();
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
            Log.e("Pic Upload ", jResponse);

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return jResponse;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (dialog.isShowing() && dialog != null)
            dialog.dismiss();

/*        if(size==0)
        {
            Toast.makeText(c, "Size is too long", Toast.LENGTH_SHORT).show();
        }*/

            try {
                JSONObject obj=new JSONObject(result);
                status=Integer.parseInt(obj.getString("response_code"));
               String message=obj.getString("response_message");
                Log.e("Hello",status+","+message);
                if (status == 0) {
                    TastyToast.makeText(c,message, TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    //Toast.makeText(c, "Hello", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                   // dialog.dismiss();
                    Intent intent = new Intent(c, ActivityWelcome.class);
                    c.startActivity(intent);
                    // TastyToast.makeText(getApplicationContext(), "SignUp Successfuly", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    ((Activity)c).finish();
                    TastyToast.makeText(c,message, TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //new Get_Subtask_properties_Api().get_subtask_properties_api(c, Global.Subtask_id);
        }
}

