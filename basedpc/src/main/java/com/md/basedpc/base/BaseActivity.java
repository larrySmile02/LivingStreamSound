package com.md.basedpc.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import com.md.basedpc.application.AppManager;
import com.md.basedpc.autosize.AutoSizeCompat;
import com.md.basedpc.autosize.internal.CustomAdapt;
import androidx.appcompat.app.AppCompatActivity;


/**
 * View层基类
 * create by 朱大可 on 2019年09月26日16:29:40
 */
public abstract class BaseActivity extends AppCompatActivity implements CustomAdapt {

    private Context mContext;
    private Activity mActivity;
    public boolean needSecure = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (needSecure)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        mContext = this;
        mActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AutoSizeCompat.autoConvertDensity(this.getResources(), this.getSizeInDp(), this.isBaseOnWidth());
    }

    @Override
    public Resources getResources() {
        AutoSizeCompat.autoConvertDensity(super.getResources(), this.getSizeInDp(), this.isBaseOnWidth());
        return super.getResources();
    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 1080.0F;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    public Context getmContext() {
        return mContext;
    }

    public Activity getmActivity() {
        return mActivity;
    }

}
