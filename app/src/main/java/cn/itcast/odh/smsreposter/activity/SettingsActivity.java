package cn.itcast.odh.smsreposter.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.itcast.odh.smsreposter.R;
import cn.itcast.odh.smsreposter.utils.Go2PermissionSetting;
import cn.itcast.odh.smsreposter.utils.SPUtils;

/**
 * Created by rhinofly on 2016/11/25 time:22:22.
 */

public class SettingsActivity extends AppCompatActivity {


    @BindView(R.id.btn_master)
    Button btnMaster;
    @BindView(R.id.btn_receiver)
    Button btnReceiver;
    @BindView(R.id.btn_permission_setting)
    Button btnPermissionSetting;
    @BindView(R.id.btn_clean_statistics)
    Button btnCleanStatistics;

    private static final int APPLICATION_DETAIL = 15555;
    @BindView(R.id.tb_settings)
    Toolbar tbSettings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initData() {
        initToolBar();
    }

    private void initToolBar() {
        tbSettings.setTitle("设置");
        tbSettings.setTitleTextColor(Color.WHITE);


        setSupportActionBar(tbSettings);
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void initView() {

    }

    @OnClick({R.id.btn_master, R.id.btn_receiver, R.id.btn_permission_setting, R.id.btn_clean_statistics})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_master:
                Toast.makeText(SettingsActivity.this, "功能完善中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_receiver:
                Toast.makeText(SettingsActivity.this, "功能完善中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_permission_setting:
                Go2PermissionSetting.gotoPermissionSettings(SettingsActivity.this);
                break;
            case R.id.btn_clean_statistics:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("注意");
                builder.setMessage("你确定要将主页的统计数据清零吗？");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cleanMsgCount();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
                break;
        }
    }

    private void cleanMsgCount() {


        SPUtils.saveInt(SettingsActivity.this, "MSG_OUT", 0);
        SPUtils.saveInt(SettingsActivity.this, "MSG_IN", 0);

    }

    //跳转到应用详情页
    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }


}
