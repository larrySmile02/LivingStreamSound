package com.md.mainpage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md.basedpc.log.Log;
import com.md.livingwidget.navigation.PHNavigationBar;
import com.md.mainpage.R;
import com.md.mainpage.adapter.CategoryDetailAdapter;
import com.md.mainpage.interfaces.CategoryContract;
import com.md.mainpage.presenter.CategoryPresenter;
import com.md.network.api.Category;
import com.md.network.api.Group;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author liyue
 * created 2021/2/28
 * desc :分类详情页
 */
public class CategoryDetailActivity extends AppCompatActivity implements PHNavigationBar.OnBarClickListener, CategoryContract.View{
    private static final String TAG="CategoryDetailActivity";
    public static final String CATEGORY_ID="CATEGORY_ID";
    public static final String CATEGORY_TITLE="CATEGORY_TITLE";
    private PHNavigationBar navBar;
    private TextView tvTitle;
    private String title;
    private int categoryId=-1;
    private CategoryPresenter mPresenter;
    private RecyclerView categoryRec;
    private CategoryDetailAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        initData();
        initView();
        requestData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra(CATEGORY_TITLE);
        categoryId = intent.getIntExtra(CATEGORY_ID,-1);
        mPresenter= new CategoryPresenter();
        mPresenter.attachView(this);
    }

    private void initView(){
        navBar=findViewById(R.id.navBar);
        categoryRec=findViewById(R.id.recView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        categoryRec.setLayoutManager(manager);
        adapter = new CategoryDetailAdapter(this,new ArrayList<>());
        categoryRec.setAdapter(adapter);
        tvTitle= navBar.getCenterTvTitle();
        tvTitle.setText(title);
        navBar.setOnBarClickListener(this);
    }

    private void requestData() {
        if(categoryId>0)
            mPresenter.getCategoryDetail(categoryId);
    }

    public static void gotoCategoryDetail(AppCompatActivity activity, String title, int categoryId){
        Intent intent = new Intent(activity,CategoryDetailActivity.class );
        intent.putExtra(CATEGORY_TITLE,title);
        intent.putExtra(CATEGORY_ID,categoryId);
        activity.startActivity(intent);
    }

    @Override
    public void setCategoryDetail(@NotNull Category category) {
        if (category !=null && category.getGroups() !=null && category.getGroups().size()>0)
            adapter.setGroups(category.getGroups());
    }

    @Override
    public void showError(@NotNull String errorMsg) {
        Log.e(errorMsg);
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
