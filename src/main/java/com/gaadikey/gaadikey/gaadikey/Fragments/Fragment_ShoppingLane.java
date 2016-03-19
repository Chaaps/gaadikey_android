package com.gaadikey.gaadikey.gaadikey.fragments;

import android.support.v4.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gaadikey.gaadikey.gaadikey.R;
import com.gaadikey.gaadikey.gaadikey.adapters.ShoppingAdapter;

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
import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_ShoppingLane extends Fragment {

    ListView listview;
    ProgressBar pb;
    ArrayList<HashMap<String, String>> shoppingList =   new ArrayList<HashMap<String, String>>();

    //
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shoppinglane, container, false);
        pb = (ProgressBar) view.findViewById(R.id.progress); // identifying progress indicator!

        System.out.println("loading listview.........");


        new RetriveShoppingProducts_GetTask().execute("https://gaadikey.in/affiliate_ads?os=android");


        listview = (ListView) view.findViewById(R.id.list);

        return view;
    }


    private class RetriveShoppingProducts_GetTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return  getData(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            pb.setVisibility(View.GONE);
            shoppingList = new ArrayList<HashMap<String, String>>();
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
                    String Source =        jObject.getString("ad_source");
                    String Title =         jObject.getString("ad_title");
                    String Image    =      jObject.getString("ad_image");
                    String Price   =       jObject.getString("ad_price");
                    String URL       =     jObject.getString("ad_clickurl");
                    String Notes     =     jObject.getString("ad_notes");
                    map.put("Source", Source);
                    map.put("Title", Title);
                    map.put("Image", Image);
                    map.put("Price", Price);
                    map.put("URL",   URL);
                    map.put("Notes", Notes);
                    shoppingList.add(map);
                }
                //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobil                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          e, COUNTRIES));
                listview.setAdapter(new ShoppingAdapter(getActivity(), shoppingList));
                //listview

            }

            catch (Exception e)
            {
                // The exception has been logged.
            }

        }
    }



    public  String getData(String url){
        InputStream inputStream = null;
        String result = "";
        SharedPreferences sharedPref =   getActivity().getSharedPreferences("android_shared" , Context.MODE_PRIVATE);
        //
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
            httpGet.addHeader("Accept-version", getString(R.string.API_VERSION)); // Added API_VERSION in the header!

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

    protected void on7ListItemClick(ListView l, View v, int position, long id)
    {

        String advertisement_url = "";
        advertisement_url = shoppingList.get(position).get("URL");
        // onClick should open  a URL IN THE BROWSER
        Intent internetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(advertisement_url));
        internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
        internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(internetIntent);

        Toast.makeText(getActivity(), "The shopping item selected in the Shopping list is in the position " + position, Toast.LENGTH_LONG).show();
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
}




