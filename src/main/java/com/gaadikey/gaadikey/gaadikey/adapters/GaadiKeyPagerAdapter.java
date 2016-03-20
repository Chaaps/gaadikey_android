package com.gaadikey.gaadikey.gaadikey.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gaadikey.gaadikey.gaadikey.fragments.OnBoardingFragment1;
import com.gaadikey.gaadikey.gaadikey.fragments.OnBoardingFragment2;
import com.gaadikey.gaadikey.gaadikey.fragments.OnBoardingFragment3;


public class GaadiKeyPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_ON_BOARDING_SCREENS = 3;

    public GaadiKeyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new OnBoardingFragment1();
            case 1: return new OnBoardingFragment2();
            case 2: return new OnBoardingFragment3();
        }
        return new OnBoardingFragment1();
    }

    @Override
    public int getCount() {
        return NUM_ON_BOARDING_SCREENS;
    }
}
