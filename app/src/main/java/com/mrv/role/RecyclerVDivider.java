package com.mrv.role;

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

/**
 * RecycleView 分割符,参考实现 <link>http://blog.csdn.net/lmj623565791/article/details/45059587</link>
 *
 * @author gzejia 978862664@qq.com
 */
public class RecyclerVDivider extends RecyclerView.ItemDecoration {

    private Drawable mDividerDraw;
    private int mLeftSpace, mRightSpace, mTopSpace, mBottomSpace;
    private int mSpaceColor;

    /**
     * @param context src/main/res/values/styles <item name="android:listDivider"/>
     */
    public RecyclerVDivider(Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        mDividerDraw = a.getDrawable(0);
        a.recycle();
    }

    /**
     * @param drawable src/main/res/drawable
     */
    public RecyclerVDivider(Drawable drawable) {
        mDividerDraw = drawable;
    }

    /**
     * @param leftSpace 左侧间距
     */
    public void setLeftSpace(int leftSpace) {
        mLeftSpace = leftSpace;
    }

    /**
     * @param rightSpace 右侧间距
     */
    public void setRightSpace(int rightSpace) {
        mRightSpace = rightSpace;
    }

    /**
     * @param topSpace 顶部间距
     */
    public void setTopSpace(int topSpace) {
        mTopSpace = topSpace;
    }

    /**
     * @param bottomSpace 底部间距
     */
    public void setBottomSpace(int bottomSpace) {
        mBottomSpace = bottomSpace;
    }

    /**
     * @param spaceColor 分割线间距颜色
     */
    public void setSpaceColor(int spaceColor) {
        mSpaceColor = spaceColor;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (0 != mSpaceColor) c.drawColor(mSpaceColor);
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin + mLeftSpace;
            final int right = child.getRight() + params.rightMargin
                    + mDividerDraw.getIntrinsicWidth() - mRightSpace;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerDraw.getIntrinsicHeight();
            mDividerDraw.setBounds(left, top, right, bottom);
            mDividerDraw.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin + mTopSpace;
            final int bottom = child.getBottom() + params.bottomMargin - mBottomSpace;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerDraw.getIntrinsicWidth();
            mDividerDraw.setBounds(left, top, right, bottom);
            mDividerDraw.draw(c);
        }
    }

    private boolean isVertical(LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getOrientation() == LinearLayout.VERTICAL;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation() == LinearLayout.VERTICAL;
        } else if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation() == LinearLayout.VERTICAL;
        }
        return true;
    }

    private int getSpanIndex(RecyclerView.Adapter adapter) {
        if (adapter instanceof BaseRVAdapter) {
            return ((BaseRVAdapter) adapter).mSpanIndex + 1;
        }
        return 0;
    }

    private int getMaxRaw(RecyclerView.Adapter adapter, int spanCount) {
        int childCount = adapter.getItemCount();
        int maxRawSize = childCount - childCount % spanCount;

        if (adapter instanceof BaseRVAdapter) {
            BaseRVAdapter baseRVAdapter = (BaseRVAdapter) adapter;

            if (null != baseRVAdapter.mFooterViews && null != baseRVAdapter.mHeaderViews) {
                childCount -= baseRVAdapter.mFooterViews.size();
                childCount -= baseRVAdapter.mHeaderViews.size();
                maxRawSize = childCount - childCount % spanCount;
                maxRawSize += baseRVAdapter.mHeaderViews.size();
            }
        }
        return maxRawSize;
    }

    private boolean getItemState(RecyclerView.Adapter adapter) {
        if (adapter instanceof BaseRVAdapter) {
            return ((BaseRVAdapter) adapter).isMainItem;
        }
        return true;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        LayoutManager layoutManager = parent.getLayoutManager();
        int spanCount = getSpanCount(parent);
        boolean isLastRaw = isVertical(layoutManager) ?
                itemPosition + 1 >= getMaxRaw(parent.getAdapter(), spanCount) :
                getSpanIndex(parent.getAdapter()) == spanCount;

        boolean isLastColum = isVertical(layoutManager) ?
                spanCount == getSpanIndex(parent.getAdapter()) :
                itemPosition + 1 >= getMaxRaw(parent.getAdapter(), spanCount);

        if (getItemState(parent.getAdapter())) {
            outRect.set(0, 0,
                    isLastColum ? 0 : mDividerDraw.getIntrinsicWidth(),// 如果是最后一列，则不需要绘制右边
                    isLastRaw ? 0 : mDividerDraw.getIntrinsicHeight());// 如果是最后一行，则不需要绘制底部
        }
    }
}