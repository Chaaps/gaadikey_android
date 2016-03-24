package com.gaadikey.gaadikey.gaadikey.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.gaadikey.gaadikey.gaadikey.R;
import com.gaadikey.gaadikey.gaadikey.adapters.GaadiKeyPagerAdapter;
import io.fabric.sdk.android.Fabric;

public class OnBoardingActivity extends FragmentActivity {

    ViewPager gaadikey_on_boarding_viewpager;
    PagerAdapter gaadikey_pager_adapter;
    ImageView on_boarding_indicator_image1;
    ImageView on_boarding_indicator_image2;
    ImageView on_boarding_indicator_image3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        Fabric.with(this, new Crashlytics());
        gaadikey_on_boarding_viewpager = (ViewPager) findViewById(R.id.gaadikey_on_boarding_viewpager);
        on_boarding_indicator_image1 = (ImageView) findViewById(R.id.on_boarding_indicator_image1);
        on_boarding_indicator_image2 = (ImageView) findViewById(R.id.on_boarding_indicator_image2);
        on_boarding_indicator_image3 = (ImageView) findViewById(R.id.on_boarding_indicator_image3);
        gaadikey_pager_adapter = new GaadiKeyPagerAdapter(getSupportFragmentManager());
        gaadikey_on_boarding_viewpager.setAdapter(gaadikey_pager_adapter);
        adjustIndicator(gaadikey_on_boarding_viewpager.getCurrentItem());
        gaadikey_on_boarding_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adjustIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void adjustIndicator(int currentScreenPosition) {
        switch (currentScreenPosition) {
            case 0:
                on_boarding_indicator_image1.setImageResource(R.drawable.on_boarding_indicator2);
                on_boarding_indicator_image2.setImageResource(R.drawable.on_boarding_indicator1);
                on_boarding_indicator_image3.setImageResource(R.drawable.on_boarding_indicator1);
                break;
            case 1:
                on_boarding_indicator_image1.setImageResource(R.drawable.on_boarding_indicator1);
                on_boarding_indicator_image2.setImageResource(R.drawable.on_boarding_indicator2);
                on_boarding_indicator_image3.setImageResource(R.drawable.on_boarding_indicator1);
                break;
            case 2:
                on_boarding_indicator_image1.setImageResource(R.drawable.on_boarding_indicator1);
                on_boarding_indicator_image2.setImageResource(R.drawable.on_boarding_indicator1);
                on_boarding_indicator_image3.setImageResource(R.drawable.on_boarding_indicator2);
                break;
        }
    }


}
