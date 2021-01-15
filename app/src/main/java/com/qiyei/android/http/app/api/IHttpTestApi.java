package com.qiyei.android.http.app.api;

import com.qiyei.android.http.api.IHttpApi;
import com.qiyei.android.http.api.IHttpTransferListener;
import com.qiyei.android.http.api.Response;
import com.qiyei.android.http.app.bean.UserBO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IHttpTestApi extends IHttpApi {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("passport/userExist")
    Call<Response<String>> isUserExist(@Query("name") String name);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("passport/register")
    Call<Response<String>> register(@Body UserBO userBO);

}
