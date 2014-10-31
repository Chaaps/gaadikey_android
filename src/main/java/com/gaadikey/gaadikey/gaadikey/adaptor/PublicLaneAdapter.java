package com.gaadikey.gaadikey.gaadikey.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by madratgames on 24/09/14.
 */
public class PublicLaneAdapter extends ArrayAdapter<HashMap<String, String>> {

    private final Context context;
    private final ArrayList<HashMap<String, String>> publicList;

    public PublicLaneAdapter(Context context, ArrayList<HashMap<String, String>> val) {
        super(context, R.layout.list_publiclane, val);
        this.context = context;
        this.publicList =  val;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_publiclane, parent, false);
        TextView GaadiNametextView = (TextView) rowView.findViewById(R.id.GaadiName);
        TextView TimestamptextView = (TextView) rowView.findViewById(R.id.timestamp);
      //  ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        //textView.setText(values[position]);
        // Change icon based on name
        String s = "filler";
        GaadiNametextView.setText(publicList.get(position).get("vehiclename"));
        TimestamptextView.setText(publicList.get(position).get("modifiedOn"));
        System.out.println(s);
        Log.e("Called", "Called");
       // new ImageDownloader(imageView).execute(publicList.get(position).get("gaadipic"));

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        if (imageView == null) {
            imageView = new ImageView(context);
        }
        //new ImageDownloader(imageView).execute(publicList.get(position).get("gaadipic"));
        String url = publicList.get(position).get("gaadipic");

        Picasso.with(context).load(url).into(imageView);
        // Enabled Picasso once again!


      //  Drawable drw =LoadImageFromWebOperations(publicList.get(position).get("gaadipic"));
        //imageView.setBackgroundDrawable(drw);
       // imageView.setImageDrawable(drw);

        return rowView;

        //return  image;
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



    private Drawable LoadImageFromWebOperations(String strPhotoUrl) {
        try {
            InputStream is = (InputStream) new URL(strPhotoUrl).getContent();
            Drawable d = Drawable.createFromStream(is, strPhotoUrl);
            return d;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }
    }




}
