package com.qiyei.android.http.server;


import android.text.TextUtils;

import com.qiyei.android.http.api.HttpRequest;
import com.qiyei.android.http.api.IHttpApi;
import com.qiyei.android.http.api.IHttpListener;
import com.qiyei.android.http.common.HttpUtils;

import java.util.function.Function;

import retrofit2.Call;

/**
 * @author Created by qiyei2015 on 2017/10/23.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class HttpTask<R,T extends IHttpApi> {
    /**
     * 任务id
     */
    protected String mTaskId;
    /**
     * http task tag
     */
    protected String mTag;

    /**
     * 请求回调
     */
    protected IHttpListener mListener;

    /**
     * HTTP 请求
     */
    protected HttpRequest<R,T> mRequest;


    /**
     * 构造器
     * @param tag taskTAG
     * @param request Http请求
     * @param listener 回调Listener
     */
    public HttpTask(String tag, HttpRequest<R,T> request, IHttpListener listener) {
        mTag = tag;
        mRequest = request;
        mListener = listener;
        if (TextUtils.isEmpty(mTag)){
            mTag = "HTTP";
        }
        mTaskId = mTag + "_" + HttpUtils.generateUUID();
    }

    /**
     * 获取请求，由子类实现
     * @return
     */
    public HttpRequest<R,T> getRequest(){
        return mRequest;
    }

    /**
     * @return {@link #mTaskId}
     */
    public String getTaskId() {
        return mTaskId;
    }

    /**
     * @return {@link #mListener}
     */
    public IHttpListener getListener() {
        return mListener;
    }



}
