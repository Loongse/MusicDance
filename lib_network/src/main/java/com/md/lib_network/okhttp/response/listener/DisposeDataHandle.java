package com.md.lib_network.okhttp.response.listener;

/**
 * 数据句柄处理
 */
public class DisposeDataHandle {
    public DisposeDownloadListener mListener = null;
    public Class<?> mClass = null;//数据类
    public String mSource = null;//文件保存路径

    public DisposeDataHandle(DisposeDownloadListener mListener) {
        this.mListener = mListener;
    }

    public DisposeDataHandle(DisposeDownloadListener mListener, Class<?> mClass) {
        this.mListener = mListener;
        this.mClass = mClass;
    }

    public DisposeDataHandle(DisposeDownloadListener mListener, String mSource) {
        this.mListener = mListener;
        this.mSource = mSource;
    }
}
