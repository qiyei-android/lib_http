package com.qiyei.android.http.ssl;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class SSLParams {

    public SSLSocketFactory sSLSocketFactory;
    public X509TrustManager trustManager;

    public SSLParams() {
    }
}
