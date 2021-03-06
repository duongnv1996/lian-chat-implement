package com.skynet.lian.ui.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.skynet.lian.models.Profile;

public class AdapterProfileViewpager extends FragmentStatePagerAdapter {
    private final String id;
    private final Profile profile;
    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public AdapterProfileViewpager(FragmentManager fm, String id,Profile profile) {
        super(fm);
        this.id = id;
        this.profile = profile;
    }

    @Override
    public int getCount() {
        return 3;
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
                return GridFragment.newInstance(id);
            }
            case 1: {
                return ListFragment.newInstance(id,profile);
            }
            case 2: {
                return VideoFragment.newInstance(id);
            }
            default: {
                return GridFragment.newInstance(id);
            }
        }

    }
}
