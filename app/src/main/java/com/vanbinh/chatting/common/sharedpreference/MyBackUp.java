package com.vanbinh.chatting.common.sharedpreference;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

/**
 * Created by binh on 9/7/2017.
 *
 */

public class MyBackUp extends BackupAgentHelper {
    static final String Preferences_Backup_File_Name = "myBackupPreferences";
    static final  String PREFS_BACKUP_KEY ="backup";

    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper =new SharedPreferencesBackupHelper(this, Preferences_Backup_File_Name);
        addHelper(PREFS_BACKUP_KEY, helper);
    }
}
