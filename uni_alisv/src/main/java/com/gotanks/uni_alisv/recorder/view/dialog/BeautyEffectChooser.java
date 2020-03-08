package com.gotanks.uni_alisv.recorder.view.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;

import com.aliyun.svideo.base.widget.beauty.BeautyParams;
import com.aliyun.svideo.base.widget.beauty.enums.BeautyLevel;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyDetailClickListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyFaceItemSeletedListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyModeChangeListener;
import com.gotanks.uni_alisv.R;
import com.gotanks.uni_alisv.recorder.view.effects.face.AlivcBeautyFaceFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 美颜模块整体dialog, 包含滤镜, 美颜, 美肌
 *
 * @author xlx
 */
public class BeautyEffectChooser extends BasePageChooser {

    /**
     * 美颜参数
     */
    private BeautyParams beautyParams;

    /**
     * 美颜fragment
     */
    private AlivcBeautyFaceFragment beautyFaceFragment;

    /**
     * 美颜微调点击listener
     */
    private OnBeautyDetailClickListener onBeautyFaceDetailClickListener;

    /**
     * 美颜item选中listener
     */
    private OnBeautyFaceItemSeletedListener onItemSeletedListener;
    /**
     * 美颜模式切换listener (普通or高级)
     */
    private OnBeautyModeChangeListener onBeautyModeChangeListener;

    /**
     * 当前viewpager的选中下标
     */
    public int currentTabPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //适配有底部导航栏的手机，在full的style下会盖住部分视图的bug
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.QUDemoFullStyle);
    }

    @Override
    public List<Fragment> createPagerFragmentList() {
        List<Fragment> fragments = new ArrayList<>();

        beautyFaceFragment = new AlivcBeautyFaceFragment();

        beautyFaceFragment.setTabTitle(getResources().getString(R.string.alivc_base_beauty));
        fragments.add(beautyFaceFragment);

        initBeautyFace();
        // dialog的tab切换监听
        setOnUpdatePageSelectedListener(new OnUpdatePageSelectedListener() {
            @Override
            public void onPageSelected(int position) {
                currentTabPosition = position;
                beautyFaceFragment.updatePageIndex(position);
            }
        });
        return fragments;
    }


    private void initBeautyFace() {
        beautyFaceFragment.setBeautyParams(beautyParams);

        // 档位选择
        beautyFaceFragment.setOnBeautyFaceItemSeletedListener(new OnBeautyFaceItemSeletedListener() {
            @Override
            public void onNormalSelected(int postion, BeautyLevel beautyLevel) {
                if (onItemSeletedListener != null) {
                    onItemSeletedListener.onNormalSelected(postion, beautyLevel);
                }
            }

            @Override
            public void onAdvancedSelected(int postion, BeautyLevel beautyLevel) {
                if (onItemSeletedListener != null) {
                    onItemSeletedListener.onAdvancedSelected(postion, beautyLevel);
                }
            }
        });

        // 高级or普通模式切换
        beautyFaceFragment.setOnBeautyModeChangeListener(new OnBeautyModeChangeListener() {
            @Override
            public void onModeChange(RadioGroup group, int checkedId) {
                if (onBeautyModeChangeListener != null) {
                    onBeautyModeChangeListener.onModeChange(group, checkedId);
                }
            }
        });

        // 高级详情
        beautyFaceFragment.setOnBeautyDetailClickListener(new OnBeautyDetailClickListener() {
            @Override
            public void onDetailClick() {
                if (onBeautyFaceDetailClickListener != null) {
                    onBeautyFaceDetailClickListener.onDetailClick();
                }
            }
        });
    }


    /**
     * 设置美颜美肌参数
     *
     * @param beautyParams 美颜美肌参数
     */
    public void setBeautyParams(BeautyParams beautyParams) {
        this.beautyParams = beautyParams;
    }

    /**
     * 美颜微调按钮点击listener
     *
     * @param listener OnBeautyDetailClickListener
     */
    public void setOnBeautyFaceDetailClickListener(
            OnBeautyDetailClickListener listener) {
        this.onBeautyFaceDetailClickListener = listener;
    }


    /**
     * 设置美颜item点击listener
     *
     * @param listener OnBeautyFaceItemSeletedListener
     */
    public void setOnBeautyFaceItemSeletedListener(OnBeautyFaceItemSeletedListener listener) {
        this.onItemSeletedListener = listener;
    }


    /**
     * 美颜模式切换listener
     *
     * @param listener OnBeautyModeChangeListener
     */
    public void setOnBeautyModeChangeListener(
            OnBeautyModeChangeListener listener) {
        this.onBeautyModeChangeListener = listener;
    }


    public int getCurrentTabIndex() {
        return currentTabPosition;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}
