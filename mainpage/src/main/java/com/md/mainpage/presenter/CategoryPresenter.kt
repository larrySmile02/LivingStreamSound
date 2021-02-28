package com.md.mainpage.presenter

import com.md.basedpc.base.BasePresenter
import com.md.mainpage.interfaces.CategoryContract
import com.md.mainpage.model.CategoryModel

class CategoryPresenter:BasePresenter<CategoryContract.View>(), CategoryContract.Presenter{

    private val model:CategoryModel by lazy {
        CategoryModel()
    }

    override fun getCategoryDetail(categoryId:Int) {
        checkViewAttached()
        val disposable =model.getCategoryDetailData(categoryId)
                .subscribe({ category ->
                    mRootView?.apply {
                       setCategoryDetail(category)
                    }
                }, { throwable ->
                    mRootView?.apply {
                        showError(throwable.toString())
                    }
                })
        disposable?.let { addSubscription(it) }
    }
}