package com.qiyei.android.http.api;


import com.qiyei.android.http.IHttpExecutor;
import com.qiyei.android.http.server.HttpServerProxy;


/**
 * @author Created by qiyei2015 on 2017/10/21.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class HttpManager implements IHttpExecutor {

    /**
     * HttpServer 服务代理
     */
    private HttpServerProxy mProxy;

    /**
     * 构造方法
     */
    public HttpManager(){
        mProxy = new HttpServerProxy();
    }

    /**
     * 执行https请求
     * @param request 请求参数
     * @param listener 回调listener
     * @param <T> 泛型参数
     * @return 该任务的taskId
     */
    @Override
    public <R,T extends IHttpApi> String execute(HttpRequest<R,T> request, IHttpListener<R> listener){
        return mProxy.execute(request,listener);
    }

    /**
     * 取消网络请求
     * @param taskId 需要取消的 taskId
     */
    @Override
    public void cancel(String taskId){
        mProxy.cancel(taskId);
    }

}
