package com.gotanks.uni_alisv.common;

import android.os.Environment;

import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.gotanks.uni_alisv.editor.bean.AlivcEditInputParam;

/**
 * 跳转到短视频录制和编辑模块的默认参数
 *
 * @author xlx
 */
public class LittleVideoParamConfig {


    /**
     * ========================裁剪参数==============================
     */
    public static class Crop {

        /**
         * minVideoDuration: 最短视频时间, 4000ms
         */
        public static final int MIN_VIDEO_DURATION = 4000;

        /**
         * maxVideoDuration: 最长视频时间, 29*1000 ms
         */
        public static final int MAX_VIDEO_DURATION = 29 * 1000;

        /**
         * minCropDuration: 最短裁剪时间, 3000ms
         */
        public static final int MIN_CROP_DURATION = 29 * 1000;

        /**
         * frameRate: 25
         */
        public static final int FRAME_RATE = 25;

        /**
         * cropMode: 裁剪模式, VideoDisplayMode.SCALE
         */
        public static final VideoDisplayMode CROP_MODE = VideoDisplayMode.SCALE;
    }

    /**
     * ========================编辑参数==============================
     */
    public static class Editor {

        /**
         * VIDEO_RATIO: 纵横比, CropKey.RATIO_MODE_9_16
         */
        public static final int VIDEO_RATIO = AlivcEditInputParam.RATIO_MODE_ORIGINAL;

        /**
         * VIDEO_SCALE: 裁剪模式, CropKey.SCALE_FILL
         */
        public static final VideoDisplayMode VIDEO_SCALE = VideoDisplayMode.FILL;

        /**
         * VIDEO_QUALITY: 清晰度, VideoQuality.HD
         */
        public static final VideoQuality VIDEO_QUALITY = VideoQuality.HD;

        /**
         * VIDEO_FRAMERATE:帧率,  25
         */
        public static final int VIDEO_FRAMERATE = 25;

        /**
         * VIDEO_GOP: 125
         */
        public static final int VIDEO_GOP = 125;

        /**
         * videoCodec:编码方式, VideoCodecs.H264_HARDWARE
         */
        public static final VideoCodecs VIDEO_CODEC = VideoCodecs.H264_HARDWARE;
    }


    /**
     * 视频列表缓存目录
     */
    public static String DIR_CACHE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AlivcQuVideo/cache";
    /**
     * 视频下载目录
     */
    public static String DIR_DOWNLOAD = "AlivcQuVideo/download/";
    /**
     * 视频合成目录
     */
    public static String DIR_COMPOSE = "AlivcQuVideo/compose/";


}
