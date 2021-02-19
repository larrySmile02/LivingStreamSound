package com.md.mymusic

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.md.mymusic.test.FragmentA
import com.md.mymusic.test.FragmentB
import kotlinx.android.synthetic.main.fragment_mine.*

class MyMusicFragment:Fragment() {
    private val fragments = ArrayList<Fragment>()
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var mContext :Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_mine,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext= this!!.context!!
        fragments.add(FragmentA())
        fragments.add(FragmentB())
        albumAdapter = AlbumAdapter(activity!!.supportFragmentManager,fragments)
        albumViewPager.adapter=albumAdapter
        smartTab.setViewPager(albumViewPager)



    }


   inner class AlbumAdapter: FragmentPagerAdapter {
        private var fragmentList: List<Fragment> = ArrayList()

        constructor(fm: FragmentManager, fragmentList: List<Fragment>):super(fm){
            this.fragmentList=fragmentList
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {

            return if (position==0) mContext.resources.getString(R.string.mine_my_album) else mContext.resources.getString(R.string.mine_save_album)
        }

    }


}