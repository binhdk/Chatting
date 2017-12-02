package com.binh.chatting.customview;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.binh.chatting.R;

/**
 * Created by binh on 9/20/2017.
 */

public class AppwidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int i =0 ;i<appWidgetIds.length;i++){
            int currentWidgetId =appWidgetIds[i];
            String url = "http://google.com";
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(url));
            PendingIntent pendingIntent =PendingIntent.getActivity(context,0,intent,0);
            RemoteViews views  =new RemoteViews(context.getPackageName(), R.layout.layout_widget);
            views.setOnClickPendingIntent(R.id.btn,pendingIntent);
            appWidgetManager.updateAppWidget(currentWidgetId,views);
            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();

        }
    }
}
