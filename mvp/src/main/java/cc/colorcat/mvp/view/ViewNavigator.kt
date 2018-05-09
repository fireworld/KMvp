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
        const val EXTRA = "cc.colorcat.mvp.view.ViewNavigator"
    }

    var extra: Bundle?

    fun <T : BaseFragment> newFragment(clazz: Class<T>, vararg pairs: Pair<String, Any>): T {
        return if (pairs.isEmpty()) clazz.newInstance() else newFragment(clazz, bundleOf(*pairs))
    }

    fun <T : BaseFragment> newFragment(clazz: Class<T>, extra: Bundle): T {
        val fragment = clazz.newInstance()
        val args = Bundle(1)
        args.putBundle(ViewNavigator.EXTRA, extra)
        fragment.arguments = args
        return fragment
    }

    fun <T : BaseActivity> newIntent(context: Context, clazz: Class<T>, vararg pairs: Pair<String, Any>): Intent {
        return if (pairs.isEmpty()) Intent(context, clazz) else newIntent(context, clazz, bundleOf(*pairs))
    }

    fun <T : BaseActivity> newIntent(context: Context, clazz: Class<T>, extra: Bundle): Intent {
        val intent = Intent(context, clazz)
        intent.putExtra(ViewNavigator.EXTRA, extra)
        return intent
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getExtra(key: String): T? = extra?.get(key) as T?

    @Suppress("UNCHECKED_CAST")
    fun <T> getExtra(key: String, defaultValue: T): T = extra?.get(key) as? T ?: defaultValue

    fun handleExtra(state: Bundle?, save: Boolean) {
        if (save) {
            extra?.also { state?.putBundle(ViewNavigator.EXTRA, it) }
        } else if (extra == null) {
            extra = state?.getBundle(ViewNavigator.EXTRA)
        }
    }
}
