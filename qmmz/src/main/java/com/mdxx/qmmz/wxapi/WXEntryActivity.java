package com.mdxx.qmmz.wxapi;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.mdxx.qmmz.common.WeChatShareUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

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

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = null;
                break;
            default:
                result = "分享失败";
                break;
        }
        if (result != null) {
            Toast.makeText(this, baseResp.errCode + result, Toast.LENGTH_SHORT).show();
        }
    }
}
