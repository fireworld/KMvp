package cc.colorcat.mvp.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cc.colorcat.mvp.R
import cc.colorcat.mvp.contract.IBase
import cc.colorcat.mvp.extension.hasPermission
import cc.colorcat.mvp.extension.requestPermissionsCompat
import cc.colorcat.mvp.extension.widget.KTip

/**
 * Created by cxx on 18-1-30.
 * xx.ch@outlook.com
 */
abstract class BaseActivity : AppCompatActivity(), IBase.View {
    private companion object {
        const val CODE_REQUEST_PERMISSION = 0x7812
    }

    private var mPermissionListener: PermissionListener? = null
    final override var extras: Bundle? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleExtra(savedInstanceState ?: intent.extras, false)
    }

    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        handleExtra(outState, true)
    }

    final override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        handleExtra(savedInstanceState, false)
    }

    protected fun requestPermissions(permissions: Array<String>, listener: PermissionListener?) {
        mPermissionListener = listener
        val denied = permissions.filter { !hasPermission(it) }
        if (denied.isEmpty()) {
            mPermissionListener?.onAllGranted()
            mPermissionListener = null
        } else {
            requestPermissionsCompat(denied.toTypedArray(), CODE_REQUEST_PERMISSION)
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

    final override val isActive: Boolean
        get() = !isFinishing

    protected open val mTip: KTip by lazy { KTip.from(this, R.layout.network_error, this as? KTip.Listener) }

    final override fun showTip() {
        mTip.showTip()
    }

    final override fun hideTip() {
        mTip.hideTip()
    }

    final override fun isTipShowing(): Boolean = mTip.isShowing

    final override fun toast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    final override fun toast(text: CharSequence) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    protected fun navigateTo(clazz: Class<out BaseActivity>, finish: Boolean = false) {
        startActivity(newIntent(this, clazz))
        if (finish) finish()
    }
}
