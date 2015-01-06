package com.gaadikey.gaadikey.gaadikey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

//bikes.php

public class MyActivity extends Activity {

    ProgressBar pb;
    ProfileObject profile_object;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "945667950391";
    private Spinner spinner;
    private static final String[] paths = {"item 1", "item 2", "item 3"};
    private String UniqueNotifyID = "";


    ArrayList<HashMap<String, String>> bikedata;
    ArrayList<String> bikestringdata;

    ArrayList<HashMap<String, String>> cardata;
    ArrayList<String> carstringdata;

    ArrayList<HashMap<String, String>> cabdata;
    ArrayList<String> cabstringdata;

    ArrayList<HashMap<String, String>> busdata;
    ArrayList<String> busstringdata;

    ArrayList<HashMap<String, String>> rickshawdata;
    ArrayList<String> rickshawstringdata;

    String GAADI_IMAGEPATH = "";
    String GAADI_MESSAGE = "" ;
    String GAADI_NAME = "";
    String selected_profile = "bike";
    String PHONE_NUMBER = "";
    Tracker t;

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
        pb = (ProgressBar) findViewById(R.id.progress); // The progress bar has been identified.

        t = ((GaadiKey) getApplication()).getTracker(GaadiKey.TrackerName.APP_TRACKER);
        t.setScreenName("CompleteProfile"); // =
        t.send(new HitBuilders.AppViewBuilder().build());

        Button btn = (Button) findViewById(R.id.button2);
        btn.setEnabled(true);

        // Reading the phone  number from persistance storage
        SharedPreferences sharedPref = getSharedPreferences("android_shared",Context.MODE_PRIVATE);
        PHONE_NUMBER = sharedPref.getString(getString(R.string.KEY_phonenumber), "phonenumber");
        getRegId();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        t.send(new HitBuilders.EventBuilder()
                                .setCategory("BikeClick")
                                .setAction("Bike_Click")
                                .setLabel("")
                                .build());
                        Log.e("Bike is Clicked", "Bike");
                        ImageView caricon = (ImageView) findViewById(R.id.caricon);
                        TextView cartextview  = (TextView) findViewById(R.id.textView2);
                        Spinner carspinner = (Spinner) findViewById(R.id.carspinner);
                        cartextview.setVisibility(View.GONE);
                        caricon.setVisibility(View.GONE);
                        carspinner.setVisibility(View.GONE);
                        selected_profile = "bike";
                        new GetBikeDataTask().execute("http://gaadikey.com/bikes.php");
                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.e("Car is Clicked", "Car");
                        t.send(new HitBuilders.EventBuilder()
                                .setCategory("CarClick")
                                .setAction("Car_Click")
                                .setLabel("")
                                .build());
                        ImageView bikeicon = (ImageView) findViewById(R.id.bikeicon);
                        TextView biketextview = (TextView) findViewById(R.id.textView);
                        Spinner bikespinner = (Spinner) findViewById(R.id.spinner1);
                        bikeicon.setVisibility(View.GONE);
                        biketextview.setVisibility(View.GONE);
                        bikespinner.setVisibility(View.GONE);
                        selected_profile="car";
                        new GetCarDataTask().execute("http://gaadikey.com/cars.php");
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialogView();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("With which vehicle would you like to build your Gaadi Key profile?").setPositiveButton("Bike", dialogClickListener)
//                .setNegativeButton("Car", dialogClickListener).show();
       //  The url is sent to the method where the response is parsed for the TheBikeObject data
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
        // Remove the Button Focus
        // Disable further Clicks!

        final android.widget.Button completeProfileClick = (Button) findViewById(R.id.button2); // Identifier for complete profile button
        // tHE BLOW CODE DISABLES THE BUTTON
        completeProfileClick.setEnabled(false);
        completeProfileClick.setFocusable(false); // removes the focus from the button!

