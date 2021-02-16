package com.md.livingstreamsound.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.md.livingstreamsound.R
import com.md.livingstreamsound.test.Fragment2
import com.md.livingstreamsound.test.Fragment3
import com.md.livingstreamsound.test.Fragment4
import com.md.mainpage.`interface`.IMainInfo
import com.md.mainpage.ui.MainPageFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author liyue
 * created 2021/2/15
 * desc 作为主页面，内部由ViewPager+FragmentPagerAdapter组成
 * 遗留问题：
 * 1. 每日必听向上滑动时分类应该跟着滑上去，以后再做
 * 2. RecyclerView应该配合DiffUtil局部刷新的，以后再做
 */
private const val TAG="MainActivityKt"
//首页在ViewPager中的position
private const val MAIN_PAGE_POS=0;
//首页在ViewPager中的position
private const val MINE_POS=1
class MainActivityKt:AppCompatActivity(),Runnable, ViewPager.OnPageChangeListener,View.OnClickListener ,IMainInfo{

    private val fragments = ArrayList<Fragment>()
    private lateinit var mainAdapter: MainAdapter
    private var isShowSearch=false

    //防止关闭Activity后Handler还未执行完造成内存泄露
    companion object{
        @JvmStatic
        private val mHandler= Handler(Looper.getMainLooper())
        @JvmStatic
        private var isExit=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(isShowSearch){
          val mainFragment =  fragments[0] as MainPageFragment
            mainFragment.onBackPressed()
            return false
        }
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if(!isExit) {
                isExit=true
                Toast.makeText(this,getString(R.string.living_tap_exit),Toast.LENGTH_SHORT).show()
                mHandler.postDelayed(this,1500)
            }
            else finish()
        }

        return false
    }

    override fun run() {
        isExit=false
    }
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        if (position==MAIN_PAGE_POS) {
            selectMainPage(true)
            selectMine(false)
        }else{
            selectMainPage(false)
            selectMine(true)
        }
        Log.e(TAG, "pos=$position")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lltMainBtn -> {
                viewPager.setCurrentItem(MAIN_PAGE_POS,true)
                selectMainPage(true)
                selectMine(false)
            }
            R.id.lltMineBtn -> {
                viewPager.setCurrentItem(MINE_POS,true)
                selectMainPage(false)
                selectMine(true)
            }
        }
    }

    private fun initView(){
        val mainFrt=MainPageFragment()
        mainFrt.setIMainInfo(this)
        fragments.add(mainFrt)
        fragments.add(Fragment2())
        fragments.add(Fragment3())
        fragments.add(Fragment4())

        mainAdapter= MainAdapter(supportFragmentManager,fragments)
        viewPager.adapter=mainAdapter
        viewPager.setCanScroll(true)
        rootContainer.setViewPager(viewPager)
        viewPager.addOnPageChangeListener(this)
        selectMainPage(true)
        selectMine(false)

        lltMineBtn.setOnClickListener(this)
        lltMainBtn.setOnClickListener(this)


    }

    private inline fun selectMainPage(isSel: Boolean ){
        ivMainpageBtn.isSelected=isSel
        tvMainpageTitle.setTextColor(if (isSel)resources.getColor(R.color.colorAccent) else resources.getColor(R.color.living_262626))

    }

    private inline fun selectMine(isSel: Boolean){
        ivMineBtn.isSelected=isSel
        tvMineTitle.setTextColor(if (isSel)resources.getColor(R.color.colorAccent) else resources.getColor(R.color.living_262626))
    }

    class MainAdapter:FragmentPagerAdapter{
        private var fragmentList: List<Fragment> = ArrayList()

        constructor(fm:FragmentManager,fragmentList: List<Fragment>):super(fm){
            this.fragmentList=fragmentList
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
           return fragmentList.size
        }

    }

    override fun getSearchStatus(isShowMain: Boolean) {
        isShowSearch=!isShowMain
    }


}