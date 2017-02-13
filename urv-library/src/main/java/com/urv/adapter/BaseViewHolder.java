package com.urv.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * RecycleView，ListView，GridView 通用适配器ViewHolder
 *
 * @author gzejia 978862664@qq.com
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    /**
     * 不重复视图类型View集合
     */
    private SparseArray<View> mViews = new SparseArray<>();

    /**
     * ItemView容器视图
     */
    private View mConvertView;

    /**
     * 构造器
     *
     * @param view ItemView
     */
    public BaseViewHolder(View view) {
        super(view);

        mViews = new SparseArray<>();
        this.mConvertView = itemView;
        mConvertView.setTag(mViews);
    }

    /**
     * 获取实例化View
     *
     * @param viewId ViewId
     * @param <T>    View泛型
     * @return 实例化泛型View
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        try {
            return (T) view;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取实例化ImageView
     *
     * @param id ViewId
     * @return ImageView
     */
    public ImageView getImageView(int id) {
        return getView(id);
    }

    /**
     * 获取实例化TextView
     *
     * @param id ViewId
     * @return TextView
     */
    public TextView getTextView(int id) {
        return getView(id);
    }

    /**
     * 获取实例化Button
     *
     * @param id ViewId
     * @return Button
     */
    public Button getButton(int id) {
        return getView(id);
    }

    /**
     * 实例化ImageView并设置显示图片资源
     *
     * @param id    ViewId
     * @param resId 图片资源Id
     */
    public void setImageResource(int id, int resId) {
        if (resId <= 0) return;
        getImageView(id).setImageResource(resId);
    }

    /**
     * 实例化ImageView并设置显示图片资源
     *
     * @param id  ViewId
     * @param bmp 图片资源Bitmap
     */
    public void setImageResource(int id, Bitmap bmp) {
        if (null == bmp) return;
        getImageView(id).setImageBitmap(bmp);
    }

    /**
     * 实例化TextView并设置显示文本内容
     *
     * @param id           ViewId
     * @param charSequence 字符文本
     */
    public void setTextView(int id, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) charSequence = "";

        getTextView(id).setText(charSequence);
    }

    /**
     * 实例化View并设置是否可见
     *
     * @param id      ViewId
     * @param visible <ul><li>View.VISIBLE</li> <li>View.INVISIBLE</li> <li>View.GONE</li></ul>
     */
    public void setVisibility(int id, int visible) {
        getView(id).setVisibility(visible);
    }

    /**
     * 实例化View并设置是否选中
     *
     * @param id      ViewId
     * @param isCheck true：选中，false：未选中
     */
    public void setCheckStats(int id, boolean isCheck) {
        if (getView(id) instanceof CheckBox) {
            ((CheckBox) getView(id)).setChecked(isCheck);
        } else if (getView(id) instanceof RadioButton) {
            ((RadioButton) getView(id)).setChecked(isCheck);
        }
    }
}