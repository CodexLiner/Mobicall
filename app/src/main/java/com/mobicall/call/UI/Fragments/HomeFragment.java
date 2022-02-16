package com.mobicall.call.UI.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobicall.call.R;
import com.mobicall.call.UI.CustomerActivity;
import com.mobicall.call.UI.HomeActivity;
import com.mobicall.call.UI.MainActivity;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.databinding.FragmentHomeBinding;
import com.mobicall.call.models.contacts;
import com.mobicall.call.models.totalCount;
import com.mobicall.call.others.staticFunctions;
import com.mobicall.call.services.DrawWindow;
import com.mobicall.call.services.ForegroundService;
import com.mobicall.call.services.PermisionClass;
import com.mobicall.call.services.callTask;
import com.mobicall.call.stateManager.CallState;
import com.mobicall.call.stateManager.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TAG";
    FragmentHomeBinding binding;
    Calendar myCalendar =Calendar.getInstance();
    SimpleDateFormat simpleDateFormat ;
    boolean swiping = false;
    static String startDateString = null;
    static String endDateString = null;
    static String initialDate = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater ,container , false);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd" , Locale.US);;
        startDateString = simpleDateFormat.format(Calendar.getInstance().getTime());
        initialDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        getContext().startService(new Intent(getContext() , CallState.class));
        checkOverlayPermission(inflater , container.getContext());
        PermissionCheck();
        startService();
        updateLabel();
        verifyUser(getActivity());
        getTotalCounts(null , null);
        setCurrentUser(getContext());
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
                new DatePickerDialog(getActivity(),startDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                binding.startLayout.setEnabled(true);
            }
        });
        binding.EndDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.EndDateLayout.setEnabled(false);
                new DatePickerDialog(getActivity(),endDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                binding.EndDateLayout.setEnabled(true);
            }
        });
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
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

//        binding.totalIntraction.setOnClickListener((View v)->{
//            Intent intent = new Intent(this , CustomerActivity.class);
//            intent.putExtra("vargs" , "");
//            startActivity(intent);
//            overridePendingTransition(0,0);
//        });
//        binding.conn.setOnClickListener((View v)->{
//            Intent intent = new Intent(this , CustomerActivity.class);
//            intent.putExtra("vargs" , "connected");
//            startActivity(intent);
//            overridePendingTransition(0,0);
//        });
//        binding.notconn.setOnClickListener((View v)->{
//            Intent intent = new Intent(this , CustomerActivity.class);
//            intent.putExtra("vargs" , "not connected");
//            startActivity(intent);
//            overridePendingTransition(0,0);
//        });
//        binding.inte.setOnClickListener((View v)->{
//            Intent intent = new Intent(this , CustomerActivity.class);
//            intent.putExtra("vargs" , "1");
//            startActivity(intent);
//            overridePendingTransition(0,0);
//        });
//        binding.mLayout2.setOnClickListener((View v)->{
//            Intent intent = new Intent(this , CustomerActivity.class);
//            intent.putExtra("vargs" , "0");
//            startActivity(intent);
//            overridePendingTransition(0,0);
//        });
        return binding.getRoot();
    }

    private void getTotalCounts(String start , String end) {
        userDatabaseHelper db = new userDatabaseHelper(getContext());
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
                                    Handler mHandler = new Handler(Looper.getMainLooper());
                                    mHandler.post(new Runnable() {
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
                                    Log.d(TAG, "onResponse:e "+e.getLocalizedMessage());
                                }

                            }
                        });
    }
    private void verifyUser(Context context) {
        userDatabaseHelper db = new userDatabaseHelper(context);
        userDatabaseModel user = db.getUser(0);
        if (user!=null){
            Log.d("TAG", "verifyUser: "+user.toString());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getContact(LayoutInflater inflater, Context context){
        Gson gson = new Gson();
        userDatabaseHelper db = new userDatabaseHelper(getContext());
        userDatabaseModel model = db.getUser(0);
        if (model.getAuth()==null){
            return;
        }
        staticFunctions.compare(getContext());
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
                                    Handler mHandler = new Handler(Looper.getMainLooper());
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast toast = new Toast(context);
                                            View view = inflater.inflate(R.layout.toast_layout , binding.getRoot());
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
                                            startCallservice(contactList , context);
                                        }
                                    },1000);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
    }
    private void startCallservice(List<contacts> contactList, Context context) {
        DrawWindow.setInWindow(contactList);
        Constants.indexValue = 0;
        Constants.windowContact = contactList;
        callTask callTask = new callTask(contactList, 0 , context);
        callTask.execute();
    }
    private void logOut() {
        userDatabaseHelper db = new userDatabaseHelper(getContext());
        startActivity(new Intent(getContext() , MainActivity.class));
        getActivity().finishAffinity();
        db.delete(0);
    }

    private void updateLabel(){
        String myFormat="dd MMM yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        binding.StartDate.setText(dateFormat.format(myCalendar.getTime()));
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

    public void checkOverlayPermission(LayoutInflater inflater, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Overlay Permission Required ");
        builder.setMessage("to turn on click okay to open settings and enable overlay permission");
        if (!Settings.canDrawOverlays(getContext())) {
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
            getContact(inflater , context);
        }
    }
    public void startService(){
        // check if the user has already granted
        // the Draw over other apps permission

        if(Settings.canDrawOverlays(getContext())) {
            // start the service based on the android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getContext().startForegroundService(new Intent(getContext(), ForegroundService.class));
            } else {
                getContext().startService(new Intent(getContext(), ForegroundService.class));
            }
        }
    }

    private void PermissionCheck() {
        if (PermisionClass.hasPermision(getContext() , PermisionClass.permisions)){
//            button2.setText("Enable Notification");
        }else{
            if (!PermisionClass.hasPermision(getContext() , PermisionClass.permisions)){
                ActivityCompat.requestPermissions(getActivity(), PermisionClass.permisions, 0);
            }
        }
    }

}