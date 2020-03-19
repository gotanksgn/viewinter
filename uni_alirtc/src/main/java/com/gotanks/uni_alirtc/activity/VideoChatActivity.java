package com.gotanks.uni_alirtc.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.rtc.AliRtcAuthInfo;
import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcEngineEventListener;
import com.alivc.rtc.AliRtcEngineNotify;
import com.alivc.rtc.AliRtcRemoteUserInfo;
import com.gotanks.uni_alirtc.R;
import com.gotanks.uni_alirtc.base.BaseActivity;
import com.gotanks.uni_alirtc.bean.ChartUserBean;
import com.gotanks.uni_alirtc.bean.RTCAuthInfo;
import com.gotanks.uni_alirtc.core.TimeUtils;
import com.gotanks.uni_alirtc.service.ForegroundService;
import com.gotanks.uni_alirtc.utils.AliRtcConstants;
import com.gotanks.uni_alirtc.utils.AppUtils;
import com.gotanks.uni_alirtc.utils.DensityUtils;

import org.webrtc.ali.ThreadUtils;
import org.webrtc.alirtcInterface.ALI_RTC_INTERFACE;
import org.webrtc.alirtcInterface.AliParticipantInfo;
import org.webrtc.alirtcInterface.AliStatusInfo;
import org.webrtc.alirtcInterface.AliSubscriberInfo;
import org.webrtc.sdk.SophonSurfaceView;

import java.util.ArrayList;
import java.util.List;

import static com.alivc.rtc.AliRtcEngine.AliRtcAudioTrack.AliRtcAudioTrackNo;
import static com.alivc.rtc.AliRtcEngine.AliRtcRenderMode.AliRtcRenderModeAuto;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackBoth;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackNo;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackScreen;
import static com.gotanks.uni_alirtc.utils.AliRtcConstants.SOPHON_RESULT_SIGNAL_HEARTBEAT_TIMEOUT;
import static com.gotanks.uni_alirtc.utils.AliRtcConstants.SOPHON_SERVER_ERROR_POLLING;

/**
 * 音视频通话的activity
 */
