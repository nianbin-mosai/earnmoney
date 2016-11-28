package com.mdxx.qmmz.newfeature.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.mdxx.qmmz.common.HintDialog;

import org.greenrobot.eventbus.EventBus;



/**
 * 描述:
 * 作者：znb
 * 时间：2016年06月24日 15:06
 * 邮箱：nianbin@mosainet.com
 */
public class BaseFragment extends Fragment {
    private static final String fragment_level = "Fragemnt_Level";
    protected String tag = this.getClass().getSimpleName();
    private HintDialog hintDialog;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        Log.e(fragment_level,tag+"onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(fragment_level,tag+"onCreate");
        if(openEventBus()){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(fragment_level,tag+"onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(fragment_level,tag+"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(fragment_level,tag+"onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(openEventBus()){
            EventBus.getDefault().unregister(this);
        }
        Log.e(fragment_level,tag+"onDestroy");
    }

    /**
     * 显示提示框
     */
    public HintDialog showHintDialog(int resId) {
        return showHintDialog(getText(resId));
    }

    /**
     * 显示提示框
     */
    public HintDialog showHintDialog(CharSequence text) {
        if (hintDialog == null) {
            hintDialog = new HintDialog(mContext);
        }
        hintDialog.setMessages(text);
        if (!hintDialog.isShowing()) {
            hintDialog.show();
        }
        return hintDialog;
    }


    /**
     * 隐藏提示框
     */
    public void dismissHintDialog() {
        if (hintDialog != null && hintDialog.isShowing()) {
            hintDialog.dismiss();
        }
    }

    protected boolean openEventBus(){
        return false;
    }
}