        Log.e("In Complete Profile Click" , "Before");
        final EditText gaadiMsgField = (EditText) findViewById(R.id.gaadiMsg);
        Log.e("Text content is ", gaadiMsgField.getText().toString());
        GAADI_MESSAGE = gaadiMsgField.getText().toString();
        //startActivity(new Intent(MyActivity.this, SettingUpActivity.class));
        Log.e("Complete_Profile_Button_Clicked ", "Complete Profile Clicked.");
        profile_object = new ProfileObject();
        profile_object.set_phonenumber(PHONE_NUMBER);
        profile_object.set_deviceid(regid);
        profile_object.set_gaadimsg(GAADI_MESSAGE);
        profile_object.set_notifyid(regid);
        Spinner bikedropdown = (Spinner) findViewById(R.id.spinner1);
        Spinner cardropdown  = (Spinner) findViewById(R.id.carspinner);
        Spinner busdropdown  = (Spinner) findViewById(R.id.busspinner);
        Spinner cabdropdown  = (Spinner) findViewById(R.id.cabspinner);
        Spinner rickshawdropdown = (Spinner) findViewById(R.id.rickshawspinner);

        if(selected_profile.equals("bike")) {
            GAADI_IMAGEPATH = bikedata.get(bikedropdown.getSelectedItemPosition()).get("bike_image");
            GAADI_NAME = bikedata.get(bikedropdown.getSelectedItemPosition()).get("bike_name");
            profile_object.set_gaadipic(GAADI_IMAGEPATH);
            profile_object.set_vehicle_name(GAADI_NAME);
            profile_object.set_vehicletype("2 wheeler");
        }
        else if(selected_profile.equals("car"))
        {
            GAADI_IMAGEPATH = cardata.get(cardropdown.getSelectedItemPosition()).get("car_image");
            GAADI_NAME = cardata.get(cardropdown.getSelectedItemPosition()).get("car_name");
            profile_object.set_gaadipic(GAADI_IMAGEPATH);
            profile_object.set_vehicle_name(GAADI_NAME);
            profile_object.set_vehicletype("4 wheeler");
        }
        else if(selected_profile.equals("cab"))
        {
            GAADI_IMAGEPATH = cabdata.get(cabdropdown.getSelectedItemPosition()).get("service_image");
            GAADI_NAME = cabdata.get(cabdropdown.getSelectedItemPosition()).get("service_name");
            profile_object.set_gaadipic(GAADI_IMAGEPATH);
            profile_object.set_vehicle_name(GAADI_NAME);
            profile_object.set_vehicletype("cab wheeler");

        }
        else if(selected_profile.equals("bus"))
        {
            GAADI_IMAGEPATH = busdata.get(busdropdown.getSelectedItemPosition()).get("bus_image");
            GAADI_NAME = busdata.get(busdropdown.getSelectedItemPosition()).get("bus_name");
            profile_object.set_gaadipic(GAADI_IMAGEPATH);
            profile_object.set_vehicle_name(GAADI_NAME);
            profile_object.set_vehicletype("bus wheeler");

        }
        else if(selected_profile.equals("rickshaw"))
        {
            GAADI_IMAGEPATH = rickshawdata.get(rickshawdropdown.getSelectedItemPosition()).get("rickshaw_image");
            GAADI_NAME = rickshawdata.get(rickshawdropdown.getSelectedItemPosition()).get("rickshaw_name");
            profile_object.set_gaadipic(GAADI_IMAGEPATH);
            profile_object.set_vehicle_name(GAADI_NAME);
            profile_object.set_vehicletype("rickshaw wheeler");

        }
        new RegisterUserTask().execute("https://gaadikey.in/register");
        Log.e("The complete profile has been clicked" , "The completed profile has been clicked ");
        // Testing by commenting out request stuff!
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
                    Log.i("The recieved  id is " , regid);
                    Button btn = (Button) findViewById(R.id.button2);
                    btn.setEnabled(true);
                   // startActivity(new Intent(MyActivity.this, ListMobileActivity.class));

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

