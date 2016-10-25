package com.wyu.earnmoney;

import android.app.Activity;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.wyu.earnmoney.utils.CrashCatcher;

public class App extends MultiDexApplication {

    public static App INSTANCE;
    private int appCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        setActivityLifecycleCallbacks();
        initCrashCatcher();
    }

    private void initCrashCatcher() {
        // 崩溃捕捉
        final CrashCatcher crashCatcher = new CrashCatcher(this);
        crashCatcher.register(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                // 崩溃时自动重启，不弹出 crash dialog
//                crashCatcher.restart(SplashActivity.class);
            }
        });
    }


    //检测app是否在前台或者后台的
    private void setActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public boolean isHome() {
        return appCount > 0;
    }
}
