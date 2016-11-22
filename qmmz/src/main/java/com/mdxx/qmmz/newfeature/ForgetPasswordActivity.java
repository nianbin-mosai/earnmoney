package com.mdxx.qmmz.newfeature;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.common.GlobalUtils;
import com.mdxx.qmmz.common.ToastUtils;
import com.mdxx.qmmz.common.UserPF;
import com.mdxx.qmmz.common.ViewUtil;
import com.mdxx.qmmz.network.AppAction;
import com.mdxx.qmmz.network.HttpResponse;
import com.mdxx.qmmz.network.OkhttpResponseHandler;
import com.mdxx.qmmz.utils.HexUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordActivity extends BaseActivity {
    private TextView tvPhoneNumber, tvPassword, tvPasswordShow, tvConfirmPassword, tvConfirmPasswordShow, tvRegion;
    private EditText etPhoneNumber, etPassword, etConfirmPassword, etCode;
    private ImageButton ibPhoneNumberClear;
    private Button btnSubmit, btnGetCode;

    private static final int GET_CODE_SCHEDULE = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_CODE_SCHEDULE:
                    int time = (int) btnGetCode.getTag();
                    if((--time) == 0){
                        btnGetCode.setClickable(true);
                        btnGetCode.setText(R.string.get_verification_code);
                    }else{
                        btnGetCode.setTag(time);
                        btnGetCode.setText(getString(R.string.seconds, time));
                        handler.sendEmptyMessageDelayed(GET_CODE_SCHEDULE, 1000);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initViews();
        initListener();
        initData();
    }
    private void initViews() {
        tvPhoneNumber = ViewUtil.findViewById(this, R.id.tvPhoneNumber);
        etPhoneNumber = ViewUtil.findViewById(this, R.id.etPhoneNumber);
        ibPhoneNumberClear = ViewUtil.findViewById(this, R.id.ibPhoneNumberClear);
        tvPassword = ViewUtil.findViewById(this, R.id.tvPassword);
        tvPasswordShow = ViewUtil.findViewById(this, R.id.tvPasswordShow);
        etPassword = ViewUtil.findViewById(this, R.id.etPassword);
        tvConfirmPassword = ViewUtil.findViewById(this, R.id.tvConfirmPassword);
        tvConfirmPasswordShow = ViewUtil.findViewById(this, R.id.tvConfirmPasswordShow);
        etConfirmPassword = ViewUtil.findViewById(this, R.id.etConfirmPassword);
        btnGetCode = ViewUtil.findViewById(this, R.id.btnGetCode);
        etCode  = ViewUtil.findViewById(this, R.id.etCode);
        tvRegion = ViewUtil.findViewById(this, R.id.tvRegion);
        btnSubmit = ViewUtil.findViewById(this, R.id.btnSubmit);
    }

    private void initListener() {
        btnSubmit.setOnClickListener(this);
        btnGetCode.setOnClickListener(this);
        tvRegion.setOnClickListener(this);
        ViewUtil.setEdittextChangeVisibilityClearButton(etPhoneNumber, ibPhoneNumberClear, tvPhoneNumber);
        ViewUtil.setPasswordShow(etPassword, tvPasswordShow, tvPassword);
        ViewUtil.setPasswordShow(etConfirmPassword, tvConfirmPasswordShow, tvConfirmPassword);
        ViewUtil.setPasswordFontDefault(etPassword);
        ViewUtil.setPasswordFontDefault(etConfirmPassword);
        ViewUtil.setEditorAction(etConfirmPassword, btnSubmit);
        findViewById(R.id.header_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegion:
                startActivityForResult(new Intent(mContext, RegisterActivity.class), 0);
                break;
            case R.id.btnSubmit:
                submit();
                break;
            case R.id.btnGetCode:
                String phone = checkPhone();
                if(phone == null){
                    showToast(R.string.tip_empty_phone);
                    return;
                }
                AppAction.getVerifyCode(mContext,etPhoneNumber.getText().toString(),"",new OkhttpResponseHandler(mContext,HttpResponse.class,ForgetPasswordActivity.this) {
                    @Override
                    public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                        try {
                            JSONObject result = new JSONObject(responseString);
                            UserPF.getInstance().setLogId(result.optString("log_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showToast(R.string.tip_verifycode_sending);
                    }

                });
                break;
            default:
                break;
        }
    }

    private void submit() {
        final String phone = checkPhone();
        if(phone == null){
            showHintMessages(getString(R.string.tip_empty_phone));
            return;
        }
        if(TextUtils.isEmpty(etCode.getText())){
            showHintMessages(R.string.verification_code_cannot_be_empty);
            return;
        }
        final String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            showHintMessages(R.string.password_cannot_be_empty);
            return;
        }
        if (password.length() < getResources().getInteger(R.integer.password_min_length)
                || password.length() > getResources().getInteger(R.integer.password_max_length)) {
            showHintMessages(R.string.password_verify);
            return;
        }
        if(!etConfirmPassword.getText().toString().equals(password)){
            showHintMessages(R.string.confirm_password_verify);
            return;
        }
        AppAction.checkVerifyCode(mContext, etPhoneNumber.getText().toString(), etCode.getText().toString(),"", new OkhttpResponseHandler(mContext,HttpResponse.class,ForgetPasswordActivity.this) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                AppAction.forgetPassword(mContext,etPhoneNumber.getText().toString(), HexUtil.getEncryptedPwd(etPassword.getText().toString()),etCode.getText().toString(),UserPF.getInstance().getLogId(), new OkhttpResponseHandler(mContext, HttpResponse.class, ForgetPasswordActivity.this) {
                    @Override
                    public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                        ToastUtils.showToast(mContext,"修改密码成功");
                    }
                });
            }
        });
    }

    private String checkPhone(){
        final String phone = etPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showHintMessages(R.string.phone_number_cannot_be_empty);
            return null;
        }
        if (!GlobalUtils.isPhone(phone)) {
            showHintMessages(R.string.phone_number_format_error);
            return null;
        }
        return phone;
    }
}
