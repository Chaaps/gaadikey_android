package com.gaadikey.gaadikey.gaadikey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by madratgames on 09/10/14.
 */
public class Fragment_Settings extends Fragment {


    String PHONE_NUMBER = ""; // PHONNUMBER get it from the persistance storage!
    View view = null;
    ArrayList<HashMap<String, String>> bikedata;
    ArrayList<String> bikestringdata;

    ArrayList<HashMap<String, String>> cardata;
    ArrayList<String> carstringdata;

    String GAADI_IMAGEPATH = "";
    String GAADI_MESSAGE = "" ;
    String GAADI_NAME = "";
    String selected_profile = "bike";
    ProfileObject profile_object;


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button UpdateProfileButton = (Button) view.findViewById(R.id.updateProfileButton);
        // Handle the click event for updateProfile button!
        UpdateProfileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                final EditText gaadiMsgField = (EditText) view.findViewById(R.id.gaadiMsg);
                GAADI_MESSAGE = gaadiMsgField.getText().toString();
                //startActivity(new Intent(MyActivity.this, SettingUpActivity.class));
                Log.e("Complete_Profile_Button_Clicked ", "Complete Profile Clicked.");
                profile_object = new ProfileObject();
                profile_object.set_phonenumber(PHONE_NUMBER);
             //   profile_object.set_deviceid(regid);
                profile_object.set_gaadimsg(GAADI_MESSAGE);
             //   profile_object.set_notifyid(regid);
                Spinner bikedropdown = (Spinner) view.findViewById(R.id.spinner1);
                Spinner cardropdown = (Spinner) view.findViewById(R.id.carspinner);
                if (selected_profile.equals("bike")) {
                    //bike_image

                    String rootstring = "http://gaadikey.com/images";
                    String web_image_path =  bikedata.get(bikedropdown.getSelectedItemPosition()).get("bike_image");
                    String path = "";
                    if(web_image_path.length() > rootstring.length() + 10 )  path = web_image_path.substring(rootstring.length());
                    String resize_path = "http://gaadikey.com/images/resize.php?src="+path+"&w=200";
                    Log.e("Resize path is ", resize_path);

                    GAADI_IMAGEPATH = bikedata.get(bikedropdown.getSelectedItemPosition()).get("bike_image");
                    GAADI_NAME = bikedata.get(bikedropdown.getSelectedItemPosition()).get("bike_name");
                    profile_object.set_gaadipic(GAADI_IMAGEPATH);
                    profile_object.set_vehicle_name(GAADI_NAME);
                    profile_object.set_vehicletype("2 wheeler");
                } else if (selected_profile.equals("car")) {
                    //car_image

                    String rootstring = "http://gaadikey.com/images";
                    String web_image_path =  cardata.get(cardropdown.getSelectedItemPosition()).get("car_image");
                    String path = "";
                    if(web_image_path.length() > rootstring.length() + 10 )  path = web_image_path.substring(rootstring.length());
                    String resize_path = "http://gaadikey.com/images/resize.php?src="+path+"&w=200";
                    Log.e("Resize path is ", resize_path);

                    GAADI_IMAGEPATH = cardata.get(cardropdown.getSelectedItemPosition()).get("car_image");
                    GAADI_NAME = cardata.get(cardropdown.getSelectedItemPosition()).get("car_name");
                    profile_object.set_gaadipic(GAADI_IMAGEPATH);
                    profile_object.set_vehicle_name(GAADI_NAME);
                    profile_object.set_vehicletype("4 wheeler");
                }
                new UpdateUserTask().execute("https://gaadikey.in/update"); // Call this and update the data to server!
                Log.e("The complete profile has been clicked", "The completed profile has been clicked ");

                try {
                    Log.e("Click", "Click");
                    // On this click you have to update the profile!


                    // startActivity(sendIntent);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });


        // updateprofilebutton is here! set a listener to this and handle the update task by pinging the appropriate Update URL!
        SharedPreferences sharedPref = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        PHONE_NUMBER = sharedPref.getString(getString(R.string.KEY_phonenumber), "phonenumber");
        //call the get request method with the URL http://gaadikey.com/bikes.php to get the JSON response and to parse for the bike_name and bike_brand and bike_imgurl




        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.e("Bike is Clicked", "Bike");
                        ImageView caricon = (ImageView) view.findViewById(R.id.caricon); // get car icon!
                        TextView cartextview  = (TextView) view.findViewById(R.id.textView2);
                        Spinner carspinner = (Spinner) view.findViewById(R.id.carspinner);
                        cartextview.setVisibility(View.GONE);
                        caricon.setVisibility(View.GONE);
                        carspinner.setVisibility(View.GONE);
                        selected_profile = "bike";
                        new GetBikeDataTask().execute("http://gaadikey.com/bikes.php");
                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.e("Car is Clicked", "Car");
                        ImageView bikeicon = (ImageView) view.findViewById(R.id.bikeicon); // get bike icon!
                        TextView biketextview = (TextView) view.findViewById(R.id.textView);
                        Spinner bikespinner = (Spinner) view.findViewById(R.id.spinner1);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage("With which vehicle would you like to build your Gaadi Key profile?").setPositiveButton("Bike", dialogClickListener)
                .setNegativeButton("Car", dialogClickListener).show();


        // Fragment Search Result layout attached!
        return view;

    }


