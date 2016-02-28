package com.gaadikey.gaadikey.gaadikey.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.activities.LaunchActivity_NavDrawer;
import com.gaadikey.gaadikey.gaadikey.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Created by madratgames on 08/10/14.
 */
public class Fragment_NumberPlate extends Fragment{

    ListView listview;
    NumberPicker picker1 = null;
    NumberPicker picker2 = null;

    String picker1filler = "KA";
    String picker2filler = "50";
    String picker3filler = "-";
    String picker4filler = "M";

    String digit1filler = "0";
    String digit2filler = "1";
    String digit3filler = "2";
    String digit4filler = "3";


    String formedString = "";
    String displayString = "";
    String formedStringPart2 = "";

    String[] stateArray = null;
    String[] regNoArray = null;
    String[] placeArray = null;
    String[] regNoTruncatedArray = null;

   // int theindex = 0;
   // int theindex2 = 0;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {


        SharedPreferences sharedPref =  getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
        picker1filler = sharedPref.getString(getString(R.string.KEY_picker1_laststate),  "KA");
        picker2filler = sharedPref.getString(getString(R.string.KEY_picker2_laststate),  "01");
        picker3filler = sharedPref.getString(getString(R.string.KEY_picker3_laststate),  "-");
        picker4filler = sharedPref.getString(getString(R.string.KEY_picker4_laststate),  "M");

        digit1filler = sharedPref.getString(getString(R.string.KEY_digit1_laststate),  "0");
        digit2filler = sharedPref.getString(getString(R.string.KEY_digit2_laststate),  "2");
        digit3filler = sharedPref.getString(getString(R.string.KEY_digit3_laststate),  "9");
        digit4filler = sharedPref.getString(getString(R.string.KEY_digit4_laststate),  "1");


        String[] picker1Array = new String[] { "AN", "AP", "AR", "AS", "BR","CG" ,"CH","DD", "DL", "DN", "GA", "GJ", "HP", "HR", "JH","JK", "KA", "KL", "LD", "MH", "ML", "MN", "MP", "MZ", "NL", "OD", "PB", "PY", "RJ", "SK", "TN" , "TR", "TS", "UK", "UP", "WB" };
        String[] picker2Array = new String[] {  "01", "02", "03", "04", "05","06" ,"07","08", "09", "10", "11", "12", "13", "14", "15","16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" , "32", "33", "34", "35", "36", "37","38", "39", "40", "41", "42", "43", "44", "45","46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61" , "62", "63", "64", "65", "66", "67","68", "69", "70" , "71", "72", "73", "74", "75","76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91" , "92", "93", "94", "95", "96", "97","98", "99" } ;

