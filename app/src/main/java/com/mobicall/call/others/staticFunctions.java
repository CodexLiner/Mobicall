package com.mobicall.call.others;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Looper;
import android.provider.CallLog;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobicall.call.R;
import com.mobicall.call.UI.HomeActivity;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.models.OtherCalls;
import com.mobicall.call.models.contacts;
import com.mobicall.call.recyclerViews.CustomerAdapter;
import com.mobicall.call.services.DrawWindow;
import com.mobicall.call.stateManager.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class staticFunctions {
    public static String getLastCallTime(Context context , String mNumber){
        Cursor cur = context.getContentResolver().query( CallLog.Calls.CONTENT_URI,null, null,null, android.provider.CallLog.Calls.DATE + " DESC");

        int number = cur.getColumnIndex( CallLog.Calls.NUMBER );
        int duration = cur.getColumnIndex( CallLog.Calls.DURATION);
        while ( cur.moveToNext() ) {
            String phNumber = cur.getString( number );
            String callDuration = cur.getString( duration );
            return callDuration;
        }
        cur.close();
        return null;
    }
    public static Pair<String , String> getLastCallTime(Context context){
        Cursor cur = context.getContentResolver().query( CallLog.Calls.CONTENT_URI,null, null,null, android.provider.CallLog.Calls.DATE + " DESC");

        int number = cur.getColumnIndex( CallLog.Calls.NUMBER );
        int duration = cur.getColumnIndex( CallLog.Calls.TYPE);
        while ( cur.moveToNext() ) {
            String phNumber = cur.getString( number );
            String callDuration = cur.getString( duration );
            return new Pair<>(phNumber , callDuration);
        }
        cur.close();
        return null;
    }
    public static String getLastNumber(Context context){
        Cursor cur = context.getContentResolver().query( CallLog.Calls.CONTENT_URI,null, null,null, android.provider.CallLog.Calls.DATE + " DESC");

        int number = cur.getColumnIndex( CallLog.Calls.NUMBER );
        int duration = cur.getColumnIndex( CallLog.Calls.DURATION);
        while ( cur.moveToNext() ) {
            String phNumber = cur.getString( number );
            String callDuration = cur.getString( duration );
            Log.d("TAG", "getLastCallTime: "+callDuration +" "+phNumber);
            return callDuration;
        }
        cur.close();
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean compare(Context context) {
        userDatabaseHelper db = new userDatabaseHelper(context);
        userDatabaseModel m = db.getUser(0);
            DateTimeFormatter f = DateTimeFormatter.ofPattern( "HH:mm" );
            LocalTime start = LocalTime.parse(m.getFrom());
            LocalTime end = LocalTime.parse( m.getTo());
            LocalDateTime time = LocalDateTime.now();
            LocalTime obj1 = LocalTime.parse( f.format(time));
        return (start.equals(obj1) || obj1.isAfter(start)) && obj1.isBefore(end);
    }
    public static void getContactinfo(String string ,String callType , Context context) {
        Gson gson = new Gson();
        userDatabaseHelper db = new userDatabaseHelper(context);
        userDatabaseModel model = db.getUser(0);
        if (model.getAuth() == null) {
            return;
        }
        try {
            if (string.startsWith("+")){
                string = string.substring(3);
            }
            String finalString = string;
            Request request = new Request.Builder().url(Constants.baseUrlbackend + "check/user/" +finalString).addHeader("authorization", "Bearer " + model.getAuth()).get().build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response)
                        throws IOException {
                    Type listType = new TypeToken<List<contacts>>() {
                    }.getType();
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        Type type = new TypeToken<List<contacts>>() {}.getType();
                        Log.d(TAG, "onResponses: "+jsonResponse.optString("contact"));
                        if (!jsonResponse.optString("contact").equals("")){
                            contacts user = gson.fromJson(jsonResponse.optString("contact").toString(), contacts.class);
                            if (user.getContact_name()!=null){
                                Constants.UserDetails = user;
                                Constants.otherCalls = new OtherCalls(user.getContact_name() , finalString , callType);
                            }
                        } else {
                            Constants.otherCalls = new OtherCalls(finalString , finalString , callType);
                        }

                    } catch (JSONException e) {
                        Log.d(TAG, "getContactinfo: "+e);
                    }

                }
            });
        } catch (Exception e) {
            Log.d(TAG, "getContactinfo: "+e);
        }
    }
    public static void showToast(Activity context){
        Toast toast = new Toast(context);
        View view = context.getLayoutInflater().inflate(R.layout.toast_layout , context.findViewById(R.id.mainLayout));
        TextView textView = view.findViewById(R.id.toastMsg);
        textView.setText("Next Call Will Starts In 5 Seconds");
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
