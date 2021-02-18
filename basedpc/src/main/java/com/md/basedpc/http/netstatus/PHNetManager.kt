package com.yxt.basic_frame.http.netstatus

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import com.yxt.basic_frame.http.netstatus.annotation.NetType
import com.yxt.basic_frame.http.netstatus.impl.NetStatusCallBack

/**
 * 网络状态管理器
 *
 * @author 徐黎平
 * @date on 2019-10-21 16:04
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PHNetManager(private val application: Application) {

    // 回调
    private val netStatusCallBack = NetStatusCallBack()

    companion object {

        @Volatile
        private var INSTANCE : PHNetManager? = null

        @JvmStatic
        fun getInstance(application: Application) : PHNetManager {
            val temp = INSTANCE
            if (null != temp) {
                return temp
            }
            synchronized(this) {
                val instance = PHNetManager(application)
                INSTANCE = instance
                return instance
            }
        }
    }

    init {
        val request = NetworkRequest.Builder().build()
        val manager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.registerNetworkCallback(request, netStatusCallBack)
    }

    fun register(activity: Any) {
        netStatusCallBack.register(activity)
    }

    fun unRegister(activity: Any) {
        netStatusCallBack.unRegister(activity)
    }

    fun unRegisterAll() {
        netStatusCallBack.unRegisterAll()
        val manager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.unregisterNetworkCallback(netStatusCallBack)
    }

    fun getNetType() : @NetType String {
        return netStatusCallBack.getNetType()
    }
}