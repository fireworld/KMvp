package cc.colorcat.mvp.extension.image

import android.graphics.Bitmap
import android.graphics.Matrix
import android.support.annotation.ColorInt

import cc.colorcat.vangogh.CircleTransformation

/**
 * Created by cxx on 17-11-24.
 * xx.ch@outlook.com
 */
class ResizeCircleTransformer : CircleTransformation, Transformer {
    private val width: Int
    private val height: Int

    private constructor(width: Int, height: Int) : super() {
        this.width = width
        this.height = height
    }

    private constructor(width: Int, height: Int, borderWidth: Float, @ColorInt borderColor: Int) : super(borderWidth, borderColor) {
        this.width = width
        this.height = height
    }

    override fun transform(source: Bitmap): Bitmap {
        val bitmap = super.transform(source)
        val reqWidth = this.width
        val reqHeight = this.height
        val width = bitmap.width
        val height = bitmap.height
        if (reqWidth != width || reqHeight != height) {
            val matrix = Matrix()
            val scaleX = reqWidth.toFloat() / width
            val scaleY = reqHeight.toFloat() / height
            val scale = Math.min(scaleX, scaleY)
            matrix.postScale(scale, scale)
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        }
        return bitmap
    }

    companion object {
        @JvmStatic
        fun create(width: Int, height: Int): ResizeCircleTransformer {
            checkSize(width, height)
            return ResizeCircleTransformer(width, height)
        }

        @JvmStatic
        fun create(width: Int, height: Int, borderWidth: Float, @ColorInt borderColor: Int): ResizeCircleTransformer {
            checkSize(width, height)
            return ResizeCircleTransformer(width, height, borderWidth, borderColor)
        }

        @JvmStatic
        private fun checkSize(width: Int, height: Int) {
            if (width <= 0 || height <= 0) {
                throw IllegalArgumentException("width and height must be positive")
            }
        }
    }
}
