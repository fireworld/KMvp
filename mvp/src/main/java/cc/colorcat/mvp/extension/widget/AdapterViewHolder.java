package cc.colorcat.mvp.extension.widget;

import android.view.View;

/**
 * Created by cxx on 17-8-14.
 * xx.ch@outlook.com
 */
public class AdapterViewHolder extends ViewHolder {
    protected int mViewType;
    protected int mPosition;

    protected AdapterViewHolder(View root) {
        super(root);
    }

    public AdapterViewHolder setViewType(int viewType) {
        mViewType = viewType;
        return this;
    }

    public int getViewType() {
        return mViewType;
    }

    public AdapterViewHolder setPosition(int position) {
        mPosition = position;
        return this;
    }

    public int getPosition() {
        return mPosition;
    }
}
