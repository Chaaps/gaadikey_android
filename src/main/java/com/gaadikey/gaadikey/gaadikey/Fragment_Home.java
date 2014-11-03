package com.gaadikey.gaadikey.gaadikey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.adaptor.StickyHomeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by madratgames on 31/10/14.
 */
public class Fragment_Home extends Fragment  {

    ArrayList<String> lanes = new ArrayList<String>();
    ListView listview;
    String IMAGE_PATH = "";
    String GAADI_MSG = "";
    String GAADI_NAME = "";

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        TextView gaadiname_field = (TextView) view.findViewById(R.id.GaadiName);
        TextView  gaadimsg_field  = (TextView) view.findViewById(R.id.GaadiMsg);
        TextView  numberplate_textview = (TextView) view.findViewById(R.id.numberplate);

        numberplate_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LaunchActivity_NavDrawer) getActivity()).displayView(5); // This opens up numberplate view!
            }
        });




        // Download the Image dynamically, Pinging the Image URL
        // Get the image path URL by rading the persistant storage

        SharedPreferences sharedPref = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        IMAGE_PATH = sharedPref.getString(getString(R.string.KEY_GaadiImage), "default");
        //  IMAGE_PATH = "http://gaadikey.com/images/GaadiKey_bikes/bajaj/bajaj-discover-100m.jpg"; // hARD CODING THE IMAGE url TO CHECK IF 37KB IMAGE LOADING IS THE ISSUE
        GAADI_MSG = sharedPref.getString(getString(R.string.KEY_GaadiMsg), "Set status");
        GAADI_NAME = sharedPref.getString(getString(R.string.KEY_GaadiName), "Your Vehicle Name here");

        gaadimsg_field.setText(GAADI_MSG);
        gaadiname_field.setText(GAADI_NAME);


        Log.e("Retrieved IMAGE_PATH ", IMAGE_PATH);
        Log.e("Retrieved GAADI_MSG ", GAADI_MSG);
        Log.e("Retrieved GAADI NAME ", GAADI_NAME);


        if(!IMAGE_PATH.equals("default")) {

            Picasso.with(getActivity().getBaseContext()).load(IMAGE_PATH).into(thumbnail);

        }

        TextView numberplateText =  (TextView) view.findViewById(R.id.numberplate);
        SharedPreferences sharedPref1 =  getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        String numberplatestring = sharedPref1.getString(getString(R.string.KEY_GaadiKey_Number_Saved),  "KA50Q7896");
        Log.e("The retreived number plate string is ", numberplatestring);

        numberplateText.setText(numberplatestring); // setting the string obtained from the KEY set by number picker change listener
        Typeface typface=Typeface.createFromAsset(getActivity().getAssets(),"LicensePlate.ttf");
        numberplateText.setTypeface(typface);

        lanes.add("Public Lane");
        lanes.add("Friends Lane");
        lanes.add("Safety Lane");
        lanes.add("Shopping Lane");

        listview = (ListView) view.findViewById(R.id.list);
        listview.setAdapter(new StickyHomeAdapter(getActivity(), lanes));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
                                    long arg3)
            {

                SharedPreferences sharedPref4 = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_4 = sharedPref4.edit();
                editor_4.putString(getString(R.string.KEY_HomeMenu), ""+position ); // Incrementing the position by 1
                editor_4.commit();

                Log.e(" The Item clicked in the list is " , ""+position);
                ((LaunchActivity_NavDrawer) getActivity()).displayView(position + 1);

            }
            });


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

                        Intent i = new Intent(getActivity(), LaunchActivity_NavDrawer.class);
                        i.putExtra("searchString",edittext.getText().toString());

                        SharedPreferences sharedPref4 = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor_4 = sharedPref4.edit();
                        editor_4.putString(getString(R.string.KEY_HomeMenu), "0" ); // Incrementing the position by 1
                        editor_4.commit();
                        startActivity(i);

                        return true;
                    }
                });



        return view;
    }

}
