
package com.qiyei.android.http.api;

import retrofit2.Call;

public interface FunctionCall<R,T extends IHttpApi> {
        Call<R> buildCall(T httpApi);

        String requestMethod();
}