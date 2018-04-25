package cc.colorcat.mvp.extension.net

/**
 * Created by cxx on 18-4-25.
 * xx.ch@outlook.com
 */
abstract class SimpleListener<T> : MListener<T> {
    override fun onStart() {
    }

    override fun onSuccess(result: T) {
    }

    override fun onFailure(code: Int, msg: String?) {
    }

    override fun onFinish() {
    }
}
