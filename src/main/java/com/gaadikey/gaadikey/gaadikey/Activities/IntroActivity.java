package com.gaadikey.gaadikey.gaadikey.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.gaadikey.gaadikey.gaadikey.Activities.BaseSampleActivity;
import com.gaadikey.gaadikey.gaadikey.GaadiKey;
import com.gaadikey.gaadikey.gaadikey.R;
import com.gaadikey.gaadikey.gaadikey.TestFragmentAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class IntroActivity extends BaseSampleActivity {

    Tracker t;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_circles);

        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        t = ((GaadiKey) getApplication()).getTracker(GaadiKey.TrackerName.APP_TRACKER);
        t.setScreenName("Welcome"); // =
        t.send(new HitBuilders.AppViewBuilder().build());

    }

    public void setCurrentPagerItem(int item)
    {
        mPager.setCurrentItem(item);
    }

    public ViewPager getViewPager() {
        if (null == mPager) {
            mPager = (ViewPager) findViewById(R.id.pager);
        }
        return mPager;
    }

}
