package cc.colorcat.mvp.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cc.colorcat.mvp.R
import cc.colorcat.mvp.contract.IBase
import cc.colorcat.mvp.extension.hasPermission
import cc.colorcat.mvp.extension.widget.KTip

/**
 * Created by cxx on 2018/1/30.
 * xx.ch@outlook.com
 */
abstract class BaseFragment : Fragment(), IBase.View {
    private companion object {
        const val CODE_REQUEST_PERMISSION = 0X2319
    }

    private var mPermissionListener: PermissionListener? = null
    private var mActive = false
    final override var extras: Bundle? = null
    @LayoutRes
    protected open val layoutResId: Int = -1

    protected fun requestPermissions(permissions: Array<String>, listener: PermissionListener?) {
        activity?.apply {
            mPermissionListener = listener
            val denied = permissions.filter { !hasPermission(it) }
            if (denied.isEmpty()) {
                mPermissionListener?.onAllGranted()
                mPermissionListener = null
            } else {
                this@BaseFragment.requestPermissions(denied.toTypedArray(), CODE_REQUEST_PERMISSION)
            }
        }
    }

    final override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionListener?.apply {
            if (requestCode == CODE_REQUEST_PERMISSION) {
                val denied = grantResults.indices.filter { grantResults[it] != PackageManager.PERMISSION_GRANTED }.map { permissions[it] }
                if (denied.isEmpty()) {
                    onAllGranted()
                } else {
                    onDenied(denied.toTypedArray())
                }
            }
            mPermissionListener = null
        }
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = super.onCreateView(inflater, container, savedInstanceState)
        handleExtra(savedInstanceState ?: arguments, false)
        if (layoutResId != -1) view = inflater.inflate(layoutResId, container, false)
        return view
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActive = true
    }

    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        handleExtra(outState, true)
    }

    final override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        handleExtra(savedInstanceState, false)
    }

    @CallSuper
    override fun onDestroyView() {
        mActive = false
        super.onDestroyView()
    }

    final override val isActive: Boolean
        get() = mActive

    /**
     * 此处务必使用延迟初始化：一则避免在不需要的时候初始化；二则在 View 初始化之前初始化 KTip 可能会异常。
     *
     * 默认采用 [KTip.from(Fragment, int, KTip.Listener)] 创建，此种方式在 ViewPager 等中可能无法正常显示甚至崩溃，
     * 建议覆盖此实现，使用 [KTip.from(View, int, KTip.Listener)] 创建，选择合适的需要覆盖的 View 以显示 KTip.
     */
    protected open val mTip: KTip by lazy { KTip.from(this, R.layout.network_error, this as? KTip.Listener) }

    final override fun showTip() {
        mTip.showTip()
    }

    final override fun hideTip() {
        mTip.hideTip()
    }

    final override fun isTipShowing(): Boolean = mTip.isShowing

    final override fun toast(resId: Int) {
        if (isActive) {
            context?.also { Toast.makeText(it, resId, Toast.LENGTH_SHORT).show() }
        }
    }

    final override fun toast(text: CharSequence) {
        if (isActive) {
            context?.also { Toast.makeText(it, text, Toast.LENGTH_SHORT).show() }
        }
    }

    final override fun finish() {
        activity?.finish()
    }

    protected fun navigateTo(clazz: Class<out BaseActivity>, finish: Boolean = false) {
        activity?.also {
            it.startActivity(newIntent(it, clazz))
            if (finish) it.finish()
        }
    }
}
