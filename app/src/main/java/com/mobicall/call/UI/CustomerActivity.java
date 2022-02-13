package com.mobicall.call.UI;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Predicates;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.Collections2;
import com.mobicall.call.R;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.databinding.ActivityCustomerBinding;
import com.mobicall.call.models.TemplateModel;
import com.mobicall.call.models.contacts;
import com.mobicall.call.recyclerViews.CustomerAdapter;
import com.mobicall.call.sort.SortByDate;
import com.mobicall.call.sort.SortByDate2;
import com.mobicall.call.sort.SortByName;
import com.mobicall.call.sort.SortByStatus;
import com.mobicall.call.stateManager.Constants;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
class pair{
    LinearLayout id;
    boolean isActive;

    public pair(LinearLayout id, boolean isActive) {
        this.id = id;
        this.isActive = isActive;
    }
}
public class CustomerActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    ActivityCustomerBinding binding;
    Calendar myCalendar =Calendar.getInstance();
    RecyclerView.LayoutManager layoutManager;
    CustomerAdapter adapter;
    SimpleDateFormat simpleDateFormat ;
    static String startDateString = null;
    static String endDateString = null;
    static String initialDate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWhatsapp();
        getEmails();
        updateLabel();
        userDatabaseHelper db = new userDatabaseHelper(this);
        userDatabaseModel model = db.getUser(0);
        if (model!=null){
             getContact();
        }
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContact();
            }
        });
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd" , Locale.US);;
        startDateString = simpleDateFormat.format(Calendar.getInstance().getTime());
        initialDate = simpleDateFormat.format(Calendar.getInstance().getTime());

        layoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        binding.customerRecycler.setLayoutManager(layoutManager);
        if (Constants.CustomerList!=null){
            binding.totalCustomer.setText("Total - "+Constants.CustomerList.size());
            binding.totalCustomer.setVisibility(View.VISIBLE);
            adapter = new CustomerAdapter(Constants.CustomerList);
            binding.customerRecycler.setAdapter(adapter);
        }
        binding.filterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout sortName , sortDateUp , sortDateDown , conn , notcon , intrest , notIntrest;
                TextView done ;
                WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                Dialog dialog = new Dialog(CustomerActivity.this);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.width = width - 30;
                dialog.setContentView(R.layout.dialog_layout);
