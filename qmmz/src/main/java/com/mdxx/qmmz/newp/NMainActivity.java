package com.mdxx.qmmz.newp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.common.Configs;
import com.mdxx.qmmz.common.LogUtils;
import com.mdxx.qmmz.common.UserPF;
import com.mdxx.qmmz.utils.InterfaceTool;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.yow.PointListener;
import com.yow.YoManage;

import java.util.ArrayList;
import java.util.List;

import tj.zl.op.AdManager;
import tj.zl.op.listener.Interface_ActivityListener;
import tj.zl.op.onlineconfig.OnlineConfigCallBack;
import tj.zl.op.os.EarnPointsOrderInfo;
import tj.zl.op.os.EarnPointsOrderList;
import tj.zl.op.os.OffersBrowserConfig;
import tj.zl.op.os.OffersManager;
import tj.zl.op.os.PointsChangeNotify;
import tj.zl.op.os.PointsEarnNotify;
import tj.zl.op.os.PointsManager;

public class NMainActivity extends BaseActivity implements OnClickListener, PointsChangeNotify, PointsEarnNotify {
    public List<Fragment> fragments = new ArrayList<Fragment>();
    public List<TextView> textList = new ArrayList<TextView>();
    private RelativeLayout home_layout;
    private RelativeLayout rank_layout;
    //	private RelativeLayout zhuan_layout;
    private RelativeLayout dui_layout;
    private RelativeLayout me_layout;
    public int position;
    private int currentTab;
    private ImageView sy_image;
    private ImageView ph_image;
    //	private ImageView z_image;
    private ImageView dh_image;
    private ImageView wo_image;
    private long mExitTime;
    private Dialog logindialog;
    private AlertDialog dialog_yaoqing;
    private final String loginurl = InterfaceTool.ULR + "user/login";
    private final String yaoqingurl = InterfaceTool.ULR + "user/yaoqing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmain);
        initUI();
        initYoumi();
        initYow();
        initTabData();
        checkVersion();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    private void initUI() {
        sy_image = (ImageView) findViewById(R.id.sy_image);
        ph_image = (ImageView) findViewById(R.id.ph_image);
//		z_image = (ImageView) findViewById(R.id.z_image);
        dh_image = (ImageView) findViewById(R.id.dh_image);
        wo_image = (ImageView) findViewById(R.id.wo_image);
        TextView sy_txt = (TextView) findViewById(R.id.sy);
        TextView ph_txt = (TextView) findViewById(R.id.ph);
        TextView dh_txt = (TextView) findViewById(R.id.dh);
        TextView wo_txt = (TextView) findViewById(R.id.wo);
        textList.add(sy_txt);
        textList.add(ph_txt);
//		textList.add(null);
        textList.add(dh_txt);
        textList.add(wo_txt);
        home_layout = (RelativeLayout) findViewById(R.id.home_layout);
        rank_layout = (RelativeLayout) findViewById(R.id.rank_layout);
//		zhuan_layout = (RelativeLayout) findViewById(R.id.zhuan_layout);
        dui_layout = (RelativeLayout) findViewById(R.id.dui_layout);
        me_layout = (RelativeLayout) findViewById(R.id.me_layout);
        home_layout.setOnClickListener(this);
        rank_layout.setOnClickListener(this);
//		zhuan_layout.setOnClickListener(this);
        dui_layout.setOnClickListener(this);
        me_layout.setOnClickListener(this);
    }

    private void initTabData() {
        if (fragments.size() > 0) {
            fragments.clear();
        }
        HomeFragment homeFragment = new HomeFragment();
        RankFragment rankFragment = new RankFragment();
//		ZhuanFragment zhuanFragment = new ZhuanFragment();
        DuiFragment duiFragment = new DuiFragment();
        MeFragment meFragment = new MeFragment();
        fragments.add(homeFragment);
        fragments.add(rankFragment);
//		fragments.add(zhuanFragment);
        fragments.add(duiFragment);
        fragments.add(meFragment);

        String p = getIntent().getStringExtra("position");
        if (p == null) {
            p = "0";
        }
        position = Integer.valueOf(p);
        FragmentTransaction ft = this.getSupportFragmentManager()
                .beginTransaction();
        ft.add(R.id.tab_content, fragments.get(position));
        ft.commit();
        updataTabBg(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_layout:
                position = 0;
                break;
            case R.id.rank_layout:
                position = 1;
                break;
//		case R.id.zhuan_layout:
//			position = 2;
//			break;
            case R.id.dui_layout:
                position = 2;
                break;
            case R.id.me_layout:
                position = 3;
                break;
        }
        updataTabBg(position);
        setCheckedChange(position);
    }

