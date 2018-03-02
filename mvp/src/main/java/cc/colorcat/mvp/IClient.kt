package cc.colorcat.mvp

import android.content.Context

/**
 * Created by cxx on 18-3-2.
 * xx.ch@outlook.com
 */
interface IClient {
    val context: Context

    val baseUrl: String

    val debug: Boolean

    fun toast(text: CharSequence)
}
