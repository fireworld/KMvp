package cc.colorcat.mvp.contract

import cc.colorcat.mvp.entity.Course

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
interface ICourses {
    interface View : IBase.View {
        fun refreshCourses(courses: List<Course>)

        fun stopRefresh()
    }

    interface Presenter : IBase.Presenter<View> {
        fun doGetCourses()

        fun toRefreshCourses()
    }
}
