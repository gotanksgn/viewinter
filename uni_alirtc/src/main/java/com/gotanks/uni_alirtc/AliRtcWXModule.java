package com.gotanks.uni_alirtc;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.gotanks.uni_alirtc.activity.VideoChatActivity;
import com.gotanks.uni_alirtc.bean.RTCAuthInfo;
import com.gotanks.uni_alirtc.utils.ParserJsonUtils;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

public class AliRtcWXModule extends WXSDKEngine.DestroyableModule {

    public static final String TAG = "AliRtcWXModule";

    @JSMethod(uiThread = true)
    public void show(JSONObject options, JSCallback jsCallback) {
        Activity activity = (Activity) mWXSDKInstance.getContext();

        Bundle videoChatArgs = new Bundle();
        //用户名
        videoChatArgs.putString("username", options.getString("userid"));
        //频道号
        videoChatArgs.putString("channel", options.getString("roomid"));
        //音频采集
        videoChatArgs.putBoolean("audioCapture", true);
        //音频播放
        videoChatArgs.putBoolean("audioPlay", true);
        RTCAuthInfo rtcAuthInfo = ParserJsonUtils.parserLoginJson(options.getJSONObject("authInfo"));
        videoChatArgs.putSerializable("rtcAuthInfo", rtcAuthInfo);
        VideoChatActivity.launchForResult(activity, videoChatArgs);

    }

    @JSMethod(uiThread = true)
    public void dismiss() {
        destroy();
    }

    @Override
    public void destroy() {
    }

}
