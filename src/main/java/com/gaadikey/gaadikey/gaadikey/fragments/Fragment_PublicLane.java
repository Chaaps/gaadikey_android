package com.gaadikey.gaadikey.gaadikey.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.gaadikey.gaadikey.gaadikey.R;
import com.gaadikey.gaadikey.gaadikey.adapters.PublicLaneAdapter;

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
import java.util.Date;
import java.util.HashMap;

public class Fragment_PublicLane extends Fragment {


    public static ArrayList<HashMap<String, String>> publicList = new ArrayList<HashMap<String, String>>();


    ListView listview;
    ProgressBar pb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_publiclane, container, false);
        pb = (ProgressBar) view.findViewById(R.id.progress);

        System.out.println("loading listview.........");


        new PublicDataTask().execute("https://gaadikey.in/publiclane");


        listview = (ListView) view.findViewById(R.id.list);


        //  listview.setAdapter(new SourceCode_FragmentAdapter(getActivity(), codeid, codelang, codetitle, codesource, codeoutput));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
                                    long arg3) {


            }
        });


        return view;


    }


    public class PublicDataTask extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls) {
            return getPublicData(urls[0]);
        }

        protected void onPostExecute(String result) {
            pb.setVisibility(View.GONE);
            publicList = new ArrayList<HashMap<String, String>>();
            try {
                JSONArray json = new JSONArray(result);
                // check if this request was sucessful... if the request was successful
                // then parse the phonebook and get contacts details
                // contacts details are rendered one by one .
                // result
                for (int i = 0; i < json.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jObject = json.getJSONObject(i);
                    String vehiclename = jObject.getString("vehiclename");
                    String gaadipic = jObject.getString("gaadipic");
                    String rootstring = "http://gaadikey.com/images";
                    String web_image_path = gaadipic;
                    String path = "";
                    if (web_image_path.length() > rootstring.length() + 10)
                        path = web_image_path.substring(rootstring.length());
                    String resize_path = "http://gaadikey.com/images/resize.php?src=" + path + "&w=200";

                    String modifiedOn = jObject.getString("modifiedOn");
                    //     modifiedOn = "Joined public lane "+TimeUtils.millisToLongDHMS(24000)+" ago.";
//                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yy:HH:mm:SSS'Z'");
//                    Date date = DATE_FORMAT.parse(modifiedOn);
                    //2014-09-24T05:34:36.228Z
                    SimpleDateFormat formatter, FORMATTER;
                    formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    // String oldDate = "2011-03-10T11:54:30.207Z";  //This is modifiedOn
                    Date date = formatter.parse(modifiedOn.substring(0, 24));
                    FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");

                    Date newdate = FORMATTER.parse(FORMATTER.format(date));
                    //  System.out.println("NewDate-->"+FORMATTER.format(date));

                    long mills = newdate.getTime();
                    CharSequence cs = DateUtils.getRelativeTimeSpanString(mills);
                    if (!vehiclename.equals("null")) {
                        map.put("vehiclename", vehiclename);
                        map.put("gaadipic", resize_path);
                        map.put("modifiedOn", "Joined public lane " + cs.toString() + " ");
                        publicList.add(map);
                    }
                }
                //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobil                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          e, COUNTRIES));

                listview.setAdapter(new PublicLaneAdapter(getActivity(), publicList));
            } catch (Exception e) {
                // The exception has been logged.
            }

        }

    }


    public String getPublicData(String url) {
        InputStream inputStream = null;
        String result = "";
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
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
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;

    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


}
