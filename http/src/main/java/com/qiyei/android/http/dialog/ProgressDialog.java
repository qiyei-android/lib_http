package com.qiyei.android.http.dialog;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.qiyei.android.http.R;
import com.qiyei.android.http.common.ProgressLoadingView;


/**
 * @author Created by qiyei2015 on 2017/10/30.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class ProgressDialog extends DialogFragment {

    /**
     * 自定义进度View
     */
    private ProgressLoadingView mProgressLoadingView;

    public ProgressDialog(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progress_dialog, null, false);
        mProgressLoadingView = (ProgressLoadingView) root.findViewById(R.id.progress_loading_view);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置布局属性
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress){
        mProgressLoadingView.setProgress(progress);
    }


}