    public void updateProfile()
    {


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

    public void choiceDialog()
    {

        final AlertDialog levelDialog;


        // Strings to Show In Dialog with Radio Buttons
        final CharSequence[] items = {"Bike","Car","Others"};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Select The Difficulty Level");

        // The dialog has to be shown along with options
        // option1 option2 option3 option3 option4
        levelDialog = builder.create();
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        Log.e("Option 1 Selected", "Bike");
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        Log.e("Option 2 Selected", "Car");
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        Log.e("Option 3 Selected", "Other");
                        break;

                }

                levelDialog.dismiss();

            }
        });


        levelDialog.show();
        Log.e("Level Dialog" , "Level dialog shown");

    }


    private class UpdateUserTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls)
        {

            return UpdateThisUser(urls[0], profile_object);


        }
        protected void onPostExecute(String result)
        {

            Log.e("Success posting", result);
            Log.e("Response from the registration step ", result);
            try
            {
                // Save the following things in sharedStorage
                SharedPreferences sharedPref = getActivity().getSharedPreferences("android_shared",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPref.edit();
                editor2.putString(getString(R.string.KEY_signupstatus), Constants.PROFILE_REGISTERED);
                editor2.commit();

                SharedPreferences sharedPref2 = getActivity().getSharedPreferences("android_shared",  Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_2 = sharedPref2.edit();
                editor_2.putString(getString(R.string.KEY_GaadiName), GAADI_NAME);
                editor_2.commit();

                SharedPreferences sharedPref3 = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_3 = sharedPref3.edit();
                editor_3.putString(getString(R.string.KEY_GaadiMsg), GAADI_MESSAGE);
                editor_3.commit();


                SharedPreferences sharedPref4 = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_4 = sharedPref4.edit();
                editor_4.putString(getString(R.string.KEY_GaadiImage), GAADI_IMAGEPATH);
                editor_4.commit();

                // All details stored in persistant , Should be able to retrieve the values with the respective keys!
                Log.e("Profile Registered State saved ", "Proof "+sharedPref.getString(getString(R.string.KEY_signupstatus), "default signup stuff"));
                Log.e("Persistence" , "Successfully inserted the access_token into the sharedPreferenes storage");
                String theString = sharedPref.getString(getString(R.string.KEY_ACCESS_TOKEN), "the default stuff");
                Log.e("Retrived value",  "The retrieved stuff is "+theString ) ;

                // Go to the home fragment from here! Once the profile has been updated!!!!!!

                ((LaunchActivity_NavDrawer) getActivity()).displayView(0);

                //startActivity(new Intent(MyActivity.this, SettingUpActivity.class));
            }
            catch(Exception e)
            {
                Log.e("Parse", "Exception in parsing");

            }
            Log.e("This should contain the access token ", result);
            Toast.makeText(getActivity().getBaseContext(), "Access token received!", Toast.LENGTH_LONG).show();
            // The data has been sent

            // The control should now go to Enter PIN Screen

            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.
            //
        }
    }


    public String UpdateThisUser(String url, ProfileObject profile_object ) {
        // Create a new HttpClient and Post Header
        InputStream inputStream = null;
        String result = "";
        String clientid = "GaadiKeyClient";
        String clientkey = "gaadi";
        String access_token = ""; // this can be retrieved from sharedpreferences
        SharedPreferences sharedPref =  getActivity().getSharedPreferences("android_shared" , Context.MODE_PRIVATE);
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
           // jsonObject.accumulate("deviceid", profile_object.get_deviceid()); // to set the device id
            jsonObject.accumulate("phonenumber", profile_object.get_phonenumber());
            jsonObject.accumulate("gaadimsg", profile_object.get_gaadimsg());
            jsonObject.accumulate("gaadipic", profile_object.get_gaadipic());
            jsonObject.accumulate("profilepic", profile_object.get_profilepic());
           // jsonObject.accumulate("notifyid", profile_object.get_notifyid()); // to set the notify id!!!!!
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




    private class GetBikeDataTask extends AsyncTask<String, Void, String>
    {

        protected String doInBackground(String... urls)
        {
            Log.e("Pinging this URL ---> ", urls[0]);
            return GetBikeData(urls[0]);

        }
        protected void onPostExecute(String result)
        {

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
            Spinner bikedropdown = (Spinner) view.findViewById(R.id.spinner1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, bikestringdata); // passing the array list instead of array!
            bikedropdown.setAdapter(adapter);

            bikedropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    // Since 0 is returned for the first item... don't do anything .
                    if(position!=0)
                    {
                        Log.e("The Selected index in bike dropdown is ", "" + position);
                        ImageView bikeicon_imageview = (ImageView) view.findViewById(R.id.bikeicon);

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

        protected String doInBackground(String... urls)
        {


            Log.e("Pinging this URL ---> ", urls[0]);
            return GetCarData(urls[0]);


        }
        protected void onPostExecute(String result)
        {

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
                String priority       =     jObject.getString("priority");
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

            Spinner cardropdown = (Spinner) view.findViewById(R.id.carspinner);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, carstringdata);
            cardropdown.setAdapter(adapter1);

            // Car On selected event

            cardropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    Log.e("The Selected index in cardrop down is ", ""+position);

                    if(position!=0)
                    {
                        Log.e("The Selected index in car dropdown is ", "" + position);
                        ImageView caricon_imageview = (ImageView) view.findViewById(R.id.caricon);

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


}