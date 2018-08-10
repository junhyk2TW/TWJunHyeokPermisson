package com.example.twinny32_ljh.twjunhyeokpermission;

import java.util.List;

public interface PermissionListener {

    void onPermissionGranted();

    void onPermissionDenied(List<String> deniedPermissions);
}
