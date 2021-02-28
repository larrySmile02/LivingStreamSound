package com.md.mainpage.model

import com.md.basedpc.rx.scheduler.SchedulerUtils
import com.md.network.api.Category
import com.md.network.api.RetrofitBuilderv2
import io.reactivex.rxjava3.core.Observable

/**
 * @author liyue
 * created 2021/2/28
 * desc 获取分类详情页数据
 */
class CategoryModel {

    //获取分类详情页数据
    fun getCategoryDetailData(id:Int): Observable<Category> {
        return RetrofitBuilderv2.apiService.getCategoryInfo(id)
                .compose(SchedulerUtils.ioToMain())
    }

}