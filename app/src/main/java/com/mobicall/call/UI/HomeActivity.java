package com.mobicall.call.UI;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mobicall.call.R;
import com.mobicall.call.UI.Fragments.CustomerFragment;
import com.mobicall.call.UI.Fragments.HomeFragment;
import com.mobicall.call.UI.Fragments.MenuFragment;
import com.mobicall.call.database.callHistory;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.databinding.ActivityHomeBinding;
import com.mobicall.call.models.contacts;
import com.mobicall.call.models.totalCount;
import com.mobicall.call.others.staticFunctions;
import com.mobicall.call.services.DrawWindow;
import com.mobicall.call.services.ForegroundService;
import com.mobicall.call.services.PermisionClass;
import com.mobicall.call.services.callTask;
import com.mobicall.call.stateManager.CallState;
import com.mobicall.call.stateManager.Constants;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    Calendar myCalendar =Calendar.getInstance();
    SimpleDateFormat simpleDateFormat ;
    boolean swiping = false;
    static String startDateString = null;
    static String endDateString = null;
    static String initialDate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkOverlayPermission();
        PermissionCheck();
        startService();
        updateLabel();
        verifyUser(this);
        getTotalCounts(null , null);
        staticFunctions.compare(this);

        setCurrentUser(getApplicationContext());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd" , Locale.US);;
        startDateString = simpleDateFormat.format(Calendar.getInstance().getTime());
        initialDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        startService(new Intent(HomeActivity.this , CallState.class));
        // home fragment
//        HomeFragment fragment=new HomeFragment();
//        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.content,fragment,"");
//        fragmentTransaction.commit();
        binding.bottomNav.setSelectedItemId(R.id.homeMenu);
        binding.bottomNav.setOnItemSelectedListener(
        new NavigationBarView.OnItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.customerList:
                {
                  startActivity(new Intent(HomeActivity.this, CustomerActivity.class));
                  overridePendingTransition(0,0);
                  break;
//                    CustomerFragment customerFragment = new CustomerFragment();
//                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction1.replace(R.id.content, customerFragment , "");
//                    fragmentTransaction1.commit();
//                    return true;

                }
                case R.id.homeMenu:{
//                    HomeFragment fragment=new HomeFragment();
//                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.content,fragment,"");
//                    fragmentTransaction.commit();
                    return true;
                }
              case R.id.menuMore:
                {
                    startActivity(new Intent(HomeActivity.this, MenuActivity.class));
                    overridePendingTransition(0,0);
                    break;
//                    MenuFragment menuFragment = new MenuFragment();
//                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction2.replace(R.id.content, menuFragment, "");
//                    fragmentTransaction2.commit();

                }

            }
            return true;
          }
        });
