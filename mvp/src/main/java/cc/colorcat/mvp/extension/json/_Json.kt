package cc.colorcat.mvp.extension.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.lang.reflect.Type

/**
 * Created by cxx on 18-2-8.
 * xx.ch@outlook.com
 */
val gson: Gson by lazy {
    GsonBuilder()
            .serializeNulls()
            .registerTypeAdapterFactory(NullStringAdapterFactory())
            .registerTypeAdapterFactory(NullMultiDateAdapterFactory("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"))
            .registerTypeAdapterFactory(NullArrayTypeAdapterFactory())
            .registerTypeAdapterFactory(NullCollectionTypeAdapterFactory())
            .create()
}

fun toJson(names: List<String>, values: List<String>): String {
    if (names.size != values.size) throw IllegalArgumentException("names.size != values.size")
    val obj = JsonObject()
    for (i in names.indices) {
        obj.addProperty(names[i], values[i])
    }
    return obj.toString()
}

fun toJson(obj: Any): String = gson.toJson(obj)

fun <T> fromJson(json: String, clazz: Class<T>): T = gson.fromJson(json, clazz)

fun <T> fromJson(json: String, typeOfT: Type): T = gson.fromJson(json, typeOfT)

inline fun <reified T> fromJson(json: String): T = gson.fromJson(json, T::class.java)
