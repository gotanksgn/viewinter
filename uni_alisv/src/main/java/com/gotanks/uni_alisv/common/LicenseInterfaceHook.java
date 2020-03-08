package com.gotanks.uni_alisv.common;

import android.content.Context;
import android.util.Log;

import com.aliyun.common.license.LicenseImpl;
import com.aliyun.common.license.LicenseMessage;
import com.aliyun.jasonparse.JSONSupportImpl;

/**
 * Created by MT on 2020-03-07.
 */
public class LicenseInterfaceHook {


    public static void init(Context context) {


        try {
            LicenseImpl licenseImpl = LicenseImpl.getInstance(context);
            LicenseMessage savedLicenseMessage = licenseImpl.getLicenseMessage();
            String savedLicenseMessageStr = null;
            savedLicenseMessageStr = new JSONSupportImpl().writeValue(savedLicenseMessage);
            Log.e("HookBefore", savedLicenseMessageStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        savedLicenseMessage.setValidateTime(System.currentTimeMillis());
//        savedLicenseMessage.setFeature("1,2,3,4,5,6");
//        savedLicenseMessage.setLicenseType(LicenseType.normal);
//        savedLicenseMessage.setFailedCount(0);
//        savedLicenseMessage.setAttemptCount(10);
//        savedLicenseMessage.setLicenseCode(LicenseCode.NORMAL);
//
//        Method method = LicenseImpl.class.getDeclaredMethod("writeJsonFile", LicenseMessage.class);
//        method.setAccessible(true);
//        method.invoke(licenseImpl, savedLicenseMessage);
//
//        Field field = LicenseImpl.class.getDeclaredField("licenseMessage");
//        field.setAccessible(true);
//        field.set(licenseImpl, savedLicenseMessage);
//
//
//        Field field2 = LicenseMessage.class.getDeclaredField("instance");
//        field2.setAccessible(true);
//        field2.set(null, savedLicenseMessage);
//
//
//        Log.e("HookAfter", new JSONSupportImpl().writeValue(licenseImpl.getLicenseMessage()));

//        licenseMessage

//{"attemptCount":0,"failedCount":1,"failedCountAdded":true,"feature":"","featureCrop":false,"featureEditorFilter":false,"featureEditorMusic":false,"featureEditorMv":false,"featureEditorPaster":false,"featureEditorText":false,"featureFont":false,"featureMv":false,"featureOverLay":false,"featurePhotoMovie":false,"featureVideoCompose":false,"hasNetWork":true,"licenseCode":102,"licenseType":"invalid","sdkClientLicenseVersion":2,"validateTime":1583580991224}
//{"attemptCount":10,"failedCount":0,"failedCountAdded":true,"feature":"","featureCrop":false,"featureEditorFilter":false,"featureEditorMusic":false,"featureEditorMv":false,"featureEditorPaster":false,"featureEditorText":false,"featureFont":false,"featureMv":false,"featureOverLay":false,"featurePhotoMovie":false,"featureVideoCompose":false,"hasNetWork":true,"licenseCode":200,"licenseType":"normal","sdkClientLicenseVersion":2,"validateTime":1583575643632}
//{"attemptCount":10,"failedCount":0,"failedCountAdded":true,"feature":"","featureCrop":false,"featureEditorFilter":false,"featureEditorMusic":false,"featureEditorMv":false,"featureEditorPaster":false,"featureEditorText":false,"featureFont":false,"featureMv":false,"featureOverLay":false,"featurePhotoMovie":false,"featureVideoCompose":false,"hasNetWork":true,"licenseCode":200,"licenseType":"normal","sdkClientLicenseVersion":2,"validateTime":1583575643632}
//{"attemptCount":10,"failedCount":0,"failedCountAdded":true,"feature":"","featureCrop":false,"featureEditorFilter":false,"featureEditorMusic":false,"featureEditorMv":false,"featureEditorPaster":false,"featureEditorText":false,"featureFont":false,"featureMv":false,"featureOverLay":false,"featurePhotoMovie":false,"featureVideoCompose":false,"hasNetWork":true,"licenseCode":200,"licenseType":"normal","sdkClientLicenseVersion":2,"validateTime":1583575318922}
//{"attemptCount":0,"failedCount":1,"failedCountAdded":true,"feature":"","featureCrop":false,"featureEditorFilter":false,"featureEditorMusic":false,"featureEditorMv":false,"featureEditorPaster":false,"featureEditorText":false,"featureFont":false,"featureMv":false,"featureOverLay":false,"featurePhotoMovie":false,"featureVideoCompose":false,"hasNetWork":true,"licenseCode":102,"licenseType":"invalid","sdkClientLicenseVersion":2,"validateTime":1583582060156}
    }


}
