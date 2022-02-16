package com.mobicall.call.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.mobicall.call.models.contacts;
import com.mobicall.call.others.staticFunctions;
import com.mobicall.call.stateManager.Constants;

import java.util.List;

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
        Constants.windowContact = contacts;
        Constants.indexValue = index + 1;
        Log.d("TAG", "doInBackground:i "+index);
        DrawWindow.setInWindow(contacts);
        if (staticFunctions.compare(context) && Constants.isLogin){
            if (!Constants.isWindowOpen){
                if (index <= contacts.size() - 1 && contacts.get(index)!=null && context!=null){
                    Log.d("TAG", "doInBackground: "+contacts.get(index).getCall_status());
                    if (contacts.get(index).getCall_status()==null ||  !contacts.get(index).getCall_status().equals("connected")){
                        Constants.byCallTask = true;
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        callIntent.setData(Uri.parse("tel:"+contacts.get(index).getPhone()));
                        context.startActivity(callIntent);
                    }else {
                        callTask callTask = new callTask(contacts, index+1 , context);
                        callTask.doInBackground();
                    }
                }
            }
        }
        return null;
    }
}
