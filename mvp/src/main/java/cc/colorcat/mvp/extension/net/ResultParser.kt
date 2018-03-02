package cc.colorcat.mvp.extension.net

import cc.colorcat.kmvp.entity.Result
import cc.colorcat.mvp.extension.json.fromJson
import cc.colorcat.netbird4.NetworkData
import cc.colorcat.netbird4.Parser
import cc.colorcat.netbird4.Response
import cc.colorcat.netbird4.StateIOException
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

/**
 * Created by cxx on 2018/1/31.
 * xx.ch@outlook.com
 */
class ResultParser<T> private constructor(private val token: TypeToken<Result<T>>) : Parser<T> {
    companion object {
        @JvmStatic
        fun <T> create(token: TypeToken<Result<T>>): ResultParser<T> = ResultParser(token)
    }

    override fun parse(response: Response): NetworkData<out T> {
        try {
            val content = response.responseBody().string()
            val result: Result<T> = fromJson(content, token.type)
            if (result.status == Result.Companion.STATUS_OK && result.data != null) {
                return NetworkData.newSuccess(result.data)
            }
            return NetworkData.newFailure(result.status, result.msg)
        } catch (e: JsonParseException) {
            throw StateIOException(response.responseCode(), response.responseMsg(), e)
        }
    }
}
