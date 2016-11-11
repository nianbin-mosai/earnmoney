package com.wyu.earnmoney.test.ads.fingerobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fingermobi.vj.activity.QdiActivity;
import com.fingermobi.vj.listener.IVJAPI;
import com.fingermobi.vj.listener.IVJAppidStatus;
import com.wyu.earnmoney.R;

public class FingermobiMainActivity extends AppCompatActivity {
    //Android半托管：ae7304f92345fc939e9edfb27c1d7305
    //Android全托管：3db531c723749a80de2d3686951fbf25
    private String tag = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingermobimain);
        Button send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IVJAPI ivjapi = new IVJAPI();
                ivjapi.setAppid("cf3a1655135a131d69542fd2e088beb6");
                ivjapi.setGameid("gameid");
                ivjapi.init(FingermobiMainActivity.this, new IVJAppidStatus() {

                    @Override
                    public void appidStatus(int status) {
                        Log.i(tag,"status:"+status);
                        switch (status) {
                            case IVJAPI.VJ_ERROR:
                                // 失败
                                break;
                            case IVJAPI.VJ_APPID_INVALID:
                                // 广告位关闭
                                break;
                            case IVJAPI.VJ_SUCCESS:
                                // 广告位打开
                                startActivity(new Intent(FingermobiMainActivity.this,
                                        QdiActivity.class));
                                break;
                            case IVJAPI.VJ_CLOSE:
                                // 界面被关闭
                                break;
                            case IVJAPI.VJ_ON_PRESENT:
                                // 界面被展示出来
                                break;
                        }
                    }
                });
            }
        });

    }
}
