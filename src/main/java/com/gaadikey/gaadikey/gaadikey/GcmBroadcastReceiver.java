package com.gaadikey.gaadikey.gaadikey;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.gaadikey.gaadikey.gaadikey.activities.LaunchActivity_NavDrawer;
import com.gaadikey.gaadikey.gaadikey.activities.WelcomesYou;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    int NOTIFICATION_ID = 12344;
    NotificationCompat.Builder mBuilder = null;
    Intent theintent;
    Context thecontext;
    ComponentName comp;
    String thetitle;
    String themessage;
    String thenavigationpage;
    PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Explicitly specify that GcmMessageHandler will handle the intent.
        thecontext = context;
        comp = new ComponentName(context.getPackageName(),
                GcmMessageHandler.class.getName());

        theintent = intent;

        //  Intent notificationIntent = new Intent(context, ListMobileActivity.class);
        // WelcomesYou page opens up  when a notification is received! This can be based on the message

        Bundle messages = intent.getExtras();
        thetitle = messages.getString("title");
        themessage = messages.getString("message");
        thenavigationpage = ""; //check for n

        thenavigationpage = messages.getString("navigation_page");
        if (thenavigationpage == null)
            thenavigationpage = "no_navigation";

        Intent notificationIntent; // Variable to declare the next Activity to be called!
        pendingIntent = null; // The Pending Intent

        if (thenavigationpage.equals("welcome")) {

            notificationIntent = new Intent(context, WelcomesYou.class);
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (thenavigationpage.equals("safetylane")) {
            notificationIntent = new Intent(context, LaunchActivity_NavDrawer.class);
            notificationIntent.putExtra("view", "SafetyLane");
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (thenavigationpage.equals("friendslane")) {
            notificationIntent = new Intent(context, LaunchActivity_NavDrawer.class);
            notificationIntent.putExtra("view", "FriendsLane");
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (thenavigationpage.equals("publiclane")) {
            notificationIntent = new Intent(context, LaunchActivity_NavDrawer.class);
            notificationIntent.putExtra("view", "PublicLane");
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (thenavigationpage.equals("shoppinglane")) {
            notificationIntent = new Intent(context, LaunchActivity_NavDrawer.class);
            notificationIntent.putExtra("view", "ShoppingLane");
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (thenavigationpage.equals("search")) {
            notificationIntent = new Intent(context, LaunchActivity_NavDrawer.class);
            notificationIntent.putExtra("view", "Search");
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (thenavigationpage.equals("numberplate")) {
            notificationIntent = new Intent(context, LaunchActivity_NavDrawer.class);
            notificationIntent.putExtra("view", "NumberPlate");
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (thenavigationpage.equals("feedback")) {
            notificationIntent = new Intent(context, LaunchActivity_NavDrawer.class);
            notificationIntent.putExtra("view", "Feedback");
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (thenavigationpage.startsWith("news")) {
            notificationIntent = new Intent(context, LaunchActivity_NavDrawer.class);
            notificationIntent.putExtra("view", "NewsPush");
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            notificationIntent = new Intent(context, LaunchActivity_NavDrawer.class);
            notificationIntent.putExtra("view", "normal"); // when it is normal
            pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        if (thenavigationpage.startsWith("news")) {
            String imageurl = thenavigationpage.substring(5, thenavigationpage.length());

            new BitmapWorkerTask().execute(imageurl);

            // creates notification after downloading the image!

            // Call Async task


            //mBuilder.setLargeIcon(bitmap);
        } else {

            createNotification(thetitle, themessage, pendingIntent, null);
            // .setSmallIcon(R.drawable.my_drawable_resource)

        }


    }


    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void createNotification(String title, String message, PendingIntent pendingIntent, Bitmap bitmap) {
        mBuilder = new NotificationCompat.Builder(thecontext);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        if (bitmap != null) {
            NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
            bigPicStyle.bigPicture(bitmap);
            bigPicStyle.setBigContentTitle(message);
            mBuilder.setStyle(bigPicStyle); // big pic style
        }

        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();
        // default phone settings for notifications
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        // cancel notification after click
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // show scrolling text on status bar when notification arrives
        notification.tickerText = title;
        // notifiy the notification using NotificationManager
        NotificationManager notificationManager = (NotificationManager) thecontext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(thecontext, (theintent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

    }


    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {


        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {

            try {

                return BitmapFactory.decodeStream((InputStream) new URL(params[0])
                        .getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            createNotification(thetitle, themessage, pendingIntent, bitmap);


        }
    }
}
