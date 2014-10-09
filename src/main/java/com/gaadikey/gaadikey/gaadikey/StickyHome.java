package com.gaadikey.gaadikey.gaadikey;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.adaptor.StickyHomeAdapter;

import java.io.InputStream;
import java.util.ArrayList;


public class StickyHome extends ListActivity {

    ArrayList<String> lanes = new ArrayList<String>();
    String IMAGE_PATH = "";
    String GAADI_MSG = "";
    String GAADI_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_home);
        lanes.add("Public Lane");
        lanes.add("Friends Lane");
        lanes.add("Safety Lane");
        lanes.add("Shopping Lane");
        lanes.add("Trips Lane");
        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);
        TextView  gaadiname_field = (TextView) findViewById(R.id.GaadiName);
        TextView  gaadimsg_field  = (TextView) findViewById(R.id.GaadiMsg);
        // Download the Image dynamically, Pinging the Image URL
        // Get the image path URL by rading the persistant storage

        SharedPreferences sharedPref = getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        IMAGE_PATH = sharedPref.getString(getString(R.string.KEY_GaadiImage), "default");
        GAADI_MSG = sharedPref.getString(getString(R.string.KEY_GaadiMsg), "Set status");
        GAADI_NAME = sharedPref.getString(getString(R.string.KEY_GaadiName), "Your Vehicle Name here");

        gaadimsg_field.setText(GAADI_MSG);
        gaadiname_field.setText(GAADI_NAME);


        Log.e("Retrieved IMAGE_PATH ", IMAGE_PATH);
        Log.e("Retrieved GAADI_MSG ", GAADI_MSG);
        Log.e("Retrieved GAADI NAME ", GAADI_NAME);


        if(!IMAGE_PATH.equals("default")) {
            new ImageDownloader(thumbnail).execute(IMAGE_PATH);

        }

        setListAdapter(new StickyHomeAdapter(StickyHome.this, lanes));
        // This should open the Sticky home


        final EditText edittext = (EditText) findViewById(R.id.editText_search);

        edittext.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                Log.e("Log", "Log");
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    Log.e("Enter captured from textbox", "Enter Captured! ");
                    // Perform action on key press
                   // Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {

        SharedPreferences sharedPref4 = getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_4 = sharedPref4.edit();
        editor_4.putString(getString(R.string.KEY_HomeMenu), "" + (position+1) ); // Incrementing the position by 1
        editor_4.commit();

        Log.e("Click happened", "Click happened ");
        // Toast.makeText(this, "You clicked " + lanes.get(position), Toast.LENGTH_LONG).show();
        // Removing toast UI from the code.. Launching the activity directly from here!
        //  Open the activity which opens up  Public Lane
        // if the clicked item is public lane then open the public lane activity
//        if (lanes.get(position).equals("Public Lane"))
//            startActivity(new Intent(StickyHome.this, PublicLaneActivity.class));
//
//        if (lanes.get(position).equals("Friends Lane"))
//            startActivity(new Intent(StickyHome.this, FriendsLaneActivity.class));
//        // The friends Lane activity,would now contain the contents present in ContactHome
//
//        if (lanes.get(position).equals("Safety Lane"))
//            startActivity(new Intent(StickyHome.this, SafetyLaneActivity.class));
//
//        if (lanes.get(position).equals("Shopping Lane"))
//            startActivity(new Intent(StickyHome.this, ShoppinglaneActivity.class));
//
//        if (lanes.get(position).equals("Trips Lane"))
//            startActivity(new Intent(StickyHome.this, TripsLaneActivity.class));


        // Remember the selected position  and then launch the activity!


        if (lanes.get(position).equals("Public Lane")) {
            startActivity(new Intent(StickyHome.this, LaunchActivity_NavDrawer.class));

        }

        if (lanes.get(position).equals("Friends Lane")) {
            startActivity(new Intent(StickyHome.this,  LaunchActivity_NavDrawer.class));
            // The friends Lane activity,would now contain the contents present in ContactHome
        }

        if(lanes.get(position).equals("Safety Lane")) {
            startActivity(new Intent(StickyHome.this, LaunchActivity_NavDrawer.class));

        }

        if(lanes.get(position).equals("Shopping Lane")) {
            startActivity(new Intent(StickyHome.this, LaunchActivity_NavDrawer.class));

        }

        if(lanes.get(position).equals("Trips Lane")) {
            startActivity(new Intent(StickyHome.this, LaunchActivity_NavDrawer.class));

        }


    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sticky_home, menu);
        return true;
    }






    public void OpenNumberPlateFragment(View view)
    {

        Log.e("Number Plate has been clicked" , "NumberPlate ");
    }

    public void SearchGaadiNo(View view)
    {
        Log.e("Search Gaadi No.. ", "Search");
    }

    public void ImageTap(View view)
    {
        Log.e("Image Tap", "Image Tap has occured ");
        // Show a popup asking the user if they wish to change the gaadi pic

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.e("Dialog selection", "Yes");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.e("Dialog selection", "No");
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to change the default gaadi picture?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
