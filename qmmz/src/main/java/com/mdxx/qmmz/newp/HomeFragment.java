package com.mdxx.qmmz.newp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.mdxx.qmmz.EventMessage;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.common.Constants;
import com.mdxx.qmmz.common.LogUtils;
import com.mdxx.qmmz.common.ToastUtils;
import com.mdxx.qmmz.common.UserPF;
import com.mdxx.qmmz.common.WeChatShareUtil;
import com.mdxx.qmmz.network.AppAction;
import com.mdxx.qmmz.network.HttpResponse;
import com.mdxx.qmmz.network.OkhttpResponseHandler;
import com.mdxx.qmmz.newfeature.Actions;
import com.mdxx.qmmz.newfeature.GameCenterActivity;
import com.mdxx.qmmz.newfeature.PayActivity;
import com.mdxx.qmmz.newfeature.TaskActivity;
import com.mdxx.qmmz.newfeature.bean.ShareAppRecord;
import com.mdxx.qmmz.newfeature.bean.ShareAppRecordComparator;
import com.mdxx.qmmz.newfeature.bean.WebViewConfigs;
import com.mdxx.qmmz.newfeature.event.Event;
import com.mdxx.qmmz.utils.InterfaceTool;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment implements OnClickListener {
    private Animation loadAnimation;
    private ImageView refresh_date;
    private WebViewConfigs webViewConfigs = new WebViewConfigs();
    private NMainActivity activity;
    private Dialog sharetoqd_dialog;
    private final String Qdburl = InterfaceTool.ULR + "user/qiandao";
    private TextView note;
    private final String infourl = InterfaceTool.ULR + "user/account";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (NMainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        view.findViewById(R.id.banner_layout).setOnClickListener(this);
        view.findViewById(R.id.renwu_one).setOnClickListener(this);
        view.findViewById(R.id.renwu_two).setOnClickListener(this);
        view.findViewById(R.id.renwu_three).setOnClickListener(this);
        view.findViewById(R.id.renwu_four).setOnClickListener(this);
        view.findViewById(R.id.weishare).setOnClickListener(this);
        view.findViewById(R.id.weiguanzhu).setOnClickListener(this);
        view.findViewById(R.id.iv_game_center).setOnClickListener(this);
//        view.findViewById(R.id.g_image2).setOnClickListener(this);
//        view.findViewById(R.id.g_image3).setOnClickListener(this);
//        view.findViewById(R.id.g_image1).setOnClickListener(this);
        view.findViewById(R.id.g_image4).setOnClickListener(this);
        note = (TextView) view.findViewById(R.id.textView8);
        refresh_date = (ImageView) view.findViewById(R.id.refresh_date);
        refresh_date.setOnClickListener(this);
        loadAnimation = AnimationUtils.loadAnimation(activity,R.anim.refresh_date_anim);
        initViewPager(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.renwu_one:
                activity.showYoumiOffersWall();
                break;
            case R.id.renwu_two:
                activity.showYowOffersWall();
                break;

            case R.id.renwu_three:
                if(!TextUtils.isEmpty(webViewConfigs.task)){
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.putExtra("url",getFormatUrl(webViewConfigs.task));
                    LogUtils.i(getFormatUrl(webViewConfigs.task));
                    startActivityForResult(intent,0);
                }
                break;
            case R.id.renwu_four:
                loginDuiba();
                break;
            case R.id.weishare:
                goToShare();
                break;
            case R.id.weiguanzhu:
                ToastUtils.showToast(getActivity(), getString(R.string.tip_developing));
                break;
            case R.id.g_image4:
                 gotopay();
                break;
            case R.id.layout_qqkj:
                break;
            case R.id.layout_wb:
                break;
            case R.id.layout_pyq:
                break;
            case R.id.iv_game_center:
                if(!TextUtils.isEmpty(webViewConfigs.game)){
                    Intent intent = new Intent(getActivity(), GameCenterActivity.class);
                    intent.putExtra("url",getFormatUrl(webViewConfigs.game));
                    LogUtils.i(getFormatUrl(webViewConfigs.game));
                    startActivityForResult(intent,0);
                }
                break;
            case R.id.refresh_date:
                refresh_date.startAnimation(loadAnimation);
                getWebViewConfigs();
                break;
        }
    }
    private void loginDuiba(){
        if(!TextUtils.isEmpty(webViewConfigs.duiba)){
            AppAction.loginDuiba(getActivity(), webViewConfigs.duiba, new OkhttpResponseHandler(getActivity(),HttpResponse.class,(BaseActivity)getActivity()) {
                @Override
                public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                    try {
                        String duibaUrl = new JSONObject(responseString).optJSONObject("data").optString("url");
                        if (!TextUtils.isEmpty(duibaUrl)){
                            Actions.loginDuiba(getActivity(),duibaUrl);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void getinfo() {
        Map map = new HashMap<String, String>();
        map.put("userid", activity.getuseid());
        InterfaceTool.Networkrequest(activity, activity.queue, activity.m_pDialog, infourl,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        activity.closewaite();
                        try {
                            String code = response.getString("code");
                            if (code.equals("1")) {
                                JSONObject data = response
                                        .getJSONObject("data");

                                String noticetext = data.getString("noticetext");
                                note.setText(noticetext);
                            } else if (code.equals("9")) {
                                activity.sp.edit().putBoolean("islogin", false).commit();
                            } else {
                                activity.Toastshow(response.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, map);
    }


    public void onEventMainThread(EventMessage eventMessage) {
        if ("getinfo".equals(eventMessage.getInfo())) {
            getinfo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        EventBus.getDefault().unregister(this);
    }

    private void gotopay() {
        if(!TextUtils.isEmpty(webViewConfigs.pay)){
            Intent intent = new Intent(getActivity(), PayActivity.class);
            intent.putExtra("url",getFormatUrl(webViewConfigs.pay));
            LogUtils.i(getFormatUrl(webViewConfigs.pay));
            startActivityForResult(intent,0);
        }
    }


    /*******************************/

    private ViewPager mViewPager;
    private static final int[] IMG_IDS = new int[]{R.drawable.ic_banner_1,
            R.drawable.ic_banner_2, R.drawable.ic_banner_3};
//    private static final int[] IMG_IDS = new int[]{R.drawable.bg_banner};
    private List<ImageView> mPageViews = new ArrayList<ImageView>();
    private int mPosition;
    private Handler mHandler = new Handler();
    private long mLastTime;

    private void initViewPager(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        for (int i = 0; i < IMG_IDS.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(IMG_IDS[i]);
            mPageViews.add(imageView);
        }
        MyPageChangeListener listener = new MyPageChangeListener();
        mViewPager.setOnPageChangeListener(listener);
        PagerAdapter adapter = new MyPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1000);
        mHandler.post(mRunnable);
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            int location = position % mPageViews.size();
            View view = mPageViews.get(location);
            ((ViewPager) container).removeView(view);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            int location = position % mPageViews.size();
            View child = mPageViews.get(location);
            ((ViewPager) container).addView(child);
            return child;
        }
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            mPosition = arg0;
            mLastTime = System.currentTimeMillis();
        }
    }

    private Runnable mRunnable = new Runnable() {
        final int DELAYED = 3000;

        @Override
        public void run() {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastTime > DELAYED) {
                mPosition++;
                mViewPager.setCurrentItem(mPosition);
                mLastTime = System.currentTimeMillis();
            }
            mHandler.postDelayed(this, DELAYED);
        }
    };
    private void getWebViewConfigs(){
        AppAction.getWebViewConfigs(getActivity(), new OkhttpResponseHandler(getActivity(),HttpResponse.class,(BaseActivity)getActivity()) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                try {
                    JSONObject result = new JSONObject(responseString);
                    JSONObject data = result.optJSONObject("data");
                    webViewConfigs.exchange = data.optString("exchange");
                    webViewConfigs.game = data.optString("game");
                    webViewConfigs.member = data.optString("member");
                    webViewConfigs.pay = data.optString("pay");
                    webViewConfigs.task = data.optString("task");
                    webViewConfigs.duiba = data.optString("duiba");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }
    private String getFormatUrl(String url){
        return String.format("%s?userid=%s&token=%s",url, UserPF.getInstance().getUserid(),UserPF.getInstance().getToken());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.i("onViewCreated");
        weChatShareUtil = WeChatShareUtil.getInstance(getActivity());
        getWebViewConfigs();
    }

    private WeChatShareUtil weChatShareUtil;

    private boolean hasShared(){
        List<ShareAppRecord> datas =  DataSupport.findAll(ShareAppRecord.class);
        if(datas!=null){
            List<ShareAppRecord> tempDatas = new ArrayList<>();
            for(ShareAppRecord data : datas){
                if(TextUtils.equals(UserPF.getInstance().getPhone(),data.getPhone())){
                    tempDatas.add(data);
                }
            }
            if(tempDatas.size()!=0){
                Collections.sort(tempDatas,new ShareAppRecordComparator());
                if(System.currentTimeMillis() - tempDatas.get(0).getShareTime() <= 24 * 60 * 60 *1000){
                    return true;
                }
            }
        }
        return false;
    }
    private void share(String title,String content,String url,String imgUrl){

        String sessionTitle = "分享";
        String sessionDescription = getString(R.string.app_name)+"下载链接";
        String sessionUrl = "https://www.pgyer.com/bh1Q";
        Bitmap sessionThumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if(!TextUtils.isEmpty(title)){
            sessionTitle = title;
        }
        if (!TextUtils.isEmpty(content)) {
            sessionDescription = content;
        }
        if (!TextUtils.isEmpty(url)) {
            sessionUrl = url;
        }
        if (!TextUtils.isEmpty(imgUrl)) {
            try {
                sessionThumb = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean result = weChatShareUtil.shareUrl(sessionUrl, sessionTitle, sessionThumb, sessionDescription, SendMessageToWX.Req.WXSceneSession);
//        if (!result) {
//            ToastUtils.showToast(getActivity(),getActivity().getString(R.string.no_wechat));
//        }
    }
    private void shareTimeline(String title,String content,String url,String imgUrl){
        if (weChatShareUtil.isSupportWX()) {
            String sessionTitle = getString(R.string.app_name)+"下载链接";
            String sessionDescription = getString(R.string.app_name)+"下载链接";
            String sessionUrl = "https://www.pgyer.com/bh1Q";
            Bitmap sessionThumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            if(!TextUtils.isEmpty(title)){
                sessionTitle = title;
            }
            if (!TextUtils.isEmpty(content)) {
                sessionDescription = content;
            }
            if (!TextUtils.isEmpty(url)) {
                sessionUrl = url;
            }
            if (!TextUtils.isEmpty(imgUrl)) {
                try {
                    sessionThumb = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            boolean result = weChatShareUtil.shareUrl(sessionUrl, sessionTitle, sessionThumb, sessionDescription, SendMessageToWX.Req.WXSceneTimeline);
//            if (!result) {
//                ToastUtils.showToast(getActivity(),getActivity().getString(R.string.no_wechat));
//            }
        } else {
            ToastUtils.showToast(getActivity(),"手机上微信版本不支持分享到朋友圈");
        }

    }
    private boolean isWebchatAvaliable(Context context) {
        //检测手机上是否安装了微信
        try {
            context.getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private void goToShare(){
        if (!isWebchatAvaliable(getActivity())) {
            ToastUtils.showToast(getActivity(),"手机没有安装微信");
            return;
        }
        if (hasShared()) {
            ToastUtils.showToast(getActivity(),"你今天已经分享过了`(*∩_∩*)′");
            return;
        }
        AppAction.getShareAppConfigs(getActivity(), new OkhttpResponseHandler(getActivity(),HttpResponse.class,(BaseActivity)getActivity()) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                /**
                 * "code": 200,
                 "message": "微信分享配置",
                 "data": {
                 "wx_share_title": "杰臣诺",
                 "wx_share_content": "下载杰臣诺",
                 "wx_share_url": "http://www.pgyer.com/bh1Q",
                 "wx_share_img": null,
                 "can_wx_share": 1
                 }
                 */
                try {
                    JSONObject result = new JSONObject(responseString);
                    JSONObject data = result.optJSONObject("data");
                    final String title = data.optString("wx_share_title");
                    final String content = data.optString("wx_share_content");
                    final String url = data.optString("wx_share_url");
                    final String imgUrl = data.optString("wx_share_img");
                    int canShare = data.optInt("can_wx_share");
                    new MaterialDialog.Builder(getActivity())
                            .title("分享完成后,请选择返回本应用的按钮方可获得积分")
                            .items(R.array.pay_items)
                            .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    /**
                                     * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                     * returning false here won't allow the newly selected radio button to actually be selected.
                                     **/
                                    if(which==0){
                                        share(title,content,url,imgUrl);
                                    }else{
                                        shareTimeline(title,content,url,imgUrl);
                                    }
                                    return true;
                                }
                            })
                            .positiveText(R.string.choose)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShareSuccessEvent(Event.ShareSuccessEvent event) {
       AppAction.addPoint(getActivity(), Constants.ShareApp, 10, "", new OkhttpResponseHandler(getActivity(),HttpResponse.class,(BaseActivity)getActivity()) {
           @Override
           public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
               ToastUtils.showToast(getActivity(),"添加积分成功:"+10);
                shareAppSuccess();
           }
       });
    }
    private void shareAppSuccess(){
        ShareAppRecord shareAppRecord = new ShareAppRecord();
        shareAppRecord.setPhone(UserPF.getInstance().getPhone());
        shareAppRecord.setShareTime(System.currentTimeMillis());
        shareAppRecord.save();
        AppAction.ShareAppSuccessAction(getActivity(), new OkhttpResponseHandler(getActivity(),HttpResponse.class,(BaseActivity)getActivity()) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                    LogUtils.i("success:"+responseString);
            }
        });
    }
}
