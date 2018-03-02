package cc.colorcat.mvp.presenter

import android.support.annotation.CallSuper
import cc.colorcat.mvp.contract.IBase

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
abstract class BasePresenter<V : IBase.View> : IBase.Presenter<V> {
    protected var mView: V? = null

    @CallSuper
    override fun onCreate(view: V) {
        mView = view
    }

    @CallSuper
    override fun onDestroy() {
        mView = null
    }
}
