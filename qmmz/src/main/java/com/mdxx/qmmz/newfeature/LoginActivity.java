package com.mdxx.qmmz.newfeature;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.common.Constants;
import com.mdxx.qmmz.common.UserPF;
import com.mdxx.qmmz.common.ViewUtil;
import com.mdxx.qmmz.network.AppAction;
import com.mdxx.qmmz.network.HttpResponse;
import com.mdxx.qmmz.network.HttpResponseHandler;
import com.mdxx.qmmz.newp.NMainActivity;
import com.mdxx.qmmz.utils.HexUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月02日 13:49
 * 邮箱：nianbin@mosainet.com
 */
public class LoginActivity extends BaseActivity {
    private TextView tvPhoneNumber, tvPassword, tvRegion, tvHelp;
    private EditText etPhoneNumber, etPassword;
    private ImageButton ibPhoneNumberClear, ibPasswordClear;
    private Button btnLogin,btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initListener();
        autoLogin();
    }
    private void initViews() {
        tvPhoneNumber = ViewUtil.findViewById(this, R.id.tvPhoneNumber);
        tvPassword = ViewUtil.findViewById(this, R.id.tvPassword);
        ibPasswordClear = ViewUtil.findViewById(this, R.id.ibPasswordClear);
        btnLogin = ViewUtil.findViewById(this, R.id.btnLogin);
        etPhoneNumber = ViewUtil.findViewById(this, R.id.etPhoneNumber);
        etPassword = ViewUtil.findViewById(this, R.id.etPassword);
        ibPhoneNumberClear = ViewUtil.findViewById(this, R.id.ibPhoneNumberClear);
        tvRegion = ViewUtil.findViewById(this, R.id.tvRegion);
        tvHelp = ViewUtil.findViewById(this, R.id.tvHelp);
        btnSignUp = ViewUtil.findViewById(this, R.id.btnSignUp);
    }
    private void initListener() {
        findViewById(R.id.tv_forgetpassword).setOnClickListener(this);
        tvRegion.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvHelp.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        ViewUtil.setEditorAction(etPassword, btnLogin);
        ViewUtil.setEdittextChangeVisibilityClearButton(etPhoneNumber, ibPhoneNumberClear, tvPhoneNumber);
        ViewUtil.setEdittextChangeVisibilityClearButton(etPassword, ibPasswordClear, tvPassword);
        ViewUtil.setPasswordFontDefault(etPassword);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                tvHelp.setVisibility(s.length() > 0 ? View.GONE : View.VISIBLE);
            }
        });
        findViewById(R.id.header_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if(TextUtils.isEmpty(etPhoneNumber.getText().toString())){
                    showToast(R.string.tip_empty_phone);
                    return;
                }
                if(TextUtils.isEmpty(etPassword.getText().toString())){
                    showToast(R.string.tip_empty_password);
                    return;
                }
                login(etPhoneNumber.getText().toString(), HexUtil.getEncryptedPwd(etPassword.getText().toString()),etPassword.getText().toString());
                break;
            case R.id.tvHelp:
                startActivityForResult(new Intent(mContext,ForgetPasswordActivity.class),0);
                break;
            case R.id.btnSignUp:
                startActivityForResult(new Intent(mContext,RegisterActivity.class),0);
                break;
            case R.id.tv_forgetpassword:
                startActivityForResult(new Intent(mContext,ForgetPasswordActivity.class),0);
                break;
            default:
                break;
        }
    }

    private void autoLogin(){
       String phone = UserPF.getInstance().getPhone();
        String password = UserPF.getInstance().getPassword();
        if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)){
            etPhoneNumber.setText(UserPF.getInstance().getPhone());
            etPassword.setText(UserPF.getInstance().getOriginPassword());
            login(phone,password,UserPF.getInstance().getOriginPassword());
        }
    }
    private void login(final String phone,final String password,final String originPassword) {
        AppAction.login(mContext, phone, password, new HttpResponseHandler(mContext,HttpResponse.class,LoginActivity.this) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                Intent intent = new Intent();
                intent.setClass(mContext, NMainActivity.class);
                startActivity(intent);
                showToast(R.string.tip_login_success);
                UserPF.getInstance().setPhone(phone);
                UserPF.getInstance().setPassword(password);
                UserPF.getInstance().setOriginPassword(originPassword);
                try {
                    JSONObject result = new JSONObject(responseString);
                    JSONObject data = result.optJSONObject("data");
                    String userid = data.optString("userid");
                    String token = data.optString("token");
                    UserPF.getInstance().setUserid(userid);
                    UserPF.getInstance().setToken(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK && data!=null){
            if(data.hasExtra(Constants.registerSuccess)){
                if(data.getBooleanExtra(Constants.registerSuccess,false)){
                    etPhoneNumber.setText(UserPF.getInstance().getPhone());
                    etPassword.setText(UserPF.getInstance().getOriginPassword());
                    login(UserPF.getInstance().getPhone(),UserPF.getInstance().getPassword(),UserPF.getInstance().getOriginPassword());
                }

            }
        }
    }
}
