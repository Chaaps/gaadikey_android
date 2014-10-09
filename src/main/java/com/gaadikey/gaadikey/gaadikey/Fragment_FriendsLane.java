package com.gaadikey.gaadikey.gaadikey;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gaadikey.gaadikey.gaadikey.adaptor.FriendsAdapter;

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

/**
 * Created by madratgames on 08/10/14.
 */
public class Fragment_FriendsLane extends Fragment{

    public static ArrayList<HashMap<String, String>> contactsList = new ArrayList<HashMap<String, String>>();

    ListView listview;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friendslane, container, false);
        System.out.println("loading listview.........");
        SharedPreferences sharedPref =  getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        String phone = sharedPref.getString(getString(R.string.KEY_phonenumber), "the default stuff");

        String thecontacts_retrieval_url = "https://gaadikey.in/dummycontacts?phonenumber="+phone;
        new RetrivePhoneBook_GetTask().execute(thecontacts_retrieval_url);

        listview = (ListView) view.findViewById(R.id.list);

        //  listview.setAdapter(new SourceCode_FragmentAdapter(getActivity(), codeid, codelang, codetitle, codesource, codeoutput));

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
//                                    long arg3) {
//
//
//                Toast.makeText(getActivity(), "The contact item selected in the friends list is in the position " + position, Toast.LENGTH_LONG).show();
//
//
//            }
//        });

        return view;

    }


    private class RetrivePhoneBook_GetTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            Log.e("GET called ", " The url is " + urls[0]);
            return  getData(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {

            contactsList = new ArrayList<HashMap<String, String>>();

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
                String Name =           jObject.getString("Name");
                String ImgUrl =         jObject.getString("ImgUrl");
                String Desc    =         jObject.getString("Desc");
                String gkey   =          jObject.getString("gkey");
                String phonenumber1       = jObject.getString("phonenumber1");
                String phonenumber2       = jObject.getString("phonenumber2");
                String phonenumber3       = jObject.getString("phonenumber3");
                String phonenumber4       = jObject.getString("phonenumber4");
                Log.e("Values ", "Name is "+Name);
                Log.e("Values ", "Phonenumber is "+phonenumber1);
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
            //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobil                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          e, COUNTRIES));
            Log.e("The number of items in the list is ", ""+contactsList.size());
            listview.setAdapter(new FriendsAdapter(getActivity(), contactsList));
        }

            catch (Exception e)
            {
                Log.e("Exception", "The Exception has occured "+e.getMessage());
                // The exception has been logged.
            }

        }
    }

    public  String getData(String url){
        InputStream inputStream = null;
        String result = "";
        SharedPreferences sharedPref =  getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
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
