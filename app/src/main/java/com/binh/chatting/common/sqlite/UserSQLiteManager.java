package com.binh.chatting.common.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.binh.chatting.model.User;

/**
 * Created by binh on 8/16/2017.
 *
 */

public class UserSQLiteManager extends SQLiteManager {
    private static final String TAG = "UserSQLiteManager";
    private static UserSQLiteManager instance = null;

    private UserSQLiteManager(Context context) {
        super(context);
    }

    public static synchronized UserSQLiteManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserSQLiteManager(context.getApplicationContext());
        }
        return instance;
    }

    public User getUser() {
        User user = new User();
        Cursor cursor = this.getAllDataFromTable(SQLiteManager.TB_USER);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    user.setId(Integer.parseInt(cursor.getString(0)));
                    user.setName(cursor.getString(1));
                    user.setEmail(cursor.getString(2));
                    user.setTel(Integer.parseInt(cursor.getString(3)));
                    user.setAvatar(cursor.getString(4));
                    user.setToken(cursor.getString(5));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            Log.d(TAG, "GET USER - Không thể getUser");
        }
        return user;
    }

    public User getUser(String token) {
        User user = new User();
        Cursor cursor = this.getOne(SQLiteManager.TB_USER, null, SQLiteManager.USER_TOKEN + "='" + token + "'");
        if (cursor != null && cursor.moveToFirst()) {
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setTel(Integer.parseInt(cursor.getString(3)));
            user.setAvatar(cursor.getString(4));
            user.setToken(cursor.getString(5));
        }
        return user;
    }

    public boolean addUser(User user) {
        ContentValues value = new ContentValues();
        value.put("id", user.getId());
        value.put("name", user.getName());
        value.put("email", user.getEmail());
        value.put("phone", user.getTel());
        value.put("avatar", user.getAvatar());
        value.put("token", user.getToken());
        boolean result = false;
        try {
            deleteUser();
            result = this.insert(SQLiteManager.TB_USER, value);
        } catch (SecurityException e) {
            Log.d(TAG, "TB_USER - Không thể insert 1");
        }
        return result;
    }

    public boolean updateUser(User user) {
        ContentValues value = new ContentValues();
        value.put("name", user.getName());
        value.put("email", user.getEmail());
        value.put("phone", user.getTel());
        value.put("avatar", user.getAvatar());
        value.put("token", user.getToken());
        return this.update(SQLiteManager.TB_USER, value, user.getId());
    }

    public boolean deleteUser(User user) {
        return this.delete(SQLiteManager.TB_USER, user.getId());
    }

    public void deleteUser() {
        this.deleteDataOfTable(SQLiteManager.TB_USER, null, null);
    }
}
