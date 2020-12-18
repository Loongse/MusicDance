package com.md.lib_network.okhttp.response.listener;

/**
 * 下载进度监听
 */
public interface DisposeDownloadListener extends DisposeDataListener{
    void onProgress(int progress);
}
