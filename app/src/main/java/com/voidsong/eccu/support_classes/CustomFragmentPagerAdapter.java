package com.voidsong.eccu.support_classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.voidsong.eccu.fragments.FragmentCamera;
import com.voidsong.eccu.fragments.FragmentWeather;
import com.voidsong.eccu.R;
import com.voidsong.eccu.abstract_classes.RefreshableFragment;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter{

    private RefreshableFragment[] fragments = new RefreshableFragment[4];
    private String[] titles = new String[4];

    public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        titles[0] = "weather";
        titles[1] = "camera 1";
        titles[2] = "camera 2";
        titles[3] = "camera 3";
    }

    public RefreshableFragment getFragment(int position) {
        return fragments[position];
    }

    @Override
    public Fragment getItem(int position) {
        RefreshableFragment fragment;
        switch (position) {
            case 0:
                fragment = FragmentWeather.new_instance(true);
                fragments[0] = fragment;
                return fragment;
            case 1:
                fragment = FragmentCamera.new_instance(R.drawable.cam_refresh, true);
                fragments[1] = fragment;
                return fragment;
            case 2:
                fragment = FragmentCamera.new_instance(R.drawable.cam_not_available, false);
                fragments[2] = fragment;
                return fragment;
            case 3:
                fragment = FragmentCamera.new_instance(R.drawable.cam_not_available, false);
                fragments[3] = fragment;
                return fragment;
            default:
                fragment = FragmentCamera.new_instance(R.drawable.cam_not_available, false);
                fragments[4] = fragment;
                return fragment;
        }
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
