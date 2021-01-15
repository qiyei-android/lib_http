package com.qiyei.android.http.server;



import com.qiyei.android.http.HTTP;
import com.qiyei.android.http.IHttpExecutor;
import com.qiyei.android.http.api.HttpRequest;
import com.qiyei.android.http.api.IHttpApi;
import com.qiyei.android.http.api.IHttpListener;
import com.qiyei.android.http.api.IHttpTransferListener;
import com.qiyei.android.http.common.HttpLog;
import com.qiyei.android.http.dialog.LoadingManager;
import com.qiyei.android.http.server.retrofit.RetrofitEngine;


/**
 * @author Created by qiyei2015 on 2017/10/21.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: http服务，处理http请求
 */
public class HttpServer implements IHttpExecutor {

    /**
     * Https引擎，http请求执行者
     */
    private IHttpEngine mEngine;

    /**
     * Http请求管理者
     */
    private HttpCallManager mCallManager;

    /**
     * Http构造方法
     */
    private HttpServer(IHttpEngine engine) {
        mEngine = engine;
        onCreate();
    }

    /**
     * 静态内部类
     */
    private static class SingleHolder{
        private final static HttpServer sServer = new HttpServer(new RetrofitEngine());
    }

    /**
     * 获取默认的HttpServer
     * @return
     */
    public static HttpServer getDefault(){
        return SingleHolder.sServer;
    }

    /**
     * 创建函数
     */
    public void onCreate(){
        HttpLog.i(HTTP.TAG,"HttpServer created ! engine is " + mEngine.toString());
    }

    /**
     * 销毁函数
     */
    public void onDestroy(){
        //做一些清理工作
    }

    /**
     * 设置引擎
     * @param engine
     */
    public void setEngine(IHttpEngine engine){
        mEngine = engine;
    }

    /**
     * 执行https请求
     * @param request 请求参数
     * @param listener 回调listener
     * @param <T> 请求泛型参数
     * @param <R> 响应泛型参数
     * @return 该任务的taskId
     */
    @Override
    public <R,T extends IHttpApi> String execute(HttpRequest<R,T> request, IHttpListener<R> listener) {
        HttpTask<R,T> task = new HttpTask<R,T>(request.mFunctionCall.requestMethod(),request, listener);
        dispatchHttpTask(task,listener);
        return task.getTaskId();
    }
    
    /**
     * 取消网络请求
     * @param taskId 需要取消的 taskId
     */
    @Override
    public void cancel(String taskId) {
        mEngine.cancelHttpCall(taskId);
    }

    /**
     * Task进行分流
     * @param task
     * @param listener
     * @param <T>
     * @param <R>
     */
    private <R,T extends IHttpApi> void dispatchHttpTask(HttpTask<R,T> task, IHttpListener<R> listener){
        if (listener instanceof IHttpTransferListener){
            final IHttpTransferListener<R> transferListener = (IHttpTransferListener<R>)listener;
            mEngine.enqueueTransferCall(task, new IHttpTransferCallback<R,T>() {
                @Override
                public void onStart(HttpTask<R,T> task) {
                    HttpLog.i(HTTP.TAG,"taskId=" + task.getTaskId() + " transfer onStart");
                }

                @Override
                public void onProgress(long currentLength, long totalLength) {
                    final int progress = (int) ((currentLength * 1.0 / totalLength) * 100);
                    mEngine.getMainExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            transferListener.onProgress(currentLength,totalLength);
                        }
                    });
                }

                @Override
                public void onSuccess(HttpResponse<R> response) {
                    HttpLog.i(HTTP.TAG,"taskId=" + task.getTaskId() + " transfer onSuccess");
                    transferListener.onSuccess(response.getContent());
                }

                @Override
                public void onFailure(Exception exception) {
                    HttpLog.i(HTTP.TAG,"taskId=" + task.getTaskId() + " transfer onFailure exception:" + exception.toString());
                    transferListener.onFailure(exception);
                }
            });
        } else {
            mEngine.enqueueCall(task, new IHttpCallback<R,T>() {
                @Override
                public void onStart(HttpTask<R,T> task) {
                    HttpLog.i(HTTP.TAG,"taskId=" + task.getTaskId() + " onStart");
                    LoadingManager.showLoadingDialog(task.getRequest().getDialogFragmentManager(),task.mTaskId);
                }

                @Override
                public void onSuccess(HttpResponse<R> response) {
                    LoadingManager.dismissLoadingDialog(task.getRequest().getDialogFragmentManager(),task.mTaskId);
                    if (HttpResponse.isOK(response)){
                        HttpLog.i(HTTP.TAG,"taskId=" + task.getTaskId() + " onSuccess");
                        listener.onSuccess(response.getContent());
                    }else {
                        listener.onFailure(new Exception("taskId=" + task.getTaskId() + " onSuccess but is not ok"));
                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    LoadingManager.dismissLoadingDialog(task.getRequest().getDialogFragmentManager(),task.mTaskId);
                    HttpLog.i(HTTP.TAG,"taskId=" + task.getTaskId() + " onFailure exception:" + exception.toString());
                    listener.onFailure(exception);
                }
            });
        }

    }
}
