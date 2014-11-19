package com.gaadikey.gaadikey.gaadikey;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


public class EnterPINActivity extends ActionBarActivity {


    public String PIN = "";
    public String phonenumber = "";

    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private Button startB;
    private final long startTime = 150 * 1000;
    private final long interval = 1 * 1000;
    private TextView TimeText;
    Tracker t;


    //SharedPreferences sharedPref = getSharedPreferences("New", Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);
        Log.e("PIN", "Entered PIN activity. You will have to Enter the PIN. ");
        Log.e("Flow", "The control has reached here!");
        TimeText = (TextView) findViewById(R.id.TimeText);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
        t = ((GaadiKey) getApplication()).getTracker(GaadiKey.TrackerName.APP_TRACKER);
        t.setScreenName("EnterPINActivity"); // =
        t.send(new HitBuilders.AppViewBuilder().build());

        // commented out!
        //Log.e("Access token (persistant) " , "The access token which is saved is " +defaultString);
       // int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(KEY_CONTENT, mContent);
    }


    //Disable Menu Items to stop showing Settings and Other Options on Menu Click

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.enter_pin, menu);
//        return true;
//    }

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

    public void PIN_Submission_Click(View Button)
    {
        t.send(new HitBuilders.EventBuilder()
                .setCategory("PINSubmitted")
                .setAction("PIN_SubmitClick")
                .setLabel("")
                .build());
        Log.e("The Button Click has occured. ", "Click");
        //Disable the Submission button immediately after click event has occurred. In order to avoid multiple clicks!


        //     // PIN submission click.. When the PIN submit is clicked.... It has to post the received PIN to server to receive the access token.
        Log.e("PIN Submission CLICK", "The PIN submission has been clicked...");
        final EditText pinField = (EditText) findViewById(R.id.PIN);

        PIN = pinField.getText().toString();
        Log.e("PIN" , "The recieved PIN is "+PIN) ;
        SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
        phonenumber = sharedPref.getString(getString(R.string.KEY_phonenumber), "the default stuff");
        Log.e("PIN verification" , "The phone number retrieved is "+phonenumber);
        // The Recieved PIN is ..
        new HttpAsyncGetTask().execute("https://gaadikey.in/generated?phonenumber="+phonenumber);
    }
    // write  the httpasyncgettask function  out here...
    // this has to submit the phonenumber to server and should return the access token... along with the expiry date if any  .
    // here is the function which does http get task asynchronously....
    // should add "GET method" which just accepts the URL which is appended with PIN.  http://gaadikey.in/generated?phonenumber=9739888428

    private class HttpAsyncGetTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            Log.e("GET called " , " The url is "+urls[0]);
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
                    new AlertDialog.Builder(EnterPINActivity.this)
                            .setTitle("Verification Status")
                            .setMessage("Verification Success! Thanks for verifying your PIN. You can now build Gaadi Key profile")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sharedPref.edit();
                    //The verified phone number is updated.
                    editor2.putString(getString(R.string.KEY_phonenumber), phonenumber);
                    editor2.commit();

                     new  GetAccessTokenPostTask().execute("https://gaadikey.in/token");

                    startActivity(new Intent(EnterPINActivity.this, MyActivity.class));
                    finish();

                    // Store the phonenumber if PIN is verified!

                    // KEY_phonenumber


                } else
                {
                    new AlertDialog.Builder(EnterPINActivity.this)
                            .setTitle("Verification Status")
                              .setMessage("The PIN which you entered is not matching! Please enter a valid PIN.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }

                // startActivity
            }

            catch (Exception e)
            {
                    Log.e("Exception", "The Exception has occured "+e.getMessage());
                    // The exception has been logged.
            }

        }
    }


    private class GetAccessTokenPostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls)
        {

            Log.e("Post", "Calling this URL in background "+urls[0]);
            return generateAccessToken(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {

            Log.e("Success posting", result);

            try
             {
                    JSONObject jObject = new JSONObject(result);
                    String access_token = jObject.getString("access_token");
                    String token_type   = jObject.getString("token_type");
                    Log.e("The access token is",  access_token );
                    Log.e("The token type is",   token_type );
                    // Save the following things in sharedStorage


                    SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.KEY_signupstatus), Constants.PIN_VERIFIED);
                    editor.commit();

                   // SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sharedPref.edit();
                    editor2.putString(getString(R.string.KEY_ACCESS_TOKEN), access_token);
                    editor2.commit();


                    Log.e("Persistence" , "Successfully inserted the access_token into the sharedPreferenes storage");


                    String theString = sharedPref.getString(getString(R.string.KEY_ACCESS_TOKEN), "the default stuff");
                    Log.e("Retrived value",  "The retrieved stuff is "+theString ) ;


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
    }

 //  This method has been put outside the class so that it fixes the this references appropriately.

    public String generateAccessToken(String url)
    {

        Log.e("URL" , "the URL is "+url);
        InputStream inputStream = null;
        String result = "";
        String clientid = "GaadiKeyClient";
        String clientkey = "gaadi";
        try
        {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
            phonenumber = sharedPref.getString(getString(R.string.KEY_phonenumber), "the default stuff");

            String formdata = "grant_type=password&username="+phonenumber+"&password="+PIN;
            Log.e("Json uploaded", "The Uploaded form data looks like "+formdata);
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(formdata);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            String credentials = clientid + ":" + clientkey;
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);// 7. Set some headers to inform server about the type of the content
            httpPost.addHeader("Authorization", "Basic " + base64EncodedCredentials);//httpPost.setHeader("Accept", "application/json");
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
                            .setMessage("Verification Success! Thanƒks for verifying your PIN. You can now build Gaadi Key profile")
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
    // This converts the received data which is in input stream format to string.

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        // reads line by line to generate the string..
        inputStream.close();
        return result;
    }


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override

        public void onFinish() {

            TimeText.setText("Time's up!");
        }
        @Override
        public void onTick(long millisUntilFinished) {

            Log.e("Tick Tick", "Tick Tick");
            TimeText.setText("You will receive PIN within " + millisUntilFinished / 1000 + " seconds");
        }
    }

}
