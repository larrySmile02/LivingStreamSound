package com.md.mainpage.`interface`

import com.md.basedpc.base.IBaseView
import com.md.basedpc.base.IPresenter
import com.md.network.api.Album
import com.md.network.api.Category

interface MainPageContract {

    interface View : IBaseView {
        fun setCateDetailList(categories: ArrayList<Category>)
        fun setDailyAlbums(albums: ArrayList<Album>)
        fun showError(errorMsg: String)
    }

    interface Presenter:IPresenter<View>{
        fun getCateDetailList()
        fun getDailyAlbums()
    }
}