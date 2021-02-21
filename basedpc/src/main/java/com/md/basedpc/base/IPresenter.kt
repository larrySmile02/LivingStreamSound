package com.md.basedpc.base

/**
 * @author liyue
 * created 2021/2/21
 * desc 所有Presenter基类，觉得别人写的好copy来的，现在还没用，以后也许会有用。
 * */
interface IPresenter<in V:IBaseView> {
    fun attachView(mRootView:V)
    fun detachView()
}