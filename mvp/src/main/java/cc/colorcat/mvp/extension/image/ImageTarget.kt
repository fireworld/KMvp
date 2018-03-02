package cc.colorcat.mvp.extension.image

import android.view.View

import cc.colorcat.vangogh.ViewTarget

/**
 * Created by cxx on 18-2-7.
 * xx.ch@outlook.com
 */
abstract class ImageTarget<V : View> protected constructor(view: V, tag: Any) : ViewTarget<V>(view, tag)
