package com.example.goodsmanage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.goodsmanage.adapter.ViewPagerFragmentAdapter;
import com.example.goodsmanage.common.entity.Type;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.example.goodsmanage.fragment.GoodsListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private FrameLayout layoutHome;
    private Context mContext;
    private QMUITabSegment mTabSegment;
    private ViewPager mContentViewPager;
    ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    List<Type> typeList = new ArrayList<>();
    List<Fragment> fragmentList = new ArrayList<>();
    private String TAG = "MainActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    layoutHome.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_shopcar:
                    layoutHome.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_mine:
                    layoutHome.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        initView();
    }

    private void initView() {
        mTabSegment = findViewById(R.id.tabSegment);
        mContentViewPager = findViewById(R.id.contentViewPager);
        layoutHome = findViewById(R.id.layout_home);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BaseUtils.showProgressDialog(mContext, "加载中...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/getAllGoodsType",
                new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog();
                String responseStr = response.body().string();
                typeList = parseType(responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initTabAndPager();
                    }
                });
            }
        });

    }

    private void initTabAndPager() {
        for (Type type : typeList) {
            mTabSegment.addTab(new QMUITabSegment.Tab(type.getType()));
            Fragment fragment = new GoodsListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("typeId", type.getId());
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(fragmentManager, fragmentList);
        mContentViewPager.setAdapter(mViewPagerFragmentAdapter);
        mContentViewPager.setCurrentItem(0, false);
        mContentViewPager.addOnPageChangeListener(new ViewPageOnPagerChangedListener());

        int space = QMUIDisplayHelper.dp2px(mContext, 16);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setPadding(space, 0, space, 0);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                Log.d(TAG, "select index " + index);
            }

            @Override
            public void onTabUnselected(int index) {
                Log.d(TAG, "unSelect index " + index);
            }

            @Override
            public void onTabReselected(int index) {
                Log.d(TAG, "reSelect index " + index);
            }

            @Override
            public void onDoubleTap(int index) {
                Log.d(TAG, "double tap index " + index);
            }
        });
    }

    private List<Type> parseType(String responseStr) {
        List<Type> typeList = new ArrayList<>();
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            typeList = gson.fromJson(responseStr, new TypeToken<ArrayList<Type>>(){}.getType());
        }
        return typeList;
    }

    private class ViewPageOnPagerChangedListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Log.e(TAG, "onOptionsItemSelected: ");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
