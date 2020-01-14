package com.gotanks.uni_alisv;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.gotanks.uni_alisv.recorder.activity.AlivcSvideoRecordActivity;
import com.gotanks.uni_alisv.recorder.bean.AlivcRecordInputParam;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

public class AliSvWXModule extends WXSDKEngine.DestroyableModule {

    @JSMethod(uiThread = true)
    public void show(JSONObject options, JSCallback jsCallback) {

        if (mWXSDKInstance.getContext() instanceof Activity) {
            Activity activity = (Activity) mWXSDKInstance.getContext();
            AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder().build();
            AlivcSvideoRecordActivity.startRecord(activity, recordParam);

//            Toast.makeText(activity, "开始录制视频", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(activity, AliSvActivity.class);
//            Bundle b = new Bundle();
//            b.putString("channel", options.getString("username"));
//            b.putString("username", options.getString("channel"));
//            JSONObject authInfo = options.getJSONObject("authInfo");
//            RTCAuthInfo rtcAuthInfo = ParserJsonUtils.parserLoginJson(authInfo);
//            b.putSerializable("rtcAuthInfo", rtcAuthInfo);

//            intent.putExtras(b);
//            activity.startActivity(intent);
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
