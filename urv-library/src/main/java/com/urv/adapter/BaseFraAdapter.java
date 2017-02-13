package com.urv.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定ViewPager使用的Fragment适配器
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

    /**
     * 获取Fragment集合大小
     *
     * @return 集合大小
     */
    @Override
    public int getCount() {
        return mFragmentLv.size();
    }

    /**
     * 获取Fragment对应标签栏显示文本内容
     *
     * @param position Fragment索引
     * @return 标签栏文本
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitleLv.get(position % mTabTitleLv.size());
    }

    /**
     * 获取当前显示ViewPager下的Fragment
     *
     * @param position Fragment索引
     * @return Fragment
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentLv.get(position);
    }

    /**
     * 重写notifyDataSetChanged();以记录Fragment集合大小
     */
    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    /**
     * 重写getItemPosition();准确判断Fragment的位置
     *
     * @param object Fragment对象
     * @return Fragment索引
     */
    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
