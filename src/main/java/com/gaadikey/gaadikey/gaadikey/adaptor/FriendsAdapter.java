package com.gaadikey.gaadikey.gaadikey.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gaadikey.gaadikey.gaadikey.R;
import com.squareup.picasso.Picasso;

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
        TextView textView = (TextView) rowView.findViewById(R.id.ContactName);
        TextView textView2 = (TextView) rowView.findViewById(R.id.GaadiName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        Button inviteButton = (Button) rowView.findViewById(R.id.inviteButton);
        inviteButton.setTag(position); // setting the position


        // Button inviteButton = (Button) rowView.findViewById(R.id.inviteButton);

        inviteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    Log.e("Click", "Click");
                    int pos = (Integer) v.getTag();


                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", "I invite you to join GaadiKey! Find out which of your friends own what vehicle! http://gaadikey.com Join NOW!");
                    sendIntent.putExtra("address", contactlist.get(pos).get("phonenumber")); // getting the position!
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    ((Activity) context).startActivity(sendIntent) ;
                   // startActivity(sendIntent);

                } catch (Exception e) {
                    Toast.makeText(((Activity) context).getApplicationContext(),
                            "SMS Sending failed, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });




        if(contactlist.get(position).get("memberstatus").equals("yes"))
        inviteButton.setVisibility(View.GONE); // This hides the invite button from the view !

        //textView.setText(values[position]);
        // Change icon based on name
        String s = "filler";
        textView.setText(contactlist.get(position).get("name"));
        textView2.setText(contactlist.get(position).get("vehiclename"));
        System.out.println(s);
       // new ImageDownloader(imageView).execute(contactlist.get(position).get("gaadipic"));
        if(contactlist.get(position).get("memberstatus").equals("yes"))
        {
            String url = contactlist.get(position).get("gaadipic");
            Picasso.with(context).load(url).into(imageView);
        }

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
