package com.gaadikey.gaadikey.gaadikey.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.activities.LaunchActivity_NavDrawer;
import com.gaadikey.gaadikey.gaadikey.R;
import com.gaadikey.gaadikey.gaadikey.adapters.NewsAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Fragment_News extends Fragment {


    public static ArrayList<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();

    ListView listview;
    ProgressBar pb;
    TextView body;
    String thumbnail;
    String entrytype;
    //Background
    // Fragment_news is here!

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        entrytype = (String) bundle.get("type");
        //entrytype= bundle.getString("type", "direct");

        //    if (view == null) {
        view = inflater.inflate(R.layout.fragment_news, container, false); // fragment_news
        pb = (ProgressBar) view.findViewById(R.id.progress);


        Button writebutton = (Button) view.findViewById(R.id.writebutton); //write button identified
        writebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LaunchActivity_NavDrawer) getActivity()).writeReviewClicked(); // The write review button has been clicked


            }
        });

        //  final View view2 = inflater.inflate(R.layout.activity_launchdrawer, container, false);

        System.out.println("loading listview.........");
        listview = (ListView) view.findViewById(R.id.list);

        if (newsList.size() == 0)
            new NewsDataTask().execute("http://blog.gaadikey.com/wp-json/posts/");
        else
            listview.setAdapter(new NewsAdapter(getActivity(), newsList));

        //  listview.setAdapter(new SourceCode_FragmentAdapter(getActivity(), codeid, codelang, codetitle, codesource, codeoutput));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
                                    long arg3) {

                //  ((YourActivityClassName)getActivity()).yourPublicMethod();
                String title = newsList.get(position).get("title");
                String byline = newsList.get(position).get("date_gmt");
                String body = newsList.get(position).get("content");
                ((LaunchActivity_NavDrawer) getActivity()).listitem_click_notify(title, byline, body); // SENDING THESE PARAMETERS to activity!

            }
        });

        //       }

//        else
//        {
//
//            ((ViewGroup)view.getParent()).removeView(view);
//        }

        return view;

    }


    public class NewsDataTask extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls) {
            return getNewsData(urls[0]);
        }

        protected void onPostExecute(String result) {

            pb.setVisibility(View.GONE);
            newsList = new ArrayList<HashMap<String, String>>();
            try {
                JSONArray json = new JSONArray(result);
                // check if this request was sucessful... if the request was successful
                // then parse the phonebook and get contacts details
                // contacts details are rendered one by one .
                // result
                for (int i = 0; i < json.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jObject = json.getJSONObject(i);
                    String title = jObject.getString("title"); // The title is retrieved from the JSON!
                    String content = jObject.getString("content"); // The content is the king!
                    String date_gmt = jObject.getString("date_gmt"); // This retrieves the article date!
                    String excerpt = jObject.getString("excerpt");
                    thumbnail = "http://gaadikey.com/images/gaadi/1.jpg";

                    Html.fromHtml(content, new Html.ImageGetter() {
                        int imagecount = 0;

                        @Override
                        public Drawable getDrawable(String source) {
                            if (imagecount < 1) {
                                imagecount++;
                                thumbnail = source;
                            }
                            return null;
                        }
                    }, null);

                    SimpleDateFormat formatter, FORMATTER;
                    formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
                    // String oldDate = "2011-03-10T11:54:30.207Z";  //This is modifiedOn
                    Date date = formatter.parse(date_gmt);
                    long mills = date.getTime();
                    CharSequence cs = DateUtils.getRelativeTimeSpanString(mills);


                    //   JSONObject attachment_meta = jObject.getJSONObject("attachment_meta");
                    //  String thumbnail = jObject.getJSONObject("attachment_meta").getJSONObject("sizes").getJSONObject("thumbnail").getString("url");
                    //     modifiedOn = "Joined public lane "+TimeUtils.millisToLongDHMS(24000)+" ago.";
//                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yy:HH:mm:SSS'Z'");
//                    Date date = DATE_FORMAT.parse(modifiedOn);
                    //2014-09-24T05:34:36.228Z
                    //  System.out.println("NewDate-->"+FORMATTER.format(date));


                    map.put("title", title);
                    map.put("content", content);
                    map.put("date_gmt", date_gmt); // date_gmt
                    map.put("posted_on", cs.toString());
                    map.put("thumbnail", thumbnail); // thumbnail
                    newsList.add(map);

                }
                //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobil                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          e, COUNTRIES));

                listview.setAdapter(new NewsAdapter(getActivity(), newsList));
                if (entrytype.equals("push")) {
                    String title = newsList.get(0).get("title");
                    String byline = newsList.get(0).get("date_gmt");
                    String body = newsList.get(0).get("content");
                    ((LaunchActivity_NavDrawer) getActivity()).listitem_click_notify(title, byline, body);

                }

                //Date newdate =  new Date()
            } catch (Exception e) {
                // The exception has been logged.
            }

        }

    }


    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            //   Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //   Log.d(TAG, "onPostExecute drawable " + mDrawable);
            //  Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                body.invalidate(); // Invalidaes
                CharSequence t = body.getText();
                body.setText(t);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                //  CharSequence t = mTv.getText();
                //  mTv.setText(t);
            }
        }
    }


    public String getNewsData(String url) {
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
