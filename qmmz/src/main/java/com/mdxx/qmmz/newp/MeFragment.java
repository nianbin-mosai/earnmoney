package com.mdxx.qmmz.newp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.common.LogUtils;
import com.mdxx.qmmz.common.UserPF;
import com.mdxx.qmmz.network.AppAction;
import com.mdxx.qmmz.network.HttpResponse;
import com.mdxx.qmmz.network.OkhttpResponseHandler;
import com.mdxx.qmmz.newfeature.LoginActivity;
import com.mdxx.qmmz.newfeature.MemberActivity;
import com.mdxx.qmmz.newfeature.bean.WebViewConfigs;
import com.pgyersdk.activity.FeedbackActivity;
import com.pgyersdk.feedback.PgyFeedback;

import org.json.JSONException;
import org.json.JSONObject;

public class MeFragment extends Fragment implements OnClickListener {
    private WebViewConfigs webViewConfigs = new WebViewConfigs();
    private Animation loadAnimation;
    private ImageView refresh_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        view.findViewById(R.id.btn_logout).setOnClickListener(this);
        view.findViewById(R.id.rl_member).setOnClickListener(this);
        view.findViewById(R.id.rl_feedback).setOnClickListener(this);
        refresh_date = (ImageView) view.findViewById(R.id.refresh_date);
        refresh_date.setOnClickListener(this);
        loadAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.refresh_date_anim);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh_date:
                refresh_date.startAnimation(loadAnimation);
                getWebViewConfigs();
                break;

            case R.id.btn_logout:
                logout();
                break;
            case R.id.rl_member:
                if (!TextUtils.isEmpty(webViewConfigs.member)) {
                    Intent intent = new Intent(getActivity(), MemberActivity.class);
                    intent.putExtra("url", getFormatUrl(webViewConfigs.member));
                    LogUtils.i(getFormatUrl(webViewConfigs.member));
                    startActivityForResult(intent, 0);
                }
                break;
            case R.id.rl_feedback:
                    // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
//                  打开沉浸式,默认为false
                // 设置顶部导航栏和底部bar的颜色
			FeedbackActivity.setBarBackgroundColor(getString(R.string.titlebar_color));
              FeedbackActivity.setBarImmersive(true);
                PgyFeedback.getInstance().showActivity(getActivity());
                break;
        }
    }


    private void logout() {
        new MaterialDialog.Builder(getActivity()).title(getString(R.string.tip_logout))
                .negativeText(getString(R.string.cancel))
                .positiveText(getString(R.string.ok))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        UserPF.getInstance().logout();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                })
                .show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWebViewConfigs();
    }

    private void getWebViewConfigs() {
        AppAction.getWebViewConfigs(getActivity(), new OkhttpResponseHandler(getActivity(), HttpResponse.class, (BaseActivity) getActivity()) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }

    private String getFormatUrl(String url) {
        return String.format("%s?userid=%s&token=%s", url, UserPF.getInstance().getUserid(), UserPF.getInstance().getToken());
    }
}
