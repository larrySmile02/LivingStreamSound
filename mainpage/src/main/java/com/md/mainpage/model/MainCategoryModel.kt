package com.md.mainpage.model

import com.md.mainpage.R
import com.md.mainpage.model.bean.FakeCategoryBean

class MainCategoryModel {

    private val categoryBeans = ArrayList<FakeCategoryBean>()
    init {
        categoryBeans.add(FakeCategoryBean("圣经朗读", R.drawable.main_read_bible))
        categoryBeans.add(FakeCategoryBean("诗歌合集", R.drawable.main_songs))
        categoryBeans.add(FakeCategoryBean("圣经解读", R.drawable.main_explain_bible))
        categoryBeans.add(FakeCategoryBean("真理", R.drawable.main_truth))
        categoryBeans.add(FakeCategoryBean("名家名作", R.drawable.main_famous_work))
        categoryBeans.add(FakeCategoryBean("信的故事", R.drawable.main_trust_story))
        categoryBeans.add(FakeCategoryBean("儿童", R.drawable.main_baby))
        categoryBeans.add(FakeCategoryBean("更多", R.drawable.main_more_category))


    }
    fun getMainCategory():List<FakeCategoryBean> {
        return categoryBeans
    }

}