package com.mobicall.call.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.stateManager.Constants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdaterClass extends AsyncTask<Void , Void , Void> {
    userDatabaseHelper db;
    userDatabaseModel model;
    com.mobicall.call.models.contacts contacts;
    String id , desc , interested , call_status , talktime;

    public UpdaterClass(String id, String desc, String interested, String call_status, String talktime , Context cn) {
        this.id = id;
        this.desc = desc;
        this.interested = interested;
        this.call_status = call_status;
        this.talktime = talktime;
        db = new userDatabaseHelper(cn);
        model = db.getUser(0);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        loginVerify();
        return null;
    }
    private void loginVerify(){
        Map<String , String> map = new HashMap<>();
        if (!(interested == null && desc == null && talktime==null)){
            map.put("interested" , interested);
            map.put("description" , desc);
            map.put("call_status" , call_status);
            map.put("talktime" , talktime);
            map.put("call_recording" , null);
        }else {
            map.put("call_status" , call_status.toString().trim());
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        final RequestBody requestBody = RequestBody.create(jsonString , MediaType.get(Constants.mediaType));
        Request request = new Request.Builder().url(Constants.baseUrlbackend +"contact/"+id+"/edit").post(requestBody).addHeader("authorization" , "Bearer "+model.getAuth()).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure: "+e.getLocalizedMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("TAG", "onFailure: "+response.body().string());
            }
        });

    }
}