public class VideoChatActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = VideoChatActivity.class.getName();


    /**
     * 用户名
     */
    String mUsername;
    /**
     * 频道名
     */
    String mChannel;
    /**
     * rtcAuthInfo，本地用户加入房间的时候返回的json
     */
    RTCAuthInfo mRtcAuthInfo;
    /**
     * SDK提供的对音视频通话处理的引擎类
     */
    private AliRtcEngine mAliRtcEngine;
    /**
     * 前台服务的Intent
     */
    private Intent mForeServiceIntent;
    /**
     * 权限判断
     */
    private boolean mGrantPermission;
    /**
     * 数据集
     */
    private Bundle mBundle;
    private boolean mIsAudioCapture;
    private boolean mIsAudioPlay;
    private SophonSurfaceView vLocalSurfaceView;
    private FrameLayout vLytBigVideoContainer;
    //    private FrameLayout vLytSmallVideoContainer;
    private TextView vTvUserName;
    private TextView vTvJobName;
    private TextView vTvJobDesc;
    private TextView vTvProgressTip;
    private TextView vTvToggleMicro;
    private TextView vTvCancel;
    private TextView vTvToggleCamera;
    private LinearLayout vLytController1;
    private TextView vTvTimeTip;
    private RelativeLayout chartParent;

    private List<ChartUserBean> chartUserBeans = new ArrayList<>();

    public static void launchForResult(Activity activity, Bundle videoChatArgs) {
        Intent intent = new Intent(activity, VideoChatActivity.class);
        intent.putExtras(videoChatArgs);
        activity.startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);
        // 请求权限
        requestPermission();
        // 获取上个页面附带的参数
        getIntentData();
        // 初始化界面上的view
        initView();
        // 初始化引擎以及打开预览界面
        initRTCEngineAndStartPreview();
        // 打开加入房间前需要的参数
        openJoinChannelBeforeNeedParams();

        autoJoinChannel();
    }

    private void autoJoinChannel() {
        if (mGrantPermission) {
            joinChannel();
        } else {
            setUpSplash();
        }
    }

    private void getIntentData() {
        mBundle = getIntent().getExtras();
        if (null != mBundle) {
            //用户名
            mUsername = mBundle.getString("username");
            //频道
            mChannel = mBundle.getString("channel");
            //音频采集
            mIsAudioCapture = mBundle.getBoolean("audioCapture");
            //音频播放
            mIsAudioPlay = mBundle.getBoolean("audioPlay");
            //rtcAuthInfo
            mRtcAuthInfo = (RTCAuthInfo) mBundle.getSerializable("rtcAuthInfo");
        }
    }

    private void initView() {

        vLocalSurfaceView = findViewById(R.id.vLocalSurfaceView);
        vLytBigVideoContainer = findViewById(R.id.vLytBigVideoContainer);
//        vLytSmallVideoContainer = findViewById(R.id.vLytSmallVideoContainer);
        vTvUserName = findViewById(R.id.vTvUserName);
        vTvJobName = findViewById(R.id.vTvJobName);
        vTvJobDesc = findViewById(R.id.vTvJobDesc);
        vTvProgressTip = findViewById(R.id.vTvProgressTip);
        vTvToggleMicro = findViewById(R.id.vTvToggleMicro);
        vTvCancel = findViewById(R.id.vTvCancel);
        vTvToggleCamera = findViewById(R.id.vTvToggleCamera);
        vLytController1 = findViewById(R.id.vLytController1);
        vTvTimeTip = findViewById(R.id.vTvTimeTip);
        chartParent = findViewById(R.id.chart_parent);
        if (AliRtcConstants.BRAND_OPPO.equalsIgnoreCase(Build.BRAND) && AliRtcConstants.MODEL_OPPO_R17.equalsIgnoreCase(
                Build.MODEL)) {
            chartParent.setPadding(0, DensityUtils.dip2px(this, 20), 0, 0);
        }

        vTvCancel.setOnClickListener(this);
        vTvToggleMicro.setOnClickListener(this);
        vTvToggleCamera.setOnClickListener(this);

//        vLytSmallVideoContainer.setOnClickListener(this);
    }

    private void initRTCEngineAndStartPreview() {
        if (this.checkPermission(Manifest.permission.CAMERA) || checkPermission(
                Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
            Toast.makeText(this.getApplicationContext(), "需要开启权限才可进行观看", Toast.LENGTH_SHORT).show();
            mGrantPermission = false;
            return;
        }
        mGrantPermission = true;
        // 防止初始化过多
        if (mAliRtcEngine == null) {
            //实例化,必须在主线程进行。
            mAliRtcEngine = AliRtcEngine.getInstance(getApplicationContext());
            //设置事件的回调监听
            mAliRtcEngine.setRtcEngineEventListener(mEventListener);
            //设置接受通知事件的回调
            mAliRtcEngine.setRtcEngineNotify(mEngineNotify);
            // 初始化本地视图
            initLocalView();
            //开启预览
            startPreview();
        }
    }

    private void openJoinChannelBeforeNeedParams() {
        //开启音频采集
        if (mIsAudioCapture) {
            mAliRtcEngine.startAudioCapture();
        } else {
            mAliRtcEngine.stopAudioCapture();
        }
        //开启音频播放
        if (mIsAudioPlay) {
            mAliRtcEngine.startAudioPlayer();
        } else {
            mAliRtcEngine.stopAudioPlayer();
        }
    }

    private boolean checkPermission(String permission) {
        try {
            int i = ActivityCompat.checkSelfPermission(this, permission);
            if (i != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        } catch (RuntimeException e) {
            return true;
        }
        return false;
    }

    private void startPreview() {
        if (mAliRtcEngine == null) {
            return;
        }
        try {
            mAliRtcEngine.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置本地的预览视图的view
     */
    private void initLocalView() {
        vLocalSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        vLocalSurfaceView.setZOrderOnTop(true);
        vLocalSurfaceView.setZOrderMediaOverlay(true);
        AliRtcEngine.AliVideoCanvas aliVideoCanvas = new AliRtcEngine.AliVideoCanvas();
        aliVideoCanvas.view = vLocalSurfaceView;
        aliVideoCanvas.renderMode = AliRtcRenderModeAuto;
        if (mAliRtcEngine != null) {
            mAliRtcEngine.setLocalViewConfig(aliVideoCanvas, AliRtcVideoTrackCamera);
        }
    }

    private void joinChannel() {
        if (mAliRtcEngine == null) {
            return;
        }
        AliRtcAuthInfo userInfo = new AliRtcAuthInfo();
        userInfo.setAppid(mRtcAuthInfo.data.appid);
        userInfo.setNonce(mRtcAuthInfo.data.nonce);
        userInfo.setTimestamp(mRtcAuthInfo.data.timestamp);
        userInfo.setUserId(mRtcAuthInfo.data.userid);
        userInfo.setGslb(mRtcAuthInfo.data.gslb);
        userInfo.setToken(mRtcAuthInfo.data.token);
        userInfo.setConferenceId(mChannel);
        /*
         *设置自动发布和订阅，只能在joinChannel之前设置
         *参数1    true表示自动发布；false表示手动发布
         *参数2    true表示自动订阅；false表示手动订阅
         */
        mAliRtcEngine.setAutoPublish(true, true);
        // 加入频道
        mAliRtcEngine.joinChannel(userInfo, mUsername);

    }


    private void updateRemoteDisplay(String uid, AliRtcEngine.AliRtcAudioTrack at, AliRtcEngine.AliRtcVideoTrack vt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == mAliRtcEngine) {
                    return;
                }
                AliRtcRemoteUserInfo remoteUserInfo = mAliRtcEngine.getUserInfo(uid);
                // 如果没有，说明已经退出了或者不存在。则不需要添加，并且删除
                if (remoteUserInfo == null) {
                    // remote user exit room
                    Log.e(TAG, "updateRemoteDisplay remoteUserInfo = null, uid = " + uid);
                    return;
                }
                //change
                AliRtcEngine.AliVideoCanvas cameraCanvas = remoteUserInfo.getCameraCanvas();
                AliRtcEngine.AliVideoCanvas screenCanvas = remoteUserInfo.getScreenCanvas();
                //视频情况
                if (vt == AliRtcVideoTrackNo) {
                    //没有视频流
                    cameraCanvas = null;
                    screenCanvas = null;
                } else if (vt == AliRtcVideoTrackCamera) {
                    //相机流
                    screenCanvas = null;
                    cameraCanvas = createCanvasIfNull(cameraCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(cameraCanvas, uid, AliRtcVideoTrackCamera);
                } else if (vt == AliRtcVideoTrackScreen) {
                    //屏幕流
                    cameraCanvas = null;
                    screenCanvas = createCanvasIfNull(screenCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(screenCanvas, uid, AliRtcVideoTrackScreen);
                } else if (vt == AliRtcVideoTrackBoth) {
                    //多流
                    cameraCanvas = createCanvasIfNull(cameraCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(cameraCanvas, uid, AliRtcVideoTrackCamera);
                    screenCanvas = createCanvasIfNull(screenCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(screenCanvas, uid, AliRtcVideoTrackScreen);
                } else {
                    return;
                }


                String uid = remoteUserInfo.getUserID();
                ChartUserBean ret = addRemoteUserIfNull(uid, remoteUserInfo);
                ret.mCameraSurface = cameraCanvas != null ? cameraCanvas.view : null;
                ret.mIsCameraFlip = cameraCanvas != null && cameraCanvas.mirrorMode == AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled;
                ret.mScreenSurface = screenCanvas != null ? screenCanvas.view : null;
                ret.mIsScreenFlip = screenCanvas != null && screenCanvas.mirrorMode == AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled;

                updateChatLayout();
            }
        });
    }


    private AliRtcEngine.AliVideoCanvas createCanvasIfNull(AliRtcEngine.AliVideoCanvas canvas) {
        if (canvas == null || canvas.view == null) {
            //创建canvas，Canvas为SophonSurfaceView或者它的子类
            canvas = new AliRtcEngine.AliVideoCanvas();
            SophonSurfaceView surfaceView = new SophonSurfaceView(this);
            surfaceView.setZOrderOnTop(false);
            surfaceView.setZOrderMediaOverlay(false);
            canvas.view = surfaceView;
            //renderMode提供四种模式：Auto、Stretch、Fill、Crop，建议使用Auto模式。
            canvas.renderMode = AliRtcRenderModeAuto;
        }
        return canvas;
    }

    private ChartUserBean convertRemoteUserInfo(AliRtcRemoteUserInfo remoteUserInfo,
                                                AliRtcEngine.AliVideoCanvas cameraCanvas,
                                                AliRtcEngine.AliVideoCanvas screenCanvas) {
        String uid = remoteUserInfo.getUserID();
        ChartUserBean ret = addRemoteUserIfNull(uid, remoteUserInfo);
        ret.mCameraSurface = cameraCanvas != null ? cameraCanvas.view : null;
        ret.mIsCameraFlip = cameraCanvas != null && cameraCanvas.mirrorMode == AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled;
        ret.mScreenSurface = screenCanvas != null ? screenCanvas.view : null;
        ret.mIsScreenFlip = screenCanvas != null && screenCanvas.mirrorMode == AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled;
        return ret;
    }

    private ChartUserBean addRemoteUserIfNull(String uid, AliRtcRemoteUserInfo remoteUserInfo) {
        ChartUserBean ret = null;
        for (ChartUserBean chartUserBean : chartUserBeans) {
            if (uid.equals(chartUserBean.mUserId)) {
                ret = chartUserBean;
                break;
            }
        }
        if (ret == null) {
            ret = new ChartUserBean();
            chartUserBeans.add(ret);
        }

        ret.mUserId = remoteUserInfo.getUserID();
        ret.mUserName = remoteUserInfo.getDisplayName();

        return ret;
    }

    private ChartUserBean removeRemoteUser(String uid) {
        ChartUserBean ret = null;
        for (ChartUserBean chartUserBean : chartUserBeans) {
            if (uid.equals(chartUserBean.mUserId)) {
                ret = chartUserBean;
                break;
            }
        }
        if (ret != null) {
            chartUserBeans.remove(ret);
        }
        return ret;
    }

    /**
     * 特殊错误码回调的处理方法
     *
     * @param error 错误码
     */
    private void processOccurError(int error) {
        switch (error) {
            case SOPHON_SERVER_ERROR_POLLING:
            case SOPHON_RESULT_SIGNAL_HEARTBEAT_TIMEOUT:
                noSessionExit(error);
                break;
            default:
                break;
        }
    }

    /**
     * 错误处理
     *
     * @param error 错误码
     */
    private void noSessionExit(int error) {
        runOnUiThread(() -> new AlertDialog.Builder(VideoChatActivity.this)
                .setTitle("ErrorCode : " + error)
                .setMessage("网络超时，请退出房间")
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    onBackPressed();
                })
                .create()
                .show());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //销毁服务
        if (null != mForeServiceIntent && AppUtils.isServiceRunning(this.getApplicationContext(),
                ForegroundService.class.getName())) {
            stopService(mForeServiceIntent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.vTvCancel) {
            //销毁服务
            if (null != mForeServiceIntent && AppUtils.isServiceRunning(this.getApplicationContext(),
                    ForegroundService.class.getName())) {
                stopService(mForeServiceIntent);
            }
            //离会
            if (mAliRtcEngine != null) {
                mAliRtcEngine.setRtcEngineNotify(null);
                mAliRtcEngine.setRtcEngineEventListener(null);
                mAliRtcEngine.stopPreview();
                mAliRtcEngine.leaveChannel();
            }
            finish();
        } else if (view.getId() == R.id.vTvToggleMicro) {
            if (mAliRtcEngine != null) {
                vTvToggleMicro.setSelected(!vTvToggleMicro.isSelected());
                if (vTvToggleMicro.isSelected()) {
                    mAliRtcEngine.setRecordingVolume(0);
                } else {
                    mAliRtcEngine.setRecordingVolume(100);
                }
            }

        } else if (view.getId() == R.id.vTvToggleCamera) {
            if (mAliRtcEngine != null) {
                //默认是前置
                vTvToggleCamera.setSelected(!vTvToggleCamera.isSelected());
                mAliRtcEngine.switchCamera();
            }
        }
    }

    private void switchVideoLayout() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (mAliRtcEngine != null) {
            mAliRtcEngine.destroy();
        }
    }


    /**
     * 用户操作回调监听(回调接口都在子线程)
     */
    private AliRtcEngineEventListener mEventListener = new AliRtcEngineEventListener() {

        /**
         * 加入房间的回调
         * @param i 结果码
         */
        @Override
        public void onJoinChannelResult(int i) {
            runOnUiThread(() -> {
                if (i == 0) {
                    //开启前台服务
                    if (null == mForeServiceIntent) {
                        mForeServiceIntent = new Intent(VideoChatActivity.this.getApplicationContext(),
                                ForegroundService.class);
                        mForeServiceIntent.putExtras(mBundle);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(mForeServiceIntent);
                    } else {
                        startService(mForeServiceIntent);
                    }
                }
            });
        }

        /**
         * 离开房间的回调
         * @param i 结果码
         */
        @Override
        public void onLeaveChannelResult(int i) {

        }

        /**
         * 推流的回调
         * @param i 结果码
         * @param s publishId
         */
        @Override
        public void onPublishResult(int i, String s) {

        }

        /**
         * 取消发布本地流回调
         * @param i 结果码
         */
        @Override
        public void onUnpublishResult(int i) {

        }

        /**
         * 订阅成功的回调
         * @param s userid
         * @param i 结果码
         * @param aliRtcVideoTrack 视频的track
         * @param aliRtcAudioTrack 音频的track
         */
        @Override
        public void onSubscribeResult(String s, int i, AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack,
                                      AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack) {
            if (i == 0) {
                updateRemoteDisplay(s, aliRtcAudioTrack, aliRtcVideoTrack);
            }
        }

        /**
         * 取消的回调
         * @param i 结果码
         * @param s userid
         */
        @Override
        public void onUnsubscribeResult(int i, String s) {
            updateRemoteDisplay(s, AliRtcAudioTrackNo, AliRtcVideoTrackNo);
        }

        /**
         * 网络状态变化的回调
         * @param aliRtcNetworkQuality
         */
        @Override
        public void onNetworkQualityChanged(String s, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality1) {

        }

        /**
         * 出现警告的回调
         * @param i
         */
        @Override
        public void onOccurWarning(int i) {

        }

        /**
         * 出现错误的回调
         * @param error 错误码
         */
        @Override
        public void onOccurError(int error) {
            //错误处理
            processOccurError(error);
        }

        @Override
        public void onPerformanceLow() {

        }

        @Override
        public void onPermormanceRecovery() {

        }

        /**
         * 连接丢失
         */
        @Override
        public void onConnectionLost() {

        }

        /**
         * 尝试恢复连接
         */
        @Override
        public void onTryToReconnect() {

        }

        /**
         * 连接已恢复
         */
        @Override
        public void onConnectionRecovery() {

        }

        /**
         * @param aliRTCSDK_client_role
         * @param aliRTCSDK_client_role1
         * 用户角色更新
         */
        @Override
        public void onUpdateRoleNotify(ALI_RTC_INTERFACE.AliRTCSDK_Client_Role aliRTCSDK_client_role, ALI_RTC_INTERFACE.AliRTCSDK_Client_Role aliRTCSDK_client_role1) {

        }
    };

    /**
     * SDK事件通知(回调接口都在子线程)
     */
    private AliRtcEngineNotify mEngineNotify = new AliRtcEngineNotify() {
        /**
         * 远端用户停止发布通知，处于OB（observer）状态
         * @param aliRtcEngine 核心引擎对象
         * @param s userid
         */
        @Override
        public void onRemoteUserUnPublish(AliRtcEngine aliRtcEngine, String s) {
            updateRemoteDisplay(s, AliRtcAudioTrackNo, AliRtcVideoTrackNo);
        }

        /**
         * 远端用户上线通知
         * @param uid
         */
        @Override
        public void onRemoteUserOnLineNotify(String uid) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //判断用户是否在线
                    if (null == mAliRtcEngine) {
                        return;
                    }
                    AliRtcRemoteUserInfo remoteUserInfo = mAliRtcEngine.getUserInfo(uid);
                    if (remoteUserInfo != null) {
                        String uid = remoteUserInfo.getUserID();
                        ChartUserBean chartUserBean = addRemoteUserIfNull(uid, remoteUserInfo);
                        chartUserBean.mIsCameraFlip = false;
                        chartUserBean.mIsScreenFlip = false;
                        showToast("对方已加入");
                        hideTip();
                        updateChatLayout();
                        startTimer();
                    }
                }
            });
        }

        /**
         * 远端用户下线通知
         * @param uid
         */
        @Override
        public void onRemoteUserOffLineNotify(String uid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ChartUserBean chartUserBean = removeRemoteUser(uid);
                    if (chartUserBean != null) {
                        showToast("对方已离开");
                        showTip("对方已离开");
                        updateChatLayout();
                    }
                }
            });
        }

        /**
         * 远端用户发布音视频流变化通知
         * @param s userid
         * @param aliRtcAudioTrack 音频流
         * @param aliRtcVideoTrack 相机流
         */
        @Override
        public void onRemoteTrackAvailableNotify(String s, AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack,
                                                 AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
            updateRemoteDisplay(s, aliRtcAudioTrack, aliRtcVideoTrack);
        }

        /**
         * 订阅流回调，可以做UI及数据的更新
         * @param s userid
         * @param aliRtcAudioTrack 音频流
         * @param aliRtcVideoTrack 相机流
         */
        @Override
        public void onSubscribeChangedNotify(String s, AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack,
                                             AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {

        }

        /**
         * 订阅信息
         * @param aliSubscriberInfos 订阅自己这边流的user信息
         * @param i 当前订阅人数
         */
        @Override
        public void onParticipantSubscribeNotify(AliSubscriberInfo[] aliSubscriberInfos, int i) {

        }

        /**
         * 首帧的接收回调
         * @param s callId
         * @param s1 stream_label
         * @param s2 track_label 分为video和audio
         * @param i 时间
         */
        @Override
        public void onFirstFramereceived(String s, String s1, String s2, int i) {

            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "首帧接受成功");

                }
            });


        }

        /**
         * 首包的发送回调
         * @param s callId
         * @param s1 stream_label
         * @param s2 track_label 分为video和audio
         * @param i 时间
         */
        @Override
        public void onFirstPacketSent(String s, String s1, String s2, int i) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "首包发送成功");
                }
            });
        }

        /**
         *首包数据接收成功
         * @param callId 远端用户callId
         * @param streamLabel 远端用户的流标识
         * @param trackLabel 远端用户的媒体标识
         * @param timeCost 耗时
         */
        @Override
        public void onFirstPacketReceived(String callId, String streamLabel, String trackLabel, int timeCost) {

        }

        /**
         * 取消订阅信息回调
         * @param aliParticipantInfos 订阅自己这边流的user信息
         * @param i 当前订阅人数
         */
        @Override
        public void onParticipantUnsubscribeNotify(AliParticipantInfo[] aliParticipantInfos, int i) {

        }

        /**
         * 被服务器踢出或者频道关闭时回调
         * @param i
         */
        @Override
        public void onBye(int i) {

        }

        @Override
        public void onParticipantStatusNotify(AliStatusInfo[] aliStatusInfos, int i) {

        }

        @Override
        public void onAliRtcStats(ALI_RTC_INTERFACE.AliRtcStats aliRtcStats) {

        }

    };

    private int useSec;
    private Handler handler = new Handler(Looper.getMainLooper());

    private void startTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vTvTimeTip.setText(TimeUtils.formatUse(++useSec));
                handler.postDelayed(this, 1000);

            }
        }, 1000);
    }

    private void showTip(String msg) {
        vTvProgressTip.setText(msg);
        vTvProgressTip.setVisibility(View.VISIBLE);
    }

    private void hideTip() {
        vTvProgressTip.setVisibility(View.GONE);
    }

    private void updateChatLayout() {
        Log.d(TAG, "updateChatLayout: " + chartUserBeans.size());
        if (chartUserBeans.size() > 0) {
            //只处理第0个
            ChartUserBean chartUserBean = chartUserBeans.get(0);
            if (chartUserBean.mCameraSurface != null) {
                if (chartUserBean.mCameraSurface.getParent() == null) {
                    vLytBigVideoContainer.removeAllViews();
                    vLytBigVideoContainer.addView(chartUserBean.mCameraSurface, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
            } else {
                vLytBigVideoContainer.removeAllViews();
            }
        } else {
            vLytBigVideoContainer.removeAllViews();
        }
    }


}