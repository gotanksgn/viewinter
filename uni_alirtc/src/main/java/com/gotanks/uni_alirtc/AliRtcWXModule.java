package com.gotanks.uni_alirtc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.gotanks.uni_alirtc.bean.RTCAuthInfo;
import com.gotanks.uni_alirtc.utils.ParserJsonUtils;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
public class AliRtcWXModule extends WXSDKEngine.DestroyableModule {

    @JSMethod(uiThread = true)
    public void show(JSONObject options, JSCallback jsCallback) {
        Activity activity = (Activity) mWXSDKInstance.getContext();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("alirtcsample://chat"));
        Bundle b = new Bundle();
        //用户名
        b.putString("username", options.getString("userid"));
        //频道号
        b.putString("channel", options.getString("roomid"));
        //音频采集
        b.putBoolean("audioCapture", true);
        //音频播放
        b.putBoolean("audioPlay", true);
        RTCAuthInfo rtcAuthInfo = ParserJsonUtils.parserLoginJson(options.getJSONObject("authInfo"));
        b.putSerializable("rtcAuthInfo", rtcAuthInfo);
        intent.putExtras(b);
        activity.startActivityForResult(intent, 1);

        JSONObject result = new JSONObject();
        result.put("type", "button");
        jsCallback.invoke(result);
    }

    @JSMethod(uiThread = true)
    public void dismiss() {
        destroy();
    }

    @Override
    public void destroy() {
    }

}
