package com.qiyei.android.http.api;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@IntDef(value = {HTTP.GET,HTTP.POST,HTTP.DOWNLOAD,HTTP.UPLOAD})
public @interface HTTP {

    int GET = 1;
    int POST = 2;
    int DOWNLOAD = 3;
    int UPLOAD = 4;
}
