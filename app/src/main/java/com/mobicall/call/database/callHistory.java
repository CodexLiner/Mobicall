package com.mobicall.call.database;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.mobicall.call.models.CallHistory;

import java.util.ArrayList;
import java.util.List;

public class callHistory {
    public static List<CallHistory> getLogsByNumber(Context context , String[] strNumber ) {
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.NUMBER + " = ? ", strNumber , CallLog.Calls.DATE+" DESC");
        List<CallHistory> list = new ArrayList<>();
        if (cursor.moveToFirst ()) {

            while (cursor.moveToNext ()) {
                String id = cursor.getString (cursor.getColumnIndex (CallLog.Calls._ID));
                String number = cursor.getString (cursor.getColumnIndex (CallLog.Calls.NUMBER));
                String name = cursor.getString (cursor.getColumnIndex (CallLog.Calls.CACHED_NAME));
                String callDate = cursor.getString (cursor.getColumnIndex (CallLog.Calls.DATE));
                String type = cursor.getString (cursor.getColumnIndex (CallLog.Calls.TYPE));
                String time = cursor.getString (cursor.getColumnIndex (CallLog.Calls.DURATION));
                Log.d("TAG", "getLogsByNumber: "+id+" "+number+" "+name+" "+callDate);
                CallHistory callHistory = new CallHistory(number , name , callDate , time , type);
                list.add(callHistory);
            }

        }
        return list;
    }
}
