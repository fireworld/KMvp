package cc.colorcat.mvp.extension

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import java.io.Serializable

/**
 * Created by cxx on 18-1-30.
 * xx.ch@outlook.com
 */
fun Context.hasPermission(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Fragment.hasPermission(permission: String) = ContextCompat.checkSelfPermission(context!!, permission) == PackageManager.PERMISSION_GRANTED

fun Activity.requestPermissionsCompat(permissions: Array<String>, requestCode: Int) = ActivityCompat.requestPermissions(this, permissions, requestCode)

@Suppress("UNCHECKED_CAST")
internal fun <T> Context.getService(service: String): T = this.getSystemService(service) as T

fun bundleOf(vararg pairs: Pair<String, Any>): Bundle {
    val args = Bundle()
    pairs.forEach { (k, v) ->
        when (v) {
            is CharSequence -> args.putCharSequence(k, v)
            is Int -> args.putInt(k, v)
            is Parcelable -> args.putParcelable(k, v)
            is Serializable -> args.putSerializable(k, v)
            is Long -> args.putLong(k, v)
            is Char -> args.putChar(k, v)
            is Float -> args.putFloat(k, v)
            is Double -> args.putDouble(k, v)
            is Short -> args.putShort(k, v)
            is Byte -> args.putByte(k, v)
            else -> throw IllegalArgumentException("unsupported data type, type=${v.javaClass}, value=$v")
        }
    }
    return args
}
