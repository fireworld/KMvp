package cc.colorcat.mvp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import cc.colorcat.mvp.extension.L
import cc.colorcat.mvp.extension.image.ImageLoader
import cc.colorcat.mvp.extension.net.ApiService

/**
 * Created by cxx on 2018/3/1.
 * xx.ch@outlook.com
 */
object ClientHelper {
    lateinit var client: IClient
        private set
    val sharedPreferences: SharedPreferences
        get() = client.context.getSharedPreferences("client", Context.MODE_PRIVATE)

    fun init(client: IClient) {
        this.client = client
        ImageLoader.init(client)
        ApiService.init(client)
        L.Threshold = if (client.debug) Log.VERBOSE else 1000
    }

    fun onLowMemory() {
        ImageLoader.releaseMemory()
    }
}
