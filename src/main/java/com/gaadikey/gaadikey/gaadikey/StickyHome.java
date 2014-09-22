package com.gaadikey.gaadikey.gaadikey;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.gaadikey.gaadikey.gaadikey.adaptor.StickyHomeAdapter;

import java.util.ArrayList;


public class StickyHome extends ListActivity {

    ArrayList<String> lanes = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_home);
        lanes.add("Public Lane");
        lanes.add("Friends Lane");
        lanes.add("Safety Lane");
        lanes.add("Shopping Lane");
        lanes.add("Trips Lane");
        setListAdapter(new StickyHomeAdapter(StickyHome.this, lanes));
        // This should open the Sticky home

    }


    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.e("Click happened", "Click happened ");
       // Toast.makeText(this, "You clicked " + lanes.get(position), Toast.LENGTH_LONG).show();

        // Removing toast UI from the code.. Launching the activity directly from here!

        //  Open the activity which opens up  Public Lane
        // if the clicked item is public lane then open the public lane activity

         if(lanes.get(position).equals("Public Lane"))
         startActivity(new Intent(StickyHome.this, PublicLaneActivity.class));

         if(lanes.get(position).equals("Friends Lane"))
         startActivity(new Intent(StickyHome.this, FriendsLaneActivity.class));
         // The friends Lane activity,would now contain the contents present in ContactHome

         if(lanes.get(position).equals("Safety Lane"))
         startActivity(new Intent(StickyHome.this, SafetyLaneActivity.class));

         if(lanes.get(position).equals("Shopping Lane"))
         startActivity(new Intent(StickyHome.this, ShoppinglaneActivity.class));

         if(lanes.get(position).equals("Trips Lane"))
         startActivity(new Intent(StickyHome.this, TripsLaneActivity.class));

    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sticky_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
