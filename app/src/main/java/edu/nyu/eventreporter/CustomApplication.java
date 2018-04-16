package edu.nyu.eventreporter;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by HaoYu on 2018/4/9.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("This is token", token);
        FirebaseMessaging.getInstance().subscribeToTopic("android");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
