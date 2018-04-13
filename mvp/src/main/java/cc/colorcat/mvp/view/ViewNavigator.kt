package cc.colorcat.mvp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cc.colorcat.mvp.extension.bundleOf

/**
 * 用于 Fragment 的实例生成和 Activity 的跳转，主要解决传入数据的自动保存与恢复。
 * 目前仅支持 Number, Char, CharSequence, Parcelable 和 Serializable 这几类数据。
 * 不建议传入过大的数据如 Bitmap 等，数据过大可能会丢失，传输较大的数据建议使用通信类框架。
 *
 * Created by cxx on 18-2-28.
 * xx.ch@outlook.com
 */
interface ViewNavigator {
    companion object {
        const val EXTRAS = "cc.colorcat.mvp.view.ViewNavigator"
    }

    var extras: Bundle?

    fun <T : BaseFragment> newFragment(clazz: Class<T>, vararg pairs: Pair<String, Any>): T {
        val fragment = clazz.newInstance()
        if (!pairs.isEmpty()) {
            val args = Bundle(1)
            args.putBundle(ViewNavigator.EXTRAS, bundleOf(*pairs))
            fragment.arguments = args
        }
        return fragment
    }

    fun <T : BaseActivity> newIntent(context: Context, clazz: Class<T>, vararg pairs: Pair<String, Any>): Intent {
        val intent = Intent(context, clazz)
        if (!pairs.isEmpty()) {
            intent.putExtra(ViewNavigator.EXTRAS, bundleOf(*pairs))
        }
        return intent
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getExtra(key: String): T? = extras?.get(key) as T?

    @Suppress("UNCHECKED_CAST")
    fun <T> getExtra(key: String, defaultValue: T): T = extras?.get(key) as? T ?: defaultValue

    fun handleExtra(state: Bundle?, save: Boolean) {
        if (save) {
            extras?.also { state?.putBundle(ViewNavigator.EXTRAS, it) }
        } else if (extras == null) {
            extras = state?.getBundle(ViewNavigator.EXTRAS)
        }
    }
}
