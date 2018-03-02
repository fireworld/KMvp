package cc.colorcat.mvp.view

import android.os.Bundle
import cc.colorcat.mvp.extension.Const

/**
 * Created by cxx on 2018/3/1.
 * xx.ch@outlook.com
 */
class ContainerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getExtra<Int>(Const.key.container_layout)!!)
    }
}
