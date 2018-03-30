package cc.colorcat.mvp.extension.image

import android.support.v7.widget.RecyclerView
import android.widget.AbsListView

/**
 * Created by cxx on 18-2-7.
 * xx.ch@outlook.com
 */
class ImageOnScrollListener private constructor(
        private val listener: AbsListView.OnScrollListener?
) : RecyclerView.OnScrollListener(), AbsListView.OnScrollListener {
    companion object {
        @JvmStatic
        fun get() = ImageOnScrollListener(null)

        @JvmStatic
        fun newInstance(listener: AbsListView.OnScrollListener) = ImageOnScrollListener(listener)
    }

    // RecyclerView
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        updateVanGoghStatus(newState == RecyclerView.SCROLL_STATE_IDLE)
    }

    // ListView
    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        updateVanGoghStatus(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
        listener?.onScrollStateChanged(view, scrollState)
    }

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        listener?.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount)
    }

    private fun updateVanGoghStatus(idle: Boolean) {
        if (idle) {
            ImageLoader.resume()
        } else {
            ImageLoader.pause()
        }
    }
}
