package com.jagt.viewinter1.application;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.aliyun.sys.AlivcSdkCore;
import com.gotanks.uni_alisv.common.LicenseInterfaceHook;
import com.gotanks.uni_alisv.recorder.faceunity.FaceUnityManager;

import io.dcloud.application.DCloudApplication;

public class MyApplication extends DCloudApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        LicenseInterfaceHook.init(getApplicationContext());
        FaceUnityManager.getInstance().setUp(this);
        //短视频sdk，暂时只支持api 18以上的版本
        AlivcSdkCore.register(getApplicationContext());
        //LittleHttpManager.getInstance().init(this);

        QupaiHttpFinal.getInstance().initOkHttpFinal();
//        DownloaderManager.getInstance().init(this);
    }
}
