package com.jagt.viewinter1.application;

import com.aliyun.common.httpfinal.QupaiHttpFinal;

import io.dcloud.application.DCloudApplication;

public class MyApplication extends DCloudApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        QupaiHttpFinal.getInstance().initOkHttpFinal();
//        DownloaderManager.getInstance().init(this);
    }
}
