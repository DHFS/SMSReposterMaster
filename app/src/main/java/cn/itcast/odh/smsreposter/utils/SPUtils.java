package cn.itcast.odh.smsreposter.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rhinofly on 2016/11/5 time:21:44.
 */

public class SPUtils {



    public static void saveNumber(Context context,String name, String num) {
        SharedPreferences sp = context.getSharedPreferences("telephone_number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name,num);
        editor.commit();

    }

    public static String  getSavedNumber(Context context , String name ,String defaultValue){

        SharedPreferences sp = context.getSharedPreferences("telephone_number", Context.MODE_PRIVATE);
        return sp.getString(name, defaultValue);
    }




    public static void saveBoolean(Context context,String name, Boolean b) {
        SharedPreferences sp = context.getSharedPreferences("telephone_number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name,b);
        editor.commit();

    }


    public static Boolean  getBoolean(Context context , String name ,Boolean defaultValue){

        SharedPreferences sp = context.getSharedPreferences("telephone_number", Context.MODE_PRIVATE);
        return sp.getBoolean(name,defaultValue);
    }


    public static void saveInt(Context context,String name, Integer i) {
        SharedPreferences sp = context.getSharedPreferences("telephone_number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(name,i);
        editor.commit();

    }


    public static Integer  getInt(Context context , String name ,int defaultValue){

        SharedPreferences sp = context.getSharedPreferences("telephone_number", Context.MODE_PRIVATE);
        return sp.getInt(name,defaultValue);
    }

}
