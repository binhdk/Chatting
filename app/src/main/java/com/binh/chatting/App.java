package com.binh.chatting;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/**
 * Created by binh on 9/4/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.TWITTER_CONSUMER_KEY), getString(R.string.TWITTER_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(twitterConfig);
    }
}
