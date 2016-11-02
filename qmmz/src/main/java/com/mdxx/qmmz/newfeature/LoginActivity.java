package com.mdxx.qmmz.newfeature;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.common.ViewUtil;

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
                break;
            case R.id.tvHelp:
                break;
            case R.id.btnSignUp:
                startActivityForResult(new Intent(mContext,RegisterActivity.class),0);
                break;
            default:
                break;
        }
    }
}
