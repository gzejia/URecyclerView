package com.mrv.role;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews = new SparseArray<>();
    private View mConvertView;

    public BaseViewHolder(View itemView) {
        super(itemView);

        mViews = new SparseArray<>();
        this.mConvertView = itemView;
        mConvertView.setTag(mViews);
    }

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

    public ImageView getImageView(int id) {
        return getView(id);
    }

    public TextView getTextView(int id) {
        return getView(id);
    }

    public Button getButton(int id) {
        return getView(id);
    }

    public void setImageResource(int id, int resId) {
        getImageView(id).setImageResource(resId);
    }

    public void setImageResource(int id, Bitmap bmp) {
        getImageView(id).setImageBitmap(bmp);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }

    public void setVisibility(int id, boolean isVisibility) {
        getView(id).setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }
}