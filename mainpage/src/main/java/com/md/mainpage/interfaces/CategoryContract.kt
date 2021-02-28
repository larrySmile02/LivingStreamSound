package com.md.mainpage.interfaces

import com.md.basedpc.base.IBaseView
import com.md.basedpc.base.IPresenter
import com.md.network.api.Category

open interface CategoryContract {

    interface View : IBaseView {
       fun setCategoryDetail(category: Category)
        fun showError(errorMsg: String)
    }

    interface Presenter: IPresenter<View> {
        fun getCategoryDetail(categoryId:Int)
    }
}