package com.urv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.urv.adapter.BaseRVAdapter;


/**
 * @author gzejia 978862664@qq.com
 */
public class URecyclerDivider extends RecyclerView.ItemDecoration {

    /**
     * 分隔符资源
     */
    private Drawable mDividerDraw;

    /**
     * 分隔符左边距
     */
    private float mLeftSpace;

    /**
     * 分隔符右边距
     */
    private float mRightSpace;

    /**
     * 分隔符顶部边距
     */
    private float mTopSpace;

    /**
     * 分隔符底部边距
     */
    private float mBottomSpace;

    /**
     * 分隔符间距色值
     */
    private int mSpaceColor;

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public URecyclerDivider(Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        mDividerDraw = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 构造器
     *
     * @param drawable 分隔符资源
     */
    public URecyclerDivider(Drawable drawable) {
        mDividerDraw = drawable;
    }

    /**
     * 设置左侧间距
     *
     * @param leftSpace 间距大小
     */
    public void setLeftSpace(float leftSpace) {
        mLeftSpace = leftSpace;
    }

    /**
     * 设置右侧间距
     *
     * @param rightSpace 间距大小
     */
    public void setRightSpace(float rightSpace) {
        mRightSpace = rightSpace;
    }

    /**
     * 设置顶部间距
     *
     * @param topSpace 间距大小
     */
    public void setTopSpace(float topSpace) {
        mTopSpace = topSpace;
    }

    /**
     * 设置底部间距
     *
     * @param bottomSpace 间距大小
     */
    public void setBottomSpace(float bottomSpace) {
        mBottomSpace = bottomSpace;
    }

    /**
     * 设置分隔符颜色
     *
     * @param spaceColor 色值Id
     */
    public void setSpaceColor(int spaceColor) {
        mSpaceColor = spaceColor;
    }

    /**
     * 绘制分割符
     *
     * @param c      画布
     * @param parent 列表视图
     * @param state  列表所处状态
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (0 != mSpaceColor) c.drawColor(mSpaceColor);
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    /**
     * 绘制水平分隔符
     *
     * @param c      画布
     * @param parent 列表视图
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final float left = child.getLeft() - params.leftMargin + mLeftSpace;
            final float right = child.getRight() + params.rightMargin
                    + mDividerDraw.getIntrinsicWidth() - mRightSpace;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerDraw.getIntrinsicHeight();
            mDividerDraw.setBounds((int) left, top, (int) right, bottom);
            mDividerDraw.draw(c);
        }
    }

    /**
     * 绘制垂直分隔符
     *
     * @param c      画布
     * @param parent 列表视图
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final float top = child.getTop() - params.topMargin + mTopSpace;
            final float bottom = child.getBottom() + params.bottomMargin - mBottomSpace;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerDraw.getIntrinsicWidth();
            mDividerDraw.setBounds(left, (int) top, right, (int) bottom);
            mDividerDraw.draw(c);
        }
    }

    /**
     * 判断当前列表方向是否为垂直类型
     *
     * @param layoutManager 布局管理器
     * @return true：垂直类型列表，false：水平类型列表
     */
    private boolean isVertical(LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getOrientation() == LinearLayout.VERTICAL;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation() == LinearLayout.VERTICAL;
        } else {
            return ((LinearLayoutManager) layoutManager).getOrientation() == LinearLayout.VERTICAL;
        }
    }

    /**
     * 获取列表显示列数
     *
     * @param parent 列表视图
     * @return 当前显示列数
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = 1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    /**
     * 获取当前遍历Item所处列数
     *
     * @param adapter 列表适配器
     * @return 列数索引
     */
    private int getSpanIndex(RecyclerView.Adapter adapter) {
        if (adapter instanceof BaseRVAdapter) {
            return ((BaseRVAdapter) adapter).mSpanIndex + 1;
        }
        return 0;
    }

    /**
     * 获取当前列表可显示最大行值
     *
     * @param adapter   列表适配器
     * @param spanCount 当前显示列数
     * @return 最大行值（除底部视图以及最后一排数据）
     */
    private int getMaxRaw(RecyclerView.Adapter adapter, int spanCount) {
        return adapter.getItemCount() / spanCount;
    }

    /**
     * 获取当前Item状态
     *
     * @param adapter 列表适配器
     * @return true_数据Item，false_头部/底部View
     */
    private boolean getItemState(RecyclerView.Adapter adapter) {
        if (adapter instanceof BaseRVAdapter) {
            return ((BaseRVAdapter) adapter).isMainItem;
        }
        return true;
    }

    /**
     * 获取列表Item项信息
     *
     * @param outRect      矩形边框
     * @param itemPosition item索引
     * @param parent       列表视图
     */
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        LayoutManager layoutManager = parent.getLayoutManager();
        int spanCount = getSpanCount(parent);

        boolean isLastRaw = isVertical(layoutManager) ?
                itemPosition >= getMaxRaw(parent.getAdapter(), spanCount) - 1 :
                getSpanIndex(parent.getAdapter()) >= spanCount;

        boolean isLastColum = isVertical(layoutManager) ?
                spanCount == getSpanIndex(parent.getAdapter()) :
                itemPosition >= getMaxRaw(parent.getAdapter(), spanCount) - 1;

        if (getItemState(parent.getAdapter())) {
            outRect.set(0, 0,
                    isLastColum ? 0 : mDividerDraw.getIntrinsicWidth(),// 如果是最后一列，则不需要绘制右边
                    isLastRaw ? 0 : mDividerDraw.getIntrinsicHeight());// 如果是最后一行，则不需要绘制底部
        }
    }
}