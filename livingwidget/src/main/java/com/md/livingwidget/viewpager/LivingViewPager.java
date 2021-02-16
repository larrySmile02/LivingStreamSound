package com.md.livingwidget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * @author liyue
 * @created 2021/2/15
 * desc 为了和自定义空间LivingFrameLayout配合使用，只能滑动到设置的item
 * */
public class LivingViewPager extends ViewPager {
    private static final String TAG="LivingViewPager";
    private boolean isCanScroll= true;

    public LivingViewPager(@NonNull Context context) {
        super(context);
    }

    public LivingViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!isCanScroll) return true;
        return super.onTouchEvent(ev);
    }

    public void setCanScroll(boolean isCanScroll){
        this.isCanScroll=isCanScroll;
    }
}
