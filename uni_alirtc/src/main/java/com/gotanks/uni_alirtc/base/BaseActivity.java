package com.gotanks.uni_alirtc.base;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gotanks.uni_alirtc.R;
import com.gotanks.uni_alirtc.utils.PermissionUtils;
import com.gotanks.uni_alirtc.utils.ThreadUtils;


/**
 * 权限基类
 */
public class BaseActivity extends AppCompatActivity {


    /**
     * 请求权限
     */
    public void setUpSplash() {

        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestPermission();
            }
        }, 1000);
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private PermissionUtils.PermissionGrant mGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {

        }

        @Override
        public void onPermissionCancel() {
            Toast.makeText(BaseActivity.this.getApplicationContext(), getString(R.string.alirtc_permission), Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    public void requestPermission() {
        PermissionUtils.requestMultiPermissions(this,
                new String[]{
                        PermissionUtils.PERMISSION_CAMERA,
                        PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE,
                        PermissionUtils.PERMISSION_RECORD_AUDIO,
                        PermissionUtils.PERMISSION_READ_EXTERNAL_STORAGE}, mGrant);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.CODE_MULTI_PERMISSION) {
            PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mGrant);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionUtils.REQUEST_CODE_SETTING) {
            new Handler().postDelayed(this::requestPermission, 500);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
