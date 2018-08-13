package com.example.twinny32_ljh.twjunhyeokpermission;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.example.twinny32_ljh.twjunhyeokpermission.util.ObjectUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 실제 퍼미션을 실행하는 다이아로그를 띄우는 액티비티
 */
public class PermissionActivity extends AppCompatActivity {


    public static final int REQ_CODE_PERMISSION_REQUEST = 10;

    public static final int REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST = 30;
    public static final int REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_SETTING = 31;

    public static final String EXTRA_PERMISSIONS = "permissions";
    public static final String EXTRA_RATIONALE_TITLE = "rationale_title";
    public static final String EXTRA_RATIONALE_MESSAGE = "rationale_message";
    public static final String EXTRA_DENY_TITLE = "deny_title";
    public static final String EXTRA_DENY_MESSAGE = "deny_message";
    public static final String EXTRA_PACKAGE_NAME = "package_name";
    public static final String EXTRA_SETTING_BUTTON = "setting_button";
    public static final String EXTRA_SETTING_BUTTON_TEXT = "setting_button_text";
    public static final String EXTRA_RATIONALE_CONFIRM_TEXT = "rationale_confirm_text";
    public static final String EXTRA_DENIED_DIALOG_CLOSE_TEXT = "denied_dialog_close_text";
    public static final String EXTRA_SCREEN_ORIENTATION = "screen_orientation";

    private static Deque<PermissionListener> permissionListenerStack;

    CharSequence rationaleTitle;
    CharSequence rationale_message;
    CharSequence denyTitle;
    CharSequence denyMessage;
    String[] permissions;
    String packageName;
    boolean hasSettingButton;
    String settingButtonText;
    String deniedCloseButtonText;
    String rationaleConfirmText;
    boolean isShownRationaleDialog; //기본값 false
    int requestedOrientation;

    public static void startActivity(Context context, Intent intent, PermissionListener listener) {
        if (permissionListenerStack == null) {
            permissionListenerStack = new ArrayDeque<>();
        }
        permissionListenerStack.push(listener);
        //리스너 전달하고 activity 생명주기 시작시킨다.
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //가져오기
        setupFromSavedInstanceState(savedInstanceState);

        // 최고급 퍼미션 체크 windows
        if (needWindowPermission()) {
            //윈도우 퍼미션(=최고 권한 : 최 상단 뷰에 그리기 )을 요청한다.
            requestWindowPermission();
        } else {
            //퍼미션을 체크한다.
            checkPermissions(false);
        }

        setRequestedOrientation(requestedOrientation);
    }


    private void setupFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            permissions = savedInstanceState.getStringArray(EXTRA_PERMISSIONS);
            rationaleTitle = savedInstanceState.getCharSequence(EXTRA_RATIONALE_TITLE);
            rationale_message = savedInstanceState.getCharSequence(EXTRA_RATIONALE_MESSAGE);
            denyTitle = savedInstanceState.getCharSequence(EXTRA_DENY_TITLE);
            denyMessage = savedInstanceState.getCharSequence(EXTRA_DENY_MESSAGE);
            packageName = savedInstanceState.getString(EXTRA_PACKAGE_NAME);

            hasSettingButton = savedInstanceState.getBoolean(EXTRA_SETTING_BUTTON, true);

            rationaleConfirmText = savedInstanceState.getString(EXTRA_RATIONALE_CONFIRM_TEXT);
            deniedCloseButtonText = savedInstanceState.getString(EXTRA_DENIED_DIALOG_CLOSE_TEXT);

