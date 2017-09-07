package com.vanbinh.chatting.apiconnect.connects;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vanbinh.chatting.common.singletons.SingleTonUser;
import com.vanbinh.chatting.apiconnect.baseconnect.BaseConnectAPI;
import com.vanbinh.chatting.apiconnect.baseconnect.RequestMethod;
import com.vanbinh.chatting.apiconnect.UrlConstants;
import com.vanbinh.chatting.models.User;

/**
 * Created by vanbinh on 8/18/2017.
 *
 */

public class DeviceTokenAPI extends BaseConnectAPI {
    private static final String TAG="DeviceTokenAPI";
    public DeviceTokenAPI(Context context) {
        super(context, UrlConstants.UPDATE_URL,
                null,
                RequestMethod.POST,
                true);
        User user=SingleTonUser.getInstance(context);
        super.data=new Gson().toJson(user);
        Log.d(TAG,"DATA: "+data);
    }

    @Override
    public void onPre() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onPost(JsonObject result) {
        Log.d(TAG,"RESULT: "+new Gson().toJson(result));
    }

    @Override
    public void doInBG() {

    }
}
