package com.urv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallBeatIndicator;

/**
 * 自定义底部加载更多
 *
 * @author gzejia 978862664@qq.com
 */
public class ULoadingView extends LinearLayout {

    private static final int sTextPadding = 8;// dp
    private static final int sLayoutPadding = 10;// dp
    private static final int sDefaultFontSize = 14;// sp
    private static final int sDefaultAnimWidth = 25;// dp
    private static final int sDefaultAnimHeight = 25;// dp
    public static final int sDefaultColor = Color.argb(255, 170, 170, 170);

    private String[] mLoadStr = {"加载更多", "点击加载更多", "已无更多加载"};

    public static final int LOAD_START = 0;// 加载更多，默认
    public static final int LOAD_CLICK = 1;// 点击加载更多
    public static final int LOAD_END = 2;// 已无更多加载

    // 当前动画加载状态
    private int mLoadStats = LOAD_START;

    // 提示文本内容
    private TextView mContentTv;

    // 提示动画
    private View mAnimV;

    public ULoadingView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ULoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ULoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        // 获取主题背景色
        TypedArray array = context.getTheme().obtainStyledAttributes(
                new int[]{
                        android.R.attr.colorBackground
                });

        ViewGroup.LayoutParams parentParam = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(parentParam);
        int layoutPadding = dip2px(context, sLayoutPadding);
        this.setGravity(Gravity.CENTER);
        this.setPadding(0, layoutPadding, 0, layoutPadding);
        this.setBackgroundColor(array.getColor(0, 0xFF00FF));
        this.setOrientation(HORIZONTAL);

        ViewGroup.LayoutParams animParam = new ViewGroup.LayoutParams(
                dip2px(context, sDefaultAnimWidth), dip2px(context, sDefaultAnimHeight));

        // 默认加载动画
        AVLoadingIndicatorView indicatorView = new AVLoadingIndicatorView(getContext());
        indicatorView.setIndicatorColor(sDefaultColor);
        indicatorView.setIndicator(new BallBeatIndicator());
        indicatorView.setLayoutParams(animParam);
        mAnimV = indicatorView;
        this.addView(mAnimV);

        mContentTv = new TextView(context);
        ViewGroup.LayoutParams contentParam = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentTv.setLayoutParams(contentParam);
        int txtPadding = dip2px(context, sTextPadding);
        mContentTv.setPadding(txtPadding, txtPadding, txtPadding, txtPadding);
        mContentTv.setText(mLoadStr[LOAD_START]);
        mContentTv.setTextColor(sDefaultColor);
        mContentTv.setTextSize(sDefaultFontSize);
        this.addView(mContentTv);
    }

    /**
     * dip转换到px
     *
     * @param context  上下文
     * @param dipValue 转换数值
     * @return px
     */
    private static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 修改加载动画以及提示文本颜色
     *
     * @param color Color
     */
    public void setLoadColor(int color) {
        if (null != mContentTv) {
            mContentTv.setTextColor(color);
        }
        if (null != mAnimV && mAnimV instanceof AVLoadingIndicatorView) {
            ((AVLoadingIndicatorView) mAnimV).setIndicatorColor(color);
        }
    }

    /**
     * 更新加载提示内容
     *
     * @param stats {@link #LOAD_START} 加载更多，{@link #LOAD_CLICK} 点击加载更多，{@link #LOAD_END} 已无更多加载
     */
    public void updateLoadStats(int stats) {
        this.mLoadStats = stats;

        mContentTv.setText(mLoadStr[mLoadStats]);
        mAnimV.setVisibility(isLoadStart() ? VISIBLE : GONE);
    }

    /**
     * 动画状态
     *
     * @return true_正在加载，false_已停止加载
     */
    public boolean isLoadStart() {
        return mLoadStats == LOAD_START;
    }

    /**
     * 设置加载更多内容为水平列表展示
     */
    public void setHorizontalShow() {
        ViewGroup.LayoutParams parentParam = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.setLayoutParams(parentParam);
        int dipPadding = dip2px(getContext(), sLayoutPadding);
        this.setPadding(dipPadding, 0, dipPadding, 0);
        this.setOrientation(VERTICAL);
    }
}

