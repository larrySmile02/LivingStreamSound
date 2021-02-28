package com.md.mainpage.interfaces

import com.md.basedpc.base.IBaseView
import com.md.basedpc.base.IPresenter
import com.md.network.api.Album
import com.md.network.api.ICategory

interface MainPageContract {

    interface View : IBaseView {
        fun setCateDetailList(categories: ArrayList<ICategory>)
        fun setDailyAlbums(albums: ArrayList<Album>)
        fun showError(errorMsg: String)
    }

    interface Presenter:IPresenter<View>{
        fun getCateDetailList()
        fun getDailyAlbums()
    }
}