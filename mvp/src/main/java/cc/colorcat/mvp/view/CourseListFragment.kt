package cc.colorcat.mvp.view

import android.graphics.Color
import android.util.TypedValue
import cc.colorcat.mvp.R
import cc.colorcat.mvp.api.GetCourses
import cc.colorcat.mvp.api.GetCoursesImpl
import cc.colorcat.mvp.contract.IList
import cc.colorcat.mvp.entity.Course
import cc.colorcat.mvp.extension.image.CornerTransformer
import cc.colorcat.mvp.extension.image.ImageLoader
import cc.colorcat.mvp.extension.net.WeakListener
import cc.colorcat.mvp.extension.widget.RvAdapter
import cc.colorcat.mvp.extension.widget.RvHolder
import cc.colorcat.mvp.extension.widget.SimpleAutoChoiceRvAdapter
import cc.colorcat.mvp.presenter.AbsListPresenter

/**
 * Created by cxx on 2018/3/1.
 * xx.ch@outlook.com
 */
class CourseListFragment : ListFragment<Course>() {
    override val mPresenter: IList.Presenter<Course> by lazy {
        object : AbsListPresenter<Course>() {
            private val mService: GetCourses = GetCoursesImpl()

            override fun toGetMoreItems(last: Course) {
            }

            override fun realGetItems(item: Course?, loadMore: Boolean) {
                mService.setType(4).setNumber(30).send {
                    object : WeakListener<List<Course>, IList.View<Course>>(mView) {
                        override fun onSuccess(view: IList.View<Course>, data: List<Course>) {
                            view.hideTip()
                            view.refreshItems(data)
                        }

                        override fun onFinish(view: IList.View<Course>) {
                            super.onFinish(view)
                            view.stopRefreshing()
                        }
                    }
                }
            }
        }
    }

    override fun createAdapter(items: List<Course>): RvAdapter {
        return object : SimpleAutoChoiceRvAdapter<Course>(items, R.layout.item_course) {
            private val borderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4F, resources.displayMetrics)
            private val tlBr = CornerTransformer.create(CornerTransformer.TYPE_TL or CornerTransformer.TYPE_BR, borderWidth, Color.RED)
            private val trBl = CornerTransformer.create(CornerTransformer.TYPE_TR or CornerTransformer.TYPE_BL, borderWidth, Color.BLUE)
            override fun bindView(holder: RvHolder, data: Course) {
                val helper = holder.helper
                val position = helper.position
                ImageLoader.load(data.picBigUrl)
                        .addTransformer(if (position and 1 == 0) tlBr else trBl)
                        .into(helper.getView(R.id.iv_icon))
                helper.setText(R.id.tv_serial_number, position.toString())
                        .setText(R.id.tv_name, data.name)
                        .setText(R.id.tv_description, data.description)
            }
        }
    }
}
