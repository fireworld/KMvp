package cc.colorcat.mvp.presenter

import cc.colorcat.mvp.contract.IList

/**
 * Created by cxx on 2018/3/1.
 * xx.ch@outlook.com
 */
abstract class AbsListPresenter<T> : BasePresenter<IList.View<T>>(), IList.Presenter<T> {

    override fun onCreate(view: IList.View<T>) {
        super.onCreate(view)
        doGetItems()
    }

    override fun doGetItems() {
        realGetItems(null, false)
    }

    override fun toRefreshItems(first: T?) {
        realGetItems(first, false)
    }

    override fun toGetMoreItems(last: T) {
        realGetItems(last, true)
    }

    override fun toDeleteItem(item: T) {
    }

    protected abstract fun realGetItems(item: T?, loadMore: Boolean)
}
