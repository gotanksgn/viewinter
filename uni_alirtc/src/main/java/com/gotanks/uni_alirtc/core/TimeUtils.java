package com.gotanks.uni_alirtc.core;

import android.annotation.SuppressLint;

/**
 * Created by MT on 2020-03-14.
 */
public class TimeUtils {
    @SuppressLint("DefaultLocale")
    public static String formatUse(int sec) {
//        int hours = sec / (3600);
        int mins = sec / 60;
        int ses = sec % 60;
        return String.format("%02d:%02d", mins, ses);
    }
}
