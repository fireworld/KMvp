package cc.colorcat.mvp.extension.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by cxx on 2018/3/1.
 * xx.ch@outlook.com
 */
public abstract class RvOnPreloadMoreScrollListener extends RecyclerView.OnScrollListener {
    private int mLimit;
    private boolean mUpOnLast = false; // 如果手指是向上滑（即滚动条向下滚）则为 true，否则为 false

    public RvOnPreloadMoreScrollListener() {
        this(4);
    }

    public RvOnPreloadMoreScrollListener(int limit) {
        super();
        mLimit = Math.max(limit, 1);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        int total = manager.getItemCount();
        View view = manager.findViewByPosition(total - mLimit);
        // 如果手指是向上滑（即滚动条向下滚），且倒数第 mLimit 个 item 所对应的 View 在屏幕内（即可见），就加载更多。
        if (mUpOnLast && view != null) {
            onLoadMore();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mUpOnLast = dy > 0; // 记录滚动方向，如果 dy > 0 则表示是 ScrollDown (即手指往上滑，滚动条向下)
    }

    public abstract void onLoadMore();
}
