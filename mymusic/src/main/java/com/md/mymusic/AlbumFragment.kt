package com.md.mymusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_my_build.*
/**
 * @author liyue
 * created 2021/2/21
 * desc 自建专辑与收藏专辑Fragment
 */
class AlbumFragment:Fragment() {
    //是否我我创建的专辑
    private var isMine=true
    companion object{
        fun getInstance(isMine:Boolean):AlbumFragment{
            val fragment = AlbumFragment()
            val bundle = Bundle()
            fragment.arguments=bundle
            fragment.isMine=isMine
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_my_build,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        fltBuildAlbum.visibility=if(isMine) View.VISIBLE else View.GONE
        var resDrawable= if(isMine) resources.getDrawable(R.drawable.mine_empty_album)else resources.getDrawable(R.drawable.mine_empty_restore)
        ivEmptyAlbum.setImageDrawable(resDrawable)
        var tip = if(isMine)resources.getString(R.string.mine_no_album)else resources.getString(R.string.mine_no_restore)
        tvEmptyAlbum.text=tip
    }
}