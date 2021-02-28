package com.md.mainpage.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.md.livingwidget.navigation.PHNavigationBar
import com.md.mainpage.R
import kotlinx.android.synthetic.main.activity_categories.*

class MainCategoryActivity :AppCompatActivity(),PHNavigationBar.OnBarClickListener {
    private val tagList= ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        initNav()

        tagList.add("10")
        tagList.add("10000")
        tagList.add("90")
        tagList.add("30")
        tagList.add("300000")
        tagList.add("3000009999")
        tagList.add("99")
        tagList.add("977779")
        tagList.add("99")
        tagList.add("3")
        tagList.add("10")
        tagList.add("102")
        tagList.add("90")
        tagList.add("99")
        tagList.add("3")
        tagList.add("10")
        tagList.add("102")
        tagList.add("90")
        flowLayout.showTag(tagList,null)
    }

    private fun initNav(){
        navBar.centerTvTitle.apply {
            text=resources.getString(R.string.main_category)
        }
        navBar.setOnBarClickListener(this)
    }

    override fun onBarLeft1Back(v: View?) {
        finish()
    }

    override fun onBarRight2Btn(v: View?) {
        //do nothing
    }

    override fun onBarRight3Btn(v: View?) {
        //do nothing
    }

    override fun onBarLeft2Back(v: View?) {
        //do nothing
    }

    override fun onBarRight1Tv(v: View?) {
        //do nothing
    }


}