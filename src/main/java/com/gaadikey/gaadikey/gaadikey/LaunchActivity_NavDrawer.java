package com.gaadikey.gaadikey.gaadikey;

/**
 * Created by madratgames on 08/10/14.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gaadikey.gaadikey.gaadikey.adaptor.NavDrawerListAdapter;

import java.util.ArrayList;


public class LaunchActivity_NavDrawer extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private String[] navMenuTags;;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private String searchString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchdrawer);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchString = extras.getString("searchString");
            Log.e("Obtained Searchstring is ", searchString);
        }
//        SnippetsDB_Helper logindb;
//        logindb=new SnippetsDB_Helper(this);
//        //logindb=logindb.open();

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // load slide menu tags
        navMenuTags = getResources().getStringArray(R.array.nav_drawer_tags);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuTags[0], navMenuIcons.getResourceId(0, -1), true, "22" ));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuTags[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuTags[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuTags[3],  navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuTags[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuTags[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuTags[6], navMenuIcons.getResourceId(6, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuTags[7], navMenuIcons.getResourceId(7, -1), true, "22"));
        //     navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuTags[8], navMenuIcons.getResourceId(8, -1)));
        // empty list
        //   navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuTags[8], navMenuIcons.getResourceId(8, -1)));
        //    navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuTags[9], navMenuIcons.getResourceId(9, -1)));
        //    navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuTags[10], navMenuIcons.getResourceId(10, -1)));
        // Pages
        //    navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuTags[11], navMenuIcons.getResourceId(11, -1)));
        // Pages
        //  navDrawerItems.add(new NavDrawerItem(navMenuTitles[12], navMenuTags[12], navMenuIcons.getResourceId(12, -1)));
//        // Pages
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[13], navMenuTags[13], navMenuIcons.getResourceId(13, -1)));
//
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[14], navMenuTags[14], navMenuIcons.getResourceId(14, -1)));
//
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[15], navMenuTags[15], navMenuIcons.getResourceId(15, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.setAdapter(adapter);
        // enabling action bar app icon and behaving it as toggle button

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {

                getSupportActionBar().setTitle(mTitle);
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                // invalidateOptionsMenu();
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                getSupportActionBar().setTitle(mTitle);
                // mDrawerList.bringToFront();
                // mDrawerLayout.requestLayout();

                // calling onPrepareOptionsMenu() to hide action bar icons
                //invalidateOptionsMenu();
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // mDrawerLayout.setFocusable(false);
        String homemenu_position = "";
        int homemenu_position_int = 0;

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //displayView(0);
            SharedPreferences sharedPref = getSharedPreferences("android_shared", Context.MODE_PRIVATE);
            homemenu_position = sharedPref.getString(getString(R.string.KEY_HomeMenu), "0");
            homemenu_position_int = Integer.parseInt(homemenu_position);
            displayView(homemenu_position_int);
        }
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item

            Log.e("The Item click has occured! ", "Item click!!! " );
            searchString="";
            displayView(position);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "code", Toast.LENGTH_LONG).show();
                // Create new fragment and transaction
//                Fragment newFragment = new Fragment_Java();
//                // consider using Java coding conventions (upper char class names!!!)
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                transaction.replace(R.id.frame_container, newFragment);
//                transaction.addToBackStack(null);
//                // Commit the transaction
//                transaction.commit();
                return true;

            case R.id.item1:
                Toast.makeText(getApplicationContext(), "send a suggestion", Toast.LENGTH_LONG).show();
                return true;

            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Meet developers", Toast.LENGTH_LONG).show();
                return true;

//            case R.id.item3:
//                Toast.makeText(getApplicationContext(), "Rate this app", Toast.LENGTH_LONG).show();
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Called when invalidateOptionsMenu() is triggered
     **/

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }




    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;


        switch (position) {

            case 0:

                //  fragment = new Fragment_SearchResults();
                fragment = Fragment_SearchResults.newInstance(searchString);

                break;
            case 1:
                fragment = new Fragment_PublicLane();
                break;
            case 2:
                fragment = new Fragment_FriendsLane(); // This should be the friends lane fragment!
                break;
            case 3:
                fragment = new Fragment_SafetyLane(); // This is friends lane fragment!
                break;
            case 4:
                fragment = new Fragment_ShoppingLane(); // Shopping lane fragment
                break;
            case 5:
                fragment = new Fragment_NumberPlate();
                break;
            case 6:
                fragment = new Fragment_Settings();
                break;
            case 7:
                fragment = new Fragment_Feedback();
                break;
//            case 5:
//                fragment = new Fragment_TripsLane();  // Trips lane fragment
//                break;

//            case 0:
//                fragment = new Fragment_a();
//                break;
//            case 1:
//                fragment = new Fragment_b();
//                break;
//            case 2:
//                fragment = new Fragment_C();
//                break;
            default:
                break;
        }

        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            //FragmentManager fragmentManager =  getFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getActionBar().setTitle(mTitle);
        getSupportActionBar().setTitle(mTitle);
    }


    // Handle the click event
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
        //
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
    }

    public void onBackPressed()
    {
        if(mDrawerLayout.isDrawerOpen(mDrawerList))
        {
            Intent i = new Intent(this, StickyHome.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(i);
            // Open the Base Activity
        }
        else
        {
            mDrawerLayout.openDrawer(mDrawerList);
            //  The Drawer is displayed!
        }

    }


}