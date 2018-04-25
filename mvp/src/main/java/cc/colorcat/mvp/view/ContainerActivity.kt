package cc.colorcat.mvp.view

import android.os.Bundle
import cc.colorcat.mvp.R
import cc.colorcat.mvp.extension.Const

/**
 * Created by cxx on 2018/3/1.
 * xx.ch@outlook.com
 */
class ContainerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        @Suppress("UNCHECKED_CAST")
        val clazz: Class<BaseFragment> = Class.forName(getExtra(Const.key.fragment_name)!!) as Class<BaseFragment>
        manager.beginTransaction()
                .replace(R.id.fl_container, clazz.newInstance(), clazz.name)
                .commit()
    }
}
