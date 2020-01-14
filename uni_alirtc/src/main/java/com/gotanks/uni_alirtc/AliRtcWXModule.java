package com.gotanks.uni_alirtc;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
public class AliRtcWXModule extends WXSDKEngine.DestroyableModule {

    @JSMethod(uiThread = true)
    public void show(JSONObject options, JSCallback jsCallback) {
//        AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder()
//                .build();
//        System.out.println("123123123213123用户进入房间123123123asa");
//        Toast.makeText(mWXSDKInstance.getContext(), "用户进入房间", Toast.LENGTH_SHORT).show();
//        AlivcSvideoRecordActivity.startRecord(mWXSDKInstance.getContext(), recordParam);
//        if (mWXSDKInstance.getContext() instanceof Activity) {
//            Activity activity = (Activity) mWXSDKInstance.getContext();
//            Toast.makeText(activity, "用户进入房间", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(activity, AliRtcActivity.class);
//            Bundle b = new Bundle();
//            b.putString("channel", options.getString("username"));
//            b.putString("username", options.getString("channel"));
////            JSONObject authInfo = options.getJSONObject("authInfo");
////            RTCAuthInfo rtcAuthInfo = ParserJsonUtils.parserLoginJson(authInfo);
////            b.putSerializable("rtcAuthInfo", rtcAuthInfo);
//
//            intent.putExtras(b);
//            activity.startActivity(intent);
//        }
    }

    @JSMethod(uiThread = true)
    public void dismiss() {
        destroy();
    }

    @Override
    public void destroy() {
    }

}
