package com.gaadikey.gaadikey.gaadikey.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.activities.LaunchActivity_NavDrawer;
import com.gaadikey.gaadikey.gaadikey.R;
import com.gaadikey.gaadikey.gaadikey.adapters.StickyHomeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Fragment_Home extends Fragment {
    ArrayList<String> lanes = new ArrayList<String>();
    ListView listview;
    String IMAGE_PATH = "";
    String GAADI_MSG = "";
    String GAADI_NAME = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        TextView gaadiname_field = (TextView) view.findViewById(R.id.GaadiName);
        TextView gaadimsg_field = (TextView) view.findViewById(R.id.GaadiMsg);
        TextView numberplate_textview = (TextView) view.findViewById(R.id.numberplate);

        numberplate_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LaunchActivity_NavDrawer) getActivity()).displayView(6); // This opens up numberplate view!
            }
        });

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Open up the settings view when clicked from here!
                ((LaunchActivity_NavDrawer) getActivity()).displayView(8); // The 7th option is settings!
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


        if (!IMAGE_PATH.equals("default")) {

            Picasso.with(getActivity().getBaseContext()).load(IMAGE_PATH).into(thumbnail);

        }

        TextView numberplateText = (TextView) view.findViewById(R.id.numberplate);
        SharedPreferences sharedPref1 = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        String numberplatestring = sharedPref1.getString(getString(R.string.KEY_GaadiKey_Number_Saved), "KA50Q7896");

        numberplateText.setText(numberplatestring); // setting the string obtained from the KEY set by number picker change listener
        Typeface typface = Typeface.createFromAsset(getActivity().getAssets(), "LicensePlate.ttf");
        numberplateText.setTypeface(typface);

        lanes.add("Public Lane");
        lanes.add("Friends Lane");
        lanes.add("Safety Lane");
        lanes.add("Shopping Lane");
        lanes.add("News");

        listview = (ListView) view.findViewById(R.id.list);
        listview.setAdapter(new StickyHomeAdapter(getActivity(), lanes));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
                                    long arg3) {

                SharedPreferences sharedPref4 = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_4 = sharedPref4.edit();
                editor_4.putString(getString(R.string.KEY_HomeMenu), "" + position); // Incrementing the position by 1
                editor_4.apply();

                ((LaunchActivity_NavDrawer) getActivity()).displayView(position + 1);

            }
        });
        return view;
    }

}
