package com.example.goodsmanage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.goodsmanage.acitvity.AddGoodsActivity;
import com.example.goodsmanage.acitvity.GoodsListActivity;
import com.example.goodsmanage.adapter.ViewPagerFragmentAdapter;
import com.example.goodsmanage.common.entity.Type;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.example.goodsmanage.common.utils.ToastUtil;
import com.example.goodsmanage.fragment.GoodsListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutHome;
    private Context mContext;
    private QMUITabSegment mTabSegment;
    private ViewPager mContentViewPager;
    ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    List<Type> typeList = new ArrayList<>();
    List<Fragment> fragmentList = new ArrayList<>();
    private String TAG = "MainActivity";

    private EditText editMax;
    private EditText editMin;
    private Button btnSearch;

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
        editMax = findViewById(R.id.edit_max);
        editMin = findViewById(R.id.edit_min);
        btnSearch = findViewById(R.id.btn_search);
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
                typeList = BaseUtils.parseType(responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initTabAndPager();
                    }
                });
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String max = editMax.getText().toString();
                String min = editMin.getText().toString();
                if (TextUtils.isEmpty(max) || TextUtils.isEmpty(min)) {
                    ToastUtil.showMsg(mContext, "请输入最大值和最小值");
                    return;
                }
                Intent intent = new Intent(mContext, GoodsListActivity.class);
                intent.putExtra("isSection", true);
                intent.putExtra("max", max);
                intent.putExtra("min", min);
                startActivity(intent);
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
                showSearchDialog(mContext);
                break;
            case R.id.add:
                Intent intent = new Intent(mContext, AddGoodsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchDialog(final Context context) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle("搜索商品")
                .setPlaceholder("输入商品名称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .setCanceledOnTouchOutside(false)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("搜索", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(final QMUIDialog dialog, int index) {
                        String search = builder.getEditText().getText().toString();
                        if (search.length() > 0) {
                            Intent intent = new Intent(mContext, GoodsListActivity.class);
                            intent.putExtra("search", search);
                            startActivity(intent);
                            dialog.dismiss();
                        } else {
                            ToastUtil.showMsg(context, "请输入商品名称");
                        }
                    }
                })
                .create().show();
    }
}
