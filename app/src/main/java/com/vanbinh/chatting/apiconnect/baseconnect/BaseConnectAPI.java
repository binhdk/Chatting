package com.vanbinh.chatting.apiconnect.baseconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vanbinh.chatting.common.sqlite.UserSQLiteManager;
import com.vanbinh.chatting.R;
import com.vanbinh.chatting.views.activities.LoginActivity;

/**
 * Created by vanbinh on 8/16/2017.
 *
 */

public abstract class BaseConnectAPI extends AsyncTask<Void, Void, String> {
    private static final String TAG = "BaseConnectAPI";
    protected Context mContext;
    protected String url;
    protected String data;
    private ProgressDialog pgDialog;
    private RequestMethod method;
    private JsonResponseFromUrl jsonResponseFromUrl;

    public BaseConnectAPI(Context context, String url, String data, RequestMethod method, boolean refresh) {
        this.mContext = context;
        this.url = url;
        this.data = data;
        this.method = method;
        this.refresh = refresh;
    }

    protected boolean refresh = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onPre();
        if (!refresh)
            initDialog();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        onUpdate();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String response = null;
        try {
            jsonResponseFromUrl = new JsonResponseFromUrl(url, data);
            switch (method) {
                case GET:
                    response = jsonResponseFromUrl.getResponseGet();
                    break;
                case POST:
                    response = jsonResponseFromUrl.getResponsePost();
                    break;
                case DELETE:
                    response=jsonResponseFromUrl.getResponseDelete();
                    break;
                case  PUT:
                    break;
            }
            doInBG();
        }catch (Exception e){
            response=null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG,"RESPONSE FROM API: "+s);
        try{
            if(jsonResponseFromUrl.getResponseCode()==402){
                UserSQLiteManager manager=UserSQLiteManager.getInstance(mContext);
                manager.deleteUser();
                Toast.makeText(mContext,
                        mContext.getString(R.string.token_expired),
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(i);

                ((Activity) mContext).finish();
            }else if(s!=null){
                JsonObject jsonObject=new JsonParser().parse(s).getAsJsonObject();
                onPost(jsonObject);
            }else{
                onError();
            }
        }catch (Exception e){
            onError();
            e.printStackTrace();
        }
        dismissDialog();
    }

    public abstract void onPre();

    public abstract void onUpdate();

    public abstract void onPost(JsonObject result);

    public abstract void doInBG();

    private void onError() {
        try {
            Log.d(TAG, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(mContext,
                mContext.getString(R.string.cannot_connect_server), Toast.LENGTH_LONG).show();
    }

    private void initDialog() {
        pgDialog = new ProgressDialog(mContext) {
            @Override
            public void onBackPressed() {
                pgDialog.dismiss();
            }
        };
        pgDialog.show();
        pgDialog.setIndeterminate(false);
        pgDialog.setCancelable(false);
        pgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pgDialog.setContentView(R.layout.layout_progressdialog);
    }

    public void dismissDialog() {
        if (pgDialog != null && pgDialog.isShowing())
            pgDialog.dismiss();
    }

    public void initDialogWithTitle(String message) {
        refresh = true;
        pgDialog = new ProgressDialog(mContext);
        pgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pgDialog.setMessage(message);
        pgDialog.setIndeterminate(false);
        pgDialog.setCanceledOnTouchOutside(false);
        pgDialog.show();
    }

    public void initDialogWithTitle(String message, boolean cancel) {
        refresh = true;
        pgDialog = new ProgressDialog(mContext);
        pgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pgDialog.setMessage(message);
        pgDialog.setIndeterminate(false);
        pgDialog.setCancelable(cancel);
        pgDialog.setCanceledOnTouchOutside(false);
        pgDialog.show();
    }
}
