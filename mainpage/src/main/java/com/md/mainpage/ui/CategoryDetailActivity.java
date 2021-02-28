package com.md.mainpage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.md.livingwidget.navigation.PHNavigationBar;
import com.md.mainpage.R;

public class CategoryDetailActivity extends AppCompatActivity implements PHNavigationBar.OnBarClickListener {
    public static final String CATEGORY_ID="CATEGORY_ID";
    public static final String CATEGORY_TITLE="CATEGORY_TITLE";
    private PHNavigationBar navBar;
    private TextView tvTitle;
    private String title;
    private int categoryId=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        initData();
        initNav();

    }

    private void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra(CATEGORY_TITLE);
        categoryId = intent.getIntExtra(CATEGORY_ID,-1);
    }

    private void initNav(){
        navBar=findViewById(R.id.navBar);
        tvTitle= navBar.getCenterTvTitle();
        tvTitle.setText(title);
        navBar.setOnBarClickListener(this);
    }

    public static void gotoCategoryDetail(AppCompatActivity activity, String title, int categoryId){
        Intent intent = new Intent(activity,CategoryDetailActivity.class );
        intent.putExtra(CATEGORY_TITLE,title);
        intent.putExtra(CATEGORY_ID,categoryId);
        activity.startActivity(intent);
    }

    @Override
    public void onBarLeft1Back(View v) {
        finish();
    }

    @Override
    public void onBarLeft2Back(View v) {

    }

    @Override
    public void onBarRight1Tv(View v) {

    }

    @Override
    public void onBarRight2Btn(View v) {

    }

    @Override
    public void onBarRight3Btn(View v) {

    }
}
