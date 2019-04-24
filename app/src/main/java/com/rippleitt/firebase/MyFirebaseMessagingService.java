package com.rippleitt.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.PojoFCMPush;
import com.rippleitt.modals.PojoFCMPushDataResp;
import com.rippleitt.modals.PojoFCMPushNotification;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc on mail/10/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

//  }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("resp"));
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));

        PojoFCMPushDataResp pojo = new Gson().fromJson(remoteMessage.getData().get("resp"), PojoFCMPushDataResp.class);
        if (pojo!=null)
        sendNotification(pojo);

        String message="Rippleitt";
        String title="Rippleitt";
        if (remoteMessage.getData().get("resp")!=null){
            try {
                JSONObject object= new JSONObject(remoteMessage.getData().get("resp"));
                message= object.getString("message");
                title= object.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message);
        NotificationManager manager = (NotificationManager)     getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }

    private void sendNotification(PojoFCMPushDataResp pojo) {

//        Intent intent = null;
//        Log.d("message", pojo.getMessage());
        Intent intent= null;
        if (pojo.getType().equals("chat") && RippleittAppInstance.currentActivity==null){

            intent = new Intent(RippleittAppInstance.broadcastMessage);
            intent.putExtra("isMessage",true);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//            intent = new Intent(this, .class);
//            intent.putExtra("",String.valueOf(pojo.getUserId()));
//            intent.putExtra(WebConstants.PARAM_USERNAME, pojo.getUsername());
//            intent.putExtra(WebConstants.PARAM_IMAGE, pojo.getUserImage());
//            intent.putExtra(WebConstants.PARAM_DEVICE_TOKEN, pojo.getDeviceToken());
//            intent.putExtra(WebConstants.PARAM_DEVICE_TYPE, pojo.getDevice());
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            notificationBuilder.setContentText(pojo.getMessage());
//            notificationBuilder.setContentTitle(pojo.getUsername()+" sent you a message ");
        }

//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//
//        int icon = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        notificationBuilder.setSmallIcon(icon);
//        notificationBuilder.setAutoCancel(true);
//        notificationBuilder.setSound(defaultSoundUri);
//        notificationBuilder.setContentIntent(pendingIntent);
//        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());

    }


}
