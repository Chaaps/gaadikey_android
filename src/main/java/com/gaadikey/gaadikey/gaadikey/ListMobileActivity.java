package com.gaadikey.gaadikey.gaadikey;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.gaadikey.gaadikey.gaadikey.adaptor.MobileArrayAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ListMobileActivity extends ListActivity {

	static final String[] MOBILE_OS = new String[] { "Android", "iOS",
			"WindowsMobile", "Blackberry"};

    ViewNotifyObject vnObj;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobile,
		//		R.id.label, MOBILE_OS));
		setListAdapter(new MobileArrayAdapter(this, MOBILE_OS));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//get selected items
		String selectedValue = (String) getListAdapter().getItem(position);
		Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

        vnObj = new ViewNotifyObject();
        vnObj.set_phonenumber("1234567890");
        vnObj.set_gkey("thegkey");
        vnObj.set_sendto("270820141113");
        vnObj.set_vehicle("Vehicle__"+selectedValue);
        vnObj.set_name(selectedValue);

        new HttpAsyncPostTask().execute("http://gaadikey.in/viewnotify");


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


    public String postData(String url,ViewNotifyObject vn) {
        // Create a new HttpClient and Post Header
        InputStream inputStream = null;
        String result = "";
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
            Log.e("Json uploaded", "The Uploaded json looks like "+json);
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
                Log.e("crash", "We are here and somehow crashing!");
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


    private class HttpAsyncPostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return postData(urls[0], vnObj);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {

            Log.e("Success posting", result);
            new AlertDialog.Builder(ListMobileActivity.this)
                    .setTitle("Success Posting")
                    .setMessage("Success Posting to URL")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }





}