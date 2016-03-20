package com.gaadikey.gaadikey.gaadikey.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.gaadikey.gaadikey.gaadikey.R;
import com.gaadikey.gaadikey.gaadikey.adapters.GaadiKeyPagerAdapter;

public class OnBoardingActivity extends FragmentActivity {

    ViewPager gaadikey_on_boarding_viewpager;
    PagerAdapter gaadikey_pager_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        gaadikey_on_boarding_viewpager = (ViewPager) findViewById(R.id.gaadikey_on_boarding_viewpager);
        gaadikey_pager_adapter = new GaadiKeyPagerAdapter(getSupportFragmentManager());
        gaadikey_on_boarding_viewpager.setAdapter(gaadikey_pager_adapter);
    }
}
