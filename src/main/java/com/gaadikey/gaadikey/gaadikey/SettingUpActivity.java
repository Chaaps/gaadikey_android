package com.gaadikey.gaadikey.gaadikey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Create the JSON such that  it should be something like
//
//            {
//                phonenumber: 9739888428;
//                book:
//                    [
//                        {
//                           "Name" : name,
//                           "Desc" : desc,
//                           "ImgUrl" : imgurl,
//                            "gkey" : gkey,
//                            "phonenumber1": phonenumber1,
//                            "phonenumber2" : phonenumber2,
//                            "phonenumber3" : phonenumber3,
//                            "phonenumber4" : phonenumber4
//
//
//                        },
//
//                        {
//
//                        }
//
//                        // this should repeat .....
//
//
//
//
//                    ]
//            }

public class SettingUpActivity extends ActionBarActivity {

    ContactsObject cob = new ContactsObject();
    String jsonString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_up);
        Log.e("Flow", "The flow is in SettingUpActivity");
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        int count = 0;
        try
        {

        JSONArray jsonArray = new JSONArray();
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            Log.e("Phone number before is ", phoneNumber);
            phoneNumber.replace(" ", "");
            phoneNumber = phoneNumber.replaceAll("\\s+","");
            phoneNumber = phoneNumber.trim();
            Log.e("Phone number after is ", phoneNumber );

            Log.e("Name is ", name);
            Log.e("Number is ", phoneNumber);
            cob.setName(name);
            cob.setDesc("public.gaaikey.com");
            cob.setImgUrl("http://gaadikey.com/images/gaadi/3.jpg");
            cob.setGkey("not_assigned");
            cob.setPhonenumber1(phoneNumber);
            cob.setPhonenumber2("not_assigned");
            cob.setPhonenumber3("not_assigned");
            cob.setPhonenumber4("not_assigned");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Name", name);
                jsonObject.put("Desc", "public.gaadikey.com");
                jsonObject.put("ImgUrl", "http://gaadikey.com/images/gaadi/4.jpg");
                jsonObject.put("gkey", "not_assigned");
                jsonObject.put("phonenumber1", phoneNumber);
                jsonObject.put("phonenumber2", "not_assigned");
                jsonObject.put("phonenumber3", "not_assigned");
                jsonObject.put("phonenumber4", "not_assigned");
                jsonArray.put(jsonObject);
                count++;
        }

        Log.e("Number of contacts in this phone", ""+count);
        JSONObject newjsonObject = new JSONObject();
        SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
        String ph = sharedPref.getString(getString(R.string.KEY_phonenumber), "the default stuff");
        // retrieving the phone number!
        newjsonObject.put("phonenumber", ph);
        newjsonObject.put("book", jsonArray);
        jsonString = newjsonObject.toString();
            // The URL // SUBMITcONTACTS HAVE TO BE CALLED ////
            // tHE RESULT HAVE TO BE POPULATED AND SUBMITTED TO  CONTACTS VIEW //////////
        }
        catch (Exception e)
        {

        }

        Log.e("Uploaded json is" , jsonString);
        new PostDataTask().execute("https://gaadikey.in/submitcontacts", jsonString);
        phones.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_up, menu);
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

    private class PostDataTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls)
        {

            return PostData(urls[0], jsonString);

        }
        protected void onPostExecute(String result)
        {

            Log.e("Success posting", result);
            Log.e("Flow change should happen" , "The flow should now shift to different view that is ListMobileActivity");
          //  startActivity(new Intent(SettingUpActivity.this, ListMobileActivity.class));
            // This intent should take to StickyHome
          //  startActivity(new Intent(SettingUpActivity.this, LaunchActivity_NavDrawer.class));
            // Remove all the older activities from the stack .. so that when the user presses back, it doesn't take to older screens which are no longer required!
            Intent intent = new Intent(SettingUpActivity.this, LaunchActivity_NavDrawer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


         //   startActivity(new Intent(SettingUpActivity.this, LaunchActivity_NavDrawer.class));
            // The flow should now shift to different view that is ListMobileActivity //

            try
            {

                // if result is  { } ... The submission of contacts were successful
                // the db should be populated by the new contacts.////
                // in case of 9739888428... it would be inserted in the mongodb as the additional record.

            }
            catch(Exception e)
            {
                Log.e("Parse", "Exception in parsing");

            }

            Log.e("This should contain the access token ", result);
            Toast.makeText(getBaseContext(), "Access token received!", Toast.LENGTH_LONG).show();
            // The data has been sent

            // The control should now go to Enter PIN Screen

            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.
            //
        }


        public String PostData(String url, String jsonstring)
        {
            // Create a new HttpClient and Post Header
            InputStream inputStream = null;
            String result = "";
            String clientid = "GaadiKeyClient";
            String clientkey = "gaadi";
            String access_token = ""; // this can be retrieved from sharedpreferences
            SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
            // sharedPref
            // retrieve this
            access_token = sharedPref.getString(getString(R.string.KEY_ACCESS_TOKEN), "the default stuff");
            Log.e("Register ", "The retrieved access token is "+access_token);
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);
                StringEntity se = new StringEntity(jsonstring);
                // 6. set httpPost Entity
                httpPost.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                //httpPost.setHeader("Accept", "application/json");
                httpPost.addHeader("Authorization", "Bearer "+access_token);
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // 10. convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.e("crash", "We are here and somehow crashing!");
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


}
