package com.md.lib_network.okhttp.response.listener;

/**
 * 业务逻辑真正处理的地方，包括Java层以及业务层异常
 */
public interface DisposeDataListener {
    /**
     * 请求成功事件处理
     *
     * @param responseObj
     */
    void onSuccess(Object responseObj);

    /**
     * 请求失败事件处理
     *
     * @param responseObj
     */
    void onFailure(Object responseObj);
}
