package com.md.mainpage.model

import com.md.mainpage.R
import com.md.mainpage.model.bean.FakeCategoryBean
import com.md.network.api.Category
import com.md.network.api.ICategory
import com.md.network.api.LocalCategory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.internal.operators.observable.ObservableObserveOn

/**
 * @author liyue
 * @created 2021/2/16
 * desc 获取首页面数据，坤阳说数据从云端取。现在写死为了测试UI，以后云端接口通了就从云端请求
 */
class MainPageModel {

    private val categoryBeans =  ArrayList<ICategory>()
    init {
        categoryBeans.add(LocalCategory(R.drawable.main_read_bible,"圣经朗读","1",""))
        categoryBeans.add(LocalCategory(R.drawable.main_songs,"诗歌合集","2",""))
        categoryBeans.add(LocalCategory(R.drawable.main_explain_bible,"圣经解读","3",""))
        categoryBeans.add(LocalCategory(R.drawable.main_truth,"真理","4",""))
        categoryBeans.add(LocalCategory(R.drawable.main_famous_work,"名家名作","5",""))
        categoryBeans.add(LocalCategory(R.drawable.main_trust_story,"信的故事","6",""))
        categoryBeans.add(LocalCategory(R.drawable.main_baby,"儿童","7",""))
        categoryBeans.add(LocalCategory(R.drawable.main_more_category,"更多","8",""))
    }
    fun getMainCategory():ArrayList<ICategory> {
        return categoryBeans
    }

}