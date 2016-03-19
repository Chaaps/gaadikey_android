package com.gaadikey.gaadikey.gaadikey.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.activities.LaunchActivity_NavDrawer;
import com.gaadikey.gaadikey.gaadikey.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by madratgames on 08/10/14.
 */
public class Fragment_NewsDetail extends Fragment {


    String title;
    String date;
    String body;
    TextView body_tv;
    TextView Heading;
    TextView byline;
    public static ArrayList<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newsdetail, container, false);

        title = ((LaunchActivity_NavDrawer) getActivity()).TITLE;
        date = ((LaunchActivity_NavDrawer) getActivity()).DATE;
        body = ((LaunchActivity_NavDrawer) getActivity()).BODY;

        Heading = (TextView) view.findViewById(R.id.Heading);
        byline = (TextView) view.findViewById(R.id.byline);
        body_tv = (TextView) view.findViewById(R.id.body_tv);

        Heading.setText(title);
        byline.setText("Posted " + goodDate(date));

        body_tv.setText(Html.fromHtml(body, new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {

                LevelListDrawable d = new LevelListDrawable();
                Drawable empty = getResources().getDrawable(R.drawable.ic_launcher);
                d.addLevel(0, 0, empty);
                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                new LoadImage().execute(source, d);
                return d;
            }
        }, null));

        return view;
    }

    public String goodDate(String inputdate) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            // String oldDate = "2011-03-10T11:54:30.207Z";  //This is modifiedOn
            Date date = formatter.parse(inputdate);
            long mills = date.getTime();
            CharSequence cs = DateUtils.getRelativeTimeSpanString(mills);
            return cs.toString(); // returns formatted date
        } catch (Exception e) {
            return null;
        }
    }


    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            //   Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //   Log.d(TAG, "onPostExecute drawable " + mDrawable);
            //  Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                body_tv.invalidate(); // Invalidaes
                CharSequence t = body_tv.getText();
                body_tv.setText(t);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                //  CharSequence t = mTv.getText();
                //  mTv.setText(t);
            }
        }
    }

}
