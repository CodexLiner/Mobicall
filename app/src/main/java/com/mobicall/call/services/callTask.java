package com.mobicall.call.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.mobicall.call.database.UserBreakHelper;
import com.mobicall.call.database.bankBreakModel;
import com.mobicall.call.models.contacts;
import com.mobicall.call.others.staticFunctions;
import com.mobicall.call.stateManager.Constants;

import java.util.List;
import java.util.Locale;

public class callTask extends AsyncTask<Void , Void , Void> {
    List<contacts> contacts ;
    int index = 0;
    private final Context context;

    public callTask(List<com.mobicall.call.models.contacts> contacts, int index, Context context) {
        this.contacts = contacts;
        this.index = index;
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        String callStatus = null;
        if (index<contacts.size() && contacts.get(index).getCall_status()!=null){
          callStatus = contacts.get(index).getCall_status().toLowerCase(Locale.ROOT);
        }
        UserBreakHelper userBreakHelper = new UserBreakHelper(context);
        bankBreakModel model = userBreakHelper.getStatus(1);
        Constants.windowContact = contacts;
        Constants.indexValue = index + 1;
        DrawWindow.setInWindow(contacts);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (staticFunctions.compare(context) && model!=null && model.getSTATUS().equals("start")){
                if (!Constants.isWindowOpen){
                    if (index <= contacts.size() - 1 && contacts.get(index)!=null && context!=null){
                        if (callStatus!=null && !callStatus.equalsIgnoreCase("connected")){
                            if (!callStatus.equalsIgnoreCase("not connected")){
                                Constants.byCallTask = true;
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                callIntent.setData(Uri.parse("tel:"+contacts.get(index).getPhone()));
                                context.startActivity(callIntent);
                            }else {
                                callTask callTask = new callTask(contacts, index+1 , context);
                                callTask.doInBackground();
                            }
                        }else if (callStatus==null){
                            Constants.byCallTask = true;
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            callIntent.setData(Uri.parse("tel:"+contacts.get(index).getPhone()));
                            context.startActivity(callIntent);
                        }else {
                            callTask callTask = new callTask(contacts, index+1 , context);
                            callTask.doInBackground();
                            Log.d("TAG", "doInBackground: is else  ");
                        }
                    }
                }
            }
        }
        return null;
    }
}
