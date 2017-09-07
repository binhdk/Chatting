package com.vanbinh.chatting.service.firebasecloudmessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vanbinh.chatting.R;
import com.vanbinh.chatting.views.activities.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static  final  String TAG="PUSH_NOTIFICATION";
    private  static final String NOTIFICATION_TAG="New Message";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG,"From: "+remoteMessage.getFrom());
        String message="";
        try{
             message=remoteMessage.getNotification().getBody();
        }catch (Exception e){
            Log.d(TAG,"Not a message!");
        }
        notify(getApplicationContext(),message,1);
    }

//    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public void notify(final Context context, final String message, final int number) {
        Resources res = context.getResources();
        Bitmap icon = BitmapFactory.decodeResource(res, R.drawable.ic_message_black_24dp);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle(NOTIFICATION_TAG)
                .setContentText(message)
                .setNumber(number)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context,MainActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true);
        NotificationManager manager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_TAG,0,builder.build());
    }
}
