package com.md.livingstreamsound.customer;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author liyue
 * @created 2021/2/15
 * desc 为了首页面自定义ViewPager只能滑动到设置的位置,规则是：
 * 1. 当前在"首页"tab上ViewPager可以滑动
 * 2. 当前在"我听"tab上
 *    1）手指向左滑动，禁止ViewPager滑动
 *    2）手指向右滑动，且超过半屏，允许ViewPager滑动
 *    3）手指向右滑动，未超过半屏，禁止ViewPager滑动
 * 注意有个bug，最低版本android 5 /api=21
 */
public class LivingFrameLayout extends FrameLayout {
    private static final String TAG = "LivingFrameLayout";
    /**设置的item，自定义ViewPager只能滑动到这个值*/
    private int maxFragmentPos = 1;
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;
    private float mLastMotionX;
    private boolean isMoveLeft;
    private LivingViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LivingFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LivingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LivingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LivingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mActivePointerId = event.getPointerId(0);
                mLastMotionX = event.getX(mActivePointerId);
                viewPager.setCanScroll(false);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                int validIndex = event.getActionIndex();
                mActivePointerId = event.getPointerId(validIndex);
                mLastMotionX = event.getX(mActivePointerId);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = event.getX();
                final float dx = x - mLastMotionX;
                if (viewPager != null) {
                    if (dx < 0) isMoveLeft = true;
                    else isMoveLeft=false;

//                    if (viewPager.getCurrentItem() >= maxFragmentPos && isMoveLeft ){
//                        viewPager.setCanScroll(false);
//                    } else  {
//                            viewPager.setCanScroll(true);
//                    }

                    if(viewPager.getCurrentItem()<maxFragmentPos) {
                        viewPager.setCanScroll(true);
                    }else {
                        if(isMoveLeft) viewPager.setCanScroll(false);
                        else {
                            if(isHalfMore((int) Math.abs(dx))) viewPager.setCanScroll(true);
                           else viewPager.setCanScroll(false);
                        }
                    }
                }

                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                int curIndex = event.getActionIndex();
                int curId = event.getPointerId(curIndex);
                if (curId == mActivePointerId) {
                    int newIndex = -1;
                    if (curIndex == event.getPointerCount() - 1) {
                        newIndex = event.getPointerCount() - 2;
                    } else {
                        newIndex = event.getPointerCount() - 1;
                    }
                    mActivePointerId = event.getPointerId(newIndex);
                }
                break;
            case MotionEvent.ACTION_UP:
                viewPager.setCanScroll(true);
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    private boolean isHalfMore(int abs){
       int W=getWidth();
       if(abs>W/2) return true;
       else return false;
    }

    public void setViewPager(LivingViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setMaxFragmentPos(int maxFragmentPos){
        this.maxFragmentPos = maxFragmentPos;
    }
}
