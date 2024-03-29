package com.mobicall.call.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.mobicall.call.stateManager.Constants;

public class ActionReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();

        boolean action=intent.getBooleanExtra("isLogin" , false);

        if(Constants.isLogin){
            performAction1();
            Log.d("TAG", "performAction1: ");
            Intent i = new Intent(context , ForegroundService.class);
            i.putExtra("name" , "End Break");
            context.startService(i);
        }
        else {
            Log.d("TAG", "performAction: ");
            performAction2();
            Intent i = new Intent(context , ForegroundService.class);
            i.putExtra("name" , "Take Break");
            context.startService(i);
        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void performAction1(){

        Constants.isLogin = false;
        Constants.NOTIFICATION = "End Break";

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void performAction2(){
        Constants.isLogin = true;
        Constants.NOTIFICATION = "Take Break";

    }

}