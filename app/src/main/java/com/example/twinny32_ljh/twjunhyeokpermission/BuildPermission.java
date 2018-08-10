package com.example.twinny32_ljh.twjunhyeokpermission;

import android.content.Context;

public class BuildPermission extends AbstractPermissionBase {
    public static final String TAG = BuildPermission.class.getSimpleName();

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder extends PermissionBuilder<Builder> {

        private Builder(Context context) {
            super(context);
        }

        public void check() {
            checkPermissions();
        }

    }
}