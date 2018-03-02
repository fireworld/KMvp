package cc.colorcat.mvp.contract

import android.support.annotation.StringRes
import cc.colorcat.mvp.view.ViewNavigator

/**
 * Created by cxx on 18-1-30.
 * xx.ch@outlook.com
 */
interface IBase {
    interface View : ViewNavigator {
        val isActive: Boolean

        fun showTip()

        fun hideTip()

        fun isTipShowing(): Boolean

        fun toast(@StringRes resId: Int)

        fun toast(text: CharSequence)

        fun finish()
    }

    interface Presenter<in V : View> {
        fun onCreate(view: V)

        fun onDestroy()
    }
}
