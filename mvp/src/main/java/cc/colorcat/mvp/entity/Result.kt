package cc.colorcat.kmvp.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
data class Result<T>(
        @SerializedName("status") val status: Int,
        @SerializedName("msg") val msg: String,
        @SerializedName("data") val data: T?
) {
    companion object {
        const val STATUS_OK = 1
    }
}
