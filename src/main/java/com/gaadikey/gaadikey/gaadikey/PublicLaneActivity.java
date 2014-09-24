package com.gaadikey.gaadikey.gaadikey;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gaadikey.gaadikey.gaadikey.adaptor.PublicLaneAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;


public class PublicLaneActivity extends ListActivity {

    //@Override
    public static ArrayList<HashMap<String, String>> publicList = new ArrayList<HashMap<String, String>>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_lane);
        new PublicDataTask().execute("http://gaadikey.in/publiclane");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.public_lane, menu);
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

    public class PublicDataTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls)
        {
            return getPublicData(urls[0]);
        }

        protected void onPostExecute(String result)
        {
            publicList = new ArrayList<HashMap<String, String>>();
            Log.e("The result is ", result);
            try
            {
                JSONArray json = new JSONArray(result);
                // check if this request was sucessful... if the request was successful
                // then parse the phonebook and get contacts details
                // contacts details are rendered one by one .
                Log.e("The response recieved from the server is " , result );
                // result
                for(int i=0;i<json.length();i++)
                {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jObject = json.getJSONObject(i);
                    String vehiclename =           jObject.getString("vehiclename");
                    String gaadipic =         jObject.getString("gaadipic");
                    String modifiedOn    =         jObject.getString("modifiedOn");
               //     modifiedOn = "Joined public lane "+TimeUtils.millisToLongDHMS(24000)+" ago.";
//                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yy:HH:mm:SSS'Z'");
//                    Date date = DATE_FORMAT.parse(modifiedOn);
                    //2014-09-24T05:34:36.228Z
                    SimpleDateFormat formatter, FORMATTER;
                    formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                   // String oldDate = "2011-03-10T11:54:30.207Z";  //This is modifiedOn
                    Date date = formatter.parse(modifiedOn.substring(0, 24));
                    FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");

                    Log.e("Formatter date", FORMATTER.format(date));
                    Date newdate = FORMATTER.parse(FORMATTER.format(date));
                  //  System.out.println("NewDate-->"+FORMATTER.format(date));

                    long mills = newdate.getTime();
                    CharSequence cs = DateUtils.getRelativeTimeSpanString(mills);
                    if(!vehiclename.equals("null")) {
                        map.put("vehiclename", vehiclename);
                        map.put("gaadipic", gaadipic);
                        map.put("modifiedOn", "Joined public lane "+cs.toString()+" ago");
                        publicList.add(map);
                    }
                }
                //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobil                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          e, COUNTRIES));
                Log.e("The number of items in the list is ", ""+publicList.size());
                setListAdapter(new PublicLaneAdapter(PublicLaneActivity.this, publicList));
            }

            catch (Exception e)
            {
                Log.e("Exception", "The Exception has occured "+e.getMessage());
                // The exception has been logged.
            }

        }

    }


    public String getPublicData(String url)
    {
        InputStream inputStream = null;
        String result = "";
        SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
        String access_token = sharedPref.getString(getString(R.string.KEY_ACCESS_TOKEN), "the default stuff");
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // Creating the httpGetObject
            HttpGet httpGet = new HttpGet(url);
            //Adds the header to the GET http object.
            httpGet.addHeader("Authorization", "Bearer " + access_token);
            // Access Token is now attached as a Bearer token!
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;

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

    public void returnNothing()
    {
        /* This function returns nothing!

         */
    }


}
