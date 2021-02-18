package com.md.basedpc;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Comments:分辨率换算工具类.<br>
 *
 * @author wanggq
 * @date :2019-11-25 17:46
 */
public class PHSizeUtils {

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context the context
     * @param pxValue the px value
     * @return int
     */
    public static int px2dp(Context context, float pxValue) {
        final double scale = (double) context.getResources().getDisplayMetrics().density;
        return (int) ((double) pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context the context
     * @param pxValue the px value
     * @return int
     */
    public static int px2sp(Context context, float pxValue) {
        final double fontScale = (double) context.getResources().getDisplayMetrics().scaledDensity;
        return (int) ((double) pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context the context
     * @param value   the sp value
     * @return int
     */
    public static int sp2px(Context context, float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * 将dp值转换为px值，保证文字大小不变
     *
     * @param context the context
     * @param value   the sp value
     * @return int
     */
    public static int dp2px(Context context, float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * 将pt值转换为px值，保证文字大小不变
     *
     * @param context the context
     * @param value   the pt value
     * @return int
     */
    public static int pt2px(Context context, float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * 将in值转换为px值，保证文字大小不变
     *
     * @param context the context
     * @param value   the in value
     * @return int
     */
    public static int in2px(Context context, float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, value, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * 将mm值转换为px值，保证文字大小不变
     *
     * @param context the context
     * @param value   the mm value
     * @return int
     */
    public static int mm2px(Context context, float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, value, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @return int
     */
    public static int getScreenWidth(Context context) {
        WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return systemService.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @return int
     */
    public static int getScreenHeight(Context context) {
        WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return systemService.getDefaultDisplay().getHeight();
    }
}
