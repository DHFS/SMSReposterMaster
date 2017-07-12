package cn.itcast.odh.smsreposter.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
;
import android.net.Uri;



/**
 * Created by rhinofly on 2016/11/23 time:15:07.
 */

public class CallingForwardUtils {
    private static final String[] needPermissionsforCalling = new String[]{Manifest.permission.CALL_PHONE};
    private static final String rationale = "需要xxxxxx权限";
    private static final String goSettingsRationale = "需要短信收发的权限，但此权限已被禁止，请设置为“允许”";
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 800;
    private static final String CALLFORWARD_IS_RUNNING = "RUN";
    private static String SWITCH_IS_CHECKED = "switch";



    public static void stopCallForwarding(Context context) {

        String callForwardString = "##21#";
        Intent intentCallForward = new Intent(Intent.ACTION_CALL); // ACTION_CALL
        Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
        intentCallForward.setData(uri2);
        if(EasyPermissionsEx.hasPermissions(context,needPermissionsforCalling)){

             context.startActivity(intentCallForward);
        }else {

            EasyPermissionsEx.requestPermissions(context, rationale, PERMISSIONS_REQUEST_CALL_PHONE, needPermissionsforCalling);
        }
        SPUtils.saveBoolean(context,SWITCH_IS_CHECKED,false);
        SPUtils.saveBoolean(context,CALLFORWARD_IS_RUNNING,false);

    }

    public static void startCallForwarding(Context context) {
        //获取用户设置的号码
        String number_forward2= SPUtils.getSavedNumber(context,"cellPhoneNumber","0");
        String callForwardString = "**21*"+number_forward2+"#";
        Intent intentCallForward = new Intent(Intent.ACTION_CALL); // ACTION_CALL
        Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
        intentCallForward.setData(uri2);
        SPUtils.saveBoolean(context,SWITCH_IS_CHECKED,true);
        SPUtils.saveBoolean(context,CALLFORWARD_IS_RUNNING,true);
        if(EasyPermissionsEx.hasPermissions(context,needPermissionsforCalling)){

            context.startActivity(intentCallForward);
        }else {

            EasyPermissionsEx.requestPermissions(context, rationale, PERMISSIONS_REQUEST_CALL_PHONE, needPermissionsforCalling);
        }
    }

}
