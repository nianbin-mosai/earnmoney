package com.mdxx.qmmz.newfeature;

import android.os.Bundle;
import android.view.View;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.newfeature.adapter.TabFragmentAdapter;
import com.mdxx.qmmz.newfeature.view.BottomNavigatorView;
import com.mdxx.qmmz.newfeature.view.FragmentNavigator;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月28日 11:08
 * 邮箱：nianbin@mosainet.com
 */
public class MainActivity extends BaseActivity implements BottomNavigatorView.OnBottomNavigatorViewItemClickListener{
    public FragmentNavigator mNavigator;
    private BottomNavigatorView bottomNavigatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDatas();
    }
    protected void initView() {
        bottomNavigatorView = (BottomNavigatorView) findViewById(R.id.bottomNavigatorView);
        if (bottomNavigatorView != null) {
            bottomNavigatorView.setOnBottomNavigatorViewItemClickListener(this);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigator.onSaveInstanceState(outState);
    }
    protected void initDatas() {
        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new TabFragmentAdapter(), R.id.container);
        setCurrentTab(TabFragmentAdapter.fragment.jilu.ordinal());
    }
    private void setCurrentTab(int position) {
        mNavigator.showFragment(position);
        bottomNavigatorView.select(position);
    }

    @Override
    public void onBottomNavigatorViewItemClick(int position, View view) {
        setCurrentTab(position);
    }
}
