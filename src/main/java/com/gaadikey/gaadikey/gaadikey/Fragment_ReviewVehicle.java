package com.gaadikey.gaadikey.gaadikey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
public class Fragment_ReviewVehicle extends Fragment {


    String title;
    String body;
    String category;
    EditText title_et; //edit text
    EditText body_et; // edit text
    EditText category_et; // edit text
    Spinner generalSpinner; // General Spinner!
    ProgressBar progress;


    ArrayList<HashMap<String, String>> bikedata; // holds all bike data which has to be added to general Spinner, ;likewise for others!
    ArrayList<String> bikestringdata;

    ArrayList<HashMap<String, String>> cardata;
    ArrayList<String> carstringdata;

    ArrayList<HashMap<String, String>> cabdata;
    ArrayList<String> cabstringdata;

    ArrayList<HashMap<String, String>> busdata;
    ArrayList<String> busstringdata;

    ArrayList<HashMap<String, String>> rickshawdata;
    ArrayList<String> rickshawstringdata;


    View view;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reviewvehicle, container, false);

        generalSpinner = (Spinner) view.findViewById(R.id.generalspinner); // general spinner is identified.
        title_et = (EditText) view.findViewById(R.id.thetitle) ;
        //title_et = (EditText) view.findViewById(R.id.thetitle);
        body_et = (EditText) view.findViewById(R.id.body);
        progress = (ProgressBar) view.findViewById(R.id.progress); // Progressbar is identified!

        RadioGroup group=(RadioGroup) view.findViewById(R.id.radioGroup1);
        final RadioButton bikeradio = (RadioButton) view.findViewById(R.id.bikeradio);
        final RadioButton carradio  = (RadioButton) view.findViewById(R.id.carradio);
        final RadioButton cabradio  = (RadioButton) view.findViewById(R.id.cabradio);
        final RadioButton busradio  = (RadioButton) view.findViewById(R.id.busradio);
        final RadioButton rickshawradio = (RadioButton) view.findViewById(R.id.rickshawradio); // rickshaw radio

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // TODO Auto-generated method stub
                Log.e("The checked ID is ", ""+checkedId); // This retrieves the id of radio button which is checked!

                // Populate the spinners as and when radio button is checked!

                if(bikeradio.isChecked())
                {
                    category = "Bikes";
                    new GetGeneralDataTask().execute("http://gaadikey.com/bikes.php");
                }
                else if(carradio.isChecked())
                {
                    category = "Cars";
                    new GetGeneralDataTask().execute("http://gaadikey.com/cars.php");
                }
                else if(cabradio.isChecked())
                {
                    category = "Cabs";
                    new GetGeneralDataTask().execute("http://gaadikey.com/cabs.php");

                }
                else if(busradio.isChecked())
                {
                    category = "Buses";
                    new GetGeneralDataTask().execute("http://gaadikey.com/buses.php");

                }
                else if(rickshawradio.isChecked())
                {
                    category = "Rickshaws";
                    new GetGeneralDataTask().execute("http://gaadikey.com/rickshaws.php"); // rickshaws json is returned

                }

