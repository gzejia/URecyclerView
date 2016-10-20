package com.mrv.role;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecycleView 适配器，参考实现 <link>http://blog.csdn.net/qibin0506/article/details/49716795</link>
 *
 * @author gzejia 978862664@qq.com
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = -1;

    public Context mContext;
    public List<T> mList = new ArrayList<>();
    public List<View> mHeaderViews = new ArrayList<>();
    public List<View> mFooterViews = new ArrayList<>();
    public boolean isCloseItemAnim;

    public BaseRVAdapter(Context context, @NonNull List<T> list) {
        this.mList = list;
        this.mContext = context;
    }

    public void updateAdapter(@NonNull List<T> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        // notifyItemRangeChanged(startIndex(0), mList.size());
    }

    public T getData(int position) {
        return mList.get(position);
    }

    public void updateData(int position, T object) {
        mList.set(position, object);

        if (isCloseItemAnim) {
            notifyDataSetChanged();
        } else {
            int updateIndex = startIndex(position);
            notifyItemChanged(updateIndex);
            notifyItemRangeChanged(updateIndex, mList.size());
        }
    }

    public void cleanData() {
        if (!isCloseItemAnim) {
            mList.clear();
            notifyDataSetChanged();
        } else {
            int cleanIndex = mHeaderViews.size();
            notifyItemRangeRemoved(cleanIndex, mList.size());
            mList.clear();
            notifyItemRangeChanged(cleanIndex, mList.size());
        }
    }

    public void addData(int position, T object) {
        int startIndex = startIndex(position);
        mList.add(position, object);

        if (isCloseItemAnim) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(startIndex);
            notifyItemRangeChanged(startIndex, mList.size());
        }
    }

    public void addDataLs(@NonNull List<T> list) {
        int addIndex = startIndex(mList.size());
        mList.addAll(list);

        if (isCloseItemAnim) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(addIndex, list.size());
            notifyItemRangeChanged(addIndex, mList.size());
        }
    }

    public void addDataLs(final int position, @NonNull List<T> list) {
        mList.addAll(position, list);

        if (isCloseItemAnim) {
            notifyDataSetChanged();
        } else {
            int addIndex = startIndex(position);
            notifyItemRangeInserted(addIndex, list.size());
            notifyItemRangeChanged(addIndex, mList.size());
        }
    }

    public void removeData(int position) {
        if (mList.size() <= position) return;

        if (isCloseItemAnim) {
            mList.remove(position);
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(startIndex(position));
            mList.remove(position);
            notifyItemRangeChanged(startIndex(position), mList.size());
        }
    }

    public void addHeaderView(@NonNull View headerView) {
        mHeaderViews.add(headerView);
        notifyDataSetChanged();
    }

    public void addHeaderViews(@NonNull List<View> headerViews) {
        mHeaderViews.addAll(headerViews);
        notifyDataSetChanged();
    }

    public void addFooterView(@NonNull View footerView) {
        mFooterViews.add(footerView);
        notifyDataSetChanged();
    }

    public void addFooterViews(@NonNull List<View> footerViews) {
        mFooterViews.addAll(footerViews);
        notifyDataSetChanged();
    }

    private int startIndex(int doneIndex) {
        return doneIndex + mHeaderViews.size();
    }

    /**
     * {@link #TYPE_HEADER} 头部列表
     * {@link #TYPE_NORMAL} 正常列表
     * {@link #TYPE_FOOTER} 底部列表
     */
    @Override
    public int getItemViewType(int position) {
        if (!mHeaderViews.isEmpty() && position < mHeaderViews.size()) {
            return TYPE_HEADER + position;
        } else if (!mFooterViews.isEmpty() && position >= mHeaderViews.size() + mList.size()) {
            int beforeSize = mHeaderViews.size() + mList.size();
            return TYPE_FOOTER - (position - beforeSize);
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        if ((null == mList || mList.isEmpty())) {
            return null == mHeaderViews ? 0 : mHeaderViews.size()
                    + (null == mFooterViews ? 0 : mFooterViews.size());
        } else {
            return mHeaderViews.size() + mList.size() + mFooterViews.size();
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!mHeaderViews.isEmpty() && viewType > TYPE_NORMAL) {
            return new BaseViewHolder(mHeaderViews.get(viewType - 1));
        } else if (!mFooterViews.isEmpty() && viewType <= TYPE_FOOTER) {
            return new BaseViewHolder(mFooterViews.get(-viewType - 1));
        } else {
            View view = LayoutInflater.from(mContext).inflate(getLayoutId(viewType),
                    parent, false);
            return new BaseViewHolder(view);
        }
    }

    public abstract int getLayoutId(int viewType);

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        if (getItemViewType(position) != TYPE_NORMAL) return;
        onBind(holder, position - mHeaderViews.size());

        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.itemSelect(
                            holder.getAdapterPosition() - mHeaderViews.size());
                }
            });
        }
    }

    public abstract void onBind(BaseViewHolder holder, int position);

    @Override
    public void onViewRecycled(final BaseViewHolder holder) {
        super.onViewRecycled(holder);
    }

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
     * @see RecyclerVDivider
     * <ul>判断是否添加分割符的标识
     * <li>true_添加，false_不添加</li></ul>
     * Title：另外使用分割符的情况下可删除该变量
     */
    public boolean isMainItem;

    /**
     * @see RecyclerVDivider
     * 获取GridLayoutManager/StaggeredGridLayoutManager当前Item项所处的列数(行数)
     * 提示：另外使用分割符的情况下可删除该变量
     */
    public int mSpanIndex;

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

    private OnItemClickListener mOnItemClickListener;

    public void addItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void itemSelect(int position);
    }
}