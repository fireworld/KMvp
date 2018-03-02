package cc.colorcat.mvp.extension

import android.util.Log

/**
 * Created by cxx on 18-2-5.
 * xx.ch@outlook.com
 */
class L private constructor() {
    companion object {
        private const val TAG = "temp"
        @JvmStatic
        internal var Threshold: Int = Log.VERBOSE

        @JvmStatic
        fun v(msg: String, tag: String = TAG) = log(tag, msg, Log.VERBOSE)

        @JvmStatic
        fun d(msg: String, tag: String = TAG) = log(tag, msg, Log.DEBUG)

        @JvmStatic
        fun i(msg: String, tag: String = TAG) = log(tag, msg, Log.INFO)

        @JvmStatic
        fun w(msg: String, tag: String = TAG) = log(tag, msg, Log.WARN)

        @JvmStatic
        fun e(msg: String, tag: String = TAG) = log(tag, msg, Log.ERROR)

        @JvmStatic
        fun e(e: Throwable) {
            if (Log.ERROR >= Threshold) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        private fun log(tag: String, msg: String, level: Int) {
            if (level >= Threshold) {
                Log.println(level, tag, msg)
            }
        }
    }
}
