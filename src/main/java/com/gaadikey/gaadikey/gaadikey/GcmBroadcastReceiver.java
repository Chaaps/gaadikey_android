package com.gaadikey.gaadikey.gaadikey;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    int NOTIFICATION_ID=12344;
    @Override
    public void onReceive(Context context, Intent intent) {
       
    	// Explicitly specify that GcmMessageHandler will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
        		GcmMessageHandler.class.getName());

        Intent notificationIntent = new Intent(context, ListMobileActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bundle extras = intent.getExtras();
        Log.e("Special", "The Special Message is " + extras.getString("title"));


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                // .setSmallIcon(R.drawable.my_drawable_resource)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(extras.getString("title"))        // GaadiKey
                .setContentText(extras.getString("message"))       // Your friend ABCD gotta know that you have a bike
                .setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();
        // default phone settings for notifications
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;

        // cancel notification after click
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // show scrolling text on status bar when notification arrives
        notification.tickerText = extras.getString("title");

        // notifiy the notification using NotificationManager
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);


        
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
