package com.wyu.earnmoney.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.wyu.earnmoney.R;
import com.wyu.earnmoney.fingermobi.vj.example.FingermobiMainActivity;
import com.wyu.earnmoney.youmi.android.offerdemo.PermissionCheckActivity;
import com.wyu.earnmoney.yow.offerdemo.YowMainActivity;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年10月24日 17:44
 * 邮箱：nianbin@mosainet.com
 */
public class TestMainActivity extends IBaseActivity {
    @Override
    protected void beforeInitView() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int setContent() {
        return R.layout.activity_testmain;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {

    }
    private void startActivity(Class<?extends Activity> clazz){
        startActivity(new Intent(context,clazz));
    }
    public void onClickYoumi(View view){
        startActivity(PermissionCheckActivity.class);
    }
    public void onClickYow(View view){
        startActivity(YowMainActivity.class);
    }
    public void onClickFingermobi(View view){
        startActivity(FingermobiMainActivity.class);
    }

}
