package com.md.basedpc.http.listener;


import com.md.basedpc.http.netstatus.PHNetWorkStatusUtils;

/**
 * Created by yxt
 *
 * @date 2020/9/10.
 * description：
 */
public interface PHNetWorkStatusListener {
    void netWorkStatus(PHNetWorkStatusUtils.Status status);
}
