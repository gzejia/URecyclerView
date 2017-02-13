package com.mrv.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.mrv.R;
import com.urv.adapter.BaseFraAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 标签栏视图
     */
    private TabLayout mMainTly;

    /**
     * 卡片视图
     */
    private ViewPager mMainVp;

    /**
     * Fragment集合适配器
     */
    private BaseFraAdapter mFraAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        initView();
        initFragment();
    }

    /**
     * 视图实例化
     */
    private void initView() {
        mMainTly = (TabLayout) findViewById(R.id.main_tly);
        mMainVp = (ViewPager) findViewById(R.id.main_vp);
    }

    /**
     * Fragment集实例化
     */
    private void initFragment() {
        int[] layoutIds = {R.layout.fragment_linear2, R.layout.fragment_linear,
                R.layout.fragment_grid, R.layout.fragment_staggered_grid};
        int[] itemLayoutIds = {R.layout.item_recycle_vertical, R.layout.item_recycle_horizonal,
                R.layout.item_recycle_vertical, R.layout.item_recycle_vertical};
        String[] mLayoutNames = {"↓Linear↑", "↓Linear←", "Grid", "StaggeredGrid"};

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0, k = layoutIds.length; i < k; i++) {
            MainFragment fragment = new MainFragment();
            fragment.setLayoutId(layoutIds[i], itemLayoutIds[i]);
            fragments.add(fragment);
        }

        mFraAdapter = new BaseFraAdapter(
                getSupportFragmentManager(), fragments, Arrays.asList(mLayoutNames));
        mMainVp.setAdapter(mFraAdapter);
        mMainVp.setCurrentItem(0);
        mMainVp.setOffscreenPageLimit(fragments.size());
        mMainTly.setupWithViewPager(mMainVp);
        mMainTly.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            try {
                Class clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
                Field field = clazz.getDeclaredField("mOptionalIconsVisible");
                if (field != null) {
                    field.setAccessible(true);
                    field.set(menu, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainFragment fragment = (MainFragment) mFraAdapter.getItem(mMainVp.getCurrentItem());
        switch (item.getItemId()) {
            case R.id.action_add:
                fragment.addData(1);
                break;
            case R.id.action_reduce:
                fragment.removeData();
                break;
            case R.id.action_refresh:
                fragment.updateData(20);
                break;
            case R.id.action_clean:
                fragment.cleanData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
