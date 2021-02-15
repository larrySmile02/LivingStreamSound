package com.md.livingstreamsound.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.md.livingstreamsound.R
import kotlinx.android.synthetic.main.frag_test.*

class Fragment2:Fragment() {
    private val currentF = "Fragment2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG,"$currentF onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.frag_test,container,false)
       return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv.text="2"
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG,"$currentF onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG,"$currentF onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG,"$currentF onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG,"$currentF onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"$currentF onDestroy")
    }
}