package com.gotanks.uni_alisv.recorder.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.aliyun.common.global.AliyunTag;
import com.aliyun.common.utils.StorageUtils;
import com.aliyun.mix.AliyunMixMediaInfoParam;
import com.aliyun.mix.AliyunMixRecorderDisplayParam;
import com.aliyun.mix.AliyunMixTrackLayoutParam;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.recorder.supply.AliyunIClipManager;
import com.aliyun.recorder.supply.RecordCallback;
import com.aliyun.svideo.base.Constants;
import com.aliyun.svideo.base.widget.ProgressDialog;
import com.aliyun.svideo.base.widget.RecordTimelineView;
import com.aliyun.svideo.common.utils.DensityUtils;
import com.aliyun.svideo.common.utils.PermissionUtils;
import com.aliyun.svideo.common.utils.ThreadUtils;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.effect.EffectPaster;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraParam;
import com.aliyun.svideo.sdk.external.struct.recorder.MediaInfo;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.gotanks.uni_alisv.R;
import com.gotanks.uni_alisv.recorder.activity.AlivcSvideoRecordActivity;
import com.gotanks.uni_alisv.recorder.mixrecorder.AlivcIMixRecorderInterface;
import com.gotanks.uni_alisv.recorder.util.Common;
import com.gotanks.uni_alisv.recorder.util.FixedToastUtils;
import com.gotanks.uni_alisv.recorder.util.OrientationDetector;
import com.gotanks.uni_alisv.recorder.util.TimeFormatterUtils;
import com.gotanks.uni_alisv.recorder.view.control.CameraType;
import com.gotanks.uni_alisv.recorder.view.control.ControlView;
import com.gotanks.uni_alisv.recorder.view.control.ControlViewListener;
import com.gotanks.uni_alisv.recorder.view.control.FlashType;
import com.gotanks.uni_alisv.recorder.view.control.RecordState;
import com.gotanks.uni_alisv.recorder.view.countdown.AlivcCountDownView;
import com.gotanks.uni_alisv.recorder.view.focus.FocusView;
import com.qu.preview.callback.OnFrameCallBack;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 新版本(> 3.6.5之后)录制模块的具体业务实现类
 */
