package com.gaadikey.gaadikey.gaadikey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.gaadikey.gaadikey.gaadikey.util.SystemUiHider;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ActivityLauncher extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref =  getSharedPreferences("android_shared" , MODE_PRIVATE);
        String launch_code = sharedPref.getString(getString(R.string.KEY_signupstatus),  Constants.PIN_NOTDISPATCHED);
        // commenting out this in order to persist all values!
        // sharedPref.edit().clear().commit();  // This clears all the values present in the sharedpreferences!
        // The ActivityLauncher activity has to be popped out of the stack once a new activity is launched.
        // Temporarily launch the IntroActivity ..  for every condition for testing purposes!!!


        // The  Intro activity is launched!!!  this should take to IntroActivity with the introduction steps!

        // Disabling the  below conditions temporarily ... All launch instances will lead to introductionActivity!

        if(launch_code.equals(""+Constants.PIN_NOTDISPATCHED))
        {
            Log.e("Status code", "PIN is not dispatched");
            Intent i = new Intent(ActivityLauncher.this, IntroActivity.class);
            i.setFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ));
            startActivity(i);
            //  If the user is unverified .. Alert him/her to enter the PIN!

//            Intent i = new Intent(ActivityLauncher.this, VerificationActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);

           // startActivity(new Intent(ActivityLauncher.this, VerificationActivity.class ));
            // launch verificationActivity
        }
        else if( launch_code.equals(""+Constants.PIN_DISPATCHED))
        {
            Log.e("Launch code", "PIN is dispatched ");
       //     startActivity(new Intent(ActivityLauncher.this, EnterPINActivity.class));

            Intent i = new Intent(ActivityLauncher.this, EnterPINActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

            // go to enter PIN activity
        }

        else if( launch_code.equals(""+Constants.PIN_VERIFIED))
        {
            Log.e("Launch code", "PIN is verified ");
          //  startActivity(new Intent(ActivityLauncher.this, MyActivity.class));
            // go to MyActivity in order to complete profile registration.
            Intent i = new Intent(ActivityLauncher.this, MyActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }

        else if( launch_code.equals(""+Constants.PROFILE_REGISTERED))
        {
            Log.e("Launch code", "Profile is registered ");
       //     startActivity(new Intent(ActivityLauncher.this,  ListMobileActivity.class));
       //     Log.e("Launch code" , "profile registered");
            // Once the profile is registered take it to StickyHome
        //    startActivity(new Intent(ActivityLauncher.this, StickyHome.class));

            //StickyHome
            Intent i = new Intent(ActivityLauncher.this, StickyHome.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);


            // The StickyHome is now LaunchActivity_NavDrawer


//            Intent i = new Intent(ActivityLauncher.this, LaunchActivity_NavDrawer.class );
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
//            startActivity(i);



            // go to Contacts Activity where all your contacts are present.
        }
        // The following conditions are not yet implemented

        else if(launch_code.equals(""+Constants.INTRO_STEP1_COMPLETED))
        {
            Log.e("Launch code", "Intro Step 1 completed");
            startActivity(new Intent(ActivityLauncher.this, IntroStep2.class));

        }

        else if(launch_code.equals(""+Constants.INTRO_STEP2_COMPLETED))
        {
            Log.e("Launch code", "Intro Step 1 completed");
            startActivity(new Intent(ActivityLauncher.this, IntroStep3.class));

        }

        else if(launch_code.equals(""+Constants.INTRO_STEP3_COMPLETED))
        {
            Log.e("Launch code", "Intro Step 1 completed");
            // This should go to actual app
        }

    }

}
