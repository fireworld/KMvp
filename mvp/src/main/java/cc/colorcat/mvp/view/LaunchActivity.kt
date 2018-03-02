package cc.colorcat.mvp.view

import android.Manifest
import android.os.Bundle
import cc.colorcat.mvp.R
import kotlinx.android.synthetic.main.activity_launch.*

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        btn_show_courses.setOnClickListener { navigateTo(CoursesActivity::class.java) }
        btn_request_permission.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), object : PermissionListener {
                override fun onAllGranted() {
                    toast("All permission granted")
                }

                override fun onDenied(permissions: Array<String>) {
                    toast(permissions.contentToString())
                }
            })
        }
    }
}
