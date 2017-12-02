package com.binh.chatting.common.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by binh on 8/10/2017.
 */

public class SQLiteManager extends SQLiteOpenHelper {
    private Context mContext;
    private static String DB_NAME = "chatting.db";
    private static int DB_NUMBER = 1;
    //table user
    static final String TB_USER = "user";
    static final String USER_ID = "id";
    static final String USER_NAME = "name";
    static final String USER_EMAIL = "email";
    static final String USER_PHONE = "phone";
    static final String USER_AVATAR = "avatar";
    static final String USER_TOKEN = "token";

    //table message
    static final String TB_MESSAGE = "message";
    static final String MESSAGE_ID = "id";
    static final String MESSAGE_KEY = "key";
    static final String MESSAGE_CONTENT = "content";
    static final String MESSAGE_USER = "user_id";
    static final String MESSAGE_TIME = "time";

    //table group
    static final String TB_GROUP = "group";
    static final String GROUP_ID = "id";
    static final String GROUP_NAME = "name";
    static final String GROUP_STATUS = "status";
    private String tb_user = "CREATE TABLE " + TB_USER + "("
            + USER_ID + " integer primary key autoincrement, "
            + USER_NAME + " text not null, "
            + USER_EMAIL + " text not null, "
            + USER_PHONE + " integer, "
            + USER_AVATAR + " text, "
            + USER_TOKEN + " text"
            + ");";
    private String tb_message = "CREATE TABLE " + TB_MESSAGE + "("
            + MESSAGE_ID + " integer primary key autoincrement, "
            + MESSAGE_KEY + " text not null, "
            + MESSAGE_CONTENT + " text, "
            + MESSAGE_USER + " integer, "
            + MESSAGE_TIME + " datetime"
            + ");";
    private String tb_group = "CREATE TABLE " + TB_GROUP + "("
            + GROUP_ID + " integer primary key autoincrement, "
            + GROUP_NAME + " text, "
            + GROUP_STATUS + " boolean"
            + ");";

    protected SQLiteManager(Context context) {
        super(context, DB_NAME, null, DB_NUMBER);
        this.mContext = context.getApplicationContext();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE");
    }

    public void createDatabase() {
        try {
            boolean dbExist = checkDataBase(DB_NAME);
            if (!dbExist) {
                SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
                        getDBPath(mContext) + DB_NAME,
                        null);
                sqLiteDatabase.execSQL(tb_user);
                sqLiteDatabase.execSQL(tb_message);
            }
        } catch (Exception e) {
            Log.d("CREATE TABLE", "Error");
            e.printStackTrace();
        }
    }

    private String getDBPath(Context context) {
        return context.getDatabasePath(DB_NAME).getPath();
    }

    private boolean checkDataBase(String dbName) {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = getDBPath(mContext) + dbName;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database chua ton tai
        }

        if (checkDB != null)
            checkDB.close();

        return checkDB != null;
    }

    public Cursor getAllDataFromTable(String tableName) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.query(tableName, null, null, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getOne(String tableName, String[] columns, String key) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.query(tableName, columns, key, null, null, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public boolean insert(String tableName, ContentValues contentValue) {
        boolean result = false;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            long rs = database.insert(tableName, null, contentValue);
            if (rs > 0)
                result = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean update(String tableName, ContentValues contentValue, int id) {
        boolean result = false;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            long rs = database.update(tableName, contentValue, USER_ID + id, null);
            if (rs > 0)
                result = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean delete(String tableName, int id) {
        boolean result = false;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            long rs = database.delete(tableName, "id=" + id, null);
            if (rs > 0)
                result = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void deleteDataOfTable(String tableName, String columnName, String value) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            if (columnName != null && value != null)
                database.delete(tableName, columnName + "=?", new String[]{String.valueOf(value)});
            else {
                database.delete(tableName, null, null);
                database.delete("sqlite_sequence", "name=?", new String[]{tableName});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