public class AliyunSVideoRecordView extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = AliyunSVideoRecordView.class.getSimpleName();
    //最小录制时长
    private static final int MIN_RECORD_TIME = 0;
    //最大录制时长
    private static final int MAX_RECORD_TIME = Integer.MAX_VALUE;

    /**
     * v3.7.8改动, GlsurfaceView --> SurfaceView
     * AliyunIRecorder.setDisplayView(GLSurfaceView surfaceView) =====>> AliyunIRecorder.setDisplayView(SurfaceView surfaceView)
     */
    private SurfaceView mRecorderSurfaceView;
    private SurfaceView mPlayerSurfaceView;

    private ControlView mControlView;
    private RecordTimelineView mRecordTimeView;
    private AlivcCountDownView mCountDownView;
    // private AliyunIRecorder recorder;

    private AlivcIMixRecorderInterface recorder;

    private AliyunIClipManager clipManager;
    private com.aliyun.svideo.sdk.external.struct.recorder.CameraType cameraType
        = com.aliyun.svideo.sdk.external.struct.recorder.CameraType.FRONT;
    private FragmentActivity mActivity;
    private boolean isOpenFailed = false;
    //正在准备录制视频,readyview显示期间为true，其他为false
    private boolean isLoadingReady = false;
    //录制视频是否达到最大值
    private boolean isMaxDuration = false;
    //录制时长
    private int recordTime = 0;

    //最小录制时长
    private int minRecordTime = 2000;
    //最大录制时长
    private int maxRecordTime = 15 * 1000;
    //关键帧间隔
    private int mGop = 5;
    //视频质量
    private VideoQuality mVideoQuality = VideoQuality.HD;
    //视频比例
    private int mRatioMode = AliyunSnapVideoParam.RATIO_MODE_3_4;
    //编码方式
    private VideoCodecs mVideoCodec = VideoCodecs.H264_HARDWARE;

    //视频输出参数
    private MediaInfo mOutputInfo;

    //合拍输入视频参数
    private AliyunMixMediaInfoParam mMixInputInfo;

    //用来合拍的视频路径
    private String mMixVideoPath;


    //视频分辨率
    private int mResolutionMode = AliyunSnapVideoParam.RESOLUTION_540P;

    //选中的贴图效果
    private EffectPaster effectPaster;
    private OrientationDetector orientationDetector;
    private int rotation;
    /**
     * 相机的原始NV21数据
     */
    private byte[] frameBytes;
    private byte[] mFuImgNV21Bytes;
    /**
     * 原始数据宽
     */
    private int frameWidth;
    /**
     * 原始数据高
     */
    private int frameHeight;

    /**
     * 控制mv的添加, 开始录制后,不允许切换mv
     */
    private boolean isAllowChangeMv = true;


    private AsyncTask<Void, Integer, Integer> finishRecodingTask;
    private AsyncTask<Void, Void, Void> faceTrackPathTask;

    private ProgressDialog progressBar;

    /**
     * 是否处于后台
     */
    private boolean mIsBackground;

    private FocusView mFocusView;
    /**
     * 用于防止sdk的oncomplete回调之前, 再次调用startRecord
     */
    private boolean tempIsComplete = true;

    public AliyunSVideoRecordView(Context context) {
        super(context);
        // initVideoView();
    }

    public AliyunSVideoRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // initVideoView();
    }

    public AliyunSVideoRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // initVideoView();
    }

    private void initVideoView() {
        //初始化surfaceView

        initControlView();
        initCountDownView();
        initRecordTimeView();
        setFaceTrackModePath();

    }




    public void onPause() {
        mIsBackground = true;
    }

    public void onResume() {
        mIsBackground = false;
    }

    public void onStop() {
        if (mFocusView != null) {
            mFocusView.activityStop();
        }
    }

    /**
     * 初始化倒计时view
     */
    private void initCountDownView() {
        if (mCountDownView == null) {
            mCountDownView = new AlivcCountDownView(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            addView(mCountDownView, params);
        }
    }

    private void initRecordTimeView() {
        mRecordTimeView = new RecordTimelineView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dip2px(getContext(), 6));

        params.setMargins(DensityUtils.dip2px(getContext(), 12),
                          DensityUtils.dip2px(getContext(), 6),
                          DensityUtils.dip2px(getContext(), 12),
                          0);
        mRecordTimeView.setColor(R.color.alivc_record_bg_timeview_duraton, R.color.alivc_record_bg_timeview_selected, R.color.alivc_common_white,
                                 R.color.alivc_record_bg_timeview);
        mRecordTimeView.setMaxDuration(clipManager.getMaxDuration());
        mRecordTimeView.setMinDuration(clipManager.getMinDuration());
        addView(mRecordTimeView, params);
        mRecordTimeView.setMaxDuration(getMaxRecordTime());
        mRecordTimeView.setMinDuration(minRecordTime);
    }

    /**
     * 初始化RecordersurfaceView
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initRecorderSurfaceView() {
        mRecorderSurfaceView = new SurfaceView(getContext());
        final ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        final GestureDetector gestureDetector = new GestureDetector(getContext(),
        new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (recorder == null) {
                    return true;
                }
                float x = e.getX() / mRecorderSurfaceView.getWidth();
                float y = e.getY() / mRecorderSurfaceView.getHeight();
                recorder.setFocus(x, y);

                mFocusView.showView();
                mFocusView.setLocation(e.getRawX(), e.getRawY());
                return true;
            }
        });
        mRecorderSurfaceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() >= 2) {
                    scaleGestureDetector.onTouchEvent(event);
                } else if (event.getPointerCount() == 1) {
                    gestureDetector.onTouchEvent(event);
                }
                return true;
            }
        });
        //添加录制surFaceView
        recorder.setDisplayView(mRecorderSurfaceView, mPlayerSurfaceView);

        addSubView(mRecorderSurfaceView);
        mRecorderSurfaceView.setLayoutParams(recorder.getLayoutParams());

    }

    /**
     * 初始化PlayerSurfaceView
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initPlayerSurfaceView() {
        mPlayerSurfaceView = new SurfaceView(getContext());
        addSubView(mPlayerSurfaceView);
    }


    /**
     * 初始化控制栏view
     */
    private void initControlView() {
        mControlView = new ControlView(getContext());
        mControlView.setControlViewListener(new ControlViewListener() {
            @Override
            public void onBackClick() {
                if (mBackClickListener != null) {
                    mBackClickListener.onClick();
                }
            }

            @Override
            public void onNextClick() {
                // 完成录制
                if (!isStopToCompleteDuration) {
                    finishRecording();
                }
            }

            @Override
            public void onCameraSwitch() {
                if (recorder != null) {
                    int cameraId = recorder.switchCamera();
                    for (com.aliyun.svideo.sdk.external.struct.recorder.CameraType type : com.aliyun.svideo.sdk
                            .external.struct.recorder.CameraType
                            .values()) {
                        if (type.getType() == cameraId) {
                            cameraType = type;
                        }
                    }
                    if (mControlView != null) {
                        for (CameraType type : CameraType.values()) {
                            if (type.getType() == cameraId) {
                                mControlView.setCameraType(type);
                            }
                        }

                        if (mControlView.getFlashType() == FlashType.ON
                                && mControlView.getCameraType() == CameraType.BACK) {
                            recorder.setLight(com.aliyun.svideo.sdk.external.struct.recorder.FlashType.TORCH);
                        }
                    }
                }
            }

            @Override
            public void onLightSwitch(FlashType flashType) {
                if (recorder != null) {
                    for (com.aliyun.svideo.sdk.external.struct.recorder.FlashType type : com.aliyun.svideo.sdk
                            .external.struct.recorder.FlashType
                            .values()) {
                        if (flashType.toString().equals(type.toString())) {
                            recorder.setLight(type);
                        }
                    }

                }
                if (mControlView.getFlashType() == FlashType.ON
                        && mControlView.getCameraType() == CameraType.BACK) {
                    recorder.setLight(com.aliyun.svideo.sdk.external.struct.recorder.FlashType.TORCH);
                }
            }

            @Override
            public void onRateSelect(float rate) {
                if (recorder != null) {
                    recorder.setRate(rate);
                }
            }

            @Override
            public void onReadyRecordClick(boolean isCancel) {
                if (isStopToCompleteDuration) {
                    /*TODO  这里是因为如果 SDK 还没有回调onComplete,倒计时录制，会crash */
                    return;
                }
                if (isCancel) {
                    cancelReadyRecord();
                } else {
                    showReadyRecordView();
                }

            }

            @Override
            public void onStartRecordClick() {
                if (!tempIsComplete) {
                    // 连续点击开始录制，停止录制，要保证在onComplete回调回来再允许点击开始录制,
                    // 否则SDK会出现ANR问题, v3.7.8修复会影响较大, 工具包暂时处理
                    return;
                }
                startRecord();
            }

            @Override
            public void onStopRecordClick() {
                if (!tempIsComplete) {
                    // 连续点击开始录制，停止录制，要保证在onComplete回调回来再允许点击开始录制,
                    // 否则SDK会出现ANR问题, v3.7.8修复会影响较大, 工具包暂时处理
                    return;
                }
                stopRecord();
            }

            @Override
            public void onDeleteClick() {
                if (isStopToCompleteDuration) {
                    // 这里是因为如果 SDK 还没有回调onComplete,点击回删会出现删除的不是最后一段的问题
                    return;
                }
                mRecordTimeView.deleteLast();
                // clipManager.deletePart();
                recorder.deleteLastPart();
                isMaxDuration = false;
                if (mControlView != null) {
                    if (clipManager.getDuration() < clipManager.getMinDuration()) {
                        mControlView.setCompleteEnable(false);
                    }

                    mControlView.updataCutDownView(true);
                }

                if (clipManager.getDuration() == 0) {
                    //音乐可以选择
                    recorder.restartMv();
                    mControlView.setHasRecordPiece(false);
                    isAllowChangeMv = true;
                }
                mControlView.setRecordTime(TimeFormatterUtils.formatTime(clipManager.getDuration()));
            }
        });
//        mControlView.setRecordType(recorder.isMixRecorder());
        addSubView(mControlView);
        mControlView.setAspectRatio(mRatioMode);
    }

    /**
     * 权限申请
     */
    String[] permission = {
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 开始录制
     */
    private void startRecord() {
        boolean checkResult = PermissionUtils.checkPermissionsGroup(getContext(), permission);
        if (!checkResult && mActivity != null) {
            PermissionUtils.requestPermissions(mActivity, permission,
                                               AlivcSvideoRecordActivity.PERMISSION_REQUEST_CODE);
            return;
        }

        if (isMaxDuration) {
            mControlView.setRecordState(RecordState.STOP);
            return;
        }

        if (recorder != null && !mIsBackground) {
            // 快速显示回删字样, 将音乐按钮置灰,假设此时已经有片段, 在录制失败时, 需要改回false
            mControlView.setHasRecordPiece(true);
            mControlView.setRecordState(RecordState.RECORDING);
            mControlView.setRecording(true);
            String videoPath = Constants.SDCardConstants.OUTPUT_PATH_DIR + File.separator + System.currentTimeMillis() + "-record.mp4";
            recorder.setOutputPath(videoPath);
            recorder.startRecording();

            Log.d(TAG, "startRecording    isStopToCompleteDuration:" + isStopToCompleteDuration);
        }

    }

    /**
     * 视频是是否正正在已经调用stopRecord到onComplete回调过程中这段时间，这段时间不可再次调用stopRecord
     * true: 正在调用stop~onComplete, false反之
     */
    private boolean isStopToCompleteDuration;

    /**
     * 停止录制
     */
    private void stopRecord() {
        Log.d(TAG, "stopRecord    isStopToCompleteDuration:" + isStopToCompleteDuration);
        if (recorder != null && !isStopToCompleteDuration && mControlView.isRecording()) {//
            isStopToCompleteDuration = true;

            //此处添加判断，progressBar弹出，也即当视频片段合成的时候，不调用stopRecording,
            //否则在finishRecording的时候调用stopRecording，会导致finishRecording阻塞
            //暂时规避，等待sdk解决该问题，取消该判断
            if ((progressBar == null || !progressBar.isShowing())) {
                recorder.stopRecording();

            }

        }

    }

    /**
     * 取消拍摄倒计时
     */
    private void cancelReadyRecord() {
        if (mCountDownView != null) {
            mCountDownView.cancle();
        }
    }

    /**
     * 显示准备拍摄倒计时view
     */
    private void showReadyRecordView() {
        if (mCountDownView != null) {
            mCountDownView.start();
        }
    }


    private void addEffectToRecord(String path) {

        if (effectPaster != null) {
            recorder.removePaster(effectPaster);
        }

        effectPaster = new EffectPaster(path);
        recorder.addPaster(effectPaster);

    }

    private FragmentManager getFragmentManager() {
        FragmentManager fm = null;
        if (mActivity != null) {
            fm = mActivity.getSupportFragmentManager();
        } else {
            Context mContext = getContext();
            if (mContext instanceof FragmentActivity) {
                fm = ((FragmentActivity) mContext).getSupportFragmentManager();
            }
        }
        return fm;
    }


    /**
     * 设置录制类型
     */
    public void setRecorder(AlivcIMixRecorderInterface recorder) {
        this.recorder = recorder;

        initRecorder();
        initVideoView();

    }

    private void initRecorder() {
        recorder.setGop(mGop);
        recorder.setVideoQuality(mVideoQuality);
        recorder.setRatioMode(mRatioMode);
        recorder.setResolutionMode(mResolutionMode);
        clipManager = recorder.getClipManager();
        clipManager.setMaxDuration(getMaxRecordTime());
        clipManager.setMinDuration(minRecordTime);
        recorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);
        //mediaInfo.setHWAutoSize(true);//硬编时自适应宽高为16的倍数
        cameraType = recorder.getCameraCount() == 1 ? com.aliyun.svideo.sdk.external.struct.recorder.CameraType.BACK : cameraType;
        recorder.setCamera(cameraType);
        recorder.setBeautyStatus(false);

        initOrientationDetector();


        //获取需要的surFaceView数量
        if (recorder.isMixRecorder()) {
            initPlayerSurfaceView();
            initRecorderSurfaceView();
            //设置录制界面位置
            recorder.setMixRecorderRatio(mRecorderSurfaceView);
            //设置播放界面的位置
            recorder.setMixPlayerRatio(mPlayerSurfaceView);
        } else {
            initRecorderSurfaceView();
        }

        //设置视频输入输出参数
        setMediaInfo();

        recorder.setOnFrameCallback(new OnFrameCallBack() {
            @Override
            public void onFrameBack(byte[] bytes, int width, int height, Camera.CameraInfo info) {
                //原始数据回调 NV21,这里获取原始数据主要是为了faceUnity高级美颜使用
                frameBytes = bytes;
                frameWidth = width;
                frameHeight = height;
            }

            @Override
            public Camera.Size onChoosePreviewSize(List<Camera.Size> supportedPreviewSizes,
                                                   Camera.Size preferredPreviewSizeForVideo) {
                return null;
            }

            @Override
            public void openFailed() {
                Log.e(AliyunTag.TAG, "openFailed----------");
                isOpenFailed = true;
            }
        });

        recorder.setRecordCallback(new RecordCallback() {
            @Override
            public void onComplete(final boolean validClip, final long clipDuration) {
                Log.i(TAG, "onComplete   duration : " + clipDuration +
                      ", clipManager.getDuration() = " + clipManager.getDuration());
                tempIsComplete = true;
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onComplete    isStopToCompleteDuration:" + isStopToCompleteDuration);

//                        if (recorder.isMixRecorder() && !isMaxDuration) {
//                            ToastUtils.show(mActivity, getResources().getString(R.string.alivc_mix_record_continue), Gravity.CENTER, Toast.LENGTH_SHORT);
//                        }

                        isStopToCompleteDuration = false;
                        handleStopCallback(validClip, clipDuration);
                        if (isMaxDuration && validClip) {
                            finishRecording();
                        }

                    }
                });

            }

            /**
             * 合成完毕的回调
             * @param outputPath
             */
            @Override
            public void onFinish(final String outputPath) {
                Log.i(TAG, "onFinish:" + outputPath);

                if (progressBar != null && progressBar.isShowing()) {
                    progressBar.dismiss();
                }

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCompleteListener != null) {
                            final int duration = clipManager.getDuration();
                            //deleteAllPart();
                            // 选择音乐后, 在录制完合成过程中退后台
                            // 保持在后台情况下, sdk合成完毕后, 会仍然执行跳转代码, 此时会弹起跳转后的页面
                            if (activityStoped) {
                                pendingCompseFinishRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isStopToCompleteDuration) {
                                            mCompleteListener.onComplete(outputPath, duration, mRatioMode);
                                        }
                                    }
                                };
                            } else {
                                if (!isStopToCompleteDuration) {
                                    mCompleteListener.onComplete(outputPath, duration, mRatioMode);
                                }
                            }
                        }
                    }
                });

            }

            @Override
            public void onProgress(final long duration) {
                final int currentDuration = clipManager.getDuration();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isAllowChangeMv = false;
                        recordTime = 0;
                        //设置录制进度
                        if (mRecordTimeView != null) {
                            mRecordTimeView.setDuration((int) duration);
                        }

                        recordTime = (int) (currentDuration + duration);

                        Log.d(TAG, "onProgress: " + recordTime + "——————" + currentDuration + "————" + duration);

                        if (recordTime <= clipManager.getMaxDuration() && recordTime >= clipManager.getMinDuration()) {
                            // 2018/7/11 让下一步按钮可点击
                            mControlView.setCompleteEnable(true);
                        } else {
                            mControlView.setCompleteEnable(false);
                        }
                        if (mControlView != null && mControlView.getRecordState().equals(RecordState.STOP)) {
                            return;
                        }
                        if (mControlView != null) {
                            mControlView.setRecordTime(TimeFormatterUtils.formatTime(recordTime));
                        }

                    }
                });

            }

            @Override
            public void onMaxDuration() {
                Log.i(TAG, "onMaxDuration:");
                isMaxDuration = true;
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mControlView != null) {
                            mControlView.setCompleteEnable(false);
                            mControlView.setRecordState(RecordState.STOP);
                            mControlView.updataCutDownView(false);
                        }
                    }
                });

            }

            @Override
            public void onError(int errorCode) {
                Log.e(TAG, "onError:" + errorCode);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempIsComplete = true;
                        if (progressBar != null && progressBar.isShowing()) {
                            progressBar.dismiss();
                            mControlView.setCompleteEnable(true);
                        } else {
                            handleStopCallback(false, 0);
                        }
                    }
                });
            }

            @Override
            public void onInitReady() {
                Log.i(TAG, "onInitReady");
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (effectPaster != null) {
                            addEffectToRecord(effectPaster.getPath());
                        }

                    }
                });
            }

            @Override
            public void onDrawReady() {

            }

            @Override
            public void onPictureBack(final Bitmap bitmap) {

            }

            @Override
            public void onPictureDataBack(final byte[] data) {

            }

        });

        recorder.setFaceTrackInternalMaxFaceCount(2);
    }


    public void setFaceTrackModePath() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                String path = StorageUtils.getCacheDirectory(getContext()).getAbsolutePath() + File.separator + Common.QU_NAME + File.separator;
                if (recorder != null) {
                    recorder.needFaceTrackInternal(true);
                    recorder.setFaceTrackInternalModelPath(path + "/model");
                }
            }
        });

    }

    public void setRecordMute(boolean recordMute) {
        if (recorder != null) {
            recorder.setMute(recordMute);
        }
    }

    private void initOrientationDetector() {
        orientationDetector = new OrientationDetector(getContext().getApplicationContext());
        orientationDetector.setOrientationChangedListener(new OrientationDetector.OrientationChangedListener() {
            @Override
            public void onOrientationChanged() {
                rotation = getCameraRotation();
                recorder.setRotation(rotation);
            }
        });
    }

    private int getCameraRotation() {
        int orientation = orientationDetector.getOrientation();
        int rotation = 90;
        if ((orientation >= 45) && (orientation < 135)) {
            rotation = 180;
        }
        if ((orientation >= 135) && (orientation < 225)) {
            rotation = 270;
        }
        if ((orientation >= 225) && (orientation < 315)) {
            rotation = 0;
        }

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraType.getType(), cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            if (rotation != 0) {
                rotation = 360 - rotation;
            }
        }
        return rotation;
    }


    private boolean activityStoped;
    private Runnable pendingCompseFinishRunnable;

    /**
     * 开始预览
     */
    public void startPreview() {
        activityStoped = false;
        if (pendingCompseFinishRunnable != null) {
            pendingCompseFinishRunnable.run();
        }
        pendingCompseFinishRunnable = null;
        if (recorder != null) {
            recorder.startPreview();
//            if (isAllowChangeMv) {
//                restoreConflictEffect();
//            }
            //            recorder.setZoom(scaleFactor);
            if (clipManager.getDuration() >= clipManager.getMinDuration() || isMaxDuration) {
                // 2018/7/11 让下一步按钮可点击
                mControlView.setCompleteEnable(true);
            } else {
                mControlView.setCompleteEnable(false);
            }
        }
        if (orientationDetector != null && orientationDetector.canDetectOrientation()) {
            orientationDetector.enable();
        }

        mCountDownView.setOnCountDownFinishListener(new AlivcCountDownView.OnCountDownFinishListener() {
            @Override
            public void onFinish() {
                FixedToastUtils.show(getContext(), getResources().getString(R.string.alivc_recorder_record_start_recorder));
                startRecord();
            }
        });

    }

    /**
     * 结束预览
     */
    public void stopPreview() {
        activityStoped = true;
        if (mControlView != null && mCountDownView != null && mControlView.getRecordState().equals(RecordState.READY)) {
            mCountDownView.cancle();
            mControlView.setRecordState(RecordState.STOP);
            mControlView.setRecording(false);
        }

        if (mControlView != null && mControlView.getRecordState().equals(RecordState.RECORDING)) {
            recorder.stopRecording();
        }
        recorder.stopPreview();

        if (orientationDetector != null) {
            orientationDetector.disable();
        }

        if (mControlView != null && mControlView.getFlashType() == FlashType.ON
                && mControlView.getCameraType() == CameraType.BACK) {
            mControlView.setFlashType(FlashType.OFF);
        }
    }

    /**
     * 销毁录制，在activity或者fragment被销毁时调用此方法
     */
    public void destroyRecorder() {

        //destroy时删除多段录制的片段文件
        deleteSliceFile();
        if (finishRecodingTask != null) {
            finishRecodingTask.cancel(true);
            finishRecodingTask = null;
        }

        if (faceTrackPathTask != null) {
            faceTrackPathTask.cancel(true);
            faceTrackPathTask = null;
        }

        if (recorder != null) {
            recorder.release();
            recorder = null;
            Log.i(TAG, "recorder destroy");
        }

        if (orientationDetector != null) {
            orientationDetector.setOrientationChangedListener(null);
        }

    }

    /**
     * 删除多段录制临时文件
     */
    private void deleteSliceFile() {
        if (recorder != null) {
            recorder.getClipManager().deleteAllPart();
        }
    }

    /**
     * 结束录制，并且将录制片段视频拼接成一个视频 跳转editorActivity在合成完成的回调的方法中，{@link AlivcSvideoRecordActivity#()}
     */
    private void finishRecording() {
        //弹窗提示
        if (progressBar == null) {
            progressBar = new ProgressDialog(getContext());
            progressBar.setMessage(getResources().getString(R.string.alivc_recorder_record_create_video));
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setCancelable(false);
            progressBar.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        }
        progressBar.show();
        mControlView.setCompleteEnable(false);
        finishRecodingTask = new FinishRecodingTask(this).executeOnExecutor(
            AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setMediaInfo() {
        // mMixInputInfo只对合拍有效，普通录制情况下，该参数将被忽略
        AliyunMixRecorderDisplayParam recorderDisplayParam = new AliyunMixRecorderDisplayParam.Builder()
        .displayMode(VideoDisplayMode.SCALE)
        .layoutParam(
            new AliyunMixTrackLayoutParam.Builder()
            .centerX(0.25f)
            .centerY(0.5f)
            .widthRatio(0.5f)
            .heightRatio(1.0f)
            .build()
        )
        .build();
        AliyunMixRecorderDisplayParam sampleDisplayParam = new AliyunMixRecorderDisplayParam
        .Builder()
        .displayMode(VideoDisplayMode.FILL)
        .layoutParam(new AliyunMixTrackLayoutParam.Builder()
                     .centerX(0.75f)
                     .centerY(0.5f)
                     .widthRatio(0.5f)
                     .heightRatio(1.0f)
                     .build())
        .build();
        mMixInputInfo = new AliyunMixMediaInfoParam
        .Builder()
        .streamStartTimeMills(0L)
        .streamEndTimeMills(0L)
        .mixVideoFilePath(mMixVideoPath)
        .mixDisplayParam(sampleDisplayParam)
        .recordDisplayParam(recorderDisplayParam)
        .build();


        mOutputInfo = new MediaInfo();
        mOutputInfo.setFps(35);
        mOutputInfo.setVideoWidth(recorder.getVideoWidth());
        mOutputInfo.setVideoHeight(recorder.getVideoHeight());
        mOutputInfo.setVideoCodec(mVideoCodec);
        recorder.setMediaInfo(mMixInputInfo, mOutputInfo);
    }

    /**
     * 录制结束的AsyncTask
     */
    public static class FinishRecodingTask extends AsyncTask<Void, Integer, Integer> {
        WeakReference<AliyunSVideoRecordView> weakReference;

        FinishRecodingTask(AliyunSVideoRecordView recordView) {
            weakReference = new WeakReference<>(recordView);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            if (weakReference == null) {
                return -1;
            }

            AliyunSVideoRecordView recordView = weakReference.get();
            if (recordView != null) {
                Log.i(TAG, "finishRecording");
                return recordView.recorder.finishRecording();

            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer code) {
            if (weakReference == null) {
                return;
            }
            if (code != AliyunErrorCode.ALIVC_COMMON_RETURN_SUCCESS) {
                Log.e(TAG, "合成失败 错误码 : " + code);
                AliyunSVideoRecordView recordView = weakReference.get();
                if (recordView != null) {
//                    ToastUtils.show(recordView.getContext(), R.string.alivc_editor_publish_compose_failed);
                    if (recordView.progressBar != null && recordView.progressBar.isShowing()) {
                        recordView.progressBar.dismiss();
                        recordView.mControlView.setCompleteEnable(true);
                    }
                }
            }
        }
    }

    /**
     * 片段录制完成的回调处理
     *
     * @param isValid
     * @param duration
     */
    private void handleStopCallback(final boolean isValid, final long duration) {


        mControlView.setRecordState(RecordState.STOP);
        if (mControlView != null) {
            mControlView.setRecording(false);
        }

        if (!isValid) {
            if (mRecordTimeView != null) {
                mRecordTimeView.setDuration(0);

                if (mRecordTimeView.getTimelineDuration() == 0) {
                    mControlView.setHasRecordPiece(false);
                }
            }
            return;
        }

        if (mRecordTimeView != null) {
            mRecordTimeView.setDuration((int) duration);
            mRecordTimeView.clipComplete();
            mControlView.setHasRecordPiece(true);
            isAllowChangeMv = false;
        }
    }

    /**
     * addSubView 添加子view到布局中
     *
     * @param view 子view
     */
    private void addSubView(View view) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(view, params);//添加到布局中
    }

    /**
     * 录制界面返回按钮click listener
     */
    private OnBackClickListener mBackClickListener;

    public void setBackClickListener(OnBackClickListener listener) {
        this.mBackClickListener = listener;
    }

    private OnFinishListener mCompleteListener;

    /**
     * 删除所有录制文件
     */
    public void deleteAllPart() {
        if (clipManager != null) {
            clipManager.deleteAllPart();
            if (clipManager.getDuration() < clipManager.getMinDuration() && mControlView != null) {
                mControlView.setCompleteEnable(false);
            }
            if (clipManager.getDuration() == 0) {
                // 音乐可以选择
                //                    musicBtn.setVisibility(View.VISIBLE);
                //                    magicMusic.setVisibility(View.VISIBLE);
                //recorder.restartMv();
                mRecordTimeView.clear();
                mControlView.setHasRecordPiece(false);
                isAllowChangeMv = true;
            }
        }
    }

    private float lastScaleFactor;
    private float scaleFactor;

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float factorOffset = detector.getScaleFactor() - lastScaleFactor;
        scaleFactor += factorOffset;
        lastScaleFactor = detector.getScaleFactor();
        if (scaleFactor < 0) {
            scaleFactor = 0;
        }
        if (scaleFactor > 1) {
            scaleFactor = 1;
        }
        recorder.setZoom(scaleFactor);
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        lastScaleFactor = detector.getScaleFactor();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    /**
     * 返回按钮事件监听
     */
    public interface OnBackClickListener {
        void onClick();
    }

    /**
     * 录制完成事件监听
     */
    public interface OnFinishListener {
        void onComplete(String path, int duration, int ratioMode);
    }

    public void setCompleteListener(OnFinishListener mCompleteListener) {
        this.mCompleteListener = mCompleteListener;
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * 获取最大录制时长
     *
     * @return
     */
    public int getMaxRecordTime() {
        if (maxRecordTime < MIN_RECORD_TIME) {
            return MIN_RECORD_TIME;
        } else if (maxRecordTime > MAX_RECORD_TIME) {
            return MAX_RECORD_TIME;
        } else {

            return maxRecordTime;
        }

    }

    /**
     * 设置录制时长
     *
     * @param maxRecordTime
     */
    public void setMaxRecordTime(int maxRecordTime) {
        this.maxRecordTime = maxRecordTime;
    }

    /**
     * 设置最小录制时长
     *
     * @param minRecordTime
     */
    public void setMinRecordTime(int minRecordTime) {
        this.minRecordTime = minRecordTime;
    }

    /**
     * 设置Gop
     *
     * @param mGop
     */
    public void setGop(int mGop) {
        this.mGop = mGop;

    }

    /**
     * 设置视频质量
     *
     * @param mVideoQuality
     */
    public void setVideoQuality(VideoQuality mVideoQuality) {
        this.mVideoQuality = mVideoQuality;
    }

    /**
     * 重新绘制SurFaceView的比例
     */
    public void setReSizeRatioMode(int ratioMode) {
        this.mRatioMode = ratioMode;
        recorder.setRatioMode(ratioMode);
        mRecorderSurfaceView.setLayoutParams(recorder.getLayoutParams());
    }

    /**
     * 设置视频比例
     *
     * @param ratioMode
     */
    public void setRatioMode(int ratioMode) {
        this.mRatioMode = ratioMode;
    }

    /**
     * 设置播放视频的路径
     */
    public void setVideoPath(String path) {
        this.mMixVideoPath = path;
    }

    /**
     * 设置视频编码方式
     *
     * @param mVideoCodec
     */
    public void setVideoCodec(VideoCodecs mVideoCodec) {
        this.mVideoCodec = mVideoCodec;
    }

    /**
     * 设置视频码率
     *
     * @param mResolutionMode
     */
    public void setResolutionMode(int mResolutionMode) {
        this.mResolutionMode = mResolutionMode;
    }


}