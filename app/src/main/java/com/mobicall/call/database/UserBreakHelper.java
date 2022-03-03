package com.mobicall.call.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class UserBreakHelper extends SQLiteOpenHelper {

    public UserBreakHelper(@Nullable Context context) {
        super(context, "USERLOG" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE " + "BreakStatus" + "("
                        + "COLUMN_ID" + " INTEGER,"
                        + "STATUS" + " TEXT"
                        + ")";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "BreakStatus");
    }
    public void addBreakStatus(int ids , String status){
        try{
            bankBreakModel c = getStatus(ids);
            if (c!= null && c.getCOLUMN_ID()==ids){
                delete(ids);
            }
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("STATUS", status);
            values.put("COLUMN_ID", 1);
            long id = db.insert("BreakStatus", null, values);
            // close db connection
            db.close();
            // return newly inserted row id

        }catch (Exception e){
         e.printStackTrace();
        }
    }
    public void delete(int id)
    {
       try {
           String[] args={String.valueOf(1)};
           int x  =getWritableDatabase().delete("BreakStatus", "COLUMN_ID=?", args);
           Log.d("TAG", "addBreakStatus: rmvd "+x);
       }catch (Exception e){
           Log.d("TAG", "addBreakStatus: rmvd "+e);
       }
    }
    public bankBreakModel getStatus(int ids){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(
                  "BreakStatus",
                    new String[]{
                        "COLUMN_ID" , "STATUS"  } ,
                    "COLUMN_ID"  +"=?" ,
                    new String[]{"1"} , null , null ,null , null);
            if (cursor!=null){
                cursor.moveToFirst();
            }

            bankBreakModel model = new bankBreakModel(
               cursor.getInt(cursor.getColumnIndex("COLUMN_ID")),
               cursor.getString(cursor.getColumnIndex("STATUS")) );
            cursor.close();
            return model;
        }catch (Exception e){
          e.printStackTrace();
            return null;
        }
    }
}
