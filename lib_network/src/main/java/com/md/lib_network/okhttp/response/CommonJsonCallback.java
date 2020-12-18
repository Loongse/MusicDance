package com.md.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.md.lib_network.okhttp.exception.OkHttpException;
import com.md.lib_network.okhttp.response.listener.DisposeDataHandle;
import com.md.lib_network.okhttp.response.listener.DisposeDataListener;
import com.md.lib_network.okhttp.utils.ResponseEntityToModule;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 用于返回json格式响应
 */
public class CommonJsonCallback implements Callback {
    protected final String EMPTY_MSG = "";
    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknown error
    /**
     * 将其他线程的数据转发到UI线程
     */
    private DisposeDataListener mListener;
    private Class<?> mClass;
    private Handler mDeliveryHandler;

    public CommonJsonCallback(DisposeDataHandle handle) {
        mListener = handle.mListener;
        mClass = handle.mClass;
        mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(String result) {
        if (result == null || result.trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        try {
            //此处不需要进行解析
            if (mClass == null) {
                mListener.onSuccess(result);
            } else {
                //解析威实体对象，可以使用gson以及fastjson代替
                Object obj = ResponseEntityToModule.parseJsonToModule(result, mClass);
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e));
        }
    }
}
