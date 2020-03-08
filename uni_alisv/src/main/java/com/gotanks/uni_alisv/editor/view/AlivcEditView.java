package com.gotanks.uni_alisv.editor.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.utils.StorageUtils;
import com.aliyun.crop.AliyunCropCreator;
import com.aliyun.crop.supply.AliyunICrop;
import com.aliyun.editor.EditorCallBack;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.qupai.editor.AliyunIEditor;
import com.aliyun.qupai.editor.AliyunPasterController;
import com.aliyun.qupai.editor.AliyunPasterManager;
import com.aliyun.qupai.editor.OnAnimationFilterRestored;
import com.aliyun.qupai.editor.OnPasterRestored;
import com.aliyun.qupai.editor.impl.AliyunEditorFactory;
import com.aliyun.svideo.base.UIConfigManager;
import com.aliyun.svideo.common.utils.DensityUtils;
import com.aliyun.svideo.common.utils.FastClickUtil;
import com.aliyun.svideo.common.utils.PermissionUtils;
import com.aliyun.svideo.common.widget.AlivcCircleLoadingDialog;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoParam;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.effect.EffectBase;
import com.aliyun.svideo.sdk.external.struct.effect.EffectBean;
import com.aliyun.svideo.sdk.external.struct.effect.EffectFilter;
import com.aliyun.svideo.sdk.external.thumbnail.AliyunIThumbnailFetcher;
import com.aliyun.svideo.sdk.external.thumbnail.AliyunThumbnailFetcherFactory;
import com.gotanks.uni_alisv.R;
import com.gotanks.uni_alisv.editor.activity.EditorActivity;
import com.gotanks.uni_alisv.editor.bean.AlivcEditOutputParam;
import com.gotanks.uni_alisv.editor.editor.thumblinebar.OverlayThumbLineBar;
import com.gotanks.uni_alisv.editor.editor.thumblinebar.ThumbLineBar;
import com.gotanks.uni_alisv.editor.editor.thumblinebar.ThumbLineConfig;
import com.gotanks.uni_alisv.editor.effects.control.BaseChooser;
import com.gotanks.uni_alisv.editor.effects.control.EditorService;
import com.gotanks.uni_alisv.editor.effects.control.EffectInfo;
import com.gotanks.uni_alisv.editor.effects.control.OnEffectActionLister;
import com.gotanks.uni_alisv.editor.effects.control.OnEffectChangeListener;
import com.gotanks.uni_alisv.editor.effects.control.OnTabChangeListener;
import com.gotanks.uni_alisv.editor.effects.control.TabGroup;
import com.gotanks.uni_alisv.editor.effects.control.TabViewStackBinding;
import com.gotanks.uni_alisv.editor.effects.control.UIEditorPage;
import com.gotanks.uni_alisv.editor.effects.control.ViewStack;
import com.gotanks.uni_alisv.editor.effects.filter.AnimationFilterController;
import com.gotanks.uni_alisv.editor.msg.Dispatcher;
import com.gotanks.uni_alisv.editor.msg.body.FilterTabClick;
import com.gotanks.uni_alisv.editor.msg.body.LongClickAnimationFilter;
import com.gotanks.uni_alisv.editor.msg.body.LongClickUpAnimationFilter;
import com.gotanks.uni_alisv.editor.msg.body.SelectColorFilter;
import com.gotanks.uni_alisv.editor.util.AlivcSnapshot;
import com.gotanks.uni_alisv.editor.util.Common;
import com.gotanks.uni_alisv.editor.util.FixedToastUtils;
import com.gotanks.uni_alisv.editor.util.ThreadUtil;
import com.gotanks.uni_alisv.editor.viewoperate.ViewOperator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;

/**
 * @author zsy_18 data:2018/8/24
 */
