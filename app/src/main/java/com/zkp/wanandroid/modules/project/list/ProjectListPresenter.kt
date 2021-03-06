package com.zkp.wanandroid.modules.project.list

import com.zkp.wanandroid.base.presenter.BasePresenter
import com.zkp.wanandroid.bean.ArticleResponseBody
import com.zkp.wanandroid.bean.HttpResult
import com.zkp.wanandroid.http.HttpsUtil

/**
 * @author: zkp
 * @project: WanAndroid
 * @package: com.zkp.android.modules.project.list
 * @time: 2019/5/30 15:21
 * @description:
 */
class ProjectListPresenter : BasePresenter<ProjectListContract.Model, ProjectListContract.View>(),
    ProjectListContract.Presenter {

    private var cid = 0
    private var currentPage: Int = 1


    override fun refresh(cid: Int) {
        this.cid = cid
        this.currentPage = 1
        getProjectList(currentPage, cid, true)
    }

    override fun loadMore() {
        currentPage++
        getProjectList(currentPage, cid, false)
    }

    override fun createModel(): ProjectListContract.Model? = ProjectListModel()

    override fun getProjectList(page: Int, cid: Int, isFresh: Boolean) {
        mView?.showLoading()
        HttpsUtil().request(mModel!!.requestProjectList(page, cid),
            object : HttpsUtil.IResponseListener<HttpResult<ArticleResponseBody>> {

                override fun onSuccess(data: HttpResult<ArticleResponseBody>) {
                    if (data.errorCode == 0) {
                        mView?.getProjectListSuccess(data.data.datas, isFresh)
                    } else {
                        mView?.getProjectListError(data.errorMsg)
                    }
                    mView?.hideLoading()
                }

                override fun onFail(errMsg: String) {
                    mView?.getProjectListError(errMsg)
                    mView?.hideLoading()
                }

            })
    }

    override fun collectArticle(id: Int) {
        mView?.showLoading()
        HttpsUtil().request(mModel!!.collectArticle(id), object : HttpsUtil.IResponseListener<HttpResult<Any>> {
            override fun onSuccess(data: HttpResult<Any>) {
                if (data.errorCode == 0) {
                    mView?.collectArticleSuccess()
                } else {
                    mView?.collectArticleError(data.errorMsg)
                }
                mView?.hideLoading()
            }

            override fun onFail(errMsg: String) {
                mView?.collectArticleError(errMsg)
                mView?.hideLoading()
            }

        })
    }

    override fun unCollectArticle(id: Int) {
        mView?.showLoading()
        HttpsUtil().request(mModel!!.unCollectArticle(id), object : HttpsUtil.IResponseListener<HttpResult<Any>> {
            override fun onSuccess(data: HttpResult<Any>) {
                if (data.errorCode == 0) {
                    mView?.unCollectArticleSuccess()
                } else {
                    mView?.unCollectArticleError(data.errorMsg)
                }
                mView?.hideLoading()
            }

            override fun onFail(errMsg: String) {
                mView?.unCollectArticleError(errMsg)
                mView?.hideLoading()
            }

        })
    }

}