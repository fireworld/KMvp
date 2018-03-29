package cc.colorcat.mvp.view

import android.Manifest
import android.os.Bundle
import android.view.View
import cc.colorcat.mvp.R
import cc.colorcat.mvp.extension.Const
import kotlinx.android.synthetic.main.activity_launch.*

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        btn_show_courses.setOnClickListener(mClick)
        btn_show_courses_with_fragment.setOnClickListener(mClick)
        btn_request_permission.setOnClickListener(mClick)
    }

    private val mClick = View.OnClickListener {
        when {
            it.id == R.id.btn_show_courses -> {
                navigateTo(CoursesActivity::class.java)
            }
            it.id == R.id.btn_show_courses_with_fragment -> {
                startActivity(newIntent(this@LaunchActivity, ContainerActivity::class.java, Const.key.container_layout to R.layout.activity_container_courses))
            }
            it.id == R.id.btn_request_permission -> {
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
}
