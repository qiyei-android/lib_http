package com.qiyei.android.http.server;


import com.qiyei.android.http.api.IHttpApi;

import java.util.concurrent.Executor;

/**
 * @author Created by qiyei2015 on 2017/10/21.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: Https引擎
 */
public interface IHttpEngine {

    /**
     *
     * @param task
     * @param callback
     * @return 返回task id
     */
    <R,T extends IHttpApi> void enqueueCall(final HttpTask<R,T> task, final IHttpCallback<R,T> callback);

    /**
     *
     * @param task
     * @param callback
     * @return 返回task id
     */
    <R,T extends IHttpApi> void enqueueTransferCall(final HttpTask<R,T> task, final IHttpTransferCallback<R,T> callback);

    /**
     * 取消http请求
     * @param taskId
     */
    void cancelHttpCall(String taskId);

    /**
     * 获取主线程的Executor
     * @return
     */
    Executor getMainExecutor();
}
