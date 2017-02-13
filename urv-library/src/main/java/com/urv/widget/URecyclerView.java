package com.urv.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urv.R;
import com.urv.adapter.BaseRVAdapter;


/**
 * <ul><b>重定义RecycleView使用，相应自定义属性如下</b>
 * <li>colors[reference]：下拉刷新颜色变换数组array，默认无</li>
 * <li>isFixSize[boolean]：是否固定列表项视图大小，默认固定</li>
 * <li>loadMoreType[enum]：auto主动加载更多，autoHide主动加载更多并隐藏，click点击加载更多，clickHide点击加载更多并隐藏，默认不提供加载更多</li>
 * <li>emptyType[enum]：none不显示空数据提示，text显示空数据文本提示，默认none</li>
 * <li>spanCount[integer]：网格布局显示列数，默认2列</li>
 * <li>divider_marginLeft[reference]：分隔符对应资源Drawable</li>
 * <li>divider_marginTop[dimension]：分隔符左边距，默认0</li>
 * <li>divider_marginRight[dimension]：分隔符右边距，默认0</li>
 * <li>divider_marginTop[dimension]：分隔符顶部边距，默认0</li>
 * <li>divider_marginBottom[dimension]：分隔符底部边距，默认0</li>
 * <li>divider_background[color]：分隔符边距底色，默认无</li>
 * <li>textNetworkFail[string|reference]：网络错误提示语</li>
 * <li>textDataEmpty[string|reference]：空数据内容提示语</li>
 * <li>layoutManagerType[enum]：list列表类型，grid网格类型，staggeredGrid，瀑布流类型</li>
 * <li>orientation[enum]：vertical垂直方向类型，horizontal水平方向列表</li>
 * </ul>
 *
 * @author gzejia 978862664@qq.com
 */
public class URecyclerView extends RecyclerView implements URecyclerViewInterface {

    private String TAG = this.getClass().toString();
    private static final boolean isDebug = true;

    public static final int TYPE_LIST = 0, TYPE_GRID = 1, TYPE_STAGGERED_GRID = 2;

    /**
     * 列表类型：{@link #TYPE_LIST}（默认）列表类型，{@link #TYPE_GRID} 网格类型，{@link #TYPE_STAGGERED_GRID} 瀑布流类型
     */
    private int mLayoutManagerType;

    public static final int VERTICAL = 1, HORIZONTAL = 0;

    /**
     * 列表方向类型：{@link #VERTICAL}（默认）垂直方向列表类型，{@link #HORIZONTAL} 水平方向列表类型
     */
    private int mOrientation;

    private static final int DEFAULT_ROW_NUM = 2;

    /**
     * GRID与STAGGERED_GRID显示列数，默认{@link #DEFAULT_ROW_NUM} 默认网格列表/瀑布流列表显示列数
     */
    private int mSpanCount;

    public static final int LOADING_MORE_NONE = 0, LOADING_MORE_AUTO = 1,
            LOADING_MORE_AUTO_HIDE = 2, LOADING_MORE_CLICK = 3, LOADING_MORE_CLICK_HIDE = 4;

    /**
     * <ul>上拉加载更多模式</ul>
     * <li>{@link #LOADING_MORE_NONE} 默认无上拉</li>
     * <li>{@link #LOADING_MORE_AUTO} 拖动到底部主动加载</li>
     * <li>{@link #LOADING_MORE_AUTO_HIDE} 拖动到底部主动加载，加载完成时隐藏</li>
     * <li>{@link #LOADING_MORE_CLICK}拖动到底部点击加载</li>
     * <li>{@link #LOADING_MORE_CLICK_HIDE} 拖动到底部点击加载，加载完成时隐藏</li>
     */
    private int mLoadMoreType;

    public static final int DATA_EMPTY_NONE = 0, DATA_EMPTY_TEXT = 1;

    /**
     * <ul>空数据提示类型</ul>
     * <li>{@link #DATA_EMPTY_NONE} 默认无提示 </li>
     * <li>{@link #DATA_EMPTY_TEXT} 仅文本提示 </li>
     */
    private int mDataEmptyType;

