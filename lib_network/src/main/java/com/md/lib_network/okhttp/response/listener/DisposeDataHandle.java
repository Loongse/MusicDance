package com.md.lib_network.okhttp.response.listener;

/**
 * 数据句柄处理
 */
public class DisposeDataHandle {
    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;//数据类
    public String mSource = null;//文件保存路径

    public DisposeDataHandle(DisposeDataListener mListener) {
        this.mListener = mListener;
    }

    public DisposeDataHandle(DisposeDataListener mListener, Class<?> mClass) {
        this.mListener = mListener;
        this.mClass = mClass;
    }

    public DisposeDataHandle(DisposeDataListener mListener, String mSource) {
        this.mListener = mListener;
        this.mSource = mSource;
    }
}
