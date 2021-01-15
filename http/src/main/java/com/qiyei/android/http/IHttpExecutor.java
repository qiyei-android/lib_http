package com.qiyei.android.http;



import com.qiyei.android.http.api.HttpRequest;
import com.qiyei.android.http.api.IHttpApi;
import com.qiyei.android.http.api.IHttpListener;


/**
 * @author Created by qiyei2015 on 2017/10/21.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public interface IHttpExecutor {

    /**
     * 执行https请求
     * @param request 请求参数
     * @param listener 回调listener
     * @param <T> 请求接口泛型参数
     * @param <R> 响应泛型参数
     * @return 该任务的taskId
     */
    <R,T extends IHttpApi> String execute(HttpRequest<R,T> request, IHttpListener<R> listener);

    /**
     * 取消网络请求
     * @param taskId 需要取消的 taskId
     */
    void cancel(String taskId);
}
