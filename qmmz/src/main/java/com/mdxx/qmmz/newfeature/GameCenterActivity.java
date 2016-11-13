package com.mdxx.qmmz.newfeature;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.newfeature.BaseWebViewActivity;

public class GameCenterActivity extends BaseWebViewActivity {

    @Override
    protected String setMtitle() {
        return getString(R.string.pay);
    }

    @Override
    protected String setScheme() {
        return "alipays";
    }
}
