package com.mrv.role;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.mrv.R;
import com.mrv.utils.UnitConverter;

/**
 * 重定义RecycleView使用
 *
 * @author gzejia 978862664@qq.com
 */
public class MyRecyclerView extends RecyclerView {

    public static final int TYPE_LIST = 0;
    public static final int TYPE_GRID = 1;
    public static final int TYPE_STAGGERED_GRID = 2;

    /**
     * <ul>列表类型
     * <li>同 ListView ：{@link #TYPE_LIST}（默认）</li>
     * <li>同 GridView ：{@link #TYPE_GRID}</li>
     * <li>同 StaggeredGridView ：{@link #TYPE_STAGGERED_GRID}</li></ul>
     */
    private int mType;

    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 0;

    /**
     * <ul>列表类型
     * <li>垂直方向 ：{@link #VERTICAL}（默认）</li>
     * <li>水平方向 ：{@link #HORIZONTAL}</li></ul>
     */
    private int mOrientation;

    /**
     * ？固定大小，默认固定
     */
    private boolean isFixSize = true;

    private static final int DEFAULT_ROW_NUM = 2;

    /**
     * GRID与STAGGERED_GRID显示列数，默认{@link #DEFAULT_ROW_NUM}列
     */
    private int mSpanCount;

    /**
     * @see RecyclerVDivider
     * 分割线，默认从styles中获取
     */
    private Drawable mDividerDraw;

    /**
     * 分割线左侧边距，默认无边距
     */
    private int mDividerLeftSpace;

    /**
     * 分割线右侧边距，默认无边距
     */
    private int mDividerRightSpace;

    /**
     * 分割线顶部边距，默认无边距
     */
    private int mDividerTopSpace;

    /**
     * 分割线底部边距，默认无边距
     */
    private int mDividerBottomSpace;

    /**
     * 分割线间距颜色（即背景色）,默认无
     */
    private int mDividerSpaceColor;

    public MyRecyclerView(Context context) {
        super(context);
        initView();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.recycle_view);
        isFixSize = typedArray.getBoolean(R.styleable.recycle_view_fixSize, true);
        mSpanCount = typedArray.getInteger(R.styleable.recycle_view_spanCount, DEFAULT_ROW_NUM);
        mType = typedArray.getInt(R.styleable.recycle_view_type, TYPE_LIST);
        mOrientation = typedArray.getInt(R.styleable.recycle_view_orientation, VERTICAL);
        mDividerDraw = typedArray.getDrawable(R.styleable.recycle_view_divider);
        mDividerLeftSpace = typedArray.getInt(R.styleable.recycle_view_dividerLeftSpace, 0);
        mDividerRightSpace = typedArray.getInt(R.styleable.recycle_view_dividerRightSpace, 0);
        mDividerTopSpace = typedArray.getInt(R.styleable.recycle_view_dividerTopSpace, 0);
        mDividerBottomSpace = typedArray.getInt(R.styleable.recycle_view_dividerBottomSpace, 0);
        mDividerSpaceColor = typedArray.getColor(R.styleable.recycle_view_dividerSpaceColor, 0);
        typedArray.recycle();
        initView();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initView() {
        // 布局样式
        switch (mType) {
            case TYPE_LIST:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        getContext(), mOrientation, false);
                this.setLayoutManager(linearLayoutManager);
                break;
            case TYPE_GRID:
                GridLayoutManager gridLayoutManager = new GridLayoutManager(
                        getContext(), mSpanCount, mOrientation, false);
                this.setLayoutManager(gridLayoutManager);
                break;
            case TYPE_STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager =
                        new StaggeredGridLayoutManager(mSpanCount, mOrientation);
                this.setLayoutManager(staggeredGridLayoutManager);
                break;
        }

        // 分割线
        if (null != mDividerDraw) {
            RecyclerVDivider divider = new RecyclerVDivider(mDividerDraw);
            divider.setLeftSpace(UnitConverter.dip2px(getContext(), mDividerLeftSpace));
            divider.setRightSpace(UnitConverter.dip2px(getContext(), mDividerRightSpace));
            divider.setTopSpace(UnitConverter.dip2px(getContext(), mDividerTopSpace));
            divider.setBottomSpace(UnitConverter.dip2px(getContext(), mDividerBottomSpace));
            if (0 != mDividerSpaceColor) divider.setSpaceColor(mDividerSpaceColor);

            this.addItemDecoration(divider);
        }

        // 大小固定
        this.setHasFixedSize(isFixSize);
        // Item出入动画
        this.setItemAnimator(new DefaultItemAnimator());
    }

    public int getSpanCount() {
        return mSpanCount;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public int getType() {
        return mType;
    }
}
