package com.md.livingwidget.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.md.livingwidget.R;

import androidx.appcompat.widget.Toolbar;

/**
 * 导航栏
 *
 * @author wanggq
 * @date :2019-11-25 17:46
 */
public class PHNavigationBar extends LinearLayout implements View.OnClickListener {

    /**
     * Toolbar
     */
    protected Toolbar mToolbar;
    /**
     * back图标
     */
    private ImageView left1Imgback;
    /**
     * 关闭按钮
     */
    private ImageView left2Imgback;

    /**
     * 关闭按钮
     */
    private TextView left2Tvback;
    /**
     * 中间加载图标
     */
    private ImageView centerImgLoading;
    /**
     * 标题
     */
    private TextView centerTvTitle;
    /**
     * 中间图标
     */
    private ImageView centerImgDrop;
    /**
     * 右侧容器，支持动态添加
     */
    protected LinearLayout rightContainer;
    /**
     * 右侧文本
     */
    protected TextView right1Tv;
    /**
     * 右侧文本
     */
    protected TextView right2Tv;

    private ImageView right2Btn;
    /**
     * 右侧图片
     */
    private ImageView right3Btn;
    /**
     * 分割线
     */
    View nav_toolbar_bar_diver;

    public PHNavigationBar(Context context) {
        super(context);
        init();
    }

    public PHNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PHNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    protected void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.ph_navigation_basic, this);
        mToolbar = findViewById(R.id.nav_toolbar);

        left1Imgback = findViewById(R.id.nav_toolbar_back_img);
        left2Imgback = findViewById(R.id.nav_toolbar_back_img2);
        left2Tvback = findViewById(R.id.nav_toolbar_back_text_back);

        centerImgLoading = findViewById(R.id.nav_toolbar_loading_icon);
        centerTvTitle = findViewById(R.id.nav_toolbar_title);
        centerImgDrop = findViewById(R.id.nav_toolbar_drop_down);

        rightContainer = findViewById(R.id.nav_toolbar_bar_right_container);
        right1Tv = findViewById(R.id.nav_toolbar_right1_tv);
        right2Tv = findViewById(R.id.nav_toolbar_right2_tv);

        right2Btn = findViewById(R.id.nav_toolbar_right2_btn);
        right3Btn = findViewById(R.id.nav_toolbar_right3_btn);
        nav_toolbar_bar_diver = findViewById(R.id.nav_toolbar_bar_diver);


        left1Imgback.setOnClickListener(this);
        left2Tvback.setOnClickListener(this);
        right1Tv.setOnClickListener(this);
        right2Tv.setOnClickListener(this);
        right2Btn.setOnClickListener(this);
        right3Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.nav_toolbar_back_img) {
            if (onBarClickListener != null)
                onBarClickListener.onBarLeft1Back(v);
        } else if (id == R.id.nav_toolbar_back_text_back) {
            if (onBarClickListener != null)
                onBarClickListener.onBarLeft2Back(v);
        } else if (id == R.id.nav_toolbar_right1_tv) {
            if (onBarClickListener != null)
                onBarClickListener.onBarRight1Tv(v);
        } else if (id == R.id.nav_toolbar_right2_btn) {
            if (onBarClickListener != null)
                onBarClickListener.onBarRight2Btn(v);
        } else if (id == R.id.nav_toolbar_right3_btn) {
            if (onBarClickListener != null)
                onBarClickListener.onBarRight3Btn(v);
        }
    }

    public ImageView getLeft1Imgback() {
        return left1Imgback;
    }

    public ImageView getLeft2Imgback() {
        return left2Imgback;
    }

    public TextView getLeft2Tvback() {
        return left2Tvback;
    }

    public ImageView getCenterImgLoading() {
        return centerImgLoading;
    }

    public TextView getCenterTvTitle() {
        return centerTvTitle;
    }

    public ImageView getCenterImgDrop() {
        return centerImgDrop;
    }

    public LinearLayout getRightContainer() {
        return rightContainer;
    }

    public TextView getRight1Tv() {
        return right1Tv;
    }

    public TextView getRight2Tv() {
        return right2Tv;
    }

    public ImageView getRight2Btn() {
        return right2Btn;
    }

    public ImageView getRight3Btn() {
        return right3Btn;
    }

    public View getNav_toolbar_bar_diver() {
        return nav_toolbar_bar_diver;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    OnBarClickListener onBarClickListener = null;

    public void setOnBarClickListener(OnBarClickListener onBarClickListener) {
        this.onBarClickListener = onBarClickListener;
    }


    public interface OnBarClickListener {
        void onBarLeft1Back(View v);

        void onBarLeft2Back(View v);

        void onBarRight1Tv(View v);

        void onBarRight2Btn(View v);

        void onBarRight3Btn(View v);
    }


}
