package com.qiyei.android.http.dialog;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.qiyei.android.http.common.HttpLog;

/**
 * @author Created by qiyei2015 on 2017/10/25.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class LoadingManager {
    public static final String TAG = "LoadingManager";

    /**
     * 显示对话框
     * @param manager
     * @param tag
     */
    public static void showLoadingDialog(FragmentManager manager, String tag){
        if (manager == null){
            return;
        }
        try {
            LoadingDialog dialog = new LoadingDialog();
            dialog.setCancelable(false);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(dialog, tag);
            fragmentTransaction.commitNowAllowingStateLoss();
            HttpLog.d(TAG,"showLoadingDialog tag:" + tag);
        } catch (Exception e) {
            e.printStackTrace();
            HttpLog.e(TAG,"showLoadingDialog Exception:" + e.getMessage());
        }
    }

    /**
     * 取消对话框显示
     * @param manager
     * @param tag
     */
    public static void dismissLoadingDialog(FragmentManager manager, String tag){

        if (manager == null){
            return;
        }
        try {
            Fragment fragment = manager.findFragmentByTag(tag);
            if (fragment instanceof LoadingDialog){
                LoadingDialog dialog = (LoadingDialog) fragment;
                dialog.dismissAllowingStateLoss();
                HttpLog.d(TAG,"dismissLoadingDialog tag:" + tag);
            } else {
                HttpLog.w(TAG,"dismissLoadingDialog error,tag:" + tag + " is not a LoadingDialog");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpLog.e(TAG,"dismissLoadingDialog Exception:" + e.getMessage());
        }
    }

    /**
     * 显示对话框
     * @param manager
     * @param tag
     */
    public static void showProgressDialog(FragmentManager manager, String tag){
        if (manager == null){
            return;
        }
        try {
            ProgressDialog dialog = new ProgressDialog();
            dialog.setCancelable(false);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(dialog, tag);
            fragmentTransaction.commitNowAllowingStateLoss();
            HttpLog.d(TAG,"showProgressDialog tag:" + tag);
        } catch (Exception e) {
            e.printStackTrace();
            HttpLog.e(TAG,"showProgressDialog Exception:" + e.getMessage());
        }
    }

    /**
     * 取消对话框显示
     * @param manager
     * @param tag
     */
    public static void dismissProgressDialog(FragmentManager manager,String tag){
        if (manager == null){
            return;
        }
        Fragment fragment = manager.findFragmentByTag(tag);
        try {
            if (fragment instanceof ProgressDialog){
                ProgressDialog dialog = (ProgressDialog) fragment;
                dialog.dismissAllowingStateLoss();
                HttpLog.d(TAG,"dismissProgressDialog tag:" + tag);
            } else {
                HttpLog.w(TAG,"dismissProgressDialog error,tag:" + tag + " is not a ProgressDialog");
            }
        } catch (Exception e) {
            HttpLog.e(TAG,"dismissProgressDialog Exception:" + e.getMessage());
        }
    }

    /**
     * 取消对话框显示
     * @param manager
     * @param tag
     * @param progress
     */
    public static void setProgress(FragmentManager manager,String tag,int progress){
        if (manager == null){
            return;
        }
        Fragment fragment = manager.findFragmentByTag(tag);
        try {
            if (fragment instanceof ProgressDialog){
                ProgressDialog dialog = (ProgressDialog) fragment;
                dialog.setProgress(progress);
                HttpLog.d(TAG,"setProgress tag:" + tag + " progress=" + progress);
            } else {
                HttpLog.w(TAG,"setProgress error,tag:" + tag + " is not a ProgressDialog");
            }
        } catch (Exception e) {
            HttpLog.e(TAG,"setProgress Exception:" + e.getMessage());
        }
    }

}
