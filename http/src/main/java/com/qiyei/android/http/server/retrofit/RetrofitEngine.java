package com.qiyei.android.http.server.retrofit;


import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.qiyei.android.http.HTTP;
import com.qiyei.android.http.api.FunctionCall;
import com.qiyei.android.http.api.HTTPException;
import com.qiyei.android.http.api.HttpRequest;
import com.qiyei.android.http.api.IHttpApi;
import com.qiyei.android.http.common.HttpLog;
import com.qiyei.android.http.server.HttpCallManager;
import com.qiyei.android.http.server.HttpResponse;
import com.qiyei.android.http.server.HttpTask;
import com.qiyei.android.http.server.IHttpCallback;
import com.qiyei.android.http.server.IHttpEngine;
import com.qiyei.android.http.server.IHttpTransferCallback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Created by qiyei2015 on 2017/10/22.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class RetrofitEngine implements IHttpEngine {

    /**
     * 主线程的Handler
     */
    private MainThreadExecutor mMainThreadExecutor = null;

    static final class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable r) {
            handler.post(r);
        }
    }

    public RetrofitEngine() {
        mMainThreadExecutor = new MainThreadExecutor();
    }

    @Override
    public <R, T extends IHttpApi> void enqueueCall(HttpTask<R,T> task, IHttpCallback<R, T> callback) {
        if (callback != null){
            callback.onStart(task);
        }
        HttpRequest<R,T> request = task.getRequest();
        FunctionCall<R,T> functionCall = request.mFunctionCall;
        Retrofit retrofit = RetrofitFactory.createRetrofit(request.getBaseUrl());

        Type type = request.mFunctionCall.getClass().getGenericInterfaces()[0];
        Class<T> apiClazz = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getActualTypeArguments().length >= 2){
                apiClazz = (Class<T>) parameterizedType.getActualTypeArguments()[1];
            }
        }
        if (apiClazz == null){
            onFailureBuildCall(task,callback,"request ParameterizedType is error,type=" + type);
            return ;
        }

        T api = retrofit.create(apiClazz);
        Call<R> call = functionCall.buildCall(api);
        if (call == null){
            onFailureBuildCall(task,callback," request buildCall error");
            return ;
        }
        //设置task到okHttp拦截器中
        setOkHttpInterceptorTag(call,task);
        //将任务加到队列里面
        HttpCallManager.getInstance().addCall(task.getTaskId(),call);
        //发起请求
        call.enqueue(new Callback<R>() {
            @Override
            public void onResponse(Call<R> call, Response<R> response) {
                //移除task
                HttpCallManager.getInstance().removeCall(task.getTaskId());
                callback.onSuccess(new HttpResponse<R>(response.body()));
            }

            @Override
            public void onFailure(Call<R> call, Throwable t) {
                //移除task
                HttpCallManager.getInstance().removeCall(task.getTaskId());
                callback.onFailure((Exception) t);
            }
        });
    }

    @Override
    public <R, T extends IHttpApi> void enqueueTransferCall(HttpTask<R,T> task, IHttpTransferCallback<R, T> callback) {

    }

