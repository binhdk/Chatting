package com.vanbinh.chatting.views.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.vanbinh.chatting.R;

/**
 * Created by vanbinh on 8/15/2017.
 *
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    public void showProgressDialog(){
        if(mProgressDialog==null){
            mProgressDialog=new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