//                dialog.getWindow().setAttributes(layoutParams);
                // variables
                sortDateDown = dialog.findViewById(R.id.sortDateDown);
                sortName = dialog.findViewById(R.id.sortName);
                sortDateUp = dialog.findViewById(R.id.sortDateUp);
                conn  = dialog.findViewById(R.id.sortConnected);
                notcon = dialog.findViewById(R.id.sortNotconnected);
                intrest = dialog.findViewById(R.id.sortIntrested);
                notIntrest = dialog.findViewById(R.id.sortNotIntrested);
                done = dialog.findViewById(R.id.buttonSubmit);
                final String[] sortFilter = {"" , ""};
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
                dialog.findViewById(R.id.closeButton).setOnClickListener((View c )->{
                    dialog.dismiss();
                });
                // list of sort by
                ArrayList<pair> list = new ArrayList<>();
                list.add(new pair(sortName, false));
                list.add(new pair(sortDateDown, false));
                list.add(new pair(sortDateUp, false));
                // sortFilter
                sortName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isActive){
                               list.get(i).isActive = false;
                               list.get(i).id.setBackgroundResource(R.drawable.fill_shape);
                            }
                        }
                        list.get(0).isActive = true;
                        sortFilter[0] = "name";
                        sortName.setBackgroundResource(R.drawable.fill2);
                    }
                });
                sortDateDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isActive){
                                list.get(i).isActive = false;
                                list.get(i).id.setBackgroundResource(R.drawable.fill_shape);

                            }
                        }
                        list.get(1).isActive = true;
                        sortFilter[0] = "dateUp";
                        sortDateDown.setBackgroundResource(R.drawable.fill2);
                    }
                });
                sortDateUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isActive){
                                list.get(i).isActive = false;
                                list.get(i).id.setBackgroundResource(R.drawable.fill_shape);

                            }
                        }
                        list.get(2).isActive = true;
                        sortFilter[0] = "dateDown";
                        sortDateUp.setBackgroundResource(R.drawable.fill2);
                    }
                });
                // list of filter
                ArrayList<pair> mList = new ArrayList<>();
                mList.add(new pair(conn, false));
                mList.add(new pair(notcon, false));
                mList.add(new pair(intrest, false));
                mList.add(new pair(notIntrest, false));
                // sort By status
                conn.setOnClickListener((View x)->{
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).isActive){
                            mList.get(i).isActive = false;
                            mList.get(i).id.setBackgroundResource(R.drawable.fill_shape);
                        }
                    }
                    mList.get(0).isActive = true;
                    sortFilter[1] = "connected";
                    conn.setBackgroundResource(R.drawable.fill2);
                });
                notcon.setOnClickListener((View x)->{
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).isActive){
                            mList.get(i).isActive = false;
                            mList.get(i).id.setBackgroundResource(R.drawable.fill_shape);
                        }
                    }
                    mList.get(1).isActive = true;
                    sortFilter[1] = "not connected";
                    notcon.setBackgroundResource(R.drawable.fill2);
                });
                intrest.setOnClickListener((View x)->{
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).isActive){
                            mList.get(i).isActive = false;
                            mList.get(i).id.setBackgroundResource(R.drawable.fill_shape);
                        }
                    }
                    mList.get(2).isActive = true;
                    sortFilter[1] = "1";
                    intrest.setBackgroundResource(R.drawable.fill2);
                });
                notIntrest.setOnClickListener((View x)->{
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).isActive){
                            mList.get(i).isActive = false;
                            mList.get(i).id.setBackgroundResource(R.drawable.fill_shape);
                        }
                    }
                    mList.get(3).isActive = true;
                    sortFilter[1] = "0";
                    notIntrest.setBackgroundResource(R.drawable.fill2);
                });
                done.setOnClickListener((View x)->{
                    dialog.dismiss();
                    startFilter(sortFilter);
                    for (String s : sortFilter) {
                        Log.d("TAG", "onClick: "+s);
                    }
                });
            }
        });
        binding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.CustomerList!=null){
                    binding.clearFilter.setVisibility(View.INVISIBLE);
                    adapter = new CustomerAdapter(Constants.CustomerList);
                    binding.customerRecycler.setAdapter(adapter);
                }
            }
        });
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
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                startDatePicker(initialDate , endDateString);
            }
        };
        DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                startDatePicker(startDateString , endDateString);
                Log.d("TAG", "onDateSet: "+endDateString+" "+startDateString);
            }
        };
        binding.startLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.startLayout.setEnabled(false);
                new DatePickerDialog(CustomerActivity.this,startDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                binding.startLayout.setEnabled(true);
            }
        });
        binding.EndDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.EndDateLayout.setEnabled(false);
                new DatePickerDialog(CustomerActivity.this,endDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                binding.EndDateLayout.setEnabled(true);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startDatePicker(String startDateString, String endDateString) {
        if (startDateString!=null && endDateString !=null){
            DateTimeFormatter f = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
            LocalDate start = LocalDate.parse( startDateString , f );
            LocalDate end = LocalDate.parse( endDateString , f );
            List<contacts> mList = new ArrayList<>();
            for (int i = 0; i < Constants.CustomerList.size(); i++) {
                String[] startDate = Constants.CustomerList.get(i).created_at.split("T");
                String[] endDate = Constants.CustomerList.get(i).updated_at.split("T");
                LocalDate obj1 = LocalDate.parse( startDate[0] , f );
                LocalDate obj2 = LocalDate.parse( endDate[0] , f );
                if ((start.equals(obj1) || start.isBefore(obj1)) && end.isAfter(obj2) || end.equals(obj2) ){
                    mList.add(Constants.CustomerList.get(i));
                    Log.d(TAG, "startDatePicker: on"+obj2+" "+end);
                }
            }
            binding.clearFilter.setVisibility(View.VISIBLE);
            adapter = new CustomerAdapter(mList);
            binding.customerRecycler.setAdapter(adapter);
        }
    }

    private void startFilter(String[] sortFilter) {
        ArrayList<Object> list = new ArrayList<>();
        list.add(new SortByDate());
        list.add(new SortByName());

        if (Constants.CustomerList!=null){
            List<contacts> newList = new ArrayList<>();
            if (sortFilter[0].equals("name")){
                Collections.sort(Constants.CustomerList , new SortByName());
                newList.addAll(Constants.CustomerList);
            }else if (sortFilter[0].equals("dateUp")){
                Constants.CustomerList.sort(new SortByDate());
                newList.addAll(Constants.CustomerList);
            }else if (sortFilter[0].equals("dateDown")){
                Constants.CustomerList.sort(new SortByDate2());
                newList.addAll(Constants.CustomerList);

            }
           if (!(sortFilter[1].equals(""))){
               if (sortFilter[1].equals("1") || sortFilter[1].equals("0")){
                   Log.d("TAG", "startFilter: 1/2");
                   for (int i = 0; i < Constants.CustomerList.size(); i++) {
                       if (Constants.CustomerList.get(i).getInterested() !=null && Constants.CustomerList.get(i).getInterested().toString().equals(sortFilter[1])){
                           newList.add(Constants.CustomerList.get(i));
                       }
                   }
               }else {
                   for (int i = 0; i < Constants.CustomerList.size(); i++) {
                       if (Constants.CustomerList.get(i).getCall_status() !=null && Constants.CustomerList.get(i).getCall_status().toString().equals(sortFilter[1])){
                           newList.add(Constants.CustomerList.get(i));
                       }

                   }
               }
           }else if (sortFilter[0].equals("")){
               return;
           }
           ArrayList<contacts> adapterLister = new ArrayList<>();
            for (contacts c: newList) {
                if (!adapterLister.contains(c)){
                    binding.clearFilter.setVisibility(View.VISIBLE);
                    adapterLister.add(c);
                }
            }
           adapter = new CustomerAdapter(adapterLister);
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   binding.customerRecycler.setAdapter(adapter);
               }
           });
        }
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
                      Constants.CustomerList = contactList;
                      adapter = new CustomerAdapter(contactList);
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             binding.swipe.setRefreshing(false);
                             binding.totalCustomer.setText("Total - "+contactList.size());
                             binding.totalCustomer.setVisibility(View.VISIBLE);
                             binding.customerRecycler.setAdapter(adapter);
                         }
                     });

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }

              }
            });
    }
    private void getWhatsapp() {
        Gson gson = new Gson();
        userDatabaseHelper db = new userDatabaseHelper(getApplicationContext());
        userDatabaseModel model = db.getUser(0);
        if (model.getAuth()==null){
            return;
        }
        Request request = new Request.Builder().url(Constants.baseUrlbackend +"wp_templates").addHeader("authorization" , "Bearer "+model.getAuth()).get().build();
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
                                try {
                                    JSONObject jsonResponse = new JSONObject(response.body().string());
                                    Type type = new TypeToken<List<TemplateModel>>(){}.getType();
                                    Constants.whatsAppTemplates = gson.fromJson(jsonResponse.optString("whatsapp_templates").toString(),type);
                                    Log.d("TAG", "onResponse: "+Constants.whatsAppTemplates.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
    }
    private void getEmails() {
        Gson gson = new Gson();
        userDatabaseHelper db = new userDatabaseHelper(getApplicationContext());
        userDatabaseModel model = db.getUser(0);
        if (model.getAuth()==null){
            return;
        }
        Request request = new Request.Builder().url(Constants.baseUrlbackend +"email_templates").addHeader("authorization" , "Bearer "+model.getAuth()).get().build();
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
                                try {
                                    JSONObject jsonResponse = new JSONObject(response.body().string());
                                    Type type = new TypeToken<List<TemplateModel>>(){}.getType();
                                    Constants.EmailTemplates = gson.fromJson(jsonResponse.optString("email_templates").toString(),type);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
    }
}