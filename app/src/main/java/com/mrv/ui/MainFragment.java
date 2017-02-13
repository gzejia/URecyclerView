package com.mrv.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mrv.R;
import com.mrv.adapter.RecycleVAdapter;
import com.mrv.bean.RecycleVModel;
import com.mrv.utils.UnitConverter;
import com.urv.adapter.BaseRVAdapter;
import com.urv.widget.URecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author gzejia 978862664@qq.com
 */
public class MainFragment extends Fragment {

    private URecyclerView mMainRv;
    private SwipeRefreshLayout mRefreshLayout;

    private RecycleVAdapter mAdapter;

    private int mLayoutId, mItemLayoutId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(mLayoutId, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMainRv = (URecyclerView) view.findViewById(R.id.main_rv);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_sw_ly);

        initRecycleView();
    }

    /**
     * 实例化列表视图，包括随机数据集与适配器
     */
    private void initRecycleView() {
        mAdapter = new RecycleVAdapter(getActivity(), getDataLs("MVR", 20), mItemLayoutId);
        mAdapter.addItemClickListener(new BaseRVAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                Toast.makeText(getActivity(), mAdapter.get(position).text, Toast.LENGTH_SHORT).show();
            }
        });

        mMainRv.setAdapter(mAdapter);
        mMainRv.setRefreshLayout(mRefreshLayout, mRefresh);
    }

    /**
     * 模拟接口请求操作
     */
    private void loadData(final int pageIndex) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pageIndex == 1) {
                    updateData(20);
                } else {
                    addData(10);
                }
            }
        }, 2000);
    }

    /**
     * 获取随机数据对象
     *
     * @param testStr 文本显示内容
     * @return 测试数据对象，随机高度值
     */
    public RecycleVModel getData(String testStr) {
        RecycleVModel model = new RecycleVModel();
        model.text = testStr;
        model.showHeight = UnitConverter.dip2px(getActivity(), (int) (60 + Math.random() * 120));
        return model;
    }

    /**
     * 获取随机数据集
     *
     * @param testStr 文本显示内容
     * @param size    数据集大小
     * @return 测试数据集，随机高度值
     */
    public List<RecycleVModel> getDataLs(String testStr, int size) {
        List<RecycleVModel> strLs = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            strLs.add(getData(testStr + i));
        }
        return strLs;
    }

    /**
     * 添加随机数据对象，默认顶部添加
     */
    public void addData(int number) {
        if (null != mAdapter) {
            List<RecycleVModel> models = getDataLs("MRV_ADD", number);

            mAdapter.add(models);
            mMainRv.refreshLoadSuccessIsEnd(models.size() < 20);
        }
    }

    /**
     * 移除数据对象，默认顶部移除
     */
    public void removeData() {
        if (null != mAdapter) {
            mAdapter.remove(0);
            mMainRv.smoothScrollToPosition(0);
        }
    }

    /**
     * 清空数据对象
     */
    public void cleanData() {
        if (null != mAdapter) {
            mAdapter.removeAll();
            mAdapter.updateEmptyViewShow();
        }
    }

    /**
     * 更新数据对象
     */
    public void updateData(int number) {
        if (null != mAdapter) {
            List<RecycleVModel> models = getDataLs("MRV_UPDATE", number);
            mAdapter.updateForPage(1, models);
            mMainRv.refreshLoadSuccessIsEnd(models.size() < 20);
        }
    }

    /**
     * 设置测试数据显示视图
     *
     * @param layoutId     页面视图布局Id
     * @param itemLayoutId 列表项视图布局Id
     */
    public void setLayoutId(int layoutId, int itemLayoutId) {
        mLayoutId = layoutId;
        mItemLayoutId = itemLayoutId;
    }

    URecyclerView.URecyclerRefreshListener mRefresh = new URecyclerView.URecyclerRefreshListener() {
        @Override
        public void onRefresh() {
            loadData(1);
        }

        @Override
        public void onLoad(int pageIex) {
            loadData(pageIex);
        }

        @Override
        public void loadStats(int stats) {

        }
    };
}
