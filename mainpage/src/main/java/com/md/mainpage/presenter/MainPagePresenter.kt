package com.md.mainpage.presenter

import android.util.Log
import com.md.basedpc.base.BasePresenter
import com.md.mainpage.interfaces.MainPageContract
import com.md.mainpage.model.MainPageModel
import com.md.mainpage.model.MainPageModelV2
const val TAG="MainPagePresenter"
class MainPagePresenter : BasePresenter<MainPageContract.View>(), MainPageContract.Presenter {

    //首页面分类先用本地数据，后续新开接口了再接入
    private val localMainpageModel by lazy {
        MainPageModel()
    }
    private val mainpageModel by lazy {
        MainPageModelV2()
    }

    fun getLoacalCategoryList(){
        mRootView?.apply{
            setCateDetailList(localMainpageModel.getMainCategory())
        }
    }

    override fun getCateDetailList() {
        checkViewAttached()
        val disposable = mainpageModel.getCategoryDetailList()
                .subscribe({ categories ->
                    mRootView?.apply {
                        setCateDetailList(categories = categories)

                    }
                }, { throwable ->
                    mRootView?.apply {
                        showError(throwable.toString())

                    }
                })

        disposable?.let { addSubscription(it) }
    }

    override fun getDailyAlbums() {
        checkViewAttached()
        val disposable = mainpageModel.getDailyAlbums()
                .subscribe({ albums ->
                    mRootView?.apply {
                        setDailyAlbums(albums)
                        Log.e(TAG,"categories.size=${albums.size}")
                    }
                }, { throwable ->
                    mRootView?.apply {
                        showError(throwable.toString())
                        Log.e(TAG,throwable.toString())
                    }
                })

        disposable?.let { addSubscription(it) }
    }


}