package cc.colorcat.mvp.extension.image

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import cc.colorcat.mvp.IClient
import cc.colorcat.mvp.extension.getService
import cc.colorcat.vangogh.Task
import cc.colorcat.vangogh.VanGogh
import java.io.File

/**
 * Created by cxx on 18-2-7.
 * xx.ch@outlook.com
 */
class ImageLoader private constructor(private val creator: Task.Creator) {
    companion object {
        @JvmStatic
        fun init(client: IClient) {
            val ctx = client.context
            val debug = client.debug
            val usableSpace = ctx.getService<ActivityManager>(Context.ACTIVITY_SERVICE).memoryClass
            val memoryCacheSize = Math.min((usableSpace * 1024 * 1024 / 7).toLong(), 1024L * 1024L * 20L)

            val size = Point()
            ctx.getService<WindowManager>(Context.WINDOW_SERVICE).defaultDisplay.getSize(size)
            val maxSize = Math.min(size.x, size.y)
            val global = Task.Options()
            global.maxSize(maxSize, maxSize)
            global.config(Bitmap.Config.RGB_565)

            val cacheDir = ctx.externalCacheDir
            val maxCacheSize = Math.min(300L * 1024L * 1024L, (cacheDir.usableSpace * 0.1).toLong())

            val vanGogh = VanGogh.Builder(ctx)
                    .connectTimeOut(10000)
                    .readTimeOut(10000)
                    .fade(true)
                    .memoryCacheSize(memoryCacheSize)
                    .defaultOptions(global)
                    .debug(debug)
                    .enableLog(debug)
//                    .defaultLoading(R.mipmap.ic_launcher_round)
//                    .defaultError(R.mipmap.ic_launcher)
                    .diskCache(cacheDir)
                    .diskCacheSize(maxCacheSize)
                    .build()
            VanGogh.setSingleton(vanGogh)
        }

        @JvmStatic
        fun releaseMemory() {
            VanGogh.get().clear()
            VanGogh.get().releaseMemory()
        }

        @JvmStatic
        fun createUri(@DrawableRes resId: Int): Uri = Uri.parse("vangogh://resource?id=$resId")

        @JvmStatic
        fun load(url: String?) = ImageLoader(VanGogh.get().load(url))

        @JvmStatic
        fun load(@DrawableRes resId: Int) = ImageLoader(VanGogh.get().load(resId))

        @JvmStatic
        fun load(file: File?) = ImageLoader(VanGogh.get().load(file))

        @JvmStatic
        fun load(uri: Uri?) = ImageLoader(VanGogh.get().load(uri))
    }

    fun config(config: Bitmap.Config): ImageLoader {
        creator.config(config)
        return this
    }

    fun addTransformer(transformer: Transformer): ImageLoader {
        creator.addTransformation(transformer)
        return this
    }

    fun loading(@DrawableRes loadingResId: Int): ImageLoader {
        creator.loading(loadingResId)
        return this
    }

    fun loading(loading: Drawable): ImageLoader {
        creator.loading(loading)
        return this
    }

    fun error(@DrawableRes errorResId: Int): ImageLoader {
        creator.error(errorResId)
        return this
    }

    fun error(error: Drawable): ImageLoader {
        creator.error(error)
        return this
    }

    fun maxSize(maxWidth: Int, maxHeight: Int): ImageLoader {
        creator.maxSize(maxWidth, maxHeight)
        return this
    }

    fun resize(width: Int, height: Int): ImageLoader {
        creator.resize(width, height)
        return this
    }

    fun listener(listener: ImageListener?): ImageLoader {
        creator.callback(listener)
        return this
    }

    fun fetch() {
        creator.fetch()
    }

    fun fetch(listener: ImageListener?) {
        creator.fetch(listener)
    }

    fun <V : View> into(target: ImageTarget<V>) {
        creator.into(target)
    }

    fun into(view: ImageView) {
        creator.into(view)
    }
}
