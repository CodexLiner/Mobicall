package com.mobicall.call.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.mobicall.call.R;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.databinding.ActivityCustomerBinding;
import com.mobicall.call.models.contacts;
import com.mobicall.call.recyclerViews.CustomerAdapter;
import com.mobicall.call.stateManager.Constants;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomerActivity extends AppCompatActivity {
    ActivityCustomerBinding binding;
    Calendar myCalendar =Calendar.getInstance();
    RecyclerView.LayoutManager layoutManager;
    CustomerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateLabel();
        userDatabaseHelper db = new userDatabaseHelper(this);
        userDatabaseModel model = db.getUser(0);
        if (model!=null){
             getContact();
        }

        layoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        binding.customerRecycler.setLayoutManager(layoutManager);
        //bottom nav bar
        binding.bottomNav.setSelectedItemId(R.id.customerList);
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.homeMenu:
                {
                    startActivity(new Intent(getApplicationContext() , HomeActivity.class));
                    overridePendingTransition(0,0);
                    finishAffinity();
                    break;
                }
                case R.id.menuMore:
                {
                    startActivity(new Intent(getApplicationContext() , MenuActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    break;
                }
            }
            return true;
        }
    });

        //        date picker
        DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd MMM yy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                binding.StartDate.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd MMM yy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                binding.EndDate.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        binding.StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CustomerActivity.this,startDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        binding.EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CustomerActivity.this,endDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);
    }

    private void updateLabel(){
        String myFormat="dd MMM yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        binding.StartDate.setText(dateFormat.format(myCalendar.getTime()));
        binding.EndDate.setText(dateFormat.format(myCalendar.getTime()));
    }
    private void getContact(){
        Gson gson = new Gson();
        userDatabaseHelper db = new userDatabaseHelper(getApplicationContext());
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
                Log.d("TAG", "onFailure: a" + e);
              }

              @Override
              public void onResponse(@NonNull Call call, @NonNull Response response)
                  throws IOException {
                Type listType = new TypeToken<List<contacts>>() {}.getType();
//                List<contacts> model = gson.fromJson(response.body().string(), listType);
                  try {
                      JSONObject jsonResponse = new JSONObject(response.body().string());
                      Type type = new TypeToken<List<contacts>>(){}.getType();
                      List<contacts> contactList = gson.fromJson(jsonResponse.optString("contacts").toString(), type);

                      adapter = new CustomerAdapter(contactList);
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             binding.customerRecycler.setAdapter(adapter);
                         }
                     });

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }

              }
            });
    }
}