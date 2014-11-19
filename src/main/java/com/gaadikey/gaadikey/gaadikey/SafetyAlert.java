package com.gaadikey.gaadikey.gaadikey;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SafetyAlert extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomes_you);
        new RegisteredUserCountTask().execute("https://gaadikey.in/getCount");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcomes_you, menu);
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

    public void BackHome_Clicked(View button)
    {
        Log.e("Button clicked ", "Yes");
        // Take this activity to StickyHome
        // Going to stickey home would be like going back to home.
        startActivity(new Intent(SafetyAlert.this, StickyHome.class));

    }

    private class RegisteredUserCountTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return  getUserCount(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {

            try
            {
                JSONObject jObject = new JSONObject(result);
                String user_count = jObject.getString("registered_users");
                Log.e("The number of registered users is ", user_count);
                TextView t = (TextView) findViewById(R.id.textstring2);
                t.setText("You are one among the "+user_count+" happy Gaadi Key users");

            }

            catch (Exception e)
            {

                Log.e("This request didn't complete ", e.getLocalizedMessage());

            }


        }
    }


    public String getUserCount(String url)
    {

        Log.e("The url to be pinged is ", url);
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

}
