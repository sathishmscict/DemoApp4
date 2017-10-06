package com.therisingtechie.geello;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Satish Gadde on 02-09-2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {



        private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

      //  private NotificationUtils notificationUtils;
    private Intent resultIntent;

    @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            Log.e(TAG, "From: " + remoteMessage.getFrom());



        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }


            if (remoteMessage == null)
                return;




        }


}