package com.mdxx.qmmz.newfeature;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;
import android.widget.Toast;

import com.mdxx.qmmz.R;


/**
 * 描述:
 * 作者：周年斌
 * 时间：16/11/27 18:56
 * 邮箱：zhounianbin@mastercom.cn
 */
public class Actions {
    public static void loginDuiba(final Activity context, String duibaUrl){
        Intent intent = new Intent();
        intent.setClass(context,DuibaActivity.class);
        intent.putExtra("navColor",context.getString(R.string.titlebar_color));    //配置导航条的背景颜色，请用#ffffff长格式。
        intent.putExtra("titleColor", "#ffffff");    //配置导航条标题的颜色，请用#ffffff长格式。
        intent.putExtra("url", duibaUrl);    //配置自动登陆地址，每次需服务端动态生成。
        context.startActivity(intent);

        DuibaActivity.creditsListener = new DuibaActivity.CreditsListener() {
            /**
             * 当点击分享按钮被点击
             * @param shareUrl 分享的地址
             * @param shareThumbnail 分享的缩略图
             * @param shareTitle 分享的标题
             * @param shareSubtitle 分享的副标题
             */
            public void onShareClick(WebView webView, String shareUrl,String shareThumbnail, String shareTitle,  String shareSubtitle) {
                //当分享按钮被点击时，会调用此处代码。在这里处理分享的业务逻辑。
                new AlertDialog.Builder(webView.getContext())
                        .setTitle("分享信息")
                        .setItems(new String[] {"标题："+shareTitle,"副标题："+shareSubtitle,"缩略图地址："+shareThumbnail,"链接："+shareUrl}, null)
                        .setNegativeButton("确定", null)
                        .show();
            }

            /**
             * 当点击“请先登录”按钮唤起登录时，会调用此处代码。
             * 用户登录后，需要将CreditsActivity.IS_WAKEUP_LOGIN变量设置为true。
             * @param webView 用于登录成功后返回到当前的webview刷新登录状态。
             * @param currentUrl 当前页面的url
             */
            public void onLoginClick(WebView webView, String currentUrl) {
                //当未登录的用户点击去登录时，会调用此处代码。
                //用户登录后，需要将CreditsActivity.IS_WAKEUP_LOGIN变量设置为true。
                //为了用户登录后能回到未登录前的页面（currentUrl）。
                //当用户登录成功后，需要重新请求一次服务端，带上currentUrl。
                //用该方法中的webview变量加载请求链接。
                //服务端收到请求后在生成免登录url时，将currentUrl放入redirect参数，通知客户端302跳转到新生成的免登录URL。
                new AlertDialog.Builder(webView.getContext())
                        .setTitle("跳转登录")
                        .setMessage("跳转到登录页面？")
                        .setPositiveButton("是", null)
                        .setNegativeButton("否", null)
                        .show();
            }

            /**
             * 当点击“复制”按钮时，触发该方法，回调获取到券码code
             * @param webView webview对象。
             * @param code 复制的券码
             */
            public void onCopyCode(WebView webView, String code) {
                //当未登录的用户点击去登录时，会调用此处代码。
                new AlertDialog.Builder(webView.getContext())
                        .setTitle("复制券码")
                        .setMessage("已复制，券码为："+code)
                        .setPositiveButton("是", null)
                        .setNegativeButton("否", null)
                        .show();
            }

            /**
             * 积分商城返回首页刷新积分时，触发该方法。
             */
            public void onLocalRefresh(WebView mWebView, String credits) {
                //String credits为积分商城返回的最新积分，不保证准确。
                //触发更新本地积分，这里建议用ajax向自己服务器请求积分值，比较准确。
                Toast.makeText(context, "触发本地刷新积分："+credits,Toast.LENGTH_SHORT).show();
            }
        };
    }
}
