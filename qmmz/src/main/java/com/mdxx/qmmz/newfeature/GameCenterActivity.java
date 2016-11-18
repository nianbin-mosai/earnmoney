package com.mdxx.qmmz.newfeature;

import com.mdxx.qmmz.R;

public class GameCenterActivity extends BaseWebViewActivity {

    @Override
    protected String setMtitle() {
        return getString(R.string.game_center);
    }

    @Override
    protected String setScheme() {
        return "alipays";
    }
}
