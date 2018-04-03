package cc.colorcat.mvp.extension.net

import cc.colorcat.netbird4.*
import java.io.IOException
import java.nio.charset.Charset

/**
 * for android studio 3.1
 *
 * Created by cxx on 2018/4/3.
 * xx.ch@outlook.com
 */
open class LoggingInterceptor(private val charsetIfAbsent: Charset = Charset.forName("UTF-8")) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        synchronized(TAG) {
            val logBuilder = StringBuilder()

            logBuilder.append(" \n").append(HALF_LINE).append(request.method().name).append(HALF_LINE)
                    .append("\nrequest url --> ").append(request.url())
            appendPair(logBuilder, "request header --> ", request.headers())

            if (request.method().needBody()) {
                appendPair(logBuilder, "request parameter --> ", request.parameters())
                appendFile(logBuilder, "request file --> ", request.fileBodies())
            }

            logBuilder.append("\n\nresponse --> ").append(response.responseCode()).append("--").append(response.responseMsg())
            appendPair(logBuilder, "response header --> ", response.headers())
            val body = response.responseBody()
            if (body != null) {
                val contentType = body.contentType()
                if (contentType != null && contentFilter(contentType)) {
                    val bytes = body.bytes()
                    val charset: Charset = body.charset() ?: charsetIfAbsent
                    val content = String(bytes, charset)
                    logBuilder.append("\nresponse content --> ").append(content)
                    val newBody = ResponseBody.create(bytes, contentType, charset)
                    response = response.newBuilder()
                            .setHeader(Headers.CONTENT_LENGTH, newBody.contentLength().toString())
                            .responseBody(newBody)
                            .build()
                }
            }
            logBuilder.append('\n').append(LINE)
            log(logBuilder.toString(), Level.INFO)
            return response
        }
    }

    protected open fun contentFilter(contentType: String): Boolean {
        return contentType.matches(".*(charset|text|html|htm|json|urlencoded)+.*".toRegex())
    }

    companion object {
        private val TAG = NetBird::class.java.simpleName
        private val LINE = buildString(80, '=')
        private val HALF_LINE = buildString(38, '=')

        @JvmStatic
        private fun appendPair(builder: StringBuilder, prefix: String, reader: PairReader) {
            for (nv in reader) {
                builder.append('\n').append(prefix).append(nv.name).append(" = ").append(nv.value)
            }
        }

        @JvmStatic
        private fun appendFile(builder: StringBuilder, prefix: String, fileBodies: List<FileBody>) {
            for (body in fileBodies) {
                builder.append('\n').append(prefix).append(body.toString())
            }
        }

        @JvmStatic
        private fun log(msg: String, level: Level) {
            Platform.get().logger().log(TAG, msg, level)
        }

        @JvmStatic
        private fun buildString(count: Int, c: Char): String {
            val builder = StringBuilder()
            for (i in 0 until count) {
                builder.append(c)
            }
            return builder.toString()
        }
    }
}