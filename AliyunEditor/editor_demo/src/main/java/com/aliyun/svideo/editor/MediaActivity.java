/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.aliyun.svideo.editor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.utils.ToastUtil;
import com.aliyun.demo.crop.AliyunImageCropActivity;
import com.aliyun.demo.crop.AliyunVideoCropActivity;
import com.aliyun.demo.crop.bean.AlivcCropInputParam;
import com.aliyun.demo.crop.bean.AlivcCropOutputParam;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.svideo.base.widget.ProgressDialog;
import com.aliyun.svideo.common.utils.FastClickUtil;
import com.aliyun.svideo.common.utils.ThreadUtils;
import com.aliyun.svideo.common.utils.ToastUtils;
import com.aliyun.svideo.editor.bean.AlivcEditInputParam;
import com.aliyun.svideo.editor.editor.EditorActivity;
import com.aliyun.svideo.media.GalleryDirChooser;
import com.aliyun.svideo.media.GalleryMediaChooser;
import com.aliyun.svideo.media.JsonExtend.JSONSupportImpl;
import com.aliyun.svideo.media.MediaDir;
import com.aliyun.svideo.media.MediaImageLoader;
import com.aliyun.svideo.media.MediaInfo;
import com.aliyun.svideo.media.MediaStorage;
import com.aliyun.svideo.media.SelectedMediaAdapter;
import com.aliyun.svideo.media.SelectedMediaViewHolder;
import com.aliyun.svideo.media.ThumbnailGenerator;
import com.aliyun.svideo.sdk.external.struct.common.CropKey;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.duanqu.transcode.NativeParser;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 编辑模块的media选择Activity
 */
