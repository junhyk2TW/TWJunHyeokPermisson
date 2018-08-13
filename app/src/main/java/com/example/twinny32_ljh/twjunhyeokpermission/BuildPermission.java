package com.example.twinny32_ljh.twjunhyeokpermission;

import android.content.Context;

public class BuildPermission extends AbstractPermissionBase {
    public static final String TAG = BuildPermission.class.getSimpleName();

    /**
     *
     * @param context 해당 정보
     * @return 인스턴스화된 객체 빌더를 반환하여 사용할수 있게해준다.
     */
    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder extends PermissionBuilder<Builder> {

        private Builder(Context context) {
            super(context);
        }

        /**
         *
         */
        public void check() {
            checkPermissions();
        }

    }
}