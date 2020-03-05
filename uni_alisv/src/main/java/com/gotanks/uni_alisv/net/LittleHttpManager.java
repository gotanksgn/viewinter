package com.gotanks.uni_alisv.net;

import android.content.Context;

import com.gotanks.uni_alisv.constants.AlivcLittleHttpConfig;
import com.gotanks.uni_alisv.constants.AlivcLittleServerApiConstants;
import com.gotanks.uni_alisv.net.data.LittleHttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;

/**
 * http manager
 */

public class LittleHttpManager {
    private static LittleHttpManager mInstance;
    private HttpEngine mHttpEngine;
    public static LittleHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (LittleHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new LittleHttpManager();
                }
            }
        }
        return mInstance;
    }
    public void init(Context context){
        mHttpEngine = new HttpEngine(context);
    }

    /**
     * 获取视频上传凭证
     *
     * @param title
     * @param fileName
     */

    public void requestVideoUploadAuth(String title, String fileName,
                                       HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                           .LittleVideoUploadAuthResponse> callback) {
//        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo();
//        String token = "";
//        if (userInfo != null) {
//            token = userInfo.getToken();
//        }
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_TITLE, title);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_FILE_NAME, fileName);
        Request request = new Request.Builder().url(
            urlAppend(AlivcLittleServerApiConstants.URL_GET_VIDEO_UPLOAD_AUTH, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleVideoUploadAuthResponse.class, callback);

    }

    /**
     * 刷新视频上传凭证
     *
     * @param videoId
     * @param callback
     */
    public void refreshVideoUploadAuth(String videoId,
                                       HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                           .LittleVideoUploadAuthResponse> callback) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_VIDEO_ID, videoId);
        Request request = new Request.Builder().url(
            urlAppend(AlivcLittleServerApiConstants.URL_REFRESH_VIDEO_UPLOAD_AUTH, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleVideoUploadAuthResponse.class, callback);
    }

    /**
     * 获取图片上传凭证
     *
     * @param callback
     */
    public void requestImageUploadAuth(HttpEngine.HttpResponseResultCallback<LittleHttpResponse
                                           .LittleImageUploadAuthResponse> callback) {
//        LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo();
//
//        String token = "";
//        if (userInfo != null) {
//            token = userInfo.getToken();
//        }
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token);
        hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_IMAGE_TYPE, "cover");
        Request request = new Request.Builder().url(
            urlAppend(AlivcLittleServerApiConstants.URL_GET_IMAGE_UPLOAD_AUTH, hashMap))
            .get()
            .build();
        mHttpEngine.request(request, LittleHttpResponse.LittleImageUploadAuthResponse.class, callback);
    }



    /**
     * 拼接get 请求的url
     *
     * @param baseUrl baseUrl
     * @param params  要拼接的参数
     * @return
     */
    private String urlAppend(String baseUrl, HashMap<String, String> params) {
        StringBuffer absUrl = new StringBuffer();
        absUrl.append(baseUrl);
        int size = 0;
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> item : entries) {
                if (size == 0) {
                    absUrl.append("?" + item.getKey() + "=" + item.getValue());
                    size++;
                } else {
                    absUrl.append("&" + item.getKey() + "=" + item.getValue());
                }
            }
        }
        return absUrl.toString();
    }

    public void cancelRequest(String cancelUrl) {
        mHttpEngine.cancel(cancelUrl);
    }

}
