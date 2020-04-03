package com.gotanks.uni_alisv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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

    private JSCallback jsCallback;

    @JSMethod(uiThread = true)
    public void recordVcr(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            Activity activity = (Activity) mWXSDKInstance.getContext();
            AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder().build();
            AlivcSvideoRecordActivity.startRecordForResult(activity, recordParam);
        }
    }

    @JSMethod(uiThread = true)
    public void recordQuestion(JSONObject options, JSCallback jsCallback) {
        if (mWXSDKInstance.getContext() instanceof Activity) {
            this.jsCallback = jsCallback;
            Activity activity = (Activity) mWXSDKInstance.getContext();
            String question = options.getString("question");
            AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder().build();
            QuestionActivity.startRecordForResult(activity, recordParam, question);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(this.jsCallback != null) {
            Bundle bundle = data.getExtras();
            JSONObject result = new JSONObject();
            result.put("videoId", bundle.getString("videoId"));
            result.put("videoDesc", bundle.getString("describe"));
            result.put("visibility", bundle.getString("visibility"));
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
