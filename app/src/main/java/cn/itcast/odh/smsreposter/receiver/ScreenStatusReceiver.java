package cn.itcast.odh.smsreposter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.itcast.odh.smsreposter.service.MainService;

/**
 * Created by rhinofly on 2016/12/2 time:13:05.
 */

public class ScreenStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("------My System.out.println------" +"onReceive屏幕状态发生变化");

        Toast.makeText(context, "屏幕状态发生变化", Toast.LENGTH_SHORT).show();
        Intent serverIntent = new Intent(context, MainService.class);
        context.startService(serverIntent);





    }
}
