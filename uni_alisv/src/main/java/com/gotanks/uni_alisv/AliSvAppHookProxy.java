package com.gotanks.uni_alisv;

import android.app.Application;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.aliyun.sys.AlivcSdkCore;
import com.gotanks.uni_alisv.common.LicenseInterfaceHook;
import com.gotanks.uni_alisv.recorder.faceunity.FaceUnityManager;

import io.dcloud.weex.AppHookProxy;

public class AliSvAppHookProxy implements AppHookProxy {

    @Override
    public void onCreate(Application application) {
        LicenseInterfaceHook.init(application.getApplicationContext());
        FaceUnityManager.getInstance().setUp(application);
        //短视频sdk，暂时只支持api 18以上的版本
        AlivcSdkCore.register(application.getApplicationContext());
        //LittleHttpManager.getInstance().init(this);

        QupaiHttpFinal.getInstance().initOkHttpFinal();
//        DownloaderManager.getInstance().init(this);
    }
}
