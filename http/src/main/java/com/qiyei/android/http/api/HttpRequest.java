package com.qiyei.android.http.api;



import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Created by qiyei2015 on 2017/10/21.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: https请求
 */
public class HttpRequest<R,T extends IHttpApi> {

    /**
     * 基础url，为了应对微服务有多个基础url
     */
    private String mBaseUrl;
    /**
     * 请求的路径url，完整 mBaseUrl + mPathUrl
     */
    private String mPathUrl;
    /**
     * 请求的Header参数
     */
    private Map<String,String> mHeaderMap;

    /**
     * 是否缓存
     */
    private boolean isCache;

    /**
     * 对话框使用的FragmentManager
     */
    private FragmentManager mDialogFragmentManager;

    public FunctionCall<R,T> mFunctionCall;

    /**
     * Http请求构造函数
     */
    private HttpRequest(){
        mHeaderMap = new HashMap<>();
        isCache = true;
    }


    /**
     * build设计模式
     */
    public static class Builder<R,T extends IHttpApi>{
        /**
         * HttpRequest引用
         */
        HttpRequest<R,T> request;

        /**
         * Builder的构造方法
         */
        public Builder(){
            request = new HttpRequest<R,T>();
        }

        /**
         * @param baseUrl the {@link #mBaseUrl} to set
         * @return 当前对象
         */
        public Builder<R,T> baseUrl(String baseUrl) {
            request.mBaseUrl = baseUrl;
            return this;
        }

        /**
         * @param pathUrl the {@link #mPathUrl} to set
         * @return 当前对象
         */
        public Builder<R,T> path(String pathUrl) {
            request.mPathUrl = pathUrl;
            return this;
        }

        /**
         * @param headerMap the {@link #mHeaderMap} to set
         * @return 当前对象
         */
        public Builder<R,T> headerMap(Map<String, String> headerMap) {
            request.mHeaderMap = headerMap;
            return this;
        }

        /**
         * @param fragmentManager the {@link #mDialogFragmentManager} to set
         * @return 当前对象
         */
        public Builder<R,T> dialogFragmentManager(FragmentManager fragmentManager) {
            request.mDialogFragmentManager = fragmentManager;
            return this;
        }

        /**
         * @param cache the {@link #isCache} to set
         * @return 当前对象
         */
        public Builder<R,T> cache(boolean cache) {
            request.isCache = cache;
            return this;
        }

        /**
         * @param call
         * @return 当前对象
         */
        public Builder<R,T> call(FunctionCall<R,T> call) {
            request.mFunctionCall = call;
            return this;
        }

        /**
         * 创建HttpRequest
         * @return
         */
        public HttpRequest<R,T> build(){
            return request;
        }

    }

    /**
     * @return {@link #mBaseUrl}
     */
    public String getBaseUrl() {
        return mBaseUrl;
    }

    /**
     * @return {@link #mPathUrl}
     */
    public String getPathUrl() {
        return mPathUrl;
    }

    /**
     * @return {@link #mHeaderMap}
     */
    public Map<String, String> getHeaderMap() {
        return mHeaderMap;
    }

    /**
     * @return {@link #isCache}
     */
    public boolean isCache() {
        return isCache;
    }


    /**
     * @return {@link #mDialogFragmentManager}
     */
    public FragmentManager getDialogFragmentManager() {
        return mDialogFragmentManager;
    }
}
