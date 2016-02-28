package com.gaadikey.gaadikey.gaadikey.Activities;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gaadikey.gaadikey.gaadikey.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


//This time it is no json..

//Basic auth required

//Only form data body is required .

public class AccessTokenActivity extends ActionBarActivity {

    // Everything is blank in the UI.. The actual code to get the access token is here!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_token);

        getAccessToken();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.access_token, menu);
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


    public void getAccessToken()
    {
        new HttpAsyncPostTask().execute("http://gaadikey.in:8081/register");
        // Call a function which does a post request along with appropriate header
        // if successful
            // The response should be a access token
        // if error
        //  Invalid  response which you cannot handle
    }


    private class HttpAsyncPostTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return postData(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {

            Log.e("Success posting", result);
            new AlertDialog.Builder(AccessTokenActivity.this)
                    .setTitle("Registration (2/2)")
                    .setMessage("We have recieved your Phone number. Please check your Email Inbox for the PIN and verify it below.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();

            // This would display the alert saying that you posted it

        }


    }



    public String postData(String url) {
        // Create a new HttpClient and Post Header

        String clientid  = "GaadiKeyClient";
        String clientkey = "gaadi";

        InputStream inputStream = null;
        String result = "";
        try
        {

            JSONObject jObject = new JSONObject(result);
            // JSON object created result...

            //parse for the object
            // hi and hello
            // string hello = jObject.getString("hello");
            // string hi = jObject.getString("hi");

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);
            // 5. set json to StringEntity
            String thebody = "grant_type=password&username=che2on&password=p@ssw0rd";
            StringEntity se = new StringEntity(thebody);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content
            //httpPost.setHeader("Accept", "application/json");
            String credentials = clientid + ":" + clientkey;
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            httpPost.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
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
