package com.mobicall.call.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mobicall.call.R;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.databinding.ActivityMainBinding;
import com.mobicall.call.models.LoginVerify;
import com.mobicall.call.models.user;
import com.mobicall.call.services.PermisionClass;
import com.mobicall.call.stateManager.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Arasko);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PermissionCheck();
        checkOverlayPermission();
        userDatabaseHelper db = new userDatabaseHelper(getApplicationContext());
        if (db.getUser(0)!=null){
            startActivity(new Intent(getApplicationContext() , HomeActivity.class));
            overridePendingTransition(0,0);
            finish();
        }
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  mPass = binding.userPassword.getText().toString().trim();
                String mNumber = binding.userMobile.getText().toString().trim();
                if (TextUtils.isEmpty(mNumber)){
                    return;
                }
                if (TextUtils.isEmpty(mPass)){
                    return;
                }
                loginVerify(mNumber , mPass);
//                startActivity(new Intent(getApplicationContext() , HomeActivity.class));
            }
        });
    }
    private void loginVerify(String mobile , String passWord){
        binding.loginButton.setEnabled(false);
        Map<String , String> map = new HashMap<>();
        map.put("phone" , mobile.toString().trim());
        map.put("password" , passWord.toString().trim());
        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        final RequestBody requestBody = RequestBody.create(jsonString , MediaType.get(Constants.mediaType));
        Request request = new Request.Builder().url(Constants.baseUrlbackend +"login").post(requestBody).build();
    new OkHttpClient()
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                runOnUiThread(
                    new Runnable() {
                      @Override
                      public void run() {
                        binding.loginButton.setEnabled(true);
                      }
                    });
              }

              @Override
              public void onResponse(@NonNull Call call, @NonNull Response response)
                  throws IOException {
                Type listType = new TypeToken<List<LoginVerify>>() {}.getType();
                // LoginVerify model = gson.fromJson(response.body().string(),listType);
                Log.d("TAG", "onResponse: a");
                try {
                  JSONObject jsonResponse = new JSONObject(response.body().string());
                      Log.d("TAG", "onResponse: a2");
                    user user =
                        gson.fromJson(jsonResponse.getJSONObject("user").toString(), user.class);
                    String token;
                    Log.d("TAG", "onResponse: a" + jsonResponse.optString("token"));
                    jsonResponse.optString("token");
                    token = jsonResponse.optString("token").toString().trim();
                    userDatabaseHelper db = new userDatabaseHelper(getApplicationContext());
                    db.insertUser(user.getPhone(), user.getName(), user.getEmail(), token, 0);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();

                  runOnUiThread(
                      new Runnable() {
                        @Override
                        public void run() {
                          binding.loginButton.setEnabled(true);
                          Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT)
                              .show();
                        }
                      });

                } catch (Exception e) {
                  Log.d("TAG", "onResponse: " + e);
                  runOnUiThread(
                      new Runnable() {
                        @Override
                        public void run() {
                          binding.loginButton.setEnabled(true);
                          Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT)
                              .show();
                        }
                      });
                }
              }
            });
    }
    public void checkOverlayPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Overlay Permission Required ");
        builder.setMessage("to turn on click okay to open settings and enable overlay permission");
        if (!Settings.canDrawOverlays(this)) {
            // send user to the device settings
            builder.setNeutralButton("DISMISS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(myIntent);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }
    private void PermissionCheck() {
        if (PermisionClass.hasPermision(MainActivity.this , PermisionClass.permisions)){
//            button2.setText("Enable Notification");
            checkOverlayPermission();
        }else{
            if (!PermisionClass.hasPermision(MainActivity.this , PermisionClass.permisions)){
                ActivityCompat.requestPermissions(MainActivity.this, PermisionClass.permisions, 0);

            }
        }
    }

    @Override
    protected void onRestart() {
        checkOverlayPermission();
        super.onRestart();
    }
}