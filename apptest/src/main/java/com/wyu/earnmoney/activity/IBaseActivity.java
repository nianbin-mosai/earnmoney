package com.wyu.earnmoney.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.wyu.earnmoney.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月04日 11:34
 * 邮箱：nianbin@mosainet.com
 */
public abstract class IBaseActivity extends BaseActivity implements View.OnClickListener{
    protected String TAG = getClass().getSimpleName();
    /*********************************************
     * 监听网络变化
     ***********************************/
    private boolean netWorkFirstTag = true;
    private IntentFilter networkChangedFilter;
    private NetworkChangedInterface networkChangedInterface;
    public BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (netWorkFirstTag) {
                netWorkFirstTag = false;
                return;
            }
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (networkChangedInterface != null) {
                    ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isAvailable()) {
                        LogUtils.e("net changed:" + true);
                        networkChangedInterface.networkChanged(true);
                    } else {
                        LogUtils.e("net changed:" + false);
                        networkChangedInterface.networkChanged(false);
                    }
                }

            }
        }
    };

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(setContent());
        fitsSystemWindows();
        beforeInitView();
        initView();
        initDatas();
        addListener();
        registerNetworkChanged();
        if(openEventBus()){
            EventBus.getDefault().register(this);
        }
    }

    protected abstract void beforeInitView();

    protected abstract void initDatas();

    /****************************
     * 沉浸状态栏控制
     ******************************/

    protected abstract int setContent();

    protected abstract void initView();

    protected abstract void addListener();

    /****************************沉浸状态栏控制******************************/
    private void fitsSystemWindows() {
        if (openfitsSystemWindows()) {
            ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.setFitsSystemWindows(true);
            }
        }
    }

    protected boolean openfitsSystemWindows() {
        return false;
    }

    private void registerNetworkChanged() {
        if (this instanceof NetworkChangedInterface) {
            networkChangedInterface = (NetworkChangedInterface) this;
            networkChangedFilter = new IntentFilter();
            networkChangedFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(myNetReceiver, networkChangedFilter);
        }

    }

    private void unregisterNetworkChanged() {
        if (this instanceof NetworkChangedInterface) {
            if (networkChangedInterface != null) {
                if (myNetReceiver != null && networkChangedFilter != null) {
                    unregisterReceiver(myNetReceiver);
                }
                myNetReceiver = null;
                networkChangedFilter = null;
            }
        }
    }
    /**************************************监听网络变化*********************************/

    //eventbus控制开关
    protected boolean openEventBus(){
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(openEventBus()){
            EventBus.getDefault().unregister(this);
        }
        unregisterNetworkChanged();
    }

    @Override
    protected boolean isChangeStatusBarBackgroud() {
        return false;
    }

    public interface NetworkChangedInterface {
        void networkChanged(boolean isAvailable);
    }

    @Override
    public boolean isChangingConfigurations() {
        return false;
    }
}
