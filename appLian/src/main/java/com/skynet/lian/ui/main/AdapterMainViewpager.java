package com.skynet.lian.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.skynet.lian.ui.tabchat.TabChatFragment;
import com.skynet.lian.ui.tabexplore.TabExploreFragment;

public class AdapterMainViewpager extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public AdapterMainViewpager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: {
                return TabChatFragment.newInstance();
            }

            case 1: {
                return TabExploreFragment.newInstance();

            }
            default: {
                return TabChatFragment.newInstance();
            }
        }

    }
}
