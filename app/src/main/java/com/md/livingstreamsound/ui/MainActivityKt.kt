package com.md.livingstreamsound.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.md.livingstreamsound.R
import com.md.livingstreamsound.test.Fragment1
import com.md.livingstreamsound.test.Fragment2
import com.md.livingstreamsound.test.Fragment3
import com.md.livingstreamsound.test.Fragment4
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author liyue
 * created 2021/2/15
 * desc 作为主页面，内部由ViewPager+FragmentPagerAdapter组成
 */
class MainActivityKt:AppCompatActivity(),Runnable {

    private val fragments = ArrayList<Fragment>()
    private lateinit var mainAdapter: MainAdapter

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



    private fun initView(){
        fragments.add(Fragment1())
        fragments.add(Fragment2())
        fragments.add(Fragment3())
        fragments.add(Fragment4())

        mainAdapter= MainAdapter(supportFragmentManager,fragments)
        viewPager.adapter=mainAdapter
        viewPager.setCanScroll(true)
        rootContainer.setViewPager(viewPager)


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


}