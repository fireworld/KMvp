package cc.colorcat.mvp.extension.image

import android.graphics.Color
import android.support.annotation.ColorInt
import cc.colorcat.vangogh.CornerTransformation

/**
 * Created by cxx on 18-3-6.
 * xx.ch@outlook.com
 */
class CornerTransformer private constructor(
        cornerRadius: FloatArray?,
        borderWidth: Float,
        @ColorInt borderColor: Int,
        type: Int
) : CornerTransformation(cornerRadius, borderWidth, borderColor, type), Transformer {
    companion object {
        const val TYPE_TL = CornerTransformation.TYPE_TL
        const val TYPE_TR = CornerTransformation.TYPE_TR
        const val TYPE_BR = CornerTransformation.TYPE_BR
        const val TYPE_BL = CornerTransformation.TYPE_BL
        const val TYPE_ALL = TYPE_TL or TYPE_TR or TYPE_BR or TYPE_BL

        @JvmStatic
        fun create(type: Int): CornerTransformer {
            return create(type, 0.0f, Color.WHITE)
        }

        @JvmStatic
        fun create(type: Int, borderWidth: Float, @ColorInt borderColor: Int): CornerTransformer {
            return CornerTransformer(null, borderWidth, borderColor, type)
        }

        @JvmStatic
        fun create(cornerRadius: FloatArray): CornerTransformer {
            return create(cornerRadius, 0.0f, Color.WHITE)
        }

        @JvmStatic
        fun create(cornerRadius: FloatArray, borderWidth: Float, @ColorInt borderColor: Int): CornerTransformer {
            return if (cornerRadius.size != 8) {
                throw IllegalArgumentException("cornerRadius.length != 8")
            } else {
                CornerTransformer(cornerRadius, borderWidth, borderColor, 0)
            }
        }
    }
}
