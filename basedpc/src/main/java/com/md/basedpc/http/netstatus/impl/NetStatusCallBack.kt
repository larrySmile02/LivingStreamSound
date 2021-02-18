package com.yxt.basic_frame.http.netstatus.impl

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.yxt.basic_frame.http.netstatus.annotation.PHNet
import com.yxt.basic_frame.http.netstatus.annotation.NetType
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.Method
import java.util.HashMap

/**
 * 网络监听
 * @author 徐黎平
 * @date on 2019-10-22 11:13
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetStatusCallBack : ConnectivityManager.NetworkCallback() {

    // 观察者，key=类、value=方法
    private val checkManMap = HashMap<Any, Method>()

    private var nowType = NetType.WIFI

    // 网络状态记录
    @Volatile
    private var netType: @NetType String = NetType.NET_UNKNOWN

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        if (nowType != NetType.NONE) {
            nowType = NetType.NONE
            post(NetType.NONE)
        }
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            netType(networkCapabilities)
        }
    }

    private fun netType(networkCapabilities: NetworkCapabilities) {
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
            if (nowType != NetType.WIFI) {
                nowType = NetType.WIFI
                post(NetType.WIFI)
            }
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            if (nowType != NetType.NET) {
                nowType = NetType.NET
                post(NetType.NET)
            }
        } else {
            if (nowType != NetType.NET_UNKNOWN) {
                nowType = NetType.NET_UNKNOWN
                post(NetType.NET_UNKNOWN)
            }
        }
    }

    // 执行
    private fun post(str: @NetType String) {
        netType = str
        val set: Set<Any> = checkManMap.keys
        for (obj in set) {
            val method = checkManMap[obj] ?: continue
            invoke(obj, method, str)
        }
    }

    // 反射执行
    private fun invoke(obj: Any, method: Method, type: @NetType String) {
        try {
            method.invoke(obj, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 注册
    fun register(obj: Any) {
        val clz = obj.javaClass
        if (!checkManMap.containsKey(clz)) {
            val method = findAnnotationMethod(clz) ?: return
            checkManMap[obj] = method
        }
    }

    // 取消注册
    fun unRegister(obj: Any) {
        checkManMap.remove(obj)
    }

    // 取消所有注册
    fun unRegisterAll() {
        checkManMap.clear()
    }

    // 获取状态
    fun getNetType(): @NetType String = netType

    // 查找监听的方法
    private fun findAnnotationMethod(clz: Class<*>): Method? {
        val method = clz.methods
        for (m in method) {
            // 看是否有注解
            m.getAnnotation(PHNet::class.java) ?: continue
            // 判断返回类型
            val genericReturnType = m.genericReturnType.toString()
            if ("void" != genericReturnType) {
                // 方法的返回类型必须为void
                throw RuntimeException("The return type of the method【${m.name}】 must be void!")
            }
            // 检查参数，必须有一个String型的参数
            val parameterTypes = m.genericParameterTypes
            if (parameterTypes.size != 1 || parameterTypes[0].toString() != "class ${String::class.java.name}") {
                throw RuntimeException("The parameter types size of the method【${m.name}】must be one (type name must be java.lang.String)!")
            }
            return m
        }
        return null
    }
}