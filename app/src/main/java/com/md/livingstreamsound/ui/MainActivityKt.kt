package com.md.livingstreamsound.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.md.livingstreamsound.R

class MainActivityKt:AppCompatActivity(),Runnable {

    companion object{
        @JvmStatic
        private val mHandler= Handler(Looper.getMainLooper())
        @JvmStatic
        private var isExit=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

}