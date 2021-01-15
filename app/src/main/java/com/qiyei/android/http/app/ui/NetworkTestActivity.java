package com.qiyei.android.http.app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.qiyei.android.http.R;
import com.qiyei.android.http.api.FunctionCall;
import com.qiyei.android.http.api.HttpManager;
import com.qiyei.android.http.api.HttpRequest;
import com.qiyei.android.http.api.IHttpListener;
import com.qiyei.android.http.api.Response;
import com.qiyei.android.http.app.api.IHttpTestApi;
import com.qiyei.android.http.app.bean.UserBO;

import retrofit2.Call;


/**
 * @author Created by qiyei2015 on 2017/10/28.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class NetworkTestActivity extends AppCompatActivity {

    private final String TAG = "NetworkTestActivity";

    private static final String BASE_URL = "http://139.155.247.210:8080/foodie/";

    private ProgressBar mDownloadProgressBar;
    private ProgressBar mUploadProgressBar;

    private TextView mDownloadProgressTv;
    private TextView mUploadProgressTv;

    /**
     * 本地IP地址
     */
    private final String baseurl = "http://192.168.1.103:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);

        mDownloadProgressBar = (ProgressBar) findViewById(R.id.download_progress);
        mUploadProgressBar = (ProgressBar) findViewById(R.id.upload_progress);
        mDownloadProgressTv = (TextView) findViewById(R.id.download_progress_tv);
        mUploadProgressTv = (TextView) findViewById(R.id.upload_progress_tv);


        mDownloadProgressBar.setMax(100);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //get请求
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpManager().execute(buildGetRequest(), new IHttpListener<Response<String>>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response != null){
                            Log.d(TAG,"get --> "+ response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Log.d(TAG,exception.getMessage());
                    }
                });
            }
        });

        //post请求
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpManager().execute(buildPostRequest(), new IHttpListener<Response<String>>() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response != null){
                            Log.d(TAG,"post --> "+ response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Log.d(TAG,exception.getMessage());
                    }
                });
            }
        });

        //download请求
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //upload请求
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    /**
     * retrofit请求
     * @return
     */
    private HttpRequest<Response<String>,IHttpTestApi> buildGetRequest(){
        HttpRequest<Response<String>,IHttpTestApi> request = new HttpRequest.Builder<Response<String>,IHttpTestApi>()
                .baseUrl(BASE_URL)
                .dialogFragmentManager(getSupportFragmentManager())
                .cache(true)
                .call(new FunctionCall<Response<String>, IHttpTestApi>() {
                    @Override
                    public Call<Response<String>> buildCall(IHttpTestApi httpApi) {
                        return httpApi.isUserExist("qiyei2009");
                    }
                    @Override
                    public String requestMethod() {
                        return "GET";
                    }
                })
                .build();
        return request;
    }

    private HttpRequest<Response<String>,IHttpTestApi> buildPostRequest(){
        HttpRequest<Response<String>,IHttpTestApi> request = new HttpRequest.Builder<Response<String>,IHttpTestApi>()
                .baseUrl(BASE_URL)
                .dialogFragmentManager(getSupportFragmentManager())
                .cache(true)
                .call(new FunctionCall<Response<String>, IHttpTestApi>() {
                    @Override
                    public Call<Response<String>> buildCall(IHttpTestApi httpApi) {
                        UserBO userBO = new UserBO("qiyei2009","123456","123456");
                        return httpApi.register(userBO);
                    }
                    @Override
                    public String requestMethod() {
                        return "POST";
                    }
                })
                .build();
        return request;
    }
}
