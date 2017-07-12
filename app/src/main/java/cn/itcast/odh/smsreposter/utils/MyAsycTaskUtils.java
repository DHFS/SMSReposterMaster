package cn.itcast.odh.smsreposter.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by rhinofly on 2016/11/20 time:12:03.
 */

public class MyAsycTaskUtils extends AsyncTask<String, Integer, Boolean> implements PreferenceManager.OnActivityResultListener {


    private static final int REQUEST_CODE = 100;
    private final Activity activity;
    private ProgressDialog dialog;

    public MyAsycTaskUtils(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String...  params) {

        checkCallingStateFromCarrier();




        return true;
    }

    private void checkCallingStateFromCarrier() {


        String callForwardString = "*#61#";
        Intent intentCallForward = new Intent(Intent.ACTION_CALL); // ACTION_CALL
        Uri uri = Uri.fromParts("tel", callForwardString, "#");
        intentCallForward.setData(uri);

    }


    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("正在检查通信状态");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Boolean s) {
        if (s==true){

         dialog.dismiss();
        }else{

            System.out.println("------My System.out.println----转发服务没有开启--" );
            dialog.dismiss();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==REQUEST_CODE){

            String s = data.toString();

            System.out.println("------My System.out.println----onActivityResultonActivityResultonActivityResult--"  +s);

        }

        return true;
    }
}
