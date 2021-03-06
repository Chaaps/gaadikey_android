package com.gaadikey.gaadikey.gaadikey;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class VerificationActivity extends ActionBarActivity {

    PhoneObject ph;
    String phone;
    String email;
    String PIN;
    Tracker t;
    ProgressBar pb; // Progressbar

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        pb = (ProgressBar) findViewById(R.id.progress); // The progress bar has been identified.

        Log.e("Loaded", "activity_verification loaded");
//        SharedPreferences sharedPref = getSharedPreferences("android_shared", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(getString(R.string.KEY_signupstatus), Constants.VERIFY_VISITED);
//        editor.commit();
        //Get a Tracker (should auto-report)
        t = ((GaadiKey) getApplication()).getTracker(GaadiKey.TrackerName.APP_TRACKER);
        t.setScreenName("VerificationActivity"); // =
        t.send(new HitBuilders.AppViewBuilder().build());

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(KEY_CONTENT, mContent);
    }

    @Override
    protected void onStart() {
        super.onStart();
      //  GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    protected void  onStop(){
        super.onStop();
        //Stop the analytics tracking
       // GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    private void addContact(String name, String phone)
    {

        ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();

        int rawContactID = ops.size();

        // Adding insert operation to operations list
        // to insert a new raw contact in the table ContactsContract.RawContacts
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null)
                .build());

        // Adding insert operation to operations list
        // to insert display name in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, name)
                .build());

        // Adding insert operation to operations list
        // to insert Mobile Number in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, phone)
                .withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
                .build());


        try{
            // Executing all the insert operations as a single database transaction
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Log.e("Contact save status", "success");
            SharedPreferences sharedPref = getSharedPreferences("android_shared", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.KEY_GaadiKey_Number_Saved), "yes");
            editor.commit();
        }catch (RemoteException e) {
            e.printStackTrace();
        }catch (OperationApplicationException e) {
            e.printStackTrace();
        }

}



    public void Phone_Submission_Click(View Button)
    {

        // Get tracker.
     //   Tracker t = ((GaadiKey) getApplication()).getTracker(GaadiKey.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("PhoneNumberSubmitted")
                .setAction("PhoneNumber_SubmitClick")
                .setLabel("")
                .build());

        Log.e("The Submission Click has occured once and should only occur once!!!", "Click event");
        final EditText phoneField = (EditText) findViewById(R.id.phoneNumber);

        final android.widget.Button submissionClick = (Button) findViewById(R.id.button);
        submissionClick.setEnabled(false);
        submissionClick.setFocusable(false); // removes the focus from the button!
        // This should disbale the button temporarily, Prevents the user from clicking the button more than once!

        phone = phoneField.getText().toString();
        /*
        new AlertDialog.Builder(this)
                .setTitle("Entries")
                .setMessage("Your entered phone number is "+phone+" and email address is "+email)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

                */

        ph = new PhoneObject();
        ph.set_deviceid("randomnumber");
        ph.set_email("test@test.com");
        ph.set_phonenumber(phone);
        SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.KEY_phonenumber), phone);
        editor.commit();

        SharedPreferences sharedPref1 =  getSharedPreferences("android_shared" , MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPref.edit();
        editor1.putString(getString(R.string.KEY_signupstatus), Constants.PIN_REQUESTSENT);
        editor1.commit();

        //PIN_REQUESTSENT
        //sharedPref1 now  storing the PIN_REQUESTSENT as early as possible


//        String contactsavestatus = sharedPref.getString(getString(R.string.KEY_GaadiKey_Number_Saved),  "no");
//        if(contactsavestatus.equals("no"))


        String nameSaved = getContactDisplayNameByNumber("9008431992");
        Log.e("The Given number is saved in the name of ", nameSaved);

        if(getContactDisplayNameByNumber("9008431992").equals("?"))
        addContact("Gaadi Key", "9008431992"); // Execute this statement only once.. That is save the gaadi phone number only when it is not saved in the phone book


      //  new HttpAsyncPostTask().execute("http s://maps.googleapis.com/maps/api/place/nearbysearch/json");
        new HttpAsyncPostTask().execute("https://gaadikey.in/generate");
       // Log.e("Response", "Expected Response");
       // Log.e("Actual Response", response);
        //System.out.print("The response is  "+response);
        //When Phone and Email address is clicked
        // Read the text from The textboxes phone and email
    }



    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = "?";

        ContentResolver contentResolver = getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }

    public void getThisThingDone()
    {
        Log.e("Do this ",  "Do this .. Do this now");
    }


    // passing https has a problem in android.. solve it
    public  String GET(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
                JSONObject jObject = new JSONObject(result);
                String actualPIN = jObject.getString("PIN");

                if(PIN.equals(actualPIN) )
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Verification Status")
                            .setMessage("Verification Success! Thanks for verifying your PIN. You can now build Gaadi Key profile")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }

                else
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Verification Status")
                            .setMessage("The PIN which you entered is not matching! Please enter a valid PIN.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            }

            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }



    public String postPhoneEmailData(String url,PhoneObject phone) {
        // Create a new HttpClient and Post Header
        InputStream inputStream = null;
        String result = "";
        try
        {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("deviceid", phone.get_deviceid());
            jsonObject.accumulate("phonenumber", phone.get_phonenumber());
            jsonObject.accumulate("email", phone.get_email());
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.e("Json uploaded", "The Uploaded json looks like "+json);
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content
            //httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Accept-version", getString(R.string.API_VERSION));
            // Specifying the API version

            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
                Log.e("crash" , "We are here and somehow crashing!");
                Log.e("Result in string ", result);

            }
            else
                result = "Did not work!";

            } catch (Exception e) {

                 Log.e("Exception block", e.getLocalizedMessage());
                // TODO Auto-generated catch block
            }

        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.verification, menu);
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


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncPostTask extends AsyncTask<String, Void, String> {


        protected void onPreExecute()
        {
            // Called before calling anything else
            pb.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... urls)
        {
            return postPhoneEmailData(urls[0],ph);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            pb.setVisibility(View.INVISIBLE); //Disables the progressbar

            Log.e("Success posting", result);
            Toast.makeText(getBaseContext(), "You will be receiving PIN by SMS", Toast.LENGTH_LONG).show();

            // The control should now go to Enter PIN Screen
            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.
            startActivity(new Intent(VerificationActivity.this, EnterPINActivity.class));
            finish();
        }
    }


    private class HttpAsyncGetTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return  GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {

            try {
                JSONObject jObject = new JSONObject(result);
                String actualPIN = jObject.getString("PIN");
                Log.e("enteredPIN", PIN);
                Log.e("actualPIN", actualPIN);

                if (PIN.equals(actualPIN)) {
                    new AlertDialog.Builder(VerificationActivity.this)
                            .setTitle("Verification Status")
                            .setMessage("Verification Success! Thanks for verifying your PIN. You can now build Gaadi Key profile")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                    // Storing this state .. that it is verified!
                    SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.KEY_signupstatus), Constants.PIN_VERIFIED);

                    startActivity(new Intent(VerificationActivity.this, MyActivity.class));

                } else {
                    new AlertDialog.Builder(VerificationActivity.this)
                            .setTitle("Verification Status")
                            .setMessage("The PIN which you entered is not matching! Please enter a valid PIN.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }

            catch (Exception e)
            {

            }

        }
    }






}
