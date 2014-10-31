package com.gaadikey.gaadikey.gaadikey;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

public class IntroActivity extends BaseSampleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_circles);

        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);




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
