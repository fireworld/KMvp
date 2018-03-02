package cc.colorcat.mvp.extension.widget

import android.app.Activity
import android.app.Fragment
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by cxx on 2018/3/1.
 * xx.ch@outlook.com
 */
class KTip private constructor(
        private val parent: ViewGroup,
        private val content: View,
        @LayoutRes
        private val tipLayout: Int,
        var listener: Listener?
) {
    companion object {
        @JvmStatic
        fun from(activity: Activity, @LayoutRes tipLayout: Int, listener: Listener?): KTip {
            val parent = activity.findViewById<ViewGroup>(android.R.id.content)
            return KTip(parent, parent.getChildAt(0), tipLayout, listener)
        }

        @JvmStatic
        fun from(fragment: android.support.v4.app.Fragment, @LayoutRes tipLayout: Int, listener: Listener?): KTip {
            return from(fragment.view!!, tipLayout, listener)
        }

        @JvmStatic
        fun from(fragment: Fragment, @LayoutRes tipLayout: Int, listener: Listener?): KTip {
            return from(fragment.view!!, tipLayout, listener)
        }

        @JvmStatic
        fun from(content: View, @LayoutRes tipLayout: Int, listener: Listener?): KTip {
            val parent: ViewGroup = content.parent as? ViewGroup
                    ?: throw NullPointerException("The specified content view must have a parent")
            return KTip(parent, content, tipLayout, listener)
        }
    }

    var isShowing: Boolean = false
        private set
    private var index: Int = 0
    private val tipView: View by lazy {
        val ctx = content.context
        val temp = LayoutInflater.from(ctx).inflate(tipLayout, parent, false)
        val id = ctx.resources.getIdentifier("tip", "id", ctx.packageName)
        temp.findViewById<View>(id)?.setOnClickListener {
            listener?.onTipClick()
        }
        temp
    }

    fun showTip() {
        if (tipView.parent == null) {
            index = parent.indexOfChild(content)
            parent.removeViewAt(index)
            parent.addView(tipView, index, content.layoutParams)
            isShowing = true
        }
    }

    fun hideTip() {
        if (isShowing && content.parent == null) {
            index = parent.indexOfChild(tipView)
            parent.removeViewAt(index)
            parent.addView(content, index)
            isShowing = false
        }
    }

    interface Listener {
        fun onTipClick()
    }
}
