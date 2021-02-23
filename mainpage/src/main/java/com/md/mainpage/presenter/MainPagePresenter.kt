package com.md.mainpage.presenter

import com.md.basedpc.base.BasePresenter
import com.md.mainpage.`interface`.MainPageContract
import com.md.mainpage.model.MainPageModelV2

class MainPagePresenter : BasePresenter<MainPageContract.View>(), MainPageContract.Presenter {

    private val mainpageModel by lazy {
        MainPageModelV2()
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
    }
}