package com.vanbinh.chatting.service.firebasecloudmessage;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.vanbinh.chatting.common.sharedpreference.MySharedPreference;

/**
 * Created by vanbinh on 8/11/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseInstanceId";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Device Token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        saveDeviceToken(refreshedToken);
    }

    private void saveDeviceToken(String refreshedToken) {
        new MySharedPreference.BuildShare()
                .init(getApplicationContext(),MySharedPreference.SR_MAIN)
                .putString(MySharedPreference.SR_TOKEN,refreshedToken)
                .save();
    }

    public MyFirebaseInstanceIdService() {
    }


}
