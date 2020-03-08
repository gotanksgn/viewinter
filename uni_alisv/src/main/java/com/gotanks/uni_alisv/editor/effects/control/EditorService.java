/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.gotanks.uni_alisv.editor.effects.control;

import android.graphics.Paint;

import com.aliyun.editor.TimeEffectType;
import com.aliyun.svideo.sdk.external.struct.AliyunIClipConstructor;
import com.gotanks.uni_alisv.editor.effects.transition.TransitionEffectCache;

import java.util.HashMap;
import java.util.Map;

public class EditorService {
    private Map<UIEditorPage, Integer> mMap = new HashMap<>();
    private Paint mPaint;
    private TransitionEffectCache mTransitionEffectCache;
    private EffectInfo lastTimeEffectInfo;

    public void addTabEffect(UIEditorPage uiEditorPage, int id) {
        mMap.put(uiEditorPage, id);
    }

    public int getEffectIndex(UIEditorPage uiEditorPage) {
        if (mMap.containsKey(uiEditorPage)) {
            return mMap.get(uiEditorPage);
        } else {
            return 0;
        }
    }

    public TransitionEffectCache getTransitionEffectCache(AliyunIClipConstructor clipConstructor) {
        if (mTransitionEffectCache == null) {
            mTransitionEffectCache = TransitionEffectCache.newInstance(clipConstructor);
        } else {
            mTransitionEffectCache.editor();
        }
        return mTransitionEffectCache;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }
}