public class AlivcEditView extends RelativeLayout
        implements View.OnClickListener, OnEffectChangeListener, OnTabChangeListener,
        OnAnimationFilterRestored {
    private static final String TAG = AlivcEditView.class.getName();

    /**
     * 编辑核心接口类
     */
    private AliyunIEditor mAliyunIEditor;
    /**
     * 动图管理接口类
     */
    private AliyunPasterManager mPasterManager;
    /**
     * 获取缩略图片接口
     */
    private AliyunIThumbnailFetcher mThumbnailFetcher;

    /**
     * 裁剪接口核心类，对于Gop比较大的视频做时间特效时需要先检查是否满足实时，如果不满足实时，需要提前做转码，逻辑如下
     */
    private AliyunICrop mTranscoder;


    private OverlayThumbLineBar mThumbLineBar;

    /**
     * 底部滑动item的横向ScrollView
     */
    private ViewGroup mBottomLinear;
    /**
     * 编辑需要渲染显示的SurfaceView
     */
    private SurfaceView mSurfaceView;
    /**
     * 底部菜单点击事件管理类
     */
    private TabGroup mTabGroup;
    /**
     * 处理底部菜单点击事件
     */
    private ViewStack mViewStack;
    /**
     * 主要用于记录各个功能view上次的状态，用于下次进入的时候进行恢复
     */
    private EditorService mEditorService;
    /**
     * 控件
     */
    private RelativeLayout mActionBar;
    private FrameLayout resCopy;
    private FrameLayout mTransCodeTip;
    private ProgressBar mTransCodeProgress;
    public FrameLayout mPasterContainer;
    private FrameLayout mGlSurfaceContainer;
    private ImageView mIvLeft;
    private TextView mTvRight;
    private LinearLayout mBarLinear;
    private TextView mPlayImage;
    private TextView mTvCurrTime;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 水印图片
     */
    private Bitmap mWatermarkBitmap;
    /**
     * 特效使用的控制类
     */
    private AnimationFilterController mAnimationFilterController;
    /**
     * 状态，使用倒放时间特效
     */
    private boolean mUseInvert = false;
    /**
     * 状态，正在添加滤镜特效那个中
     */
    private boolean mUseAnimationFilter = false;
    /**
     * 状态，判断是否可以继续添加时间特效，true不可以继续添加特效
     */
    private boolean mCanAddAnimation = true;
    /**
     * 状态，是否正在转码中
     */
    private boolean mIsTranscoding = false;
    /**
     * 状态，界面是否被销毁
     */
    private boolean mIsDestroyed = false;
    /**
     * 状态，与生命周期onStop有关
     */
    private boolean mIsStop = false;
    private boolean mWaitForReady = false;

    /**
     * 音量
     */
    private int mVolume = 50;
    /**
     * 控制UI变动
     */
    private ViewOperator mViewOperate;
    private Point mPasterContainerPoint;
    private EffectBean lastMusicBean;
    //用户滑动thumbLineBar时的监听器
    private ThumbLineBar.OnBarSeekListener mBarSeekListener;
    //播放时间、显示时间、缩略图位置同步接口
    private PlayerListener mPlayerListener;
    private ObjectAnimator animatorX;
    private Toast showToast;

    /**
     * 编辑模块Handler处理类
     */
    private AlivcEditHandler alivcEditHandler;
    /**
     * 线程池
     */
    private ExecutorService executorService;
    /**
     * 封面保存路径
     */
    private final String PATH_THUMBNAIL = Environment.getExternalStorageDirectory() + File.separator + "thumbnail.jpg";
    /**
     * 是否可以截图
     */
    private boolean isTakeFrame = false;
    /**
     * 是否确认选择截图
     */
    private boolean isTakeFrameSelected = false;
    /**
     * 是否已经截取封面
     */
    private boolean hasCaptureCover = false;
    /**
     * 截图工具，用于获取surface的画面
     */
    private AlivcSnapshot mSnapshop;
    /**
     * 是否使用默认水印
     */
    private boolean hasWaterMark;
    /**
     * 判断是否有音乐
     */
    private boolean mHasRecordMusic;

    /**
     * 是否合拍，合拍无法使用音乐
     */
    private boolean isMixRecord;

    private AlivcCircleLoadingDialog mLoadingDialog;


    public AlivcEditView(Context context) {
        this(context, null);
    }

    public AlivcEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlivcEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        Dispatcher.getInstance().register(this);

        Point point = new Point();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        mScreenWidth = point.x;
        LayoutInflater.from(getContext()).inflate(R.layout.alivc_editor_view_edit, this, true);
        initView();
        initListView();
        add2Control();
        initThreadHandler();
        if (PermissionUtils.checkPermissionsGroup(getContext(), PermissionUtils.PERMISSION_STORAGE)) {
            copyAssets();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        resCopy = (FrameLayout) findViewById(R.id.copy_res_tip);
        mTransCodeTip = (FrameLayout) findViewById(R.id.transcode_tip);
        mTransCodeProgress = (ProgressBar) findViewById(R.id.transcode_progress);
        mBarLinear = (LinearLayout) findViewById(R.id.bar_linear);
        mBarLinear.bringToFront();
        mActionBar = (RelativeLayout) findViewById(R.id.action_bar);
        mActionBar.setBackgroundDrawable(null);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mTvRight = findViewById(R.id.tv_right);
        mIvLeft.setImageResource(R.mipmap.aliyun_svideo_back);
        //uiConfig中的属性
        //UIConfigManager.setImageResourceConfig(mTvRight, R.attr.finishImage, R.mipmap.aliyun_svideo_complete_red);
        mIvLeft.setVisibility(View.VISIBLE);

        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
        mTvCurrTime = (TextView) findViewById(R.id.tv_curr_duration);

        mGlSurfaceContainer = (FrameLayout) findViewById(R.id.glsurface_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.play_view);
        mBottomLinear = findViewById(R.id.edit_bottom_tab);
        mPasterContainer = (FrameLayout) findViewById(R.id.pasterView);

        mPlayImage = findViewById(R.id.play_button);
        mPlayImage.setOnClickListener(this);
        switchPlayStateUI(false);

        final GestureDetector mGesture = new GestureDetector(getContext(), new MyOnGestureListener());
        View.OnTouchListener pasterTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGesture.onTouchEvent(event);
            }
        };

        mPasterContainer.setOnTouchListener(pasterTouchListener);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomEditorView();
            }
        });

    }


    public OverlayThumbLineBar getThumbLineBar() {
        return mThumbLineBar;
    }

    private void initGlSurfaceView() {
        if (mVideoParam == null) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mGlSurfaceContainer.getLayoutParams();
        FrameLayout.LayoutParams surfaceLayout = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
        int outputWidth = mVideoParam.getOutputWidth();
        int outputHeight = mVideoParam.getOutputHeight();

        float percent;
        if (outputWidth >= outputHeight) {
            percent = (float) outputWidth / outputHeight;
        } else {
            percent = (float) outputHeight / outputWidth;
        }
        /*
          指定surfaceView的宽高比是有必要的，这样可以避免某些非标分辨率下造成显示比例不对的问题
         */
        surfaceLayout.width = mScreenWidth;
        surfaceLayout.height = Math.round((float) outputHeight * mScreenWidth / outputWidth);
        mPasterContainerPoint = new Point(surfaceLayout.width, surfaceLayout.height);
        ViewGroup.MarginLayoutParams marginParams = null;
        if (layoutParams instanceof MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) surfaceLayout;
        } else {
            marginParams = new MarginLayoutParams(surfaceLayout);
        }
        if (percent < 1.5) {
            marginParams.setMargins(0,
                    getContext().getResources().getDimensionPixelSize(R.dimen.alivc_svideo_title_height), 0, 0);
        } else {
            if (outputWidth > outputHeight) {
                marginParams.setMargins(0,
                        getContext().getResources().getDimensionPixelSize(R.dimen.alivc_svideo_title_height) * 2, 0, 0);
                //} else {
                //    int screenWidth = ScreenUtils.getRealWidth(getContext());
                //    int screenHeight = ScreenUtils.getRealHeight(getContext());
                //    float screenRatio = screenWidth / (float)screenHeight;
                //    if (screenRatio <= 9 / 16f) {
                //        //长手机，宽高比小于9/16
                //        marginParams.height = screenHeight;
                //        marginParams.width = screenHeight / 16 * 9;
                //    }
            }
        }
        mGlSurfaceContainer.setLayoutParams(layoutParams);
        mPasterContainer.setLayoutParams(marginParams);
        mSurfaceView.setLayoutParams(marginParams);
    }

    public float dip2px(Context paramContext, float paramFloat) {
        return 0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density;
    }

    private void initListView() {
        mViewOperate = new ViewOperator(this, mActionBar, mSurfaceView, mBottomLinear, mPasterContainer, mPlayImage);
        mViewOperate.setAnimatorListener(new ViewOperator.AnimatorListener() {
            @Override
            public void onShowAnimationEnd() {
                UIEditorPage index = UIEditorPage.get(mTabGroup.getCheckedIndex());
                switch (index) {

                    default:
                        break;
                }
            }

            @Override
            public void onHideAnimationEnd() {
                if (isTakeFrameSelected) {
                    isTakeFrame = true;
                    //继续播放保证截图
                    playingResume();
                    //播放按钮变为可见状态
                    mPlayImage.setVisibility(VISIBLE);
                    isTakeFrameSelected = false;
                }
            }
        });
        mTvRight.setVisibility(View.VISIBLE);
        mEditorService = new EditorService();
        mTabGroup = new TabGroup();
        mViewStack = new ViewStack(getContext(), this, mViewOperate);
        mViewStack.setEditorService(mEditorService);
        mViewStack.setEffectChange(this);
        mViewStack.setOnEffectActionLister(mOnEffectActionLister);
        mTabGroup.addView(findViewById(R.id.tab_filter));
        mTabGroup.addView(findViewById(R.id.tab_effect_filter));
        mTabGroup.addView(findViewById(R.id.tab_cover));

    }

    private void add2Control() {
        TabViewStackBinding tabViewStackBinding = new TabViewStackBinding();
        tabViewStackBinding.setViewStack(mViewStack);
        mTabGroup.setOnCheckedChangeListener(tabViewStackBinding);
        mTabGroup.setOnTabChangeListener(this);
    }

    private void initEditor() {
        //设置onTextureRender能够回调
        mEditorCallback.mNeedRenderCallback = EditorCallBack.RENDER_CALLBACK_TEXTURE;
        mAliyunIEditor = AliyunEditorFactory.creatAliyunEditor(mUri, mEditorCallback);
        initGlSurfaceView();
        {
            //该代码块中的操作必须在AliyunIEditor.init之前调用，否则会出现动图、动效滤镜的UI恢复回调不执行，开发者将无法恢复动图、动效滤镜UI
            mPasterManager = mAliyunIEditor.createPasterManager();
            FrameLayout.LayoutParams surfaceLayout = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
            /*
              指定显示区域大小后必须调用mPasterManager.setDisplaySize，否则将无法添加和恢复一些需要提前获知区域大小的资源，如字幕，动图等
              如果开发者的布局使用了wrapContent或者matchParent之类的布局，务必获取到view的真实宽高之后在调用
             */
            try {
                mPasterManager.setDisplaySize(surfaceLayout.width, surfaceLayout.height);
            } catch (Exception e) {
                showToast = FixedToastUtils.show(getContext(), e.getMessage());
                ((Activity) getContext()).finish();
                return;
            }
            mPasterManager.setOnPasterRestoreListener(mOnPasterRestoreListener);
            mAnimationFilterController = new AnimationFilterController(getContext().getApplicationContext(),
                    mAliyunIEditor);
            mAliyunIEditor.setAnimationRestoredListener(AlivcEditView.this);
        }

        mTranscoder = AliyunCropCreator.createCropInstance(getContext());
        VideoDisplayMode mode = mVideoParam.getScaleMode();
        int ret = mAliyunIEditor.init(mSurfaceView, getContext().getApplicationContext());
        mAliyunIEditor.setDisplayMode(mode);
        mAliyunIEditor.setVolume(mVolume);
        mAliyunIEditor.setFillBackgroundColor(Color.BLACK);
        if (ret != AliyunErrorCode.ALIVC_COMMON_RETURN_SUCCESS) {
            showToast = FixedToastUtils.show(getContext(),
                    getResources().getString(R.string.alivc_editor_edit_tip_init_failed));
            ((Activity) getContext()).finish();
            return;
        }
        mEditorService.addTabEffect(UIEditorPage.FILTER_EFFECT, mAliyunIEditor.getFilterLastApplyId());
        mEditorService.setPaint(mAliyunIEditor.getPaintLastApply());

        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (FastClickUtil.isFastClickActivity(EditorActivity.class.getSimpleName())) {
                    return;
                }
                mTvRight.setEnabled(false);
                //合成方式分为两种，当前页面合成（前台页面）和其他页面合成（后台合成，这里后台并不是真正的app退到后台）
                //前台合成如下：如果要直接合成（当前页面合成），请打开注释，参考注释代码这种方式
                //                int ret = mAliyunIEditor.compose(mVideoParam, "/sdcard/output_compose.mp4", new
                // AliyunIComposeCallBack() {
                //                    @Override
                //                    public void onComposeError(int errorCode) {
                //                        runOnUiThread(new Runnable() {
                //                            @Override
                //                            public void run() {
                //                                v.setEnabled(true);
                //                            }
                //                        });
                //
                //                        Log.d(AliyunTag.TAG, "Compose error, error code "+errorCode);
                //                    }
                //
                //                    @Override
                //                    public void onComposeProgress(int progress) {
                //                        Log.d(AliyunTag.TAG, "Compose progress "+progress+"%");
                //                    }
                //
                //                    @Override
                //                    public void onComposeCompleted() {
                //                        runOnUiThread(new Runnable() {
                //                            @Override
                //                            public void run() {
                //                                v.setEnabled(true);
                //                            }
                //                        });
                //                        Log.d(AliyunTag.TAG, "Compose complete");
                //                    }
                //                });
                //                if(ret != AliyunErrorCode.ALIVC_COMMON_RETURN_SUCCESS) {
                //                    Log.e(AliyunTag.TAG, "Compose error, error code "+ret);
                //                    v.setEnabled(true);//compose error
                //                }

                //后台合成如下：如果要像Demo默认的这样，在其他页面合成，请参考下面这种方式
                mAliyunIEditor.saveEffectToLocal();
                //已经选择封面，并且封面尚未生成的过程不允许跳转
                if (hasCaptureCover && mSnapshop.isSnapshotting()) {
                    alivcEditHandler.sendEmptyMessageDelayed(SAVE_COVER, 500);
                    if (mTransitionAnimation == null) {
                        //转场animation
                        mTransitionAnimation = new AlivcCircleLoadingDialog(getContext(), mPasterContainer.getHeight());
                    }
                    mTransitionAnimation.show();
                    return;
                }
                if (hasCaptureCover && !mSnapshop.isSnapshotting()) {
                    //如果已经选择了封面，则直接跳到下一个页面
                    jumpToNextActivity();
                } else {
                    //如果没有选择封面，则选择原视频的缩略图作为封面，并开启loading框

                    if (mLoadingDialog == null) {
                        mLoadingDialog = new AlivcCircleLoadingDialog(getContext(), 0);
                        mLoadingDialog.show();
                    } else {
                        return;
                    }
                    final AliyunIThumbnailFetcher fetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
                    fetcher.fromConfigJson(mUri.getPath());
                    fetcher.setParameters(mAliyunIEditor.getVideoWidth(), mAliyunIEditor.getVideoHeight(),
                            AliyunIThumbnailFetcher.CropMode.Mediate, VideoDisplayMode.SCALE, 1);
                    fetcher.requestThumbnailImage(new long[]{0}, new AliyunIThumbnailFetcher.OnThumbnailCompletion() {

                        @Override
                        public void onThumbnailReady(Bitmap bitmap, long l) {
                            FileOutputStream fileOutputStream = null;
                            try {
                                fileOutputStream = new FileOutputStream(PATH_THUMBNAIL);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                        fileOutputStream = null;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            jumpToNextActivity();
                            mLoadingDialog.dismiss();
                            mLoadingDialog = null;
                            fetcher.release();
                        }

                        @Override
                        public void onError(int errorCode) {
                            fetcher.release();
                            mLoadingDialog.dismiss();
                            mLoadingDialog = null;
                        }
                    });
                }

            }
        });

        mPlayerListener = new PlayerListener() {

            @Override
            public long getCurrDuration() {
                return mAliyunIEditor.getCurrentStreamPosition();
            }

            @Override
            public long getDuration() {
                long streamDuration = mAliyunIEditor.getStreamDuration();
                Log.d(TAG, "getDuration: " + streamDuration);
                return streamDuration;
            }

            @Override
            public void updateDuration(long duration) {
                mTvCurrTime.setText(convertDuration2Text(duration));
            }
        };

        mViewStack.setPlayerListener(mPlayerListener);
        //配置缩略图滑动条
        initThumbLineBar();
        //非编辑态隐藏
        mThumbLineBar.hide();
        File mWatermarkFile = new File(
                StorageUtils.getCacheDirectory(getContext()) + "/AliyunEditorDemo/tail/logo.png");
        if (mWatermarkFile.exists()) {
            if (mWatermarkBitmap == null || mWatermarkBitmap.isRecycled()) {
                mWatermarkBitmap = BitmapFactory.decodeFile(
                        StorageUtils.getCacheDirectory(getContext()) + "/AliyunEditorDemo/tail/logo.png");
            }
            mSurfaceView.post(new Runnable() {
                @Override
                public void run() {
                    int outputWidth = mVideoParam.getOutputWidth();
                    int outputHeight = mVideoParam.getOutputHeight();
                    int mWatermarkBitmapWidth = DensityUtils.dip2px(getContext(), 30);
                    int mWatermarkBitmapHeight = DensityUtils.dip2px(getContext(), 30);
                    if (mWatermarkBitmap != null && !mWatermarkBitmap.isRecycled()) {
                        mWatermarkBitmapWidth = mWatermarkBitmap.getWidth();
                        mWatermarkBitmapHeight = mWatermarkBitmap.getHeight();
                    }
                    float posY = 0;
                    float percent = (float) outputHeight / outputWidth;
                    if (percent > 1.5) {
                        posY = 0f
                                + (float) (mWatermarkBitmapHeight / 2 + getContext().getResources().getDimensionPixelSize(
                                R.dimen.alivc_svideo_title_height)) / 1.5f / mSurfaceView.getHeight();
                    } else {
                        posY = 0f + (float) mWatermarkBitmapHeight / 1.5f / mSurfaceView.getHeight() / 2;
                    }
                    /**
                     * 水印例子 水印的大小为 ：水印图片的宽高和显示区域的宽高比，注意保持图片的比例，不然显示不完全
                     * 水印的位置为 ：以水印图片中心点为基准，显示区域宽高的比例为偏移量，0,0为左上角，1,1为右下角
                     *
                     */
                    if (hasWaterMark) {
                        mAliyunIEditor.applyWaterMark(
                                StorageUtils.getCacheDirectory(getContext()) + "/AliyunEditorDemo/tail/logo.png",
                                (float) mWatermarkBitmapWidth * 0.5f * 0.8f / mSurfaceView.getWidth(),
                                (float) mWatermarkBitmapHeight * 0.5f * 0.8f / mSurfaceView.getHeight(),
                                (float) mWatermarkBitmapWidth / 1.5f / mSurfaceView.getWidth() / 2,
                                posY);
                    }
                    //旋转水印
                    //ActionRotate actionRotate = new ActionRotate();
                    //actionRotate.setStartTime(0);
                    //actionRotate.setTargetId(id);
                    //actionRotate.setDuration(10 * 1000 * 1000);
                    //actionRotate.setRepeat(true);
                    //actionRotate.setDurationPerCircle(3 * 1000 * 1000);
                    //mAliyunIEditor.addFrameAnimation(actionRotate);

                    /* //图片水印
                    EffectPicture effectPicture = new EffectPicture("/sdcard/1.png");
                    effectPicture.x = 0.5f;
                    effectPicture.y = 0.5f;
                    effectPicture.width = 0.5f;
                    effectPicture.height = 0.5f;
                    effectPicture.start = 0;
                    effectPicture.end = 10 * 1000 * 1000;
                    mAliyunIEditor.addImage(effectPicture);

                    ActionRotate actionRotateImg = new ActionRotate();
                    actionRotateImg.setStartTime(0);
                    actionRotateImg.setTargetId(effectPicture.getViewId());
                    actionRotateImg.setDuration(10 * 1000 * 1000);
                    actionRotateImg.setRepeat(true);
                    actionRotateImg.setDurationPerCircle(3 * 1000 * 1000);
                    mAliyunIEditor.addFrameAnimation(actionRotateImg);*/
                    if (hasTailAnimation) {
                        //片尾水印
                        mAliyunIEditor.addTailWaterMark(
                                StorageUtils.getCacheDirectory(getContext()) + "/AliyunEditorDemo/tail/logo.png",
                                (float) mWatermarkBitmapWidth / mSurfaceView.getWidth(),
                                (float) mWatermarkBitmapHeight / mSurfaceView.getHeight(), 0.5f, 0.5f, 2000 * 1000);
                    }

                }
            });
        }

        mAliyunIEditor.play();

    }

    /**
     * 配置新的缩略条
     */
    private void initThumbLineBar() {
        //获取每张缩略图的尺寸
        int thumbnailSize = getResources().getDimensionPixelOffset(R.dimen.aliyun_editor_size_square_thumbnail);
        Point thumbnailPoint = new Point(thumbnailSize, thumbnailSize);

        //缩略图获取
        if (mThumbnailFetcher == null) {
            mThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
            mThumbnailFetcher.fromConfigJson(mUri.getPath());
        } else if (mThumbnailFetcher.getTotalDuration() != mAliyunIEditor.getStreamDuration() / 1000) {
            //时长改变的时候才去修改缩略图
            Log.i(TAG, "initThumbLineBar: reset thumbLine");
            mAliyunIEditor.saveEffectToLocal();
            mThumbnailFetcher.release();
            mThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
            mThumbnailFetcher.fromConfigJson(mUri.getPath());
        }

        //设置缩略条配置文件
        ThumbLineConfig thumbLineConfig = new ThumbLineConfig.Builder()
                .thumbnailFetcher(mThumbnailFetcher)
                .screenWidth(mScreenWidth)
                .thumbPoint(thumbnailPoint)
                .thumbnailCount(10).build();

        if (mThumbLineBar == null) {
            mThumbLineBar = findViewById(R.id.simplethumblinebar);

            mBarSeekListener = new ThumbLineBar.OnBarSeekListener() {

                @Override
                public void onThumbLineBarSeek(long duration) {
                    mAliyunIEditor.seek(duration);
                    if (mThumbLineBar != null) {
                        mThumbLineBar.pause();
                    }
                    switchPlayStateUI(true);

                    if (mUseInvert) {
                        //当seek到最后时，不允许添加特效
                        if (duration <= USE_ANIMATION_REMAIN_TIME) {
                            mCanAddAnimation = false;
                        } else {
                            mCanAddAnimation = true;
                        }
                    } else {
                        //当seek到最后时，不允许添加特效
                        if (mAliyunIEditor.getDuration() - duration <= USE_ANIMATION_REMAIN_TIME) {
                            mCanAddAnimation = false;
                        } else {
                            mCanAddAnimation = true;
                        }
                    }

                }

                @Override
                public void onThumbLineBarSeekFinish(long duration) {
                    mAliyunIEditor.seek(duration);
                    if (mThumbLineBar != null) {
                        mThumbLineBar.pause();
                    }
                    switchPlayStateUI(true);
                    if (mUseInvert) {
                        //当seek到最后时，不允许添加特效
                        if (duration <= USE_ANIMATION_REMAIN_TIME) {
                            mCanAddAnimation = false;
                        } else {
                            mCanAddAnimation = true;
                        }
                    } else {
                        //当seek到最后时，不允许添加特效
                        if (mAliyunIEditor.getDuration() - duration >= USE_ANIMATION_REMAIN_TIME) {
                            mCanAddAnimation = true;
                        } else {
                            mCanAddAnimation = false;
                        }
                    }
                }
            };

        }

        mThumbLineBar.setup(thumbLineConfig, mBarSeekListener, mPlayerListener);

    }

    /**
     * 更改播放状态的图标和文字 播放时,文字内容显示为: 暂停播放, 图标使暂停图标, mipmap/aliyun_svideo_pause 暂停时,文字内容显示为: 播放全篇, 图标使用播放图标,
     * mipmap/aliyun_svideo_play
     *
     * @param changeState, 需要显示的状态,  true: 播放全篇, false: 暂停播放
     */
    public void switchPlayStateUI(boolean changeState) {
        if (changeState) {
            mPlayImage.setText(getResources().getString(R.string.alivc_editor_edit_play_start));
            UIConfigManager.setImageResourceConfig(mPlayImage, 0, R.attr.playImage, R.mipmap.aliyun_svideo_play);
        } else {
            mPlayImage.setText(getResources().getString(R.string.alivc_editor_edit_play_pause));
            UIConfigManager.setImageResourceConfig(mPlayImage, 0, R.attr.pauseImage, R.mipmap.aliyun_svideo_pause);
        }
    }

    private final OnPasterRestored mOnPasterRestoreListener = new OnPasterRestored() {

        @Override
        public void onPasterRestored(final List<AliyunPasterController> controllers) {

            Log.d(TAG, "onPasterRestored: " + controllers.size());

            mPasterContainer.post(new Runnable() {//之所以要放在这里面，是因为下面的操作中有UI相关的，需要保证布局完成后执行，才能保证UI更新的正确性
                @Override
                public void run() {

                    if (mThumbLineBar != null && mThumbLineBar.getChildCount() != 0) {
                        //这里做合成（时间和转场特效会清空paster特效）恢复 针对缩略图的覆盖效果
                    }

                    if (mPasterContainer != null) {
                        mPasterContainer.removeAllViews();
                    }
                    for (AliyunPasterController c : controllers) {
                        if (!c.isPasterExists()) {
                            continue;
                        }
                        if (c.getPasterStartTime() >= mAliyunIEditor.getStreamDuration()) {
                            //恢复时覆盖超出缩略图,丢弃
                            continue;
                        }
                        c.setOnlyApplyUI(true);

                    }

                }
            });
        }

    };

    @Override
    public void onEffectChange(final EffectInfo effectInfo) {
        Log.e("editor", "====== onEffectChange ");
        //返回素材属性

        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        UIEditorPage type = effectInfo.type;

        Log.d(TAG, "effect path " + effectInfo.getPath());
        switch (type) {
            case FILTER_EFFECT:
                if (effect.getPath().contains("Vertigo")) {
                    EffectFilter filter = new EffectFilter(effect.getPath());
                    mAliyunIEditor.addAnimationFilter(filter);
                } else {
                    mAliyunIEditor.applyFilter(effect);
                }
                break;

            default:
                break;
        }
    }


    private boolean mIsTransitioning = false;
    private AlivcCircleLoadingDialog mTransitionAnimation;

    private void startTransitionAnimation() {
        mTransitionAnimation.show();
        mIsTransitioning = true;
    }

    private void stopTransitionAnimation() {
        mTransitionAnimation.dismiss();
        mIsTransitioning = false;
    }


    /**
     * 初始化线程池和Handler
     */
    private void initThreadHandler() {
        executorService = ThreadUtil.newDynamicSingleThreadedExecutor(new AlivcEditThread());
        alivcEditHandler = new AlivcEditHandler(this);
    }

    /**
     * 是否存在录制有音乐
     *
     * @param isHashRecordMusic boolean
     */
    public void setHasRecordMusic(boolean isHashRecordMusic) {
        this.mHasRecordMusic = isHashRecordMusic;
    }

    /**
     * 获取存在录制有音乐
     *
     * @return boolean
     */
    public boolean isHasRecordMusic() {
        return mHasRecordMusic;
    }

    /**
     * 是否是合拍过来的视频
     */
    public void setIsMixRecord(boolean isMixRecord) {
        this.isMixRecord = isMixRecord;
    }

    /**
     * 获取是否是合拍
     */
    public boolean isMaxRecord() {
        return isMixRecord;
    }

    public static class AlivcEditThread implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("AlivcEdit Thread");
            return thread;
        }
    }

    private static final int ADD_TRANSITION = 1;
    private static final int REVERT_TRANSITION = 2;
    private static final int SAVE_COVER = 3;

    private static class AlivcEditHandler extends Handler {

        private WeakReference<AlivcEditView> reference;

        public AlivcEditHandler(AlivcEditView editView) {
            reference = new WeakReference<>(editView);
        }

        @Override
        public void handleMessage(Message msg) {
            AlivcEditView alivcEditView = reference.get();
            if (alivcEditView == null) {
                return;
            }
            switch (msg.what) {
                case REVERT_TRANSITION:
                    alivcEditView.playingResume();
                    alivcEditView.stopTransitionAnimation();
                    alivcEditView.clickCancel();
                    break;
                case ADD_TRANSITION:
                    EffectInfo effectInfo = (EffectInfo) msg.getData().getSerializable("effectInfo");
                    alivcEditView.addTransitionSuccess(effectInfo);
                    break;

                case SAVE_COVER:
                    //循环查询截取封面工作是否结束，结束跳转到下个页面
                    if (alivcEditView.mSnapshop.isSnapshotting()) {
                        sendEmptyMessageDelayed(SAVE_COVER, 500);
                    } else {
                        removeMessages(SAVE_COVER);
                        alivcEditView.mTransitionAnimation.dismiss();
                        alivcEditView.jumpToNextActivity();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 添加转场成功
     *
     * @param effectInfo
     */
    private void addTransitionSuccess(EffectInfo effectInfo) {

        //提前一秒
        long advanceTime = 1000 * 1000;
        long clipStartTime = mAliyunIEditor.getClipStartTime(effectInfo.clipIndex + 1);

        advanceTime = clipStartTime - advanceTime >= 0 ? clipStartTime - advanceTime : 0;
        mAliyunIEditor.seek(advanceTime);
        playingResume();
        mWaitForReady = true;
        stopTransitionAnimation();
        Log.d(TAG, "onTransitionPreview: index = " + effectInfo.clipIndex
                + " ,clipStartTime = " + clipStartTime
                + " ,duration = " + mAliyunIEditor.getDuration()
                + " ,advanceTime = " + advanceTime
        );
    }


    @Override
    public void onTabChange() {
        Log.d(TAG, "onTabChange: ");
        UIEditorPage page = UIEditorPage.get(mTabGroup.getCheckedIndex());
        switch (page) {
            case COVER:
                //暂停播放并隐藏播放按钮。
                playingPause();
                mPlayImage.setVisibility(GONE);
                break;

            default:
                break;
        }
    }


    private List<EffectBase> mPasterEffecCachetList = new ArrayList<>();

    private void checkAndRemovePaster() {

    }

    /**
     * 调用resetThumbLine的时机
     */
    private void resetTimeLineLayout() {
        Log.i(TAG, "resetTimeLineLayout");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                initThumbLineBar();
            }
        });
    }

    protected void playingPause() {
        if (mAliyunIEditor.isPlaying()) {
            mAliyunIEditor.pause();
            if (mThumbLineBar != null) {
                mThumbLineBar.pause();
            }
            switchPlayStateUI(true);
        }
    }

    private void playingResume() {
        if (!mAliyunIEditor.isPlaying()) {
            if (mAliyunIEditor.isPaused()) {
                mAliyunIEditor.resume();
            } else {
                mAliyunIEditor.play();
            }
            if (mThumbLineBar != null) {
                mThumbLineBar.resume();
            }
            switchPlayStateUI(false);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == mPlayImage && mAliyunIEditor != null) {
            //当在添加特效的时候，关闭该按钮
            if (mUseAnimationFilter) {
                return;
            }
            if (mAliyunIEditor.isPlaying()) {
                playingPause();
            } else {
                playingResume();
            }
        }
    }

    private void clickConfirm() {

        // 确认后变化，各个模块自行实现
        int checkIndex = mTabGroup.getCheckedIndex();
        UIEditorPage page = UIEditorPage.get(checkIndex);
        switch (page) {
            case COVER:
                //改变标示，具体截图代码在mEditorCallback.onTextureRender中实现
                isTakeFrameSelected = true;
                hasCaptureCover = true;
                break;
            default:
                break;
        }
        mViewOperate.hideBottomView();
    }

    /**
     * 编辑态视图点击返回
     */
    private void clickCancel() {
        // 取消后变化，各个模块自行实现
        int checkIndex = mTabGroup.getCheckedIndex();
        UIEditorPage page = UIEditorPage.get(checkIndex);
        switch (page) {
            case COVER:
                isTakeFrameSelected = false;
                mPlayImage.setVisibility(VISIBLE);
                break;
            default:
                break;
        }

        mViewOperate.hideBottomView();
    }

    /**
     * 点击空白出弹窗消失
     */
    private void hideBottomEditorView() {

        int checkIndex = mTabGroup.getCheckedIndex();
        if (checkIndex == -1) {
            return;
        }
        UIEditorPage page = UIEditorPage.get(checkIndex);

        mViewOperate.hideBottomEditorView(page);

    }

    /**
     * 恢复动效滤镜UI（这里主要是编辑页面顶部时间轴的覆盖
     *
     * @param animationFilters
     */
    @Override
    public void animationFilterRestored(final List<EffectFilter> animationFilters) {
        mPasterContainer.post(new Runnable() {
            @Override
            public void run() {
                mAnimationFilterController.setThumbLineBar(mThumbLineBar);
                if (mAnimationFilterController != null) {
                    mAnimationFilterController.restoreAnimationFilters(animationFilters);
                }
            }
        });
    }

    /**
     * 页面缩小时 对应的paster也要缩小
     *
     * @param scaleSize 缩小比率
     */
    public void setPasterDisplayScale(float scaleSize) {
        mPasterManager.setDisplaySize((int) (mPasterContainerPoint.x * scaleSize),
                (int) (mPasterContainerPoint.y * scaleSize));
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        float mPosX;
        float mPosY;
        boolean shouldDrag = true;

        boolean shouldDrag() {
            return shouldDrag;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d(TAG, "onDoubleTapEvent");
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d(TAG, "onSingleTapConfirmed");

            if (!shouldDrag) {
                boolean outside = true;
                BaseChooser bottomView = null;
                if (mViewOperate != null) {
                    bottomView = mViewOperate.getBottomView();
                }

                if (outside) {
                    hideBottomEditorView();
                }
            } else {
                playingPause();
            }
            return shouldDrag;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d(TAG, "onShowPress");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (shouldDrag()) {
                if (mPosX == 0 || mPosY == 0) {
                    mPosX = e1.getX();
                    mPosY = e1.getY();
                }
                float x = e2.getX();
                float y = e2.getY();

                mPosX = x;
                mPosY = y;

            } else {

            }

            return shouldDrag;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return shouldDrag;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown");
            shouldDrag = false;
            mPosX = 0;
            mPosY = 0;
            return true;

        }
    }

    StringBuilder mDurationText = new StringBuilder(5);

    private String convertDuration2Text(long duration) {
        mDurationText.delete(0, mDurationText.length());
        float relSec = (float) duration / (1000 * 1000);// us -> s
        int min = (int) ((relSec % 3600) / 60);
        int sec = 0;
        sec = (int) (relSec % 60);
        if (min >= 10) {
            mDurationText.append(min);
        } else {
            mDurationText.append("0").append(min);
        }
        mDurationText.append(":");
        if (sec >= 10) {
            mDurationText.append(sec);
        } else {
            mDurationText.append("0").append(sec);
        }
        return mDurationText.toString();
    }

    private void copyAssets() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Common.copyAll(getContext(), resCopy);
            }
        });
    }

    public AliyunIEditor getEditor() {
        return this.mAliyunIEditor;
    }

    public void showMessage(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(id);
        builder.setNegativeButton(R.string.alivc_common_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private OnEffectActionLister mOnEffectActionLister = new OnEffectActionLister() {
        @Override
        public void onCancel() {
            clickCancel();
        }

        @Override
        public void onComplete() {
            clickConfirm();
        }
    };

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventColorFilterSelected(SelectColorFilter selectColorFilter) {
        EffectInfo effectInfo = selectColorFilter.getEffectInfo();
        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        mAliyunIEditor.applyFilter(effect);
    }

    /**
     * 长按时需要恢复播放
     *
     * @param filter
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventAnimationFilterLongClick(LongClickAnimationFilter filter) {
        if (!mUseAnimationFilter) {
            mUseAnimationFilter = true;
        }
        if (mCanAddAnimation) {
            playingResume();
        } else {
            playingPause();
        }

    }

    /**
     * 长按抬起手指需要暂停播放
     *
     * @param filter
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventAnimationFilterClickUp(LongClickUpAnimationFilter filter) {
        if (mUseAnimationFilter) {
            mUseAnimationFilter = false;
        }
        if (mAliyunIEditor.isPlaying()) {
            playingPause();

        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventFilterTabClick(FilterTabClick ft) {
        //切换到特效的tab需要暂停播放，切换到滤镜的tab需要恢复播放
        if (mAliyunIEditor != null) {
            switch (ft.getPosition()) {
                case FilterTabClick.POSITION_ANIMATION_FILTER:
                    if (mAliyunIEditor.isPlaying()) {
                        playingPause();
                    }
                    break;
                case FilterTabClick.POSITION_COLOR_FILTER:
                    if (!mAliyunIEditor.isPlaying()) {
                        playingResume();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private EditorCallBack mEditorCallback = new EditorCallBack() {
        @Override
        public void onEnd(int state) {

            post(new Runnable() {
                @Override
                public void run() {

                    if (!mUseAnimationFilter) {
                        //当正在添加滤镜的时候，不允许重新播放
                        mAliyunIEditor.replay();
                        mThumbLineBar.restart();
                    } else {
                        mCanAddAnimation = false;
                        switchPlayStateUI(true);

                    }

                }
            });
        }

        @Override
        public void onError(final int errorCode) {
            Log.e(TAG, "play error " + errorCode);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    switch (errorCode) {
                        case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_WRONG_STATE:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_PROCESS_FAILED:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_NO_FREE_DISK_SPACE:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_CREATE_DECODE_GOP_TASK_FAILED:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_AUDIO_STREAM_DECODER_INIT_FAILED:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_VIDEO_STREAM_DECODER_INIT_FAILED:

                        case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_SPS_PPS_NULL:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_H264_PARAM_SET_FAILED:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_HEVC_PARAM_SET_FAILED:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_DECODER_FAILED:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_STATE:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_INPUT:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_NO_BUFFER_AVAILABLE:

                        case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_DECODE_SPS:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_CREATE_DECODER_FAILED:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_STATE:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_INPUT:
                        case AliyunErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_NO_BUFFER_AVAILABLE:
                            showToast = FixedToastUtils.show(getContext(), errorCode + "");
                            ((Activity) getContext()).finish();
                            break;
                        case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_CACHE_DATA_SIZE_OVERFLOW:
                            showToast = FixedToastUtils.show(getContext(), errorCode + "");
                            mThumbLineBar.restart();
                            mAliyunIEditor.play();
                            break;
                        case AliyunErrorCode.ALIVC_SVIDEO_ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                            showToast = FixedToastUtils.show(getContext(),
                                    getResources().getString(R.string.alivc_editor_error_tip_not_supported_audio));
                            ((Activity) getContext()).finish();
                            break;
                        case AliyunErrorCode.ALIVC_SVIDEO_ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                            showToast = FixedToastUtils.show(getContext(),
                                    getResources().getString(R.string.alivc_editor_error_tip_not_supported_video));
                            ((Activity) getContext()).finish();
                            break;
                        case AliyunErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_STREAM_NOT_EXISTS:
                        case AliyunErrorCode.ALIVC_SVIDEO_ERROR_MEDIA_NOT_SUPPORTED_PIXEL_FORMAT:
                            showToast = FixedToastUtils.show(getContext(),
                                    getResources().getString(R.string.alivc_editor_error_tip_not_supported_pixel_format));
                            ((Activity) getContext()).finish();
                            break;
                        case AliyunErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_INTERRUPT:
                            showToast = FixedToastUtils.show(getContext(),
                                    getResources().getString(R.string.alivc_editor_edit_tip_decoder_error_interrupt));
                            ((Activity) getContext()).finish();
                            break;
                        default:
                            showToast = FixedToastUtils.show(getContext(),
                                    getResources().getString(R.string.alivc_editor_error_tip_play_video_error));
                            ((Activity) getContext()).finish();
                            break;
                    }
                }
            });

        }

        @Override
        public int onCustomRender(int srcTextureID, int width, int height) {
            return srcTextureID;
        }

        @Override
        public int onTextureRender(int srcTextureID, int width, int height) {
            if (isTakeFrame) {
                if (mSnapshop == null) {
                    mSnapshop = new AlivcSnapshot();
                }
                mSnapshop.useTextureIDGetFrame(srcTextureID, mSurfaceView, width, height, new File(PATH_THUMBNAIL));
                isTakeFrame = false;
            }

            return 0;
        }

        @Override
        public void onPlayProgress(final long currentPlayTime, final long currentStreamPlayTime) {
            post(new Runnable() {
                @Override
                public void run() {
                    long currentPlayTime = mAliyunIEditor.getCurrentPlayPosition();
                    if (mUseAnimationFilter
                            && mAliyunIEditor.getDuration() - currentPlayTime < USE_ANIMATION_REMAIN_TIME) {
                        mCanAddAnimation = false;
                    } else {
                        mCanAddAnimation = true;
                    }
                }
            });

        }

        private int c = 0;

        @Override
        public void onDataReady() {
            post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onDataReady received");
                    if (mWaitForReady && c > 0) {
                        Log.d(TAG, "onDataReady resume");
                        mWaitForReady = false;
                        mAliyunIEditor.resume();
                    }
                    c++;
                }
            });

        }
    };
    public static final int USE_ANIMATION_REMAIN_TIME = 300 * 1000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KEYCODE_VOLUME_DOWN:
                mVolume -= 5;
                if (mVolume < 0) {
                    mVolume = 0;
                }
                mAliyunIEditor.setVolume(mVolume);
                return true;
            case KEYCODE_VOLUME_UP:
                mVolume += 5;
                if (mVolume > 100) {
                    mVolume = 100;
                }
                mAliyunIEditor.setVolume(mVolume);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    private boolean isNeedResume = true;

    public void onStart() {
        mIsStop = false;
        if (mViewStack != null) {
            mViewStack.setVisibleStatus(true);
        }
    }

    public void onResume() {
        mTvRight.setEnabled(true);
        if (isNeedResume) {
            playingResume();
        }
        checkAndRemovePaster();
    }

    public void onPause() {
        isNeedResume = mAliyunIEditor.isPlaying();
        playingPause();
        mAliyunIEditor.saveEffectToLocal();
    }

    public void onStop() {

        if (mTvRight != null) {
            mTvRight.setEnabled(true);
        }
        mIsStop = true;
        if (mViewStack != null) {
            mViewStack.setVisibleStatus(false);
        }
        if (showToast != null) {
            showToast.cancel();
            showToast = null;
        }
    }

    public void onDestroy() {
        mIsDestroyed = true;
        Dispatcher.getInstance().unRegister(this);
        if (mAliyunIEditor != null) {
            mAliyunIEditor.onDestroy();
        }

        if (mAnimationFilterController != null) {
            mAnimationFilterController.destroyController();
        }

        if (mThumbLineBar != null) {
            mThumbLineBar.stop();
        }

        if (mThumbnailFetcher != null) {
            mThumbnailFetcher.release();
        }

        if (mTranscoder != null) {
            if (mIsTranscoding) {
                mTranscoder.cancel();
            } else {
                mTranscoder.dispose();
            }
        }

        if (mViewOperate != null) {
            mViewOperate.setAnimatorListener(null);
            mViewOperate = null;
        }

        if (animatorX != null) {
            animatorX.cancel();
            animatorX.addUpdateListener(null);
            animatorX.addListener(null);
            animatorX = null;
        }

        if (mWatermarkBitmap != null && !mWatermarkBitmap.isRecycled()) {
            mWatermarkBitmap.recycle();
            mWatermarkBitmap = null;
        }

        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public boolean onBackPressed() {
        if (mIsTranscoding) {
            //转码过程中无法操作
            showToast = FixedToastUtils.show(getContext(),
                    getResources().getString(R.string.alivc_editor_edit_tip_transcode_no_operate));
            return true;
        }
        if (mViewOperate != null) {
            boolean isShow = mViewOperate.isBottomViewShow();
            // 直接隐藏
            if (isShow) {
                if (mViewOperate != null) {
                    mViewOperate.getBottomView().onBackPressed();
                    hideBottomEditorView();
                }
            }
            return isShow;
        } else {
            return false;
        }
    }

    private Uri mUri;
    private boolean hasTailAnimation = false;

    public void setParam(AliyunVideoParam mVideoParam, Uri mUri, boolean hasTailAnimation, boolean hasWaterMark) {
        this.hasTailAnimation = hasTailAnimation;
        this.mUri = mUri;
        this.mVideoParam = mVideoParam;
        this.hasWaterMark = hasWaterMark;
        initEditor();

    }


    private AliyunVideoParam mVideoParam;

    /**
     * 播放时间、显示时间同步接口
     */
    public interface PlayerListener {
        /**
         * 获取当前的播放时间（-->缩略图条位置同步）
         *
         * @return 前的播放时间
         */
        long getCurrDuration();

        /**
         * 获取视频总时间
         *
         * @return 视频总时间
         */
        long getDuration();

        /**
         * 更新时间（-->显示时间同步）
         *
         * @param duration 更新时间
         */
        void updateDuration(long duration);
    }

    /**
     * 根据配置跳转到下一个activity
     */
    private void jumpToNextActivity() {
        if (mOnFinishListener != null) {
            AlivcEditOutputParam outputParam = new AlivcEditOutputParam();
            outputParam.setConfigPath(mUri.getPath());
            outputParam.setOutputVideoHeight(mAliyunIEditor.getVideoHeight());
            outputParam.setOutputVideoWidth(mAliyunIEditor.getVideoWidth());
            outputParam.setVideoRatio(((float) mPasterContainerPoint.x) / mPasterContainerPoint.y);
            outputParam.setVideoParam(mVideoParam);
            outputParam.setThumbnailPath(PATH_THUMBNAIL);
            mOnFinishListener.onComplete(outputParam);
        }
    }

    /**
     * 编辑完成事件监听
     */
    public interface OnFinishListener {
        void onComplete(AlivcEditOutputParam outputParam);
    }

    private OnFinishListener mOnFinishListener;

    public OnFinishListener getOnFinishListener() {
        return mOnFinishListener;
    }

    public void setmOnFinishListener(OnFinishListener finishListener) {
        this.mOnFinishListener = finishListener;
    }

}

