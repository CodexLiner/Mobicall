package com.mobicall.call.UI;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.mobicall.call.R;
import com.mobicall.call.databinding.ActivityPermisionBinding;
import com.mobicall.call.services.PermisionClass;

public class PermisionActivity extends AppCompatActivity {
    ActivityPermisionBinding binding;
    private static int STATUS = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermisionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("permission" , MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("pm",false)){
            Log.d(TAG, "onperms: ");
            overlayPermission();
        }
        if (sharedPreferences!=null){
            if (sharedPreferences.contains("perms")){
                if (sharedPreferences.getBoolean("perms", false)){
                    if (sharedPreferences.contains("overlay")){
                        if (sharedPreferences.getBoolean("overlay", false)){
                            if (sharedPreferences.getBoolean("auto" , false)){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                overridePendingTransition(0,0);
                                finishAffinity();
                            }else {
                                enableAuto(this);
                            }
                        }
                    }
                }
            }
        }
        binding.buttonPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editor.putBoolean("pm" , true);
                    editor.commit();
                    checkPermission();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void overlayPermission() {
        Log.d(TAG, "overlayPermission: ss");
        AlertDialog.Builder builder = new AlertDialog.Builder(PermisionActivity.this);
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

        }else {
            Log.d(TAG, "overlayPermission: dd");
              if (sharedPreferences.contains("auto") && sharedPreferences.getBoolean("auto", false)){
                  startActivity(new Intent(getApplicationContext(),MainActivity.class));
                  overridePendingTransition(0,0);
                  finishAffinity();
              }else {
                  enableAuto(getApplicationContext());
            }
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            checkPermission();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void checkPermission() throws InterruptedException {
        if (PermisionClass.hasPermision(PermisionActivity.this , PermisionClass.permisions)){
            STATUS = 1;
            editor.putBoolean("perms" , true);
            editor.commit();
            overlayPermission();
        }else if (!PermisionClass.hasPermision(PermisionActivity.this , PermisionClass.permisions)){
                ActivityCompat.requestPermissions(PermisionActivity.this, PermisionClass.permisions, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED && requestCode == 0){
            STATUS = 1;
            editor.putBoolean("perms" , true);
            editor.commit();
            overlayPermission();
        }
    }

    private void enableAuto(Context context) {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Dialog dialog = new Dialog(PermisionActivity.this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = width - 30;
        dialog.setContentView(R.layout.auto_layout);
        dialog.show();
        dialog.findViewById(R.id.buttonSubmit).setOnClickListener((View v)->{
            AutoStartPermissionHelper.Companion.getInstance().getAutoStartPermission(context , true , true);
            editor.putBoolean("auto" , true);
            editor.apply();
        });
        dialog.findViewById(R.id.closeButton).setOnClickListener((View v)->{
            dialog.dismiss();
            editor.putBoolean("auto" , true);
            editor.apply();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            overridePendingTransition(0,0);
            finishAffinity();
        });
    }

    private void isAccess()  {
        final String aName = getPackageName()+"/services.AccessService";
        Log.d(TAG, "isAccess: "+aName);
        boolean status = false;
        int code = 0;
        try {
            code = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "isAccess: " + code);
        if (code==1){
            TextUtils.SimpleStringSplitter mStrin = new TextUtils.SimpleStringSplitter(':');
            String setVal = Settings.Secure.getString(getContentResolver() , Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (setVal!=null){
                TextUtils.SimpleStringSplitter splitter = mStrin;
                splitter.setString(setVal);
                while (splitter.hasNext()){
                    String s = splitter.next();
                    if (s.equals(aName)){
                        editor.putBoolean("access" , true);
                        editor.apply();
                        Log.d(TAG, "isAccess: sas" + s);
                        return;
                    }
                }
            }
        }
    }

}