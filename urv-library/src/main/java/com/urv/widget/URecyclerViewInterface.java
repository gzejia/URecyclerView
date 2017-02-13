package com.urv.widget;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * @author gzejia 978862664@qq.com
 */
public interface URecyclerViewInterface {

    /**
     * @param refreshLayout SwipeRefreshLayout
     * @param listener      上下拉刷新监听
     */
    void setRefreshLayout(SwipeRefreshLayout refreshLayout,
                          URecyclerView.URecyclerRefreshListener listener);

    /**
     * 设置加载更多视图
     *
     * @param loadView
     */
    void setLoadMoreView(View loadView);

    /**
     * 设置加载更多视图（默认）
     */
    void setLoadMoreView();

    /**
     * 更新加载更多显示状态
     */
    void updateLoadMoreShow(int stats);

    /**
     * 设置空数提示视图
     *
     * @param emptyView
     */
    void setEmptyView(View emptyView);

    /**
     * 设置空数提示视图（默认）
     */
    void setEmptyView();

    /**
     * 启动数据刷新加载
     *
     * @param pageIndex 页码
     */
    void refreshLoadStart(int pageIndex);

    /**
     * 启动数据刷新加载
     */
    void refreshLoadStart();

    /**
     * 结束数据刷新加载，刷新,加载更多成功时调用
     */
    void refreshLoadSuccess();

    /**
     * 结束数据刷新加载，刷新,加载更多成功时调用
     *
     * @param isLoadMoreEnd true_结束上拉加载（已全部加载完成）
     */
    void refreshLoadSuccessIsEnd(boolean isLoadMoreEnd);

    /**
     * 结束数据刷新加载，刷新,加载更多失败时调用
     */
    void refreshLoadFail();

    /**
     * true_禁用下拉刷新
     */
    void disRefresh(boolean isHideRefresh);

    /**
     * true_禁用上拉加载更多
     */
    void disLoadMore(boolean isDisLoadMore);

    /**
     * 刷新状态
     *
     * @return true_正处于刷新，false_非刷新状态
     */
    boolean isRefresh();
}
