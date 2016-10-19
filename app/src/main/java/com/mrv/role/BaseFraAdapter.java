package com.mrv.role;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaseFraAdapter extends FragmentStatePagerAdapter {

    private List<String> tabTitleLv = new ArrayList<>();
    private List<Fragment> fragmentLv = new ArrayList<>();

    public BaseFraAdapter(FragmentManager fm, List<Fragment> fragmentList,
                          List<String> tabTitleList) {
        super(fm);
        this.tabTitleLv.addAll(tabTitleList);
        this.fragmentLv.addAll(fragmentList);
    }

    public void updateAdapter(List<Fragment> fragmentList, List<String> tabTitleList) {
        this.tabTitleLv.clear();
        this.fragmentLv.clear();
        this.tabTitleLv.addAll(tabTitleList);
        this.fragmentLv.addAll(fragmentList);
        notifyDataSetChanged();
    }

    public List<Fragment> getFragmentLv() {
        return fragmentLv;
    }

    @Override
    public int getCount() {
        return fragmentLv.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleLv.get(position % tabTitleLv.size());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentLv.get(position);
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
