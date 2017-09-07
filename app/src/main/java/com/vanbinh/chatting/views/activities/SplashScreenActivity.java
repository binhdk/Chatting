package com.vanbinh.chatting.views.activities;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Transaction;
import com.vanbinh.chatting.R;
import com.vanbinh.chatting.common.singletons.SingleTonUser;
import com.vanbinh.chatting.common.sqlite.UserSQLiteManager;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG ="SplashScreenActivity" ;
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    UserSQLiteManager manager;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
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
            if ( mAuth.getCurrentUser()==null) {
                Log.d(TAG, "Current user token:  "+SingleTonUser.getInstance(this).getToken());
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                Log.d(TAG,"Not yet login!");
                startActivity(intent);
                finish();
            } else {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                Log.d(TAG,"Logged user!");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
