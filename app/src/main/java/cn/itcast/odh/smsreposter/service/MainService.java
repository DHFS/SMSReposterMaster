package cn.itcast.odh.smsreposter.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.itcast.odh.smsreposter.activity.MainActivity;
import cn.itcast.odh.smsreposter.utils.MessageUtils;
import cn.itcast.odh.smsreposter.utils.SPUtils;

/**
 * Created by rhinofly on 2016/11/5 time:21:24.
 */

public class MainService extends Service {


    private String num;
    private MainReceiver Receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new myBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        num = intent.getStringExtra("num");
        StartReceiver(num);
        System.out.println("------My System.out.println------onStartCommand+startReceiver" + "\n" +num);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("------My System.out.println-----o服务 ondestroy方法。。。。。-" );
        startService(new Intent(this,MainService.class));

    }

    //开启广播接收

    private void StartReceiver(String num){

        if (Receiver==null){

        Receiver = new MainReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        MainService.this.registerReceiver(Receiver,intentFilter);
        System.out.println("------My System.out.println------注册了广播"  );
        }


    }

    public  class  MainReceiver extends BroadcastReceiver {




        @Override
            public void onReceive (Context context, Intent intent){
            //接收方号码
            String s = intent.getAction().toString();
            System.out.println("短信来了");
            // 监听短信广播
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");// 获取短信内容
                int current_msg_received_counts = pdus.length;
                Integer msg_pre_received = SPUtils.getInt(MainService.this, "MSG_IN", 0);
                SPUtils.saveInt(MainService.this,"MSG_IN",msg_pre_received+current_msg_received_counts);

                System.out.println("------My System.out.println---pdus.length---" + +current_msg_received_counts);
                ArrayList<String> allMsg=new ArrayList<String>();
                String sender="";
                Date date;
                String sendtime="";
                int sendCount=0;
                for (Object pdu : pdus) {
                    byte[] data = (byte[]) pdu;
                    SmsMessage message = SmsMessage.createFromPdu(data);// 使用pdu格式的短信数据生成短信对象
                    // 获取短信的发送者
                    if (sender.equals("")){

                    sender = message.getOriginatingAddress();
                    }

                    allMsg.add(message.getMessageBody());   // 获取短信的内容

                    if (sendtime.equals("")){
                        date = new Date(message.getTimestampMillis());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sendtime = format.format(date);

                    }

                    sendCount++;

                }

                String number = SPUtils.getSavedNumber(context, "cellPhoneNumber", "0");
                MessageUtils.sendMsg(MainService.this,number, sender, sendtime, allMsg,current_msg_received_counts);
                Integer msg_out_already = SPUtils.getInt(context, "MSG_OUT", 0);
                SPUtils.saveInt(context,"MSG_OUT",msg_out_already+sendCount);
                sendCount=0;

            }
        }

        }

    public int getInMessageCount(){

        Integer inMsgtotalCount = SPUtils.getInt(MainService.this, "MSG_IN", 0);


        return inMsgtotalCount;
    }
    public int getOutMessageCount(){

        Integer outMsgtotalCount = SPUtils.getInt(MainService.this, "MSG_OUT", 0);


        return outMsgtotalCount;
    }

    public class myBinder extends Binder {

        public int   getInCount(){

         return getInMessageCount();
        }

        public int   getOutCount(){

            return getOutMessageCount();
        }


    }


    }

