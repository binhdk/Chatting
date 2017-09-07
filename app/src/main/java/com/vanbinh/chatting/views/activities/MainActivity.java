package com.vanbinh.chatting.views.activities;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.twitter.sdk.android.core.TwitterCore;
import com.vanbinh.chatting.common.singletons.SingleTonUser;
import com.vanbinh.chatting.R;
import com.vanbinh.chatting.views.fragments.MessageFragment;
import com.vanbinh.chatting.views.fragments.UserInfoFragment;
import com.vanbinh.chatting.databinding.ActivityMainBinding;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    DrawerLayout drawer;
    private static final String TAG ="MainActivity";
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        setSupportActionBar(binding.appBarMain.toolbar);
        mAuth=FirebaseAuth.getInstance();
        drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_content,new MessageFragment())
                .commit();
        binding.navView.setNavigationItemSelectedListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_info) {
            showUserInfo();
        }else if(id==R.id.nav_log_out){
            logout();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showUserInfo() {
        Fragment fragment=new UserInfoFragment();
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_content,fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private void logout() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Log.d(TAG, "Sign out google account");
                        }
                    });
        }
        SingleTonUser.deleteInstance();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        MainActivity.this.finish();
    }
}
