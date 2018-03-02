package cc.colorcat.mvp.extension.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 用于 {@link RecyclerView} 加载更多。
 * 在最后一个 item 可见并且滑动已停止时提示加载更多。
 * <p>
 * Created by cxx on 17-6-30.
 * xx.ch@outlook.com
 */
public abstract class RvOnLoadMoreScrollListener extends RecyclerView.OnScrollListener {
    private boolean mUpOnLast = false; // 如果手指是向上滑（即滚动条向下滚）则为 true，否则为 false

    public RvOnLoadMoreScrollListener() {
        super();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        int total = manager.getItemCount();
        View view = manager.findViewByPosition(total - 1);
        // 如果手指是向上滑（即滚动条向下滚），且最后一个 item 所对应的 View 在屏幕内（即可见），且当前没有滚动就加载更多。
        if (mUpOnLast && view != null && newState == RecyclerView.SCROLL_STATE_IDLE) {
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
