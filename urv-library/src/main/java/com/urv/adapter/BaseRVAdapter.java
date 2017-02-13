package com.urv.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.urv.NetworkUtil.NetworkUtil;
import com.urv.widget.URecyclerDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gzejia 978862664@qq.com
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseViewHolder>
        implements RvDataInterface<T>, RvViewInterface {

    /**
     * 上下文
     */
    public Context mContext;

    /**
     * true：启动列表项更新动画，false：关闭列表项更新动画
     */
    public boolean isCloseItemAnim;

    /**
     * 网络错误提示语,空数据列表提示语
     */
    public String mNetworkFailStr, mDataEmptyStr;

    /**
     * 列表项数据集合
     */
    private List<T> mList = new ArrayList<>();

    /**
     * 列表头部视图
     */
    public List<View> mHeaderViews = new ArrayList<>();

    /**
     * 列表底部视图
     */
    public List<View> mFooterViews = new ArrayList<>();

    /**
     * 空数据提示内容视图
     */
    public View mEmptyView;

    public BaseRVAdapter(Context context, List<T> list) {
        if (null == list) return;

        this.mList = list;
        this.mContext = context;
    }

    @Override
    public void add(T object) {
        add(mList.size(), object);
    }

    @Override
    public void add(int position, T object) {
        mList.add(position, object);

        int index = getInsert(position);

        if (isCloseItemAnim) {
            notifyDataSetChanged();
        } else {
            notifyItemChanged(index - 1 > 0 ? index - 1 : 0);
            notifyItemInserted(index);
            notifyItemRangeChanged(index, mList.size());
        }
    }

    @Override
    public void add(List<T> list) {
        add(mList.size(), list);
    }

    @Override
    public void add(int position, List<T> list) {
        mList.addAll(position, list);

        int index = getInsert(position);

        if (isCloseItemAnim) {
            notifyDataSetChanged();
        } else {
            notifyItemChanged(index - 1 > 0 ? index - 1 : 0);
            notifyItemRangeInserted(index, list.size());
            notifyItemRangeChanged(index, mList.size() - index);
        }

        updateEmptyViewShow();
    }

    @Override
    public void removeAll() {
        if (mList.isEmpty()) return;

        if (isCloseItemAnim) {
            mList.clear();
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(getInsert(0), mList.size());
            mList.clear();
        }
    }

    @Override
    public void remove(T object) {
        for (int i = 0, k = mList.size(); i < k; i++) {
            if (mList.get(i).equals(object)) {
                remove(i);
                return;
            }
        }
    }

    @Override
    public void remove(int position) {
        if (mList.isEmpty()) return;

        if (isCloseItemAnim) {
            mList.remove(position);
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(getInsert(position));
            mList.remove(position);
            notifyItemRangeChanged(getInsert(position), getItemCount() - 1);
        }
    }

    @Override
    public T get(int position) {
        return mList.get(position - mHeaderViews.size());
    }

    @Override
    public List<T> getAll() {
        return mList;
    }

    @Override
    public void updateAll(List<T> list) {
        if (null == list) {
            mList.clear();
        } else {
            mList = list;
        }

        notifyDataSetChanged();
        updateEmptyViewShow();
    }

    @Override
    public void update(int position, T object) {
        if (null == object) {
            remove(position);
            return;
        }

        mList.set(position, object);

        if (isCloseItemAnim) {
            notifyDataSetChanged();
        } else {
            notifyItemChanged(getInsert(position));
        }
    }

    @Override
    public void updateForPage(int pageIndex, List<T> list) {
        if (pageIndex > 1) {// 1_首页
            add(list);
        } else {
            updateAll(list);
        }
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Override
    public void addHeaderView(View headerView) {
        if (null != headerView && !mHeaderViews.contains(headerView)) {
            mHeaderViews.add(headerView);

            notifyItemInserted(0);
            notifyItemRangeChanged(0, getItemCount() - 1);
        }
    }

    @Override
    public void removeHeaderView(View headerView) {
        if (null == mHeaderViews || !mHeaderViews.contains(headerView)) return;

        for (int i = 0, k = mHeaderViews.size(); i < k; i++) {

            if (mHeaderViews.get(i) == headerView) {
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, getItemCount() - i);
                mHeaderViews.remove(i);
                return;
            }
        }
    }

    @Override
    public void addFooterView(View footerView) {
        if (null != footerView && !mFooterViews.contains(footerView)) {
            mFooterViews.add(footerView);

            notifyItemInserted(getInsert(mList.size()));
            notifyItemRangeChanged(getInsert(mList.size()), 1);
        }
    }

    @Override
    public void removeFooterView(View footerView) {
        if (footerView == null || !mFooterViews.contains(footerView)) return;

        for (int i = 0, k = mFooterViews.size(); i < k; i++) {

            if (mFooterViews.get(i) == footerView) {
                int index = mHeaderViews.size() + mList.size() + i;
                notifyItemRemoved(index);
                notifyItemRangeChanged(index, mFooterViews.size() - i);
                mFooterViews.remove(i);
                return;
            }
        }
    }

    @Override
    public void removeAllFooterView() {
        if (mFooterViews.isEmpty()) return;

        int index = mHeaderViews.size() + mList.size();
        notifyItemRangeRemoved(index, mFooterViews.size());
        mFooterViews.clear();
    }

    @Override
    public void updateEmptyViewShow() {
        if (mList.isEmpty() && null != mEmptyView) {
            if (mEmptyView instanceof TextView) {
                ((TextView) mEmptyView).setText(!NetworkUtil.isConnected(mContext) ?
                        mNetworkFailStr : mDataEmptyStr);
            }

            addHeaderView(mEmptyView);
            removeAllFooterView();
        } else {
            removeHeaderView(mEmptyView);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + mHeaderViews.size() + mFooterViews.size();
    }

    private int getInsert(int position) {
        position = position >= mList.size() ? mList.size() : position >= 0 ? position : 0;

        return position + (mHeaderViews.contains(mEmptyView) ?
                mHeaderViews.size() - 1 : mHeaderViews.size());
    }

    /**
     * 标记Item类型为常规数据类型
     */
    private static final int TYPE_NORMAL = 0;

    /**
     * 标记Item类型为头部视图类型
     */
    private static final int TYPE_HEADER = 1;

    /**
     * 标记Item类型为底部视图类型
     */
    private static final int TYPE_FOOTER = -1;

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaderViews.size()) {
            return TYPE_HEADER + position;
        } else if (position >= mHeaderViews.size() + mList.size()) {
            int beforeSize = mHeaderViews.size() + mList.size();
            return TYPE_FOOTER - (position - beforeSize);
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType > TYPE_NORMAL) {
            return new BaseViewHolder(mHeaderViews.get(viewType - 1));
        } else if (viewType <= TYPE_FOOTER) {
            return new BaseViewHolder(mFooterViews.get(-viewType - 1));
        } else {
            View view = LayoutInflater.from(mContext).inflate(getLayoutId(viewType), parent, false);
            return new BaseViewHolder(view);
        }
    }

    /**
     * 获取列表项视图布局Id
     *
     * @param layoutId
     * @return
     */
    public abstract int getLayoutId(int layoutId);

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (getItemViewType(position) != TYPE_NORMAL) return;

        onBind(holder, position - mHeaderViews.size(), get(position));

        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.itemClick(position);
                }
            });
        }

        if (null != mOnItemLongClickListener) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.itemSelect(position);
                    return true;
                }
            });
        }
    }

    /**
     * 子类获取绑定列表项视图信息
     *
     * @param holder   对应列表项ViewHolder
     * @param position 对应列表项索引
     */
    public abstract void onBind(BaseViewHolder holder, int position, T model);

    @Override
    public void onViewRecycled(final BaseViewHolder holder) {
        super.onViewRecycled(holder);
    }

    /**
     * RecycleView外部视图依赖插入
     *
     * @param recyclerView 列表视图
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) != TYPE_NORMAL ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * true：添加分割符，false：不添加分割符
     *
     * @see URecyclerDivider#getItemState(RecyclerView.Adapter)
     */
    public boolean isMainItem;

    /**
     * 获取GridLayoutManager/StaggeredGridLayoutManager当前Item项所处的列数(行数)
     *
     * @see URecyclerDivider#getSpanIndex(RecyclerView.Adapter)
     */
    public int mSpanIndex;


    /**
     * RecycleView外部插入视图显示处理
     *
     * @param holder 列表视图项对应ViewHolder
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (null == lp) return;
        isMainItem = getItemViewType(holder.getLayoutPosition()) == TYPE_NORMAL;

        if (lp instanceof GridLayoutManager.LayoutParams) {
            GridLayoutManager.LayoutParams p = (GridLayoutManager.LayoutParams) lp;
            mSpanIndex = p.getSpanIndex();
        } else if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            mSpanIndex = p.getSpanIndex();
            p.setFullSpan(!isMainItem);
        }
    }

    /**
     * 自定义列表项点击操作监听
     */
    private OnItemClickListener mOnItemClickListener;

    /**
     * 添加列表项点击监听
     *
     * @param listener 实例化监听
     */
    public void addItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 列表项点击监听回调处理
     */
    public interface OnItemClickListener {
        void itemClick(int position);
    }

    /**
     * 自定义列表项长按操作监听
     */
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * 添加列表项长按监听
     *
     * @param listener 实例化监听
     */
    public void addItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    /**
     * 列表项长按监听回调处理
     */
    public interface OnItemLongClickListener {
        void itemSelect(int position);
    }
}