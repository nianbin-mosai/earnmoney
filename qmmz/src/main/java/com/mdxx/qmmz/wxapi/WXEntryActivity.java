package com.mdxx.qmmz.wxapi;


import android.app.Activity;
import android.os.Bundle;

import com.mdxx.qmmz.common.LogUtils;
import com.mdxx.qmmz.common.UserPF;
import com.mdxx.qmmz.common.WeChatShareUtil;
import com.mdxx.qmmz.newfeature.bean.ShareAppRecord;
import com.mdxx.qmmz.newfeature.event.Event;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IWXAPI api = WXAPIFactory.createWXAPI(this, WeChatShareUtil.APP_ID, true);
        api.handleIntent(getIntent(),this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.i("onReq");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtils.i("onResp");
        String result;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                saveShareTime();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = null;
                break;
            default:
                result = "分享失败";
                break;
        }
        if (result != null) {
            LogUtils.i(baseResp.errCode + result);
        }
    }
    private void saveShareTime(){
        ShareAppRecord shareAppRecord = new ShareAppRecord();
        shareAppRecord.setPhone(UserPF.getInstance().getPhone());
        shareAppRecord.setShareTime(System.currentTimeMillis());
        shareAppRecord.save();
        EventBus.getDefault().post(new Event.ShareSuccessEvent());
    }
}
