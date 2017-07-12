package cn.itcast.odh.smsreposter.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by rhinofly on 2016/11/5 time:21:34.
 */

public class NumberUtils {



    public static boolean verifyNumber(String telNum){
        String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(telNum);
        return m.matches();
    }

        //回去当前手机号码
    public static String getLocalNumber(Context context) {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String number = tManager.getLine1Number();
        return number;
    }


}