    /**
     * 刷新动画颜色数组,(默认无)
     */
    private int[] mColors;

    /**
     * true：Item大小固定，false：Item大小不固定
     */
    private boolean isFixSize = true;

    /**
     * 列表分割线资源
     *
     * @see URecyclerDivider#mDividerDraw
     */
    private Drawable mDividerDraw;

    /**
     * 分割线各方向边距，默认无边距
     */
    private float mDividerLeftSpace, mDividerRightSpace, mDividerTopSpace, mDividerBottomSpace;

    /**
     * 分割线间距颜色（即背景色）,默认无
     */
    private int mDividerSpaceColor;

    /**
     * 加载更多与空数据提示View
     */
    private View mLoadMoreView, mEmptyView;

    private static final String NETWORK_FAIL_HIDE = "奇怪，网络怎么不见了~";
    private static final String DATA_EMPTY_HIDE = "尴尬中，下拉刷新试试吧~";

    /**
     * 网络错误提示语,空数据列表提示语
     */
    public CharSequence mNetworkFailStr, mDataEmptyStr;

    /**
     * 是否启动加载更多----> 是否允许上拉加载更多 ----> 是否加载完成
     */
    private boolean isNeedLoadMore, isCanLoadMore = true, isLoadMoreEnd;

    /**
     * 是否显示空列表数据提示（默认不现实）
     */
    private boolean isShowEmptyHide;

    /**
     * 下拉刷新控件
     */
    private SwipeRefreshLayout mRefreshLayout;

    /**
     * 当前加载页数
     */
    public int mPageIndex = 1;

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public URecyclerView(Context context) {
        super(context);
        initView();
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param attrs   自定义属性
     */
    public URecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.recycle_view);
        int colorResId = typedArray.getResourceId(R.styleable.recycle_view_colors, 0);
        if (colorResId > 0) {
            mColors = getResources().getIntArray(colorResId);
        }

        isFixSize = typedArray.getBoolean(R.styleable.recycle_view_isFixSize, true);

        mLoadMoreType = typedArray.getInt(R.styleable.recycle_view_loadMoreType, LOADING_MORE_NONE);
        isNeedLoadMore = mLoadMoreType != LOADING_MORE_NONE;// 不启动上拉加载更多

        mDataEmptyType = typedArray.getInt(R.styleable.recycle_view_emptyType, DATA_EMPTY_NONE);
        isShowEmptyHide = mDataEmptyType != DATA_EMPTY_NONE;// 不启动空列表数据提醒

        mSpanCount = typedArray.getInteger(R.styleable.recycle_view_spanCount, DEFAULT_ROW_NUM);
        mLayoutManagerType = typedArray.getInt(R.styleable.recycle_view_layoutManagerType, TYPE_LIST);
        mOrientation = typedArray.getInt(R.styleable.recycle_view_orientation, VERTICAL);
        mDividerDraw = typedArray.getDrawable(R.styleable.recycle_view_divider_drawable);
        mDividerLeftSpace = typedArray.getDimension(R.styleable.recycle_view_divider_marginLeft, 0);
        mDividerRightSpace = typedArray.getDimension(R.styleable.recycle_view_divider_marginRight, 0);
        mDividerTopSpace = typedArray.getDimension(R.styleable.recycle_view_divider_marginTop, 0);
        mDividerBottomSpace = typedArray.getDimension(R.styleable.recycle_view_divider_marginBottom, 0);
        mDividerSpaceColor = typedArray.getColor(R.styleable.recycle_view_divider_background, 0);

        mDataEmptyStr = typedArray.getText(R.styleable.recycle_view_textDataEmpty);
        if (TextUtils.isEmpty(mDataEmptyStr)) mDataEmptyStr = DATA_EMPTY_HIDE;

        mNetworkFailStr = typedArray.getText(R.styleable.recycle_view_textNetworkFail);
        if (TextUtils.isEmpty(mNetworkFailStr)) mNetworkFailStr = NETWORK_FAIL_HIDE;

