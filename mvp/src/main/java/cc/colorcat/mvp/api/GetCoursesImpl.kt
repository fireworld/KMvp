package cc.colorcat.mvp.api

import cc.colorcat.mvp.entity.Course
import cc.colorcat.netbird4.MRequest

/**
 * Created by cxx on 2018/1/31.
 * xx.ch@outlook.com
 */
class GetCoursesImpl : BaseImpl<List<Course>>(), GetCourses {
    private var type = 4
    private var number = 30

    override fun setType(type: Int): GetCourses {
        this.type = type
        return this
    }

    override fun setNumber(number: Int): GetCourses {
        this.number = number
        return this
    }

    override fun builder(): MRequest.Builder<List<Course>> {
        return create()
                .path(GetCourses.PATH)
                .get()
                .add("type", type.toString())
                .add("num", number.toString())
    }
}
