package cn.itcast.odh.smsreposter.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.List;

/**
 * Created by rhinofly on 2016/11/26 time:9:56.
 */

public class Go2PermissionSetting {

    /**
     * 跳转到应用权限设置页面 http://www.tuicool.com/articles/jUby6rA
     * @param context 传入app 或者 activity context，通过context获取应用packegename，之后通过packegename跳转制定应用
     * @return 是否是miui
     */
    public static boolean gotoPermissionSettings(Context context) {
        boolean mark = isMIUI();

        if ( mark ) {

            // 之兼容miui v5/v6  的应用权限设置页面，否则的话跳转应用设置页面（权限设置上一级页面）
            try {
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter","com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivity(localIntent);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(),null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        }

        return mark;
    }

    /**
     * 检查手机是否是miui
     * @ref http://dev.xiaomi.com/doc/p=254/index.html
     * @return
     */
    public static boolean isMIUI(){
        String device = Build.MANUFACTURER;
        System.out.println( "Build.MANUFACTURER = " + device );
        if ( device.equals( "Xiaomi" ) ) {
            System.out.println( "this is a xiaomi device" );
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * 判断是否是miui V5/V6，老的miui无法兼容
     * @param context
     * @return
     */
    public static boolean isMIUIv5v6(Context context) {
        boolean result = false;
        Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        localIntent.setClassName("com.miui.securitycenter","com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        if (isIntentAvailable(context, localIntent)) {
            result = true;
        }
        return result;
    }

    /**
     * 检查是否有这个activity
     * @param context
     * @param intent
     * @return
     */
    private static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }
}