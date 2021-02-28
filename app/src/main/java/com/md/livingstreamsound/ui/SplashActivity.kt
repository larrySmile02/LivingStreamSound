package com.md.livingstreamsound.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.liulishuo.filedownloader.demo.MainDownPlayActivity
import com.md.livingstreamsound.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity:AppCompatActivity(),Runnable ,View.OnClickListener{

   companion object{
       @JvmStatic
        val mHandler= Handler(Looper.getMainLooper())
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        btnMain.setOnClickListener(this)
        btnDownload.setOnClickListener(this)
//        mHandler.postDelayed(this,2000)
    }

    override fun run() {
        intent= Intent(this,MainActivityKt::class.java)
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnMain->{
                val intent = Intent(this,MainActivityKt::class.java)
                startActivity(intent)
            }

            R.id.btnDownload->{
                val intent = Intent(this,MainDownPlayActivity::class.java)
                startActivity(intent)
            }

        }
    }
}