            settingButtonText = savedInstanceState.getString(EXTRA_SETTING_BUTTON_TEXT);
            requestedOrientation = savedInstanceState.getInt(EXTRA_SCREEN_ORIENTATION);
        } else {

            Intent intent = getIntent();
            permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS);
            rationaleTitle = intent.getCharSequenceExtra(EXTRA_RATIONALE_TITLE);
            rationale_message = intent.getCharSequenceExtra(EXTRA_RATIONALE_MESSAGE);
            denyTitle = intent.getCharSequenceExtra(EXTRA_DENY_TITLE);
            denyMessage = intent.getCharSequenceExtra(EXTRA_DENY_MESSAGE);
            packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
            hasSettingButton = intent.getBooleanExtra(EXTRA_SETTING_BUTTON, true);
            rationaleConfirmText = intent.getStringExtra(EXTRA_RATIONALE_CONFIRM_TEXT);
            deniedCloseButtonText = intent.getStringExtra(EXTRA_DENIED_DIALOG_CLOSE_TEXT);
            settingButtonText = intent.getStringExtra(EXTRA_SETTING_BUTTON_TEXT);
            requestedOrientation = intent.getIntExtra(EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        }


    }

    /**
     *  가지고 있는 권한중 SYSTEM_ALERT_WINDOW 체크하기
     *
     *  만약 SYSTEM_ALERT_WINDOW(취상위 뷰에 그리기) 권한이 요청 목록에 있으면
     *  hasWindowPermission을 실행한다.
     * @return
     * =======================false 일 경우(2)=======================
     * 1. SYSTEM_ALERT_WINDOW 요청권한이 없을때
     * 2. SYSTEM_ALERT_WINDOW 의 권한을 가지고 있을떄
     * =======================true 일 경우(1)=======================
     * 1. SYSTEM_ALERT_WINDOW  권한을 가지고 있지 않을때
     */
    private boolean needWindowPermission() {
        for (String permission : permissions) {
            if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                return !hasWindowPermission();
            }
        }
        return false;
    }

    /**
     * @return 권한을 가지고있으면 true 없으면 false
     */
    @TargetApi(Build.VERSION_CODES.M)
    private boolean hasWindowPermission() {
        return Settings.canDrawOverlays(getApplicationContext());
    }

    /**
     * 윈도우 퍼미션(=최고 권한 : 최 상단 뷰에 그리기 )을 요청한다.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void requestWindowPermission() {
        Uri uri = Uri.fromParts("package", packageName, null);
        final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);

        if (!TextUtils.isEmpty(rationale_message)) {
            new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                    .setMessage(rationale_message)
                    .setCancelable(false)
                    .setNegativeButton(rationaleConfirmText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivityForResult(intent, REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST);
                        }
                    })
                    .show();
            isShownRationaleDialog = true;
        } else {
            startActivityForResult(intent, REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST);
        }
    }

    /**
     * 권한체크
     * @param fromOnActivityResult
     */
    private void checkPermissions(boolean fromOnActivityResult) {

        List<String> needPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (!hasWindowPermission()) {
                    needPermissions.add(permission);
                }
            } else {
                if (AbstractPermissionBase.isDenied(this, permission)) {
                    needPermissions.add(permission);
                }
            }
        }

        if (needPermissions.isEmpty()) {//권한이 없을 때.
            permissionResult(null);
        } else if (fromOnActivityResult) { //From Setting Activity
            permissionResult(needPermissions);
        } else if (needPermissions.size() == 1 && needPermissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)) {   // window permission deny
            permissionResult(needPermissions);
        } else if (!isShownRationaleDialog && !TextUtils.isEmpty(rationale_message)) {
            //권한 다이아로그 띄우기
            showRationaleDialog(needPermissions);
        } else { // //Need Request Permissions
            requestPermissions(needPermissions);
        }
    }

    /**
     * 권한설정 결과>> finish()시킴
     * @param deniedPermissions 거절된 권한들
     */
    private void permissionResult(List<String> deniedPermissions) {
        Log.v(BuildPermission.TAG, "permissionResult(): " + deniedPermissions);
        if (permissionListenerStack != null) {
            PermissionListener listener = permissionListenerStack.pop();

            if (ObjectUtils.isEmpty(deniedPermissions)) {
                //거절된 퍼미션이 없을경우 다 수락된 것으로 표시
                listener.onPermissionGranted();
            } else {
                //거절된 퍼미션이 있을우 리스너로 날림.
                listener.onPermissionDenied(deniedPermissions);
            }
            if (permissionListenerStack.size() == 0) {
                permissionListenerStack = null;
            }
        }

        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void showRationaleDialog(final List<String> needPermissions) {

        new AlertDialog.Builder(this, R.style.Dialog)
                .setTitle(rationaleTitle)
                .setMessage(rationale_message)
                .setCancelable(false)

                .setNegativeButton(rationaleConfirmText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(needPermissions);

                    }
                })
                .show();
        isShownRationaleDialog = true;


    }

    /**
     * 권한 요청하기
     * @param needPermissions 요구하는 권한 들
     */
    public void requestPermissions(List<String> needPermissions) {
        ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]),
                REQ_CODE_PERMISSION_REQUEST);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray(EXTRA_PERMISSIONS, permissions);
        outState.putCharSequence(EXTRA_RATIONALE_TITLE, rationaleTitle);
        outState.putCharSequence(EXTRA_RATIONALE_MESSAGE, rationale_message);
        outState.putCharSequence(EXTRA_DENY_TITLE, denyTitle);
        outState.putCharSequence(EXTRA_DENY_MESSAGE, denyMessage);
        outState.putString(EXTRA_PACKAGE_NAME, packageName);
        outState.putBoolean(EXTRA_SETTING_BUTTON, hasSettingButton);
        outState.putString(EXTRA_DENIED_DIALOG_CLOSE_TEXT, deniedCloseButtonText);
        outState.putString(EXTRA_RATIONALE_CONFIRM_TEXT, rationaleConfirmText);
        outState.putString(EXTRA_SETTING_BUTTON_TEXT, settingButtonText);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        List<String> deniedPermissions = AbstractPermissionBase.getDeniedPermissions(this, permissions);

        if (deniedPermissions.isEmpty()) {
            permissionResult(null);
        } else {
            showPermissionDenyDialog(deniedPermissions);
        }
    }

    public void showPermissionDenyDialog(final List<String> deniedPermissions) {

        if (TextUtils.isEmpty(denyMessage)) {
            // denyMessage 설정 안함
            permissionResult(deniedPermissions);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Dialog);

        builder.setTitle(denyTitle)
                .setMessage(denyMessage)
                .setCancelable(false)
                .setNegativeButton(deniedCloseButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        permissionResult(deniedPermissions);
                    }
                });

        if (hasSettingButton) {

            if (TextUtils.isEmpty(settingButtonText)) {
                settingButtonText = getString(R.string.permission_setting);
            }

            builder.setPositiveButton(settingButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AbstractPermissionBase.startSettingActivityForResult(PermissionActivity.this);

                }
            });

        }
        builder.show();
    }

    public boolean shouldShowRequestPermissionRationale(List<String> needPermissions) {

        if (needPermissions == null) {
            return false;
        }

        for (String permission : needPermissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this, permission)) {
                return false;
            }
        }

        return true;

    }


    /**
     * 권한이 거부되고 거절메세지가 있는경우
     */
    public void showWindowPermissionDenyDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setMessage(denyMessage)
                .setCancelable(false)
                .setNegativeButton(deniedCloseButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkPermissions(false);
                    }
                });

        if (hasSettingButton) {
            if (TextUtils.isEmpty(settingButtonText)) {
                settingButtonText = getString(R.string.permission_setting);
            }

            builder.setPositiveButton(settingButtonText, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.fromParts("package", packageName, null);
                    final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
                    startActivityForResult(intent, REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_SETTING);
                }
            });

        }
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AbstractPermissionBase.REQ_CODE_REQUEST_SETTING: // 권한 설정에서 돌아왔을때의 결과.
                checkPermissions(true);
                break;
            case REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST:   // 윈도우 퍼미션(=최고 권한 : 최 상단 뷰에 그리기 ) ALERT WINDOW 요청에 대한 결과
                if (!hasWindowPermission() && !TextUtils.isEmpty(denyMessage)) {  // 권한이 거부되고 denyMessage 가 있는 경우
                    showWindowPermissionDenyDialog();
                } else {     // 권한있거나 또는 denyMessage가 없는 경우는 일반 permission 을 확인한다.
                    checkPermissions(false);
                }
                break;
            case REQ_CODE_SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_SETTING:   //  ALERT WINDOW 권한 설정 실패후 재 요청에 대한 결과
                checkPermissions(false);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }
}