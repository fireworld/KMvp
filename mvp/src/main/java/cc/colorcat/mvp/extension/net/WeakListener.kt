package cc.colorcat.mvp.extension.net

import android.support.annotation.CallSuper
import cc.colorcat.mvp.contract.IBase
import cc.colorcat.netbird4.HttpStatus
import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
abstract class WeakListener<R, in V : IBase.View>(view: V?) : MListener<R> {
    private val reference: Reference<V> = WeakReference(view)

    final override fun onStart() {
        reference.get()?.also { if (it.isActive) onStart(it) }
    }

    final override fun onSuccess(result: R) {
        reference.get()?.also { if (it.isActive) onSuccess(it, result) }
    }

    final override fun onFailure(code: Int, msg: String) {
        reference.get()?.also { if (it.isActive) onFailure(it, code, msg) }
    }

    final override fun onFinish() {
        reference.get()?.also {
            if (it.isActive) onFinish(it)
            reference.clear()
        }
    }

    open fun onStart(view: V) {}

    abstract fun onSuccess(view: V, data: R)

    @CallSuper
    open fun onFailure(view: V, code: Int, msg: String) {
        when (code) {
            HttpStatus.CODE_CONNECT_ERROR -> view.showTip()
        }
    }

    open fun onFinish(view: V) {}
}
