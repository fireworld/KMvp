package cc.colorcat.mvp.extension.net

import cc.colorcat.netbird4.*
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by cxx on 2018/3/30.
 * xx.ch@outlook.com
 */
open class LoggingInterceptor(private val charsetIfAbsent: Charset = Charset.forName("UTF-8")) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        synchronized(TAG) {
            val log = StringBuilder()
                    .append(" ").append('\n').append(HALF_LINE).append(request.method().name).append(HALF_LINE)
                    .append('\n').append("request url --> ").append(request.url())
                    .append(logPair("request header", request.headers(), Level.DEBUG))


//            log(HALF_LINE + request.method().name + HALF_LINE, Level.DEBUG)
//            log("request url = " + request.url(), Level.DEBUG)
//            logPair("request header", request.headers(), Level.DEBUG)
            if (request.method().needBody()) {
                log.append(logPair("request parameter", request.parameters(), Level.DEBUG))
                        .append(logFile(request.fileBodies(), Level.DEBUG))

//                logPair("request parameter", request.parameters(), Level.DEBUG)
//                logFile(request.fileBodies(), Level.DEBUG)
            }
//            log(requestLog.toString(), Level.DEBUG)

            val responseLog = StringBuilder()
            log.append("response --> ").append(response.responseCode()).append("--").append(response.responseMsg())
                    .append(logPair("response header", response.headers(), Level.INFO))
//            log(responseLog.toString(), Level.INFO)

//            log("response --> " + response.responseCode() + "--" + response.responseMsg(), Level.INFO)
//            logPair("response header", response.headers(), Level.INFO)
            val body = response.responseBody()
            if (body != null) {
                val contentType = body.contentType()
                if (contentType != null && contentFilter(contentType)) {
                    val bytes = body.bytes()
                    var charset: Charset? = body.charset()
                    if (charset == null) charset = charsetIfAbsent
                    val content = String(bytes, charset)
//                    log("response content --> $content", Level.WARN)
                    log.append('\n').append("response content --> ").append(content)
                    val newBody = ResponseBody.create(bytes, contentType, charset)
                    response = response.newBuilder()
                            .setHeader(Headers.CONTENT_LENGTH, java.lang.Long.toString(newBody.contentLength()))
                            .responseBody(newBody)
                            .build()
                }
            }
            log.append('\n').append(LINE)
            log(log.toString(), Level.INFO)
//            log(responseLog.toString(), Level.INFO)
//            log(LINE, Level.INFO)
            return response
        }
    }

    protected open fun contentFilter(contentType: String): Boolean {
        return contentType.matches(".*(charset|text|html|htm|json|urlencoded)+.*".toRegex())
    }

    companion object {
        private val TAG = NetBird::class.java.simpleName
        private val LINE = buildString(80, '-')
        private val HALF_LINE = buildString(38, '-')

        private fun logPair(type: String, reader: PairReader, level: Level): String {
            val log = StringBuilder()
            for (nv in reader) {
                log.append('\n').append(type).append(" --> ").append(nv.name).append(" = ").append(nv.value)
//                val msg = type + " --> " + nv.name + " = " + nv.value
//                log(msg, level)
            }
            return log.toString()
        }

        private fun logFile(fileBodies: List<FileBody>, level: Level): String {
            val log = StringBuilder()
            for (body in fileBodies) {
                log.append('\n').append("request file --> ").append(body.toString())
//                log("request file --> $body", level)
            }
            return log.toString()
        }

        private fun log(msg: String, level: Level) {
            Platform.get().logger().log(TAG, msg, level)
        }

        private fun buildString(count: Int, c: Char): String {
            val builder = StringBuilder()
            for (i in 0 until count) {
                builder.append(c)
            }
            return builder.toString()
        }
    }
}