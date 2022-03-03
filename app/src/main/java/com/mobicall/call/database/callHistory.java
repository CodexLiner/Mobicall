package com.mobicall.call.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import com.mobicall.call.models.CallHistory;

import java.util.ArrayList;
import java.util.List;

public class callHistory {
    public static List<CallHistory> getLogsByNumber(Context context , String[] strNumber ) {
        if (strNumber[0].length()<9){
            if (strNumber[0].startsWith("+")){
                strNumber[0] = strNumber[0].substring(3);
            }
        }else if (strNumber[0].length()==10  && !strNumber[0].startsWith("+")){
            strNumber[0] = "+91"+strNumber[0];
        }
        List<CallHistory> list = new ArrayList<>();
        try (Cursor cursor = context.getContentResolver().query(Uri.parse("content://call_log/calls"), null, CallLog.Calls.NUMBER + " = ? ", strNumber, CallLog.Calls.DATE + " DESC")) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID));
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                    String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    String time = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                  //  Log.d("TAG", "getLogsByNumber: " + id + " " + number + " " + name + " " + callDate);
                    CallHistory callHistory = new CallHistory(number, name, callDate, time, type);
                    list.add(callHistory);
                }

            }
        }
        return list;
    }
}
