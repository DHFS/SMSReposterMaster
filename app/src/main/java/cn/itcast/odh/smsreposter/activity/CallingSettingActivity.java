package cn.itcast.odh.smsreposter.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.itcast.odh.smsreposter.R;
import cn.itcast.odh.smsreposter.utils.SPUtils;
import cn.itcast.odh.smsreposter.utils.UIUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by rhinofly on 2016/11/15 time:9:44.
 */


@RuntimePermissions
public class CallingSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {


    boolean switchCondition=false;

    boolean callForwardingIsRunning=false;

    public static final String SWITCH_IS_CHECKED="SWITCH";







    Switch switchCallForward;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initView() {
        UIUtils.stateBarSetting(this);


        checkCallingStateFromCarrier();


        switchCallForward.setOnCheckedChangeListener(this);

        //回显开关状态

        switchCondition=SPUtils.getBoolean(CallingSettingActivity.this,SWITCH_IS_CHECKED,false);
        switchCallForward.setChecked(switchCondition);

    }

    private void checkCallingStateFromCarrier() {



    }

    private void initData() {
    }

    //开关状态监听
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        TelephonyManager manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

        manager.listen(new myPhoneStateListener(), PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR);

        SPUtils.saveBoolean(CallingSettingActivity.this,SWITCH_IS_CHECKED,isChecked);

        if(isChecked) {

            startCallForwarding();

        }else {

           stopCallForwarding();
        }
    }

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void needsPermission(Intent intentCallForward) {

        Toast.makeText(CallingSettingActivity.this, "权限申请成功", Toast.LENGTH_SHORT).show();
        startActivity(intentCallForward);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @OnShowRationale({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void onShowRationale(final PermissionRequest request) {
        showRationaleDialog("使用此功能需要申请拨号权限", request);
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void onPermissionDenied() {
        Toast.makeText(CallingSettingActivity.this, "您拒绝了应用获取权限，该功能不可用", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void onNeverAskAgain() {
        Toast.makeText(CallingSettingActivity.this, "不再允许访问该权限，功能不可用", Toast.LENGTH_SHORT).show();
    }


    public class myPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallForwardingIndicatorChanged(boolean cfi) {
            System.out.println("------My System.out.println------" + "\n" +cfi);
        }
    }


    private void stopCallForwarding() {

        String callForwardString = "##21#";
        Intent intentCallForward = new Intent(Intent.ACTION_CALL); // ACTION_CALL
        Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
        intentCallForward.setData(uri2);


    }

    private void startCallForwarding() {
        //获取用户设置的号码
        String number_forward2= SPUtils.getSavedNumber(CallingSettingActivity.this,"cellPhoneNumber","0");
        String callForwardString = "**21*"+number_forward2+"#";
        Intent intentCallForward = new Intent(Intent.ACTION_CALL); // ACTION_CALL
        Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
        intentCallForward.setData(uri2);


        callForwardingIsRunning=true;
    }

    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }


}
