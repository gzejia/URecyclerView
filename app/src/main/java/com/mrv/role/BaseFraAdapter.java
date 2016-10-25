package com.mrv.role;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定ViewPager使用的Fragment适配器
 *
 * @author gzejia 978862664@qq.com
 */
public class BaseFraAdapter extends FragmentStatePagerAdapter {

    /**
     * 标签栏显示文本集合
     */
    public List<String> mTabTitleLv = new ArrayList<>();

    /**
     * 绑定Fragment集合
     */
    public List<Fragment> mFragmentLv = new ArrayList<>();

    /**
     * 记录Fragment集合大小
     */
    private int mChildCount = 0;

    /**
     * 构造器
     *
     * @param fm           Fragment控制器
     * @param fragmentList Fragment集合
     * @param tabTitleList 标签栏显示文本集合
     */
    public BaseFraAdapter(FragmentManager fm, List<Fragment> fragmentList,
                          List<String> tabTitleList) {
        super(fm);
        this.mTabTitleLv.addAll(tabTitleList);
        this.mFragmentLv.addAll(fragmentList);
    }

    /**
     * 更新Fragment显示
     *
     * @param fragmentList Fragment集合
     * @param tabTitleList 标签栏显示文本集合
     */
    public void updateAdapter(List<Fragment> fragmentList, List<String> tabTitleList) {
        this.mTabTitleLv.clear();
        this.mFragmentLv.clear();
        this.mTabTitleLv.addAll(tabTitleList);
        this.mFragmentLv.addAll(fragmentList);
        notifyDataSetChanged();
    }

    public List<Fragment> getFragmentLv() {
        return mFragmentLv;
    }

    @Override
    public int getCount() {
        return mFragmentLv.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitleLv.get(position % mTabTitleLv.size());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentLv.get(position);
    }

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
