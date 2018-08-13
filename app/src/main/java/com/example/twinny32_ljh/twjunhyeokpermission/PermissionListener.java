package com.example.twinny32_ljh.twjunhyeokpermission;

import java.util.List;

/**
 * 권한 체크 리스너
 */
public interface PermissionListener {

    void onPermissionGranted();

    void onPermissionDenied(List<String> deniedPermissions);
}
