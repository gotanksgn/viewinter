package com.gotanks.uni_alisv.net.data;

/**
 * http response
 * 包含公共部分和独有部分
 */
public class LittleHttpResponse {
    public boolean result;
    public String requestId;
    public String message;
    public int code;


    /**
     * 视频上传凭证
     */
    public static class LittleVideoUploadAuthResponse extends LittleHttpResponse {
        public LittleVideoUploadAuthInfo data;
    }
    /**
     * 图片上传凭证
     */
    public static class LittleImageUploadAuthResponse extends LittleHttpResponse {
        public LittleImageUploadAuthInfo data;
    }
}