//                if(radiobutton1.isChecked())
//                {
//
//                    fall.setVisibility(View.GONE);
//                    trip.setVisibility(View.GONE);
//                    illness.setVisibility(View.GONE);
//                }
//                else if(radiobutton2.isChecked())
//                {
//
//
//                }

            }
        });

        Button updatebuton = (Button) view.findViewById(R.id.articlesubmit);
        updatebuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("The click has occured", "");
                new ReviewSubmitTask().execute("http://gaadikey.in/submitarticle"); // Endpoint used to submit the article
            }

        });

        title_et.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {


                title_et.requestLayout();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                return false;
            }
        });

        body_et.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                Log.e("We are in the body trigger", "Yes");


                body_et.requestLayout();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                return false;
            }
        });

        category = "Bikes";
        new GetGeneralDataTask().execute("http://gaadikey.com/bikes.php");
        // Fragment Search Result layout attached!
        return view;
    }



    private class GetGeneralDataTask extends AsyncTask<String, Void, String>
    {

        String cat;


        protected void onPreExecute()
        {

            // Loading bar can be added
           progress.setVisibility(View.VISIBLE); // Showing the progress bar when the elements are loading.
        }

        protected String doInBackground(String... urls)
        {


            Log.e("Pinging this URL ---> ", urls[0]);

            return GetData(urls[0]); // the 2nd parameter category is passed to a general function which then pings the appropriate category URL to get the data!



        }
        protected void onPostExecute(String result)
        {

            progress.setVisibility(View.GONE);
         //   pb.setVisibility(View.GONE); // hiding the loading bar
            Log.e("And we received", result);
            Log.e("Response from the registration step ", result);
            try
            {
                // The populate spinner should be present here!
                if(category.equals("Bikes"))
                PopulateBikeSpinner(result);
                else if(category.equals("Cars"))
                PopulateCarSpinner(result);
                else if(category.equals("Cabs"))
                PopulateCabSpinner(result);
                else if(category.equals("Buses"))
                PopulateBusSpinner(result);
                else if(category.equals("Rickshaws"))
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

    public  String GetData(String url){
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


    private class ReviewSubmitTask extends AsyncTask<String, Void, String>
    {

        protected void onPreExecute()
        {

            // Loading bar can be added
            progress.setVisibility(View.VISIBLE); // Showing the progress bar when the elements are loading.
        }

        protected String doInBackground(String... urls)
        {
            return SubmitReview(urls[0]);
        }

        protected void onPostExecute(String result)
        {

            progress.setVisibility(View.GONE);
            Log.e("Success posting", result);
            Log.e("Response from the registration step ", result);
            try
            {
                // Save the following  things in sharedStorage

                Log.e("Response is received" , "Parse for success msg ");
            }
            catch(Exception e)
            {
                Log.e("Parse", "Exception in parsing");

            }
            Log.e("This should contain the access token ", result);

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());
            dlgAlert.setCancelable(false);
            dlgAlert.setMessage("Your review has been submitted. Rs 25 will be credited to your mobile when the review is approved.");

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            ((LaunchActivity_NavDrawer) getActivity()).displayView(5);

                            //dismiss the dialog
                        }
                    });

            dlgAlert.show();
            Toast.makeText(getActivity().getBaseContext(), "Review submission successful!", Toast.LENGTH_LONG).show();
            // The data has been sent
            // The control should now go to Enter PIN Screen
            // Once the  Phone number is recieved by the server, The flow has to go to EnterPINActivity.
            //
        }
    }

    public String SubmitReview(String url) {
        // Create a new HttpClient and Post Header
        InputStream inputStream = null;
        String result = "";
        String clientid = "GaadiKeyClient";
        String clientkey = "gaadi";
        String access_token = ""; // this can be retrieved from sharedpreferences
        SharedPreferences sharedPref =  getActivity().getSharedPreferences("android_shared" ,  Context.MODE_PRIVATE);
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
            title_et = (EditText) view.findViewById(R.id.thetitle);
            body_et = (EditText) view.findViewById(R.id.body);

            title = title_et.getText().toString();
            body  = body_et.getText().toString(); // body extracted


            jsonObject.accumulate("title", title );
            jsonObject.accumulate("content", body ); // the body has been attached to the content parameter of JSON
            jsonObject.accumulate("category", category);  // Submits this category.
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
            Spinner bikedropdown = (Spinner) view.findViewById(R.id.generalspinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, bikestringdata); // passing the array list instead of array!
            bikedropdown.setAdapter(adapter);
            bikedropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    Log.e("Item click happened for bikes list! ", "Bikes");
                    // Since 0 is returned for the first item... don't do anything .
                    //   if(position!=0)

                    title_et.setText("Review - "+bikestringdata.get(position)); // Bike String data is chosen!
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

            Spinner cardropdown = (Spinner) view.findViewById(R.id.generalspinner); // The general spinner has been identified here
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, carstringdata);
            cardropdown.setAdapter(adapter1);

            // Car On selected event
            cardropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    title_et.setText("Review - "+carstringdata.get(position));
                    Log.e("Car string selection is ", ""+position);


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

            Spinner cabdropdown = (Spinner) view.findViewById(R.id.generalspinner);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cabstringdata);
            cabdropdown.setAdapter(adapter1);

            // Car On selected event

            cabdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    Log.e("The Selected index in cardrop down is ", ""+position);



                        Log.e("The Selected index in cardrop down is ", ""+position);
                        Log.e("Cab string data is ",cabstringdata.get(position));
                        title_et.setText("Review - "+cabstringdata.get(position));

                        //  new ImageDownloader(caricon_imageview).execute(cardata.get(position).get("car_image"));


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

            Spinner busdropdown = (Spinner) view.findViewById(R.id.generalspinner);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, busstringdata);
            busdropdown.setAdapter(adapter1);

            // Car On selected event

            busdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    Log.e("The Selected index in cardrop down is ", ""+position);
                    title_et.setText("Review - "+busstringdata.get(position));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }

        catch(Exception e)
        {

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

            Spinner rickshawdropdown = (Spinner) view.findViewById(R.id.generalspinner);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, rickshawstringdata);
            rickshawdropdown.setAdapter(adapter1);

            // Car On selected event

            rickshawdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    Log.e("The Selected index in cardrop down is ", ""+position);
                    title_et.setText("Review - "+rickshawstringdata.get(position)); //title_et text has been set!

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