//
//    public <T, R> void enqueueDownloadCall(final HttpTask<T> task, final IHttpTransferCallback<T,R> callback) {
//        if (callback != null){
//            callback.onStart(task);
//        }
//        Retrofit retrofit = RetrofitFactory.createRetrofit(task.getRequest().getBaseUrl());
//        //获取到OkHttpClient
//        OkHttpClient client = (OkHttpClient) retrofit.callFactory();
//
//        //找到Interceptor
//        ProgressInterceptor interceptor = new ProgressInterceptor();
//        interceptor.setProgressResponseBody(new ProgressResponseBody(callback));
//        client = client.newBuilder().addInterceptor(interceptor).build();
//
//        //改变okHttpClient，向其中添加拦截器
//        Retrofit.Builder newBuilder = retrofit.newBuilder();
//        newBuilder.client(client);
//        retrofit = newBuilder.build();
//
//
//
//        //获取task要执行的方法的参数
//        Object params = task.getRequest().getBody();
//
//        //构造Call
//        Call call = buildCall(retrofit,task,params);
//        if (call == null){
//            onFailureBuildCall(task,callback);
//            return ;
//        }
//        //设置task到okHttp拦截器中
//        setOkHttpInterceptorTag(call,task);
//
//        //将任务加到队列里面
//        HttpCallManager.getInstance().addCall(task.getTaskId(),call);
//
//        call.enqueue(new Callback<R>() {
//            @Override
//            public void onResponse(Call<R> call, final Response<R> response) {
//                //可以在这里构造ProgressResponseBody来实现进度的监听
//                final ResponseBody responseBody = (ResponseBody) response.body();
////                final ProgressResponseBody responseBody = new ProgressResponseBody((ResponseBody) response.body(),callback);
////                new Thread(){
////                    @Override
////                    public void run() {
////
////                        //read the body to file
////                        BufferedSource source = responseBody.source();
////                        File outFile = new File(task.getRequest().getFilePath());
////                        outFile.delete();
////                        outFile.getParentFile().mkdirs();
////                        try {
////                            outFile.createNewFile();
////                            BufferedSink sink = Okio.buffer(Okio.sink(outFile));
////                            source.readAll(sink);
////                            sink.flush();
////                            source.close();
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////
////                    }
////                }.start();
//
//                new Thread(){
//                    @Override
//                    public void run() {
//                        try {
//                            InputStream is = responseBody.byteStream();
//                            File file = new File(task.getRequest().getFilePath());
//                            //创建父目录
//                            file.getParentFile().mkdirs();
//                            if (file.exists()){
//                                file.delete();
//                            }
//                            FileOutputStream fos = new FileOutputStream(file);
//                            BufferedInputStream bis = new BufferedInputStream(is);
//                            byte[] buffer = new byte[1024];
//                            int len;
//                            while ((len = bis.read(buffer)) != -1) {
//                                fos.write(buffer, 0, len);
//                                fos.flush();
//                            }
//                            fos.close();
//                            bis.close();
//                            is.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        mMainThreadExecutor.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                //移除task
//                                HttpCallManager.getInstance().removeCall(task.getTaskId());
//                                HttpResponse<R> obj = new HttpResponse<>(response.body());
//                                callback.onSuccess(obj);
//                            }
//                        });
//                    }
//                }.start();
//            }
//
//            @Override
//            public void onFailure(Call<R> call, Throwable t) {
//                //移除task
//                HttpCallManager.getInstance().removeCall(task.getTaskId());
//
//                callback.onFailure((Exception) t);
//            }
//        });
//    }
//
//
//    public <T, R> void enqueueUploadCall(HttpTask<T> task, IHttpTransferCallback<T,R> callback) {
//        if (callback != null){
//            callback.onStart(task);
//        }
//
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

    /**
     * @return {@link #mMainThreadExecutor}
     */
    @Override
    public Executor getMainExecutor() {
        return mMainThreadExecutor;
    }

    /**
     * 设置OkHttp拦截器的Tag
     * @param call
     * @param task
     * @param <T>
     */
    private <R,T extends IHttpApi> void setOkHttpInterceptorTag(Call call,HttpTask<R,T> task){
        //获取OkHttp的request
        Request request = call.request();
        //反射设置 tag
        Class<?> clazz = request.getClass();
        try {
            Field field = clazz.getDeclaredField("tags");
            field.setAccessible(true);
            //将task设置成tag字段，保存数据
            Map<Class<?>,Object> originalMap = (Map<Class<?>, Object>) field.get(request);
            Map<Class<?>,Object> newMap = new HashMap<>();
            for (Map.Entry<Class<?>,Object> entry : originalMap.entrySet()){
                newMap.put(entry.getKey(),entry.getValue());
            }
            newMap.put(HttpTask.class,task);
            field.set(request,newMap);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

//    /**
//     * 构造Call
//     * @param task
//     * @param params
//     * @param <T>
//     * @return
//     */
//    private <T> Call buildCall(HttpTask<T> task,Object params){
//        Retrofit retrofit = RetrofitFactory.createRetrofit(task.getRequest().getBaseUrl());
//        //根据Class找到要执行的RetrofitApiService接口
//        Object apiService = retrofit.create(task.getRequest().getReqApiClazz());
//
//        //获取到要执行的方法
//        String methodName = getMethodName(task);
//
//        if (TextUtils.isEmpty(methodName)){
//            HttpLog.i(HTTP.TAG,"cannot find method in " + task.getRequest().getReqApiClazz());
//            return null;
//        }
//
//        Call call = null;
//        try {
//            Class<?> paramsClazz;
//            if (params != null){
//                //有参方法
//                paramsClazz = getParamsClazz(apiService.getClass(),params.getClass(),methodName);
//            }else {
//                //无参方法
//                paramsClazz = null;
//            }
//
//            Method method;
//            if (paramsClazz == null){
//                method = apiService.getClass().getDeclaredMethod(methodName,new Class[0]);
////                params = new Object[0];
//            }else {
//                method = apiService.getClass().getDeclaredMethod(methodName,new Class[]{paramsClazz});
//            }
//
//            HttpLog.i(HTTP.TAG,"params:" + params);
//
//            if (method != null){
//                if (params != null){
//                    call = (Call) method.invoke(apiService,params);
//                }else {
//                    call = (Call) method.invoke(apiService,new Object[0]);
//                }
//            }
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return call;
//    }

//    /**
//     *
//     * @param retrofit
//     * @param task
//     * @param params
//     * @param <T>
//     * @return
//     */
//    private <T> Call buildCall(Retrofit retrofit,HttpTask<T> task,Object params){
//        if (retrofit == null){
//            retrofit = RetrofitFactory.createRetrofit(task.getRequest().getBaseUrl());
//        }
//
//        //根据Class找到要执行的RetrofitApiService接口
//        Object apiService = retrofit.create(task.getRequest().getReqApiClazz());
//
//        //获取到要执行的方法
//        String methodName = getMethodName(task);
//
//        if (TextUtils.isEmpty(methodName)){
//            HttpLog.i(HTTP.TAG,"cannot find method in " + task.getRequest().getReqApiClazz());
//            return null;
//        }
//
//        Call call = null;
//        try {
//            Class<?> paramsClazz;
//            if (params != null){
//                //有参方法
//                paramsClazz = getParamsClazz(apiService.getClass(),params.getClass(),methodName);
//            }else {
//                //无参方法
//                paramsClazz = null;
//            }
//
//            Method method;
//            if (paramsClazz == null){
//                method = apiService.getClass().getDeclaredMethod(methodName,new Class[0]);
////                params = new Object[0];
//            }else {
//                method = apiService.getClass().getDeclaredMethod(methodName,new Class[]{paramsClazz});
//            }
//
//            HttpLog.i(HTTP.TAG,"params:" + params);
//
//            if (method != null){
//                if (params != null){
//                    call = (Call) method.invoke(apiService,params);
//                }else {
//                    call = (Call) method.invoke(apiService,new Object[0]);
//                }
//            }
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return call;
//    }
//
//    /**
//     * 找到method
//     * @param task
//     * @return
//     */
//    private String getMethodName(HttpTask task){
//        String methodName = null;
//
//        //这个循环找到method
//        for (Method method : task.getRequest().getReqApiClazz().getDeclaredMethods()){
//
//            String value = null;
//            HttpLog.d(HTTP.TAG,"taskId=" + task.getTaskId() + " getMethodName method=" + method.getName() + " from class " + task.getRequest().getReqApiClazz().getCanonicalName());
//            switch (task.getRequest().getMethod()){
//                case HTTP.GET:
//                    GET getAnnotation = method.getAnnotation(GET.class);
//                    if (getAnnotation != null){
//                        value = getAnnotation.value();
//                    }
//                    break;
//
//                case HTTP.POST:
//                    POST postAnnotation = method.getAnnotation(POST.class);
//                    if (postAnnotation != null){
//                        value = postAnnotation.value();
//                    }
//                    break;
//
//                case HTTP.DOWNLOAD:
//                    if (method.getAnnotation(Streaming.class) != null){
//                        GET downloadAnnotation = method.getAnnotation(GET.class);
//                        if (downloadAnnotation != null){
//                            value = downloadAnnotation.value();
//                        }
//                    }
//                    break;
//
//                case HTTP.UPLOAD:
//                    if (method.getAnnotation(Multipart.class) != null){
//                        POST uploadAnnotation = method.getAnnotation(POST.class);
//                        if (uploadAnnotation != null){
//                            value = uploadAnnotation.value();
//                        }
//                    }
//
//                    break;
//
//                default:
//                    break;
//            }
//
//            if (value != null && task.getRequest().getPathUrl().equals(value)){
//                methodName = method.getName();
//                HttpLog.v(HTTP.TAG,"method :" + methodName);
//            }
//        }
//
//        return methodName;
//    }


    /**
     * 找到原始的接口的中的正确的参数类型
     * @param targetClazz
     * @param paramsClazz
     * @param name
     * @return
     */
    private Class<?> getParamsClazz(Class<?> targetClazz,Class<?> paramsClazz,String name){

        if (targetClazz == null || paramsClazz == null || TextUtils.isEmpty(name)){
            return null;
        }

        for (Method method : targetClazz.getDeclaredMethods()){
            //根据名字找到方法
            if (name.equals(method.getName())){
                HttpLog.v(HTTP.TAG,"paramsClazz :" + paramsClazz.getName());
                //目前只考虑只有一个参数的
                Class<?>[] typeClazzs = method.getParameterTypes();

                //该方法是无参方法
                if (typeClazzs == null) {
                    return null;
                }
                if (typeClazzs != null && typeClazzs.length <= 0){
                    return null;
                }

                Class<?> clazz = paramsClazz;

                List<Class<?>> interfaces = new ArrayList<>();
                if (clazz.getInterfaces() != null){
                    interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
                }

                //现在本类及父类里面找
                while (clazz != null && !clazz.getName().equals(typeClazzs[0].getName())){
                    clazz = clazz.getSuperclass();
                    if (clazz != null && clazz.getInterfaces() != null){
                        interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
                    }
                }

                //在父类里面找到
                if (clazz != null){
                    return clazz;
                }

                //在接口里面找
                for (Class<?> cla : interfaces){
                    //接口里面找到
                    if (cla.getName().equals(typeClazzs[0].getName())){
                        return cla;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RetrofitEngine";
    }

    private <R,T extends IHttpApi> void onFailureCall(HttpTask<R,T> task, final IHttpCallback<R,T> callback,Exception exception){
        //移除task
        HttpCallManager.getInstance().removeCall(task.getTaskId());
        callback.onFailure(exception);
    }

    private <R,T extends IHttpApi> void onFailureBuildCall(HttpTask<R,T> task, final IHttpCallback<R,T> callback,String msg){
        onFailureCall(task,callback,new HTTPException(task.getTaskId(),"build call exception:" + msg));
    }
}
