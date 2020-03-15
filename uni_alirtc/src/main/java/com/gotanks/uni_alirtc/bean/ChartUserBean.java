package com.gotanks.uni_alirtc.bean;

import org.webrtc.sdk.SophonSurfaceView;

import java.util.Objects;

public class ChartUserBean {

    public String mUserId;

    public ChartUserBean(String mUserId) {
        this.mUserId = mUserId;
    }

    public ChartUserBean() {
    }

    public SophonSurfaceView mCameraSurface;
    public SophonSurfaceView mScreenSurface;
    public String mUserName;
    //是否相机镜像
    public boolean mIsCameraFlip;
    //是否屏幕镜像
    public boolean mIsScreenFlip;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChartUserBean that = (ChartUserBean) o;

        return Objects.equals(mUserId, that.mUserId);
    }

    @Override
    public int hashCode() {
        return mUserId != null ? mUserId.hashCode() : 0;
    }
}
