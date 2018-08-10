package com.example.twinny32_ljh.twjunhyeokpermission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twinny32_ljh.twjunhyeokpermission.util.HandlerUtil;
import com.example.twinny32_ljh.twjunhyeokpermission.util.IHandlerMessage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionListener {

    private final int HD_TEST_0 = 1000;

    TextView textView;

    HandlerUtil handlerUtil = new HandlerUtil(new IHandlerMessage() {
        @Override
        public void handlerMessage(Message msg) {

            switch (msg.what) {
                case HD_TEST_0:
                    Toast.makeText(getBaseContext(), "HD_TEST_0", Toast.LENGTH_SHORT).show();
                    TelephonyManager systemService = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    assert systemService != null;
                    @SuppressLint("HardwareIds") String phoneNum = systemService.getLine1Number();
                    textView.setText(phoneNum);
                    break;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkMyAppPermission(new String[]
                {
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                });
    }

    private void initView() {
        textView = findViewById(R.id.textView);
    }


    /**
     * 권한설정 테드퍼미션 사용메소드
     */
    private void checkMyAppPermission(String[] permission) {

        BuildPermission.with(this)
                .setPermissionListener(this)
                .setRationaleMessage("안녕하센가요")
                .setRationaleTitle("제모목모고")
                .setRationaleConfirmText("안뇽이아야아아")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(permission
                )
                .check();
    }

    @Override
    public void onPermissionGranted() {
//        Toast.makeText(getBaseContext(), "권한", Toast.LENGTH_SHORT).show();
        handlerUtil.sendEmptyMessage(HD_TEST_0);
    }

    @Override
    public void onPermissionDenied(List<String> deniedPermissions) {
//        Toast.makeText(getBaseContext(),"권한거부로 인해 앱을 종료 안합니다",Toast.LENGTH_SHORT).show();
    }
}
