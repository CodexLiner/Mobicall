package com.mobicall.call.stateManager;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.models.contacts;
import com.mobicall.call.others.staticFunctions;
import com.mobicall.call.services.DrawWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallState extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            String phoneState = extras.getString(TelephonyManager.EXTRA_STATE);
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                DrawWindow drawWindow = new DrawWindow(context);
                String number = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (Constants.byCallTask){
                    drawWindow.open("" , "");
                }else {
                    if (extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)!=null){
                        staticFunctions.getContactinfo(extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER) , "incoming", context.getApplicationContext());
                        Thread.sleep(2000);
                        drawWindow.openWith();
                    }
                }
//                Constants.isWindowOpen = true;
            }else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)/*&& pref.getInt("numOfCalls",1)==1*/) {
                staticFunctions.getLastNumber(context);
                DrawWindow drawWindow = new DrawWindow(context);
                if (Constants.byCallTask){
                    drawWindow.open("outgoing call" , "+919399846909");
                }else {
                    if (extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)!=null){
                        staticFunctions.getContactinfo(extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER) , "outgoing", context);
                        drawWindow.openWith();
                    }
                }
//                Constants.isWindowOpen = true;

            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//                callTask callTask = new callTask(Constants.windowContact ,  Constants.indexValue , context);
//                callTask.execute();
            }

        } catch (Exception e) {
            Log.d(TAG, "onReceive: "+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

//    private void dispacthExtra(Context context, Intent intent, String phoneNumber, String extraState) {
//        if (extraState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(new Intent(context, RecorderService.class).putExtra("commandType", Constants.STATE_CALL_START));
//            } else {
//                context.startService(new Intent(context, RecorderService.class)
//                        .putExtra("commandType", Constants.STATE_CALL_START));
//            }
//        } else if (extraState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(new Intent(context, RecorderService.class)
//                        .putExtra("commandType", Constants.STATE_CALL_END));
//            } else {
//                context.startService(new Intent(context, RecorderService.class)
//                        .putExtra("commandType", Constants.STATE_CALL_END));
//            }
//        } else if (extraState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//            if (phoneNumber == null)
//                phoneNumber = intent.getStringExtra(
//                        TelephonyManager.EXTRA_INCOMING_NUMBER);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(new Intent(context, RecorderService.class)
//                        .putExtra("commandType", Constants.STATE_INCOMING_NUMBER)
//                        .putExtra("phoneNumber", phoneNumber));
//            } else {
//                context.startService(new Intent(context, RecorderService.class)
//                        .putExtra("commandType", Constants.STATE_INCOMING_NUMBER)
//                        .putExtra("phoneNumber", phoneNumber));
//            }
//        }
//    }
}
