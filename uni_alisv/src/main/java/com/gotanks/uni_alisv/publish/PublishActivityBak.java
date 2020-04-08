package com.gotanks.uni_alisv.publish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aliyun.common.global.AliyunTag;
import com.aliyun.common.utils.ToastUtil;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.qupai.editor.AliyunIComposeCallBack;
import com.aliyun.qupai.editor.AliyunIVodCompose;
import com.aliyun.qupai.editor.impl.AliyunVodCompose;
import com.aliyun.qupaiokhttp.HttpRequest;
import com.aliyun.qupaiokhttp.RequestParams;
import com.aliyun.qupaiokhttp.StringHttpRequestCallback;
import com.aliyun.svideo.base.Constants;
import com.aliyun.svideo.base.widget.ProgressDialog;
import com.aliyun.svideo.common.utils.DateTimeUtils;
import com.aliyun.svideo.common.utils.ThreadUtils;
import com.aliyun.svideo.common.utils.ToastUtils;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.thumbnail.AliyunIThumbnailFetcher;
import com.aliyun.svideo.sdk.external.thumbnail.AliyunThumbnailFetcherFactory;
import com.gotanks.uni_alisv.AliSvWXModule;
import com.gotanks.uni_alisv.R;
import com.gotanks.uni_alisv.bean.SvOptions;
import com.gotanks.uni_alisv.constants.AlivcLittleServerApiConstants;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by macpro on 2017/11/6.
 * 视频合成页面
 */

public class PublishActivityBak extends Activity implements View.OnClickListener {
    private static final String TAG = PublishActivityBak.class.getName();

    public static final String KEY_PARAM_CONFIG = "project_json_path";
    public static final String KEY_PARAM_THUMBNAIL = "svideo_thumbnail";
    public static final String KEY_PARAM_VIDEO_RATIO = "key_param_video_ratio";
    public static final String KEY_PARAM_VIDEO_WIDTH = "key_param_video_width";
    public static final String KEY_PARAM_VIDEO_HEIGHT = "key_param_video_height";
    public static final String KEY_PARAM_QUESTION_MODE = "key_param_question_mode";


    private View mActionBar;
    private ImageView mIvLeft;
    private ProgressBar mProgress;
    private ImageView mCoverImage;
    private View mCoverSelect;
    private View mComposeProgressView;
    private TextView mComposeProgress;
    private View mComposeIndiate;
    private TextView mComposeStatusText, mComposeStatusTip;
    private TextView mPublish;

    private String mComposeFileName = null;
    private String mComposeOutputPath = "";

    private String mThumbnailPath;
    private AliyunIVodCompose mComposeClient;
    private boolean mComposeCompleted;
    private AsyncTask<String, Void, Bitmap> mAsyncTaskOnCreate;
    private AsyncTask<String, Void, Bitmap> mAsyncTaskResult;
    private String mConfigPath;
    private RadioGroup vRgPublishType;
    private TextView tv_center;
    private TextView vTvPublishName;

    private int videoWidth;
    private int videoHeight;

    private boolean isQuestionMode;



