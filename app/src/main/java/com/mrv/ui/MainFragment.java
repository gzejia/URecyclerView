package com.mrv.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mrv.R;
import com.mrv.adapter.TestAdapter;
import com.mrv.bean.TestModel;
import com.mrv.role.BaseRVAdapter;
import com.mrv.role.MyRecyclerView;
import com.mrv.utils.UnitConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gzejia 978862664@qq.com
 */
public class MainFragment extends Fragment {

    private MyRecyclerView mMainRv;
    private TestAdapter mAdapter;
    private int mLayoutId, mItemLayoutId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(mLayoutId, container, false);
        mMainRv = (MyRecyclerView) view.findViewById(R.id.main_rv);
        initRecycleView();

        return view;
    }

    private void initRecycleView() {
        mAdapter = new TestAdapter(getActivity(), getDataLs("MVR", 28), mItemLayoutId);
        mAdapter.addItemClickListener(new BaseRVAdapter.OnItemClickListener() {
            @Override
            public void itemSelect(int position) {
                Toast.makeText(getActivity(), mAdapter.getData(position).text, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        List<View> headerViewLs = getHeadFootViews("This is header view", mMainRv.getOrientation());
        List<View> footerViewLs = getHeadFootViews("This is footer view", mMainRv.getOrientation());
        if (headerViewLs.size() <= 1) {
            mAdapter.addHeaderView(headerViewLs.get(0));
        } else {
            mAdapter.addHeaderViews(headerViewLs);
        }

        if (footerViewLs.size() <= 1) {
            mAdapter.addFooterView(footerViewLs.get(0));
        } else {
            mAdapter.addFooterViews(footerViewLs);
        }
        mMainRv.setAdapter(mAdapter);
    }

    private List<View> getHeadFootViews(String str, int origin) {
        List<View> views = new ArrayList<>();

        for (int i = 0, k = 1 + (int) (Math.random() * 2); i < k; i++) {
            TextView textView = new TextView(getActivity());
            textView.setText(str);
            ViewGroup.LayoutParams params;
            if (origin == MyRecyclerView.VERTICAL) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        UnitConverter.dip2px(getActivity(), 50));
            } else {
                params = new ViewGroup.LayoutParams(UnitConverter.dip2px(getActivity(), 50),
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setBackgroundColor(views.size() > 0 ? Color.GREEN : Color.CYAN);
            views.add(textView);
        }
        return views;
    }

    public TestModel getData(String testStr) {
        TestModel model = new TestModel();
        model.text = testStr;
        model.showHeight = UnitConverter.dip2px(getActivity(), (int) (60 + Math.random() * 120));
        return model;
    }

    public List<TestModel> getDataLs(String testStr, int size) {
        List<TestModel> strLs = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            strLs.add(getData(testStr + i));
        }
        return strLs;
    }

    public void addRvData() {
        if (null != mAdapter) {
            mAdapter.addData(0, getData("MRV_ADD"));
            mMainRv.scrollToPosition(mAdapter.mHeaderViews.size());
        }
    }

    public void addRvDataLs() {
        if (null != mAdapter) {
            mAdapter.addDataLs(0, getDataLs("MRV_ADD", 2));
            mMainRv.scrollToPosition(mAdapter.mHeaderViews.size());
        }
    }

    public void removeRvData() {
        if (null != mAdapter) {
            mAdapter.removeData(0);
        }
    }

    public void updateRvData() {
        if (null != mAdapter) {
            mAdapter.updateData(0, getData("MRV_UPDATE"));
        }
    }

    public void setLayoutId(int layoutId, int itemLayoutId) {
        mLayoutId = layoutId;
        mItemLayoutId = itemLayoutId;
    }
}
