package cc.colorcat.mvp.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cc.colorcat.mvp.R
import cc.colorcat.mvp.contract.IList
import cc.colorcat.mvp.extension.image.ImageOnScrollListener
import cc.colorcat.mvp.extension.widget.KTip
import cc.colorcat.mvp.extension.widget.OnRvItemClickListener
import cc.colorcat.mvp.extension.widget.RvAdapter
import cc.colorcat.mvp.extension.widget.RvOnLoadMoreScrollListener
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Created by cxx on 2018/2/11.
 * xx.ch@outlook.com
 */
abstract class ListFragment<T> : BaseFragment(), IList.View<T>, KTip.Listener {
    override val layoutResId: Int = R.layout.fragment_list
    override val tip: KTip by lazy { KTip.from(rv_items, R.layout.network_error, this) }

    protected open val mItemClickEnabled: Boolean = false
    protected open val mRefreshable = true

    protected abstract val mPresenter: IList.Presenter<T>
    protected val mItems: MutableList<T> = mutableListOf()
    private val mAdapter: RvAdapter by lazy { createAdapter(mItems) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_items.layoutManager = layoutManager(view.context)
        rv_items.adapter = mAdapter
        rv_items.addOnScrollListener(ImageOnScrollListener.get())
        rv_items.addOnScrollListener(object : RvOnLoadMoreScrollListener() {
            override fun onLoadMore() {
                mItems.lastOrNull()?.also { mPresenter.toGetMoreItems(it) }
            }
        })
        if (mItemClickEnabled) {
            rv_items.addOnItemTouchListener(object : OnRvItemClickListener() {
                override fun onItemClick(holder: RecyclerView.ViewHolder) {
                    super.onItemClick(holder)
                    this@ListFragment.onItemClick(mItems[holder.adapterPosition])
                }

                override fun onItemLongClick(holder: RecyclerView.ViewHolder) {
                    super.onItemLongClick(holder)
                    this@ListFragment.onItemLongClick(mItems[holder.adapterPosition])
                }
            })
        }
        if (mRefreshable) {
            srl_root.isEnabled = true
            srl_root.setOnRefreshListener { mPresenter.toRefreshItems(mItems.firstOrNull()) }
        } else {
            srl_root.isEnabled = false
        }
        mPresenter.onCreate(this)
    }

    override fun onDestroyView() {
        mPresenter.onDestroy()
        super.onDestroyView()
    }

    protected open fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    override fun refreshItems(items: List<T>) {
        mItems.clear()
        mItems.addAll(items)
        mAdapter.notifyDataSetChanged()
    }

    override fun addMoreItems(items: List<T>) {
        val size = mItems.size
        mItems.addAll(items)
        mAdapter.notifyItemRangeInserted(size, items.size)
    }

    override fun removeItem(item: T) {
        val removedIndex = mItems.indexOf(item)
        if (removedIndex != -1) {
            mItems.removeAt(removedIndex)
            mAdapter.notifyItemRemoved(removedIndex)
        }
    }

    override fun stopRefreshing() {
        srl_root.isRefreshing = false
    }

    protected open fun onItemClick(item: T) {}

    protected open fun onItemLongClick(item: T) {}

    override fun onTipClick() {
        mPresenter.toRefreshItems(mItems.firstOrNull())
    }

    protected abstract fun createAdapter(items: List<T>): RvAdapter
}
