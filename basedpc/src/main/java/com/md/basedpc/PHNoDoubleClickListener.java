package com.md.basedpc;

import android.view.View;

import java.util.Calendar;

/**
 * CopyRright (c),2013-2023,YunXueTang Co. Ltd<br>
 * Project:ph<br>
 * Author:Created by yyf<br>
 * Create Date:2019/11/25 0008<br>
 * Mail:yyf@yxt.com<br>
 * Comments:防止过快点击事件.<br>
 * <p>
 * Modified By:yyf <br>
 * Modified Date: 2019/11/25 0008 <br>
 * Why & What is modified: null<br>
 * Version:1.0
 */
public abstract class PHNoDoubleClickListener implements View.OnClickListener {

    public PHNoDoubleClickListener(){
    }

    public PHNoDoubleClickListener(int clickDelay){
        this.clickDelay = clickDelay;
    }

    private int clickDelay = 500;//这里设置不能超过多长时间
    private long lastClickTime = 0;

    protected abstract void onNoDoubleClick(View v);

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > clickDelay) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
}