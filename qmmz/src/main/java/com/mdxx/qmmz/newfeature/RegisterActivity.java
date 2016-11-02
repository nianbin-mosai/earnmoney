package com.mdxx.qmmz.newfeature;

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
import com.mdxx.qmmz.common.ViewUtil;


public class RegisterActivity extends BaseActivity {
    private TextView tvName, tvPhoneNumber, tvPassword, tvPasswordShow, tvConfirmPassword, tvConfirmPasswordShow, tvRegion;
    private EditText etName, etPhoneNumber, etPassword, etConfirmPassword, etCode;
    private ImageButton ibNameClear, ibPhoneNumberClear;
    private Button btnSignUp, btnGetCode;

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
        setContentView(R.layout.activity_register);
        initViews();
        initListener();
    }
    private void initViews() {
        tvName = ViewUtil.findViewById(this, R.id.tvName);
        etName = ViewUtil.findViewById(this, R.id.etName);
        ibNameClear = ViewUtil.findViewById(this, R.id.ibNameClear);
        btnSignUp = ViewUtil.findViewById(this, R.id.btnSignUp);
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
    }

    private void initListener() {
        btnSignUp.setOnClickListener(this);
        btnGetCode.setOnClickListener(this);
        tvRegion.setOnClickListener(this);
        ViewUtil.setEdittextChangeVisibilityClearButton(etName, ibNameClear, tvName);
        ViewUtil.setEdittextChangeVisibilityClearButton(etPhoneNumber, ibPhoneNumberClear, tvPhoneNumber);
        ViewUtil.setPasswordShow(etPassword, tvPasswordShow, tvPassword);
        ViewUtil.setPasswordShow(etConfirmPassword, tvConfirmPasswordShow, tvConfirmPassword);
        ViewUtil.setPasswordFontDefault(etPassword);
        ViewUtil.setPasswordFontDefault(etConfirmPassword);
        ViewUtil.setEditorAction(etConfirmPassword, btnSignUp);
        findViewById(R.id.header_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                register();
                break;
            case R.id.btnGetCode:
                String phone = checkPhone();
                if(phone == null){
                    return;
                }
                break;
            default:
                break;
        }
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
    private void register() {
        final String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showHintMessages(R.string.name_cannot_be_empty);
            return;
        }
        final String phone = checkPhone();
        if(phone == null){
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
        final String areaCode = tvRegion.getText().toString();
    }
}
