package com.gaadikey.gaadikey.gaadikey;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class MyActivity extends Activity {

    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "945667950391";
    private Spinner spinner;
    private static final String[] paths = {"item 1", "item 2", "item 3"};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        TextView contactView = (TextView) findViewById(R.id.con);

        Cursor cursor = getContacts();

        while (cursor.moveToNext()) {
            String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            //  String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.));
            contactView.append("Name: ");
            contactView.append(displayName);
            contactView.append("\n");
            //  contactView.append("Number: ");
            //  contactView.append(contactNumber);
            //  contactView.append("\n");
        }

        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"Honda Unicorn", "TVS Apache", "Bajaj Pulsar", "Yamaha RX", "Royal Enfield"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        Spinner cardropdown = (Spinner) findViewById(R.id.carspinner);
        String[] car_items = new String[]{"Honda City", "Hyundai Santro", "Maruti 800", "Maruti 1000"};
        ArrayAdapter<String> car_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, car_items);
        cardropdown.setAdapter(car_adapter);


    }

    private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + ("1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }


    public void CompleteProfile_Click(View Button)
    {
        Log.e("Complete_Profile_Button_Clicked ", "Complete Profile Clicked.");
        getRegId();

        // CompleteProfile
    }

    // verify  API has to be called.
    // Generate Registartion ID for this device

    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM",  msg);
                    startActivity(new Intent(MyActivity.this, ListMobileActivity.class));

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

                Log.e("Registration ID", "This device's unique registration ID is "+msg );
                //etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }


}


