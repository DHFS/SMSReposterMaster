package cn.itcast.odh.smsreposter.activity;

import android.Manifest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.itcast.odh.smsreposter.R;
import cn.itcast.odh.smsreposter.receiver.ScreenStatusReceiver;
import cn.itcast.odh.smsreposter.service.MainService;
import cn.itcast.odh.smsreposter.utils.CallingForwardUtils;
import cn.itcast.odh.smsreposter.utils.EasyPermissionsEx;
import cn.itcast.odh.smsreposter.utils.NumberUtils;
import cn.itcast.odh.smsreposter.utils.SPUtils;
import cn.itcast.odh.smsreposter.utils.ServiceUtils;
import cn.itcast.odh.smsreposter.utils.UIUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 800;
    private static final int RC_SETTINGS_SCREEN = 1;
    private static final int CALL_STATE_REQUEST_CODE = 100;

    private static String SWITCH_IS_CHECKED = "switch";
    private static final String CALLFORWARD_IS_RUNNING = "RUN";
    private final String[] needPermissions = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.CALL_PHONE};
    private static final String rationale = "需要xxxxxx权限";
    private static final String goSettingsRationale = "需要短信收发的权限，但此权限已被禁止，请设置为“允许”";


    String SP_name = "cellPhoneNumber";


    @BindView(R.id.et_destination_number)
    EditText etDestinationNumber;
    @BindView(R.id.btn_start_service)
    Button btnStartService;
    @BindView(R.id.btn_stop_service)
    Button btnStopService;
    @BindView(R.id.tv_detail)
    TextView tvDetail1;
    @BindView(R.id.tv_detail2)
    TextView tvDetail2;
    @BindView(R.id.tv_detail3)
    TextView tvDetail3;

    @BindView(R.id.fab_setting)
    FloatingActionButton fabSetting;
    @BindView(R.id.fab_help)
    FloatingActionButton fabLocation;
    @BindView(R.id.fab_phone)
    FloatingActionButton fabPhone;
    @BindView(R.id.fab_home)
    FloatingActionsMenu fabHome;
    @BindView(R.id.swt_calling)
    Switch swcCall;
    @BindView(R.id.tv_detail4)
    TextView tvDetailStatistic;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;


    private Intent mainServiceIntent;
    boolean serviceIsRunning = false;
    private MainService.myBinder binder;

    public Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 1:

                   int count = msg.arg1;
                   int count2=msg.arg2;
                   tvDetailStatistic.setText("统计:IN="+count+"条"+",OUT="+count2+"条");

               break;

           }
        }
    };
    private myConn conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        initView();
        initData();
        initPermission();

    }

    private void initPermission() {


    }

    private void initView() {


        UIUtils.stateBarSetting(this);
        backShow4Service();


        setListener4CallingForwardSwitch();
        BackShow4Switch();

        registerScreenActionReceiver();

    }
    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder.build();
    notification.flags=Notification.FLAG_NO_CLEAR;
// 设置通知的基本信息：icon、标题、内容
        builder.setSmallIcon(getNotificationIcon());
        builder.setContentTitle("千手观音");
        builder.setContentText("服务运行中");

// 设置通知的点击行为：这里启动一个 Activity
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

