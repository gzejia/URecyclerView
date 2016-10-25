package com.mrv.role;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView,GridView 通用Adapter
 *
 * @author gzejia 978862664@qq.com
 */
public abstract class BaseLGAdapter<T> extends BaseAdapter {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 数据集合
     */
    private List<T> mList = new ArrayList<>();

    /**
     * 构造器
     *
     * @param context 上下文
     * @param list    数据集合
     */
    public BaseLGAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list;
    }

    /**
     * 更新数据
     *
     * @param list 数据集合
     */
    public void updateAdapter(@NonNull List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void cleanAdapter() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param position 添加位置
     * @param data     数据对象
     */
    public void addData(int position, T data) {
        this.mList.add(position, data);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param list 数据集合
     */
    public void addDataLs(@NonNull List<T> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加数据集
     *
     * @param position 添加位置
     * @param list     数据集合
     */
    public void addDataLs(int position, @NonNull List<T> list) {
        this.mList.addAll(position, list);
        notifyDataSetChanged();
    }

    /**
     * 移除数据
     *
     * @param position 移除位置
     */
    public void removeData(int position) {
        if (mList.size() <= position) return;
        this.mList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 移除数据
     *
     * @param data 数据对象
     */
    public void removeData(T data) {
        this.mList.remove(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.size() > position ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
        onBind(new BaseViewHolder(view), position);
        return view;
    }

    protected abstract int getLayoutId();

    protected abstract void onBind(BaseViewHolder holder, int position);
}