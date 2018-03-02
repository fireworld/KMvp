package cc.colorcat.mvp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import cc.colorcat.mvp.extension.L
import cc.colorcat.mvp.extension.image.ImageLoader
import cc.colorcat.mvp.extension.json.fromJson
import cc.colorcat.mvp.extension.json.toJson
import cc.colorcat.mvp.extension.net.ApiService
import java.util.*

/**
 * Created by cxx on 2018/3/1.
 * xx.ch@outlook.com
 */
object ClientHelper {
    val cache = WeakHashMap<String, Any>()

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

    fun set(key: String, value: Any) {
        cache[key] = value
        val json = toJson(value)
        val editor = sharedPreferences.edit()
        editor.putString(key, json)
        editor.apply()
    }

    inline fun <reified T> get(key: String): T? {
        var result: T? = cache[key] as? T
        if (result == null) {
            val json: String = sharedPreferences.getString(key, "")
            if (json.isNotEmpty()) {
                result = fromJson(json, T::class.java)
                if (result != null) {
                    cache[key] = result
                }
            }
        }
        return result
    }

    inline fun <reified T> get(key: String, defaultValue: T): T = get(key) ?: defaultValue

    fun remove(key: String) {
        cache.remove(key)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}
