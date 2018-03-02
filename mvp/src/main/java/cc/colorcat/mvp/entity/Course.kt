package cc.colorcat.mvp.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by cxx on 2018/1/31.
 * xx.ch@outlook.com
 */
data class Course(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("picSmall") val picSmallUrl: String,
        @SerializedName("picBig") val picBigUrl: String,
        @SerializedName("description") val description: String,
        @SerializedName("learner") val numOfLearner: Int
)
