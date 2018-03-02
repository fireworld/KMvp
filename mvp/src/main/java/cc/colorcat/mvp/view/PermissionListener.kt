package cc.colorcat.mvp.view

/**
 * Created by cxx on 18-1-30.
 * xx.ch@outlook.com
 */
interface PermissionListener {

    fun onAllGranted()

    fun onDenied(permissions: Array<String>)
}
