package com.binh.chatting.common.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.binh.chatting.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 8/16/2017.
 */

public class MessageSQLiteManager {
    private static final String TAG = "MessageSQLite";
    private SQLiteManager manager;

    public List<Message> getAllMessage() {
        List<Message> list = new ArrayList<>();
        Message message = new Message();
        Cursor cursor = manager.getAllDataFromTable(SQLiteManager.TB_MESSAGE);
        try {
            if (cursor.moveToFirst()) {
                do {
                    message.setId(Integer.parseInt(cursor.getString(1)));
                    message.setMessage(cursor.getString(2));
                    message.setUsername(cursor.getString(3));
                    message.setTime((cursor.getLong(4)));
                    list.add(message);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, " - Không thể getMessage");
        }
        return list;
    }

    public Message getMessage(String id) {
        Message message = new Message();
        Cursor cursor = manager.getOne(SQLiteManager.TB_MESSAGE, new String[]{SQLiteManager.USER_TOKEN}, id);
        if (cursor.moveToFirst()) {
            message.setId(Integer.parseInt(cursor.getString(1)));
            message.setMessage(cursor.getString(2));
            message.setUsername(cursor.getString(3));
            message.setTime((cursor.getLong(4)));
        }
        return message;
    }

    public boolean addMessage(Message message) {
        ContentValues value = new ContentValues();
        value.put("message", message.getMessage());
        value.put("username", message.getUsername());
        value.put("time", message.getTime());
        return manager.insert(SQLiteManager.TB_MESSAGE, value);
    }

    public boolean update(Message message) {
        ContentValues value = new ContentValues();
        value.put("message", message.getMessage());
        value.put("username", message.getUsername());
        value.put("time", message.getTime());
        return manager.update(SQLiteManager.TB_MESSAGE, value, message.getId());
    }

    public boolean deleteMessage(Message message) {
        return manager.delete(SQLiteManager.TB_MESSAGE, message.getId());
    }
}
