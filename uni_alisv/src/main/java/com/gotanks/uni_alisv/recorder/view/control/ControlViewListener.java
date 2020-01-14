package com.gotanks.uni_alisv.recorder.view.control;

/**
 * @author zsy_18 data:2018/7/31
 */
public interface ControlViewListener {
    /**
     * 返回事件
     */
    void onBackClick();

    /**
     * 下一个事件
     */
    void onNextClick();

    /**
     * 摄像头转换事件
     */
    void onCameraSwitch();

    /**
     * 闪光灯模式
     * @param flashType
     */
    void onLightSwitch(FlashType flashType);

    /**
     * 播放速率选择
     *
     * @param rate
     */
    void onRateSelect(float rate);

    /**
     * 准备录制视频事件
     *
     * @param isCancel true 取消准备，false开始准备
     */
    void onReadyRecordClick(boolean isCancel);

    /**
     * 开始录制视频事件
     */
    void onStartRecordClick();

    /**
     * 停止录制视频事件
     */
    void onStopRecordClick();

    /**
     * 删除点击事件
     */
    void onDeleteClick();

}