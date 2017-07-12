package cn.itcast.odh.smsreposter.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.odh.smsreposter.interfaces.onMessageReceivedListener;

/**
 * Created by rhinofly on 2016/11/17 time:16:31.
 */
public class MessageUtils {

    /* 自定义ACTION常数，作为广播的Intent Filter识别常数 */
    public static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
    public static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";


    public static void sendMsg(Context context, String number, String sender, String sendtime, ArrayList<String> content, int length) {

        SmsManager smsManager = SmsManager.getDefault();

        Intent itSend = new Intent(SMS_SEND_ACTIOIN);
        Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);

          /* sentIntent参数为传送后接受的广播信息PendingIntent */
        PendingIntent mSendPI = PendingIntent.getBroadcast(context, 0, itSend, 0);

          /* deliveryIntent参数为送达后接受的广播信息PendingIntent */
        PendingIntent mDeliverPI = PendingIntent.getBroadcast(context, 0, itDeliver, 0);


        if (length> 1) {

            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();

            String head="时间："+"\n"+sendtime+"\n"+"来自："+"\n"+sender+"\n"+"短信内容："+"\n";
            content.add(0,head);
            smsManager.sendMultipartTextMessage(number, null, content, sentIntents, null);
        } else {
            smsManager.sendTextMessage(number, null, "时间："+sendtime+"\n"+"来自："+sender+"\n"+"短信内容："+"\n"+content, mSendPI, mDeliverPI);
        }


    }
}