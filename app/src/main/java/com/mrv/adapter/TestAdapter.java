package com.mrv.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrv.R;
import com.mrv.bean.TestModel;
import com.mrv.role.BaseRVAdapter;
import com.mrv.role.BaseViewHolder;

import java.util.List;

/**
 * 仅字符串显示的RecycleView适配器
 *
 * @author gzejia 978862664@qq.com
 */
public class TestAdapter extends BaseRVAdapter<TestModel> {

    private int mLayoutId;

    public TestAdapter(Context context, @NonNull List list, int layoutId) {
        super(context, list);
        this.mLayoutId = layoutId;
    }

    @Override
    public int getLayoutId(int viewType) {
        return mLayoutId;
    }

    @Override
    public void onBind(BaseViewHolder holder, int position) {
        TestModel model = getData(position);
        TextView mStrTv = holder.getTextView(R.id.item_tv);

        mStrTv.setText(model.text);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (null != lp && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            boolean isVertical = lp.width == ViewGroup.LayoutParams.MATCH_PARENT;
            if (isVertical) {
                mStrTv.setHeight(model.showHeight);
            } else {
                mStrTv.setWidth(model.showHeight);
            }
            mStrTv.setBackgroundResource(R.drawable.bg_origin_press);
        }
    }
}
