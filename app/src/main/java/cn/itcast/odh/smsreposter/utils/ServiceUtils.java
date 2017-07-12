package cn.itcast.odh.smsreposter.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.itcast.odh.smsreposter.activity.MainActivity;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by rhinofly on 2016/11/14 time:13:55.
 */

public class ServiceUtils {

    public static boolean getServiceStatues(Context context) {

        ActivityManager manager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            String serviceName = "cn.itcast.odh.smsreposter.service.MainService";
            if (serviceName.equals(service.service.getClassName())) {
                    return  true;
            }
        }
        return false;
    }
}