package com.gaadikey.gaadikey.gaadikey;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.adaptor.GaadiNumberAdapter;

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
public class Fragment_SearchResults extends Fragment {

    ListView listview;
    ArrayList<HashMap<String, String>> gaadiList= new ArrayList<HashMap<String, String>>();

    public static Fragment_SearchResults newInstance(String searchString) {
        Fragment_SearchResults myFragment = new Fragment_SearchResults();
        Bundle args = new Bundle();
        args.putString("searchString", searchString);
        myFragment.setArguments(args);
        return myFragment;
    }




    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_searchresults, container, false);
        EditText edt = (EditText) view.findViewById(R.id.editText_search);
        edt.setText(getArguments().getString("searchString"));
        // Fragment Search Result layout attached!
        SharedPreferences sharedPref =  getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        String phone = sharedPref.getString(getString(R.string.KEY_phonenumber), "the default stuff");

        if(!getArguments().getString("searchString").equals("")) {
            String search_retrieval_url = "https://gaadikey.in/search?gaadinumber=" + getArguments().getString("searchString"); // concatinating the searched string to the end point..
            new SearchGaadiNoTask().execute(search_retrieval_url);

        }

        listview = (ListView) view.findViewById(R.id.list);



        final EditText edittext = (EditText) view.findViewById(R.id.editText_search);

        edittext.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {



                        if (event != null) {
                            // if shift key is down, then we want to insert the '\n' char in the TextView;
                            // otherwise, the default action is to send the message.

                            if (!event.isShiftPressed())
                            {
                                Log.e(" Enter is pressed! ", "Yes");
                                //return true;
                                // Start the activity here

                            }
                            return false;
                        }

                        Log.e("here we are ", "Yes");

                        // Once the user presses the enter key carry forward the search operation!

                        String search_retrieval_url = "https://gaadikey.in/search?gaadinumber=" + edittext.getText().toString(); // concatinating the searched string to the end point..
                        new SearchGaadiNoTask().execute(search_retrieval_url);



                        return true;
                    }
                });



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


    private class SearchGaadiNoTask extends AsyncTask<String, Void, String>
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

            gaadiList = new ArrayList<HashMap<String, String>>();

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
                    String gaadi_no =           jObject.getString("gaadi_no");
                    String gaadi_name =         jObject.getString("gaadi_name");
                    String gaadi_type    =         jObject.getString("gaadi_type");
                    String gaadi_owner_name   =          jObject.getString("gaadi_owner_name");
                    String gaadi_owner_phonenumber       = jObject.getString("gaadi_owner_phonenumber");
                    String gaadi_image       = jObject.getString("gaadi_image");
                    String gaadi_status       = jObject.getString("gaadi_status");
                    String joined_on       = jObject.getString("joined_on");


                    map.put("gaadi_no", gaadi_no);
                    map.put("gaadi_name", gaadi_name);
                    map.put("gaadi_type", gaadi_type);
                    map.put("gaadi_owner_name", gaadi_owner_name);
                    map.put("gaadi_owner_phonenumber",  gaadi_owner_phonenumber);
                    map.put("gaadi_image",  gaadi_image);
                    map.put("gaadi_status", gaadi_status);
                    map.put("joined_on",  joined_on);
                    gaadiList.add(map);
                }
                //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobil                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          e, COUNTRIES));
                Log.e("The number of items in the list is ", ""+gaadiList.size());


                listview.setAdapter(new GaadiNumberAdapter(getActivity(), gaadiList));
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