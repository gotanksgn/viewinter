package com.gotanks.uni_alisv.editor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.aliyun.qupai.import_core.AliyunIImport;
import com.aliyun.qupai.import_core.AliyunImportCreator;
import com.aliyun.svideo.sdk.external.struct.common.AliyunImageClip;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoClip;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoParam;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.gotanks.uni_alisv.R;
import com.gotanks.uni_alisv.editor.view.AlivcEditView;
import com.gotanks.uni_alisv.media.MediaInfo;
import com.gotanks.uni_alisv.net.LittleHttpManager;
import com.gotanks.uni_alisv.net.data.LittleImageUploadAuthInfo;
import com.gotanks.uni_alisv.net.data.LittleVideoUploadAuthInfo;
import com.gotanks.uni_alisv.publish.PublishManager;
import com.gotanks.uni_alisv.recorder.bean.AlivcEditInputParam;
import com.gotanks.uni_alisv.recorder.bean.AlivcEditOutputParam;
import com.gotanks.uni_alisv.recorder.util.NotchScreenUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 编辑并上传；
 */
public class VideoEditorActivity extends AppCompatActivity {
    /**
     * 编辑后的特效配置文件地址，用户合成视频
     */
    private String mConfigPath;
    /**
     * 封面地址
     */
    private String mThumbnailPath;
    /**
     * 视频描述
     */
    private String mVideoDesc;
    /**
            * 视频合成输出文件名称
     */
    private String mComposeFileName = "output_compose_video.mp4";
    /**
     * 视频合成输出文件路径
     */
    private String mComposeOutputPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AlivcQuVideo/compose" + "/" + mComposeFileName;
    /**
     * 视频上传凭证信息
     */
    private LittleVideoUploadAuthInfo mVideoUploadAuthInfo;
    /**
     * 图片上传凭证信息
     */
    private LittleImageUploadAuthInfo mImageUploadAuthInfo;
    /**
     * 视频上传凭证信息获取成功
     */
    private boolean isVideoUploadAuthRequestSuccess = false;
    /**
     * 图片上传凭证信息获取成功
     */
    private boolean isImageUploadAuthRequestSuccess = false;

