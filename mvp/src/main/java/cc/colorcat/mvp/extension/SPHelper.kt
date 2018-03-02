package cc.colorcat.mvp.extension

import cc.colorcat.mvp.ClientHelper
import cc.colorcat.mvp.extension.json.fromJson
import cc.colorcat.mvp.extension.json.toJson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by cxx on 18-2-5.
 * xx.ch@outlook.com
 */
class SPHelper private constructor() {
    companion object {
        @JvmStatic
        val cache = WeakHashMap<String, Any>()

        @JvmStatic
        fun set(key: String, value: Any) {
            cache[key] = value
            val json = toJson(value)
            val editor = ClientHelper.sharedPreferences.edit()
            editor.putString(key, json)
            editor.apply()
        }

        @JvmStatic
        inline fun <reified T> get(key: String, defaultValue: T): T = get(key) ?: defaultValue

        @JvmStatic
        inline fun <reified T> get(key: String): T? {
            var result: T? = cache[key] as? T
            if (result == null) {
                val json: String = ClientHelper.sharedPreferences.getString(key, "")
                if (json.isNotEmpty()) {
                    result = fromJson(json, object : TypeToken<T>() {}.type)
                    if (result != null) {
                        cache[key] = result
                    }
                }
            }
            return result
        }

        @JvmStatic
        fun remove(key: String) {
            cache.remove(key)
            val editor = ClientHelper.sharedPreferences.edit()
            editor.remove(key)
            editor.apply()
        }
    }
}
