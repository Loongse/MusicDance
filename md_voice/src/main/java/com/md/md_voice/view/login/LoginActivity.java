package com.md.md_voice.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.md.lib_common_ui.base.BaseActivity;
import com.md.lib_network.okhttp.response.listener.DisposeDataListener;
import com.md.md_voice.R;
import com.md.md_voice.api.RequestCenter;

public class LoginActivity extends BaseActivity {
    public static void start(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestCenter.login(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {

                    }

                    @Override
                    public void onFailure(Object responseObj) {

                    }
                });
            }
        });
    }
}