    private void AlertDialogView() {
        final CharSequence[] items = { "I have a bike", "I have a car", "I take a cab", "I take a bus", "I take a rickshaw" };

        final AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);//ERROR ShowDialog cannot be resolved to a type
        builder.setCancelable(false);
        builder.setTitle("How do you commute?");
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item)
                    {
                        if(item == 0)
                        {
                            // Go to Choosing bike page
                            t.send(new HitBuilders.EventBuilder()
                                    .setCategory("BikeClick")
                                    .setAction("Bike_Click")
                                    .setLabel("")
                                    .build()); // Analytics for Google!

                            Log.e("Bike is Clicked", "Bike");
                            RelativeLayout bikeWidget = (RelativeLayout) findViewById(R.id.bikewidget);
                            bikeWidget.setVisibility(View.VISIBLE);


//                            ImageView bikeicon = (ImageView) findViewById(R.id.bikeicon);
//                            TextView biketextview = (TextView) findViewById(R.id.textView);
//                            Spinner bikespinner = (Spinner) findViewById(R.id.spinner1);
//                            bikeicon.setVisibility(View.VISIBLE);
//                            biketextview.setVisibility(View.VISIBLE);
//                            bikespinner.setVisibility(View.VISIBLE);

                            selected_profile = "bike";
                            new GetBikeDataTask().execute("http://gaadikey.com/bikes.php");
                            dialog.dismiss();
                            //Yes button clicked

                        }
                        if(item == 1)
                        {
                            Log.e("Car is Clicked", "Car");
                            t.send(new HitBuilders.EventBuilder()
                                    .setCategory("CarClick")
                                    .setAction("Car_Click")
                                    .setLabel("")
                                    .build());

                            RelativeLayout carWidget = (RelativeLayout) findViewById(R.id.carwidget);
                            carWidget.setVisibility(View.VISIBLE);

//                            ImageView caricon = (ImageView) findViewById(R.id.caricon);
//                            TextView cartextview  = (TextView) findViewById(R.id.textView2);
//                            Spinner carspinner = (Spinner) findViewById(R.id.carspinner);
//                            cartextview.setVisibility(View.VISIBLE);
//                            caricon.setVisibility(View.VISIBLE);
//                            carspinner.setVisibility(View.VISIBLE);
                            selected_profile="car";
                            new GetCarDataTask().execute("http://gaadikey.com/cars.php");
                            dialog.dismiss();
                            // Go Car page
                        }

                        if( item ==2 )
                        {
                            Log.e("Cab is clicked" , "Cab");
                            t.send(new HitBuilders.EventBuilder()
                                    .setCategory("CabClick")
                                    .setAction("Cab_Click")
                                    .setLabel("")
                                    .build());

                            RelativeLayout cabWidget = (RelativeLayout) findViewById(R.id.cabwidget);
                            cabWidget.setVisibility(View.VISIBLE);
                            selected_profile = "cab";
                            new GetCabDataTask().execute("http://gaadikey.com/cabs.php");
                            dialog.dismiss();

                        }

                        if( item ==3 )
                        {
                            Log.e("Bus is clicked" , "Bus");
                            t.send(new HitBuilders.EventBuilder()
                                    .setCategory("BusClick")
                                    .setAction("Bus_Click")
                                    .setLabel("")
                                    .build());

                            RelativeLayout buswidget = (RelativeLayout) findViewById(R.id.buswidget);
                            buswidget.setVisibility(View.VISIBLE);
                            selected_profile = "bus";
                            new GetBusDataTask().execute("http://gaadikey.com/buses.php");
                            dialog.dismiss();

                        }

                        if( item == 4 )
                        {
                            Log.e("Rickshaw is clicked " , "Rickshaw");
                            t.send(new HitBuilders.EventBuilder()
                                    .setCategory("RickshawClick")
                                    .setAction("Rickshaw_Click")
                                    .setLabel("")
                                    .build());

                            RelativeLayout rickshawwidget = (RelativeLayout) findViewById(R.id.rickshawwidget);
                            rickshawwidget.setVisibility(View.VISIBLE);
                            selected_profile = "rickshaw";
                            new GetRickshawDataTask().execute("http://gaadikey.com/rickshaws.php");
                            dialog.dismiss();

                        }


                        Toast.makeText(getApplicationContext(), items[item],
                                Toast.LENGTH_SHORT).show();
                        // Open up activity with corresponding options!

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private class RegisterUserTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls)
        {

            return RegisterThisUser(urls[0], profile_object);


        }
        protected void onPostExecute(String result)
        {

                Log.e("Success posting", result);
                Log.e("Response from the registration step ", result);
                try
                {
                    // Save the following things in sharedStorage
                    SharedPreferences sharedPref = getSharedPreferences("android_shared",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sharedPref.edit();
                    editor2.putString(getString(R.string.KEY_signupstatus), Constants.PROFILE_REGISTERED);
                    editor2.commit();

                    SharedPreferences sharedPref2 = getSharedPreferences("android_shared",  Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor_2 = sharedPref2.edit();
                    editor_2.putString(getString(R.string.KEY_GaadiName), GAADI_NAME);
                    editor_2.commit();

                    SharedPreferences sharedPref3 = getSharedPreferences("android_shared", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor_3 = sharedPref3.edit();
                    editor_3.putString(getString(R.string.KEY_GaadiMsg), GAADI_MESSAGE);
                    editor_3.commit();


                    SharedPreferences sharedPref4 = getSharedPreferences("android_shared", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor_4 = sharedPref4.edit();
                    String rootstring = "http://gaadikey.com/images";
                    String web_image_path =  GAADI_IMAGEPATH;
                    String path = "";
                    if(web_image_path.length() > rootstring.length() + 10 )  path = web_image_path.substring(rootstring.length());
                    String resize_path = "http://gaadikey.com/images/resize.php?src="+path+"&w=200";

                    Log.e("Image path is ", path);
                    editor_4.putString(getString(R.string.KEY_GaadiImage), resize_path);
                    editor_4.commit();

                    // All details stored in persistant , Should be able to retrieve the values with the respective keys!
                    Log.e("Profile Registered State saved ", "Proof "+sharedPref.getString(getString(R.string.KEY_signupstatus), "default signup stuff"));
                    Log.e("Persistence" , "Successfully inserted the access_token into the sharedPreferenes storage");
                    String theString = sharedPref.getString(getString(R.string.KEY_ACCESS_TOKEN), "the default stuff");
                    Log.e("Retrived value",  "The retrieved stuff is "+theString ) ;
                    startActivity(new Intent(MyActivity.this, SettingUpActivity.class));
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





    public String RegisterThisUser(String url, ProfileObject profile_object ) {
        // Create a new HttpClient and Post Header
        InputStream inputStream = null;
        String result = "";
        String clientid = "GaadiKeyClient";
        String clientkey = "gaadi";
        String access_token = ""; // this can be retrieved from sharedpreferences
        SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
        //sharedPref which picks the token
        // sharedPref
        // retrieve this
        access_token = sharedPref.getString(getString(R.string.KEY_ACCESS_TOKEN), "the default stuff");
        Log.e("Register ", "The retrieved access token is "+access_token);
        //access_token
        try {

            Log.e("Inside Try block URL is ", url);
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("deviceid", profile_object.get_deviceid());
            jsonObject.accumulate("phonenumber", profile_object.get_phonenumber());
            jsonObject.accumulate("gaadimsg", profile_object.get_gaadimsg());
            jsonObject.accumulate("gaadipic", profile_object.get_gaadipic());
            jsonObject.accumulate("profilepic", profile_object.get_profilepic());
            jsonObject.accumulate("notifyid", profile_object.get_notifyid());
            jsonObject.accumulate("vehicletype", profile_object.get_vehicletype());
            jsonObject.accumulate("vehiclename", profile_object.get_vehiclename());
            Log.e("Hello the vehiclename is ", profile_object.get_vehiclename());
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.e("Json uploaded", "The Uploaded json looks like " + json);
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content
            //httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-version", getString(R.string.API_VERSION)); // Registering the User using API 0.0.1 ..
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

            } else
                result = "Did not work!";

        } catch (Exception e)
        {
            Log.e("Crashed again", "Crashed");
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




    private class GetBikeDataTask extends AsyncTask<String, Void, String>
    {

        protected void onPreExecute()
        {
            pb.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls)
        {


            Log.e("Pinging this URL ---> ", urls[0]);
            return GetBikeData(urls[0]);


        }
        protected void onPostExecute(String result)
        {
            pb.setVisibility(View.GONE);
            Log.e("And we received", result);
            Log.e("Response from the registration step ", result);
            try
            {
                    // The populate spinner should be present here!
                    PopulateBikeSpinner(result);
            }
            catch(Exception e)
            {
                Log.e("Parse", "Exception in parsing");

            }

            // The data has been sent

            // The control should now go to Enter PIN Screen

            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.
            //
        }
    }


    // GetBikeData is called



    public void PopulateBikeSpinner(String result)
    {

        try {
            JSONArray json = new JSONArray(result);
            // check if this request was sucessful... if the request was successful
            // then parse the phonebook and get contacts details
            // contacts details are rendered one by one.
            Log.e("The response recieved from the server is " , result );
            // result
            bikedata = new ArrayList<HashMap<String, String>>();
            bikestringdata = new ArrayList<String>();

            for(int i=0;i<json.length();i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jObject = json.getJSONObject(i);
                String id =                 jObject.getString("id");
                String bike_name =          jObject.getString("bike_name");
                String bike_image    =      jObject.getString("bike_image");
                String bike_brand   =       jObject.getString("bike_brand");
                String priority       =     jObject.getString("priority");
                map.put("id" , id);
                map.put("bike_name", bike_name);
                map.put("bike_image", bike_image);
                map.put("bike_brand", bike_brand);
                map.put("priority", priority);
                Log.e("Bikes name received is  ", bike_name);
                bikedata.add(map);
                bikestringdata.add(bike_name);
                // loading these variables
            }
            //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobile, COUNTRIES));
            Spinner bikedropdown = (Spinner) findViewById(R.id.spinner1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bikestringdata); // passing the array list instead of array!
            bikedropdown.setAdapter(adapter);

            bikedropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    // Since 0 is returned for the first item... don't do anything .
                 //   if(position!=0)
                    {
                        Log.e("The Selected index in bike dropdown is ", "" + position);
                        ImageView bikeicon_imageview = (ImageView) findViewById(R.id.bikeicon);

                        String rootstring = "http://gaadikey.com/images";
                        String web_image_path =  bikedata.get(position).get("bike_image");
                        String path = "";
                        if(web_image_path.length() > rootstring.length() + 10 )  path = web_image_path.substring(rootstring.length());
                        String resize_path = "http://gaadikey.com/images/resize.php?src="+path+"&w=200";
                        Log.e("Resize path is ", resize_path);
                        new ImageDownloader(bikeicon_imageview).execute(resize_path);
                    }
                    // Now change the bike images, based on the selection among bikes
                    // your code here
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView)
                {

                    Log.e("The very first time ", "Nothing is selected ");
                    // your code here
                }
            });

        }

        catch(Exception e)
        {

        }
    }


    public  String GetBikeData(String url){
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


    private class GetCarDataTask extends AsyncTask<String, Void, String>
    {

        protected void onPreExecute()
        {
            pb.setVisibility(View.VISIBLE); // The progressbar is now shown!

        }

        protected String doInBackground(String... urls)
        {


            Log.e("Pinging this URL ---> ", urls[0]);
            return GetCarData(urls[0]);


        }
        protected void onPostExecute(String result)
        {
            pb.setVisibility(View.GONE); // The progressbar is now gone!
            Log.e("And we received", result);
            Log.e("Response from the registration step ", result);
            try
            {
                // The populate spinner should be present here!
                PopulateCarSpinner(result);
            }
            catch(Exception e)
            {
                Log.e("Parse", "Exception in parsing");

            }

            // The data has been sent

            // The control should now go to Enter PIN Screen

            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.
            //
        }
    }


    public void PopulateCarSpinner(String result)
    {



        try {
            JSONArray json = new JSONArray(result);
            // check if this request was sucessful... if the request was successful
            // then parse the phonebook and get contacts details
            // contacts details are rendered one by one.
            Log.e("The response recieved from the server is " , result );
            // result
            cardata = new ArrayList<HashMap<String, String>>();
            carstringdata = new ArrayList<String>();

            for(int i=0;i<json.length();i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jObject = json.getJSONObject(i);
                String id =                 jObject.getString("id");
                String car_name =          jObject.getString("car_name");
                String car_image    =      jObject.getString("car_image");
                String car_brand   =       jObject.getString("car_brand");
                String priority       =    jObject.getString("priority");
                map.put("id" , id);
                map.put("car_name", car_name);
                map.put("car_image", car_image);
                map.put("car_brand", car_brand);
                map.put("priority", priority);
                Log.e("Cars name received is  ", car_name);
                cardata.add(map);
                carstringdata.add(car_name);
                // loading these variables
            }

            Spinner cardropdown = (Spinner) findViewById(R.id.carspinner);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, carstringdata);
            cardropdown.setAdapter(adapter1);

            // Car On selected event

            cardropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    Log.e("The Selected index in cardrop down is ", ""+position);

                  //  if(position!=0)
                    {
                        Log.e("The Selected index in car dropdown is ", "" + position);
                        ImageView caricon_imageview = (ImageView) findViewById(R.id.caricon);

                        String rootstring = "http://gaadikey.com/images";
                        String web_image_path =  cardata.get(position).get("car_image");
                        String path = "";
                        if(web_image_path.length() > rootstring.length() + 10 )  path = web_image_path.substring(rootstring.length());
                        String resize_path = "http://gaadikey.com/images/resize.php?src="+path+"&w=200";
                        Log.e("Resize path is", resize_path);
                        new ImageDownloader(caricon_imageview).execute(resize_path);


                      //  new ImageDownloader(caricon_imageview).execute(cardata.get(position).get("car_image"));
                    }

                    // The below code is pending as we are not retrieving the car data

//                    ImageView bikeicon_imageview = (ImageView) findViewById(R.id.bikeicon);
//                    new ImageDownloader(bikeicon_imageview).execute(bikedata.get(position).get("ImgUrl"));


                    // your code here
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            // Bike on selected event



        }

        catch(Exception e)
        {

        }

    }


    public  String GetCarData(String url){
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




    private class GetCabDataTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            pb.setVisibility(View.VISIBLE); // Shows the progress bar
        }

        protected String doInBackground(String... urls)
        {


            Log.e("Pinging this URL ---> ", urls[0]);
            return GetCabData(urls[0]);


        }
        protected void onPostExecute(String result)
        {
            pb.setVisibility(View.GONE); // Remove the progress bar!
            Log.e("And we received", result);
            Log.e("Response from the registration step ", result);
            try
            {
                // The populate spinner should be present here!
                PopulateCabSpinner(result);
            }
            catch(Exception e)
            {
                Log.e("Parse", "Exception in parsing");

            }

            // The data has been sent

            // The control should now go to Enter PIN Screen

            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.
            //
        }
    }


    public void PopulateCabSpinner(String result)
    {



        try {
            JSONArray json = new JSONArray(result);
            // check if this request was sucessful... if the request was successful
            // then parse the phonebook and get contacts details
            // contacts details are rendered one by one.
            Log.e("The response recieved from the server is " , result );
            // result
            cabdata = new ArrayList<HashMap<String, String>>();
            cabstringdata = new ArrayList<String>();

            for(int i=0;i<json.length();i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jObject = json.getJSONObject(i);
                String id =                 jObject.getString("id");
                String service_name =          jObject.getString("service_name");
                String service_image    =      jObject.getString("service_image");

                map.put("id" , id);
                map.put("service_name", service_name);
                map.put("service_image", service_image);

                Log.e("Cabs name received is  ", service_name);
                cabdata.add(map);
                cabstringdata.add(service_name);
                // loading these variables
            }

            Spinner cabdropdown = (Spinner) findViewById(R.id.cabspinner);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cabstringdata);
            cabdropdown.setAdapter(adapter1);

            // Car On selected event

            cabdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    Log.e("The Selected index in cardrop down is ", ""+position);

                  //  if(position!=0)
                    {
                        Log.e("The Selected index in car dropdown is ", "" + position);
                        ImageView caricon_imageview = (ImageView) findViewById(R.id.cabicon);

                        String rootstring = "http://gaadikey.com/images";
                        String web_image_path =  cabdata.get(position).get("service_image");
                        String path = "";
                        if(web_image_path.length() > rootstring.length() + 10 )  path = web_image_path.substring(rootstring.length());
                        String resize_path = "http://gaadikey.com/images/resize.php?src="+path+"&w=200";
                        Log.e("Resize path is", resize_path);
                        new ImageDownloader(caricon_imageview).execute(resize_path);


                        //  new ImageDownloader(caricon_imageview).execute(cardata.get(position).get("car_image"));
                    }

                    // The below code is pending as we are not retrieving the car data

//                    ImageView bikeicon_imageview = (ImageView) findViewById(R.id.bikeicon);
//                    new ImageDownloader(bikeicon_imageview).execute(bikedata.get(position).get("ImgUrl"));


                    // your code here
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            // Bike on selected event



        }

        catch(Exception e)
        {

        }

    }

    public  String GetCabData(String url)
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
            }
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }


    private class GetBusDataTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            pb.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls)
        {


            Log.e("Pinging this URL ---> ", urls[0]);
            return GetBusData(urls[0]);


        }
        protected void onPostExecute(String result)
        {
            pb.setVisibility(View.GONE); // It is gone!

            Log.e("And we received", result);
            Log.e("Response from the registration step ", result);
            try
            {
                // The populate spinner should be present here!
                PopulateBusSpinner(result);
            }
            catch(Exception e)
            {
                Log.e("Parse", "Exception in parsing");

            }

            // The data has been sent

            // The control should now go to Enter PIN Screen

            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.
            //
        }
    }


    public void PopulateBusSpinner(String result)
    {

        try {
            JSONArray json = new JSONArray(result);
            // check if this request was sucessful... if the request was successful
            // then parse the phonebook and get contacts details
            // contacts details are rendered one by one.
            Log.e("The response recieved from the server is " , result );
            // result
            busdata = new ArrayList<HashMap<String, String>>();
            busstringdata = new ArrayList<String>();
            for(int i=0;i<json.length();i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jObject = json.getJSONObject(i);
                String id =                 jObject.getString("id");
                String bus_name =          jObject.getString("bus_name");
                String bus_image    =      jObject.getString("bus_image");
                map.put("id" , id);
                map.put("bus_name", bus_name);
                map.put("bus_image", bus_image);
                Log.e("Bus name received is  ", bus_name);
                busdata.add(map);
                busstringdata.add(bus_name);

                // loading these variables
            }

            Spinner busdropdown = (Spinner) findViewById(R.id.busspinner);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, busstringdata);
            busdropdown.setAdapter(adapter1);

            // Car On selected event

            busdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    Log.e("The Selected index in cardrop down is ", ""+position);
                    //  if(position!=0)
                    {
                        Log.e("The Selected index in car dropdown is ", "" + position);
                        ImageView busicon_imageview = (ImageView) findViewById(R.id.busicon);
                        String rootstring = "http://gaadikey.com/images";
                        String web_image_path =  busdata.get(position).get("bus_image");
                        String path = "";
                        if(web_image_path.length() > rootstring.length() + 10 )  path = web_image_path.substring(rootstring.length());
                        String resize_path = "http://gaadikey.com/images/resize.php?src="+path+"&w=200";
                        Log.e("Resize path is", resize_path);
                        new ImageDownloader(busicon_imageview).execute(resize_path);
                        //  new ImageDownloader(caricon_imageview).execute(cardata.get(position).get("car_image"));
                    }

                    // The below code is pending as we are not retrieving the car data

//                    ImageView bikeicon_imageview = (ImageView) findViewById(R.id.bikeicon);
//                    new ImageDownloader(bikeicon_imageview).execute(bikedata.get(position).get("ImgUrl"));


                    // your code here
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            // Bike on selected event



        }

        catch(Exception e)
        {

        }

    }


    public  String GetBusData(String url){
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

    private class GetRickshawDataTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            pb.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls)
        {


            Log.e("Pinging this URL ---> ", urls[0]);
            return GetRickshawData(urls[0]);


        }
        protected void onPostExecute(String result)
        {
            pb.setVisibility(View.GONE); // The progressbar is now removed!
            Log.e("And we received", result);
            Log.e("Response from the registration step ", result);
            try
            {
                // The populate spinner should be present here!
                PopulateRickshawSpinner(result);
            }
            catch(Exception e)
            {
                Log.e("Parse", "Exception in parsing");

            }

            // The data has been sent

            // The control should now go to Enter PIN Screen

            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.
            //
        }
    }


    public void PopulateRickshawSpinner(String result)
    {



        try {
            JSONArray json = new JSONArray(result);
            // check if this request was sucessful... if the request was successful
            // then parse the phonebook and get contacts details
            // contacts details are rendered one by one.
            Log.e("The response recieved from the server is " , result );
            // result
            rickshawdata = new ArrayList<HashMap<String, String>>();
            rickshawstringdata = new ArrayList<String>();

            for(int i=0;i<json.length();i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jObject = json.getJSONObject(i);
                String id =                 jObject.getString("id");
                String rickshaw_name =          jObject.getString("rickshaw_name");
                String rickshaw_image    =      jObject.getString("rickshaw_image");

                map.put("id" , id);
                map.put("rickshaw_name", rickshaw_name);
                map.put("rickshaw_image", rickshaw_image);
                Log.e("Rickshaw name received is  ", rickshaw_name);
                rickshawdata.add(map);
                rickshawstringdata.add(rickshaw_name);
                // loading these variables
            }

            Spinner rickshawdropdown = (Spinner) findViewById(R.id.rickshawspinner);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rickshawstringdata);
            rickshawdropdown.setAdapter(adapter1);

            // Car On selected event

            rickshawdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    Log.e("The Selected index in cardrop down is ", ""+position);

                    //  if(position!=0)
                    {
                        Log.e("The Selected index in car dropdown is ", "" + position);
                        ImageView caricon_imageview = (ImageView) findViewById(R.id.rickshawicon);

                        String rootstring = "http://gaadikey.com/images";
                        String web_image_path =  rickshawdata.get(position).get("rickshaw_image");
                        String path = "";
                        if(web_image_path.length() > rootstring.length() + 10 )  path = web_image_path.substring(rootstring.length());
                        String resize_path = "http://gaadikey.com/images/resize.php?src="+path+"&w=200";
                        Log.e("Resize path is", resize_path);
                        new ImageDownloader(caricon_imageview).execute(resize_path);
                        //  new ImageDownloader(caricon_imageview).execute(cardata.get(position).get("car_image"));
                    }

                    // The below code is pending as we are not retrieving the car data

//                    ImageView bikeicon_imageview = (ImageView) findViewById(R.id.bikeicon);
//                    new ImageDownloader(bikeicon_imageview).execute(bikedata.get(position).get("ImgUrl"));


                    // your code here
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            // Bike on selected event



        }

        catch(Exception e)
        {

        }

    }


    public  String GetRickshawData(String url){
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




    public void onBackPressed()
    {

        RelativeLayout rickshawWidget = (RelativeLayout) findViewById(R.id.rickshawwidget);
        rickshawWidget.setVisibility(View.GONE);

        RelativeLayout bikeWidget = (RelativeLayout) findViewById(R.id.bikewidget);
        bikeWidget.setVisibility(View.GONE);

        RelativeLayout carWidget = (RelativeLayout) findViewById(R.id.carwidget);
        carWidget.setVisibility(View.GONE);

        RelativeLayout cabWidget = (RelativeLayout) findViewById(R.id.cabwidget);
        cabWidget.setVisibility(View.GONE);

        RelativeLayout busWidget = (RelativeLayout) findViewById(R.id.buswidget);
        busWidget.setVisibility(View.GONE);

        AlertDialogView(); // Shows the dialog to choose among transportation mode
    }





}


