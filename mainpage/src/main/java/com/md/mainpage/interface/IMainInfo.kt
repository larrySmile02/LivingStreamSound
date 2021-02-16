package com.md.mainpage.`interface`
/**
 * @author liyue
 * created 2021/2/16
 * 仅供壳工程使用，获取状态信息
 */
interface IMainInfo {
    //当前是否在展示搜索区
    fun getSearchStatus(isShowMain:Boolean)
}