// 发送通知 id 需要在应用内唯一
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(9090, notification);
    }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.icon_small : R.mipmap.ic_launcher;
    }



    private void setListener4CallingForwardSwitch() {
        swcCall.setOnCheckedChangeListener(this);

    }

    //来电转移开关状态回显
    private void BackShow4Switch() {

        Boolean switchState = SPUtils.getBoolean(this, SWITCH_IS_CHECKED, false);

        swcCall.setChecked(switchState);


    }


    //通过服务开启状态回显
    private void backShow4Service() {

        boolean isRunning = ServiceUtils.getServiceStatues(MainActivity.this);

        if (isRunning) {
            setting4ServiceStatuesON();

        } else {

            setting4ServiceStatuesOff();
        }
    }


    private void initData() {

    }


    @OnClick({R.id.btn_start_service, R.id.btn_stop_service, R.id.fab_setting, R.id.fab_help, R.id.fab_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_service:
                //开启通知栏


                //点击后隐藏软键盘
                click2HideSoftKeyBoard(btnStartService);
                if (ServiceUtils.getServiceStatues(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "服务已经开启,不要再戳了！", Toast.LENGTH_SHORT).show();
                    break;
                }
                //获取输入框数据
                String num = etDestinationNumber.getText().toString().trim();
                //交给工具类进行判断
                Boolean isCorrect = NumberUtils.verifyNumber(num);
                //获取当前权限的状态
                if (isCorrect) {
                    //合法，进行SP保存并开启服务
                    SPUtils.saveNumber(getApplicationContext(), SP_name, num);

                    if (EasyPermissionsEx.hasPermissions(this, needPermissions)) {
                        Log.w(TAG, "BTN_START-------HasPermission");
                        StartRepostService(num);
                        viewShiftControl(num);
                        etDestinationNumber.clearFocus();
                        setting4ServiceStatuesON();
                    } else {
                        Log.w(TAG, "BTN_START-------requestPermissions");
                        EasyPermissionsEx.requestPermissions(this, rationale, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, needPermissions);
                    }
                } else {
                    //不合法，弹出吐司提示
                    Toast.makeText(MainActivity.this, "输入的号码有误，请输入有效号码。", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_stop_service:
                serviceIsRunning = ServiceUtils.getServiceStatues(MainActivity.this);
                if (serviceIsRunning) {
                    //关闭服务
                    stopService(mainServiceIntent);
                    unbindService(conn);
                    //将EDTEXT设置为可见，将TV设置为不可见
                    etDestinationNumber.setVisibility(View.VISIBLE);
                    etDestinationNumber.setText("");
                    Toast.makeText(MainActivity.this, "服务关闭", Toast.LENGTH_SHORT).show();
                    setting4ServiceStatuesOff();
                    return;
                }
                break;
            case R.id.fab_setting:
                Intent settingintent =new Intent(this,SettingsActivity.class);
                startActivity(settingintent);


                break;
            case R.id.fab_help:
                Intent helpIntent =new Intent(this,helpActivity.class);
                startActivity(helpIntent);

                break;
            case R.id.fab_phone:
                Toast.makeText(MainActivity.this, "功能未开通", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //隐藏软键盘
    private void click2HideSoftKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //显示服务状态
    private void setting4ServiceStatuesOff() {

        tvDetail3.setText("服务状态：未启动");

        tvDetail2.setText("接收号码：未设置号码");

        tvDetail1.setText("本机号码：" + NumberUtils.getLocalNumber(MainActivity.this));

        etDestinationNumber.setVisibility(View.VISIBLE);


    }

    private void setting4ServiceStatuesON() {

        tvDetail3.setText("服务状态：已经启动");

        tvDetail2.setText("接收号码：" + SPUtils.getSavedNumber(MainActivity.this, "cellPhoneNumber", "0"));
        tvDetail1.setText("本机号码：" + NumberUtils.getLocalNumber(MainActivity.this));
        etDestinationNumber.setVisibility(View.INVISIBLE);
        if (conn==null){

            conn=new myConn();
        }
        if(mainServiceIntent==null){

            mainServiceIntent = new Intent(this, MainService.class);
        }
            bindService(mainServiceIntent,conn,BIND_AUTO_CREATE);


    }

    //界面切换
    private void viewShiftControl(String num) {
        Toast.makeText(MainActivity.this, "短信转发服务已经开启", Toast.LENGTH_SHORT).show();
        etDestinationNumber.setVisibility(View.INVISIBLE);
        tvDetail2.setText("接收号码：" + num);
        tvDetail2.setVisibility(View.VISIBLE);
        tvDetail1.setVisibility(View.VISIBLE);

    }


    //开启服务
    private void StartRepostService(String num) {


        mainServiceIntent = new Intent(this, MainService.class);
        mainServiceIntent.putExtra("num", num);

        startService(mainServiceIntent);
        conn = new myConn();
        bindService(mainServiceIntent, conn,BIND_AUTO_CREATE);

    }

    public class  myConn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {

            System.out.println("------My System.out.println------"  +"onServiceConnected方法执行了");

                if (service!=null){

                    binder = (MainService.myBinder) service;


                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Thread.sleep(3000);
                                    int inCount = binder.getInCount();
                                    int outCount = binder.getOutCount();
                                    System.out.println("------My System.out.println--服务运行中---inCount:" + inCount + ",----outCount:" + outCount);
                                    Message message = new Message();
                                    message.what = 1;
                                    message.arg1 = inCount;
                                    message.arg2 = outCount;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();


                }


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    //点击空白处的隐藏键盘方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etDestinationNumber.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etDestinationNumber.getWindowToken(), 0);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean serviceStatues = ServiceUtils.getServiceStatues(MainActivity.this);

        if (serviceStatues) {
            setting4ServiceStatuesON();

        } else {


            tvDetail3.setText("服务状态：未启动");

            tvDetail2.setText("接收号码：未设置号码");

            tvDetail1.setText("本机号码：" + NumberUtils.getLocalNumber(MainActivity.this));

            etDestinationNumber.setVisibility(View.VISIBLE);

        }

    }


    private void startAlphaAnimationIn(ImageView iv) {
        //渐变动画    从显示（1.0）到隐藏（0.0）
        AlphaAnimation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
        //执行三秒
        alphaAnim.setDuration(2000);
        alphaAnim.setFillAfter(true);
        iv.startAnimation(alphaAnim);
    }

    private void startAlphaAnimationOut(ImageView iv) {
        //渐变动画    从显示（1.0）到隐藏（0.0）
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0f);
        //执行三秒
        alphaAnim.setDuration(100);
        alphaAnim.setFillAfter(true);
        iv.startAnimation(alphaAnim);
    }


    //EasyPermission

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    Log.w(TAG, "onRequestPermissionsResult: Permission granted.");

                    String num = etDestinationNumber.getText().toString();

                    mainServiceIntent = new Intent(this, MainService.class);
                    mainServiceIntent.putExtra("num", num);
                    startService(mainServiceIntent);


                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Log.d(TAG, "onRequestPermissionsResult: Permission denied.");

                    // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
                    // This will display a dialog directing them to enable the permission in app settings.
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, needPermissions)) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationale, "去设置", RC_SETTINGS_SCREEN);

                    }
                }

                return;
            }

            case PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    Log.w(TAG, "onRequestPermissionsResult: Permission granted.");


                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Log.d(TAG, "onRequestPermissionsResult: Permission denied.");

                    // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
                    // This will display a dialog directing them to enable the permission in app settings.
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, needPermissions)) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationale, "去设置", RC_SETTINGS_SCREEN);
                    }
                }

                return;
        }

        // other 'case' lines to check for other permissions this app might request
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SETTINGS_SCREEN) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Log.d(TAG, "onActivityResult: " + data);
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
        }


        if (requestCode == CALL_STATE_REQUEST_CODE) {


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //来电转移开关
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Boolean isruning = SPUtils.getBoolean(this, CALLFORWARD_IS_RUNNING, false);
        if (isChecked) {
            if (isruning) {
                return;
            }
            CallingForwardUtils.startCallForwarding(this);

        } else {
            if (!isruning) {
                return;
            }
            CallingForwardUtils.stopCallForwarding(this);

        }


    }


    //注册屏幕广播
    private void registerScreenActionReceiver(){
        ScreenStatusReceiver receiver = new ScreenStatusReceiver();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver, filter);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            moveTaskToBack(true);

        }

        return super.onKeyDown(keyCode,event);


    }

}

