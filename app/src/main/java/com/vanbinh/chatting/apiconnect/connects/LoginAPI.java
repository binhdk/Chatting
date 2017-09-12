package com.vanbinh.chatting.apiconnect.connects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vanbinh.chatting.common.sharedpreference.MySharedPreference;
import com.vanbinh.chatting.common.singletons.SingleTonUser;
import com.vanbinh.chatting.common.sqlite.UserSQLiteManager;
import com.vanbinh.chatting.apiconnect.baseconnect.BaseConnectAPI;
import com.vanbinh.chatting.apiconnect.baseconnect.RequestMethod;
import com.vanbinh.chatting.apiconnect.UrlConstants;
import com.vanbinh.chatting.models.User;
import com.vanbinh.chatting.R;
import com.vanbinh.chatting.activities.MainActivity;
/**
 * Created by vanbinh on 8/16/2017.
 *
 */

public class LoginAPI extends BaseConnectAPI {
    private static final String TAG="LoginAPI";
    public LoginAPI(Context context, String data) {
        super(context, UrlConstants.LOGIN_URL, data,RequestMethod.POST,false);
    }

    @Override
    public void onPre() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onPost(JsonObject result) {
        Log.d(TAG,"LOGGED USER: "+result.toString());
        try{
            User user=new Gson().fromJson(
                    result.get("user").getAsJsonObject(),
                    User.class);
            if(user!=null){
                String token= new MySharedPreference.BuildShare()
                        .init(mContext, MySharedPreference.SR_MAIN)
                        .get()
                        .getString(MySharedPreference.SR_TOKEN, null);
                user.setToken(token);
                UserSQLiteManager manager=UserSQLiteManager.getInstance(mContext);
                manager.addUser(user);
                SingleTonUser.setInstance(mContext);
                new DeviceTokenAPI(mContext).execute();
                Intent i = new Intent(mContext, MainActivity.class);
                mContext.startActivity(i);
                ((Activity)mContext).finish();
            }else{
                Toast.makeText(mContext,
                        mContext.getString(R.string.error_login),
                        Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(mContext,
                    mContext.getString(R.string.error_login),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void doInBG() {

    }
}
