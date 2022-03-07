package com.mobicall.call.stateManager;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobicall.call.R;
import com.mobicall.call.UI.HomeActivity;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.models.contacts;
import com.mobicall.call.others.staticFunctions;
import com.mobicall.call.services.DrawWindow;
import com.mobicall.call.services.UpdaterClass;
import com.mobicall.call.services.callTask;

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
       if (Constants.drawWindow==null){
           Log.d(TAG, "callstate: created ");
         //  Constants.drawWindow = new DrawWindow(context);
       }
        try {
            Bundle extras = intent.getExtras();
            String phoneState = extras.getString(TelephonyManager.EXTRA_STATE);
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String number = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    if (staticFunctions.compare(context) && Constants.isLogin){
//                        if (Constants.byCallTask){
//                            Constants.drawWindow.open("" , "");
//                        }else {
//                            if (extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)!=null){
//                                staticFunctions.getContactinfo(extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER) , "incoming", context.getApplicationContext());
//                                Thread.sleep(2000);
//                                Constants.drawWindow.openWith();
//                            }
//                        }
//                    }
//                }
            }else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)/*&& pref.getInt("numOfCalls",1)==1*/) {
                staticFunctions.getLastNumber(context);
                Log.d(TAG, "callstate outgoin: ");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    if (staticFunctions.compare(context) && Constants.isLogin){
//                        if (Constants.byCallTask){
//                            Constants.drawWindow.open("outgoing call" , "+919399846909");
//                        }else {
//                            if (extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)!=null){
//                                staticFunctions.getContactinfo(extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER) , "outgoing", context);
//                                Constants.drawWindow.openWith();
//                            }
//                        }
//                    }
//                }
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                DrawWindow drawWindow = new DrawWindow(context);
                Thread.sleep(1000);
                String time = staticFunctions.getLastCallTime(context , null);
                Log.d(TAG, "onReceive: "+time);
                Pair<String , String> pair = staticFunctions.getLastCallTime(context);
                Constants.MN = pair.first;
                if (time!=null && time.equals("0")){
                    UpdaterClass updaterClass = new UpdaterClass(null ,
                            null ,
                            null, "not connected" , null, context ,
                            null
                            ,pair.first
                            ,null);
                    updaterClass.execute();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (Constants.byCallTask){
                                Constants.byCallTask = false;
                                if (Constants.indexValue - 1 < Constants.windowContact.size()){
                                    Constants.windowContact.get(Constants.indexValue -1).setCall_status("not connected");
                                    Constants.CustomerList = Constants.windowContact;
                                }
                                callTask callTask = new callTask(Constants.windowContact ,  Constants.indexValue , context);
                                callTask.execute();
                            }
                        }
                    },5000);
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (staticFunctions.compare(context) && Constants.isLogin){
                        if (time!=null && !time.equals("0")){
                            if (pair!=null && pair.first != null && pair.second!=null){
                                String type = "none";

                                if (pair.second.equals("1")){
                                    type = "Incoming Call";
                                }else if (pair.second.equals("2")){
                                    type = "Outgoing Call";
                                }
                                Constants.UserDetails = null;
                                drawWindow.open(type , pair.first);
                            }
                        }
                    }
                }
//                if (time!=null){
//                    if (time.equals("0")){
//                      if (pair!=null && pair.first!=null){
//                          UpdaterClass updaterClass = new UpdaterClass(null ,
//                                  null ,
//                                  "0", "not connected" , null, context ,
//                                  null
//                                  ,pair.first
//                                  ,null);
//                          updaterClass.execute();
//                      }if (Constants.isWindowOpen){
//                            Thread.sleep(1000);
//                            getContact(context);
//                            Toast.makeText(context, "next call will starts in 5 Seconds", Toast.LENGTH_SHORT).show();
//                            Constants.drawWindow.close();
//                        } //   Constants.first = true;
//
//                    }
//                }
            }

        } catch (Exception e) {
            Log.d(TAG, "onReceive: "+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
    private void getContact(Context context){
        Gson gson = new Gson();
        userDatabaseHelper db = new userDatabaseHelper(context);
        userDatabaseModel model = db.getUser(0);
        if (model.getAuth()==null){
            return;
        }
        Request request = new Request.Builder().url(Constants.baseUrlbackend +"contacts").addHeader("authorization" , "Bearer "+model.getAuth()).get().build();
        new OkHttpClient()
                .newCall(request)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.d("TAG", "calltask: a" + e);
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response)
                                    throws IOException {
                                Type listType = new TypeToken<List<contacts>>() {}.getType();
                                try {
                                    JSONObject jsonResponse = new JSONObject(response.body().string());
                                    Type type = new TypeToken<List<contacts>>(){}.getType();
                                    List<contacts> contactList = gson.fromJson(jsonResponse.optString("contacts").toString(), type);
                                    Constants.CustomerList = contactList;
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            startCallservice(contactList , context);
                                        }
                                    },1000);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
    }
    private void startCallservice(List<contacts> contactList, Context context) {
        DrawWindow.setInWindow(contactList);
        Constants.windowContact = contactList;
        Log.d("TAG", "calltask: service ");
        callTask callTask = new callTask(contactList, 0 , context);
        callTask.execute();
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
