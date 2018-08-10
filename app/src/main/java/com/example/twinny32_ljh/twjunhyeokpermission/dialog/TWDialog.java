package com.example.twinny32_ljh.twjunhyeokpermission.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TWDialog{

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static Builder with(@NonNull Context context, int themeResId) {
        return new Builder(context, themeResId);
    }

    public static Builder with(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        return new Builder(context, cancelable, cancelListener);
    }


    public static class Builder extends DialogBuilder<Builder> {


        public Builder(@NonNull Context context) {
            super(context);
        }

        public Builder(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        protected Builder(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }



        @Override
        public void show() {
            super.show();
        }
    }

}
