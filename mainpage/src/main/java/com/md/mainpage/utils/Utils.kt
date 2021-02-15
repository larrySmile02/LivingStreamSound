package com.md.mainpage.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


object Utils {

    fun hideSoftKeyboard(context: Context, list:List<View>){
        if (list.isEmpty()) return
        var inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        list.forEach {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

    }


}