package com.md.mainpage.model

import com.md.basedpc.rx.scheduler.SchedulerUtils
import com.md.network.api.Album
import com.md.network.api.Category
import com.md.network.api.ICategory
import com.md.network.api.RetrofitBuilderv2
import io.reactivex.rxjava3.core.Observable

class MainPageModelV2 {

    fun getCategoryDetailList(): Observable<ArrayList<ICategory>> {
        return RetrofitBuilderv2.apiService.getCategoriesList()
                .compose(SchedulerUtils.ioToMain())
    }
    fun getDailyAlbums():Observable<ArrayList<Album>>{
        return RetrofitBuilderv2.apiService.getDailyAlbums()
                .compose(SchedulerUtils.ioToMain())
    }
}