package com.md.livingstreamsound.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.md.livingstreamsound.R

class SplashActivity:AppCompatActivity(),Runnable {

   companion object{
       @JvmStatic
        val mHandler= Handler(Looper.getMainLooper())
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mHandler.postDelayed(this,2000)
    }

    override fun run() {
        intent= Intent(this,MainActivityKt::class.java)
        startActivity(intent)
        finish()
    }
}