    private void setCheckedChange(int pos) {
        Fragment fragment = fragments.get(pos);
        FragmentTransaction ft = obtainFragmentTransaction(pos);
        getCurrentFragment().onPause();
        if (fragment.isAdded()) {
            fragment.onResume();
        } else {
            ft.add(R.id.tab_content, fragment);
        }
        showTab(pos);
        ft.commit();
    }

    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = this.getSupportFragmentManager()
                .beginTransaction();
        if (index > currentTab) {
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            ft.setCustomAnimations(R.anim.slide_right_in,
                    R.anim.slide_right_out);
        }
        return ft;
    }

    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    private void updataTabBg(int pos) {
        switch (pos) {
            case 0:
                sy_image.setBackgroundResource(R.drawable.home_pre);
                ph_image.setBackgroundResource(R.drawable.rank_normal);
//			z_image.setBackgroundResource(R.drawable.zzzzzzzz);
                dh_image.setBackgroundResource(R.drawable.money_normal);
                wo_image.setBackgroundResource(R.drawable.my_normal);
                break;
            case 1:
                sy_image.setBackgroundResource(R.drawable.home_normal);
                ph_image.setBackgroundResource(R.drawable.rank_press);
//			z_image.setBackgroundResource(R.drawable.zzzzzzzz);
                dh_image.setBackgroundResource(R.drawable.money_normal);
                wo_image.setBackgroundResource(R.drawable.my_normal);
                break;
//		case 2:
//			sy_image.setBackgroundResource(R.drawable.home_normal);
//			ph_image.setBackgroundResource(R.drawable.rank_normal);
//			z_image.setBackgroundResource(R.drawable.zzzzz);
//			dh_image.setBackgroundResource(R.drawable.money_normal);
//			wo_image.setBackgroundResource(R.drawable.my_normal);
//			break;
            case 2:
                sy_image.setBackgroundResource(R.drawable.home_normal);
                ph_image.setBackgroundResource(R.drawable.rank_normal);
//			z_image.setBackgroundResource(R.drawable.zzzzzzzz);
                dh_image.setBackgroundResource(R.drawable.money_press);
                wo_image.setBackgroundResource(R.drawable.my_normal);
                break;
            case 3:
                sy_image.setBackgroundResource(R.drawable.home_normal);
                ph_image.setBackgroundResource(R.drawable.rank_normal);
//			z_image.setBackgroundResource(R.drawable.zzzzzzzz);
                dh_image.setBackgroundResource(R.drawable.money_normal);
                wo_image.setBackgroundResource(R.drawable.my_pre);
                break;

        }
        for (int i = 0; i < fragments.size(); i++) {
//			if (i != 2) {
            if (i == pos) {
                textList.get(i).setTextColor(
                        this.getResources().getColor(R.color.blue));
            } else {
                textList.get(i).setTextColor(
                        this.getResources().getColor(R.color.black));
            }
//			}
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        // super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 1500) {
            Toast.makeText(this, "再点击一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
    private void checkVersion(){
//        PgyUpdateManager.register(this);
        PgyUpdateManager.register(NMainActivity.this,
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(mContext)
                                .setTitle("更新")
                                .setMessage("")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startDownloadTask(
                                                        NMainActivity.this,
                                                        appBean.getDownloadURL());
                                            }
                                        }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragments.clear();
        // （可选）注销积分监听
        // 如果在onCreate调用了PointsManager.getInstance(this).registerNotify(this)进行积分余额监听器注册，那这里必须得注销
        PointsManager.getInstance(this).unRegisterNotify(this);

        // （可选）注销积分订单赚取监听
        // 如果在onCreate调用了PointsManager.getInstance(this).registerPointsEarnNotify(this)进行积分订单赚取监听器注册，那这里必须得注销
        PointsManager.getInstance(this).unRegisterPointsEarnNotify(this);

        // 回收积分广告占用的资源
        OffersManager.getInstance(this).onAppExit();
        PgyUpdateManager.unregister();
    }

    private void issim() {
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (manager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
            Toastshow("请先插入SIM卡");
            finish();
        }
    }









    private void initYoumi() {
        // 自v6.3.0起，所有其他代码必须在初始化接口调用之后才能生效
        // 初始化接口，应用启动的时候调用(appId, appSecret, isTestModel, isEnableYoumiLog)
        AdManager.getInstance(this).init(Configs.YMAppId, Configs.YMAppSecret, false, true);

        // 有米android 积分墙sdk 5.0.0之后支持定制浏览器顶部标题栏的部分UI
        setOfferBrowserConfig();

        // 如果开发者使用积分墙的服务器回调,
        // 1.需要告诉sdk，现在采用服务器回调
        // 2.建议开发者传入自己系统中用户id（如：邮箱账号之类的）（请限制在50个字符串以内）
        // 3.务必在下面的OffersManager.getInstance(this).onAppLaunch();代码之前声明使用服务器回调

         OffersManager.getInstance(this).setUsingServerCallBack(false);
        // OffersManager.getInstance(this).setCustomUserId("user_id");

        // 如果使用积分广告，请务必调用积分广告的初始化接口:
        OffersManager.getInstance(this).onAppLaunch();

        // (可选)注册积分监听-随时随地获得积分的变动情况
        PointsManager.getInstance(this).registerNotify(this);

        // (可选)注册积分订单赚取监听（sdk v4.10版本新增功能）
        PointsManager.getInstance(this).registerPointsEarnNotify(this);

        // (可选)设置是否在通知栏显示下载相关提示。默认为true，标识开启；设置为false则关闭。（sdk v4.10版本新增功能）
        // AdManager.getInstance(this).setIsDownloadTipsDisplayOnNotification(false);

        // (可选)设置安装完成后是否在通知栏显示已安装成功的通知。默认为true，标识开启；设置为false则关闭。（sdk v4.10版本新增功能）
        // AdManager.getInstance(this).setIsInstallationSuccessTipsDisplayOnNotification(false);

        // (可选)设置是否在通知栏显示积分赚取提示。默认为true，标识开启；设置为false则关闭。
        // 如果开发者采用了服务器回调积分的方式，那么本方法将不会生效
        // PointsManager.getInstance(this).setEnableEarnPointsNotification(false);

        // (可选)设置是否开启积分赚取的Toast提示。默认为true，标识开启；设置为false这关闭。
        // 如果开发者采用了服务器回调积分的方式，那么本方法将不会生效
        // PointsManager.getInstance(this).setEnableEarnPointsToastTips(false);

        // -------------------------------------------------------------------------------------------
        // 积分墙SDK 5.3.0 新增分享任务，下面为新增接口

        // (可选) 获取当前应用的签名md5字符串，可用于申请微信appid时使用
        // 注意：获取是确保应用采用的是发布版本的签名而不是debug签名
        LogUtils.i(String.format("包名：%s\n签名md5值：%s", this.getPackageName(),
                OffersManager.getInstance(this).getSignatureMd5String()));

        // (重要) 如果开发者需要开启分享任务墙，需要调用下面代码以注册微信appid（这里的appid为贵应用在微信平台上注册获取得到的appid）
        // 1. 微信的appid，请开发者在微信官网上自行注册
        // 2. 如果注册失败(返回false)，请参考/doc/有米AndroidSDK常见问题.html
//        boolean isRegisterSuccess = OffersManager.getInstance(this).registerToWx("wxbe86d519b643cf08");
//        Toast.makeText(this, String.format("注册微信appid %s", isRegisterSuccess ? "成功" : "失败"), Toast.LENGTH_SHORT).show();

        // 查询积分余额
        // 从5.3.0版本起，客户端积分托管将由 int 转换为 float
//        float pointsBalance = PointsManager.getInstance(this).queryPoints();
//		mTextViewPoints.setText("积分余额：" + pointsBalance);

        boolean isSuccess = OffersManager.getInstance(mContext).checkOffersAdConfig();
        LogUtils.i(isSuccess+"");
    }

    /**
     * 设置积分墙浏览器标题栏样式
     */
    private void setOfferBrowserConfig() {

        // 设置标题栏——标题
        OffersBrowserConfig.getInstance(this).setBrowserTitleText("秒取积分");

        // 设置标题栏——背景颜色（ps：积分墙标题栏默认背景颜色为#FFBB34）
//		OffersBrowserConfig.getInstance(this).setBrowserTitleBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        // 设置标题栏——是否显示积分墙右上角积分余额区域 true：是 false：否
        OffersBrowserConfig.getInstance(this).setPointsLayoutVisibility(true);

    }

    /**
     * 积分余额发生变动时，就会回调本方法（本回调方法执行在UI线程中）
     * <p/>
     * 从5.3.0版本起，客户端积分托管将由 int 转换为 float
     */
    @Override
    public void onPointBalanceChange(float pointsBalance) {
        LogUtils.i("(onPointBalanceChange)有米积分余额：" + pointsBalance);
//		mTextViewPoints.setText("积分余额：" + pointsBalance);
        showYoumiPoints();
    }

    /**
     * 积分订单赚取时会回调本方法（本回调方法执行在UI线程中）
     */
    @Override
    public void onPointEarn(Context context, EarnPointsOrderList list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        // 遍历订单并且toast提示
        for (int i = 0; i < list.size(); ++i) {
            EarnPointsOrderInfo info = list.get(i);
            LogUtils.i("onPointEarn:"+info.getMessage());
            Toast.makeText(this, info.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void showYowOffersWall(){
        YoManage.showOffer(this, UserPF.getInstance().getPhone());
        showYowPoints();
    }
    public void showYoumiOffersWall() {
//        getWYOnlineConfigs();
        checkConfig();
        tj.zl.op.os.OffersManager.getInstance(this).showOffersWall(new Interface_ActivityListener() {

            /**
             * 当积分墙销毁的时候，即积分墙的Activity调用了onDestory的时候回调
             */
            @Override
            public void onActivityDestroy(Context context) {
//                Toast.makeText(context, "全屏积分墙退出了", Toast.LENGTH_SHORT).show();
            }
        });
        showYoumiPoints();

    }
    private void getWYOnlineConfigs(){
        Toast.makeText(this, "获取在线参数中...", Toast.LENGTH_LONG).show();

        // 注意：这里获取的在线参数的key为 ：isOpen，为演示的key ， 开发者需要将key替换为开发者在自己有米后台上面设置的key
        tj.zl.op.AdManager.getInstance(this).asyncGetOnlineConfig(Configs.YMAppSecret, new OnlineConfigCallBack() {

            /**
             * 获取在线参数成功就会回调本方法（本回调方法执行在UI线程中）
             */
            @Override
            public void onGetOnlineConfigSuccessful(String key, String value) {
                // 获取在线参数成功
//                showToast(String.format("在线参数获取结果：\nkey=%s, value=%s", key, value));
                LogUtils.i(String.format("在线参数获取结果：\nkey=%s, value=%s", key, value));
                // //
                // 开发者在这里可以判断一下获取到的value值，然后设置一个boolean值并将其保存在文件中，每次调用广告之前从文件中获取boolean
                // 值并判断一下是否可以展示广告
                // if (key.equals("isOpen")) {
                // if (value.equals("1")) {
                // // 如果满足开发者自己的定义：如示例中如果key=isOpen value=1 则定义为开启广告
                // // 则将flag（boolean值）设置为true，然后每次调用广告代码之前都判断一下flag，如果flag为true则执行展示广告的代码
                // flag = true;
                // // 写入文件 ...
                // }
                // }

            }

            /**
             * 获取在线参数失败就会回调本方法（本回调方法执行在UI线程中）
             */
            @Override
            public void onGetOnlineConfigFailed(String key) {
                // 获取在线参数失败，可能原因有：键值未设置或为空、网络异常、服务器异常
//                showToast(String.format("在线参数获取结果：\n获取在线key=%s失败!\n具体失败原因请查看Log，Log标签：YoumiSdk", key));
                LogUtils.i(String.format("在线参数获取结果：\n获取在线key=%s失败!\n具体失败原因请查看Log，Log标签：YoumiSdk", key));
            }
        });
    }
    /**
     * 检查广告配置
     */
    private void checkConfig() {
        StringBuilder sb = new StringBuilder();

        addTextToSb(sb, tj.zl.op.os.OffersManager.getInstance(this).checkOffersAdConfig() ? "广告配置结果：正常" :
                "广告配置结果：异常，具体异常请查看Log，Log标签：YoumiSdk");
        addTextToSb(sb, "%s服务器回调", tj.zl.op.os.OffersManager.getInstance(this).isUsingServerCallBack() ? "已经开启" : "没有开启");
        addTextToSb(sb, "%s通知栏下载相关的通知",
                tj.zl.op.AdManager.getInstance(this).isDownloadTipsDisplayOnNotification() ? "已经开启" : "没有开启");
        addTextToSb(sb, "%s通知栏安装成功的通知",
                tj.zl.op.AdManager.getInstance(this).isInstallationSuccessTipsDisplayOnNotification() ? "已经开启" : "没有开启");
        addTextToSb(sb, "%s通知栏赚取积分的提示",
                tj.zl.op.os.PointsManager.getInstance(this).isEnableEarnPointsNotification() ? "已经开启" : "没有开启");
        addTextToSb(sb, "%s积分赚取的Toast提示",
                tj.zl.op.os.PointsManager.getInstance(this).isEnableEarnPointsToastTips() ? "已经开启" : "没有开启");

//        new AlertDialog.Builder(this).setTitle("检查结果").setMessage(sb.toString())
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//
//                }).create().show();
        LogUtils.i(sb.toString());
    }

    /**
     * 格式化字符串
     */
    private void addTextToSb(StringBuilder sb, String format, Object... args) {
        sb.append(String.format(format, args));
        sb.append(System.getProperty("line.separator"));
    }

    private void initYow(){
        // 初始化积分墙
        YoManage.getInstance(this, Configs.YowId, "ch").init();
        // 设置积分奖励回调,如果不需要回调可以不用设置
        YoManage.setGivePointListener(pointListener);
    }
    PointListener pointListener = new PointListener() {

        //奖励用户 虚拟币
        @Override
        public void givePointResult(int points, int totalPoints) {
            LogUtils.i("givePointResult points=" + points + "#totalPoints=" + totalPoints);
            // points 奖励积分数
            // totalPoints 当前总积分数
            showToast("完成任务获得积分：" + points + "#当前总积分=" + totalPoints);
            showYowPoints();
        }


        //获取用户“虚拟币”
        @Override
        public void getPointResult(int points) {
            LogUtils.i("getPointResult points=" + points);
            // points 当前用户虚拟币数
            showToast("当前积分数：" + points);
            LogUtils.i("聚优积分余额：" + points);

        }

        //扣除玩家“虚拟币”
        @Override
        public void consumePointResult(int points, int status) {
            LogUtils.i("consumePointResult points=" + points + "#status=" + status);
            // points 要扣除的 虚拟币
            // status 0：成功 1:失败 2:用户虚拟币余额不足
            showToast("消费：" + points + "积分#status=" + status);
            showYowPoints();
        }

        //手动奖励用户虚拟币
        @Override
        public void rewardPointResult(String unitName, int totalPoints, int status) {
            LogUtils.i("rewardPointResult unitName=" + unitName + "#totalPoints=" + totalPoints + "#status=" + status);
            // unitName 当前积分名称
            // totalPoints 当前用户总积分数
            // status 0:奖励成功 ,1:奖励失败
            showToast("奖励：" + unitName + "#totalPoints=" + totalPoints + "#status=" + status);
            showYowPoints();
        }
    };
    private void showYowPoints(){
        YoManage.getPoints(pointListener);
    }
    private void showYoumiPoints(){
        float pointsBalance = tj.zl.op.os.PointsManager.getInstance(this).queryPoints();
        LogUtils.i("有米积分余额：" + pointsBalance);
    }
}
