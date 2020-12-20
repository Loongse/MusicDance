package com.md.lib_common_ui.base.constant;

import android.Manifest;

/**
 * 用于定义常量
 */
public class Constant {
    /**
     * 权限常量相关
     */
    public static final int WRITE_READ_EXTERNAL_CODE = 0x01;
    public static final String[] WRITE_READ_EXTERNAL_PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int HARDWARE_CAMERA_CODE = 0x02;
    public static final String[] HARDWARE_CAMERA_PERMISSION = new String[]{
            Manifest.permission.CAMERA
    };
}
