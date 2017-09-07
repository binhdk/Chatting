package com.vanbinh.chatting.common.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vanbinh on 8/10/2017.
 */

public class MySharedPreference {
    public static final String SR_MAIN="chat";
    public static final  String SR_USER="user";
    public static final  String SR_LANGUAGE="language";
    public static final  String SR_TOKEN="token";

    public static class BuildShare{
        Context context;
        String name;
        SharedPreferences pre;
        SharedPreferences.Editor editor;
        public BuildShare init(Context context, String name){
            this.context = context;
            this.name = name;
            pre= context.getSharedPreferences (
                    name
                    ,MODE_PRIVATE
            );
            editor = pre.edit();
            return this;
        }
        public BuildShare putString(String key,String value){
            editor.putString(key,value);
            return  this;
        }
        public BuildShare putInt(String key,int value){
            editor.putInt(key,value);
            return  this;
        }
        public BuildShare putBoolean(String key, boolean value){
            editor.putBoolean(key,value);
            return this;
        }

        public BuildShare putFloat(String key, float value){
            editor.putFloat(key,value);
            return this;
        }

        public BuildShare putLong(String key, long value){
            editor.putLong(key,value);
            return this;
        }
        public BuildShare save(){
            editor.commit();
            return this;
        }
        public SharedPreferences get(){
            return pre;
        }
    }
}
