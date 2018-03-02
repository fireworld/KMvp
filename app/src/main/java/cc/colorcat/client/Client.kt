package cc.colorcat.client

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import cc.colorcat.mvp.ClientHelper
import cc.colorcat.mvp.IClient

/**
 * Created by cxx on 18-3-2.
 * xx.ch@outlook.com
 */
class Client : Application(), IClient {
    override val context: Context = this
    override val baseUrl: String = BuildConfig.BASE_URL
    override val debug: Boolean = BuildConfig.DEBUG

    override fun onCreate() {
        super.onCreate()
        ClientHelper.init(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ClientHelper.onLowMemory()
    }

    private lateinit var mToast: Toast

    override fun toast(text: CharSequence) {
        if (!this::mToast.isInitialized) {
            @SuppressLint("ShowToast")
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        } else {
            mToast.setText(text)
        }
        mToast.show()
    }
}
