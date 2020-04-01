package com.gotanks.uni_alisv;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.gotanks.uni_alisv.question.activity.QuestionActivity;
import com.gotanks.uni_alisv.recorder.activity.AlivcSvideoRecordActivity;
import com.gotanks.uni_alisv.recorder.bean.AlivcRecordInputParam;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

public class AliSvWXModule extends WXSDKEngine.DestroyableModule {

    public static final int REQ_CODE = 8888;

    /**
     * 最短 和 最长
     */
    public static final int MIN_TIME = 10 * 1000;
    public static final int MAX_TIME = 60 * 1000;

    @JSMethod(uiThread = true)
    public void show(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            Activity activity = (Activity) mWXSDKInstance.getContext();
            AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder().build();
            AlivcSvideoRecordActivity.startRecordForResult(activity, recordParam);
            JSONObject result = new JSONObject();
            result.put("type", "button");
            jsCallback.invoke(result);
        }
    }

    @JSMethod(uiThread = true)
    public void showQuestion(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            Activity activity = (Activity) mWXSDKInstance.getContext();
            String question = options.getString("question");
            AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder().build();
            QuestionActivity.startRecordForResult(activity, recordParam, question);
            JSONObject result = new JSONObject();
            result.put("type", "button");
            jsCallback.invoke(result);
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
