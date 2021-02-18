package com.md.basedpc.http.netstatus;



import com.md.basedpc.http.listener.PHNetWorkStatusListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yxt
 *
 * @date 2020/9/10.
 * description：
 */
public class PHNetWorkStatusUtils {
    private volatile static PHNetWorkStatusUtils mInstance;
    private Map<String, PHNetWorkStatusListener> listenerList = new HashMap<>();

    public enum Status {NO_NETWORK, WIFI, NET, NET_WORK_CONNECTION}

    public static PHNetWorkStatusUtils getInstance() {
        if (mInstance == null) {
            synchronized (PHNetWorkStatusUtils.class) {
                if (mInstance == null) {
                    mInstance = new PHNetWorkStatusUtils();
                }
            }
        }
        return mInstance;
    }

    public void netWorkStatusChanged(Status status) {
        for (PHNetWorkStatusListener l : listenerList.values()) {
            l.netWorkStatus(status);
        }
    }

    /**
     * 添加网络监听器
     *
     * @param target   当前监听器名称
     * @param listener 监听listener
     */
    public void setNetWorkListener(String target, PHNetWorkStatusListener listener) {
        listenerList.put(target, listener);
    }

    /**
     * 移除网络监听
     *
     * @param target 网络监听器名称
     */
    public void removeNetWorkListener(String target) {
        listenerList.remove(target);
    }


}
