package cc.colorcat.mvp.extension.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView
import cc.colorcat.vangogh.VanGogh

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
class VanGoghScrollListener private constructor(
        private val listener: AbsListView.OnScrollListener? = null
) : RecyclerView.OnScrollListener(), AbsListView.OnScrollListener {
    companion object {
        val Instance by lazy { VanGoghScrollListener() }
        fun newInstance(listener: AbsListView.OnScrollListener) = VanGoghScrollListener(listener)
    }

    // RecyclerView
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        updateVanGoghStatus(recyclerView.context, newState == RecyclerView.SCROLL_STATE_IDLE)
    }

    // ListView
    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        updateVanGoghStatus(view.context, scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
        listener?.onScrollStateChanged(view, scrollState)
    }

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        listener?.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount)
    }

    private fun updateVanGoghStatus(ctx: Context, idle: Boolean) {
        if (idle) {
            VanGogh.with(ctx).resume()
        } else {
            VanGogh.with(ctx).pause()
        }
    }
}
