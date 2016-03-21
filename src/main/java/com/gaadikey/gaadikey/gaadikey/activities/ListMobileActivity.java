package com.gaadikey.gaadikey.gaadikey.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.gaadikey.gaadikey.gaadikey.R;
import com.gaadikey.gaadikey.gaadikey.serializables.ViewNotifyObject;
import com.gaadikey.gaadikey.gaadikey.adapters.MobileArrayAdapter;

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

public class ListMobileActivity extends ListActivity {

	static final String[] MOBILE_OS = new String[] { "Android", "iOS",
			"WindowsMobile", "Blackberry"};

    ArrayList<String> mylist = new ArrayList<String>();
    public static ArrayList<HashMap<String, String>> contactsList = new ArrayList<HashMap<String, String>>();
  //  mylist.add(mystring); //this adds an element to the list.

    ViewNotifyObject vnObj;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobile,
		//		R.id.label, MOBILE_OS));

        SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
        String phone = sharedPref.getString(getString(R.string.KEY_phonenumber), "the default stuff");

        String thecontacts_retrieval_url = "http://gaadikey.in/dummycontacts?phonenumber="+phone;
        new RetrivePhoneBook_GetTask().execute(thecontacts_retrieval_url);
       // setListAdapter(new MobileArrayAdapter(this, MOBILE_OS));


        // Call The  /dummyContacts API from here..  so that you can dispaly the JSON in a phonebook format

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
    {

        Toast.makeText(this,"You clicked bike "+position, Toast.LENGTH_LONG ).show();

        // Show a toast notification





//        Toast.makeText(this,position, Toast.LENGTH_LONG ).show();
//		//get selected items
//	//	String selectedValue = (String) getListAdapter().getItem(position);
//	//	Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
//
//        vnObj = new ViewNotifyObject();
//        vnObj.set_phonenumber("1234567890");
//        vnObj.set_gkey("thegkey");
//        vnObj.set_sendto("270820141113");
//        vnObj.set_vehicle("Vehicle__" + "selectedValue");
//        vnObj.set_name("selectedValue");
//
//        new HttpAsyncPostTask().execute("http://gaadikey.in/viewnotify");

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


    // ---- This Module is to  notify the other users in the network that their profile has been viewed ..


    public String postData(String url,ViewNotifyObject vn) {
        // Create a new HttpClient and Post Header
        InputStream inputStream = null;
        String result = "";
        //sub
        try
        {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("phonenumber", vn.get_phonenumber());
            jsonObject.accumulate("gkey",  vn.get_gkey() );
            jsonObject.accumulate("name",  vn.get_name());
            jsonObject.accumulate("vehicle", vn.get_vehicle());
            jsonObject.accumulate("sendto", vn.get_sendto());
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
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

            }
            else
                result = "Did not work!";

        } catch (Exception e) {

            // TODO Auto-generated catch block
        }

        return result;
    }


    private class HttpAsyncPostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return postData(urls[0], vnObj);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {

            new AlertDialog.Builder(ListMobileActivity.this)
                    .setTitle("Success Posting")
                    .setMessage("Success Posting to URL")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();

            // The Contacts retrieved is  []
        }
    }


    // ---- This module  ends here ----


    // Now start a new module which sends a Get Request  to retrieve the phone numbers ...

    public  String getData(String url){
        InputStream inputStream = null;
        String result = "";

        SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
        // sharedPref
        // retrieve this
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

    private class RetrivePhoneBook_GetTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            return  getData(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {

            try
            {

                JSONArray json = new JSONArray(result);

                // check if this request was sucessful... if the request was successful

                // then parse the phonebook and get contacts details

                // contacts details are rendered one by one .

                // result

                for(int i=0;i<json.length();i++)
                {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jObject = json.getJSONObject(i);

                    String Name =           jObject.getString("Name");
                    String ImgUrl =         jObject.getString("ImgUrl");
                    String Desc    =         jObject.getString("Desc");
                    String gkey   =          jObject.getString("gkey");
                    String phonenumber1       = jObject.getString("phonenumber1");
                    String phonenumber2       = jObject.getString("phonenumber2");
                    String phonenumber3       = jObject.getString("phonenumber3");
                    String phonenumber4       = jObject.getString("phonenumber4");


                    map.put("Name", Name);
                    map.put("ImgUrl", ImgUrl);
                    map.put("Desc", Desc);
                    map.put("gkey", gkey);
                    map.put("phonenumber1",  phonenumber1);
                    map.put("phonenumber2",  phonenumber2);
                    map.put("phonenumber3",  phonenumber3);
                    map.put("phonenumber4",  phonenumber4);
                    contactsList.add(map);

                }

                //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobile, COUNTRIES));

                setListAdapter(new MobileArrayAdapter(ListMobileActivity.this, contactsList));

                 // Temporarily commenting out.. Should so something  once we get hold of the jSonArray ( the 1st item )

//                JSONObject jObject = new JSONObject(result);
//                String Name = jObject.getString("Name");
//                String ImgUrl = jObject.getString("ImgUrl");
//                String Des    = jObject.getString("Des");
//                String gkey   = jObject.getString("gkey");
//                String phonenumber1       = jObject.getString("phonenumber1");
//                String phonenumber2       = jObject.getString("phonenumber2");
//                String phonenumber3       = jObject.getString("phonenumber3");
//                String phonenumber4       = jObject.getString("phonenumber4");



                // startActivity
            }

            catch (Exception e)
            {
                // The exception has been logged.
            }

        }
    }











}