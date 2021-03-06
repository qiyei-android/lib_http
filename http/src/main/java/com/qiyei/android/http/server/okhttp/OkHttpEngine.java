package com.qiyei.android.http.server.okhttp;

import android.os.Handler;
import android.os.Looper;


import com.qiyei.android.http.api.HTTPException;
import com.qiyei.android.http.api.IHttpApi;
import com.qiyei.android.http.server.HttpCallManager;
import com.qiyei.android.http.server.HttpResponse;
import com.qiyei.android.http.server.HttpTask;
import com.qiyei.android.http.common.HttpUtils;
import com.qiyei.android.http.server.IHttpCallback;
import com.qiyei.android.http.server.IHttpEngine;
import com.qiyei.android.http.server.IHttpTransferCallback;
import com.qiyei.android.http.server.ProgressResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * @author Created by qiyei2015 on 2017/10/21.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 单纯使用OkHttp引擎
 */
public class OkHttpEngine implements IHttpEngine {

    /**
     * OkHttpClient
     */
    private OkHttpClient mClient;
    /**
     * 线程派发时的Handler
     */
    private Handler mHandler;

    /**
     * 构造函数
     */
    public OkHttpEngine(){
        mClient = OkHttpFactory.createOkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public <R, T extends IHttpApi> void enqueueCall(HttpTask<R, T> task, IHttpCallback<R, T> callback) {

    }

    @Override
    public <R, T extends IHttpApi> void enqueueTransferCall(HttpTask<R, T> task, IHttpTransferCallback<R, T> callback) {

    }

    //    @Override
//    public <T, R> void enqueueCall(HttpTask<T> task, IHttpCallback<T, R> callback) {
//
//    }
//
//    @Override
//    public <T, R> void enqueueTransferCall(HttpTask<T> task, IHttpTransferCallback<T, R> callback) {
//
//    }

//
//    public <T, R> void enqueueGetCall(final HttpTask<T> task, final IHttpCallback<T,R> callback) {
//
//        Call call = OkHttpHelper.buildGetCall(mClient,task);
//        if (call == null){
//            onFailureBuildCall(task,callback);
//            return ;
//        }
//
//        //添加call
//        HttpCallManager.getInstance().addCall(task.getTaskId(),call);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, final IOException e) {
//                onFailureCall(task,callback,e);
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//
//                //移除call
//                HttpCallManager.getInstance().removeCall(task.getTaskId());
//
//                String result = null;
//                if (response != null && response.isSuccessful()) {
//                    result = response.body().string();
//                }
//                final R obj = (R) HttpUtils.parseJson(result, task.getListener().getClass(), true);
//                final HttpResponse<R> responseObj = new HttpResponse<R>(obj);
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (responseObj != null) {
//                            callback.onSuccess(responseObj);
//                        } else {
//                            callback.onFailure(new Exception("response is null"));
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//
//    public <T, R> void enqueuePostCall(final HttpTask<T> task, final IHttpCallback<T,R> callback) {
//
//        Call call = OkHttpHelper.buildPostCall(mClient,task);
//        if (call == null){
//            onFailureBuildCall(task,callback);
//            return ;
//        }
//
//        //添加call
//        HttpCallManager.getInstance().addCall(task.getTaskId(),call);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, final IOException e) {
//                onFailureCall(task,callback,e);
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                //移除call
//                HttpCallManager.getInstance().removeCall(task.getTaskId());
//
//                String result = null;
//                if (response != null && response.isSuccessful()) {
//                    result = response.body().string();
//                }
//                final R obj = (R) HttpUtils.parseJson(result, task.getListener().getClass(), true);
//                final HttpResponse<R> responseObj = new HttpResponse<R>(obj);
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (responseObj != null) {
//                            callback.onSuccess(responseObj);
//                        } else {
//                            callback.onFailure(new Exception("response is null"));
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//
//    public <T, R> void enqueueDownloadCall(final HttpTask<T> task, final IHttpTransferCallback<T,R> callback) {
//
//        Call call = OkHttpHelper.buildDownloadCall(mClient,task);
//        if (call == null){
//            onFailureBuildCall(task,callback);
//            return ;
//        }
//
//        //添加call
//        HttpCallManager.getInstance().addCall(task.getTaskId(),call);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, final IOException e) {
//                onFailureCall(task,callback,e);
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//
//                ResponseBody responseBody = new ProgressResponseBody(response.body(), callback);
//                //read the body to file
//                BufferedSource source = responseBody.source();
//                File outFile = new File(task.getRequest().getFilePath());
//                outFile.delete();
//                outFile.getParentFile().mkdirs();
//                outFile.createNewFile();
//                BufferedSink sink = Okio.buffer(Okio.sink(outFile));
//                source.readAll(sink);
//                sink.flush();
//                source.close();
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        String success = "success";
//                        final HttpResponse responseObj = new HttpResponse(success);
//                        callback.onSuccess(responseObj);
//                    }
//                });
//
//                //移除call
//                HttpCallManager.getInstance().removeCall(task.getTaskId());
//            }
//        });
//    }
//
//
//    public <T, R> void enqueueUploadCall(final HttpTask<T> task, final IHttpTransferCallback<T,R> callback) {
//
//        Call call = OkHttpHelper.buildUploadCall(mClient,task,callback);
//        if (call == null){
//            onFailureBuildCall(task,callback);
//            return ;
//        }
//
//        //添加call
//        HttpCallManager.getInstance().addCall(task.getTaskId(),call);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, final IOException e) {
//                onFailureCall(task,callback,e);
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//
//                ResponseBody responseBody = new ProgressResponseBody(response.body(),callback);
//                //read the body to file
//                BufferedSource source = responseBody.source();
//                File outFile = new File(task.getRequest().getFilePath());
//                outFile.delete();
//                outFile.getParentFile().mkdirs();
//                outFile.createNewFile();
//                BufferedSink sink = Okio.buffer(Okio.sink(outFile));
//                source.readAll(sink);
//                sink.flush();
//                source.close();
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        String success = "success";
//                        final HttpResponse responseObj = new HttpResponse(success);
//                        callback.onSuccess(responseObj);
//                    }
//                });
//
//                //移除call
//                HttpCallManager.getInstance().removeCall(task.getTaskId());
//            }
//        });
//    }


    @Override
    public void cancelHttpCall(String taskId) {
        Object object = HttpCallManager.getInstance().queryCall(taskId);
        if (object == null){
            return;
        }

        if (object instanceof Call){
            Call call = (Call) object;
            if (call != null && !call.isCanceled()){
                call.cancel();
            }
        }
    }

    @Override
    public String toString() {
        return "OkHttpEngine";
    }

    /**
     * @return {@link #mHandler}
     */
    @Override
    public Executor getMainExecutor() {
        return null;
    }


//    private <T,R> void onFailureCall(HttpTask<T> task, final IHttpCallback<T,R> callback,Exception exception){
//        //移除call
//        HttpCallManager.getInstance().removeCall(task.getTaskId());
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                callback.onFailure(exception);
//            }
//        });
//    }
//
//    private <T,R> void onFailureBuildCall(HttpTask<T> task, final IHttpCallback<T,R> callback){
//        onFailureCall(task,callback,new HTTPException(task.getTaskId(),"build call exception ,result is null"));
//    }
}