    /**
     * 视频缩略图截取，不同于MediaMetadataRetriever，可精准获取视频非关键帧图片
     */
    private AliyunIThumbnailFetcher aliyunIThumbnailFetcher;
    private boolean mIsUpload;
    private long videoSize;
    private long imageSize;
    private SvOptions svOptions;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_editor_activity_publish);
        Intent intent = getIntent();
        this.svOptions = intent.getParcelableExtra(AliSvWXModule.KEY_OPTIONS);
        isQuestionMode = intent.getBooleanExtra(KEY_PARAM_QUESTION_MODE, false);
        initView();
        mConfigPath = intent.getStringExtra(KEY_PARAM_CONFIG);
        mThumbnailPath = intent.getStringExtra(KEY_PARAM_THUMBNAIL);
        videoWidth = intent.getIntExtra(KEY_PARAM_VIDEO_WIDTH, 0);
        videoHeight = intent.getIntExtra(KEY_PARAM_VIDEO_HEIGHT, 0);

        aliyunIThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();

        mComposeClient = ComposeFactory.INSTANCE.getAliyunVodCompose();
        mComposeClient.init(this.getApplicationContext());

        //开始合成

        mComposeFileName = DateTimeUtils.getDateTimeFromMillisecond(System.currentTimeMillis()) + Constants.SDCardConstants.COMPOSE_SUFFIX;
        mComposeOutputPath = Constants.SDCardConstants.OUTPUT_PATH_DIR + mComposeFileName;
        int ret = mComposeClient.compose(mConfigPath, mComposeOutputPath, mCallback);
        if (ret != AliyunErrorCode.ALIVC_COMMON_RETURN_SUCCESS) {
            return;
        }
        mAsyncTaskOnCreate = new MyAsyncTask(this).execute(mThumbnailPath);
    }

    private void initThumbnail(Bitmap thumbnail) {
        mCoverImage.setImageBitmap(thumbnail);

    }

    static class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<PublishActivityBak> ref;
        private float maxWidth;

        MyAsyncTask(PublishActivityBak activity) {
            ref = new WeakReference<>(activity);
            maxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    240, activity.getResources().getDisplayMetrics());
        }

        @Override
        protected Bitmap doInBackground(String... thumbnailPaths) {
            Bitmap bmp = null;
            if (ref != null) {
                PublishActivityBak publishActivity = ref.get();
                if (publishActivity != null) {
                    String path = thumbnailPaths[0];
                    if (TextUtils.isEmpty(path)) {
                        return null;
                    }
                    File thumbnail = new File(path);
                    if (!thumbnail.exists()) {
                        return null;
                    }
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(path, opt);
                    float bw = opt.outWidth;
                    float bh = opt.outHeight;
                    float scale;
                    if (bw > bh) {
                        scale = bw / maxWidth;
                    } else {
                        scale = bh / maxWidth;
                    }
                    boolean needScaleAfterDecode = scale != 1;
                    opt.inJustDecodeBounds = false;
                    bmp = BitmapFactory.decodeFile(path, opt);
                    if (bmp != null && needScaleAfterDecode) {
                        bmp = publishActivity.scaleBitmap(bmp, scale);
                    }
                }
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null && ref != null && ref.get() != null) {
                ref.get().initThumbnail(bitmap);
            }
        }
    }

    private Bitmap scaleBitmap(Bitmap bmp, float scale) {
        Matrix mi = new Matrix();
        mi.setScale(1 / scale, 1 / scale);
        Bitmap temp = bmp;
        bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), mi, false);
        temp.recycle();
        return bmp;
    }

    private void initView() {
        tv_center = findViewById(R.id.tv_center);
        vTvPublishName = findViewById(R.id.vTvPublishName);
        mActionBar = findViewById(R.id.action_bar);
        mActionBar.setBackgroundColor(
                getResources().getColor(R.color.alivc_common_theme_primary_alpha_50));
        mPublish = (TextView) findViewById(R.id.tv_right);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mIvLeft.setOnClickListener(this);
        mIvLeft.setImageResource(R.mipmap.aliyun_svideo_icon_back);
        mPublish.setText(R.string.alivc_editor_publish_tittle);
        mIvLeft.setVisibility(View.VISIBLE);
        mPublish.setVisibility(View.VISIBLE);
        mProgress = (ProgressBar) findViewById(R.id.publish_progress);
        mComposeProgressView = findViewById(R.id.compose_progress_view);
        mCoverImage = (ImageView) findViewById(R.id.publish_cover_image);
        mComposeIndiate = findViewById(R.id.image_compose_indicator);
        mPublish.setEnabled(mComposeCompleted);
        mPublish.setOnClickListener(this);
        mCoverSelect = findViewById(R.id.publish_cover_select);
        mCoverSelect.setEnabled(mComposeCompleted);
        mCoverSelect.setOnClickListener(this);
        mComposeProgress = (TextView) findViewById(R.id.compose_progress_text);
        mComposeStatusText = (TextView) findViewById(R.id.compose_status_text);
        mComposeStatusTip = (TextView) findViewById(R.id.compose_status_tip);
        vRgPublishType = findViewById(R.id.vRgPublishType);

        if (isQuestionMode) {
            vTvPublishName.setText("提交");
            tv_center.setText("提交视频");
            mPublish.setText("提交");
            findViewById(R.id.vLytOptions).setVisibility(View.INVISIBLE);
        }
    }

    private int count(String text) {
        int len = text.length();
        int skip;
        int letter = 0;
        int chinese = 0;
        for (int i = 0; i < len; i += skip) {
            int code = text.codePointAt(i);
            skip = Character.charCount(code);
            if (code == 10) {
                continue;
            }
            String s = text.substring(i, i + skip);
            if (isChinese(s)) {
                chinese++;
            } else {
                letter++;
            }

        }
        letter = letter % 2 == 0 ? letter / 2 : (letter / 2 + 1);
        int result = chinese + letter;
        return result;
    }

    // 完整的判断中文汉字和符号
    private boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == mPublish) {
            mPublish.setEnabled(false);


            startUploadVideo();

        } else if (v == mCoverSelect) {
            Intent intent = new Intent(this, CoverEditActivity.class);
            intent.putExtra(CoverEditActivity.KEY_PARAM_VIDEO, mComposeOutputPath);
            startActivityForResult(intent, 0);
        } else if (v == mIvLeft) {
            onBackPressed();
        }
    }


    @Override
    public void onBackPressed() {
        if (mComposeCompleted) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.setTitle(R.string.alivc_editor_publish_dialog_cancel_content_tip)
                    .setNegativeButton(R.string.alivc_editor_publish_goback, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mComposeCompleted) {
                                finish();
                            } else {
                                if (mComposeClient != null) {
                                    mComposeClient.cancelCompose();
                                }
                                finish();
                            }
                        }
                    })
                    .setPositiveButton(R.string.alivc_editor_publish_continue, null).create();
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            mThumbnailPath = data.getStringExtra(CoverEditActivity.KEY_PARAM_RESULT);
            mAsyncTaskResult = new MyAsyncTask(this).execute(mThumbnailPath);
        }
    }

    private final AliyunIComposeCallBack mCallback = new AliyunIComposeCallBack() {
        @Override

        public void onComposeError(int errorCode) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mComposeProgress.setVisibility(View.GONE);
                    mComposeIndiate.setVisibility(View.VISIBLE);
                    mComposeIndiate.setActivated(false);
                    mComposeStatusTip.setText(R.string.alivc_editor_publish_tip_retry);
                    mComposeStatusText.setText(R.string.alivc_editor_publish_compose_failed);
                }
            });
        }

        @Override
        public void onComposeProgress(final int progress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mComposeProgress.setText(progress + "%");
                    mProgress.setProgress(progress);
                }
            });
        }

        @Override
        public void onComposeCompleted() {
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{mComposeOutputPath}, new String[]{"video/mp4"}, null);
            mComposeCompleted = true;
            aliyunIThumbnailFetcher.addVideoSource(mComposeOutputPath, 0, Integer.MAX_VALUE, 0);
            aliyunIThumbnailFetcher.setParameters(videoWidth, videoHeight,
                    AliyunIThumbnailFetcher.CropMode.Mediate, VideoDisplayMode.SCALE, 8);
            requestThumbnailImage(0);
        }
    };

    private void requestThumbnailImage(int index) {
        Log.e("frameBitmap", "requestThumbnailImage" + index);
        aliyunIThumbnailFetcher.requestThumbnailImage(new long[]{index}, mThumbnailCallback);
    }

    private final AliyunIThumbnailFetcher.OnThumbnailCompletion mThumbnailCallback = new AliyunIThumbnailFetcher.OnThumbnailCompletion() {
        private int vecIndex = 1;
        private int mInterval = 100;

        @Override
        public void onThumbnailReady(Bitmap frameBitmap, long time) {
            if (frameBitmap != null && !frameBitmap.isRecycled()) {
                Log.e("frameBitmap", "isRecycled");
                mCoverImage.setVisibility(View.VISIBLE);
                initThumbnail(frameBitmap);
                mPublish.setEnabled(mComposeCompleted);
                mProgress.setVisibility(View.GONE);
                mComposeProgress.setVisibility(View.GONE);

                mComposeIndiate.setVisibility(View.VISIBLE);
                mComposeIndiate.setActivated(true);
                mComposeStatusTip.setVisibility(View.GONE);
                mComposeStatusText.setText(R.string.alivc_editor_publish_compose_success);
                mComposeProgressView.postDelayed(composeProgressRunnable, 2000);
            } else {
                vecIndex = vecIndex + mInterval;
                requestThumbnailImage(vecIndex);
            }


        }

        @Override
        public void onError(int errorCode) {
            Log.d(TAG, "fetcher onError " + errorCode);
            ToastUtils.show(PublishActivityBak.this, R.string.alivc_editor_cover_fetch_cover_error);
        }
    };

    private Runnable composeProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mComposeProgressView != null) {
                mComposeProgressView.setVisibility(View.GONE);
            }
            if (mCoverSelect != null) {
                mCoverSelect.setVisibility(View.VISIBLE);
                mCoverSelect.setEnabled(mComposeCompleted);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mComposeClient != null) {
            mComposeClient.resumeCompose();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mComposeClient != null) {
            mComposeClient.pauseCompose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        aliyunIThumbnailFetcher.release();

        if (mComposeClient != null) {
            mComposeClient.release();
            mComposeClient = null;
        }
        if (mComposeProgressView != null) {
            mComposeProgressView.removeCallbacks(composeProgressRunnable);
        }

        if (mAsyncTaskOnCreate != null) {
            mAsyncTaskOnCreate.cancel(true);
            mAsyncTaskOnCreate = null;
        }

        if (mAsyncTaskResult != null) {
            mAsyncTaskResult.cancel(true);
            mAsyncTaskResult = null;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mIsUpload) {
            mComposeClient.resumeUpload();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsUpload) {
            mComposeClient.pauseUpload();
        }

    }


    private volatile boolean isPublishing = false;

    private void startUploadVideo() {
        if (mThumbnailPath != null) {
            imageSize = new File(mThumbnailPath).length();
        }
        if (mComposeOutputPath != null) {
            videoSize = new File(mComposeOutputPath).length();
        }
        isPublishing = true;
        startImageUpload();
    }

    private void cancelUpload() {
        isPublishing = false;
        if (mComposeClient != null) {
            mComposeClient.cancelUpload();
        }
        ToastUtil.showToast(this, "已取消");
        mPublish.setEnabled(true);
    }

    private ProgressDialog progressDialog = null;

    private void showUploadProgress(int progress, boolean cancelAble) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "正在发布", "正在发布", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //取消发布
                    Log.d(TAG, "onCancel: 取消发布");
                    cancelUpload();
                }
            });
            progressDialog.setMax(100);
        }
        progressDialog.setProgress(progress);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }


    private void hideUploadProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    private VodImageUploadAuth vodImageUploadAuth = null;
    private VodVideoUploadAuth vodVideoUploadAuth = null;

    /**
     * 上传图片
     */
    private void startImageUpload() {
        showUploadProgress(0, false);
        Log.e(TAG, "startImageUpload");
        RequestParams params = new RequestParams();
        params.addFormDataPart("imageType", "cover");
        HttpRequest.get(AlivcLittleServerApiConstants.URL_GET_IMAGE_UPLOAD_AUTH,
                params, new StringHttpRequestCallback() {
                    @Override
                    protected void onSuccess(String s) {
                        super.onSuccess(s);
                        if (!isPublishing) {
                            Log.w(TAG, "onSuccess: 已取消上传");
                            return;
                        }
                        Log.d(AliyunTag.TAG, s);
                        vodImageUploadAuth = VodImageUploadAuth.getImageTokenInfo(s);
                        if (vodImageUploadAuth != null && mComposeClient != null) {
                            int rv = mComposeClient.uploadImageWithVod(mThumbnailPath, vodImageUploadAuth.getUploadAddress(), vodImageUploadAuth.getUploadAuth(), mUploadCallback);
                            if (rv < 0) {
                                Log.d(AliyunTag.TAG, "上传参数错误 video path : " + mComposeOutputPath + " thumbnailk : " + mThumbnailPath);
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast(PublishActivityBak.this, "image 上传错误");
                                        hideUploadProgress();
                                        mPublish.setEnabled(true);
                                    }
                                });
                            } else {
                                mIsUpload = true;
                            }

                        } else {
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast(PublishActivityBak.this, "image upload auth info 获取错误");
                                    hideUploadProgress();
                                    mPublish.setEnabled(true);
                                }
                            });
                            Log.e(AliyunTag.TAG, "Get image upload auth info failed");
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String msg) {
                        super.onFailure(errorCode, msg);
                        if (!isPublishing) {
                            Log.w(TAG, "onSuccess: 已取消上传");
                            return;
                        }
                        Log.e(AliyunTag.TAG, "Get image upload auth info failed, errorCode:" + errorCode + ", msg:" + msg);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(PublishActivityBak.this, "image upload auth info 获取错误");
                                hideUploadProgress();
                                mPublish.setEnabled(true);
                            }
                        });
                    }
                });
    }

    /**
     * 上传视频
     */
    private void startVideoUpload() {
        RequestParams params = new RequestParams();
        params.addFormDataPart("title", "默认描述");
        params.addFormDataPart("fileName", mComposeFileName);
        HttpRequest.get(AlivcLittleServerApiConstants.URL_GET_VIDEO_UPLOAD_AUTH, params, new StringHttpRequestCallback() {
            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                if (!isPublishing) {
                    Log.w(TAG, "onSuccess: 已取消上传");
                    return;
                }
                vodVideoUploadAuth = VodVideoUploadAuth.getVideoTokenInfo(s);
                if (vodVideoUploadAuth != null && mComposeClient != null) {
                    int rv = mComposeClient.uploadVideoWithVod(mComposeOutputPath, vodVideoUploadAuth.getUploadAddress(), vodVideoUploadAuth.getUploadAuth(), mUploadCallback);
                    if (rv < 0) {
                        Log.d(AliyunTag.TAG, "上传参数错误 video path : " + mComposeOutputPath + " thumbnailk : " + mThumbnailPath);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(PublishActivityBak.this, "video 上传错误");
                                hideUploadProgress();
                                mPublish.setEnabled(true);
                            }
                        });
                    } else {
                        mIsUpload = true;
                    }

                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(PublishActivityBak.this, "Get video upload auth failed");
                            hideUploadProgress();
                            mPublish.setEnabled(true);
                        }
                    });
                    Log.e(AliyunTag.TAG, "Get video upload auth info failed");
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                if (!isPublishing) {
                    Log.w(TAG, "onSuccess: 已取消上传");
                    return;
                }
                Log.e(AliyunTag.TAG, "Get video upload auth failed, errorCode:" + errorCode + ", msg:" + msg);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(PublishActivityBak.this, "Get video upload auth failed");
                        hideUploadProgress();
                        mPublish.setEnabled(true);
                    }
                });
            }
        });
    }

    private final AliyunVodCompose.AliyunIVodUploadCallBack mUploadCallback = new AliyunVodCompose.AliyunIVodUploadCallBack() {

        @Override
        public void onUploadSucceed() {
            if (!isPublishing) {
                Log.w(TAG, "onSuccess: 已取消上传");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mComposeClient != null && mComposeClient.getState() == AliyunIVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                        //如果是图片上传回调，继续视频上传
                        startVideoUpload();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("videoId", vodVideoUploadAuth.getVideoId());
                        intent.putExtra("imageUrl", vodImageUploadAuth.getImageURL());
                        intent.putExtra("describe", "默认描述");
                        intent.putExtra("publishType", String.valueOf(findViewById(vRgPublishType.getCheckedRadioButtonId()).getTag()));
                        setResult(RESULT_OK, intent);
                        hideUploadProgress();
                        if (isQuestionMode) {
                            ToastUtil.showToast(getApplicationContext(), "提交成功");
                        } else {
                            ToastUtil.showToast(getApplicationContext(), "上传成功");
                        }
                        finish();
                    }
                }
            });

        }

        @Override
        public void onUploadFailed(String code, String message) {
            Log.e(AliyunTag.TAG, "onUploadFailed, errorCode:" + code + ", msg:" + message);
            if (!isPublishing) {
                Log.w(TAG, "onSuccess: 已取消上传");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideUploadProgress();
                    mPublish.setEnabled(true);
                }
            });

        }

        @Override
        public void onUploadProgress(final long uploadedSize, final long totalSize) {
            if (!isPublishing) {
                Log.w(TAG, "onSuccess: 已取消上传");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mComposeClient == null) {
                        return;
                    }
                    int progress = 0;
                    if (mComposeClient.getState() == AliyunVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                        progress = (int) ((uploadedSize * 100) / (totalSize + videoSize));
                    } else if (mComposeClient.getState() == AliyunVodCompose.AliyunComposeState.STATE_VIDEO_UPLOADING) {
                        progress = (int) (((uploadedSize + imageSize) * 100) / (totalSize + imageSize));
                    }
                    showUploadProgress(progress, true);
                }
            });

        }

        @Override
        public void onUploadRetry(String code, String message) {

        }

        @Override
        public void onUploadRetryResume() {

        }

        @Override
        public void onUploadTokenExpired() {
            if (!isPublishing) {
                Log.w(TAG, "onSuccess: 已取消上传");
                return;
            }
            if (mComposeClient == null) {
                return;
            }
            if (mComposeClient.getState() == AliyunIVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                startImageUpload();
            } else if (mComposeClient.getState() == AliyunIVodCompose.AliyunComposeState.STATE_VIDEO_UPLOADING) {
                refreshVideoUpload(vodVideoUploadAuth.getVideoId());
            }
        }
    };

    /**
     * 刷新视频凭证
     *
     * @param videoId
     */
    private void refreshVideoUpload(String videoId) {

        RequestParams params = new RequestParams();
        params.addFormDataPart("videoId", videoId);
        HttpRequest.get(AlivcLittleServerApiConstants.URL_REFRESH_VIDEO_UPLOAD_AUTH, params, new StringHttpRequestCallback() {
            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);

                if (!isPublishing) {
                    Log.w(TAG, "onSuccess: 已取消上传");
                    return;
                }

                RefreshVodVideoUploadAuth tokenInfo = RefreshVodVideoUploadAuth.getReVideoTokenInfo(s);
                if (tokenInfo != null && mComposeClient != null) {
                    int rv = mComposeClient.refreshWithUploadAuth(tokenInfo.getUploadAuth());
                    if (rv < 0) {
                        Log.d(AliyunTag.TAG, "上传参数错误 video path : " + mComposeOutputPath + " thumbnailk : " + mThumbnailPath);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(PublishActivityBak.this, "刷新凭证错误");
                            }
                        });
                    } else {
                        mIsUpload = true;
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(PublishActivityBak.this, "刷新凭证获取错误");
                        }
                    });
                    Log.e(AliyunTag.TAG, "Get video upload auth info failed");
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }


}
