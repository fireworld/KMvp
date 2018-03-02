package cc.colorcat.mvp.extension.net

import cc.colorcat.mvp.IClient
import cc.colorcat.netbird4.Level
import cc.colorcat.netbird4.MRequest
import cc.colorcat.netbird4.NetBird
import cc.colorcat.netbird4.android.AndroidPlatform
import cc.colorcat.netbird4.logging.LoggingTailInterceptor
import java.io.IOException

/**
 * Created by cxx on 18-2-8.
 * xx.ch@outlook.com
 */
object ApiService {
    private lateinit var netBird: NetBird

    fun init(client: IClient) {
        val builder = NetBird.Builder(client.baseUrl)
                .platform(AndroidPlatform())
                .connectTimeOut(10000)
                .readTimeOut(10000)
                .enableGzip(true)
                .logLevel(if (client.debug) Level.VERBOSE else Level.NOTHING)
        if (client.debug) {
            builder.addTailInterceptor(LoggingTailInterceptor())
        }
        netBird = builder.build()
    }

    @Throws(IOException::class)
    fun <T> execute(request: MRequest<T>): T? {
        return netBird.execute(request)
    }

    fun <T> send(request: MRequest<T>): Any {
        return netBird.send(request)
    }

    fun cancelWaiting(tag: Any) {
        netBird.cancelWaiting(tag)
    }
}
