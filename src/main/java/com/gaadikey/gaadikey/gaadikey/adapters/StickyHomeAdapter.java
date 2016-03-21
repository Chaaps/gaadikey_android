package com.gaadikey.gaadikey.gaadikey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaadikey.gaadikey.gaadikey.R;

import java.util.ArrayList;

/**
 * Created by madratgames on 22/09/14.
 */
public class StickyHomeAdapter extends ArrayAdapter<String> {

    private Context context;
    private final ArrayList<String> laneList;


    public StickyHomeAdapter(Context context, ArrayList<String> val) {
        super(context, R.layout.list_lanes, val);
        this.context = context;
        this.laneList = val;

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_mobile, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        //textView.setText(values[position]);
        // Change icon based on name
        String s = laneList.get(position);
        textView.setText(laneList.get(position));
        System.out.println(s);
        if (s.equals("Public Lane")) {
            imageView.setImageResource(R.drawable.public_lane);
        } else if (s.equals("Friends Lane")) {
            imageView.setImageResource(R.drawable.friends_lane);
        } else if (s.equals("Safety Lane")) {
            imageView.setImageResource(R.drawable.safety_lane);
        } else if (s.equals("Shopping Lane")) {
            imageView.setImageResource(R.drawable.shopping_lane);
        } else if (s.equals("News")) {
            imageView.setImageResource(R.drawable.feedback);
        }
        //  new ImageDownloader(imageView).execute(contactlist.get(position).get("ImgUrl"));

        return rowView;
    }


}
