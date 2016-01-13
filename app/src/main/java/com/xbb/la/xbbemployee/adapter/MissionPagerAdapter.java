package com.xbb.la.xbbemployee.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.xbb.la.xbbemployee.ui.fragment.MissionListFragment;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class MissionPagerAdapter extends FragmentPagerAdapter {
    public MissionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle args = null;
        switch (position) {
            case 0:
                fragment = new MissionListFragment();
                args = new Bundle();
                args.putSerializable("type", 0);
                break;
            case 1:
                fragment = new MissionListFragment();
                args = new Bundle();
                args.putSerializable("type", 1);
                break;
            case 2:
                fragment = new MissionListFragment();
                args = new Bundle();
                args.putSerializable("type", 2);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
