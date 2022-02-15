package com.mobicall.call.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobicall.call.R;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.databinding.ActivityMenuBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.mobicall.call.models.TemplateModel;
import com.mobicall.call.models.contacts;
import com.mobicall.call.stateManager.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity {
    ActivityMenuBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWhatsapp();
        getEmails();
        binding.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , TemplateList.class);
                intent.putExtra("uriName" , "email");
                startActivity(intent);
            }
        });
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , TemplateList.class);
                intent.putExtra("uriName" , "wp");
                startActivity(intent);
            }
        });
        binding.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , TemplateList.class);
                intent.putExtra("uriName" , "sms");
                startActivity(intent);
            }
        });
        binding.helpFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDatabaseHelper db = new userDatabaseHelper(getApplicationContext());
                userDatabaseModel model = db.getUser(0);
                if (model.getAuth()==null){
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ model.getcMail() , "support@mobicall.live"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback For "+model.getcName());
                intent.putExtra(Intent.EXTRA_CC, new String[]{ "support@mobicall.live"});
                intent.putExtra(Intent.EXTRA_TEXT, "Write Your Complaint or Feedback Here");
                startActivity(intent);
            }
        });
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, "Failed to Share", Toast.LENGTH_SHORT).show();
            }
        });
        binding.whatsapp.setEnabled(false);
        binding.sms.setEnabled(false);
        binding.email.setEnabled(false);
        binding.bottomNav.setSelectedItemId(R.id.menuMore);
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
                    case R.id.customerList:
                    {
                        startActivity(new Intent(getApplicationContext() , CustomerActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    }
                }
                return true;
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
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           binding.whatsapp.setEnabled(true);
                                           binding.sms.setEnabled(true);
                                       }
                                   });
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
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.email.setEnabled(true);
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);
    }
}