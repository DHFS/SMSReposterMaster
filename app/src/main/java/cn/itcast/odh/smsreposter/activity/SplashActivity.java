package cn.itcast.odh.smsreposter.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.itcast.odh.smsreposter.R;
import cn.itcast.odh.smsreposter.utils.Go2PermissionSetting;
import cn.itcast.odh.smsreposter.utils.SPUtils;
import cn.itcast.odh.smsreposter.utils.UIUtils;

/**
 * Created by rhinofly on 2016/11/23 time:17:26.
 */

public class SplashActivity extends AppCompatActivity {
    private static final String rationale = "需要设置的权限";
    private static final String goSettingsRationale = "需要权限，请到应用详情中将权限列表设置为允许";
    private static final int RC_SETTINGS_SCREEN = 2;
    private static final int APPLICATION_DETAIL = 1001;
    private static final String IS_FIRST_TIMEM = "FIRST";
    @BindView(R.id.btn_master)
    Button btnMaster;
    @BindView(R.id.btn_receiver)
    Button btnReceiver;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.btn_permission_setting)
    Button btnPermissionSetting;
    @BindView(R.id.splash_guide)
    RelativeLayout splashGuide;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        UIUtils.stateBarSetting(this);
        init();
    }

    private void init() {

    }

    @OnClick({R.id.btn_master, R.id.btn_receiver, R.id.btn_permission_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_master:
                startActivity(new Intent(this, MainActivity.class));

                SPUtils.saveBoolean(SplashActivity.this,IS_FIRST_TIMEM,false);
                finish();
                break;
            case R.id.btn_receiver:
                break;

            case R.id.btn_permission_setting:
                boolean ismiui = Go2PermissionSetting.gotoPermissionSettings(SplashActivity.this);
                if(ismiui==false){
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", this.getPackageName(),null);
                    intent.setData(uri);
                   this.startActivity(intent);
                }
                break;
        }
    }


}