public class MediaActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CODE_VIDEO_CROP = 1;
    private static final int REQUEST_CODE_IMAGE_CROP = 2;
    private static final int IMAGE_DURATION = 3000;//图片代表的时长

    private final static String TAG = MediaActivity.class.getSimpleName();
    private MediaStorage storage;
    private ProgressDialog progressDialog;
    private ThumbnailGenerator thumbnailGenerator;
    private GalleryMediaChooser galleryMediaChooser;
    private TextView mTvTotalDuration;
    private ImageButton back;
    private TextView title;

    private SelectedMediaAdapter mSelectedVideoAdapter;
    private Transcoder mTransCoder;
    private MediaInfo mCurrMediaInfo;
    private int mCropPosition;
    private boolean mIsReachedMaxDuration = false;

    private Button mBtnNextStep;

    private AlivcEditInputParam mInputParam;

    /**
     * 页面恢复时保存mBundleSaveMedias对象的key
     * 保存时 {@link #onSaveInstanceState(Bundle)}
     * 恢复时 {@link #onRestoreInstanceState(Bundle)}
     */
    private static final String BUNDLE_KEY_SAVE_MEDIAS = "bundle_key_save_transcoder";

    /**
     * 页面恢复时保存选择Medias对象
     */
    private ArrayList<MediaInfo> mBundleSaveMedias;
    /**
     * 转码结果文件，用于导入编辑 & onDestroy删除缓存文件
     */
    private List<MediaInfo> mTranscodeResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_media_activity_media_import);
        initData();
        init();
    }

    private void initData() {
        Intent intent = getIntent();
        int mFrameRate = intent.getIntExtra(AlivcEditInputParam.INTENT_KEY_FRAME, 30);
        int mGop = intent.getIntExtra(AlivcEditInputParam.INTENT_KEY_GOP, 250 );
        int mRatio = intent.getIntExtra(AlivcEditInputParam.INTENT_KEY_RATION_MODE, AlivcEditInputParam.RATIO_MODE_9_16);
        VideoQuality mVideoQuality = (VideoQuality)intent.getSerializableExtra(AlivcEditInputParam.INTENT_KEY_QUALITY);
        if (mVideoQuality == null) {
            mVideoQuality = VideoQuality.HD;
        }
        int mResolutionMode = intent.getIntExtra(AlivcEditInputParam.INTENT_KEY_RESOLUTION_MODE, AlivcEditInputParam.RESOLUTION_720P );
        VideoCodecs mVideoCodec = (VideoCodecs)intent.getSerializableExtra(AlivcEditInputParam.INTENT_KEY_CODEC);
        if (mVideoCodec == null) {
            mVideoCodec = VideoCodecs.H264_HARDWARE;
        }
        int mCrf = intent.getIntExtra(AlivcEditInputParam.INTETN_KEY_CRF, 23 );
        float mScaleRate = intent.getFloatExtra(AlivcEditInputParam.INTETN_KEY_SCANLE_RATE, 1.0f );
        VideoDisplayMode mScaleMode = (VideoDisplayMode)intent.getSerializableExtra(AlivcEditInputParam.INTETN_KEY_SCANLE_MODE);
        if (mScaleMode == null) {
            mScaleMode = VideoDisplayMode.FILL;
        }
        boolean mHasTailAnimation = intent.getBooleanExtra(AlivcEditInputParam.INTENT_KEY_TAIL_ANIMATION, false );
        boolean canReplaceMusic = intent.getBooleanExtra(AlivcEditInputParam.INTENT_KEY_REPLACE_MUSIC, true );
        ArrayList<MediaInfo> mediaInfos = intent.getParcelableArrayListExtra(AlivcEditInputParam.INTENT_KEY_MEDIA_INFO);
        boolean hasWaterMark = intent.getBooleanExtra(AlivcEditInputParam.INTENT_KEY_WATER_MARK, false );
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
        .build();
    }
    private void init() {
        mTransCoder = new Transcoder();
        mTransCoder.init(this);
        mTransCoder.setTransCallback(new Transcoder.TransCallback() {
            @Override
            public void onError(Throwable e, final int errorCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        switch (errorCode) {
                        case AliyunErrorCode.ALIVC_SVIDEO_ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                            ToastUtil.showToast(MediaActivity.this, R.string.alivc_crop_video_tip_not_supported_audio);
                            break;
                        case AliyunErrorCode.ALIVC_SVIDEO_ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                            ToastUtil.showToast(MediaActivity.this, R.string.alivc_crop_video_tip_crop_failed);
                            break;
                        case AliyunErrorCode.ALIVC_COMMON_UNKNOWN_ERROR_CODE:
                        default:
                            ToastUtil.showToast(MediaActivity.this, R.string.alivc_crop_video_tip_crop_failed);
                        }
                    }
                });

            }
            @Override
            public void onProgress(int progress) {
                if (progressDialog != null) {
                    progressDialog.setProgress(progress);
                }
            }
            @Override
            public void onComplete(List<MediaInfo> resultVideos) {
                Log.d("TRANCODE", "ONCOMPLETED, dialog : " + (progressDialog == null));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                mInputParam.setMediaInfos((ArrayList<MediaInfo>)resultVideos);
                EditorActivity.startEdit(MediaActivity.this, mInputParam  );
            }

            @Override
            public void onCancelComplete() {
                //取消完成
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnNextStep.setEnabled(true);
                    }
                });
            }
        });
        mBtnNextStep = (Button)findViewById(R.id.btn_next_step);
        RecyclerView galleryView = (RecyclerView) findViewById(R.id.gallery_media);
        title = (TextView)findViewById(R.id.gallery_title);
        title.setText(R.string.alivc_media_gallery_all_media);
        back = (ImageButton)findViewById(R.id.gallery_closeBtn);
        back.setOnClickListener(this);
        storage = new MediaStorage(this, new JSONSupportImpl());
        thumbnailGenerator = new ThumbnailGenerator(this);
        GalleryDirChooser galleryDirChooser = new GalleryDirChooser(this, findViewById(R.id.topPanel),
                thumbnailGenerator, storage);
        galleryMediaChooser = new GalleryMediaChooser(galleryView, galleryDirChooser, storage, thumbnailGenerator);
        storage.setSortMode(MediaStorage.SORT_MODE_MERGE);
        try {
            storage.startFetchmedias();
        } catch (SecurityException e) {
            ToastUtils.show(MediaActivity.this, getResources().getString(R.string.alivc_common_no_permission));
        }
        storage.setOnMediaDirChangeListener(new MediaStorage.OnMediaDirChange() {
            @Override
            public void onMediaDirChanged() {
                MediaDir dir = storage.getCurrentDir();
                if (dir.id == -1) {
                    title.setText(getString(R.string.alivc_media_gallery_all_media));
                } else {
                    title.setText(dir.dirName);
                }
                galleryMediaChooser.changeMediaDir(dir);
            }
        });
        storage.setOnCompletionListener(new MediaStorage.OnCompletion() {
            @Override
            public void onCompletion() {
                MediaDir dir = storage.getCurrentDir();
                if (dir != null && dir.id != -1) {
                    //选择了媒体目录时，在加载完成的时候需要刷新一次
                    title.setText(dir.dirName);
                    galleryMediaChooser.changeMediaDir(dir);
                }
            }
        });
        storage.setOnCurrentMediaInfoChangeListener(new MediaStorage.OnCurrentMediaInfoChange() {
            @Override
            public void onCurrentMediaInfoChanged(MediaInfo info) {

                Log.i(TAG, "log_editor_video_path : " + info.filePath);
                MediaInfo infoCopy = new MediaInfo();
                infoCopy.addTime = info.addTime;
                infoCopy.mimeType = info.mimeType;
                String outputPath = null;
                if (info.mimeType.startsWith("image")) {
                    if (info.filePath.endsWith("gif") || info.filePath.endsWith("GIF")) {
                        NativeParser parser = new NativeParser();
                        parser.init(info.filePath);
                        int frameCount = 0;
                        try {
                            frameCount = Integer.parseInt(parser.getValue(NativeParser.VIDEO_FRAME_COUNT));
                        } catch (Exception e) {
                            ToastUtils.show(MediaActivity.this, R.string.alivc_editor_error_tip_play_video_error);
                            return;
                        } finally {
                            parser.release();
                            parser.dispose();
                        }
                        //当gif动图为一帧的时候当作图片处理，否则当作视频处理
                        if (frameCount > 1) {
                            infoCopy.mimeType = "video";
                            infoCopy.duration = Integer.parseInt(parser.getValue(NativeParser
                                                                 .VIDEO_DURATION)) / 1000;
                        } else {
                            infoCopy.duration = IMAGE_DURATION;
                        }

                    } else {
                        infoCopy.duration = IMAGE_DURATION;
                    }

                } else {
                    infoCopy.duration = info.duration;
                }
                if (outputPath != null) {
                    infoCopy.filePath = outputPath;//info.filePath;
                } else {
                    infoCopy.filePath = info.filePath;
                }
                infoCopy.id = info.id;
                infoCopy.isSquare = info.isSquare;
                infoCopy.thumbnailPath = info.thumbnailPath;
                infoCopy.title = info.title;
                infoCopy.type = info.type;

                mSelectedVideoAdapter.addMedia(infoCopy);
                mTransCoder.addMedia(infoCopy);
            }
        });
        RecyclerView rvSelectedVideo = (RecyclerView) findViewById(R.id.rv_selected_video);
        mTvTotalDuration = (TextView)findViewById(R.id.tv_duration_value);
        mSelectedVideoAdapter = new SelectedMediaAdapter(new MediaImageLoader(this), 5 * 60 * 1000);//最大时长5分钟
        rvSelectedVideo.setAdapter(mSelectedVideoAdapter);
        rvSelectedVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTvTotalDuration.setText(convertDuration2Text(0));
        mTvTotalDuration.setActivated(false);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //首先回调的方法 返回int表示是否监听该方向
                int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//拖拽
                int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//侧滑删除
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                //滑动事件
                mSelectedVideoAdapter.swap((SelectedMediaViewHolder)viewHolder, (SelectedMediaViewHolder)target);
                mTransCoder.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }
        });

        itemTouchHelper.attachToRecyclerView(rvSelectedVideo);
        mSelectedVideoAdapter.setItemViewCallback(new SelectedMediaAdapter.OnItemViewCallback() {
            @Override
            public void onItemPhotoClick(MediaInfo info, int position) {
                mCurrMediaInfo = info;
                mCropPosition = position;
                if (FastClickUtil.isFastClickActivity(MediaActivity.class.getSimpleName())) {
                    return;
                }
                if (info.filePath.endsWith("gif") || info.filePath.endsWith("GIF")) {
                    Toast.makeText(MediaActivity.this, R.string.alivc_crop_media_gif_not_support, Toast.LENGTH_SHORT).show();
                    return;
                }
                AlivcCropInputParam cropInputParam = new AlivcCropInputParam.Builder()
                .setRatioMode(mInputParam.getRatio())
                .setResolutionMode(mInputParam.getResolutionMode())
                .setCropMode(mInputParam.getScaleMode())
                .setFrameRate(mInputParam.getFrameRate())
                .setGop(mInputParam.getGop())
                .setQuality(mInputParam.getVideoQuality())
                .setVideoCodecs(mInputParam.getVideoCodec())
                .setAction(CropKey.ACTION_SELECT_TIME)
                .build();
                if (info.mimeType.startsWith("video")) {
                    cropInputParam.setPath(info.filePath);
                    AliyunVideoCropActivity.startVideoCropForResult(MediaActivity.this, cropInputParam, REQUEST_CODE_VIDEO_CROP );
                } else if (info.mimeType.startsWith("image")) {
                    cropInputParam.setPath(info.filePath);
                    AliyunImageCropActivity.startImageCropForResult(MediaActivity.this, cropInputParam, REQUEST_CODE_IMAGE_CROP);
                }
            }

            @Override
            public void onItemDeleteClick(MediaInfo info) {
                mTransCoder.removeMedia(info);
            }

            @Override
            public void onDurationChange(long currDuration, boolean isReachedMaxDuration) {
                mTvTotalDuration.setText(convertDuration2Text(currDuration));
                mTvTotalDuration.setActivated(isReachedMaxDuration);
                mIsReachedMaxDuration = isReachedMaxDuration;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            AlivcCropOutputParam outputParam = (AlivcCropOutputParam)data.getSerializableExtra(AlivcCropOutputParam.RESULT_KEY_OUTPUT_PARAM);
            if (outputParam == null) {
                return;
            }
            String path = outputParam.getOutputPath();
            switch (requestCode) {
            case REQUEST_CODE_VIDEO_CROP:

                long duration = outputParam.getDuration();
                long startTime = outputParam.getStartTime();
                if (!TextUtils.isEmpty(path) && duration > 0 && mCurrMediaInfo != null) {
                    mSelectedVideoAdapter.changeDurationPosition(mCropPosition, duration);
                    int index = mTransCoder.removeMedia(mCurrMediaInfo);
                    mCurrMediaInfo.filePath = path;
                    mCurrMediaInfo.startTime = startTime;
                    mCurrMediaInfo.duration = (int)duration;
                    mTransCoder.addMedia(index, mCurrMediaInfo);
                }
                break;
            case REQUEST_CODE_IMAGE_CROP:
                if (!TextUtils.isEmpty(path) && mCurrMediaInfo != null) {
                    int index = mTransCoder.removeMedia(mCurrMediaInfo);
                    mCurrMediaInfo.filePath = path;
                    mTransCoder.addMedia(index, mCurrMediaInfo);
                }
                break;
            default:
                break;
            }

        }
    }
    private String convertDuration2Text(long duration) {
        int sec = Math.round(((float)duration) / 1000);
        int hour = sec / 3600;
        int min = (sec % 3600) / 60;
        sec = (sec % 60);
        return String.format(getString(R.string.alivc_media_video_duration),
                             hour,
                             min,
                             sec);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTranscodeFile();
        storage.saveCurrentDirToCache();
        storage.cancelTask();
        mTransCoder.release();
        thumbnailGenerator.cancelAllTask();
    }

    /**
     * 删除临时文件
     */
    private void deleteTranscodeFile() {
        if (mTranscodeResult != null) {
            ThreadUtils.runOnSubThread(new Runnable() {
                @Override
                public void run() {
                    for (MediaInfo mediaInfo : mTranscodeResult) {
                        if (mediaInfo.filePath.contains("_transcode")) {
                            File file = new File(mediaInfo.filePath);
                            if (file.exists()) {
                                //noinspection ResultOfMethodCallIgnored
                                file.delete();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //恢复选择的medias
        if (mBundleSaveMedias != null) {
            for (MediaInfo mediaInfo : mBundleSaveMedias) {
                mSelectedVideoAdapter.addMedia(mediaInfo);
                mTransCoder.addMedia(mediaInfo);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //每次退到后台清空save的值，避免正常时也会恢复
        mBundleSaveMedias = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_KEY_SAVE_MEDIAS, mTransCoder.getOriginalVideos());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<MediaInfo> data = savedInstanceState.getParcelableArrayList(BUNDLE_KEY_SAVE_MEDIAS);
        if (data != null && data.size() != 0) {
            mBundleSaveMedias = data;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
        } else if (v.getId() == R.id.btn_next_step) {//点击下一步

            if (FastClickUtil.isFastClick()) {
                return;
            }
            if (mIsReachedMaxDuration) {
                ToastUtil.showToast(MediaActivity.this, R.string.alivc_media_message_max_duration_import);
                return;
            }
            //对于大于720P的视频需要走转码流程

            int videoCount = mTransCoder.getVideoCount();
            if (videoCount > 0 && (progressDialog == null || !progressDialog.isShowing())) {
                progressDialog = ProgressDialog.show(this, null, getResources().getString(R.string.alivc_media_wait));
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setOnCancelListener(new OnCancelListener(this));
                mTransCoder.transcode(mInputParam.getScaleMode());
            } else {
                ToastUtil.showToast(this, R.string.alivc_media_please_select_video);
            }
        }
    }

    /**
     * progressDialog cancel listener
     */
    private static class OnCancelListener implements DialogInterface.OnCancelListener {

        private WeakReference<MediaActivity> weakReference;

        public OnCancelListener(MediaActivity mediaActivity) {
            weakReference = new WeakReference<>(mediaActivity);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            MediaActivity mediaActivity = weakReference.get();
            if (mediaActivity != null) {
                mediaActivity.mBtnNextStep.setEnabled(false);//为了防止未取消成功的情况下就开始下一次转码，这里在取消转码成功前会禁用下一步按钮
                mediaActivity.mTransCoder.cancel();
            }
        }
    }

    public static void startImport(Context context, AlivcEditInputParam param) {
        if (param == null) {
            return;
        }
        Intent intent = new Intent(context, MediaActivity.class);
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
        intent.putParcelableArrayListExtra(AlivcEditInputParam.INTENT_KEY_MEDIA_INFO, param.getMediaInfos());
        context.startActivity(intent);
    }

}
