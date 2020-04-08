package com.gotanks.uni_alisv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.gotanks.uni_alisv.bean.SvOptions;
import com.gotanks.uni_alisv.question.activity.QuestionActivity;
import com.gotanks.uni_alisv.recorder.activity.AlivcSvideoRecordActivity;
import com.gotanks.uni_alisv.recorder.bean.AlivcRecordInputParam;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

public class AliSvWXModule extends WXSDKEngine.DestroyableModule {

    public static final int REQ_CODE = 8888;
    public static final String KEY_OPTIONS = "KEY_OPTIONS_AliSvWXModule";

    /**
     * 最短 和 最长
     */
    public static final int MIN_TIME = 10 * 1000;
    public static final int MAX_TIME = 60 * 1000;

    private JSCallback jsCallback;

    @JSMethod(uiThread = true)
    public void openRecordVcr(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            SvOptions svOptions = decode(options);
            Activity activity = (Activity) mWXSDKInstance.getContext();
            AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder().build();
            AlivcSvideoRecordActivity.startRecordForResult(activity, recordParam, svOptions);
        }
    }

    private SvOptions decode(JSONObject options) {
        SvOptions svOptions = new SvOptions();
        svOptions.setFileName(options.getString("fileName"));
        svOptions.setImageType(options.getString("imageType"));
        svOptions.setQuestion(options.getString("question"));
        svOptions.setTitle(options.getString("title"));

        SvOptions.ImageUploadAuthBean imageUploadAuthBean = new SvOptions.ImageUploadAuthBean();
        svOptions.setImageUploadAuth(imageUploadAuthBean);
        JSONObject imageUploadAuth = options.getJSONObject("imageUploadAuth");
        imageUploadAuthBean.setImageId(imageUploadAuth.getString("imageId"));
        imageUploadAuthBean.setImageURL(imageUploadAuth.getString("imageURL"));
        imageUploadAuthBean.setUploadAddress(imageUploadAuth.getString("uploadAddress"));
        imageUploadAuthBean.setUploadAuth(imageUploadAuth.getString("uploadAuth"));

        SvOptions.VideoUploadAuthBean videoUploadAuthBean = new SvOptions.VideoUploadAuthBean();
        JSONObject videoUploadAuth = options.getJSONObject("imageUploadAuth");
        svOptions.setVideoUploadAuth(videoUploadAuthBean);
        videoUploadAuthBean.setUploadAddress(videoUploadAuth.getString("uploadAddress"));
        videoUploadAuthBean.setUploadAuth(videoUploadAuth.getString("uploadAuth"));
        videoUploadAuthBean.setVideoId(videoUploadAuth.getString("videoId"));

        return svOptions;
    }

    @JSMethod(uiThread = true)
    public void openRecordQuestion(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            Activity activity = (Activity) mWXSDKInstance.getContext();
            SvOptions svOptions = decode(options);
            AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder().build();
            QuestionActivity.startRecordForResult(activity, recordParam, svOptions);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.jsCallback != null) {
            Bundle bundle = data.getExtras();
            JSONObject result = new JSONObject();
            if (bundle != null) {
                result.put("videoId", bundle.getString("videoId"));
                result.put("videoDesc", bundle.getString("videoDesc"));
                result.put("visibility", bundle.getString("visibility"));
            }
            this.jsCallback.invoke(result);
        }
    }

    @JSMethod(uiThread = true)
    public void dismiss() {
        destroy();
    }

    @Override
    public void destroy() {
    }

}