    public static void startEdit(Activity activity, AlivcEditInputParam param) {
        if (param == null || param.getMediaInfos() == null || param.getMediaInfos().size() == 0) {
            return;
        }
        Intent intent = new Intent(activity, VideoEditorActivity.class);
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_FRAME, param.getFrameRate());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_GOP, param.getGop());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_RATION_MODE, param.getRatio());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_QUALITY, param.getVideoQuality());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_RESOLUTION_MODE, param.getResolutionMode());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_CODEC, param.getVideoCodec());
        intent.putExtra(AlivcEditInputParam.INTETN_KEY_CRF, param.getCrf());
        intent.putExtra(AlivcEditInputParam.INTETN_KEY_SCANLE_RATE, param.getScaleRate());
        intent.putExtra(AlivcEditInputParam.INTETN_KEY_SCANLE_MODE, param.getScaleMode());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_TAIL_ANIMATION, param.isHasTailAnimation());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_REPLACE_MUSIC, param.isCanReplaceMusic());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_WATER_MARK, param.isHasWaterMark());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_RATION_MODE, param.getRatio());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_IS_MIX, param.isMixRecorder());
        intent.putParcelableArrayListExtra(AlivcEditInputParam.INTENT_KEY_MEDIA_INFO, param.getMediaInfos());
        activity.startActivityForResult(intent, 1);
    }


    private static final String TAG = "EditorActivity";
    private AlivcEditView editView;
    /**
     * 编辑输入参数
     */
    private AlivcEditInputParam mInputParam;
    private AliyunVideoParam mVideoParam;

    /**
     * 合成、上传管理类
     */
    private PublishManager mPublishManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 检测是否是全面屏手机, 如果不是, 设置FullScreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (!NotchScreenUtil.checkNotchScreen(this)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_editor);
        initData();
        editView = findViewById(R.id.alivc_edit_view);
        boolean hasTailAnimation = mInputParam.isHasTailAnimation();
        boolean isReplaceMusic = mInputParam.isCanReplaceMusic();
        boolean isMixRecorder = mInputParam.isMixRecorder();
        List<MediaInfo> mediaInfos = mInputParam.getMediaInfos();
        Uri mUri = Uri.fromFile(new File(getProjectJsonPath(mediaInfos)));
        editView.setParam(mVideoParam, mUri, hasTailAnimation, mInputParam.isHasWaterMark());
        editView.setReplaceMusic(isReplaceMusic);
        editView.setHasRecordMusic(isReplaceMusic);
        editView.setIsMixRecord(isMixRecorder);

        editView.setmOnFinishListener(new AlivcEditView.OnFinishListener() {
            @Override
            public void onComplete(AlivcEditOutputParam outputParam) {
                //编辑完成跳转到其他界面
//                Intent intent = new Intent();
//                intent.setClassName(this, NEXT_ACTIVITY_CLASS_NAME);
//                intent.putExtra(PublishActivity.KEY_PARAM_THUMBNAIL, outputParam.getThumbnailPath());
//                intent.putExtra(PublishActivity.KEY_PARAM_CONFIG, outputParam.getConfigPath());
//                intent.putExtra(PublishActivity.KEY_PARAM_VIDEO_WIDTH, outputParam.getOutputVideoWidth());
//                intent.putExtra(PublishActivity.KEY_PARAM_VIDEO_HEIGHT, outputParam.getOutputVideoHeight());
//                //传入视频比列
//                intent.putExtra(PublishActivity.KEY_PARAM_VIDEO_RATIO, outputParam.getVideoRatio());
//                intent.putExtra("videoParam", outputParam.getVideoParam());
//                startActivity(intent);
                Log.d(TAG, "onComplete: " + outputParam.getThumbnailPath());
                Log.d(TAG, "onComplete: " + outputParam.getConfigPath());
                Log.d(TAG, "onComplete: " + outputParam.getVideoParam());
                Toast.makeText(VideoEditorActivity.this, "111", Toast.LENGTH_SHORT).show();
                mThumbnailPath = outputParam.getThumbnailPath();
                mVideoDesc = "123123";
                mConfigPath = outputParam.getConfigPath();
                if (mPublishManager != null) {
                    mPublishManager.releaseCompose();
                }
                mPublishManager = new PublishManager(VideoEditorActivity.this.getApplicationContext());
                Toast.makeText(VideoEditorActivity.this, "222", Toast.LENGTH_SHORT).show();
                //TODO 进度条；上传；返回；
                LittleHttpManager.getInstance().init(VideoEditorActivity.this.getApplicationContext());
                Toast.makeText(VideoEditorActivity.this, "333", Toast.LENGTH_SHORT).show();
                LittleHttpManager.getInstance().requestImageUploadAuth(
                        (result, message, data) -> {
                            if (result) {
                                isImageUploadAuthRequestSuccess = true;
                                mImageUploadAuthInfo = data.data;
                                if (isVideoUploadAuthRequestSuccess) {
                                    mPublishManager.startComposeAndUpload(
                                            mConfigPath,
                                            mComposeOutputPath,
                                            mVideoUploadAuthInfo.getVideoId(),
                                            mVideoUploadAuthInfo.getUploadAddress(),
                                            mVideoUploadAuthInfo.getUploadAuth(),
                                            mVideoDesc,
                                            mThumbnailPath,
                                            mImageUploadAuthInfo.getImageId(),
                                            mImageUploadAuthInfo.getImageURL(),
                                            mImageUploadAuthInfo.getUploadAddress(),
                                            mImageUploadAuthInfo.getUploadAuth(),
                                            mUploadCallback);
                                }
                            }
                        });
                Toast.makeText(VideoEditorActivity.this, "444", Toast.LENGTH_SHORT).show();
                LittleHttpManager.getInstance().requestVideoUploadAuth("video", mComposeFileName,
                        (result, message, data) -> {
                            if (result) {
                                isVideoUploadAuthRequestSuccess = true;
                                mVideoUploadAuthInfo = data.data;
                                if (isImageUploadAuthRequestSuccess) {
                                    mPublishManager.startComposeAndUpload(
                                            mConfigPath,
                                            mComposeOutputPath,
                                            mVideoUploadAuthInfo.getVideoId(),
                                            mVideoUploadAuthInfo.getUploadAddress(),
                                            mVideoUploadAuthInfo.getUploadAuth(),
                                            mVideoDesc,
                                            mThumbnailPath,
                                            mImageUploadAuthInfo.getImageId(),
                                            mImageUploadAuthInfo.getImageURL(),
                                            mImageUploadAuthInfo.getUploadAddress(),
                                            mImageUploadAuthInfo.getUploadAuth(),
                                            mUploadCallback);
                                }
                            }

                        });

                Toast.makeText(VideoEditorActivity.this, "555", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private PublishManager.MyComposeListener mUploadCallback = new PublishManager.MyComposeListener() {

        @Override
        public void onComposeStart() {
//            showProgress(mPublishManager.getThumbnail());
        }

        @Override
        public void onComposeProgress(int i) {
//            updateProgress(i / 2);
        }

        @Override
        public void onComposeCompleted() {

        }

        @Override
        public void onComposeError(int i) {
//            if (mOutOnUploadCompleteListener != null) {
//                mOutOnUploadCompleteListener.onFailure(i + "", getResources().getString(R.string.alivc_little_main_creation_failed));
//            }
        }

        @Override
        public void onUploadStart() {

        }

        @Override
        public void onUploadSucceed(String videoId, String imageUrl, String describe) {
//            if (mOutOnUploadCompleteListener != null) {
//                mOutOnUploadCompleteListener.onSuccess(videoId, imageUrl, describe);
//            }
            if (mPublishManager != null) {
                mPublishManager.releaseCompose();
                mPublishManager = null;
            }

            Log.i(TAG, "videoId"+videoId);
            Log.i(TAG, "imageUrl"+imageUrl);
            Log.i(TAG, "describe"+describe);
            Intent intent = new Intent();
            intent.putExtra("videoId",videoId);
            intent.putExtra("imageUrl",imageUrl);
            intent.putExtra("describe",describe);
//            intent.setClass(VideoEditorActivity.this, PandoraEntry.class);
//            startActivity(intent);

            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void onUploadFailed(String code, String message, boolean isImageUpload) {
//            if (TEIM_STAMP_ERROR.equals(code)) {
//                if (mOutOnUploadCompleteListener != null) {
//                    mOutOnUploadCompleteListener.onFailure(code, message);
//                }
//                return;
//            }
//            showRetryDialog(code, message );
            if (mPublishManager != null) {
                mPublishManager.releaseCompose();
                mPublishManager = null;
            }
        }

        @Override
        public void onUploadProgress(long uploadedSize, long totalSize) {
            int progress = (int)((uploadedSize * 100) / totalSize) + 50;
            Log.e("onUploadProgress", "progress" + progress);
//            updateProgress(progress);
        }

        @Override
        public void onUploadAuthExpired(boolean isImageUpload, String videoId) {
//            if (mAuthInfoExpiredListener != null) {
//                if (isImageUpload) {
//                    mAuthInfoExpiredListener.onImageAuthInfoExpired();
//                } else {
//                    mAuthInfoExpiredListener.onVideoAuthInfoExpired(videoId);
//                }
//            }
        }

    };

    private void initData() {
        Intent intent = getIntent();
        int mFrameRate = intent.getIntExtra(AlivcEditInputParam.INTENT_KEY_FRAME, 30);
        int mGop = intent.getIntExtra(AlivcEditInputParam.INTENT_KEY_GOP, 250);
        int mRatio = intent.getIntExtra(AlivcEditInputParam.INTENT_KEY_RATION_MODE, AlivcEditInputParam.RATIO_MODE_9_16);
        VideoQuality mVideoQuality = (VideoQuality) intent.getSerializableExtra(AlivcEditInputParam.INTENT_KEY_QUALITY);
        if (mVideoQuality == null) {
            mVideoQuality = VideoQuality.HD;
        }
        int mResolutionMode = intent.getIntExtra(AlivcEditInputParam.INTENT_KEY_RESOLUTION_MODE, AlivcEditInputParam.RESOLUTION_720P);
        VideoCodecs mVideoCodec = (VideoCodecs) intent.getSerializableExtra(AlivcEditInputParam.INTENT_KEY_CODEC);
        if (mVideoCodec == null) {
            mVideoCodec = VideoCodecs.H264_HARDWARE;
        }
        int mCrf = intent.getIntExtra(AlivcEditInputParam.INTETN_KEY_CRF, 23);
        float mScaleRate = intent.getFloatExtra(AlivcEditInputParam.INTETN_KEY_SCANLE_RATE, 1.0f);
        VideoDisplayMode mScaleMode = (VideoDisplayMode) intent.getSerializableExtra(AlivcEditInputParam.INTETN_KEY_SCANLE_MODE);
        if (mScaleMode == null) {
            mScaleMode = VideoDisplayMode.FILL;
        }
        boolean mHasTailAnimation = intent.getBooleanExtra(AlivcEditInputParam.INTENT_KEY_TAIL_ANIMATION, false);
        boolean canReplaceMusic = intent.getBooleanExtra(AlivcEditInputParam.INTENT_KEY_REPLACE_MUSIC, true);
        ArrayList<MediaInfo> mediaInfos = intent.getParcelableArrayListExtra(AlivcEditInputParam.INTENT_KEY_MEDIA_INFO);
        boolean hasWaterMark = intent.getBooleanExtra(AlivcEditInputParam.INTENT_KEY_WATER_MARK, false);
        boolean isMixRecord = intent.getBooleanExtra(AlivcEditInputParam.INTENT_KEY_IS_MIX, false);
        mInputParam = new AlivcEditInputParam.Builder()
                .setFrameRate(mFrameRate)
                .setGop(mGop)
                .setRatio(mRatio)
                .setVideoQuality(mVideoQuality)
                .setResolutionMode(mResolutionMode)
                .setVideoCodec(mVideoCodec)
                .setCrf(mCrf)
                .setScaleRate(mScaleRate)
                .setScaleMode(mScaleMode)
                .setHasTailAnimation(mHasTailAnimation)
                .setCanReplaceMusic(canReplaceMusic)
                .addMediaInfos(mediaInfos)
                .setHasWaterMark(hasWaterMark)
                .setIsMixRecorder(isMixRecord)
                .build();
        if (isMixRecord) {
            mVideoParam = mInputParam.generateMixVideoParam();
        } else {
            mVideoParam = mInputParam.generateVideoParam();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (editView != null) {
            editView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (editView != null) {
            editView.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (editView != null) {
            editView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (editView != null) {
            editView.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (editView != null) {
            editView.onStart();
        }
    }

    @Override
    public void onBackPressed() {
        boolean isConsume = false;
        if (editView != null) {
            isConsume = editView.onBackPressed();
        }
        if (!isConsume) {
            super.onBackPressed();
            finish();
        }
    }

    /**
     * 通过MediaInfo生成ProjectJson
     *
     * @param mediaInfos List<MediaInfo>
     * @return jsonPath
     */
    private String getProjectJsonPath(List<MediaInfo> mediaInfos) {

        AliyunIImport mImport = AliyunImportCreator.getImportInstance(this);
        mImport.setVideoParam(mVideoParam);
        int size = mediaInfos.size();
        for (int i = 0; i < size; i++) {
            MediaInfo mediaInfo = mediaInfos.get(i);
            if (mediaInfo.mimeType.startsWith("video")) {
                mImport.addMediaClip(new AliyunVideoClip.Builder()
                        .source(mediaInfo.filePath)
                        .startTime(mediaInfo.startTime)
                        .endTime(mediaInfo.startTime + mediaInfo.duration)
                        .build());
            } else if (mediaInfo.mimeType.startsWith("image")) {
                mImport.addMediaClip(new AliyunImageClip.Builder()
                        .source(mediaInfo.filePath)
                        .duration(mediaInfo.duration)
                        .build());
            }
        }
        String projectConfigure = mImport.generateProjectConfigure();
        mImport.release();

        return projectConfigure;
    }

    public static void startEdit(Context context, AlivcEditInputParam param) {
        if (param == null || param.getMediaInfos() == null || param.getMediaInfos().size() == 0) {
            return;
        }
        Intent intent = new Intent(context, VideoEditorActivity.class);
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_FRAME, param.getFrameRate());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_GOP, param.getGop());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_RATION_MODE, param.getRatio());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_QUALITY, param.getVideoQuality());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_RESOLUTION_MODE, param.getResolutionMode());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_CODEC, param.getVideoCodec());
        intent.putExtra(AlivcEditInputParam.INTETN_KEY_CRF, param.getCrf());
        intent.putExtra(AlivcEditInputParam.INTETN_KEY_SCANLE_RATE, param.getScaleRate());
        intent.putExtra(AlivcEditInputParam.INTETN_KEY_SCANLE_MODE, param.getScaleMode());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_TAIL_ANIMATION, param.isHasTailAnimation());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_REPLACE_MUSIC, param.isCanReplaceMusic());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_WATER_MARK, param.isHasWaterMark());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_RATION_MODE, param.getRatio());
        intent.putExtra(AlivcEditInputParam.INTENT_KEY_IS_MIX, param.isMixRecorder());
        intent.putParcelableArrayListExtra(AlivcEditInputParam.INTENT_KEY_MEDIA_INFO, param.getMediaInfos());
        context.startActivity(intent);
    }

}
