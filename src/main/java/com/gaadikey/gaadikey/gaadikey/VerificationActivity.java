package com.gaadikey.gaadikey.gaadikey;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
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
import android.widget.EditText;
import android.widget.Toast;

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
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Log.e("Loaded", "activity_verification loaded");

        SharedPreferences sharedPref = getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.KEY_signupstatus), Constants.PIN_NOTDISPATCHED);
        editor.commit();
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
            Toast.makeText(getBaseContext(), "Contact is successfully added", Toast.LENGTH_SHORT).show();
        }catch (RemoteException e) {
            e.printStackTrace();
        }catch (OperationApplicationException e) {
            e.printStackTrace();
        }

}

    public void Phone_Submission_Click(View Button)
    {

        final EditText phoneField = (EditText) findViewById(R.id.phoneNumber);
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

        addContact("Gaadi Key", "9008431992");

        new HttpAsyncPostTask().execute("http://gaadikey.in/generate");
       // Log.e("Response", "Expected Response");
       // Log.e("Actual Response", response);
        //System.out.print("The response is  "+response);
        //When Phone and Email address is clicked
        // Read the text from The textboxes phone and email
    }

    public  String GET(String url){
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
        @Override
        protected String doInBackground(String... urls)
        {

            return postPhoneEmailData(urls[0],ph);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {

            Log.e("Success posting", result);
            new AlertDialog.Builder(VerificationActivity.this)
                    .setTitle("Registration (2/2)")
                    .setMessage("We have recieved your Phone number. Please check your Email Inbox for the PIN and verify it below.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.KEY_signupstatus), Constants.PIN_DISPATCHED);
            editor.commit();

            // The control should now go to Enter PIN Screen

            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.

            startActivity(new Intent(VerificationActivity.this, EnterPINActivity.class));

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
