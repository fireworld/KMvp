package cc.colorcat.mvp.presenter

import cc.colorcat.mvp.api.GetCoursesImpl
import cc.colorcat.mvp.contract.ICourses
import cc.colorcat.mvp.entity.Course
import cc.colorcat.mvp.extension.net.WeakListener

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
class CoursesPresenter : BasePresenter<ICourses.View>(), ICourses.Presenter {
    private val mService = GetCoursesImpl()

    override fun onCreate(view: ICourses.View) {
        super.onCreate(view)
        doGetCourses()
    }

    override fun doGetCourses() {
        realGetCourses()
    }

    override fun toRefreshCourses() {
        realGetCourses()
    }

    private fun realGetCourses() {
        mService.setType(4).setNumber(30).send {
            object : WeakListener<List<Course>, ICourses.View>(mView) {
                override fun onSuccess(view: ICourses.View, data: List<Course>) {
                    view.refreshCourses(data)
                }

                override fun onFinish(view: ICourses.View) {
                    view.stopRefresh()
                }
            }
        }
    }
}
