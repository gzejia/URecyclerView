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
import com.mrv.role.BaseFraAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mMainTly;
    private ViewPager mMainVp;
    private BaseFraAdapter mFraAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
    }

    private void initView() {
        mMainTly = (TabLayout) findViewById(R.id.main_tly);
        mMainVp = (ViewPager) findViewById(R.id.main_vp);
    }

    private void initFragment() {
        int[] layoutIds = {R.layout.fragment_linear, R.layout.fragment_grid,
                R.layout.fragment_staggered_grid, R.layout.fragment_linear_h,
                R.layout.fragment_grid_h, R.layout.fragment_staggered_grid_h};
        int[] itemLayoutIds = {R.layout.item_recycle_main, R.layout.item_recycle_main,
                R.layout.item_recycle_main, R.layout.item_recycle_main_h,
                R.layout.item_recycle_main_h, R.layout.item_recycle_main_h};
        String[] mLayoutNames = {
                "Linear", "Grid", "StaggeredGrid", "LinearH", "GridH", "StaggeredGridH"};

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
                fragment.addRvData();
                break;
            case R.id.action_adds:
                fragment.addRvDataLs();
                break;
            case R.id.action_reduce:
                fragment.removeRvData();
                break;
            case R.id.action_refresh:
                fragment.updateRvData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
