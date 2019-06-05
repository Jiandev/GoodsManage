package com.example.goodsmanage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jiandev
 * Modify By:
 * Date: 2019/6/5 14:35
 * Description:
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList = new ArrayList<Fragment>();

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int i) {
        return mList.get(i);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
