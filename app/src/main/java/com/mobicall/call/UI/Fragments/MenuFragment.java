package com.mobicall.call.UI.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobicall.call.R;
import com.mobicall.call.UI.MenuActivity;
import com.mobicall.call.UI.TemplateList;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.databinding.FragmentMenuBinding;
import com.mobicall.call.models.TemplateModel;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentMenuBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater ,container ,false);
        getWhatsapp();
        getEmails();
        binding.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , TemplateList.class);
                intent.putExtra("uriName" , "email");
                startActivity(intent);
            }
        });
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , TemplateList.class);
                intent.putExtra("uriName" , "wp");
                startActivity(intent);
            }
        });
        binding.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , TemplateList.class);
                intent.putExtra("uriName" , "sms");
                startActivity(intent);
            }
        });
        binding.helpFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDatabaseHelper db = new userDatabaseHelper(getContext());
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

            }
        });
        binding.whatsapp.setEnabled(false);
        binding.sms.setEnabled(false);
        binding.email.setEnabled(false);

        return binding.getRoot();
    }
    private void getWhatsapp() {
        Gson gson = new Gson();
        userDatabaseHelper db = new userDatabaseHelper(getContext());
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
                                    Handler mHandler = new Handler(Looper.getMainLooper());
                                    mHandler.post(new Runnable() {
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
        userDatabaseHelper db = new userDatabaseHelper(getContext());
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
                                    Handler mHandler = new Handler(Looper.getMainLooper());
                                    mHandler.post(new Runnable() {
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

}