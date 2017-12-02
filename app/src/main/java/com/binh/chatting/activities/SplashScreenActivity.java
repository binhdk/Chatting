package com.binh.chatting.activities;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.binh.chatting.R;
import com.binh.chatting.common.singletons.SingleTonUser;
import com.binh.chatting.common.sqlite.UserSQLiteManager;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreenActivity";
    UserSQLiteManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        FirebaseApp.initializeApp(this);
        int SPLASH_DISPLAY_LENGTH = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLogin();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void checkLogin() {
        manager = UserSQLiteManager.getInstance(this);
        try {
            manager.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Log.d(TAG, "Current user token:  " + SingleTonUser.getInstance(this).getToken());
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                Log.d(TAG, "Not yet login!");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                Log.d(TAG, "Logged user!");
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
