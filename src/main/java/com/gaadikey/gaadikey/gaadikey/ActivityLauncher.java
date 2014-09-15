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

     //   sharedPref.edit().clear().commit();  // This clears all the values present in the sharedpreferences!


        if(launch_code.equals(""+Constants.PIN_NOTDISPATCHED))
        {
            Log.e("Status code", "PIN is not dispatched");
            startActivity(new Intent(ActivityLauncher.this, VerificationActivity.class ));
            // launch verificationActivity
        }
        else if( launch_code.equals(""+Constants.PIN_DISPATCHED))
        {
            Log.e("Launch code", "PIN is dispatched ");
            startActivity(new Intent(ActivityLauncher.this, EnterPINActivity.class));
            // go to enter PIN activity
        }

        else if( launch_code.equals(""+Constants.PIN_VERIFIED))
        {
            Log.e("Launch code", "PIN is verified ");
            startActivity(new Intent(ActivityLauncher.this, MyActivity.class));
            // go to MyActivity in order to complete profile registration.
        }

        else if( launch_code.equals(""+Constants.PROFILE_REGISTERED))
        {
            Log.e("Launch code", "Profile is registered ");
            startActivity(new Intent(ActivityLauncher.this,  ListMobileActivity.class));
            Log.e("Launch code" , "profile registered");
            // go to Contacts Activity where all your contacts are present.
        }
    }

}
