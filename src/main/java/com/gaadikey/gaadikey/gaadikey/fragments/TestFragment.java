package com.gaadikey.gaadikey.gaadikey.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gaadikey.gaadikey.gaadikey.R;
import com.gaadikey.gaadikey.gaadikey.adapters.TestFragmentAdapter;
import com.gaadikey.gaadikey.gaadikey.activities.VerificationActivity;


public final class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    public static String running_content = "";
    public static TestFragmentAdapter testfragadapt;


    public static TestFragment newInstance(String content, TestFragmentAdapter test) {
        TestFragment fragment = new TestFragment();
        StringBuilder builder = new StringBuilder();

        testfragadapt = test;
        // Get Started Blue button has to be added for the last instance!
        // The 20 time repeater has been removed from the text!!!!!
        // The text is fetched depending on the type of intro like intro1, intro2, intro3, intro4 .. etc ...
//        for (int i = 0; i < 20; i++) {
//            builder.append(content).append(" ");
//        }
        running_content = content; // The running_content is populated! depending on the condition change the layout of the introduction page!


        if (content.equals("intro1")) {
            builder.append("GaadiKey - Power to access the network of automobiles");
        } else if (content.equals("intro2")) {
            builder.append("Find out which Gaadis do your friends have!");
        } else if (content.equals("intro3")) {
            builder.append("Read Gaadi reviews by actual owners.");
        } else if (content.equals("intro4")) {
            builder.append("Connects you and your Gaadi with your friends and their Gaadis");

        }

        fragment.mContent = builder.toString();
        fragment.id = content;


        return fragment;
    }

    private String mContent = "???";
    private String id = "???";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }

        // mContent = savedInstanceState.getString(KEY_CONTENT); //Saved Instance state is directly read!

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        final LayoutInflater _inflater = inflater;
        if (id.equalsIgnoreCase("intro1")) {
            view = inflater.inflate(R.layout.intro1, container, false);

            LinearLayout l1 = (LinearLayout) view.findViewById(R.id.layout1);
            l1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });


            return view;
        } else if (id.equalsIgnoreCase("intro2")) {
            view = inflater.inflate(R.layout.intro2, container, false);
            LinearLayout l2 = (LinearLayout) view.findViewById(R.id.layout2);
            l2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                }
            });


            return view;
        } else if (id.equalsIgnoreCase("intro3")) {
            view = inflater.inflate(R.layout.intro3, container, false);
            LinearLayout l3 = (LinearLayout) view.findViewById(R.id.layout3);
            l3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });
            return view;
        } else if (id.equalsIgnoreCase("intro4")) {
            view = inflater.inflate(R.layout.intro4, container, false);

            Button button = (Button) view.findViewById(R.id.StartButton);

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Start new activity from a fragment

                    Intent intent = new Intent(getActivity(), VerificationActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }
            });
            return view;
        }

        return view;


//
//
//
//        TextView text = new TextView(getActivity());
//        text.setGravity(Gravity.CENTER);
//        text.setText(mContent);
//        text.setTextSize(20 * getResources().getDisplayMetrics().density);
//        text.setPadding(20, 20, 20, 20);
//
//
//
//        LinearLayout layout = new LinearLayout(getActivity());
//        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//        layout.setGravity(Gravity.CENTER);
//
//        if(running_content.equals("intro4"))
//        {
//            Button btn = new Button(getActivity());
//            btn.setGravity(Gravity.CENTER);
//            btn.setText("GET STARTED");
//            btn.setTextSize(12* getResources().getDisplayMetrics().density);
//            btn.setPadding(5,5,5,5);
//            btn.setId(R.id.GetStarted);
//            layout.addView(btn);
//        }
//
//        layout.addView(text);
//
////        if(running_content.equals("intro4"))
////        {
////
////            // Add the button here.. on click it has to react and go to verify phonenumber
////            Button btn = new Button(getActivity());
////            btn.setGravity(Gravity.CENTER);
////            btn.setText("GET STARTED");
////            btn.setTextSize(12* getResources().getDisplayMetrics().density);
////            btn.setPadding(5,5,5,5);
////            layout.addView(btn);
////        }
//
//        return layout;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}