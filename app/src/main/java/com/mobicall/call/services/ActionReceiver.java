package com.mobicall.call.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.mobicall.call.UI.HomeActivity;
import com.mobicall.call.database.UserBreakHelper;
import com.mobicall.call.database.bankBreakModel;
import com.mobicall.call.stateManager.Constants;

public class ActionReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        UserBreakHelper userBreakHelper = new UserBreakHelper(context);
        bankBreakModel model = userBreakHelper.getStatus(1);
        if (model!=null && model.getSTATUS().equals("start")){
            userBreakHelper.addBreakStatus(1 , "end");
            Toast.makeText(context,"Break Started", Toast.LENGTH_SHORT).show();
        }else {
            userBreakHelper.addBreakStatus(1 , "start");
            Toast.makeText(context,"Break Ended", Toast.LENGTH_SHORT).show();
        }
        boolean action=intent.getBooleanExtra("isLogin" , false);
//        if(Constants.isLogin){
//            Toast.makeText(context,"Break Started", Toast.LENGTH_SHORT).show();
//            performAction1();
//            userBreakHelper.addBreakStatus(1 , "start");
//            Intent i = new Intent(context , ForegroundService.class);
//            i.putExtra("name" , "End Break");
//            context.startService(i);
//        }
//        else {
//            userBreakHelper.addBreakStatus(1 , "end");
//            Toast.makeText(context,"Break Ended", Toast.LENGTH_SHORT).show();
//            performAction2();
//            Intent i = new Intent(context , ForegroundService.class);
//            i.putExtra("name" , "Take Break");
//            context.startService(i);
//        }

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