package com.md.md_voice.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.md.lib_common_ui.base.BaseActivity;
import com.md.lib_network.okhttp.response.listener.DisposeDataListener;
import com.md.lib_network.okhttp.utils.ResponseEntityToModule;
import com.md.md_voice.R;
import com.md.md_voice.api.MockData;
import com.md.md_voice.api.RequestCenter;
import com.md.md_voice.view.login.manager.UserManager;
import com.md.md_voice.view.login.user.LoginEvent;
import com.md.md_voice.view.login.user.User;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseActivity implements DisposeDataListener {
    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestCenter.login(LoginActivity.this);
            }
        });
    }

    @Override
    public void onSuccess(Object responseObj) {
        //处理正常逻辑
        User user = (User) responseObj;
        UserManager.getInstance().saveUser(user);
        EventBus.getDefault().post(new LoginEvent());
        finish();
    }

    @Override
    public void onFailure(Object responseObj) {
        //登录失败逻辑
        onSuccess(ResponseEntityToModule.parseJsonToModule(MockData.LOGIN_DATA, User.class));
    }
}