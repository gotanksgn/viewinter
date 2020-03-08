/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.gotanks.uni_alisv.editor.effects.control;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gotanks.uni_alisv.editor.effects.cover.CoverChooserView;
import com.gotanks.uni_alisv.editor.effects.filter.AnimationFilterChooserView;
import com.gotanks.uni_alisv.editor.effects.filter.ColorFilterChooserView;
import com.gotanks.uni_alisv.editor.util.SharedPreferenceUtils;
import com.gotanks.uni_alisv.editor.view.AlivcEditView;
import com.gotanks.uni_alisv.editor.viewoperate.ViewOperator;

/**
 * 底部导航栏的 view stack
 */
public class ViewStack {

    private final static String TAG = ViewStack.class.getName();
    private AlivcEditView rootView;
    private ViewOperator mViewOperator;
    private final Context mContext;
    private EditorService mEditorService;
    private OnEffectChangeListener mOnEffectChangeListener;

    private ColorFilterChooserView mColorFilterCHoosrView;
    private AnimationFilterChooserView mAnimationChooserView;//特效
    private OnEffectActionLister mOnEffectActionLister;
    private AlivcEditView.PlayerListener mPlayerListener;
    /**
     * 封面选择view
     */
    private CoverChooserView mCoverChooserView;

    public ViewStack(Context context, AlivcEditView editView, ViewOperator viewOperator) {

        mContext = context;
        rootView = editView;
        mViewOperator = viewOperator;
    }

    public void setActiveIndex(int value) {

        UIEditorPage index = UIEditorPage.get(value);

        switch (index) {
            case FILTER:
                // 颜色滤镜
                if (mColorFilterCHoosrView == null) {
                    mColorFilterCHoosrView = new ColorFilterChooserView(mContext);
                }
                mViewOperator.showBottomView(mColorFilterCHoosrView);
                break;
            case FILTER_EFFECT:
                // 特效滤镜
                mAnimationChooserView = new AnimationFilterChooserView(mContext);
                mAnimationChooserView.setEditorService(mEditorService);
                mAnimationChooserView.setFirstShow(SharedPreferenceUtils.isAnimationEffectFirstShow(mContext));
                mAnimationChooserView.setOnEffectChangeListener(mOnEffectChangeListener);
                mAnimationChooserView.setPlayerListener(mPlayerListener);
                mAnimationChooserView.addThumbView(rootView.getThumbLineBar());
                mAnimationChooserView.setOnEffectActionLister(mOnEffectActionLister);
                mViewOperator.showBottomView(mAnimationChooserView);
                SharedPreferenceUtils.setAnimationEffectFirstShow(mContext, false);
                break;
            case COVER:
                // show cover select view
                mCoverChooserView = new CoverChooserView(mContext);
                mCoverChooserView.setOnEffectActionLister(mOnEffectActionLister);
                mCoverChooserView.addThumbView(rootView.getThumbLineBar());
                boolean isFirstShow = SharedPreferenceUtils.isCoverViewFirstShow(mContext);
                if (isFirstShow) {
                    SharedPreferenceUtils.setCoverViewFirstShow(mContext, false);
                }
                mCoverChooserView.setFirstShow(isFirstShow);
                mViewOperator.showBottomView(mCoverChooserView);

                break;

            default:
                Log.d(TAG, "点击编辑效果，方法setActiveIndex未匹配");
                return;
        }
        BaseChooser bottomView = mViewOperator.getBottomView();
        if (bottomView != null && bottomView.isPlayerNeedZoom()) {
            //缩放
            rootView.setPasterDisplayScale(ViewOperator.SCALE_SIZE);
        }

    }

    /**
     * 设置LayoutParams
     *
     * @param baseChooser view
     */
    private void setLayoutParams(BaseChooser baseChooser) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        baseChooser.setLayoutParams(layoutParams);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

        }
    }

    public void setEditorService(EditorService editorService) {
        mEditorService = editorService;
    }

    public void setEffectChange(OnEffectChangeListener onEffectChangeListener) {
        mOnEffectChangeListener = onEffectChangeListener;
    }

    public void setOnEffectActionLister(OnEffectActionLister effectActionLister) {
        mOnEffectActionLister = effectActionLister;
    }

    /**
     * 设置view的可见状态, 会在activity的onStart和onStop中调用
     *
     * @param isVisible true: 可见, false: 不可见
     */
    public void setVisibleStatus(boolean isVisible) {

    }

    /**
     * 播放时间回调器
     *
     * @param playerListener PlayerListener
     */
    public void setPlayerListener(AlivcEditView.PlayerListener playerListener) {
        mPlayerListener = playerListener;
    }
}
