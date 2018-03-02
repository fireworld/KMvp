package cc.colorcat.mvp.contract

/**
 * Created by cxx on 2018/2/11.
 * xx.ch@outlook.com
 */
interface IList {
    interface View<in T> : IBase.View {
        fun refreshItems(items: List<T>)

        fun addMoreItems(items: List<T>)

        fun removeItem(item: T)

        fun toItemDetail(item: T)

        fun stopRefreshing()
    }

    interface Presenter<T> : IBase.Presenter<View<T>> {
        fun doGetItems()

        fun toRefreshItems(first: T?)

        fun toGetMoreItems(last: T)

        fun toDeleteItem(item: T)
    }
}
