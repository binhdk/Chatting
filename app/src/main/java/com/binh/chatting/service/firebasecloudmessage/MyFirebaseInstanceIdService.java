package com.binh.chatting.service.firebasecloudmessage;

import com.binh.chatting.common.sharedpreference.MySharedPreference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by binh on 8/11/2017.
 * get Device token Id
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        saveDeviceToken(refreshedToken);
    }

    private void saveDeviceToken(String refreshedToken) {
        new MySharedPreference.BuildShare()
                .init(getApplicationContext(), MySharedPreference.SR_MAIN)
                .putString(MySharedPreference.SR_TOKEN, refreshedToken)
                .save();
    }

    public MyFirebaseInstanceIdService() {
    }


}
