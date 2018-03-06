package cc.colorcat.mvp.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import cc.colorcat.mvp.R
import cc.colorcat.mvp.contract.ICourses
import cc.colorcat.mvp.entity.Course
import cc.colorcat.mvp.extension.image.CornerTransformer
import cc.colorcat.mvp.extension.image.ImageLoader
import cc.colorcat.mvp.extension.widget.*
import cc.colorcat.mvp.presenter.CoursesPresenter
import kotlinx.android.synthetic.main.activity_courses.*

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
class CoursesActivity : BaseActivity(), ICourses.View, KTip.Listener {
    private val mPresenter = CoursesPresenter()
    private val mCourses = mutableListOf<Course>()
    private val mAdapter: ChoiceRvAdapter by lazy {
        object : AutoChoiceRvAdapter() {
            val tlBr = CornerTransformer.create(CornerTransformer.TYPE_TL or CornerTransformer.TYPE_BR)
            val trBl = CornerTransformer.create(CornerTransformer.TYPE_TR or CornerTransformer.TYPE_BL)

            override fun getLayoutResId(viewType: Int): Int = R.layout.item_course

            override fun bindView(holder: RvHolder, position: Int) {
                val course = mCourses[position]
                val helper = holder.helper
                ImageLoader.load(course.picBigUrl)
                        .addTransformer(if (position and 1 == 0) trBl else tlBr)
                        .into(helper.getView(R.id.iv_icon))
                helper.setText(R.id.tv_serial_number, position.toString())
                        .setText(R.id.tv_name, course.name)
                        .setText(R.id.tv_description, course.description)
            }

            override fun getItemCount(): Int = mCourses.size
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        rv_courses.layoutManager = LinearLayoutManager(this)
        rv_courses.addOnScrollListener(VanGoghScrollListener.Companion.Instance)
        rv_courses.adapter = mAdapter

        srl_root.setOnRefreshListener { mPresenter.toRefreshCourses() }

        mPresenter.onCreate(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.courses, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.single -> {
                mAdapter.choiceMode = ChoiceRvAdapter.ChoiceMode.SINGLE
                mAdapter.notifyDataSetChanged()
                return true
            }
            R.id.multi -> {
                mAdapter.choiceMode = ChoiceRvAdapter.ChoiceMode.MULTIPLE
                mAdapter.notifyDataSetChanged()
                return true
            }
            R.id.none -> {
                mAdapter.choiceMode = ChoiceRvAdapter.ChoiceMode.NONE
                mAdapter.notifyDataSetChanged()
                return true
            }
            R.id.select_first -> {
                mAdapter.selection = 0
                return true
            }
            R.id.move -> {
                if (mCourses.size >= 5) {
                    val moved = mCourses.subList(1, 4).toList()
                    mCourses.removeAll(moved)
                    mAdapter.notifyItemRangeRemoved(1, 3)
                    mCourses.addAll(2, moved)
                    mAdapter.notifyItemRangeInserted(2, 3)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    override fun refreshCourses(courses: List<Course>) {
        hideTip()
        mCourses.clear()
        mCourses.addAll(courses)
        mAdapter.notifyDataSetChanged()
    }

    override fun stopRefresh() {
        srl_root.isRefreshing = false
    }

    override fun onTipClick() {
        mPresenter.toRefreshCourses()
    }
}
