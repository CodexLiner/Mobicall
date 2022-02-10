package com.mobicall.call.stateManager;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mobicall.call.services.DrawWindow;

public class CallState extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            String phoneState = extras.getString(TelephonyManager.EXTRA_STATE);
            Log.d(TAG, " MYCallState: " + phoneState);

            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                DrawWindow drawWindow = new DrawWindow(context);
                String number = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                drawWindow.open("incoming" , number);
//                Constants.isWindowOpen = true;
            }else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)/*&& pref.getInt("numOfCalls",1)==1*/) {
                Log.d(TAG, "MYCallState: "+Constants.isWindowOpen);
                DrawWindow drawWindow = new DrawWindow(context);
                String number = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                drawWindow.open("outgoing" , "+919399846909");
//                Constants.isWindowOpen = true;

            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//                callTask callTask = new callTask(Constants.windowContact ,  Constants.indexValue , context);
//                callTask.execute();
                Log.d(TAG, "MYCallState: stopped "+Constants.indexValue);
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