        typedArray.recycle();
        initView();
    }

    /**
     * 构造器
     *
     * @param context  上下文
     * @param attrs    自定义属性
     * @param defStyle 自定义风格
     */
    public URecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setRefreshLayout(SwipeRefreshLayout refreshLayout,
                                 final URecyclerRefreshListener listener) {
        if (null == refreshLayout || null == listener) return;

        mRefreshLayout = refreshLayout;
        mListener = listener;

        if (null != mColors) {
            mRefreshLayout.setColorSchemeColors(mColors);
        }

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (null == mListener) return;
                toLoadMore(isNeedLoadMore);

                mPageIndex = 1;
                mListener.onRefresh();
            }
        });
    }

    @Override
    public void refreshLoadStart(int pageIndex) {
        if (pageIndex > 1) {
            toLoadMore(true);
            toRefresh(false);
        } else {
            toRefresh(true);
            toLoadMore(false);

            if (isURecyclerVAdapter() && null != mEmptyView) {
                ((BaseRVAdapter) getAdapter()).removeHeaderView(mEmptyView);
            }
        }
    }

    @Override
    public void refreshLoadStart() {
        refreshLoadStart(1);
    }

    @Override
    public void refreshLoadSuccess() {
        refreshLoadSuccessIsEnd(false);
    }

    @Override
    public void refreshLoadSuccessIsEnd(boolean isLoadMoreEnd) {
        toRefresh(false);
        toLoadMore(isNeedLoadMore);

        this.isLoadMoreEnd = isLoadMoreEnd;
    }

    @Override
    public void refreshLoadFail() {
        refreshLoadSuccessIsEnd(false);
        mPageIndex = mPageIndex > 1 ? --mPageIndex : 1;
    }

    @Override
    public void disRefresh(boolean isHideRefresh) {
        if (null != mRefreshLayout) {
            mRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void disLoadMore(boolean isDisLoadMore) {
        this.isNeedLoadMore = isDisLoadMore;
    }

    @Override
    public boolean isRefresh() {
        return null != mRefreshLayout && mRefreshLayout.isRefreshing();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        // 避免在 setAdapter() 前执行 setEmptyView()
        if (null != mEmptyView) {
            setEmptyView(mEmptyView);
        } else if (isShowEmptyHide) {
            setEmptyView();
        }

        setEmptyHide(String.valueOf(mNetworkFailStr), String.valueOf(mDataEmptyStr));
    }

    /**
     * 设置加载更多视图
     *
     * @param view
     */
    @Override
    public void setLoadMoreView(View view) {
        mLoadMoreView = view;
    }

    /**
     * 设置加载更多视图（默认）
     */
    @Override
    public void setLoadMoreView() {
        ULoadingView loadingView = new ULoadingView(getContext());

        if (mOrientation == HORIZONTAL) {
            loadingView.setHorizontalShow();
        }

        setLoadMoreView(loadingView);
    }

    @Override
    public void updateLoadMoreShow(int stats) {
        if (!isURecyclerVAdapter() || null == mLoadMoreView || null == mListener) return;

        mListener.loadStats(stats);

        if (mLoadMoreView instanceof ULoadingView) {
            ((ULoadingView) mLoadMoreView).updateLoadStats(stats);
        }

        if (stats == ULoadingView.LOAD_CLICK) {
            mLoadMoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateLoadMoreShow(ULoadingView.LOAD_START);
                }
            });
        } else {
            mLoadMoreView.setOnClickListener(null);

            if (stats == ULoadingView.LOAD_START) {
                mListener.onLoad(++mPageIndex);
                isCanLoadMore = false;
            }
        }

        BaseRVAdapter adapter = ((BaseRVAdapter) getAdapter());

        if (stats == ULoadingView.LOAD_END &&
                (mLoadMoreType == URecyclerView.LOADING_MORE_AUTO_HIDE ||
                        mLoadMoreType == URecyclerView.LOADING_MORE_CLICK_HIDE)) {
            adapter.removeFooterView(mLoadMoreView);
        } else {
            adapter.addFooterView(mLoadMoreView);

            if (stats == ULoadingView.LOAD_END || stats == ULoadingView.LOAD_CLICK) {
                smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        }
    }

    @Override
    public void setEmptyView(View emptyView) {
        isShowEmptyHide = true;

        if (!isURecyclerVAdapter()) {
            this.mEmptyView = emptyView;
        } else {
            BaseRVAdapter adapter = ((BaseRVAdapter) getAdapter());
            adapter.mEmptyView = emptyView;

            adapter.mEmptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 下拉刷新的不需要点击重新加载更多
                    if (null == mListener || null != mRefreshLayout) return;

                    mListener.onRefresh();
                }
            });
        }
    }

    @Override
    public void setEmptyView() {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        textView.setTextColor(ULoadingView.sDefaultColor);
        textView.setPadding(0, 0, 0, 80);

        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        setEmptyView(textView);
    }

    /**
     * 设置提示语
     *
     * @param networkFailStr 网络异常时提示
     * @param dataEmptyStr   空数据列表提示
     */
    public void setEmptyHide(String networkFailStr, String dataEmptyStr) {
        mNetworkFailStr = networkFailStr;
        mDataEmptyStr = dataEmptyStr;

        if (isURecyclerVAdapter()) {
            BaseRVAdapter adapter = (BaseRVAdapter) getAdapter();
            adapter.mNetworkFailStr = networkFailStr;
            adapter.mDataEmptyStr = dataEmptyStr;
        }
    }

    /**
     * 设置提示语
     *
     * @param networkFailId 网络异常时提示
     * @param dataEmptyId   空数据列表提示
     */
    public void setEmptyHide(int networkFailId, int dataEmptyId) {
        Resources resources = getContext().getResources();

        setEmptyHide(resources.getString(networkFailId), resources.getString(dataEmptyId));
    }

    /**
     * 实例化列表类型
     */
    private void initView() {
        // 布局样式
        switch (mLayoutManagerType) {
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
            URecyclerDivider divider = new URecyclerDivider(mDividerDraw);
            divider.setLeftSpace(mDividerLeftSpace);
            divider.setRightSpace(mDividerRightSpace);
            divider.setTopSpace(mDividerTopSpace);
            divider.setBottomSpace(mDividerBottomSpace);
            if (0 != mDividerSpaceColor) divider.setSpaceColor(mDividerSpaceColor);

            this.addItemDecoration(divider);
        }

        // 滑动至底部加载更多监听
        this.addOnScrollListener(mScroll);
        // 大小固定
        this.setHasFixedSize(isFixSize);
        // Item出入动画
        this.setItemAnimator(new DefaultItemAnimator());
    }

    private boolean isCanLoadMore() {
        if (!isNeedLoadMore) {
            if (isDebug) {
                Log.w(TAG, "URecyclerView: 未启动上拉加载更多");
            }
            return false;
        }

        if (!isCanLoadMore) {
            if (isDebug) {
                Log.w(TAG, "URecyclerView: 当前正在刷新或者已经是加载更多");
            }
            return false;
        }

        if (null == mListener) {
            if (isDebug) {
                Log.w(TAG, "URecyclerView: 未设置上下拉监听");
            }
            return false;
        }

        if (null == mRefreshLayout || mRefreshLayout.isRefreshing()) {
            if (isDebug) {
                Log.w(TAG, "URecyclerView: 未设置下拉刷新或正在执行刷新");
            }
            return false;
        }

        if (!isURecyclerVAdapter()) {
            if (isDebug) {
                Log.w(TAG, "URecyclerView: 未使用指定适配器GRecyclerAdapter");
            }
            return false;
        }
        return true;
    }

    private boolean isURecyclerVAdapter() {
        return null != getAdapter() && getAdapter() instanceof BaseRVAdapter;
    }

    /**
     * 刷新操作的控制
     *
     * @param isRefresh true_启动下拉刷新，禁止上拉加载更多, false_取消下拉刷新，允许上拉加载更多
     */
    private void toRefresh(boolean isRefresh) {
        if (null == mRefreshLayout) return;

        if (isURecyclerVAdapter()) {
            BaseRVAdapter adapter = ((BaseRVAdapter) getAdapter());
            adapter.removeHeaderView(adapter.mEmptyView);
            adapter.removeFooterView(mLoadMoreView);
        }

        if (isRefresh && null != mListener) {
            mPageIndex = 1;

            mRefreshLayout.setRefreshing(true);
            mListener.onRefresh();
        } else {
            mRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 上拉加载的控制
     *
     * @param isLoad true_可上拉加载更多, false_取消上拉加载更多
     */
    private void toLoadMore(boolean isLoad) {
        if (null != getAdapter() && getAdapter() instanceof BaseRVAdapter
                && ((BaseRVAdapter) getAdapter()).getAll().isEmpty()) {
            // 列表数据空的情况下不允许上拉加载
            isCanLoadMore = false;
        } else {
            isCanLoadMore = isLoad;
        }
    }

    /**
     * 获取列表显示列数
     *
     * @return 列数
     */
    public int getSpanCount() {
        return mSpanCount;
    }

    /**
     * 获取列表方向类型
     *
     * @return 方向类型：{@link #VERTICAL}，{@link #HORIZONTAL}
     */
    public int getOrientation() {
        return mOrientation;
    }

    /**
     * 获取列表类型
     *
     * @return 类型：{@link #TYPE_LIST}，{@link #TYPE_GRID}，{@link #TYPE_STAGGERED_GRID}
     */
    public int getLayoutManagerType() {
        return mLayoutManagerType;
    }

    private URecyclerRefreshListener mListener;

    public interface URecyclerRefreshListener {

        void onRefresh();

        void onLoad(int pageIex);

        /**
         * @param stats {@link ULoadingView } LOAD_START_加载更多，LOAD_CLICK_点击加载更多，LOAD_CLICK_已无更多加载
         */
        void loadStats(int stats);
    }

    OnScrollListener mScroll = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            /**
             * 参考实现：http://www.loongwind.com/archives/189.html
             */
            // 得到当前显示的最后一个item的view
            View lastChildView = recyclerView.getLayoutManager().getChildAt(
                    recyclerView.getLayoutManager().getChildCount() - 1);
            if (null == lastChildView) return;

            // 得到lastChildView的bottom坐标值
            int lastChildBottom = mOrientation == VERTICAL ?
                    lastChildView.getBottom() : lastChildView.getRight();

            // 得到 RecyclerView 的底部坐标减去底部 padding 值，也就是显示内容最底部的坐标
            int recyclerBottom = mOrientation == VERTICAL ?
                    (recyclerView.getBottom() - recyclerView.getPaddingBottom()) :
                    (recyclerView.getRight() - recyclerView.getPaddingRight());

            // 通过这个 lastChildView 得到这个 view 当前的 position 值
            int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

            // 判断lastChildView的bottom值跟recyclerBottom
            // 判断lastPosition是不是最后一个position
            // 如果两个条件都满足则说明是真正的滑动到了底部
            if (isCanLoadMore() &&
                    lastChildBottom == recyclerBottom &&
                    lastPosition == recyclerView.getLayoutManager().getItemCount() - 1 &&
                    isURecyclerVAdapter()) {

                final BaseRVAdapter adapter = (BaseRVAdapter) getAdapter();
                if (adapter.getAll().isEmpty()) return;

                if (null == mLoadMoreView) {
                    setLoadMoreView();
                }

                if (isLoadMoreEnd) {
                    updateLoadMoreShow(ULoadingView.LOAD_END);
                } else {
                    updateLoadMoreShow(mLoadMoreType == LOADING_MORE_AUTO ||
                            mLoadMoreType == LOADING_MORE_AUTO_HIDE ?
                            ULoadingView.LOAD_START : ULoadingView.LOAD_CLICK);
                }
            }
        }
    };
}
