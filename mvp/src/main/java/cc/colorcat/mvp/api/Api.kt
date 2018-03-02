package cc.colorcat.mvp.api

import cc.colorcat.mvp.entity.Course
import cc.colorcat.mvp.extension.net.MListener
import java.io.IOException

/**
 * Created by cxx on 2018/1/31.
 * xx.ch@outlook.com
 */
interface Base<T> {
    @Throws(IOException::class)
    fun execute(): T?

    fun send(listener: MListener<T>?): Any

    fun send(create: () -> MListener<T>?): Any

    fun cancel()
}

interface GetCourses : Base<List<Course>> {
    companion object {
        const val PATH = "/api/teacher"
    }

    fun setType(type: Int): GetCourses

    fun setNumber(number: Int): GetCourses
}
