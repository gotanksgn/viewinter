package com.gotanks.uni_alisv;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.gotanks.uni_alisv.recorder.activity.AlivcSvideoRecordActivity;
import com.gotanks.uni_alisv.recorder.bean.AlivcRecordInputParam;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

public class AliSvWXModule extends WXSDKEngine.DestroyableModule {

    public static final int REQ_CODE = 8888;

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
    public void dismiss() {
        destroy();
    }

    @Override
    public void destroy() {
    }

}
