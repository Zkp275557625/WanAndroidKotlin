package com.zkp.wanandroid.base.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import com.zkp.wanandroid.app.App
import com.zkp.wanandroid.base.model.IModel
import com.zkp.wanandroid.base.view.IView
import com.zkp.wanandroid.utils.SpUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.simple.eventbus.EventBus

/**
 * @author: zkp
 * @project: WanAndroid
 * @package: com.zkp.android.base
 * @time: 2019/5/27 14:27
 * @description:
 */
open class BasePresenter<M : IModel, V : IView> : IPresenter<V>, LifecycleObserver {

    var mModel: M? = null
    var mView: V? = null
    var mCompositeDisposable: CompositeDisposable? = null

    open fun createModel(): M? = null

    override fun attachView(mView: V) {
        this.mView = mView
        mModel = createModel()

        if (mView is LifecycleOwner) {
            (mView as LifecycleOwner).lifecycle.addObserver(this)
            if (mModel != null && mModel is LifecycleOwner) {
                (mModel as LifecycleOwner).lifecycle.addObserver(mModel as LifecycleObserver)
            }
        }
        registerEventBus()
    }

    override fun detachView() {
        unregisterEventBus()
        unDispose()
        this.mView = null
        this.mModel = null
        this.mCompositeDisposable = null
    }

    private fun unDispose() {
        //保证Activity结束时取消
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
    }

    override fun reload() {

    }

    /**
     * 注册EventBus
     */
    override fun registerEventBus() {
        EventBus.getDefault().register(this)
    }

    /**
     * 取消注册EventBus
     */
    override fun unregisterEventBus() {
        EventBus.getDefault().unregister(this)
    }

    open fun addSubscribe(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let { mCompositeDisposable?.add(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }

    override fun getLoginStatus(): Boolean {
        return SpUtils().getBoolean(App.getContext(), "loginStatus")
    }

    override fun getUserAccount(): String {
        return SpUtils().getString(App.getContext(), "userAccount")!!
    }

    override fun setUserAccount(userAccount: String) {
        return SpUtils().putString(App.getContext(), "userAccount", userAccount)
    }

}