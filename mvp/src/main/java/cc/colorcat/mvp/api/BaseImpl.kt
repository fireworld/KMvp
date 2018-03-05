package cc.colorcat.mvp.api

import cc.colorcat.kmvp.entity.Result
import cc.colorcat.mvp.extension.net.ApiService
import cc.colorcat.mvp.extension.net.MListener
import cc.colorcat.mvp.extension.net.ResultParser
import cc.colorcat.netbird4.MRequest
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.ParameterizedType

/**
 * Created by cxx on 2018/1/31.
 * xx.ch@outlook.com
 */
abstract class BaseImpl<T> : Base<T> {
    private lateinit var tag: Any

    @Throws(IOException::class)
    override fun execute(): T? {
        val request = builder().build()
        tag = request.tag()
        return ApiService.execute(request)
    }

    override fun send(listener: MListener<T>?): Any {
        val request = builder().listener(listener).build()
        tag = ApiService.send(request)
        return tag
    }

    override fun send(create: () -> MListener<T>?): Any {
        return send(create())
    }

    override fun cancel() {
        if (this::tag.isInitialized) {
            ApiService.cancelWaiting(tag)
        }
    }

    protected abstract fun builder(): MRequest.Builder<T>

    protected fun create(): MRequest.Builder<T> {
        val pt = this::class.java.genericSuperclass as ParameterizedType
        val token = TypeToken.getParameterized(Result::class.java, *pt.actualTypeArguments)
        @Suppress("UNCHECKED_CAST")
        return MRequest.Builder(ResultParser.create(token as TypeToken<Result<T>>))
    }

    companion object {
        @JvmStatic
        protected inline fun <reified R> builderOf(): MRequest.Builder<R> {
            val token = TypeToken.getParameterized(Result::class.java, object : TypeToken<R>() {}.type)
            @Suppress("UNCHECKED_CAST")
            return MRequest.Builder(ResultParser.create(token as TypeToken<Result<R>>))
        }
    }
}
