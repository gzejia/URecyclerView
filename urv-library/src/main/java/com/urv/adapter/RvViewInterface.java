package com.urv.adapter;

import android.view.View;

/**
 * @author gzejia 978862664@qq.com
 */
public interface RvViewInterface {

    /**
     * 添加头部视图
     *
     * @param headerView
     */
    void addHeaderView(View headerView);

    /**
     * 移除头部视图
     */
    void removeHeaderView(View headerView);

    /**
     * 添加底部视图
     *
     * @param footerView
     */
    void addFooterView(View footerView);

    /**
     * 移除底部视图
     */
    void removeFooterView(View footerView);

    /**
     * 移除所有底部视图
     */
    void removeAllFooterView();

    /**
     * 更新空数据提示视图显示状态
     */
    void updateEmptyViewShow();
}
