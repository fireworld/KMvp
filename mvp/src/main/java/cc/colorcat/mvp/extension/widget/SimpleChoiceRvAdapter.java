package cc.colorcat.mvp.extension.widget;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by cxx on 2017/11/28.
 * xx.ch@outlook.com
 */
public abstract class SimpleChoiceRvAdapter<T> extends ChoiceRvAdapter {
    private final List<? extends T> mData;
    @LayoutRes
    private final int mLayoutResId;

    public SimpleChoiceRvAdapter(List<? extends T> data, @LayoutRes int layoutResId) {
        mData = data;
        mLayoutResId = layoutResId;
    }

    @Override
    public final int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public final int getItemCount() {
        return mData.size();
    }

    @Override
    public final int getLayoutResId(int viewType) {
        return mLayoutResId;
    }

    @Override
    public void bindView(@NonNull RvHolder holder, int position) {
        bindView(holder, mData.get(position));
    }

    public abstract void bindView(@NonNull RvHolder holder, T data);
}
