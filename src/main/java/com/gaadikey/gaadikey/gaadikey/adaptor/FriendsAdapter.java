package com.gaadikey.gaadikey.gaadikey.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by madratgames on 22/09/14.
 */


public class FriendsAdapter extends ArrayAdapter <HashMap<String, String>>  {

    private final Context context;
    private final ArrayList<HashMap<String, String>> contactlist;

    public FriendsAdapter(Context context, ArrayList<HashMap<String, String>> val) {
        super(context, R.layout.list_friends, val);
        this.context = context;
        this.contactlist = val;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_friends, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        //textView.setText(values[position]);
        // Change icon based on name
        String s = "filler";
        textView.setText(contactlist.get(position).get("Name"));
        System.out.println(s);
        new ImageDownloader(imageView).execute(contactlist.get(position).get("ImgUrl"));
        return rowView;
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
