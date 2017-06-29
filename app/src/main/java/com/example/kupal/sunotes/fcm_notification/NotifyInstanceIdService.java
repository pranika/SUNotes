package com.example.kupal.sunotes.fcm_notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kupal.sunotes.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by nikhiljain on 4/3/17.
 */

public class NotifyInstanceIdService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d("myfirebaseid", "Refreshed token: " + recent_token);
        SharedPreferences sharedPreferences=getApplicationContext().
                getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(getString(R.string.NOTIFY_TOKEN),recent_token);
        editor.commit();
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        // TODO: Implement this method to send any registration to your app's servers.
       // sendRegistrationToServer(refreshedToken);
    }
}