        String[] picker3Array = new String[] { "-","A", "B", "C", "D", "E","F" ,"G","H", "I", "J", "K", "L", "M", "N", "O","P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
        String[] picker4Array = new String[] { "-", "A", "B", "C", "D", "E","F" ,"G","H", "I", "J", "K", "L", "M", "N", "O","P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        String[] digit1Array = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-" };
        String[] digit2Array = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-" };
        String[] digit3Array = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-" };
        String[] digit4Array = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-" };



        final View view = inflater.inflate(R.layout.fragment_numberplate, container, false);
        String RTOData = "";

        RTOData = loadJSONFromAsset();



        try
        {
            JSONArray json = new JSONArray(RTOData);
            // The RTO Data has to be parsed for following values RegNo, Place, State

            stateArray = new String[RTOData.length()];
            regNoArray = new String[RTOData.length()];
            placeArray = new String[RTOData.length()];
            regNoTruncatedArray = new String[RTOData.length()];


            for(int i=0;i<json.length();i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jObject = json.getJSONObject(i);
                stateArray[i] =  jObject.getString("State");
                regNoArray[i] =  jObject.getString("RegNo");
                Log.e("", jObject.getString("RegNo"));
                regNoTruncatedArray[i] = regNoArray[i].substring(0,2);
                placeArray[i] =  jObject.getString("Place");

            }

            String unchangedString =  picker1filler + picker2filler;
            int theindex = Arrays.asList(regNoArray).indexOf(unchangedString);
            displayString = stateArray[theindex] + "    "+placeArray[theindex];
            TextView statetext = (TextView) view.findViewById(R.id.statename);
            statetext.setText(displayString);

            //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_mobil                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          e, COUNTRIES));


        }

        catch(Exception e)
        {


        }


        picker1 =  (NumberPicker) view.findViewById(R.id.picker1);
        picker1.setMinValue(0);
        picker1.setMaxValue(35);

        picker1.setDisplayedValues(picker1Array);
        picker1.setValue(Arrays.asList(picker1Array).indexOf(picker1filler));
        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {

                String state = numberPicker.getDisplayedValues()[numberPicker.getValue()];
                TextView statetext = (TextView) view.findViewById(R.id.statename);
                formedString = "";
                formedString +=  state;

//                if(state.equals("AN"))
//                {
//                    statetext.setText("Andaman & Nicobar");
//                }
//
//                if(state.equals("AP"))
//                {
//                    statetext.setText("Andra Pradesh");
//                }
//
//                if(state.equals("DL"))
//                {
//                    statetext.setText("Delhi");
//                }
//                if(state.equals("KL"))
//                {
//                    statetext.setText("Kerala");
//                }
//                if(state.equals("GJ"))
//                {
//                    statetext.setText("Gujarat");
//                }
//                if(state.equals("HP"))
//                {
//                    statetext.setText("Himachal Pradesh");
//                }
//                if(state.equals("UP"))
//                {
//                    statetext.setText("Uttar Pradesh");
//                }
//                if(state.equals("MP"))
//                {
//                    statetext.setText("Madya Pradesh");
//                }
//                if(state.equals("KA"))
//                {
//                    statetext.setText("Karnataka");
//                }
//                if(state.equals("TN"))
//                {
//                    statetext.setText("Tamilnadu");
//                }
                Log.e("State is ", state);

                formedString += picker2.getDisplayedValues()[picker2.getValue()];
                if (Arrays.asList(regNoArray).contains(formedString))
                {
                    // true
                    int theindex = Arrays.asList(regNoArray).indexOf(formedString);
                    displayString = stateArray[theindex] + "    "+placeArray[theindex];
                    statetext.setText(displayString);
                }

                else
                {
                    displayString = "";
                    statetext.setText(displayString);
                }

            }



        });

        formedString +=  picker1.getDisplayedValues()[picker1.getValue()];

        picker2 =  (NumberPicker) view.findViewById(R.id.picker2);
        picker2.setMinValue(0);
        picker2.setMaxValue(98);
        picker2.setDisplayedValues(picker2Array);
        picker2.setValue(Arrays.asList(picker2Array).indexOf(picker2filler));

        picker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                              @Override
                                              public void onValueChange(NumberPicker numberPicker, int i, int i2) {

                                                  TextView statetext = (TextView) view.findViewById(R.id.statename);
                                                  formedString = "";
                                                  formedString += picker1.getDisplayedValues()[picker1.getValue()];
                                                  formedString += picker2.getDisplayedValues()[picker2.getValue()];
                                                 // int index = "";
                                                  if (Arrays.asList(regNoArray).contains(formedString))
                                                  {
                                                      // true
                                                      int theindex = Arrays.asList(regNoArray).indexOf(formedString);
                                                      displayString = stateArray[theindex] + "    "+placeArray[theindex];
                                                      statetext.setText(displayString);

                                                  }
                                                  else
                                                  {
                                                      displayString = "";
                                                      statetext.setText(displayString);
                                                  }




                                                  //  formedString += picker2.getDisplayedValues();

                                                  //numberPicker.

                                              }
                                          }
        );

        formedString += picker2.getDisplayedValues()[picker2.getValue()];






        final NumberPicker picker3 =  (NumberPicker) view.findViewById(R.id.picker3);
        picker3.setMinValue(0);
        picker3.setMaxValue(26);
        picker3.setDisplayedValues(picker3Array);
        picker3.setValue(Arrays.asList(picker3Array).indexOf(picker3filler));

        final NumberPicker picker4 =  (NumberPicker) view.findViewById(R.id.picker4);
        picker4.setMinValue(0);
        picker4.setMaxValue(26);
        picker4.setDisplayedValues(picker4Array);
        picker4.setValue(Arrays.asList(picker4Array).indexOf(picker4filler));

        final NumberPicker digit1 =  (NumberPicker) view.findViewById(R.id.digit1);
        digit1.setMinValue(0);
        digit1.setMaxValue(10);
        digit1.setDisplayedValues(digit1Array);
        digit1.setValue(Arrays.asList(digit1Array).indexOf(digit1filler));

        final NumberPicker digit2 =  (NumberPicker) view.findViewById(R.id.digit2);
        digit2.setMinValue(0);
        digit2.setMaxValue(10);
        digit2.setDisplayedValues(digit2Array);
        digit2.setValue(Arrays.asList(digit2Array).indexOf(digit2filler));


        final NumberPicker digit3 =  (NumberPicker) view.findViewById(R.id.digit3);
        digit3.setMinValue(0);
        digit3.setMaxValue(10);
        digit3.setDisplayedValues(digit3Array);
        digit3.setValue(Arrays.asList(digit3Array).indexOf(digit3filler));

        Log.e("Index of digit 3 =>  "+digit3filler, " "+Arrays.asList(digit3Array).indexOf(digit3filler));


        final NumberPicker digit4 =  (NumberPicker) view.findViewById(R.id.digit4);
        digit4.setMinValue(0);
        digit4.setMaxValue(10);
        digit4.setDisplayedValues(digit4Array);
        digit4.setValue(Arrays.asList(digit4Array).indexOf(digit4filler));


        listview = (ListView) view.findViewById(R.id.list);


        Button updatebuton = (Button) view.findViewById(R.id.updatebutton);
        updatebuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("The new plate has been updated ", "Plate");
                SharedPreferences sharedPref5 = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_5 = sharedPref5.edit();
                formedStringPart2 = "";
                //Forming the number plate string without hyphens!

                if (!picker3.getDisplayedValues()[picker3.getValue()].equals("-")) {
                    formedStringPart2 += picker3.getDisplayedValues()[picker3.getValue()];
                }

                if (!picker4.getDisplayedValues()[picker4.getValue()].equals("-"))
                {
                    formedStringPart2 += picker4.getDisplayedValues()[picker4.getValue()];
                }

                if(!digit1.getDisplayedValues()[digit1.getValue()].equals("-"))
                {
                    formedStringPart2 += digit1.getDisplayedValues()[digit1.getValue()];
                }

                if(!digit2.getDisplayedValues()[digit2.getValue()].equals("-"))
                {
                    formedStringPart2 += digit2.getDisplayedValues()[digit2.getValue()];
                }

                if(!digit3.getDisplayedValues()[digit3.getValue()].equals("-"))
                {
                    formedStringPart2 += digit3.getDisplayedValues()[digit3.getValue()];
                }

                if(!digit4.getDisplayedValues()[digit4.getValue()].equals("-"))
                {
                    formedStringPart2 += digit4.getDisplayedValues()[digit4.getValue()];
                }

                if(formedString.trim().length() == 0 )
                {
                    formedString = sharedPref5.getString(getString(R.string.KEY_GaadiKey_Number_Saved),  "KA50Q7896");
                }

                // WelcomesYou

                editor_5.putString(getString(R.string.KEY_picker1_laststate), picker1.getDisplayedValues()[picker1.getValue()]);
                editor_5.putString(getString(R.string.KEY_picker2_laststate), picker2.getDisplayedValues()[picker2.getValue()]);
                editor_5.putString(getString(R.string.KEY_picker3_laststate), picker3.getDisplayedValues()[picker3.getValue()]);
                editor_5.putString(getString(R.string.KEY_picker4_laststate), picker4.getDisplayedValues()[picker4.getValue()]);

                editor_5.putString(getString(R.string.KEY_digit1_laststate), digit1.getDisplayedValues()[digit1.getValue()]);
                editor_5.putString(getString(R.string.KEY_digit2_laststate), digit2.getDisplayedValues()[digit2.getValue()]);
                editor_5.putString(getString(R.string.KEY_digit3_laststate), digit3.getDisplayedValues()[digit3.getValue()]);
                editor_5.putString(getString(R.string.KEY_digit4_laststate), digit4.getDisplayedValues()[digit4.getValue()]);

                editor_5.putString(getString(R.string.KEY_GaadiKey_Number_Saved),  formedString+" "+formedStringPart2);
                editor_5.commit();


                ((LaunchActivity_NavDrawer) getActivity()).displayView(0);

//                Intent i = new Intent(getActivity(), StickyHome.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
//                startActivity(i);

            }
        });

        // Default display of State name and Area name... This should display when know selection has happened




        return view;



    }


    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getActivity().getAssets().open("rto.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

//    public void NumberPlateUpdated(View view)
//    {
//        Log.e("The new plate has been updated ", "Plate");
//        SharedPreferences sharedPref5 = getActivity().getSharedPreferences("android_shared", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor_5 = sharedPref5.edit();
//        editor_5.putString(getString(R.string.KEY_GaadiKey_Number_Saved),  formedString);
//        editor_5.commit();
//
//    }



}