//        date picker
        DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                binding.startLayout.setEnabled(true);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd MMM yy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                binding.StartDate.setText(dateFormat.format(myCalendar.getTime()));
                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd" , Locale.US);
                startDateString = newFormat.format(myCalendar.getTime());
                getTotalCounts(initialDate , endDateString);
            }
        };
        DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                binding.EndDateLayout.setEnabled(true);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd MMM yy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                binding.EndDate.setText(dateFormat.format(myCalendar.getTime()));
                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd" , Locale.US);
                endDateString = newFormat.format(myCalendar.getTime());
                binding.clearFilter.setVisibility(View.VISIBLE);
                getTotalCounts(startDateString , endDateString);
            }
        };
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiping = true;
                getTotalCounts(null , null);
            }
        });
        binding.startLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.startLayout.setEnabled(false);
                new DatePickerDialog(HomeActivity.this,startDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                binding.startLayout.setEnabled(true);
            }
        });
        binding.EndDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.EndDateLayout.setEnabled(false);
                new DatePickerDialog(HomeActivity.this,endDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                binding.EndDateLayout.setEnabled(true);
            }
        });
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomeActivity.this)
                        .setMessage("Are you sure you want to logout?")
                        .setTitle("Alert!").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOut();
                    }
                }).setNegativeButton("NO", null).show();

            }
        });
        binding.totalCalled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.clearFilter.setVisibility(View.GONE);
                getTotalCounts(null , null);
            }
        });

        binding.totalIntraction.setOnClickListener((View v)->{
            Intent intent = new Intent(this , CustomerActivity.class);
            intent.putExtra("vargs" , "");
            startActivity(intent);
            overridePendingTransition(0,0);
        });
        binding.conn.setOnClickListener((View v)->{
            Intent intent = new Intent(this , CustomerActivity.class);
            intent.putExtra("vargs" , "connected");
            startActivity(intent);
            overridePendingTransition(0,0);
        });
        binding.notconn.setOnClickListener((View v)->{
            Intent intent = new Intent(this , CustomerActivity.class);
            intent.putExtra("vargs" , "not connected");
            startActivity(intent);
            overridePendingTransition(0,0);
        });
        binding.inte.setOnClickListener((View v)->{
            Intent intent = new Intent(this , CustomerActivity.class);
            intent.putExtra("vargs" , "1");
            startActivity(intent);
            overridePendingTransition(0,0);
        });
        binding.mLayout2.setOnClickListener((View v)->{
            Intent intent = new Intent(this , CustomerActivity.class);
            intent.putExtra("vargs" , "0");
            startActivity(intent);
            overridePendingTransition(0,0);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNav.setSelectedItemId(R.id.homeMenu);
    }

    private void getTotalCounts(String start , String end) {
        userDatabaseHelper db = new userDatabaseHelper(getApplicationContext());
        userDatabaseModel model = db.getUser(0);
        if (model.getAuth()==null){
            return;
        }
        Log.d("TAG", "getTotalCounts: "+start);
        Map<String , String> map = new HashMap<>();
        map.put("from_date" , start);
        map.put("to_date" , end);
        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        final RequestBody requestBody = RequestBody.create(jsonString , MediaType.get(Constants.mediaType));
        Request request = new Request.Builder().url(Constants.baseUrlbackend +"overview").addHeader("authorization" , "Bearer "+model.getAuth()).post(requestBody).build();
        new OkHttpClient()
                .newCall(request)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.d("TAG", "onFailure: a" + e);
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response)
                                    throws IOException {
                                Type listType = new TypeToken<List<contacts>>() {}.getType();
                                try {
                                    JSONObject jsonResponse = new JSONObject(response.body().string());
                                    Type type = new TypeToken<List<contacts>>(){}.getType();
                                    totalCount count = gson.fromJson(jsonResponse.toString(), totalCount.class);
                                  runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          if (swiping){
                                              swiping= false;
                                              binding.clearFilter.setVisibility(View.GONE);
                                          }
                                          binding.swipe.setRefreshing(false);
                                          binding.totalCalled.setText(count.getTotal());
                                          binding.notConnected.setText(count.getNot_connected());
                                          binding.connected.setText(count.getConnected());
                                          binding.intrested.setText(count.getInterested());
                                          binding.notIntrested.setText(count.getNot_interested());
                                      }
                                  });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
    }
    private void verifyUser(HomeActivity context) {
        userDatabaseHelper db = new userDatabaseHelper(context);
        userDatabaseModel user = db.getUser(0);
        if (user!=null){
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm" , Locale.US);
                final Date endTime = sdf.parse(user.getTo());
                final Date startTime = sdf.parse(user.getFrom());
                String et , st;
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("K:mm: a" , Locale.US);
                et = simpleDateFormat1.format(endTime);
                st = simpleDateFormat1.format(startTime);
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      String slot = "( "+ st+" To "+et+" )";
                      binding.userSlot.setText(slot);
                  }
              });
            } catch (final ParseException e) {
                e.printStackTrace();
            }
            Log.d("TAG", "verifyUser: "+user.toString());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getContact(){
    Gson gson = new Gson();
    userDatabaseHelper db = new userDatabaseHelper(getApplicationContext());
    userDatabaseModel model = db.getUser(0);
    if (model.getAuth()==null){
        return;
    }
        staticFunctions.compare(getApplicationContext());
    Request request = new Request.Builder().url(Constants.baseUrlbackend +"contacts").addHeader("authorization" , "Bearer "+model.getAuth()).get().build();
    new OkHttpClient()
            .newCall(request)
            .enqueue(
                    new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.d("TAG", "onFailure: a" + e);
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response)
                                throws IOException {
                            Type listType = new TypeToken<List<contacts>>() {}.getType();
                            try {
                                JSONObject jsonResponse = new JSONObject(response.body().string());
                                Type type = new TypeToken<List<contacts>>(){}.getType();
                                List<contacts> contactList = gson.fromJson(jsonResponse.optString("contacts").toString(), type);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast = new Toast(HomeActivity.this);
                                        View view = getLayoutInflater().inflate(R.layout.toast_layout , findViewById(R.id.mainLayout));
                                        toast.setView(view);
//                                        toast.setMargin(0f , 10f);
                                        toast.setGravity(Gravity.BOTTOM, 0, 150);
                                        toast.setDuration(Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        startCallservice(contactList);
                                    }
                                },1000);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
    }
    private void startCallservice( List<contacts> contactList) {
        DrawWindow.setInWindow(contactList);
        Constants.indexValue = 0;
        Constants.windowContact = contactList;
        callTask callTask = new callTask(contactList, 0 , getApplicationContext());
        callTask.execute();
    }
    private void logOut() {
        userDatabaseHelper db = new userDatabaseHelper(getApplicationContext());
        startActivity(new Intent(getApplicationContext() , MainActivity.class));
        finishAffinity();
        db.delete(0);
    }

    private void updateLabel(){
        String myFormat="dd MMM yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
//        binding.StartDate.setText(dateFormat.format(myCalendar.getTime()));
        binding.EndDate.setText(dateFormat.format(myCalendar.getTime()));
    }
    private void setCurrentUser(Context context) {
        userDatabaseHelper db = new userDatabaseHelper(context);
        userDatabaseModel user =db.getUser(0);
        if (user!=null){
            Log.d("TAG", "setCurrentUser: "+user.toString());
            binding.userName.setText(user.getName());
            binding.userMobile.setText(user.getMobile());
            binding.CompanyName.setText(user.getcName());
        }
    }

    public void checkOverlayPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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
            getContact();
        }
    }
    public void startService(){
        // check if the user has already granted
        // the Draw over other apps permission

        if(Settings.canDrawOverlays(this)) {
            // start the service based on the android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, ForegroundService.class));
            } else {
                startService(new Intent(this, ForegroundService.class));
            }
        }
    }

    private void PermissionCheck() {
        if (PermisionClass.hasPermision(HomeActivity.this , PermisionClass.permisions)){
//            button2.setText("Enable Notification");
        }else{
            if (!PermisionClass.hasPermision(HomeActivity.this , PermisionClass.permisions)){
                ActivityCompat.requestPermissions(HomeActivity.this, PermisionClass.permisions, 0);
            }
        }
    }


    boolean doubleBackToExitPressedOnce = false;
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                },
                2000);
    }
}