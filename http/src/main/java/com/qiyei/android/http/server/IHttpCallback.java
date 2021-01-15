package com.qiyei.android.http.server;

import com.qiyei.android.http.api.IHttpApi;

/**
 * @author Created by qiyei2015 on 2017/10/21.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: http内部回调
 */
public interface IHttpCallback<R,T extends IHttpApi>{

    /**
     * 请求开始
     */
    void onStart(HttpTask<R,T> task);

    /**
     * 成功的回调
     * @param response
     */
    void onSuccess(HttpResponse<R> response);

    /**
     * 失败的回调
     * @param exception
     */
    void onFailure(Exception exception);

}
