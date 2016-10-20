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

    private Context mContext;
    private List<T> mList = new ArrayList<>();

    public BaseLGAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void updateAdapter(@NonNull List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void cleanData(int position, T data) {
        this.mList.clear();
        notifyDataSetChanged();
    }

    public void addData(int position, T data) {
        this.mList.add(position, data);
        notifyDataSetChanged();
    }

    public void addDataLs(@NonNull List<T> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addDataLs(int position, @NonNull List<T> list) {
        this.mList.addAll(position, list);
        notifyDataSetChanged();
    }

    public void removeData(int position, T data) {
        if (mList.size() <= position) return;
        this.mList.add(position, data);
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